package hr.fer.zemris.studentforms.formdefinition.panels;

import hr.fer.zemris.form.FormTemplateParameters;
import hr.fer.zemris.form.FormTemplateUtility;
import hr.fer.zemris.studentforms.formdefinition.DefinitionStatus;
import hr.fer.zemris.studentforms.formdefinition.data.FormDefinitionData;
import hr.fer.zemris.studentforms.formdefinition.data.FormDefinitionDataAdapter;
import hr.fer.zemris.studentforms.formdefinition.data.FormDefinitionDataListener;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FormDefinitionToolsPanel extends JPanel {

    /**
     * Number that JVM uses for serialization.
     */
    private static final long serialVersionUID = 1L;
    private JButton btnLoadImage;
    private JButton btnDefineMarkers;
    private JButton btnDefinePoints;
    private JButton btnSaveTemplate;
    private FormDefinitionData dataModel;

    private FormDefinitionDataListener btnDefinePointsDataListener = new FormDefinitionDataAdapter() {
        @Override
        public void statusChanged(DefinitionStatus status) {
            switch (status) {
            case DEFINED_TEMPLATE:
                btnDefinePoints.setEnabled(true);
                break;
            case POINTS_DEFINITION:
                btnDefinePoints.setEnabled(true);
                break;
            default:
                btnDefinePoints.setEnabled(false);
                break;
            }
        }
    };

    private FormDefinitionDataListener btnLoadImageDataListener = new FormDefinitionDataAdapter() {
        @Override
        public void statusChanged(DefinitionStatus status) {
            switch (status) {
            case DEFINED_TEMPLATE:
                btnLoadImage.setEnabled(true);
                break;
            case UNDEFINED_TEMPLATE:
                btnLoadImage.setEnabled(true);
                break;
            case NO_TEMPLATE:
                btnLoadImage.setEnabled(true);
                break;
            default:
                btnLoadImage.setEnabled(false);
            }
        };
    };
    private FormDefinitionDataListener btnDefineMarkersDataListener = new FormDefinitionDataAdapter() {
        @Override
        public void statusChanged(DefinitionStatus status) {
            switch (status) {
            case DEFINED_TEMPLATE:
                btnDefineMarkers.setEnabled(true);
                break;
            case UNDEFINED_TEMPLATE:
                btnDefineMarkers.setEnabled(true);
                break;
            default:
                btnDefineMarkers.setEnabled(false);
            }
        }
    };

    private ActionListener btnSaveTemplateListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            saveTemplateDialog();
        }

    };

    private ActionListener btnLoadImageListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            loadImageDialog();
        }
    };

    private ActionListener btnDefinePointsListener = new ActionListener() {

        private boolean stateCancel = false;

        @Override
        public void actionPerformed(ActionEvent e) {

            stateCancel = !stateCancel;
            if (stateCancel) {
                dataModel.setStatus(DefinitionStatus.POINTS_DEFINITION);
                btnDefinePoints.setText("Done");
            } else {
                dataModel.setStatus(DefinitionStatus.DEFINED_TEMPLATE);
                btnDefinePoints.setText("Define points");
            }
        }
    };

    private ActionListener btnDefineMarkersListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            dataModel.setStatus(DefinitionStatus.MARKER_DEFINITION);
        }
    };

    public FormDefinitionToolsPanel(FormDefinitionData dataModel) {
        this.dataModel = dataModel;
        this.dataModel.addListener(btnDefineMarkersDataListener);
        this.dataModel.addListener(btnLoadImageDataListener);
        this.dataModel.addListener(btnDefinePointsDataListener);
        initGUI();
    }

    private void changeImage(BufferedImage image) {
        dataModel.setImage(image);
    }

    private void changeTemplate(FormTemplateParameters templateParameters) {
        dataModel.setFormParams(templateParameters);
    }

    /**
     * Method initializes panel's GUI.
     */
    private void initGUI() {
        setLayout(new GridLayout(10, 1, 1, 2));
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1, true));

        btnLoadImage = new JButton("Load template");
        add(btnLoadImage);
        btnLoadImage.addActionListener(btnLoadImageListener);

        btnSaveTemplate = new JButton("Save template");
        add(btnSaveTemplate);
        btnSaveTemplate.addActionListener(btnSaveTemplateListener);

        btnDefineMarkers = new JButton("Define markers");
        add(btnDefineMarkers);
        btnDefineMarkers.addActionListener(btnDefineMarkersListener);
        btnDefineMarkers.setEnabled(false);

        btnDefinePoints = new JButton("Define points");
        add(btnDefinePoints);
        btnDefinePoints.addActionListener(btnDefinePointsListener);
        btnDefinePoints.setEnabled(false);

    }

    private void invalidFileFormatMsg() {
        JOptionPane.showMessageDialog(this, "File that you tried to load had invalid format", "Invalid file format",
                JOptionPane.ERROR_MESSAGE);
    }

    private void loadImage(File imageFile) {
        Thread t = new Thread() {
            private BufferedImage image;

            @Override
            public void run() {
                try {
                    image = ImageIO.read(imageFile);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                SwingUtilities.invokeLater(() -> changeImage(image));

            }
        };
        t.start();

    }

    private void loadImageDialog() {
        String userDir = System.getProperty("user.home") + "/Desktop";
        JFileChooser chooser = new JFileChooser(userDir);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Templates", "jpg", "png", "ftp");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File selectedFile = chooser.getSelectedFile();
        if (selectedFile.toPath().getFileName().toString().endsWith(".ftp")) {
            loadTemplate(selectedFile);
        } else {
            loadImage(selectedFile);
        }
    }

    private void loadTemplate(File templateFile) {
        Thread t = new Thread() {
            private FormTemplateParameters params;

            @Override
            public void run() {

                try {
                    params = FormTemplateUtility.readTemplateFromFile(templateFile);
                    SwingUtilities.invokeLater(() -> changeTemplate(params));
                } catch (ClassNotFoundException e) {
                    SwingUtilities.invokeLater(() -> invalidFileFormatMsg());
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> unsuccesfullIOOperationMsg());
                }

            }

        };
        t.start();

    }

    private void saveTemplateDialog() {
        String userDir = System.getProperty("user.home") + "/Desktop";
        JFileChooser chooser = new JFileChooser(userDir);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Templates", "ftp");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File selectedFile = chooser.getSelectedFile();
        Thread t = new Thread() {
            private final File file = selectedFile;
            private final FormTemplateParameters params = dataModel.getFormTemplate();

            @Override
            public void run() {
                try {
                    FormTemplateUtility.writeTemplateToFile(file, params);
                } catch (IOException e) {
                    SwingUtilities.invokeLater(() -> unsuccesfullIOOperationMsg());
                }

            }

        };
        t.start();

    }

    private void unsuccesfullIOOperationMsg() {
        JOptionPane.showMessageDialog(this,
                "There was an error while reading/writing to a file because of file system error.", "Error",
                JOptionPane.ERROR_MESSAGE);
    }

}
