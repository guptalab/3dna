/** Author: Shikhar K Gupta, Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */

import com.itextpdf.text.pdf.Barcode128;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class SaveLatexActionListener implements ActionListener {

    static ArrayList<VoxelToBrick> DNASequenceData=SaveFinalSequences.FinalData;
    static CoordinatesSeqMap c =new CoordinatesSeqMap();
    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

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

        File dir = new File(MainFrame.projectPath+"/3DNA_LateX_Files");
        dir.mkdirs();
        File teXFile=new File(MainFrame.projectPath+"/3DNA_LateX_Files"+"/"+"3DNA_LateX_File.tex");
       /* Date date=new Date();
        Barcode128 barcode = new Barcode128();
        barcode.setGenerateChecksum(true);
        barcode.setCode(String.valueOf(date.getTime())+ "_3DNA_Data");
        java.awt.Image img = barcode.createAwtImage(Color.BLACK, Color.WHITE);
        BufferedImage outImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        outImage.getGraphics().drawImage(img, 0, 0, null);
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        try {
            ImageIO.write(outImage, "png", bytesOut);
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            bytesOut.flush();
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        byte[] pngImageData = bytesOut.toByteArray();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("3DNA_LateX_Files"+"/"+"3DNA_Barcode"+".png");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            fos.write( pngImageData);
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            fos.flush();
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            fos.close();
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        */
        //begin writing in the file
        try {
            FileWriter fw=new FileWriter(teXFile.getAbsolutePath());
            BufferedWriter bw=new BufferedWriter(fw);
            bw.write("\\documentclass[12pt]{article}\n" +
                    "\\usepackage{pdflscape}\n" +
                    "\\usepackage{caption}\n" +
                    "\\usepackage[pdftex]{graphicx}  \n" +
                    "\\usepackage{rotating}\n" +
                    "\\usepackage{amssymb}\n" +
                    "\\setcounter{tocdepth}{3}\n" +
                    "\\usepackage{graphicx}\n" +
                    "%\\usepackage{subcaption}\n" +
                    "\\usepackage{subfigure}\n" +
                    "\\usepackage{longtable}\n" +
                    "%  #############################\n" +
                    "\\topmargin=-30pt\n" +
                    "\\textheight=648pt\n" +
                    "\\oddsidemargin=0pt\n" +
                    "\\textwidth=468pt\n" +
                    "%  ##############################\n" +
                    "\\pagestyle{plain}\n" +
                    "\\renewcommand{\\baselinestretch}{1.15}\n" +
                    "\\begin{document}\n" +
                    "\n" +
                    "\\begin{landscape}\n" +
                    "\\begin{center}\n" +
                    "{\\bf 3DNA Data}\n" +
                    "\\end{center}\n" +
                    "\\[\n" +
                    "\\begin{array}{|c|c|}\n" +
                    "\\hline\n" +
                    " \\mbox{\\bf Description} & \\mbox{\\bf Value} \\\\\\hline\\hline\n" +
                    "\\mbox{Number of Strands} &"+DNASequenceData.size()+"  \\\\\n" +
                    "\\mbox{3DNA Canvas Dimensions} &"+MainFrame.height+"H X "+MainFrame.width+"H X "+MainFrame.depth*8+"B"+" \\\\\n" +
                    "\\mbox{Number of Nucleotides} &"+domainCount+"  \\\\ \n" +
                    "\\hline\n" +
                    "\\end{array}\n" +
                    "\\]\n" +
                    "\\end{landscape}\n" +
                    "\\begin{landscape}\n" +
                    "\\begin{figure}\n" +
                    "\\centering\n" +
                    "%\\caption{Barcode Identifier}\n" +
                    "\\label{digitalgrid}\n" +
                    "\\end{figure}\n" +
                    "\\end{landscape}\n" +
                            /*"\\newpage\n" +
                            "\\begin{landscape}\n" +
                            "\\begin{figure}\n" +
                            "%\\centering\n" +
                            "\\includegraphics[scale=0.9]{TileView.png}\n" +
                            "%\\caption{Barcode Identifier}\n" +
                            "\\label{tileview}\n" +
                            "\\end{figure}\n" +
                            "\\end{landscape}\n" +*/
                    "{\\small\n" +
                    "\\begin{landscape}\n" +
                    "\n" +
                    "    \\centering\n" +
                    "   \\begin{longtable}{|l|l|l|l|}\n" +
                    " %\\caption{DNA Data}\\\\\n" +
                    "\\hline \\multicolumn{4}{l}{\\textit{\\bf Continued on next page, generate by 3DNA, http://www.guptalab.org/3dna}} \\\\\n" +
                    "\\endfoot\n" +
                    "\\hline\n" +
                    "\\endlastfoot\n" +
                    "\\hline\n" +
                    "   \\mbox{\\bf Seq ID} &\\mbox{\\bf Voxel 1} & \\mbox{\\bf Voxel 2} & \\mbox{\\bf DNA  Sequence Generated} \\\\\\hline\\hline\n" +
                    printSequence()+
                    " \\hline\n" +
                    "    \\end{longtable}\n" +
                    "{\\bf Generated by DNA Pen, http://www.guptalab.org/dnapen}\n" +
                    "\\end{landscape}\n" +
                    "}\n" +
                    "\\end{document}");
            bw.close();
            JOptionPane.showMessageDialog(null, "LateX file successfully generated.",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public String printSequence(){
        String retValue="";
        for (int i=0;i<DNASequenceData.size();i++){
            retValue=retValue+(i+1)+" &  "+DNASequenceData.get(i).voxel1[0]+DNASequenceData.get(i).voxel1[1]
                    +DNASequenceData.get(i).voxel1[2]+"& "+
                    DNASequenceData.get(i).voxel1[0]+DNASequenceData.get(i).voxel1[1]+DNASequenceData.get(i).voxel1[2]
                    +"& "+DNASequenceData.get(i).getCompleteSequence()  +" \\\\ \n";
        }
        return retValue;
    }
}

