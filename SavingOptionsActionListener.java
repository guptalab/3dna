

import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SavingOptionsActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        if((MainFrame.isBoundaryCalled && (MainFrame.isPDFBoundarySaved == false || MainFrame.isCSVBoundarySaved == false)) ||
                (MainFrame.isBoundaryCalled == false && (MainFrame.isPDFSaved == false ||  MainFrame.isCSVSaved == false ))) {
            //option pane
            ImageIcon saveAsCSVIcon = new ImageIcon("icons/csv.png");
            ImageIcon saveAsPDFIcon = new ImageIcon("icons/pdf.png");
            ImageIcon img = new ImageIcon("icons/software_logo.png");
            Image imag=img.getImage();

            final JFrame outputFrame = new JFrame("3DNA Output Options for .pdf");
            outputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            outputFrame.setBackground(Color.DARK_GRAY);

            outputFrame.setIconImage(imag);

            final JPanel mainPanel = new JPanel();
            mainPanel.setBackground(Color.DARK_GRAY);

            final JCheckBox saveAsPDFCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Save as .pdf</h4></html>");
            saveAsPDFCheckbox.setBackground(Color.DARK_GRAY);
            final JCheckBox saveAsCSVCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Save as .csv</h4></html>");
            saveAsCSVCheckbox.setBackground(Color.DARK_GRAY);
            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Cancel");

            mainPanel.add(saveAsPDFCheckbox);
            mainPanel.add(saveAsCSVCheckbox);
            mainPanel.add(okButton);
            outputFrame.add(mainPanel, BorderLayout.CENTER);
            outputFrame.pack();
            outputFrame.setVisible(true);
            outputFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 3);

            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Check if boundary bricks have been enabled
                    if (MainFrame.isMinXBoundaryPlaneEnabled || MainFrame.isMaxXBoundaryPlaneEnabled || MainFrame.isMinYBoundaryPlaneEnabled || MainFrame.isMaxYBoundaryPlaneEnabled)
                        MainFrame.isBoundaryCalled = true;
                    else
                        MainFrame.isBoundaryCalled = false;

                    if(saveAsPDFCheckbox.isSelected()){
                        if (MainFrame.isImportSequencesListEnabled || MainFrame.isYonggangSequencesListEnabled) {
                            System.out.println("Save as pdf from imported file called");
                            SavePDFFromImportedFile savePDF = new SavePDFFromImportedFile();
                            if (MainFrame.isBoundaryCalled == true) {
                                savePDF.PrintBoundaryData();
                                MainFrame.isPDFBoundarySaved = true;
                            }
                            else {
                                savePDF.PrintData();
                                MainFrame.isPDFSaved = true;
                            }
                        }

                        else{
                            System.out.println("Save as pdf called");
                            SavePDF savePDF = new SavePDF();
                            if (MainFrame.isBoundaryCalled == true) {
                                savePDF.PrintBoundaryData();
                                MainFrame.isPDFBoundarySaved = true;
                            }
                            else {
                                savePDF.PrintData();
                                MainFrame.isPDFSaved = true;
                            }
                        }

                    }
                    if(saveAsCSVCheckbox.isSelected()){
                        if (MainFrame.isImportSequencesListEnabled || MainFrame.isYonggangSequencesListEnabled) {
                            System.out.println("Save as csv from imported file called");
                            SaveCSVFromImportedFile saveCSV = new SaveCSVFromImportedFile();
                            if (MainFrame.isBoundaryCalled == true) {
                                saveCSV.PrintBoundaryData();
                                MainFrame.isCSVBoundarySaved = true;
                            }
                            else {
                                saveCSV.PrintData();
                                MainFrame.isCSVSaved = true;
                            }
                        }

                        else {
                            System.out.println("Save as csv called");
                            SaveCSV saveCSV = new SaveCSV();
                            if (MainFrame.isBoundaryCalled == true) {
                                saveCSV.PrintBoundaryData();
                                MainFrame.isCSVBoundarySaved = true;
                            }
                            else {
                                saveCSV.PrintData();
                                MainFrame.isCSVSaved = true;
                            }
                        }

                    }
                    outputFrame.dispose();
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainFrame.isPDFSaved = false;
                    outputFrame.dispose();
                }
            });
        }
        else{
            ImageIcon img = new ImageIcon("icons/software_logo.png");
            Image imag=img.getImage();

            final JFrame alreadySavedFrame = new JFrame("Output error");
            alreadySavedFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            alreadySavedFrame.setBackground(Color.DARK_GRAY);
            alreadySavedFrame.setIconImage(imag);

            JPanel alreadySavedPanel = new JPanel();
            alreadySavedPanel.setBackground(Color.DARK_GRAY);

            final JLabel alreadySavedLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Already saved all files!! </h4></html>");
            JButton okButton = new JButton("OK");

            alreadySavedPanel.add(alreadySavedLabel);
            alreadySavedPanel.add(okButton);
            alreadySavedFrame.add(alreadySavedPanel, BorderLayout.CENTER);
            alreadySavedFrame.pack();
            alreadySavedFrame.setVisible(true);
            alreadySavedFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 3);

            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    alreadySavedFrame.dispose();
                }
            });
        }
    }

}


