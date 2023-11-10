package com.huii.auth.utils;

import java.util.Random;

/**
 * 计算表达式生成工具
 *
 * @author huii
 */
public class ExpressionGenerator {
    private static final String[] NUMBER = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public static class ExpressionResult {
        public String expression;
        public int result;

        public ExpressionResult(String expression, int result) {
            this.expression = expression;
            this.result = result;
        }
    }

    public static ExpressionResult generateExpression() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int x = random.nextInt(10);
        int y = random.nextInt(10);
        int res;
        int randomOperands = (int) Math.round(Math.random() * 2);

        if (randomOperands == 0) {
            res = x * y;
            sb.append(NUMBER[x]);
            sb.append("x");
            sb.append(NUMBER[y]);
        } else if (randomOperands == 1) {
            if (!(x == 0) && y % x == 0) {
                res = y / x;
                sb.append(NUMBER[y]);
                sb.append("/");
                sb.append(NUMBER[x]);
            } else {
                res = x + y;
                sb.append(NUMBER[x]);
                sb.append("+");
                sb.append(NUMBER[y]);
            }
        } else if (randomOperands == 2) {
            if (x >= y) {
                res = x - y;
                sb.append(NUMBER[x]);
                sb.append("-");
                sb.append(NUMBER[y]);
            } else {
                res = y - x;
                sb.append(NUMBER[y]);
                sb.append("-");
                sb.append(NUMBER[x]);
            }
        } else {
            res = x + y;
            sb.append(NUMBER[x]);
            sb.append("+");
            sb.append(NUMBER[y]);
        }
        return new ExpressionResult(sb.toString(), res);
    }
}
