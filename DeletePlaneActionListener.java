import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by shikhar.kumar-gupta on 28.08.2014.
 */
public class DeletePlaneActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Delete plane has been called");
        MainFrame.deleteRowRadioButton.setEnabled(false);
        MainFrame.deletePlaneRadioButton.setEnabled(false);

        final JFrame deletePlaneFrame = new JFrame("Delete Canvas plane");
        ImageIcon img = new ImageIcon("images/logod.png");
        Image imag=img.getImage();
        deletePlaneFrame.setIconImage(imag);
        deletePlaneFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
        dimensionFrameGridBagConstraints.insets = new Insets(5, 5, 5, 5);

        JLabel selectedPlaneLabel = new JLabel("Select Canvas Plane: ");
        String[] planeOptions = {"Select Canvas plane","X Plane","Y Plane","Z Plane"};

        final JComboBox planeListComboBox = new JComboBox(planeOptions);
        planeListComboBox.setSelectedIndex(0);

        JLabel selectedPlaneNumberLabel = new JLabel("Enter plane Co-ordinate");
        final JTextField selectedPlaneNumberTextField= new JTextField(2);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        selectedPlaneLabel.setToolTipText("Enter the canvas plane (X/Y/Z)");
        selectedPlaneNumberLabel.setToolTipText("Enter the plane co-ordinate you want to delete");

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 0;
        mainPanel.add(selectedPlaneLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 0;
        mainPanel.add(planeListComboBox, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 1;
        mainPanel.add(selectedPlaneNumberLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 1;
        mainPanel.add(selectedPlaneNumberTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 2;
        mainPanel.add(cancelButton, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 2;
        mainPanel.add(okButton, dimensionFrameGridBagConstraints);

        deletePlaneFrame.add(mainPanel, BorderLayout.CENTER);
        deletePlaneFrame.pack();
        deletePlaneFrame.setVisible(true);
        deletePlaneFrame.setLocation(500, 300);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedPlaneNumber = selectedPlaneNumberTextField.getText();
                String selectedPlane = (String) planeListComboBox.getSelectedItem();
                selectedPlaneNumberForPlaneDelete = Integer.parseInt(selectedPlaneNumber);
                CanvasActionListener.deletePlane(selectedPlane,selectedPlaneNumberForPlaneDelete);
                deletePlaneFrame.dispose();

            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deletePlaneFrame.dispose();
                MainFrame.deleteRowRadioButton.setEnabled(true);
            }
        });
        deletePlaneFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deletePlaneFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                MainFrame.deleteRowRadioButton.setEnabled(true);
                MainFrame.deletePlaneRadioButton.setEnabled(true);
            }
        });
    }
    public static int selectedPlaneNumberForPlaneDelete;
}
