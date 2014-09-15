/**
 * Created by foram.joshi on 29.08.2014.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SaveDetailedDataFromImportedFile implements ActionListener {

    public ImportSequencesCoordinatesMap c;
    static boolean isBoundary = false;

    @Override
    public void actionPerformed(ActionEvent e) {

        if (MainFrame.isCSVSaved==false||MainFrame.isCSVBoundarySaved==false) {

            //option pane
            final JFrame outputFrame = new JFrame("3DNA Output Options");
            outputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            GridBagConstraints outputFrameGridBagConstraints = new GridBagConstraints();
            outputFrameGridBagConstraints.insets = new Insets(5, 5, 5, 5);

            final JRadioButton withBoundaryBricksCheckBox = new JRadioButton("Create DNA Sequences with 48nt boundary bricks");
            withBoundaryBricksCheckBox.setSelected(false);
            final JRadioButton withoutBoundaryBricksCheckBox = new JRadioButton("Create DNA Sequences with 16nt boundary bricks");
            withoutBoundaryBricksCheckBox.setSelected(true);
            ButtonGroup group = new ButtonGroup();
            group.add(withBoundaryBricksCheckBox);
            group.add(withoutBoundaryBricksCheckBox);

            JButton okButton = new JButton("OK");
            JButton cancelButton = new JButton("Cancel");

            outputFrameGridBagConstraints.gridx = 0;
            outputFrameGridBagConstraints.gridy = 0;
            mainPanel.add(withBoundaryBricksCheckBox, outputFrameGridBagConstraints);

            outputFrameGridBagConstraints.gridx = 0;
            outputFrameGridBagConstraints.gridy = 1;
            mainPanel.add(withoutBoundaryBricksCheckBox, outputFrameGridBagConstraints);

            outputFrameGridBagConstraints.gridx = 0;
            outputFrameGridBagConstraints.gridy = 2;
            mainPanel.add(okButton, outputFrameGridBagConstraints);

            outputFrameGridBagConstraints.gridx = 1;
            outputFrameGridBagConstraints.gridy = 2;
            mainPanel.add(cancelButton, outputFrameGridBagConstraints);

            outputFrame.add(mainPanel, BorderLayout.CENTER);
            outputFrame.pack();
            outputFrame.setVisible(true);
            outputFrame.setLocation(500, 300);

            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (withoutBoundaryBricksCheckBox.isSelected() == true&&MainFrame.isCSVSaved == false) {
                        MainFrame.isCSVSaved = true;
                        MainFrame.isBoundaryCalled = false;
                        PrintData();
                    } else if (withBoundaryBricksCheckBox.isSelected() == true&&MainFrame.isCSVBoundarySaved == false) {
                        MainFrame.isCSVBoundarySaved =true;
                        MainFrame.isBoundaryCalled = true;
                        PrintBoundaryData();
                    }
                    outputFrame.dispose();
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainFrame.isCSVSaved = true;
                    outputFrame.dispose();
                }
            });
        }
        else{
            JOptionPane.showMessageDialog(null, "Already Saved File !",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //This function saves the detailed data without boundary bricks in a file.
    public void PrintData(){

        ArrayList<VoxelToBrick> DNASequenceData = SetSequencesFromFile.finalData;
        java.util.Date date = new java.util.Date();
        long timeStamp = date.getTime();

        if(MainFrame.isPDFSaved == false) {
            c = new ImportSequencesCoordinatesMap();
            DNASequenceData = SetSequencesFromFile.finalData;
        }
        // TODO Auto-generated method stub
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(MainFrame.projectPath+"/3DNA_output_" + timeStamp + ".csv"));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println("csv file has been successfully created in your project workspace");
        //if file doesnt exists, then create it

        try {
            bufferedWriter.write("\n\n3DNA -DNA Sequences Data for nano structure "+MainFrame.height+"H x "+MainFrame.width+"H x "+MainFrame.depth*8+"B");
            bufferedWriter.write("\n\nNumber of strands:"+DNASequenceData.size()+"\n");

            int domainCount = 0;
            for (int i = 0; i < DNASequenceData.size(); i++) {
                if (DNASequenceData.get(i).isMapped == true) {
                    if (DNASequenceData.get(i).Domain1 != null)
                        domainCount++;
                    if (DNASequenceData.get(i).Domain2 != null)
                        domainCount++;
                    if (DNASequenceData.get(i).Domain3 != null)
                        domainCount++;
                    if (DNASequenceData.get(i).Domain4 != null)
                        domainCount++;
                    if (DNASequenceData.get(i).Domain5 != null)
                        domainCount++;
                    if (DNASequenceData.get(i).Domain6 != null)
                        domainCount++;
                }
            }
            bufferedWriter.write("Number of nucleotides: "+domainCount*8+"\n\n");

            bufferedWriter.write("Strand number");
            bufferedWriter.write(";");
            bufferedWriter.write("Plate number");
            bufferedWriter.write(";");
            bufferedWriter.write("Position");
            bufferedWriter.write(";");
            bufferedWriter.write("DNA Sequences");
            bufferedWriter.write(";");
            bufferedWriter.write("[x,y,z] coordinates");
            bufferedWriter.write(";");
            bufferedWriter.write("helix");
            bufferedWriter.write("\n");

            if(isBoundary==false) {
                for (int i = 0; i < DNASequenceData.size(); i++) {

                    if (DNASequenceData.get(i).isMapped == true) {
                        bufferedWriter.write(String.valueOf(DNASequenceData.get(i).strandNumber));
                        bufferedWriter.write(";");
                        bufferedWriter.write(String.valueOf(DNASequenceData.get(i).plateNumber));
                        bufferedWriter.write(";");
                        bufferedWriter.write(String.valueOf(DNASequenceData.get(i).position));
                        bufferedWriter.write(";");
                        System.out.println("printing for" + i + " " + DNASequenceData.get(i).getCompleteSequence());
                        bufferedWriter.write(DNASequenceData.get(i).getCompleteSequence());
                        bufferedWriter.write(";");
                        //identify and print for domains 1 and 2
                        if (DNASequenceData.get(i).x3 == -1) {
                            bufferedWriter.write("[" + DNASequenceData.get(i).x1 + "," +
                                    DNASequenceData.get(i).y1 + "," +
                                    DNASequenceData.get(i).z1 + "]");
                            bufferedWriter.write("[" + DNASequenceData.get(i).x2 + "," +
                                    DNASequenceData.get(i).y2 + "," +
                                    DNASequenceData.get(i).z2 + "]");
                            bufferedWriter.write(";");
                        }
                        //identify and print for domains 3 and 4
                        else if (DNASequenceData.get(i).x1 == -1) {
                            bufferedWriter.write("[" + DNASequenceData.get(i).x3 + "," +
                                    DNASequenceData.get(i).y3 + "," +
                                    DNASequenceData.get(i).z3 + "]");
                            bufferedWriter.write("[" + DNASequenceData.get(i).x4 + "," +
                                    DNASequenceData.get(i).y4 + "," +
                                    DNASequenceData.get(i).z4 + "]");
                            bufferedWriter.write(";");
                        }
                        //identify and print for full bricks
                        else {
                            bufferedWriter.write("[" + DNASequenceData.get(i).x1 + "," +
                                    DNASequenceData.get(i).y1 + "," +
                                    DNASequenceData.get(i).z1 + "][" +
                                    DNASequenceData.get(i).x2 + "," +
                                    DNASequenceData.get(i).y2 + "," +
                                    DNASequenceData.get(i).z2 + "][" + DNASequenceData.get(i).x3 + "," +
                                    DNASequenceData.get(i).y3 + "," +
                                    DNASequenceData.get(i).z3 + "][" +
                                    DNASequenceData.get(i).x4 + "," +
                                    DNASequenceData.get(i).y4 + "," +
                                    DNASequenceData.get(i).z4 + "]");
                            bufferedWriter.write(";");
                        }
                        bufferedWriter.write("[");
                        //Printing helix values
                        if (DNASequenceData.get(i).helix1 != -1) {
                            bufferedWriter.write(String.valueOf(DNASequenceData.get(i).helix1));
                            bufferedWriter.write(",");
                            bufferedWriter.write(String.valueOf(DNASequenceData.get(i).z1));
                            bufferedWriter.write(",");
                            bufferedWriter.write(String.valueOf(DNASequenceData.get(i).helix2));
                            bufferedWriter.write(",");
                            bufferedWriter.write(String.valueOf(DNASequenceData.get(i).z2));
                        }
                        if (DNASequenceData.get(i).helix1 != -1 && DNASequenceData.get(i).helix3 != -1)
                            bufferedWriter.write(",");
                        if (DNASequenceData.get(i).helix3 != -1) {
                            bufferedWriter.write(String.valueOf(DNASequenceData.get(i).helix3));
                            bufferedWriter.write(",");
                            bufferedWriter.write(String.valueOf(DNASequenceData.get(i).z3));
                            bufferedWriter.write(",");
                            bufferedWriter.write(String.valueOf(DNASequenceData.get(i).helix4));
                            bufferedWriter.write(",");
                            bufferedWriter.write(String.valueOf(DNASequenceData.get(i).z4));
                        }
                        bufferedWriter.write("]");
                        bufferedWriter.write("\n");
                    }
                }
            }


            bufferedWriter.write("\n\n Generated using 3DNA");
            bufferedWriter.write(";");
            bufferedWriter.write("(http://www.guptalab.org/3dna/)");
            bufferedWriter.close();
            JOptionPane.showMessageDialog(null, "File Saved Successfully !",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    //This function saves the detailed data with boundary bricks in a file.
    public void PrintBoundaryData(){

        ArrayList<VoxelToBrick> DNASequenceBoundaryData = SetSequencesFromFile.finalData;
        java.util.Date date = new java.util.Date();
        long timeStamp = date.getTime();

        if(MainFrame.isPDFBoundarySaved == false){
            c = new ImportSequencesCoordinatesMap();
            DNASequenceBoundaryData = SetSequencesFromFile.finalData;
        }
        // TODO Auto-generated method stub
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(MainFrame.projectPath+"/3DNA_output" + timeStamp + ".csv"));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println("csv file has been successfully created in your project workspace");
        //if file doesnt exists, then create it

        try {
            bufferedWriter.write("\n\n3DNA -DNA Sequences Data for nano structure "+MainFrame.height+"H x "+MainFrame.width+"H x "+MainFrame.depth*8+"B");
            bufferedWriter.write("\n\nNumber of strands:"+DNASequenceBoundaryData.size()+"\n");

            int domainCount=0;
            for (int i = 0; i < DNASequenceBoundaryData.size(); i++) {
                if(DNASequenceBoundaryData.get(i).isMapped == true) {
                    if (DNASequenceBoundaryData.get(i).Domain1 != null)
                        domainCount++;
                    if (DNASequenceBoundaryData.get(i).Domain2 != null)
                        domainCount++;
                    if (DNASequenceBoundaryData.get(i).Domain3 != null)
                        domainCount++;
                    if (DNASequenceBoundaryData.get(i).Domain4 != null)
                        domainCount++;
                    if (DNASequenceBoundaryData.get(i).Domain5 != null)
                        domainCount++;
                    if (DNASequenceBoundaryData.get(i).Domain6 != null)
                        domainCount++;
                }
            }
            bufferedWriter.write("Number of nucleotides: "+domainCount*8+"\n\n");
            bufferedWriter.write("Strand number");
            bufferedWriter.write(";");
            bufferedWriter.write("Plate number");
            bufferedWriter.write(";");
            bufferedWriter.write("Position");
            bufferedWriter.write(";");
            bufferedWriter.write("DNA Sequences");
            bufferedWriter.write(";");
            bufferedWriter.write("[x,y,z] coordinates");
            bufferedWriter.write(";");
            bufferedWriter.write("helix");
            bufferedWriter.write("\n");

            for (int i = 0; i < DNASequenceBoundaryData.size(); i++) {
                //System.out.println("printing for" + i);
                if (DNASequenceBoundaryData.get(i).isMapped == true) {
                    bufferedWriter.write(String.valueOf(DNASequenceBoundaryData.get(i).strandNumber));
                    bufferedWriter.write(";");
                    bufferedWriter.write(String.valueOf(DNASequenceBoundaryData.get(i).plateNumber));
                    bufferedWriter.write(";");
                    bufferedWriter.write(String.valueOf(DNASequenceBoundaryData.get(i).position));
                    bufferedWriter.write(";");
                    bufferedWriter.write(DNASequenceBoundaryData.get(i).getCompleteSequence());
                    bufferedWriter.write(";");
                    //Identify and print bricks

                    //check if domains 1 and 2 are present
                    if (DNASequenceBoundaryData.get(i).x1 != -1) {
                        bufferedWriter.write("[" + DNASequenceBoundaryData.get(i).x1 + "," +
                                DNASequenceBoundaryData.get(i).y1 + "," +
                                DNASequenceBoundaryData.get(i).z1 + "][" +
                                DNASequenceBoundaryData.get(i).x2 + "," +
                                DNASequenceBoundaryData.get(i).y2 + "," +
                                DNASequenceBoundaryData.get(i).z2 + "]");
                    }
                    //check if domains 3 and 4 are present
                    if (DNASequenceBoundaryData.get(i).x3 != -1) {
                        bufferedWriter.write("[" + DNASequenceBoundaryData.get(i).x3 + "," +
                                DNASequenceBoundaryData.get(i).y3 + "," +
                                DNASequenceBoundaryData.get(i).z3 + "][" +
                                DNASequenceBoundaryData.get(i).x4 + "," +
                                DNASequenceBoundaryData.get(i).y4 + "," +
                                DNASequenceBoundaryData.get(i).z4 + "]");
                    }
                    //now print the domains 5 and 6
                    if (DNASequenceBoundaryData.get(i).x5 != -1) {
                        bufferedWriter.write("[" + DNASequenceBoundaryData.get(i).x5 + "," +
                                DNASequenceBoundaryData.get(i).y5 + "," +
                                DNASequenceBoundaryData.get(i).z5 + "][" +
                                DNASequenceBoundaryData.get(i).x6 + "," +
                                DNASequenceBoundaryData.get(i).y6 + "," +
                                DNASequenceBoundaryData.get(i).z6 + "]");
                    }
                    bufferedWriter.write(";");
                    //Identify and print helix
                    //check if domains 1 and 2 are present
                    bufferedWriter.write("[");
                    if (DNASequenceBoundaryData.get(i).x1 != -1) {
                        bufferedWriter.write(DNASequenceBoundaryData.get(i).helix1 + "," +
                                DNASequenceBoundaryData.get(i).z1 + "," +
                                DNASequenceBoundaryData.get(i).helix2 + "," +
                                DNASequenceBoundaryData.get(i).z2);
                    }
                    if (DNASequenceBoundaryData.get(i).x1 != -1 && DNASequenceBoundaryData.get(i).x3 != -1)
                        bufferedWriter.write(",");
                    //check if domains 3 and 4 are present
                    if (DNASequenceBoundaryData.get(i).x3 != -1) {
                        bufferedWriter.write(DNASequenceBoundaryData.get(i).helix3 + "," +
                                DNASequenceBoundaryData.get(i).z3 + "," +
                                DNASequenceBoundaryData.get(i).helix4 + "," +
                                DNASequenceBoundaryData.get(i).z4);
                    }
                    if ((DNASequenceBoundaryData.get(i).x1 != -1 && DNASequenceBoundaryData.get(i).x5 != -1) ||
                            (DNASequenceBoundaryData.get(i).x3 != -1 && DNASequenceBoundaryData.get(i).x5 != -1))
                        bufferedWriter.write(",");
                    //now print the domains 5 and 6
                    if (DNASequenceBoundaryData.get(i).x5 != -1) {
                        bufferedWriter.write("[" + DNASequenceBoundaryData.get(i).helix5 + "," +
                                DNASequenceBoundaryData.get(i).z5 + "," +
                                DNASequenceBoundaryData.get(i).helix6 + "," +
                                DNASequenceBoundaryData.get(i).z6 + "]");
                    }
                    bufferedWriter.write("]");
                    bufferedWriter.write(";");
                    bufferedWriter.write("\n");
                }
            }

            bufferedWriter.write("\n\n Generated using 3DNA");
            bufferedWriter.write(";");
            bufferedWriter.write("(http://www.guptalab.org/3dna/)");
            bufferedWriter.close();
            JOptionPane.showMessageDialog(null, "File Saved Successfully !",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}


