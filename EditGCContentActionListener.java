/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class EditGCContentActionListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

        final JFrame dimensionFrame = new JFrame("3DNA GC Content Range");
        dimensionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
        dimensionFrameGridBagConstraints.insets = new Insets(5, 5, 5, 5);

        final JLabel lowerLimitLabel = new JLabel("GC Content Lower Limit (in %)");
        JLabel upperLimitLabel = new JLabel("GC Content Upper Limit (in %)");

        final JTextField lowerLimitTextField = new JTextField(5);
        final JTextField upperLimitTextField = new JTextField(5);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 0;
        mainPanel.add(lowerLimitLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 1;
        mainPanel.add(upperLimitLabel, dimensionFrameGridBagConstraints);


        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 0;
        mainPanel.add(lowerLimitTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 1;
        mainPanel.add(upperLimitTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 3;
        mainPanel.add(okButton, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 2;
        dimensionFrameGridBagConstraints.gridy = 3;
        mainPanel.add(cancelButton, dimensionFrameGridBagConstraints);

        dimensionFrame.add(mainPanel, BorderLayout.CENTER);
        dimensionFrame.pack();
        dimensionFrame.setVisible(true);
        dimensionFrame.setLocation(500, 300);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int l,u;
                l=Integer.parseInt(lowerLimitTextField.getText());
                u=Integer.parseInt(upperLimitTextField.getText());

                if(l>u){
                    JOptionPane optionPane = new JOptionPane("ErrorMsg", JOptionPane.ERROR_MESSAGE);
                    JDialog dialog = optionPane.createDialog("Lower limit must be smaller that the upper limit. Please reenter your values");
                    dialog.setAlwaysOnTop(true);
                    dialog.setVisible(true);
                }
                else if(l==0||u==0||l==100||u==100){
                    JOptionPane optionPane = new JOptionPane("ErrorMsg", JOptionPane.ERROR_MESSAGE);
                    JDialog dialog = optionPane.createDialog("The values are either too big or too small. Please reenter your values");
                    dialog.setAlwaysOnTop(true);
                    dialog.setVisible(true);
                }
                else {
                    System.out.println("the value of isDimensionEntered is true");
                    MainFrame.GClowerLimit = Integer.parseInt(lowerLimitTextField.getText());
                    MainFrame.GCupperLimit = Integer.parseInt(upperLimitTextField.getText());
                    dimensionFrame.dispose();
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dimensionFrame.dispose();
            }
        });
    }

}


