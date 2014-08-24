/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


public class AboutActionListener implements ActionListener {
    String osName = System.getProperty("os.name");

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JDialog jDialog = new JDialog(null,"About 3DNA", JDialog.ModalityType.DOCUMENT_MODAL);
        JButton creditsButton = new JButton("Credits"); // Link to Credits
        JButton homepageButton = new JButton("Homepage"); // Link to Homepage

        creditsButton.setBorder(new LineBorder(Color.BLACK, 2, false));
        creditsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ex) {
                Desktop desktop = Desktop.getDesktop();
                // If the Credits.pdf exists, open it in the default reader.
                try {
                    File tempFile = new File("TempFile");
                    FileWriter fileWriter = new FileWriter(tempFile);
                    fileWriter.write("TempFile");
                    fileWriter.close();

                    String filePath = tempFile.getAbsolutePath();
                    filePath = filePath.substring(0, filePath.indexOf("TempFile"));

                    if (osName.toLowerCase().contains("mac")) {
                        filePath = filePath.concat("DNA Pen.app/Contents/Resources/DNA Pen/docs/Credits.pdf");
                    } else {
                        filePath = filePath.concat("/docs/Credits.pdf");
                    }
                    desktop.open(new File(filePath));
                    tempFile.delete();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace(System.out);
                    JOptionPane.showMessageDialog(null, "Exception occurred.",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                    JOptionPane.showMessageDialog(null, "Exception occurred.",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        homepageButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ex) {
                Desktop desktop = Desktop.getDesktop();
                // Open up the Homepage in the default Web Browser
                try {
                    URI updateLink = new URI("http://www.guptalab.org/3dna/");
                    desktop.browse(updateLink);
                } catch (URISyntaxException e) {
                    e.printStackTrace(System.out);
                    JOptionPane.showMessageDialog(null, "Exception occurred.",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    e.printStackTrace(System.out);
                    JOptionPane.showMessageDialog(null, "Exception occurred.",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        String versionDetails = "<html><center><h1>3DNA</h1> <br/> Version : 1.0<br/>"+
                "Copyright \u00a9 2014 Manish K. Gupta<br>Laboratory of Natural Information Processing DA-IICT,<br> Gandhinagar, Gujarat 382007 <br>http://www.guptalab.org/3dna  </center><p><center>Principle Investigator: Manish K. Gupta<br>Graduate Mentor: Dixita Limbachiya<br>Developers: Shikhar Kumar Gupta, Foram Meghal Joshi<br>Software Logo: Foram Meghal Joshi</center></p></html>";
        String filePath = "";

        // Get the Logo from the Images folder.
        try {
            File tempFile = new File("TempFile");
            FileWriter fileWriter = new FileWriter(tempFile);
            fileWriter.write("TempFile");
            fileWriter.close();
            filePath = tempFile.getAbsolutePath();
            filePath = filePath.substring(0, filePath.indexOf("TempFile"));

            if (osName.toLowerCase().contains("mac")) {
                filePath = filePath.concat("3DNA.app/Contents/Resources/3DNA/images/logod.png");
            } else {
                filePath = filePath.concat("/images/logob.png");
            }
            tempFile.delete();
        } catch (IllegalArgumentException e) {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Exception occurred.",
                    "Error!", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Exception occurred.",
                    "Error!", JOptionPane.INFORMATION_MESSAGE);
        }

        JLabel jLabel = new JLabel(versionDetails, new ImageIcon(filePath), JLabel.CENTER);

        jLabel.setBorder(new BevelBorder(BevelBorder.RAISED));
        jLabel.setOpaque(true);
        jLabel.setBackground(Color.WHITE);

        JPanel jPanel = new JPanel(new GridLayout(1,2));

        creditsButton.setMaximumSize(new Dimension(45, 60));
        creditsButton.setMinimumSize(new Dimension(45, 60));
        creditsButton.setBackground(Color.white);
        creditsButton.setBorder(new BevelBorder(BevelBorder.RAISED));

        homepageButton.setMaximumSize(new Dimension(45, 60));
        homepageButton.setMinimumSize(new Dimension(45, 60));
        homepageButton.setBackground(Color.white);
        homepageButton.setBorder(new BevelBorder(BevelBorder.RAISED));

        jPanel.add(creditsButton);
        jPanel.add(homepageButton);

        Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();

        jDialog.add(jPanel, BorderLayout.SOUTH);
        jDialog.add(jLabel, BorderLayout.CENTER);
        jDialog.setBounds(screenDimensions.width / 3, screenDimensions.height / 3, 500, 400);
        jDialog.setResizable(false);
        jDialog.setVisible(true);
    }
}
