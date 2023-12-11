package com.huii.framework.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.huii.common.annotation.DataScope;
import com.huii.common.core.domain.SysRole;
import com.huii.common.core.domain.SysUser;
import com.huii.common.enums.DataScopeType;
import com.huii.common.exception.BasicAuthenticationException;
import com.huii.common.utils.SecurityUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MybatisDataPermissionHandler {

    private final Map<String, DataScope> dataPermissionCacheMap = new ConcurrentHashMap<>();
    private final Set<String> invalidCacheSet = new ConcurrentHashSet<>();

    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        DataScope dataScope = findAnnotation(mappedStatementId);
        if (ArrayUtil.isEmpty(dataScope)) {
            invalidCacheSet.add(mappedStatementId);
            return where;
        }
        if (SecurityUtils.isAdmin()) {
            return where;
        }
        String filteredSql = buildSql(dataScope);
        if (StringUtils.isBlank(filteredSql)) {
            return where;
        }
        try {
            Expression expression = CCJSqlParserUtil.parseExpression(filteredSql);
            Parenthesis parenthesis = new Parenthesis(expression);
            if (ObjectUtil.isNotNull(where)) {
                return new AndExpression(where, parenthesis);
            } else {
                return parenthesis;
            }
        } catch (JSQLParserException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isInvalid(String mappedStatementId) {
        return invalidCacheSet.contains(mappedStatementId);
    }

    //TODO
    //TO TEST
    private String buildSql(DataScope dataScope) {
        try {
            SysUser user = SecurityUtils.getUser();
            List<String> list = user.getRoles().stream().distinct().map(SysRole::getRoleScope).toList();
            if (list.contains(DataScopeType.ALL.getCode())) {
                return "";
            }
            StringBuilder sqlString = new StringBuilder();
            for (int i = 0; i < user.getRoles().size(); i++) {
                if (i >= 1) {
                    sqlString.append(" OR ");
                }
                user.setRoleId(user.getRoles().get(i).getRoleId());
                String roleScope = user.getRoles().get(i).getRoleScope();
                if (roleScope.equals(DataScopeType.CUSTOM.getCode())) {
                    sqlString.append(String.format(" %s.dept_id IN (SELECT dept_id from sys_role_dept WHERE role_id = %s )",
                            dataScope.deptAlias(), user.getRoleId()));
                } else if (roleScope.equals(DataScopeType.DEPT.getCode())) {
                    sqlString.append(String.format(" %s.dept_id = %s ",
                            dataScope.deptAlias(), user.getDeptId()));
                } else if (roleScope.equals(DataScopeType.DEPT_AND_CHILD.getCode())) {
                    //pgsql
                    sqlString.append(String.format(
                            "%s.dept_id in (SELECT dept_id FROM sys_dept" +
                                    "WHERE dept_id IN (" +
                                    "    WITH RECURSIVE DeptHierarchy AS (" +
                                    "        SELECT dept_id, parent_id" +
                                    "        FROM sys_dept" +
                                    "        WHERE dept_id = %s " +
                                    "        UNION" +
                                    "        SELECT d.dept_id, d.parent_id" +
                                    "        FROM sys_dept d" +
                                    "        JOIN DeptHierarchy h ON d.parent_id = h.dept_id" +
                                    "    )" +
                                    "    SELECT dept_id FROM DeptHierarchy" +
                                    ")" +
                                    ")",
                            dataScope.deptAlias(), user.getDeptId()));
                } else if (roleScope.equals(DataScopeType.SELF.getCode())) {
                    sqlString.append(String.format(" %s.user_id = %s ",
                            dataScope.userAlias(), user.getUserId()));
                }
            }
            if (StringUtils.isNotBlank(sqlString)) {
                return sqlString.toString();
            } else {
                return "";
            }
        } catch (BasicAuthenticationException ignored) {
        }
        return "";
    }

    @SneakyThrows
    private DataScope findAnnotation(String mappedStatementId) {
        DataScope dataScope = dataPermissionCacheMap.get(mappedStatementId);
        if (ObjectUtil.isNotNull(dataScope)) {
            return dataScope;
        }
        StringBuilder sb = new StringBuilder(mappedStatementId);
        int index = sb.lastIndexOf(".");
        String clazzName = sb.substring(0, index);
        String methodName = sb.substring(index + 1, sb.length());
        Class<?> clazz = ClassUtil.loadClass(clazzName);
        List<Method> methods = Arrays.stream(ClassUtil.getDeclaredMethods(clazz))
                .filter(m -> m.getName().equals(methodName)).toList();
        for (Method method : methods) {
            if (AnnotationUtil.hasAnnotation(method, DataScope.class)) {
                DataScope scope = method.getAnnotation(DataScope.class);
                dataPermissionCacheMap.put(mappedStatementId, scope);
                return scope;
            }
        }
        return null;
    }
}