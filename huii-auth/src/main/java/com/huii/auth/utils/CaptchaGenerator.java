package com.huii.auth.utils;

import com.huii.auth.core.entity.Captcha;
import com.huii.auth.core.entity.RectangleDto;
import com.huii.common.constants.CaptchaConstants;
import com.huii.common.enums.ResType;
import com.huii.common.exception.ServiceException;
import com.huii.common.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 图片验证码生成工具
 *
 * @author huii
 */
@Slf4j
public class CaptchaGenerator {

    private final static String IMG_PATH = "C:/Temp/wallpaper/%s.jpg";
    private final static String IMG_URL = "https://loyer.wang/view/ftp/wallpaper/%s.jpg";

    private static final List<Character> letters = new ArrayList<>();

    static {
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            letters.add(ch);
        }
    }

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

    public static int createRandom(Random random, int start, int end) {
        return random.nextInt(end - start + 1) + start;
    }

    private static int createRandom(Random random, int start, int end, int gap) {
        return random.nextInt((end - start - gap) / gap + 1) * gap + start;
    }


    public static String createUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成滑动验证码
     */
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

    /**
     * 生成点击验证码
     */
    public static Captcha createClickTextCaptcha(Captcha captcha) {
        checkCaptcha(captcha);
        int canvasWidth = captcha.getCanvasWidth();
        int canvasHeight = captcha.getCanvasHeight();

        BufferedImage canvasImage = getBufferedImage(captcha.getPlace());
        Set<Rectangle> occupiedAreas = new LinkedHashSet<>();
        String text = textBuilder();

        canvasImage = clickImgBuilder(canvasImage, String.valueOf(text), occupiedAreas, canvasWidth, canvasHeight);

        String nonceStr = UUID.randomUUID().toString().replace("-", "");
        captcha.setNonceStr(nonceStr);
        captcha.setClickText("请依次点击" + text.substring(0, CaptchaConstants.CAPTCHA_CLICK_TEXT_GEN_EFFECT * 2 - 1));
        captcha.setClickSrc(toBase64(canvasImage, "png"));
        captcha.setRectangles(occupiedAreas.stream().map(RectangleDto::new)
                .toArray(RectangleDto[]::new));

        return captcha;
    }

    /**
     * 生成旋转验证码
     */
    public static Captcha createRotateCaptcha(Captcha captcha) {
        checkCaptcha(captcha);
        BufferedImage canvasImage = getBufferedImage(captcha.getPlace());
        canvasImage = imageResizeLimit(canvasImage, 160, 160);

        Random random = new Random();
        int rotationAngle = random.nextInt(90) + 180;
        canvasImage = rotateImage(canvasImage, rotationAngle);

        canvasImage = cropToCircle(canvasImage, 80);

        String nonceStr = UUID.randomUUID().toString().replace("-", "");
        captcha.setCanvasHeight(160);
        captcha.setCanvasWidth(160);
        captcha.setNonceStr(nonceStr);
        captcha.setCanvasSrc(toBase64(canvasImage, "png"));
        captcha.setBlockX(rotationAngle);
        return captcha;
    }

    /**
     * 校验 captcha 参数
     */
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

    /**
     * 获取图像
     */
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
            ResType resType = ResType.SYS_CODE_GEN_ERROR;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
    }

    /**
     * 缩放图像
     */
    public static BufferedImage imageResize(BufferedImage bufferedImage, int width, int height) {
        Image image = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resultImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();
        return resultImage;
    }

    /**
     * 缩放图像 按照给定限制宽度、高度
     */
    public static BufferedImage imageResizeLimit(BufferedImage inputImage, int newWidth, int newHeight) {
        int type = inputImage.getColorModel().getTransparency();
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        BufferedImage img = new BufferedImage(newWidth, newHeight, type);
        Graphics2D graphics2d = img.createGraphics();
        graphics2d.setRenderingHints(renderingHints);
        graphics2d.drawImage(inputImage, 0, 0, newWidth, newHeight, 0, 0, width, height, null);
        graphics2d.dispose();
        return img;
    }

    /**
     * 旋转图像
     */
    static BufferedImage rotateImage(BufferedImage image, int randomAngle) {
        BufferedImage rotatedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g2d = rotatedImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(randomAngle), image.getWidth() / 2.0, image.getHeight() / 2.0);
        g2d.setTransform(transform);

        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        return rotatedImage;
    }

    /**
     * 裁剪图像为圆形
     */
    private static BufferedImage cropToCircle(BufferedImage originalImage, int radius) {
        BufferedImage circularImage = new BufferedImage(radius * 2, radius * 2, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = circularImage.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Ellipse2D.Double clip = new Ellipse2D.Double(0, 0, radius * 2, radius * 2);

        g2d.setClip(clip);

        g2d.drawImage(originalImage, 0, 0, radius * 2, radius * 2, null);
        g2d.dispose();

        return circularImage;
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
        int face1 = ThreadLocalRandom.current().nextInt(0, 4);
        int face2;
        do {
            face2 = ThreadLocalRandom.current().nextInt(0, 4);
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

    private static String textBuilder() {
        Collections.shuffle(letters);
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < CaptchaConstants.CAPTCHA_CLICK_TEXT_GEN_ALL; i++) {
            text.append(letters.get(i)).append(",");
        }
        return String.valueOf(text);
    }

    private static Font createRandomFont(Random random) {
        int fontSize = random.nextInt(20) + 26;
        int fontWeight = random.nextBoolean() ? Font.BOLD : Font.PLAIN;
        return new Font("Arial", fontWeight, fontSize);
    }

    private static boolean isOverlapping(Rectangle newArea, Set<Rectangle> occupiedAreas) {
        for (Rectangle occupiedArea : occupiedAreas) {
            if (newArea.intersects(occupiedArea)) {
                return true;
            }
        }
        return false;
    }

    private static BufferedImage clickImgBuilder(BufferedImage canvasImage, String text, Set<Rectangle> occupiedAreas, int canvasWidth, int canvasHeight) {
        canvasImage = imageResize(canvasImage, canvasWidth, canvasHeight);
        Graphics2D g2d = canvasImage.createGraphics();
        Random random = new Random(System.currentTimeMillis());
        for (String s : text.split(",")) {
            Font font = createRandomFont(random);
            FontRenderContext frc = g2d.getFontRenderContext();
            Rectangle2D textBounds = font.getStringBounds(s, frc);

            int textWidth = (int) textBounds.getWidth();
            int textHeight = (int) textBounds.getHeight();

            int x, y;
            Rectangle textArea;
            do {
                x = createRandom(random, textWidth + 26, canvasWidth - 26, 48);
                y = createRandom(random, textHeight + 18, canvasHeight - 18, 36);
                textArea = new Rectangle(x - textWidth / 2, y - textHeight / 2, textWidth + 12, textHeight + 12);
            } while (isOverlapping(textArea, occupiedAreas));

            Color textColor = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            int rotationAngle = random.nextInt(270);

            AffineTransform affineTransform = new AffineTransform();
            affineTransform.rotate(Math.toRadians(rotationAngle), x, y);

            g2d.setTransform(affineTransform);
            g2d.setColor(textColor);
            g2d.setFont(font);

            g2d.drawString(s, x - textWidth / 2, y - textHeight / 2);
            g2d.setTransform(new AffineTransform());

            occupiedAreas.add(textArea);
        }

        g2d.dispose();
        return canvasImage;
    }

    public static String toBase64(BufferedImage bufferedImage, String type) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, type, byteArrayOutputStream);
            String base64 = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
            return String.format("data:image/%s;base64,%s", type, base64);
        } catch (IOException e) {
            ResType resType = ResType.SYS_CODE_GEN_ERROR;
            throw new ServiceException(resType.getCode(), MessageUtils.message(resType.getI18n()));
        }
    }
}