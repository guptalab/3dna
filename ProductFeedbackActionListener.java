/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */


import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JOptionPane;

public class ProductFeedbackActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Desktop desktop = Desktop.getDesktop();
        try {
            URI updateLink = new URI("https://docs.google.com/forms/d/1YGu_I9z7Z56oAP1enGByBahqs-ItHbLqnBwCoJouOro/viewform");
            desktop.browse(updateLink);
        } catch (URISyntaxException e1) {
            e1.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Exception occurred.",
                    "Error!", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e2) {
            e2.printStackTrace(System.out);
            JOptionPane.showMessageDialog(null, "Exception occurred.",
                    "Error!", JOptionPane.INFORMATION_MESSAGE);
        }
	}

}
