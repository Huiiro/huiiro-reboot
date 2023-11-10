package com.huii.auth.utils;

import com.huii.auth.core.entity.Captcha;
import com.huii.common.exception.ServiceException;
import org.apache.commons.lang3.RandomUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * 图片验证码生成工具
 *
 * @author huii
 */
public class CaptchaGenerator {

    private final static String IMG_PATH = "C:/Temp/wallpaper/%s.jpg";
    private final static String IMG_URL = "https://loyer.wang/view/ftp/wallpaper/%s.jpg";

    public static String createRandom(int length) {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < length; i++) {
            code.append(random.nextInt(10));
        }

        return code.toString();
    }

    public static int createRandom(int start, int end) {
        Random random = new Random(System.currentTimeMillis());
        return random.nextInt(end - start + 1) + start;
    }

    public static String createUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static Captcha createImgSlideCaptcha(Captcha captcha) {
        checkCaptcha(captcha);
        int canvasWidth = captcha.getCanvasWidth();
        int canvasHeight = captcha.getCanvasHeight();
        int blockWidth = captcha.getBlockWidth();
        int blockHeight = captcha.getBlockHeight();
        int blockRadius = captcha.getBlockRadius();

        BufferedImage canvasImage = getBufferedImage(captcha.getPlace());
        canvasImage = imageResize(canvasImage, canvasWidth, canvasHeight);

        int blockX = createRandom(blockWidth, canvasWidth - blockWidth - 10);
        int blockY = createRandom(10, canvasHeight - blockHeight + 1);
        BufferedImage blockImage = new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_4BYTE_ABGR);
        cutByTemplate(canvasImage, blockImage, blockWidth, blockHeight, blockRadius, blockX, blockY);

        String nonceStr = UUID.randomUUID().toString().replace("-", "");

        captcha.setNonceStr(nonceStr);
        captcha.setBlockY(blockY);
        captcha.setBlockX(blockX);
        captcha.setBlockSrc(toBase64(blockImage, "png"));
        captcha.setCanvasSrc(toBase64(canvasImage, "png"));

        return captcha;
    }

    public static void checkCaptcha(Captcha captcha) {
        if (captcha.getCanvasWidth() == null) {
            captcha.setCanvasWidth(320);
        }
        if (captcha.getCanvasHeight() == null) {
            captcha.setCanvasHeight(155);
        }
        if (captcha.getBlockWidth() == null) {
            captcha.setBlockWidth(65);
        }
        if (captcha.getBlockHeight() == null) {
            captcha.setBlockHeight(55);
        }
        if (captcha.getBlockRadius() == null) {
            captcha.setBlockRadius(9);
        }
        if (captcha.getPlace() == null) {
            captcha.setPlace(0);
        }
    }

    public static BufferedImage getBufferedImage(Integer place) {
        try {
            int nonce = createRandom(0, 1000);
            if (0 == place) {
                String imgUrl = String.format(IMG_URL, nonce);
                URL url = new URL(imgUrl);
                return ImageIO.read(url.openStream());
            } else {
                String imgPath = String.format(IMG_PATH, nonce);
                File file = new File(imgPath);
                return ImageIO.read(file);
            }
        } catch (Exception e) {
            throw new ServiceException("资源获取失败");
        }
    }

    public static BufferedImage imageResize(BufferedImage bufferedImage, int width, int height) {
        Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resultImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();
        return resultImage;
    }

    public static void cutByTemplate(BufferedImage canvasImage, BufferedImage blockImage, int blockWidth, int blockHeight, int blockRadius, int blockX, int blockY) {
        BufferedImage waterImage = new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_4BYTE_ABGR);
        int[][] blockData = getBlockData(blockWidth, blockHeight, blockRadius);
        for (int i = 0; i < blockWidth; i++) {
            for (int j = 0; j < blockHeight; j++) {
                try {
                    if (blockData[i][j] == 1) {
                        waterImage.setRGB(i, j, Color.BLACK.getRGB());
                        blockImage.setRGB(i, j, canvasImage.getRGB(blockX + i, blockY + j));
                        if (blockData[i + 1][j] == 0 || blockData[i][j + 1] == 0 || blockData[i - 1][j] == 0 || blockData[i][j - 1] == 0) {
                            blockImage.setRGB(i, j, Color.WHITE.getRGB());
                            waterImage.setRGB(i, j, Color.WHITE.getRGB());
                        }
                    } else {
                        blockImage.setRGB(i, j, Color.TRANSLUCENT);
                        waterImage.setRGB(i, j, Color.TRANSLUCENT);
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }
        addBlockWatermark(canvasImage, waterImage, blockX, blockY);
    }

    private static int[][] getBlockData(int blockWidth, int blockHeight, int blockRadius) {
        int[][] data = new int[blockWidth][blockHeight];
        double po = Math.pow(blockRadius, 2);
        int face1 = RandomUtils.nextInt(0, 4);
        int face2;
        do {
            face2 = RandomUtils.nextInt(0, 4);
        } while (face1 == face2);
        int[] circle1 = getCircleCoords(face1, blockWidth, blockHeight, blockRadius);
        int[] circle2 = getCircleCoords(face2, blockWidth, blockHeight, blockRadius);
        int shape = createRandom(0, 1);
        for (int i = 0; i < blockWidth; i++) {
            for (int j = 0; j < blockHeight; j++) {
                data[i][j] = 0;
                if ((i >= blockRadius && i <= blockWidth - blockRadius && j >= blockRadius && j <= blockHeight - blockRadius)) {
                    data[i][j] = 1;
                }
                double d1 = Math.pow(i - Objects.requireNonNull(circle1)[0], 2) + Math.pow(j - circle1[1], 2);
                double d2 = Math.pow(i - Objects.requireNonNull(circle2)[0], 2) + Math.pow(j - circle2[1], 2);
                if (d1 <= po || d2 <= po) {
                    data[i][j] = shape;
                }
            }
        }
        return data;
    }

    private static int[] getCircleCoords(int face, int blockWidth, int blockHeight, int blockRadius) {
        if (0 == face) {
            return new int[]{blockWidth / 2 - 1, blockRadius};
        } else if (1 == face) {
            return new int[]{blockRadius, blockHeight / 2 - 1};
        } else if (2 == face) {
            return new int[]{blockWidth / 2 - 1, blockHeight - blockRadius - 1};
        } else if (3 == face) {
            return new int[]{blockWidth - blockRadius - 1, blockHeight / 2 - 1};
        }
        return null;
    }

    private static void addBlockWatermark(BufferedImage canvasImage, BufferedImage blockImage, int x, int y) {
        Graphics2D graphics2D = canvasImage.createGraphics();
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.8f));
        graphics2D.drawImage(blockImage, x, y, null);
        graphics2D.dispose();
    }

    public static String toBase64(BufferedImage bufferedImage, String type) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, type, byteArrayOutputStream);
            String base64 = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
            return String.format("data:image/%s;base64,%s", type, base64);
        } catch (IOException e) {
            throw new ServiceException("图片获取失败，转换异常");
        }
    }
}