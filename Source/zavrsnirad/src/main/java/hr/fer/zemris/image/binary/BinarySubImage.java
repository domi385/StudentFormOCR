package hr.fer.zemris.image.binary;

/**
 * Class that represents a subimage of original binary image.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 12.5.2017.
 */
public class BinarySubImage extends AbstractBinaryImage {

    /**
     * Number that JVM uses for setialization.
     */
    private static final long serialVersionUID = 1L;
    /**
     * X coordinate of upper left corner of subimage on original image.
     */
    private int x0;
    /**
     * Y coordinate of upper left corner of subimage on original image.
     */
    private int y0;

    /**
     * Width of the sub image.
     */
    private int width;
    /**
     * Height of the sub image.
     */
    private int height;

    /**
     * Reference to the original binary image.
     */
    private IBinaryImage original;

    /**
     * Constructor that constructs subimage from original image and offset parameters.
     *
     * @param x0
     *            x coordinate of upper left corner of subimage
     * @param y0
     *            y coordinate of upper left cornet of subimage
     * @param width
     *            subimage's width
     * @param height
     *            subimage's height
     * @param original
     *            original binary image
     */
    public BinarySubImage(int x0, int y0, int width, int height, IBinaryImage original) {
        super();
        this.x0 = x0;
        this.y0 = y0;
        this.width = width;
        this.height = height;
        this.original = original;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public boolean getPixel(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw new IllegalArgumentException();
        }
        return original.getPixel(x0 + x, y0 + y);
    }

    @Override
    public int getWidth() {
        return width;
    }

}
