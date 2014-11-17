import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

/**
 * Created by shikhar.kumar-gupta on 28.08.2014.
 */
public class UploadSequenceActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e){
        final JFrame chooseSequencesListFrame = new JFrame("DNA Sequences Lists");
        chooseSequencesListFrame.setBackground(Color.DARK_GRAY);
        chooseSequencesListFrame.setResizable(false);
        chooseSequencesListFrame.setIconImage(MainFrame.imag);
        chooseSequencesListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
        dimensionFrameGridBagConstraints.insets = new Insets(3, 3, 3, 3);
        JLabel chooseSequencesListLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Select List:</h4></html>");
        final JRadioButton randomSequencesListRadioButton = new JRadioButton("<html><style>h4{color:white;}</style><h4>Random Sequences</h4></html>");
        randomSequencesListRadioButton.setBackground(Color.DARK_GRAY);
        if (MainFrame.isRandomSequencesListEnabled)
            randomSequencesListRadioButton.setSelected(true);
        final JRadioButton importSequencesListRadioButton = new JRadioButton("<html><style>h4{color:white;}</style><h4>Import Sequences</h4></html>");
        importSequencesListRadioButton.setBackground(Color.DARK_GRAY);
        if (MainFrame.isImportSequencesListEnabled)
            importSequencesListRadioButton.setSelected(true);

