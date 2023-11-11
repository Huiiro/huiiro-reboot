package com.huii.common.utils;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * json书写工具
 *
 * @author huii
 */
@Slf4j
@Component
public class JsonWriteUtils {

    public static void writePrintJson(HttpServletResponse response, int status, Object obj) {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=UTF-8");
            if (ObjectUtils.isNotEmpty(status)) {
                response.setStatus(status);
            }
            PrintWriter out = response.getWriter();
            out.write(JSON.toJSONString(obj));
            out.flush();
            out.close();
        } catch (IOException e) {
            log.error("print json error: ", e);
        }
    }

    public static void writeOptJson(HttpServletResponse response, int status, Object obj) {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json;charset=UTF-8");
            if (ObjectUtils.isNotEmpty(status)) {
                response.setStatus(status);
            }
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(JSON.toJSONString(obj).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            log.error("opt json error: ", e);
        }
    }
}
