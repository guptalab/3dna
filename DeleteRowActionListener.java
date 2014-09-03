import javafx.scene.control.ComboBox;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by shikhar.kumar-gupta on 28.08.2014.
 */
public class DeleteRowActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Delete row has been called");
        MainFrame.deletePlaneRadioButton.setEnabled(false);
        MainFrame.deleteRowRadioButton.setEnabled(false);

        final JFrame deleteRowFrame = new JFrame("Delete Canvas row");
        ImageIcon img = new ImageIcon("images/logod.png");
        Image imag=img.getImage();
        deleteRowFrame.setIconImage(imag);
        deleteRowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
        dimensionFrameGridBagConstraints.insets = new Insets(5, 5, 5, 5);

        JLabel selectedPlaneLabel = new JLabel("Select Canvas Plane: ");

        String[] rowOptions = {"Select Canvas Plane","X Plane","Y Plane","Z Plane"};
        final JComboBox planeListComboBox = new JComboBox(rowOptions);
        planeListComboBox.setSelectedIndex(0);

        JLabel selectedPlaneNumberLabel = new JLabel("Enter plane Co-ordinate");
        final JTextField selectedPlaneNumberTextField= new JTextField(2);

        final JLabel selectedRowMinXLabel = new JLabel("X Min");
        final JTextField selectedRowMinXTextField= new JTextField(2);
        final JLabel selectedRowMaxXLabel = new JLabel("X Max");
        final JTextField selectedRowMaxXTextField= new JTextField(2);
        final JLabel selectedRowMinYLabel = new JLabel("Y Min");
        final JTextField selectedRowMinYTextField= new JTextField(2);
        final JLabel selectedRowMaxYLabel = new JLabel("Y Max");
        final JTextField selectedRowMaxYTextField= new JTextField(2);
        final JLabel selectedRowMinZLabel = new JLabel("Z Min");
        final JTextField selectedRowMinZTextField= new JTextField(2);
        final JLabel selectedRowMaxZLabel = new JLabel("Z Max");
        final JTextField selectedRowMaxZTextField= new JTextField(2);

        selectedRowMinXLabel.setEnabled(false);
        selectedRowMinXTextField.setEnabled(false);
        selectedRowMaxXLabel.setEnabled(false);
        selectedRowMaxXTextField.setEnabled(false);
        selectedRowMinYLabel.setEnabled(false);
        selectedRowMinYTextField.setEnabled(false);
        selectedRowMaxYLabel.setEnabled(false);
        selectedRowMaxYTextField.setEnabled(false);
        selectedRowMinZLabel.setEnabled(false);
        selectedRowMinZTextField.setEnabled(false);
        selectedRowMaxZLabel.setEnabled(false);
        selectedRowMaxZTextField.setEnabled(false);

        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        selectedPlaneLabel.setToolTipText("Enter the canvas row (X/Y/Z)");

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
        mainPanel.add(selectedRowMinXLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 2;
        mainPanel.add(selectedRowMinXTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 3;
        mainPanel.add(selectedRowMaxXLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 3;
        mainPanel.add(selectedRowMaxXTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 4;
        mainPanel.add(selectedRowMinYLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 4;
        mainPanel.add(selectedRowMinYTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 5;
        mainPanel.add(selectedRowMaxYLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 5;
        mainPanel.add(selectedRowMaxYTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 6;
        mainPanel.add(selectedRowMinZLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 6;
        mainPanel.add(selectedRowMinZTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 7;
        mainPanel.add(selectedRowMaxZLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 7;
        mainPanel.add(selectedRowMaxZTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 8;
        mainPanel.add(cancelButton, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 8;
        mainPanel.add(okButton, dimensionFrameGridBagConstraints);

        deleteRowFrame.add(mainPanel, BorderLayout.CENTER);
        deleteRowFrame.pack();
        deleteRowFrame.setVisible(true);
        deleteRowFrame.setLocation(500, 300);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedPlane = (String) planeListComboBox.getSelectedItem();
                String selectedPlaneNumber = selectedPlaneNumberTextField.getText();
                selectedPlaneNumberForRowDelete = Integer.parseInt(selectedPlaneNumber);

                if(selectedRowMinXTextField.isEnabled())
                    minX=Integer.parseInt(selectedRowMinXTextField.getText());
                if(selectedRowMinYTextField.isEnabled())
                    minY=Integer.parseInt(selectedRowMinYTextField.getText());
                if(selectedRowMinZTextField.isEnabled())
                    minZ=Integer.parseInt(selectedRowMinZTextField.getText());

                if(selectedRowMaxXTextField.isEnabled())
                    maxX=Integer.parseInt(selectedRowMaxXTextField.getText());
                if(selectedRowMaxYTextField.isEnabled())
                    maxY=Integer.parseInt(selectedRowMaxYTextField.getText());
                if(selectedRowMaxZTextField.isEnabled())
                    maxZ=Integer.parseInt(selectedRowMaxZTextField.getText());

                deleteRowFrame.dispose();

                CanvasActionListener.deleteRow(selectedPlane, selectedPlaneNumberForRowDelete, minX, minY, minZ, maxX, maxY, maxZ);
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRowFrame.dispose();
                MainFrame.deleteRowRadioButton.setEnabled(true);
            }
        });
        deleteRowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteRowFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                MainFrame.deletePlaneRadioButton.setEnabled(true);
                MainFrame.deleteRowRadioButton.setEnabled(true);
            }
        });
        planeListComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox planeListBox = (JComboBox) e.getSource();
                int selectedItemIndex = planeListBox.getSelectedIndex();

                if(selectedItemIndex==1){
                    selectedRowMinXLabel.setEnabled(false);
                    selectedRowMinXTextField.setEnabled(false);
                    selectedRowMaxXLabel.setEnabled(false);
                    selectedRowMaxXTextField.setEnabled(false);
                    selectedRowMinYLabel.setEnabled(true);
                    selectedRowMinYTextField.setEnabled(true);
                    selectedRowMaxYLabel.setEnabled(true);
                    selectedRowMaxYTextField.setEnabled(true);
                    selectedRowMinZLabel.setEnabled(true);
                    selectedRowMinZTextField.setEnabled(true);
                    selectedRowMaxZLabel.setEnabled(true);
                    selectedRowMaxZTextField.setEnabled(true);
                }
                if(selectedItemIndex==2){
                    selectedRowMinXLabel.setEnabled(true);
                    selectedRowMinXTextField.setEnabled(true);
                    selectedRowMaxXLabel.setEnabled(true);
                    selectedRowMaxXTextField.setEnabled(true);
                    selectedRowMinYLabel.setEnabled(false);
                    selectedRowMinYTextField.setEnabled(false);
                    selectedRowMaxYLabel.setEnabled(false);
                    selectedRowMaxYTextField.setEnabled(false);
                    selectedRowMinZLabel.setEnabled(true);
                    selectedRowMinZTextField.setEnabled(true);
                    selectedRowMaxZLabel.setEnabled(true);
                    selectedRowMaxZTextField.setEnabled(true);

                }
                if(selectedItemIndex==3){
                    selectedRowMinXLabel.setEnabled(true);
                    selectedRowMinXTextField.setEnabled(true);
                    selectedRowMaxXLabel.setEnabled(true);
                    selectedRowMaxXTextField.setEnabled(true);
                    selectedRowMinYLabel.setEnabled(true);
                    selectedRowMinYTextField.setEnabled(true);
                    selectedRowMaxYLabel.setEnabled(true);
                    selectedRowMaxYTextField.setEnabled(true);
                    selectedRowMinZLabel.setEnabled(false);
                    selectedRowMinZTextField.setEnabled(false);
                    selectedRowMaxZLabel.setEnabled(false);
                    selectedRowMaxZTextField.setEnabled(false);
                }
            }
        });
    }
    public int selectedPlaneNumberForRowDelete;
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;
}
