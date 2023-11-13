package com.huii.common.utils.request;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.File;

/**
 * ip2region 离线查询IP地址
 *
 * @author huii
 * @link <a href="https://github.com/lionsoul2014/ip2region/tree/master/data">ip2region</a>
 */
@Slf4j
public class RegionUtils {
    private static final String path = "/ip2region.xdb";
    private static Searcher SEARCHER;

    @PostConstruct
    private void init() {
        File existFile = FileUtil.file(FileUtil.getTmpDir() + FileUtil.FILE_SEPARATOR + path);
        if (!FileUtil.exist(existFile)) {
            ClassPathResource fileStream = new ClassPathResource(path);
            if (ObjectUtil.isEmpty(fileStream.getStream())) {
                throw new RuntimeException("ip2region init fail, reason for: db load fail");
            }
            FileUtil.writeFromStream(fileStream.getStream(), existFile);
        }
        String dbPath = existFile.getPath();
        byte[] cBuff;
        try {
            cBuff = Searcher.loadContentFromFile(dbPath);
        } catch (Exception e) {
            throw new RuntimeException("ip2region init fail, reason for: xdb load fail" + e.getMessage());
        }
        try {
            SEARCHER = Searcher.newWithBuffer(cBuff);
        } catch (Exception e) {
            throw new RuntimeException("ip2region init fail, reason for: " + e.getMessage());
        }
        log.debug("ip2region load success");
    }

    public static String query(String ip) {
        try {
            String region = SEARCHER.search(ip.trim());
            return region.replace("0|", "").replace("|0", "");
        } catch (Exception e) {
            log.error("ip2region query offline ip fail: {}", ip);
            return null;
        }
    }
}
