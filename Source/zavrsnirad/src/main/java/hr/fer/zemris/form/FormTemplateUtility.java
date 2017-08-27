package hr.fer.zemris.form;

import hr.fer.zemris.image.geometry.RegionEdge;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for form manipulation using form template.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 30.4.2017.
 */
public class FormTemplateUtility {

    /**
     * Method extracts fields from given image with form template parameters and region crop offsets.
     *
     * @param ftp
     *            form tempalte parameters
     * @param formImage
     *            new form image
     * @param heightOffset
     *            crop height offset
     * @param widthOffset
     *            crop width offset
     * @param deltax
     *            distance from original origins' x coordinate
     * @param deltay
     *            distance from original origins' y coordinate
     * @param scalex
     *            scaling x axis factor
     * @param scaley
     *            scaling y axis factor
     * @return extracted fields
     */
    public static List<BufferedImage> extractFields(FormTemplateParameters ftp, BufferedImage formImage,
            int heightOffset, int widthOffset, int deltax, int deltay, double scalex, double scaley) {
        List<RegionEdge> pointFields = ftp.getPointFields();
        List<BufferedImage> resultFileds = new ArrayList<BufferedImage>();
        for (RegionEdge field : pointFields) {
            Rectangle fieldRectangle = field.getBoundingRectange();
            BufferedImage fieldImage = formImage.getSubimage(fieldRectangle.x + deltax + widthOffset, fieldRectangle.y
                    + deltay + heightOffset, (int) (fieldRectangle.width * scalex) - 2 * widthOffset,
                    (int) (fieldRectangle.height * scaley) - 2 * heightOffset);
            resultFileds.add(fieldImage);
        }
        return resultFileds;
    }

    /**
     * Method reads form template from file.
     *
     * @param templateFile
     *            form template file
     * @return form template or null if the file isn't form template //TODO
     * @throws IOException
     *             if there was an exception while reading from file
     * @throws ClassNotFoundException
     *             if read object's class cannot be found
     */
    public static FormTemplateParameters readTemplateFromFile(File templateFile) throws IOException,
    ClassNotFoundException {
        try (ObjectInputStream ons = new ObjectInputStream(Files.newInputStream(templateFile.toPath(),
                StandardOpenOption.READ))) {
            FormTemplateParameters ftp = (FormTemplateParameters) ons.readObject();
            return ftp;
        }
    }

    /**
     * Method writes form template to the output file.
     *
     * @param outputFile
     *            output file
     * @param formTemplate
     *            form template to be written
     * @throws IOException
     *             if there was an error while writing to file
     */
    public static void writeTemplateToFile(File outputFile, FormTemplateParameters formTemplate) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(outputFile.toPath(),
                StandardOpenOption.WRITE, StandardOpenOption.CREATE))) {
            oos.writeObject(formTemplate);
        }
    }

    /**
     * Private utility class constructor.
     */
    private FormTemplateUtility() {
    }

}
