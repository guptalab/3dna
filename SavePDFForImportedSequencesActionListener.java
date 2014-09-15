/**
 * Created by foram.joshi on 05.09.2014.
 */
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;


public class SavePDFForImportedSequencesActionListener implements ActionListener {
    public ImportSequencesCoordinatesMap c;
    @Override
    public void actionPerformed(ActionEvent e) {
        if(MainFrame.isPDFSaved == false ||MainFrame.isPDFBoundarySaved==false ) {

            //option pane
            final JFrame outputFrame = new JFrame("3DNA Output Options for .pdf");
            outputFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new GridBagLayout());
            GridBagConstraints outputFrameGridBagConstraints = new GridBagConstraints();
            outputFrameGridBagConstraints.insets = new Insets(5, 5, 5, 5);

            final JRadioButton withBoundaryBricksCheckBox = new JRadioButton("Create DNA Sequences with 48nt boundary bricks");
            withBoundaryBricksCheckBox.setSelected(false);
            final JRadioButton withoutBoundaryBricksCheckBox = new JRadioButton("Create DNA Sequences without boundary bricks");
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
                    if (withoutBoundaryBricksCheckBox.isSelected() == true && MainFrame.isPDFSaved==false) {
                        MainFrame.isPDFSaved = true;
                        MainFrame.isBoundaryCalled = false;
                        PrintData();
                    } else if (withBoundaryBricksCheckBox.isSelected() == true && MainFrame.isPDFBoundarySaved==false) {
                        MainFrame.isPDFBoundarySaved = true;
                        MainFrame.isBoundaryCalled = true;
                        PrintBoundaryData();
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
            JOptionPane.showMessageDialog(null, "Already Saved File !",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void PrintData(){
        ArrayList<VoxelToBrick> DNASequenceData = SetSequencesFromFile.finalData;
        if(MainFrame.isCSVSaved == false) {
            c = new ImportSequencesCoordinatesMap();
            DNASequenceData = SetSequencesFromFile.finalData;
        }
        // TODO Auto-generated method stub
        long timeStamp;
        java.util.Date date = new java.util.Date();
        timeStamp = date.getTime();
        String stringTime = String.valueOf(timeStamp);
        Document document = new Document(new com.itextpdf.text.Rectangle(PageSize.A4));
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(MainFrame.projectPath+"/3DNA_OutputPDF" + timeStamp + ".pdf"));
        }catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        catch (DocumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        document.open();
        try {
            document.add(new Paragraph("Barcode generated at: "+(new Timestamp(timeStamp))));
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Barcode128 code128 = new Barcode128();
        code128.setGenerateChecksum(true);
        code128.setCode(stringTime+"Data_"+MainFrame.projectName);

        try {
            document.add(code128.createImageWithBarcode(writer.getDirectContent(), null, null));
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        com.itextpdf.text.Image image1 = null;
        try {
            image1 = com.itextpdf.text.Image.getInstance("sample456.png");
            image1.scaleToFit(100,100);
        } catch (BadElementException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            document.add(new Paragraph("\n\n3DNA -DNA Sequences Data for nano structure "+MainFrame.height+"H x "+MainFrame.width+"H x "+MainFrame.depth*8+"B"));
            document.add(new Paragraph("Number of strands:"+DNASequenceData.size()+"\n"));

            int domainCount=0;
            for (int i = 0; i < DNASequenceData.size(); i++) {
                if(DNASequenceData.get(i).isMapped == true) {
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
            document.add(new Paragraph("Number of nucleotides: "+domainCount*8+"\n\n\n"));
            document.add(image1);
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {

            FontSelector selector = new FontSelector();
            com.itextpdf.text.Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9);
            selector.addFont(f1);
            document.add(new Paragraph("\n"));
            float[] colsWidth = { 1f, 1f, 1f, 3f, 3f, 3f}; // Code 1
            PdfPTable table = new PdfPTable(colsWidth);

            table.setWidthPercentage(90); // Code 2
            table.setHorizontalAlignment(Element.ALIGN_LEFT);//Code 3

            PdfPCell cell;

            table.addCell(selector.process("Strand number"));
            table.addCell(selector.process("Plate number"));
            table.addCell(selector.process("Position"));
            table.addCell(selector.process("DNA Sequences"));
            table.addCell(selector.process("Voxels [x, y, z]"));
            table.addCell(selector.process("Helix and Z-coordinates"));



            for (int i = 0; i < DNASequenceData.size(); i++) {
                //  	System.out.println("printing for"+i);
                if (DNASequenceData.get(i).isMapped) {
                    table.addCell(selector.process(String.valueOf(DNASequenceData.get(i).strandNumber)));
                    table.addCell(selector.process(String.valueOf(DNASequenceData.get(i).plateNumber)));
                    table.addCell(selector.process(String.valueOf(DNASequenceData.get(i).position)));
                    table.addCell(selector.process(DNASequenceData.get(i).getCompleteSequence()));
                    if (DNASequenceData.get(i).x3 == -1)
                        table.addCell(selector.process("[" + DNASequenceData.get(i).x1 + "," +
                                DNASequenceData.get(i).y1 + "," +
                                DNASequenceData.get(i).z1 + "]" +
                                "[" + DNASequenceData.get(i).x2 + "," +
                                DNASequenceData.get(i).y2 + "," +
                                DNASequenceData.get(i).z2 + "]"));
                    else if (DNASequenceData.get(i).x1 == -1)
                        table.addCell(selector.process("[" + DNASequenceData.get(i).x3 + "," +
                                DNASequenceData.get(i).y3 + "" +
                                DNASequenceData.get(i).z3 + "]" +
                                "[" + DNASequenceData.get(i).x4 + "," +
                                DNASequenceData.get(i).y4 + "," +
                                DNASequenceData.get(i).z4 + "]"));
                    else
                        table.addCell(selector.process("[" + DNASequenceData.get(i).x1 + "," +
                                DNASequenceData.get(i).y1 + "," +
                                DNASequenceData.get(i).z1 + "]" +
                                "[" + DNASequenceData.get(i).x2 + "," +
                                DNASequenceData.get(i).y2 + "," +
                                DNASequenceData.get(i).z2 + "]" +
                                "[" + DNASequenceData.get(i).x3 + "," +
                                DNASequenceData.get(i).y3 + "" +
                                DNASequenceData.get(i).z3 + "]" +
                                "[" + DNASequenceData.get(i).x4 + "," +
                                DNASequenceData.get(i).y4 + "," +
                                DNASequenceData.get(i).z4 + "]"));

                    if (DNASequenceData.get(i).x3 == -1)
                        table.addCell(selector.process("[" + DNASequenceData.get(i).helix1 + "," +
                                DNASequenceData.get(i).z1 + "," +
                                DNASequenceData.get(i).helix2 + "," +
                                DNASequenceData.get(i).z2 + "]"));

                    else if (DNASequenceData.get(i).x1 == -1)
                        table.addCell(selector.process("[" + DNASequenceData.get(i).helix3 + "," +
                                DNASequenceData.get(i).z3 + "," +
                                DNASequenceData.get(i).helix4 + "," +
                                DNASequenceData.get(i).z4 + "]"));

                    else
                        table.addCell(selector.process("[" + DNASequenceData.get(i).helix1 + "," +
                                DNASequenceData.get(i).z1 + "," +
                                DNASequenceData.get(i).helix2 + "," +
                                DNASequenceData.get(i).z2 + "," +
                                DNASequenceData.get(i).helix3 + "," +
                                DNASequenceData.get(i).z3 + "," +
                                DNASequenceData.get(i).helix4 + "," +
                                DNASequenceData.get(i).z4 + "]"));
                }
            }

            document.add(table);
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Chunk chunk0=new Chunk(" ");
        try {
            document.add(chunk0);
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            document.add(new Paragraph("PDF Producer: iText 5.4.1 2000-2012 1T3XT BVBA (AGPL-Version)"));
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            document.add(chunk0);
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        try {
            document.add(chunk0);
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            document.add(chunk0);
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            document.add(new Paragraph("Generated using 3DNA (http://guptalab.org/3dna)"));
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        document.close();
        JOptionPane.showMessageDialog(null, "File Saved Successfully !",
                "Success!", JOptionPane.INFORMATION_MESSAGE);


        System.out.println("Document Generated...!!!!!!");

    }

    public void PrintBoundaryData(){
        ArrayList<VoxelToBrick> DNASequenceBoundaryData=SetSequencesFromFile.finalData;
        if(MainFrame.isCSVBoundarySaved==false) {
            c = new ImportSequencesCoordinatesMap();
            DNASequenceBoundaryData = SetSequencesFromFile.finalData;
        }

        // TODO Auto-generated method stub
        long timeStamp;
        java.util.Date date =new java.util.Date();
        timeStamp=date.getTime();
        System.out.println(new Timestamp(timeStamp));
        String stringTime=String.valueOf(timeStamp);
        System.out.println(stringTime);
        Document document = new Document(new com.itextpdf.text.Rectangle(PageSize.A4));
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(MainFrame.projectPath+"/3DNA_OutputPDF" + timeStamp + ".pdf"));
        }catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        catch (DocumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        document.open();
        try {
            document.add(new Paragraph("Barcode generated at: "+(new Timestamp(timeStamp))));
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Barcode128 code128 = new Barcode128();
        code128.setGenerateChecksum(true);
        code128.setCode(stringTime+"Data_");

        try {
            document.add(code128.createImageWithBarcode(writer.getDirectContent(), null, null));
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        com.itextpdf.text.Image image1 = null;
        try {
            image1 = com.itextpdf.text.Image.getInstance("sample456.png");
            image1.scaleToFit(100,100);
        } catch (BadElementException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {

            document.add(new Paragraph("\n\n3DNA -DNA Sequences Data for nano structure "+MainFrame.height+"H x "+MainFrame.width+"H x "+MainFrame.depth*8+"B"));
            document.add(new Paragraph("Number of strands:"+DNASequenceBoundaryData.size()+"\n"));

            int domainCount=0;
            for (int i = 0; i < DNASequenceBoundaryData.size(); i++) {
                if (DNASequenceBoundaryData.get(i).isMapped == true) {
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
            document.add(new Paragraph("Number of nucleotides: "+domainCount*8+"\n\n\n"));
            document.add(image1);
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {

            FontSelector selector = new FontSelector();
            com.itextpdf.text.Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 9);
            selector.addFont(f1);
            document.add(new Paragraph("\n"));
            float[] colsWidth = { 1f, 1f, 1f, 5f, 3f, 3f}; // Code 1
            PdfPTable table = new PdfPTable(colsWidth);

            table.setWidthPercentage(90); // Code 2
            table.setHorizontalAlignment(Element.ALIGN_LEFT);//Code 3

            table.addCell(selector.process("Strand number"));
            table.addCell(selector.process("Plate number"));
            table.addCell(selector.process("Position"));
            table.addCell(selector.process("DNA Sequence"));
            table.addCell(selector.process("Voxels [x, y, z]"));
            table.addCell(selector.process("Helix and Z-coordinates"));


            for (int i = 0; i < DNASequenceBoundaryData.size(); i++) {
                //  	System.out.println("printing for"+i);
                if(DNASequenceBoundaryData.get(i).isMapped == true) {
                    table.addCell(selector.process(String.valueOf(DNASequenceBoundaryData.get(i).strandNumber)));
                    table.addCell(selector.process(String.valueOf(DNASequenceBoundaryData.get(i).plateNumber)));
                    table.addCell(selector.process(String.valueOf(DNASequenceBoundaryData.get(i).position)));
                    table.addCell(selector.process(DNASequenceBoundaryData.get(i).getCompleteSequence()));
                    if (DNASequenceBoundaryData.get(i).x5 != -1) {
                        if (DNASequenceBoundaryData.get(i).x3 == -1)
                            table.addCell(selector.process("[" + DNASequenceBoundaryData.get(i).x1 + "," +
                                    DNASequenceBoundaryData.get(i).y1 + "," +
                                    DNASequenceBoundaryData.get(i).z1 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x2 + "," +
                                    DNASequenceBoundaryData.get(i).y2 + "," +
                                    DNASequenceBoundaryData.get(i).z2 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x5 + "," +
                                    DNASequenceBoundaryData.get(i).y5 + "," +
                                    DNASequenceBoundaryData.get(i).z5 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x6 + "," +
                                    DNASequenceBoundaryData.get(i).y6 + "," +
                                    DNASequenceBoundaryData.get(i).z6 + "]"));

                        else if (DNASequenceBoundaryData.get(i).x1 == -1)
                            table.addCell(selector.process("[" + DNASequenceBoundaryData.get(i).x3 + "," +
                                    DNASequenceBoundaryData.get(i).y3 + "," +
                                    DNASequenceBoundaryData.get(i).z3 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x4 + "," +
                                    DNASequenceBoundaryData.get(i).y4 + "," +
                                    DNASequenceBoundaryData.get(i).z4 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x5 + "," +
                                    DNASequenceBoundaryData.get(i).y5 + "," +
                                    DNASequenceBoundaryData.get(i).z5 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x6 + "," +
                                    DNASequenceBoundaryData.get(i).y6 + "," +
                                    DNASequenceBoundaryData.get(i).z6 + "]"));
                        else if(DNASequenceBoundaryData.get(i).x3 != -1 && DNASequenceBoundaryData.get(i).x1 != -1)
                            table.addCell(selector.process("[" + DNASequenceBoundaryData.get(i).x1 + "," +
                                    DNASequenceBoundaryData.get(i).y1 + "," +
                                    DNASequenceBoundaryData.get(i).z1 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x2 + "," +
                                    DNASequenceBoundaryData.get(i).y2 + "," +
                                    DNASequenceBoundaryData.get(i).z2 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x3 + "," +
                                    DNASequenceBoundaryData.get(i).y3 + "," +
                                    DNASequenceBoundaryData.get(i).z3 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x4 + "," +
                                    DNASequenceBoundaryData.get(i).y4 + "," +
                                    DNASequenceBoundaryData.get(i).z4 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x5 + "," +
                                    DNASequenceBoundaryData.get(i).y5 + "," +
                                    DNASequenceBoundaryData.get(i).z5 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x6 + "," +
                                    DNASequenceBoundaryData.get(i).y6 + "," +
                                    DNASequenceBoundaryData.get(i).z6 + "]"));
                    } else {
                        if (DNASequenceBoundaryData.get(i).x3 == -1)
                            table.addCell(selector.process("[" + DNASequenceBoundaryData.get(i).x1 + "," +
                                            DNASequenceBoundaryData.get(i).y1 + "," +
                                            DNASequenceBoundaryData.get(i).z1 + "]" +
                                            "[" + DNASequenceBoundaryData.get(i).x2 + "," +
                                            DNASequenceBoundaryData.get(i).y2 + "," +
                                            DNASequenceBoundaryData.get(i).z2 + "]"
                            ));

                        else if (DNASequenceBoundaryData.get(i).x1 == -1)
                            table.addCell(selector.process("[" + DNASequenceBoundaryData.get(i).x3 + "," +
                                    DNASequenceBoundaryData.get(i).y3 + "," +
                                    DNASequenceBoundaryData.get(i).z3 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x4 + "," +
                                    DNASequenceBoundaryData.get(i).y4 + "," +
                                    DNASequenceBoundaryData.get(i).z4 + "]"));
                        else if(DNASequenceBoundaryData.get(i).x3 != -1 && DNASequenceBoundaryData.get(i).x1 != -1)
                            table.addCell(selector.process("[" + DNASequenceBoundaryData.get(i).x1 + "," +
                                    DNASequenceBoundaryData.get(i).y1 + "," +
                                    DNASequenceBoundaryData.get(i).z1 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x2 + "," +
                                    DNASequenceBoundaryData.get(i).y2 + "," +
                                    DNASequenceBoundaryData.get(i).z2 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x3 + "," +
                                    DNASequenceBoundaryData.get(i).y3 + "," +
                                    DNASequenceBoundaryData.get(i).z3 + "]" +
                                    "[" + DNASequenceBoundaryData.get(i).x4 + "," +
                                    DNASequenceBoundaryData.get(i).y4 + "," +
                                    DNASequenceBoundaryData.get(i).z4 + "]"));

                    }

                    //adding the helix coordinates
                    if (DNASequenceBoundaryData.get(i).x5 != -1) {
                        if (DNASequenceBoundaryData.get(i).x3 == -1)
                            table.addCell(selector.process("[" + DNASequenceBoundaryData.get(i).helix1 + "," +
                                    DNASequenceBoundaryData.get(i).z1 + "," +
                                    DNASequenceBoundaryData.get(i).helix2 + "," +
                                    DNASequenceBoundaryData.get(i).z2 + "," +
                                    DNASequenceBoundaryData.get(i).helix5 + "," +
                                    DNASequenceBoundaryData.get(i).z5 + "," +
                                    DNASequenceBoundaryData.get(i).helix6 + "," +
                                    DNASequenceBoundaryData.get(i).z6 + "]"));

                        else if (DNASequenceBoundaryData.get(i).x1 == -1)
                            table.addCell(selector.process("[" + DNASequenceBoundaryData.get(i).helix3 + "," +
                                    DNASequenceBoundaryData.get(i).z3 + "," +
                                    DNASequenceBoundaryData.get(i).helix4 + "," +
                                    DNASequenceBoundaryData.get(i).z4 + "," +
                                    DNASequenceBoundaryData.get(i).helix5 + "," +
                                    DNASequenceBoundaryData.get(i).z5 + "," +
                                    DNASequenceBoundaryData.get(i).helix6 + "," +
                                    DNASequenceBoundaryData.get(i).z6 + "]"));
                        else if(DNASequenceBoundaryData.get(i).x3 != -1 && DNASequenceBoundaryData.get(i).x1 != -1)
                            table.addCell(selector.process("[" + DNASequenceBoundaryData.get(i).helix1 + "," +
                                    DNASequenceBoundaryData.get(i).z1 + "," +
                                    DNASequenceBoundaryData.get(i).helix2 + "," +
                                    DNASequenceBoundaryData.get(i).z2 + "," +
                                    DNASequenceBoundaryData.get(i).helix3 + "," +
                                    DNASequenceBoundaryData.get(i).z3 + "," +
                                    DNASequenceBoundaryData.get(i).helix4 + "," +
                                    DNASequenceBoundaryData.get(i).z4 + "," +
                                    DNASequenceBoundaryData.get(i).helix5 + "," +
                                    DNASequenceBoundaryData.get(i).z5 + "," +
                                    DNASequenceBoundaryData.get(i).helix6 + "," +
                                    DNASequenceBoundaryData.get(i).z6 + "]"));
                    } else {
                        if (DNASequenceBoundaryData.get(i).x3 == -1)
                            table.addCell(selector.process("[" + DNASequenceBoundaryData.get(i).helix1 + "," +
                                    DNASequenceBoundaryData.get(i).z1 + "," +
                                    DNASequenceBoundaryData.get(i).helix2 + "," +
                                    DNASequenceBoundaryData.get(i).z2 + "]"));

                        else if (DNASequenceBoundaryData.get(i).x1 == -1)
                            table.addCell(selector.process("[" + DNASequenceBoundaryData.get(i).helix3 + "," +
                                    DNASequenceBoundaryData.get(i).z3 + "," +
                                    DNASequenceBoundaryData.get(i).helix4 + "," +
                                    DNASequenceBoundaryData.get(i).z4 + "]"));

                        else if(DNASequenceBoundaryData.get(i).x3 != -1 && DNASequenceBoundaryData.get(i).x1 != -1)
                            table.addCell(selector.process("[" + DNASequenceBoundaryData.get(i).helix1 + "," +
                                    DNASequenceBoundaryData.get(i).z1 + "," +
                                    DNASequenceBoundaryData.get(i).helix2 + "," +
                                    DNASequenceBoundaryData.get(i).z2 + "," +
                                    DNASequenceBoundaryData.get(i).helix3 + "," +
                                    DNASequenceBoundaryData.get(i).z3 + "," +
                                    DNASequenceBoundaryData.get(i).helix4 + "," +
                                    DNASequenceBoundaryData.get(i).z4 + "]"));

                    }
                }
            }

            document.add(table);
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Chunk chunk0=new Chunk(" ");
        try {
            document.add(chunk0);
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            document.add(new Paragraph("PDF Producer: iText 5.4.1 2000-2012 1T3XT BVBA (AGPL-Version)"));
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            document.add(chunk0);
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        try {
            document.add(chunk0);
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            document.add(chunk0);
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            document.add(new Paragraph("Generated using 3DNA (http://guptalab.org/3dna)"));
        } catch (DocumentException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        document.close();
        JOptionPane.showMessageDialog(null, "File Saved Successfully !",
                "Success!", JOptionPane.INFORMATION_MESSAGE);


        System.out.println("Document Generated...!!!!!!");

    }


}


