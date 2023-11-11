package com.huii.common.utils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * 敏感词处理工具
 *
 * @author huii
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "sensitive.word.enable", havingValue = "true")
@SuppressWarnings({"rawtypes", "unchecked"})
public class SensitiveTextUtils {

    @Value("${sensitive.word.path}")
    private String filePath;

    /**
     * 敏感词匹配规则
     * 最小匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国]人
     * 最大匹配规则，如：敏感词库["中国","中国人"]，语句："我是中国人"，匹配结果：我是[中国人]
     */
    public static final int MinMatchTYpe = 1;
    public static final int MaxMatchType = 2;

    /**
     * 敏感词集合
     */
    public static HashMap sensitiveWordMap;

    /**
     * 初始化敏感词库
     */
    @PostConstruct
    private synchronized void init() {
        String SENSITIVE_WORD_PATH = filePath;
        Set<String> sensitiveWordSet = new HashSet<>();

        try {
            log.info("init sensitiveWords database....{}", SENSITIVE_WORD_PATH);
            File wordFileDir = new File(SENSITIVE_WORD_PATH);
            File[] wordFiles = wordFileDir.listFiles();
            assert wordFiles != null;
            for (File wordFile : wordFiles) {
                log.info("init sensitiveWords file：{}", wordFile.getName());
                BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(wordFile.toPath()), StandardCharsets.UTF_8));
                String line;
                while ((line = reader.readLine()) != null) {
                    sensitiveWordSet.add(line);
                    log.trace("init sensitiveWords line：{}", line);
                }
                reader.close();
            }
        } catch (IOException e) {
            log.error("init sensitiveWords fail....");
        }
        log.info("init sensitiveWords complete，count: {}", sensitiveWordSet.size());
        initSensitiveWordMap(sensitiveWordSet);
    }

    /**
     * 初始化敏感词库
     */
    private static void initSensitiveWordMap(Set<String> sensitiveWordSet) {
        sensitiveWordMap = new HashMap(sensitiveWordSet.size());
        String key;
        Map nowMap;
        Map<String, String> newWorMap;
        for (String s : sensitiveWordSet) {
            key = s;
            nowMap = sensitiveWordMap;
            for (int i = 0; i < key.length(); i++) {
                char keyChar = key.charAt(i);
                Object wordMap = nowMap.get(keyChar);
                if (wordMap != null) {
                    nowMap = (Map) wordMap;
                } else {
                    newWorMap = new HashMap<>();
                    newWorMap.put("isEnd", "0");
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
                if (i == key.length() - 1) {
                    nowMap.put("isEnd", "1");
                }
            }
        }
    }

    /**
     * 判断文字是否包含敏感字符
     */
    public static boolean contains(String txt, int matchType) {
        boolean flag = false;
        for (int i = 0; i < txt.length(); i++) {
            int matchFlag = checkSensitiveWord(txt, i, matchType);
            if (matchFlag > 0) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 判断文字是否包含敏感字符
     */
    public static boolean contains(String txt) {
        return contains(txt, MaxMatchType);
    }

    /**
     * 获取文字中的敏感词
     */
    public static Set<String> getSensitiveWord(String txt, int matchType) {
        Set<String> sensitiveWordList = new HashSet<>();

        for (int i = 0; i < txt.length(); i++) {
            int length = checkSensitiveWord(txt, i, matchType);
            if (length > 0) {
                sensitiveWordList.add(txt.substring(i, i + length));
                i = i + length - 1;
            }
        }
        return sensitiveWordList;
    }

    /**
     * 获取文字中的敏感词
     */
    public static Set<String> getSensitiveWord(String txt) {
        return getSensitiveWord(txt, MaxMatchType);
    }

    /**
     * 替换敏感字字符
     */
    public static String replaceSensitiveWord(String txt, char replaceChar, int matchType) {
        String resultTxt = txt;
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        String replaceString;
        while (iterator.hasNext()) {
            word = iterator.next();
            replaceString = getReplaceChars(replaceChar, word.length());
            resultTxt = resultTxt.replaceAll(word, replaceString);
        }

        return resultTxt;
    }

    /**
     * 替换敏感字字符
     */
    public static String replaceSensitiveWord(String txt, char replaceChar) {
        return replaceSensitiveWord(txt, replaceChar, MaxMatchType);
    }

    /**
     * 替换敏感字字符
     */
    public static String replaceSensitiveWord(String txt, String replaceStr, int matchType) {
        String resultTxt = txt;
        Set<String> set = getSensitiveWord(txt, matchType);
        Iterator<String> iterator = set.iterator();
        String word;
        while (iterator.hasNext()) {
            word = iterator.next();
            resultTxt = resultTxt.replaceAll(word, replaceStr);
        }

        return resultTxt;
    }

    /**
     * 替换敏感字字符
     */
    public static String replaceSensitiveWord(String txt, String replaceStr) {
        return replaceSensitiveWord(txt, replaceStr, MaxMatchType);
    }

    /**
     * 获取替换字符串
     */
    private static String getReplaceChars(char replaceChar, int length) {
        return replaceChar + String.valueOf(replaceChar).repeat(Math.max(0, length - 1));
    }

    /**
     * 检查文字中是否包含敏感字符
     */
    private static int checkSensitiveWord(String txt, int beginIndex, int matchType) {
        boolean flag = false;
        int matchFlag = 0;
        char word;
        Map nowMap = sensitiveWordMap;
        for (int i = beginIndex; i < txt.length(); i++) {
            word = txt.charAt(i);
            nowMap = (Map) nowMap.get(word);
            if (nowMap != null) {
                matchFlag++;
                if ("1".equals(nowMap.get("isEnd"))) {
                    flag = true;
                    if (MinMatchTYpe == matchType) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
        if (matchFlag < 2 || !flag) {
            matchFlag = 0;
        }
        return matchFlag;
    }
}

