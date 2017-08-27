package hr.fer.zemris.image.binary;

/**
 * Utility class for working with binary images.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 29.4.2017.
 */
public class BinaryImageUtility {

    /**
     * Method calculates histogram on horizontal axis of given image.
     *
     * @param image
     *            binarized image
     * @return histogram of pixels on image width
     */
    public static int[] calcHorizontalHistogram(IBinaryImage image) {
        int[] histogram = new int[image.getWidth()];

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (!image.getPixel(x, y)) {
                    histogram[x]++;
                }
            }
        }
        return histogram;
    }

    /**
     * Method calculates histogram on vertical axis of given image.
     *
     * @param image
     *            binarized image
     * @return histogram of pixels on image height
     */
    public static int[] calcVerticalHistogram(IBinaryImage image) {
        int[] histogram = new int[image.getHeight()];

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (!image.getPixel(x, y)) {
                    histogram[y]++;
                }
            }
        }
        return histogram;
    }

    /**
     * Method crops binary image for completely black parts.
     *
     * @param image
     *            binary image
     * @return croped binary image
     */
    public static IBinaryImage crop(IBinaryImage image) {
        return cropWithThreshold(image, 0);
    }

    /**
     * Method crops image borders with given threshold.
     *
     * @param image
     *            binarized image
     * @param threshold
     *            all borders that have less than threshold number of pixels will be croped
     * @return croped binary image
     */
    public static IBinaryImage cropWithThreshold(IBinaryImage image, int threshold) {
        int[] verticalHistogram = BinaryImageUtility.calcVerticalHistogram(image);

        int miny = 0;
        for (int i = 0; i < verticalHistogram.length; i++) {
            if (verticalHistogram[i] > threshold) {
                miny = i;
                break;
            }
        }

        int maxy = verticalHistogram.length - 1;
        for (int j = verticalHistogram.length - 1; j >= 0; j--) {
            if (verticalHistogram[j] > threshold) {
                maxy = j;
                break;
            }
        }

        int[] horizontalHistogram = BinaryImageUtility.calcHorizontalHistogram(image);
        int minx = 0;
        int maxx = horizontalHistogram.length - 1;

        for (int i = 0; i < horizontalHistogram.length; i++) {
            if (horizontalHistogram[i] > threshold) {
                minx = i;
                break;
            }
        }
        for (int j = horizontalHistogram.length - 1; j >= 0; j--) {
            if (horizontalHistogram[j] > threshold) {
                maxx = j;
                break;
            }
        }

        return image.getSubimage(minx, miny, maxx - minx, maxy - miny);

    }

    /**
     * Private utility class constructor.
     */
    private BinaryImageUtility() {
    }

}
