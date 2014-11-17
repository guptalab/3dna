/** Author: Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

//This class saves the detailed dna data in a csv file.
public class SaveCSV {

    public CoordinatesSequenceMap c;

    public void SaveCSV() {
        System.out.println("Save as csv called from saving options");

    }

    //This function saves the detailed data without boundary bricks in a file.
    public void PrintData(){

        ArrayList<VoxelToBrick> DNASequenceData = CoordinatesSequenceMap.brickList;
        java.util.Date date = new java.util.Date();
        long timeStamp = date.getTime();

        if(MainFrame.isPDFSaved == false) {
            c = new CoordinatesSequenceMap();
            DNASequenceData = CoordinatesSequenceMap.brickList;
        }
        // TODO Auto-generated method stub
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(MainFrame.projectPath+"/3DNA_output_" + timeStamp + ".csv"));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        MainFrame.printLog("csv file has been successfully created in your project workspace", Color.green);
        //if file doesnt exists, then create it

        try {
            for (int i = 0; i < DNASequenceData.size(); i++) {
                if (DNASequenceData.get(i).isRemoved == true) {
                    DNASequenceData.remove(i);
                    i--;
                }
            }

            bufferedWriter.write("\n\n3DNA -DNA Sequences Data for nano structure "+MainFrame.height+"H x "+MainFrame.width+"H x "+MainFrame.depth*8+"B");
            bufferedWriter.write("\n\nNumber of strands:"+DNASequenceData.size()+"\n");

            int domainCount = 0;
            for (int i = 0; i < DNASequenceData.size(); i++) {
                if(DNASequenceData.get(i).Domain1!=null)
                    domainCount++;
                if(DNASequenceData.get(i).Domain2!=null)
                    domainCount++;
                if(DNASequenceData.get(i).Domain3!=null)
                    domainCount++;
                if(DNASequenceData.get(i).Domain4!=null)
                    domainCount++;
                if(DNASequenceData.get(i).Domain5!=null)
                    domainCount++;
                if(DNASequenceData.get(i).Domain6!=null)
                    domainCount++;
            }
            bufferedWriter.write("Number of nucleotides: "+domainCount*8+"\n\n");

            bufferedWriter.write("DNA Sequences");
            bufferedWriter.write(String.valueOf(MainFrame.delimiter));
            bufferedWriter.write("[x y z] coordinates");
            bufferedWriter.write(String.valueOf(MainFrame.delimiter));
            bufferedWriter.write("helix");
            bufferedWriter.write("\n");

            for (int i = 0; i < DNASequenceData.size(); i++) {
                System.out.println("printing for" + i + " " + DNASequenceData.get(i).x2 + " " + DNASequenceData.get(i).y2 + " " + DNASequenceData.get(i).z2
                        + ", " + DNASequenceData.get(i).x3 + " " + DNASequenceData.get(i).y3 + " " + DNASequenceData.get(i).z3
                        + " Sequence:" + DNASequenceData.get(i).Domain1 + " " + DNASequenceData.get(i).Domain2 + " " + DNASequenceData.get(i).Domain3 +
                " " + DNASequenceData.get(i).Domain4);
                bufferedWriter.write(DNASequenceData.get(i).getCompleteSequence());
                bufferedWriter.write(String.valueOf(MainFrame.delimiter));
                //identify and print for domains 1 and 2
                if (DNASequenceData.get(i).x3 == -1) {
                    bufferedWriter.write("[" + DNASequenceData.get(i).x1 + "  " +
                            DNASequenceData.get(i).y1 + "  " +
                            DNASequenceData.get(i).z1 + "]");
                    bufferedWriter.write("[" + DNASequenceData.get(i).x2 + "  " +
                            DNASequenceData.get(i).y2 + "  " +
                            DNASequenceData.get(i).z2 + "]");
                    bufferedWriter.write(String.valueOf(MainFrame.delimiter));
                }
                //identify and print for domains 3 and 4
                else if (DNASequenceData.get(i).x1 == -1) {
                    bufferedWriter.write("[" + DNASequenceData.get(i).x3 + "  " +
                            DNASequenceData.get(i).y3 + "  " +
                            DNASequenceData.get(i).z3 + "]");
                    bufferedWriter.write("[" + DNASequenceData.get(i).x4 + "  " +
                            DNASequenceData.get(i).y4 + "  " +
                            DNASequenceData.get(i).z4 + "]");
                    bufferedWriter.write(String.valueOf(MainFrame.delimiter));
                }
                //identify and print for full bricks
                else {
                    bufferedWriter.write("[" + DNASequenceData.get(i).x1 + "  " +
                            DNASequenceData.get(i).y1 + "  " +
                            DNASequenceData.get(i).z1 + "][" +
                            DNASequenceData.get(i).x2 + "  " +
                            DNASequenceData.get(i).y2 + "  " +
                            DNASequenceData.get(i).z2 +"][" + DNASequenceData.get(i).x3 + "  " +
                            DNASequenceData.get(i).y3 + "  " +
                            DNASequenceData.get(i).z3 + "][" +
                            DNASequenceData.get(i).x4 + "  " +
                            DNASequenceData.get(i).y4 + "  " +
                            DNASequenceData.get(i).z4 + "]");
                    bufferedWriter.write(String.valueOf(MainFrame.delimiter));
                }
                bufferedWriter.write("[");
                //Printing helix values
                if(DNASequenceData.get(i).helix1 != -1) {
                    bufferedWriter.write(String.valueOf(DNASequenceData.get(i).helix1));
                    bufferedWriter.write("  ");
                    bufferedWriter.write(String.valueOf(DNASequenceData.get(i).z1));
                    bufferedWriter.write("  ");
                    bufferedWriter.write(String.valueOf(DNASequenceData.get(i).helix2));
                    bufferedWriter.write("  ");
                    bufferedWriter.write(String.valueOf(DNASequenceData.get(i).z2));
                }
                if(DNASequenceData.get(i).helix1 != -1 && DNASequenceData.get(i).helix3 != -1)
                    bufferedWriter.write("  ");
                if(DNASequenceData.get(i).helix3!=-1) {
                    bufferedWriter.write(String.valueOf(DNASequenceData.get(i).helix3));
                    bufferedWriter.write("  ");
                    bufferedWriter.write(String.valueOf(DNASequenceData.get(i).z3));
                    bufferedWriter.write("  ");
                    bufferedWriter.write(String.valueOf(DNASequenceData.get(i).helix4));
                    bufferedWriter.write("  ");
                    bufferedWriter.write(String.valueOf(DNASequenceData.get(i).z4));
                }
                bufferedWriter.write("]");
                bufferedWriter.write("\n");
            }



            bufferedWriter.write("\n\n Generated using 3DNA");
            bufferedWriter.write(String.valueOf(MainFrame.delimiter));
            bufferedWriter.write("(http://www.guptalab.org/3dna/)");
            bufferedWriter.close();

            final JFrame fileSavedFrame = new JFrame("Output success");
            fileSavedFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fileSavedFrame.setBackground(Color.DARK_GRAY);
            fileSavedFrame.setIconImage(MainFrame.imag);

            JPanel fileSavedPanel = new JPanel();
            fileSavedPanel.setBackground(Color.DARK_GRAY);

            final JLabel fileSavedLabel = new JLabel("<html><style>h4{color:white;}</style><h4> CSV file has been saved in your workspace</h4></html>");
            JButton okButton = new JButton("OK");

            fileSavedPanel.add(fileSavedLabel);
            fileSavedPanel.add(okButton);
            fileSavedFrame.add(fileSavedPanel, BorderLayout.CENTER);
            fileSavedFrame.pack();
            fileSavedFrame.setVisible(true);
            fileSavedFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 3);

            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fileSavedFrame.dispose();
                }
            });

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    //This function saves the detailed data with boundary bricks in a file.
    public void PrintBoundaryData(){

        ArrayList<VoxelToBrick> DNASequenceBoundaryData = CoordinatesSequenceMap.boundaryBrickList;
        java.util.Date date = new java.util.Date();
        long timeStamp = date.getTime();

        if(MainFrame.isPDFBoundarySaved == false){
            c = new CoordinatesSequenceMap();
            DNASequenceBoundaryData = CoordinatesSequenceMap.boundaryBrickList;
        }
        // TODO Auto-generated method stub
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(MainFrame.projectPath+"/3DNA_output" + timeStamp + ".csv"));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        MainFrame.printLog("csv file with boundary sequences has been successfully created in your project workspace", Color.green);
        //if file doesnt exists, then create it

        try {
            for (int i = 0; i < DNASequenceBoundaryData.size(); i++) {
                if (DNASequenceBoundaryData.get(i).isRemoved == true) {
                    DNASequenceBoundaryData.remove(i);
                    i--;
                }
            }
            bufferedWriter.write("\n\n3DNA -DNA Sequences Data for nano structure "+MainFrame.height+"H x "+MainFrame.width+"H x "+MainFrame.depth*8+"B");
            bufferedWriter.write("\n\nNumber of strands:"+DNASequenceBoundaryData.size()+"\n");

            int domainCount=0;
            for (int i = 0; i < DNASequenceBoundaryData.size(); i++) {
                if(DNASequenceBoundaryData.get(i).Domain1!=null)
                    domainCount++;
                if(DNASequenceBoundaryData.get(i).Domain2!=null)
                    domainCount++;
                if(DNASequenceBoundaryData.get(i).Domain3!=null)
                    domainCount++;
                if(DNASequenceBoundaryData.get(i).Domain4!=null)
                    domainCount++;
                if(DNASequenceBoundaryData.get(i).Domain5!=null)
                    domainCount++;
                if(DNASequenceBoundaryData.get(i).Domain6!=null)
                    domainCount++;
            }
            bufferedWriter.write("Number of nucleotides: "+domainCount*8+"\n\n");
            bufferedWriter.write("DNA Sequences");
            bufferedWriter.write(MainFrame.delimiter);
            bufferedWriter.write("[x y z] coordinates");
            bufferedWriter.write(MainFrame.delimiter);
            bufferedWriter.write("helix");
            bufferedWriter.write("\n");

            for (int i = 0; i < DNASequenceBoundaryData.size(); i++) {
                //System.out.println("printing for" + i);
                bufferedWriter.write(DNASequenceBoundaryData.get(i).getCompleteSequence());
                bufferedWriter.write(String.valueOf(MainFrame.delimiter));
                //Identify and print bricks

                //check if domains 1 and 2 are present
                if (DNASequenceBoundaryData.get(i).x1 != -1) {
                    bufferedWriter.write("[" + DNASequenceBoundaryData.get(i).x1 + "  " +
                            DNASequenceBoundaryData.get(i).y1 + "  " +
                            DNASequenceBoundaryData.get(i).z1 + "][" +
                            DNASequenceBoundaryData.get(i).x2 + "  " +
                            DNASequenceBoundaryData.get(i).y2 + "  " +
                            DNASequenceBoundaryData.get(i).z2 + "]");
                }
                //check if domains 3 and 4 are present
                if (DNASequenceBoundaryData.get(i).x3 != -1) {
                    bufferedWriter.write("[" + DNASequenceBoundaryData.get(i).x3 + "  " +
                            DNASequenceBoundaryData.get(i).y3 + "  " +
                            DNASequenceBoundaryData.get(i).z3 + "][" +
                            DNASequenceBoundaryData.get(i).x4 + "  " +
                            DNASequenceBoundaryData.get(i).y4 + "  " +
                            DNASequenceBoundaryData.get(i).z4 +"]");
                }
                //now print the domains 5 and 6
                if (DNASequenceBoundaryData.get(i).x5 != -1) {
                    bufferedWriter.write("[" + DNASequenceBoundaryData.get(i).x5 + "  " +
                            DNASequenceBoundaryData.get(i).y5 + "  " +
                            DNASequenceBoundaryData.get(i).z5 + "][" +
                            DNASequenceBoundaryData.get(i).x6 + "  " +
                            DNASequenceBoundaryData.get(i).y6 + "  " +
                            DNASequenceBoundaryData.get(i).z6 + "]");
                }
                bufferedWriter.write(String.valueOf(MainFrame.delimiter));
                //Identify and print helix
                //check if domains 1 and 2 are present
                bufferedWriter.write("[");
                if (DNASequenceBoundaryData.get(i).x1 != -1) {
                    bufferedWriter.write(DNASequenceBoundaryData.get(i).helix1 + "  " +
                            DNASequenceBoundaryData.get(i).z1 + "  " +
                            DNASequenceBoundaryData.get(i).helix2 + "  " +
                            DNASequenceBoundaryData.get(i).z2);
                }
                if (DNASequenceBoundaryData.get(i).x1 != -1 && DNASequenceBoundaryData.get(i).x3 != -1)
                    bufferedWriter.write("  ");
                //check if domains 3 and 4 are present
                if (DNASequenceBoundaryData.get(i).x3 != -1) {
                    bufferedWriter.write(DNASequenceBoundaryData.get(i).helix3 + "  " +
                            DNASequenceBoundaryData.get(i).z3 + "  " +
                            DNASequenceBoundaryData.get(i).helix4 + "  " +
                            DNASequenceBoundaryData.get(i).z4);
                }
                if ((DNASequenceBoundaryData.get(i).x1 != -1 && DNASequenceBoundaryData.get(i).x5 != -1) ||
                        (DNASequenceBoundaryData.get(i).x3 != -1 && DNASequenceBoundaryData.get(i).x5 != -1))
                    bufferedWriter.write("  ");
                //now print the domains 5 and 6
                if (DNASequenceBoundaryData.get(i).x5 != -1) {
                    bufferedWriter.write(DNASequenceBoundaryData.get(i).helix5 + "  " +
                            DNASequenceBoundaryData.get(i).z5 + "  " +
                            DNASequenceBoundaryData.get(i).helix6 + "  " +
                            DNASequenceBoundaryData.get(i).z6);
                }
                bufferedWriter.write("]");
                bufferedWriter.write(String.valueOf(MainFrame.delimiter));
                bufferedWriter.write("\n");
            }


            bufferedWriter.write("\n\n Generated using 3DNA");
            bufferedWriter.write(String.valueOf(MainFrame.delimiter));
            bufferedWriter.write("(http://www.guptalab.org/3dna/)");
            bufferedWriter.close();
            ImageIcon img = new ImageIcon("icons/software_logo.png");
            java.awt.Image imag=img.getImage();

            final JFrame fileSavedFrame = new JFrame("Output success");
            fileSavedFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            fileSavedFrame.setBackground(Color.DARK_GRAY);
            fileSavedFrame.setIconImage(imag);

            JPanel fileSavedPanel = new JPanel();
            fileSavedPanel.setBackground(Color.DARK_GRAY);

            final JLabel fileSavedLabel = new JLabel("<html><style>h4{color:white;}</style><h4> CSV file with (boundary bricks) has been saved in your workspace</h4></html>");
            JButton okButton = new JButton("OK");
            okButton.setBackground(Color.DARK_GRAY);

            fileSavedPanel.add(fileSavedLabel);
            fileSavedPanel.add(okButton);
            fileSavedFrame.add(fileSavedPanel, BorderLayout.CENTER);
            fileSavedFrame.pack();
            fileSavedFrame.setVisible(true);
            fileSavedFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 3);

            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fileSavedFrame.dispose();
                }
            });

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
}


