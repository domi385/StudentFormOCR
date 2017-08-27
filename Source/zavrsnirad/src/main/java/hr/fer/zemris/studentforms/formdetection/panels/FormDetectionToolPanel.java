package hr.fer.zemris.studentforms.formdetection.panels;

import hr.fer.zemris.studentforms.dialogs.DisplayImagesDialog;
import hr.fer.zemris.studentforms.formdetection.FormDetectionStatus;
import hr.fer.zemris.studentforms.formdetection.data.FormDetectionDataAdapter;
import hr.fer.zemris.studentforms.formdetection.data.FormDetectionDataListener;
import hr.fer.zemris.studentforms.formdetection.data.FormDetectionDataModel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.ProgressMonitor;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Panel contains tools for form detection.
 *
 * @author Domagoj Pluscec
 * @version v1.0, 9.6.2017.
 */
public class FormDetectionToolPanel extends JPanel {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Form detection data model.
     */
    private FormDetectionDataModel dataModel;

    /**
     * Button for loading form for regions detection.
     */
    private JButton btnLoadForm;
    /**
     * Button for detecting coordanate system on a form.
     */
    private JButton btnDetectCoordinateSystem;
    /**
     * Button for rotating a form.
     */
    private JButton btnRotateForm;
    /**
     * Button for croping form.
     */
    private JButton btnCropForm;
    /**
     * Button extracts region from form.
     */
    private JButton btnExtractRegions;
    /**
     * Button display extracted regions from form.
     */
    private JButton btnDisplayExtractedRegions;
    /**
     * Button saves extracted segments.
     */
    private JButton btnSaveSegments;
    /**
     * Button saves extracted regions.
     */
    private JButton btnSaveRegions;

    /**
     * Action listener extracts regions.
     */
    private ActionListener btnExtractRegionsListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            ProgressMonitor progressMonitor = new ProgressMonitor(getParent(), "operation in progress...", "", 0,
                    dataModel.getPointsRegionsNum() - 1);
            FormDetectionDataAdapter regionsExtractionProgress = new FormDetectionDataAdapter() {
                @Override
                public void extractionUpdate(int progress) {
                    progressMonitor.setProgress(progress);
                }
            };

