package hr.fer.zemris.form;

import hr.fer.zemris.image.binary.IBinaryImage;
import hr.fer.zemris.image.geometry.CoordinateSystem2D;

/**
 * Class models a form node used in form processing. It contains important form informations.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 29.4.2017.
 */
public class FormNode {

    /**
     * Form coordinate system.
     */
    private CoordinateSystem2D coordinateSystem;
    /**
     * Binarized form.
     */
    private IBinaryImage binarizedForm;
    /**
     * Form template parameters for form data extraction.
     */
    private FormTemplateParameters formTemplateParameters;

    /**
     * Form file name.
     */
    private String formName;

    /**
     * Method obtains form file name.
     *
     * @return form file name
     */
    public String getFormName() {
        return formName;
    }

    /**
     * Method sets form file name.
     *
     * @param formName
     *            form file name
     */
    public void setFormName(String formName) {
        this.formName = formName;
    }

    /**
     * Public empty constructor for sequentially building of a form node.
     */
    public FormNode() {

    }

    /**
     * Constructor that initializes form node with binarized image and form template.
     *
     * @param image
     *            binarized form image
     * @param params
     *            form template parameters for form data extraction
     * @throws IllegalArgumentException
     *             if any of arguments is null
     */
    public FormNode(IBinaryImage image, FormTemplateParameters params) throws IllegalArgumentException {
        if (binarizedForm == null || params == null) {
            throw new IllegalArgumentException("When fully defining form node all arguments must not be null");
        }
        this.binarizedForm = image;
        this.formTemplateParameters = params;
        defineCoordinateSystem();
    }

    /**
     * Method defines current form coordinate system.
     */
    public void defineCoordinateSystem() {
        if (binarizedForm == null || formTemplateParameters == null) {
            throw new IllegalStateException("Form image or form template hasn't been defined.");
        }
        coordinateSystem = MarkersUtility.coordinateSystemFromMarkers(MarkersUtility.findMarkersByPositions(
                binarizedForm, formTemplateParameters.getMarkerPositions(),
                formTemplateParameters.getExpMarkerSize() * 2));

    }

    /**
     * Method obtains binarized form image.
     *
     * @return binarized form image
     */
    public IBinaryImage getBinarizedForm() {
        return binarizedForm;
    }

    /**
     * Method obtains form node coordinate system.
     *
     * @return coordinate system
     */
    public CoordinateSystem2D getCoordinateSystem() {
        return coordinateSystem;
    }

    /**
     * Method obtains form template used when defining current form node.
     *
     * @return form template parameters
     */
    public FormTemplateParameters getFormTemplate() {
        return formTemplateParameters;
    }

    /**
     * Method checks if the coordinate system is defined for current form node.
     *
     * @return true if the form node is defined, false otherwise
     */
    public boolean isCoordinateSystemDefined() {
        return coordinateSystem != null;
    }

    /**
     * Method sets binarized image of a form.
     *
     * @param binarizedForm
     *            binarized form
     */
    public void setBinarizedForm(IBinaryImage binarizedForm) {
        this.binarizedForm = binarizedForm;
    }

    /**
     * Method sets form template parameters.
     *
     * @param formTemplateParameters
     *            template parameters used to define a form
     */
    public void setFormTemplateParameters(FormTemplateParameters formTemplateParameters) {
        this.formTemplateParameters = formTemplateParameters;
    }

}