        ButtonGroup sequencesListGroup = new ButtonGroup();
        sequencesListGroup.add(randomSequencesListRadioButton);
        sequencesListGroup.add(importSequencesListRadioButton);

        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.DARK_GRAY);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 0;
        mainPanel.add(chooseSequencesListLabel, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 1;
        mainPanel.add(randomSequencesListRadioButton, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 2;
        mainPanel.add(importSequencesListRadioButton, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 3;
        mainPanel.add(okButton, dimensionFrameGridBagConstraints);

        chooseSequencesListFrame.add(mainPanel, BorderLayout.CENTER);
        chooseSequencesListFrame.pack();
        chooseSequencesListFrame.setVisible(true);
        chooseSequencesListFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 4);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (randomSequencesListRadioButton.isSelected()){
                    MainFrame.isRandomSequencesListEnabled = true;
                    MainFrame.isYonggangSequencesListEnabled = false;
                    MainFrame.isImportSequencesListEnabled =false;
                }
                else if (importSequencesListRadioButton.isSelected()) {
                    final boolean[] is16ntSelected = {false};
                    final boolean[] is32ntSelected = {false};
                    final boolean[] is48ntSelected = {false};


                    final JTextArea log;
                    log = new JTextArea(5, 40);
                    log.setEditable(false);
                    log.setMargin(new Insets(5, 5, 5, 5));
                    JScrollPane logScrollPane = new JScrollPane(log);
                    final JFileChooser halfBrickSequenceFileChooser = new JFileChooser();
                    halfBrickSequenceFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    final JFileChooser fullBrickSequenceFileChooser = new JFileChooser();
                    fullBrickSequenceFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    final JFileChooser boundaryBrickSequenceFileChooser = new JFileChooser();
                    boundaryBrickSequenceFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                    final JFrame importcsvFrame = new JFrame("Import .csv files");
                    importcsvFrame.setBackground(Color.DARK_GRAY);
                    ImageIcon img = new ImageIcon("icons/software_logo.png");
                    Image imag = img.getImage();
                    importcsvFrame.setIconImage(imag);
                    importcsvFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    JPanel mainPanel = new JPanel(new BorderLayout());
                    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
                    mainPanel.setBackground(Color.DARK_GRAY);

                    JButton halfBrickListButton = new JButton("Select 16nt file");
                    halfBrickListButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    halfBrickListButton.setBackground(Color.DARK_GRAY);
                    JButton fullBrickListButton = new JButton("Select 32nt file");
                    fullBrickListButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    fullBrickListButton.setBackground(Color.DARK_GRAY);
                    JButton boundaryBrickListButton = new JButton("Select 48nt file");
                    boundaryBrickListButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    boundaryBrickListButton.setBackground(Color.DARK_GRAY);
                    JButton okButton = new JButton("OK");
                    okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    okButton.setBackground(Color.DARK_GRAY);

                    mainPanel.add(halfBrickListButton, BorderLayout.CENTER);
                    mainPanel.add(Box.createVerticalStrut(10));
                    mainPanel.add(fullBrickListButton, BorderLayout.CENTER);
                    mainPanel.add(Box.createVerticalStrut(10));
                    mainPanel.add(boundaryBrickListButton, BorderLayout.CENTER);
                    mainPanel.add(Box.createVerticalStrut(10));
                    mainPanel.add(logScrollPane, BorderLayout.CENTER);
                    mainPanel.add(okButton, BorderLayout.CENTER);

                    importcsvFrame.add(mainPanel, BorderLayout.CENTER);
                    importcsvFrame.pack();
                    importcsvFrame.setVisible(true);
                    importcsvFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 3);

                    halfBrickListButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int returnValue = halfBrickSequenceFileChooser.showOpenDialog(null);
                            if (returnValue == JFileChooser.APPROVE_OPTION) {
                                File importedHalfBrickFile = halfBrickSequenceFileChooser.getSelectedFile();
                                MainFrame.importedHalfBrickFilePath = new String();
                                MainFrame.importedHalfBrickFilePath = importedHalfBrickFile.getAbsolutePath();
                                log.append("Selected file for 16nt:" + importedHalfBrickFile.getName() + "\n");
                                log.setCaretPosition(log.getDocument().getLength());
                                is16ntSelected[0] = true;
                                if (is16ntSelected[0] == true && is32ntSelected[0] == true && is48ntSelected[0] == true) {
                                    MainFrame.isYonggangSequencesListEnabled = false;
                                    MainFrame.isRandomSequencesListEnabled = false;
                                    MainFrame.isImportSequencesListEnabled = true;
                                    log.append("All files selected\n");
                                    log.setCaretPosition(log.getDocument().getLength());
                                }

                                try {
                                    System.out.println(importedHalfBrickFile.getName());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });
                    fullBrickListButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int returnValue = fullBrickSequenceFileChooser.showOpenDialog(null);
                            if (returnValue == JFileChooser.APPROVE_OPTION) {
                                File importedFullBrickFile = fullBrickSequenceFileChooser.getSelectedFile();
                                MainFrame.importedFullBrickFilePath = new String();
                                MainFrame.importedFullBrickFilePath = importedFullBrickFile.getAbsolutePath();
                                log.append("Selected file for 32nt:" + importedFullBrickFile.getName() + "\n");
                                log.setCaretPosition(log.getDocument().getLength());
                                is32ntSelected[0] = true;
                                if (is16ntSelected[0] == true && is32ntSelected[0] == true && is48ntSelected[0] == true) {
                                    MainFrame.isImportSequencesListEnabled = true;
                                    MainFrame.isYonggangSequencesListEnabled = false;
                                    MainFrame.isRandomSequencesListEnabled = false;
                                    log.append("All files selected\n");
                                    log.setCaretPosition(log.getDocument().getLength());
                                }
                                try {
                                    System.out.println(importedFullBrickFile.getName());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });
                    boundaryBrickListButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int returnValue = boundaryBrickSequenceFileChooser.showOpenDialog(null);
                            if (returnValue == JFileChooser.APPROVE_OPTION) {
                                File importedBoundaryBrickFile = boundaryBrickSequenceFileChooser.getSelectedFile();
                                MainFrame.importedBoundaryBrickFilePath = new String();
                                MainFrame.importedBoundaryBrickFilePath = importedBoundaryBrickFile.getAbsolutePath();
                                log.append("Selected file for 48nt:" + importedBoundaryBrickFile.getName() + "\n");
                                log.setCaretPosition(log.getDocument().getLength());
                                is48ntSelected[0] = true;
                                if (is16ntSelected[0] == true && is32ntSelected[0] == true && is48ntSelected[0] == true) {
                                    MainFrame.isImportSequencesListEnabled = true;
                                    MainFrame.isYonggangSequencesListEnabled = false;
                                    MainFrame.isRandomSequencesListEnabled = false;
                                    log.append("All files selected\n");
                                    log.setCaretPosition(log.getDocument().getLength());
                                }
                                try {
                                    System.out.println(importedBoundaryBrickFile.getName());
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });
                    okButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (MainFrame.importedHalfBrickFilePath == null) {
                                MainFrame.printLog("error: no file chosen for 16nt sequences", Color.yellow);
                            }
                            if (MainFrame.importedFullBrickFilePath == null) {
                                MainFrame.printLog("error: no file chosen for 32nt sequences", Color.yellow);
                            }
                            if (MainFrame.importedBoundaryBrickFilePath == null) {
                                MainFrame.printLog("error: no file chosen for 48nt sequences", Color.yellow);
                            }
                            if ((MainFrame.importedHalfBrickFilePath != null)) {
                                MainFrame.printLog("File imported for 16nt sequences: " + MainFrame.importedHalfBrickFilePath, Color.cyan);
                            }
                            if ((MainFrame.importedFullBrickFilePath != null)) {
                                MainFrame.printLog("File imported for 32nt sequences: " + MainFrame.importedFullBrickFilePath, Color.cyan);
                            }
                            if ((MainFrame.importedBoundaryBrickFilePath != null)) {
                                MainFrame.printLog("File imported for 48nt sequences: " + MainFrame.importedBoundaryBrickFilePath, Color.cyan);
                            }
                            importcsvFrame.dispose();

                        }
                    });
                    importcsvFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    importcsvFrame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosed(WindowEvent e) {
                        }
                    });
                }
                chooseSequencesListFrame.dispose();

            }
        });
        chooseSequencesListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


            }
        }
