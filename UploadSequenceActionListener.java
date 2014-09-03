import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * Created by shikhar.kumar-gupta on 28.08.2014.
 */
public class UploadSequenceActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e){
        final JFileChooser sequenceFileChooser = new JFileChooser();
        sequenceFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = sequenceFileChooser.showOpenDialog(null);

        if(returnValue== JFileChooser.APPROVE_OPTION) {
            File f = sequenceFileChooser.getSelectedFile();
            try {
                System.out.println(f.getName());
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }
}
