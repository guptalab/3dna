/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;


public class SaveDetailedDataListener implements ActionListener {
	
	public CoordinatesSeqMap c;
    static boolean isBoundary=false;

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
                    if (withoutBoundaryBricksCheckBox.isSelected() == true&&MainFrame.isCSVSaved==false) {
                        MainFrame.isCSVSaved=true;
                        MainFrame.isBoundaryCalled = false;
                        PrintData();
                    } else if (withBoundaryBricksCheckBox.isSelected() == true&&MainFrame.isCSVBoundarySaved==false) {
                        MainFrame.isCSVBoundarySaved=true;
                        MainFrame.isBoundaryCalled = true;
                        PrintBoundaryData();
                    }
                    outputFrame.dispose();
                }
            });
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MainFrame.isCSVSaved=true;
                    outputFrame.dispose();
                }
            });
        }
        else{
            JOptionPane.showMessageDialog(null, "Already Saved File !",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void PrintData(){

        ArrayList<VoxelToBrick> DNASequenceData=SaveFinalSequences.FinalData;
        if(MainFrame.isPDFSaved == false)
            c=new CoordinatesSeqMap();
        // TODO Auto-generated method stub
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(MainFrame.projectPath+"/3DNA_output" + ".csv"));
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        System.out.println("csv file has been successfully created in your project workspace");
        //if file doesnt exists, then create it

        try {
            bufferedWriter.write("\n\n3DNA -DNA Sequences Data for nano structure "+MainFrame.height+"H x "+MainFrame.width+"H x "+MainFrame.depth*8+"B");
            bufferedWriter.write("\n\nNumber of strands:"+DNASequenceData.size()+"\n");

            int domainCount=0;
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
            bufferedWriter.write(",");
            bufferedWriter.write("Voxels");
            bufferedWriter.write("\n");

            if(isBoundary==false) {
                for (int i = 0; i < DNASequenceData.size(); i++) {
                    System.out.println("printing for" + i);
                    bufferedWriter.write(DNASequenceData.get(i).getCompleteSequence());
                    bufferedWriter.write(",");
                    if (DNASequenceData.get(i).voxel1[0] == -1) {
                        bufferedWriter.write("[" + DNASequenceData.get(i).voxel2[0] + "" +
                                DNASequenceData.get(i).voxel2[1] + "" +
                                DNASequenceData.get(i).voxel2[2] + "]");
                        bufferedWriter.write(",");
                    } else if (DNASequenceData.get(i).voxel2[0] == -1) {
                        bufferedWriter.write("[" + DNASequenceData.get(i).voxel1[0] + "" +
                                DNASequenceData.get(i).voxel1[1] + "" +
                                DNASequenceData.get(i).voxel1[2] + "]");
                        bufferedWriter.write(",");
                    }
                    else {
                        bufferedWriter.write("[" + DNASequenceData.get(i).voxel1[0] + "" +
                                DNASequenceData.get(i).voxel1[1] + "" +
                                DNASequenceData.get(i).voxel1[2] + "  " +
                                DNASequenceData.get(i).voxel2[0] + "" +
                                DNASequenceData.get(i).voxel2[1] + "" +
                                DNASequenceData.get(i).voxel2[2] + "]");
                    }

                    bufferedWriter.write("\n");
                }
            }


            bufferedWriter.write("\n\n Generated using 3DNA");
            bufferedWriter.write(",");
            bufferedWriter.write("(http://www.guptalab.org/3dna/)");
            bufferedWriter.close();
            JOptionPane.showMessageDialog(null, "File Saved Successfully !",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void PrintBoundaryData(){

        ArrayList<VoxelToBrick> DNASequenceBoundaryData=SaveFinalSequences.FinalData;
        if(MainFrame.isPDFBoundarySaved == false)
            c=new CoordinatesSeqMap();
        // TODO Auto-generated method stub
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(MainFrame.projectPath+"/3DNA_output" + ".csv"));
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
            bufferedWriter.write(",");
            bufferedWriter.write("Voxels");
            bufferedWriter.write("\n");

                for (int i = 0; i < DNASequenceBoundaryData.size(); i++) {
                    System.out.println("printing for" + i);
                    bufferedWriter.write(DNASequenceBoundaryData.get(i).getCompleteSequence());
                    bufferedWriter.write(",");
                    if (DNASequenceBoundaryData.get(i).voxel3[0] != -1) {
                        if (DNASequenceBoundaryData.get(i).voxel1[0] == -1) {
                            bufferedWriter.write("[" + DNASequenceBoundaryData.get(i).voxel2[0] + "" +
                                    DNASequenceBoundaryData.get(i).voxel2[1] + "" +
                                    DNASequenceBoundaryData.get(i).voxel2[2] + "  " +
                                    DNASequenceBoundaryData.get(i).voxel3[0] + "" +
                                    DNASequenceBoundaryData.get(i).voxel3[1] + "" +
                                    DNASequenceBoundaryData.get(i).voxel3[2] + "]");
                            bufferedWriter.write(",");
                        }

                        else if (DNASequenceBoundaryData.get(i).voxel2[0] == -1) {
                            bufferedWriter.write("[" + DNASequenceBoundaryData.get(i).voxel1[0] + "" +
                                    DNASequenceBoundaryData.get(i).voxel1[1] + "" +
                                    DNASequenceBoundaryData.get(i).voxel1[2] + "  " +
                                    DNASequenceBoundaryData.get(i).voxel3[0] + "" +
                                    DNASequenceBoundaryData.get(i).voxel3[1] + "" +
                                    DNASequenceBoundaryData.get(i).voxel3[2] +"]");
                            bufferedWriter.write(",");
                        }

                        else{
                            bufferedWriter.write("[" + DNASequenceBoundaryData.get(i).voxel1[0] + "" +
                                    DNASequenceBoundaryData.get(i).voxel1[1] + "" +
                                    DNASequenceBoundaryData.get(i).voxel1[2] + "  " +
                                    DNASequenceBoundaryData.get(i).voxel2[0] + "" +
                                    DNASequenceBoundaryData.get(i).voxel2[1] + "" +
                                    DNASequenceBoundaryData.get(i).voxel2[2] + "  " +
                                    DNASequenceBoundaryData.get(i).voxel3[0] + "" +
                                    DNASequenceBoundaryData.get(i).voxel3[1] + "" +
                                    DNASequenceBoundaryData.get(i).voxel3[2] + "]");
                        }

                    }

                    else{
                        if (DNASequenceBoundaryData.get(i).voxel1[0] == -1) {
                            bufferedWriter.write("[" + DNASequenceBoundaryData.get(i).voxel2[0] + "" +
                                    DNASequenceBoundaryData.get(i).voxel2[1] + "" +
                                    DNASequenceBoundaryData.get(i).voxel2[2] + "]");
                            bufferedWriter.write(",");
                        } else if (DNASequenceBoundaryData.get(i).voxel2[0] == -1) {
                            bufferedWriter.write("[" + DNASequenceBoundaryData.get(i).voxel1[0] + "" +
                                    DNASequenceBoundaryData.get(i).voxel1[1] + "" +
                                    DNASequenceBoundaryData.get(i).voxel1[2] + "]");
                            bufferedWriter.write(",");

                        } else
                            bufferedWriter.write("[" + DNASequenceBoundaryData.get(i).voxel1[0] + "" +
                                    DNASequenceBoundaryData.get(i).voxel1[1] + "" +
                                    DNASequenceBoundaryData.get(i).voxel1[2] + "  " +
                                    DNASequenceBoundaryData.get(i).voxel2[0] + "" +
                                    DNASequenceBoundaryData.get(i).voxel2[1] + "" +
                                    DNASequenceBoundaryData.get(i).voxel2[2] + "]");
                    }
                    bufferedWriter.write("\n");
                }


            bufferedWriter.write("\n\n Generated using 3DNA");
            bufferedWriter.write(",");
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


