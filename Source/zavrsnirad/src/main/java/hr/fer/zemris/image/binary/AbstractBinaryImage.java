package hr.fer.zemris.image.binary;

import hr.fer.zemris.image.geometry.Point;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract binary image that implements common methods to all binary images.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 30.4.2017.
 */
public abstract class AbstractBinaryImage implements IBinaryImage {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public abstract int getHeight();

    @Override
    public abstract boolean getPixel(int x, int y);

    @Override
    public double[] getPixels() {
        double[] pixels = new double[getWidth() * getHeight()];
        for (int y = 0, endHeight = getHeight(); y < endHeight; y++) {
            for (int x = 0, endWidth = getWidth(); x < endWidth; x++) {
                if (getPixel(x, y)) {
                    pixels[y * endWidth + x] = 1;
                }
            }
        }
        return pixels;

    }

    @Override
    public List<Point> getPixels(boolean pixelValue) {
        List<Point> pixels = new ArrayList<>();
        for (int y = 0, endHeight = getHeight(); y < endHeight; y++) {
            for (int x = 0, endWidth = getWidth(); x < endWidth; x++) {
                if (getPixel(x, y) == pixelValue) {
                    pixels.add(new Point(x, y));
                }
            }
        }
        return pixels;
    }

    @Override
    public IBinaryImage getSubimage(int x, int y, int width, int height) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight() || x + width > getWidth() || y + height > getHeight()
                || width < 0 || height < 0) {
            throw new IllegalArgumentException();
        }

        return new BinarySubImage(x, y, width, height, this);
    }

    @Override
    public abstract int getWidth();

    @Override
    public BufferedImage toImage() {
        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_BYTE_BINARY);

        for (int y = 0, endHeight = getHeight(); y < endHeight; y++) {
            for (int x = 0, endWidth = getWidth(); x < endWidth; x++) {
                if (getPixel(x, y)) {
                    bi.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    bi.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        return bi;
    }
}
