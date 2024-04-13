package com.huii.oss.utils;

import com.huii.oss.entity.MultipartFileDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * 定义文字加水印
 *
 * @author huii
 */
@Slf4j
public class ImageWatermarkUtils {

    /**
     * 水印字体
     */
    private static final Font FONT = new Font("微软雅黑", Font.PLAIN, 15);

    /**
     * 透明度
     */
    private static final AlphaComposite COMPOSITE = AlphaComposite
            .getInstance(AlphaComposite.SRC_OVER, 0.84f);

    /**
     * 水印之间的间隔
     */
    private static final int X_MOVE = 120;

    /**
     * 水印之间的间隔
     */
    private static final int Y_MOVE = 100;

    /**
     * 打水印(文字)
     *
     * @param file MultipartFile
     * @return MultipartFile
     */
    public static MultipartFile markWithContent(MultipartFile file, String keyword) {
        FileOutputStream fos = null;
        try {
            BufferedImage srcImg = ImageIO.read(file.getInputStream());

            // 图片宽、高
            int imgWidth = srcImg.getWidth();
            int imgHeight = srcImg.getHeight();

            // 图片缓存
            BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);

            // 创建绘图工具
            Graphics2D graphics = bufImg.createGraphics();

            // 画入原始图像
            graphics.drawImage(srcImg, 0, 0, imgWidth, imgHeight, null);

            // 设置水印颜色
            graphics.setColor(Color.lightGray);

            // 设置水印透明度
            graphics.setComposite(COMPOSITE);

            // 设置倾斜角度
            graphics.rotate(Math.toRadians(-35), (double) bufImg.getWidth() / 2,
                    (double) bufImg.getHeight() / 2);

            // 设置水印字体
            graphics.setFont(FONT);

            // 消除java.awt.Font字体的锯齿
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int xCoordinate = -imgWidth / 2, yCoordinate;
            String mark = "huiiro";
            // 字体长度
            int markWidth = FONT.getSize() * getTextLength(mark);
            // 字体高度
            int markHeight = FONT.getSize();

            double odf = 1.5;
            // 循环添加水印
            while (xCoordinate < imgWidth * odf) {
                yCoordinate = -imgHeight / 2;
                while (yCoordinate < imgHeight * odf) {
                    graphics.drawString(keyword, xCoordinate, yCoordinate);
                    yCoordinate += markHeight + Y_MOVE;
                }
                xCoordinate += markWidth + X_MOVE;
            }
            InputStream inputStream = buffToInputStream(bufImg);
            // 释放画图工具
            graphics.dispose();
            return new MultipartFileDto(file.getName(), file.getOriginalFilename(), file.getContentType(), inputStream);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 计算水印文本长度
     * 1、中文长度即文本长度 2、英文长度为文本长度二分之一
     */
    public static int getTextLength(String text) {
        //水印文字长度
        int length = text.length();

        for (int i = 0; i < text.length(); i++) {
            String s = String.valueOf(text.charAt(i));
            if (s.getBytes().length > 1) {
                length++;
            }
        }
        length = length % 2 == 0 ? length / 2 : length / 2 + 1;
        return length;
    }

    public static InputStream buffToInputStream(BufferedImage buffer) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(buffer, "png", os);
        return new ByteArrayInputStream(os.toByteArray());
    }

}