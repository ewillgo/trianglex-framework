package org.trianglex.common.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class CaptchaRender {

    private static final Logger logger = LoggerFactory.getLogger(CaptchaRender.class);
    private static final Random random = new Random(System.nanoTime());

    private static final int DEFAULT_WIDTH = 108;
    private static final int DEFAULT_HEIGHT = 40;
    private int width;
    private int height;
    private String captcha;

    private static final char[] CHAR_ARRAY = "3456789abcdefghjkmnpqrstuvwxyABCDEFGHJKMNPQRSTUVWXY".toCharArray();
    private static final Font[] RANDOM_FONT = new Font[]{
            new Font(Font.DIALOG, Font.BOLD, 33),
            new Font(Font.DIALOG_INPUT, Font.BOLD, 34),
            new Font(Font.SERIF, Font.BOLD, 33),
            new Font(Font.SANS_SERIF, Font.BOLD, 34),
    };

    public CaptchaRender(int size) {
        this(size, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public CaptchaRender(int size, int width, int height) {
        this.width = width;
        this.height = height;
        this.captcha = getRandomString(size);
    }

    public String getCaptcha() {
        return captcha;
    }

    public void render(HttpServletResponse response) {

        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        ServletOutputStream sos = null;
        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            drawGraphic(image);

            sos = response.getOutputStream();
            ImageIO.write(image, "jpeg", sos);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (sos != null) {
                try {
                    sos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private String getRandomString(int size) {
        char[] randomChars = new char[size];
        for (int i = 0; i < randomChars.length; i++) {
            randomChars[i] = CHAR_ARRAY[random.nextInt(CHAR_ARRAY.length)];
        }
        return String.valueOf(randomChars);
    }

    private void drawGraphic(BufferedImage image) {

        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setColor(getRandColor(210, 250));
        g.fillRect(0, 0, width, height);

        Color color = null;
        for (int i = 0; i < 20; i++) {
            color = getRandColor(120, 200);
            g.setColor(color);
            String rand = String.valueOf(CHAR_ARRAY[random.nextInt(CHAR_ARRAY.length)]);
            g.drawString(rand, random.nextInt(width), random.nextInt(height));
            color = null;
        }

        g.setFont(RANDOM_FONT[random.nextInt(RANDOM_FONT.length)]);

        for (int i = 0; i < captcha.length(); i++) {

            int degree = random.nextInt(28);
            if (i % 2 == 0) {
                degree = degree * (-1);
            }

            int x = 22 * i, y = 21;
            g.rotate(Math.toRadians(degree), x, y);
            color = getRandColor(20, 130);
            g.setColor(color);
            g.drawString(String.valueOf(captcha.charAt(i)), x + 8, y + 10);
            g.rotate(-Math.toRadians(degree), x, y);
        }

        g.setColor(color);
        BasicStroke bs = new BasicStroke(3);
        g.setStroke(bs);
        QuadCurve2D.Double curve = new QuadCurve2D.Double(0d, random.nextInt(height - 8) + 4, width / 2, height / 2, width, random.nextInt(height - 8) + 4);
        g.draw(curve);
        g.dispose();
    }

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}