            dataModel.extractRegions(regionsExtractionProgress);

        }
    };
    /**
     * Action listener rotates transformed form.
     */
    private ActionListener btnRotateFormListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            dataModel.rotateForm();
        }
    };

    /**
     * Action listener defines form coordinate system.
     */
    private ActionListener btnDefineCoordinateSystemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dataModel.defineCoordinateSystem();
        };
    };

    /**
     * Action listener crops form when button is clicked.
     */
    private ActionListener btnCropListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dataModel.cropForm();
        };
    };

    /**
     * Action listener loads a form.
     */
    private ActionListener btnLoadFormListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            loadFormDialog();
        }
    };
    /**
     * Action listener detects form coordinate system.
     */
    private FormDetectionDataListener btnCoordDataListener = new FormDetectionDataAdapter() {
        @Override
        public void statusChanged(hr.fer.zemris.studentforms.formdetection.FormDetectionStatus status) {
            switch (status) {
            case NO_FORM:
                btnDetectCoordinateSystem.setEnabled(false);
                break;
            default:
                btnDetectCoordinateSystem.setEnabled(true);
            }
        };
    };
    /**
     * Listener that enables and disables rotation button according to data model.
     */
    private FormDetectionDataListener btnRotateDataListener = new FormDetectionDataAdapter() {
        @Override
        public void statusChanged(FormDetectionStatus status) {
            switch (status) {
            case NO_FORM:
                btnRotateForm.setEnabled(false);
                break;
            case NO_COORD:
                btnRotateForm.setEnabled(false);
                break;
            default:
                btnRotateForm.setEnabled(true);
                break;
            }
        }
    };
    /**
     * Listener that enables and disables crop button according to data model.
     */
    private FormDetectionDataListener btnCropDataListener = new FormDetectionDataAdapter() {
        @Override
        public void statusChanged(FormDetectionStatus status) {
            switch (status) {
            case NO_FORM:
                btnCropForm.setEnabled(false);
                break;
            case NO_COORD:
                btnCropForm.setEnabled(false);
                break;
            default:
                btnCropForm.setEnabled(true);
                break;
            }
        }
    };
    /**
     * Listener that enables and disables display button according to data model.
     */
    private FormDetectionDataListener btnDisplayRegionsDataListener = new FormDetectionDataAdapter() {
        @Override
        public void statusChanged(FormDetectionStatus status) {
            switch (status) {
            case DETECTED_REGIONS:
                btnDisplayExtractedRegions.setEnabled(true);
                break;
            default:
                btnDisplayExtractedRegions.setEnabled(false);
                break;
            }
        }
    };
    /**
     * Listener that enables and disables extract regions button according to data model.
     */
    private FormDetectionDataListener btnExtractRegionsDataListener = new FormDetectionDataAdapter() {
        @Override
        public void statusChanged(FormDetectionStatus status) {
            switch (status) {
            case NO_REGIONS:
                btnExtractRegions.setEnabled(true);
                break;
            case DETECTED_REGIONS:
                btnExtractRegions.setEnabled(true);
                break;
            default:
                btnExtractRegions.setEnabled(false);
                break;
            }
        }
    };
    /**
     * Listener that enables and disables save segments button according to data model.
     */
    private FormDetectionDataListener btnSaveSegmentsDataListener = new FormDetectionDataAdapter() {
        @Override
        public void statusChanged(FormDetectionStatus status) {
            switch (status) {
            case DETECTED_REGIONS:
                btnSaveSegments.setEnabled(true);
                break;
            default:
                btnSaveSegments.setEnabled(false);
                break;
            }
        }
    };
    /**
     * Listener that enables and disables save regions button according to data model.
     */
    private FormDetectionDataListener btnSaveRegionsDataListener = new FormDetectionDataAdapter() {
        @Override
        public void statusChanged(FormDetectionStatus status) {
            switch (status) {
            case DETECTED_REGIONS:
                btnSaveRegions.setEnabled(true);
                break;
            default:
                btnSaveRegions.setEnabled(false);
                break;
            }
        }
    };

    /**
     * Constructor that initializes panels data model, gui and data model listeners.
     *
     * @param dataModel
     *            panels from detection data model
     */
    public FormDetectionToolPanel(FormDetectionDataModel dataModel) {
        this.dataModel = dataModel;
        initGUI();
        initDataModelListeners();
    }

    /**
     * Method attaches all panels data model listeners.
     */
    private void initDataModelListeners() {
        dataModel.addListener(btnCoordDataListener);
        dataModel.addListener(btnRotateDataListener);
        dataModel.addListener(btnCropDataListener);
        dataModel.addListener(btnExtractRegionsDataListener);
        dataModel.addListener(btnDisplayRegionsDataListener);
        dataModel.addListener(btnSaveSegmentsDataListener);
        dataModel.addListener(btnSaveRegionsDataListener);
    }

    /**
     * Method changes form template image.
     *
     * @param image
     *            new image
     */
    private void changeImage(BufferedImage image) {
        dataModel.changeForm(image);
    }

    /**
     * Method displays extracted regions dialog.
     */
    private void displayExtractedRegionsDialog() {
        DisplayImagesDialog dialog = new DisplayImagesDialog(dataModel.getPointRegions());
        JButton btnDisplaySegments = new JButton("Display segmented");
        btnDisplaySegments.addActionListener(e -> {
            DisplayImagesDialog segmentsDialog = new DisplayImagesDialog(dataModel.getSegmentedImages(dialog
                    .getImageIndex()));
            segmentsDialog.setVisible(true);
        });
        dialog.addLowerComponent(btnDisplaySegments);
        dialog.setVisible(true);
    }

    /**
     * Method initializes panels GUI.
     */
    private void initGUI() {
        final int numberOfToolCols = 1;
        final int numberOfToolRows = 11;
        setLayout(new GridLayout(numberOfToolRows, numberOfToolCols));
        btnLoadForm = new JButton("Load form");
        btnLoadForm.addActionListener(btnLoadFormListener);
        add(btnLoadForm);

        btnDetectCoordinateSystem = new JButton("Detect coordinate system");
        add(btnDetectCoordinateSystem);
        btnDetectCoordinateSystem.addActionListener(btnDefineCoordinateSystemListener);
        btnDetectCoordinateSystem.setEnabled(false);

        btnRotateForm = new JButton("Rotate form");
        btnRotateForm.addActionListener(btnRotateFormListener);
        add(btnRotateForm);
        btnRotateForm.setEnabled(false);

        btnCropForm = new JButton("Crop form");
        btnCropForm.addActionListener(btnCropListener);
        add(btnCropForm);
        btnCropForm.setEnabled(false);

        btnExtractRegions = new JButton("Extract regions");
        btnExtractRegions.addActionListener(btnExtractRegionsListener);
        add(btnExtractRegions);
        btnExtractRegions.setEnabled(false);

        btnDisplayExtractedRegions = new JButton("Display regions");
        btnDisplayExtractedRegions.addActionListener(e -> displayExtractedRegionsDialog());
        add(btnDisplayExtractedRegions);
        btnDisplayExtractedRegions.setEnabled(false);

        btnSaveRegions = new JButton("Save regions");
        add(btnSaveRegions);
        btnSaveRegions.setEnabled(false);
        btnSaveRegions.addActionListener(e -> saveExtracedRegions());
        btnSaveSegments = new JButton("Save segments");
        add(btnSaveSegments);
        btnSaveSegments.setEnabled(false);
        btnSaveSegments.addActionListener(e -> saveExtractedSegments());

    }

    /**
     * Method saves regions to selected directory.
     */
    private void saveExtracedRegions() {
        File directory = selectDirectory();
        if (directory == null) {
            return;
        }
        dataModel.saveRegions(directory);
    }

    /**
     * Method saves segmented regions to selected directory.
     */
    private void saveExtractedSegments() {
        File directory = selectDirectory();
        if (directory == null) {
            return;
        }
        dataModel.saveSegments(directory);
    }

    /**
     * Method selects directory file.
     *
     * @return directory file or null if it wasn't successfully selected
     */
    private File selectDirectory() {
        String userDir = System.getProperty("user.home") + "/Desktop";
        JFileChooser chooser = new JFileChooser(userDir);
        chooser.setDialogTitle("Select directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return null;
        }
        return chooser.getSelectedFile();
    }

    /**
     * Method displays dialog for selecting form in form of image or template file.
     */
    private void loadFormDialog() {
        String userDir = System.getProperty("user.home") + "/Desktop";
        JFileChooser chooser = new JFileChooser(userDir);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File selectedFile = chooser.getSelectedFile();
        loadImage(selectedFile);
    }

    /**
     * Method loads image in new thread and saves it by calling changeImage method.
     *
     * @param imageFile
     *            image file to be loaded
     */
    private void loadImage(File imageFile) {
        Thread t = new Thread() {
            private BufferedImage image;

            @Override
            public void run() {
                try {
                    image = ImageIO.read(imageFile);
                } catch (IOException e) {
                    // TODO
                    e.printStackTrace();
                }
                SwingUtilities.invokeLater(() -> changeImage(image));

            }

        };
        t.start();
    }
}
