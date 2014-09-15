/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class ExportActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        File file = new File(MainFrame.projectPath+"/"+MainFrame.projectName);
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(file.getAbsolutePath() + "3DNA_Export.3dna"));
        }
        catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        try {
            bufferedWriter.write(String.valueOf(MainFrame.height));
            bufferedWriter.write("\n");
            bufferedWriter.write(String.valueOf(MainFrame.width));
            bufferedWriter.write("\n");
            bufferedWriter.write(String.valueOf(MainFrame.depth));
            bufferedWriter.write("\n");
            int h = 0;
            for(int i=0;i<MainFrame.height;i++){
                for(int j=0;j<MainFrame.width;j++){
                    for(int k=0;k<MainFrame.depth;k++){
                        if(MainFrame.deletedCoordinates[i][j][k]==false) {
                            bufferedWriter.write(String.valueOf(i));
                            bufferedWriter.write("\n");
                            bufferedWriter.write(String.valueOf(j));
                            bufferedWriter.write("\n");
                            bufferedWriter.write(String.valueOf(k));
                            bufferedWriter.write("\n");
                        }
                    }
                }
            }
            bufferedWriter.close();
            JOptionPane.showMessageDialog(null, "File Saved Successfully !",
                    "Success!", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }


        System.out.println("csv file created");

    }

}
