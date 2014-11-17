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
        final JFrame deleteRowFrame = new JFrame("Delete Canvas row");
        deleteRowFrame.setBackground(Color.DARK_GRAY);
        ImageIcon img = new ImageIcon("icons/software_logo.png");
        Image imag=img.getImage();
        deleteRowFrame.setIconImage(imag);
        deleteRowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
        dimensionFrameGridBagConstraints.insets = new Insets(3, 3, 3, 3);

        JLabel selectedAxisLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Select Axis:</h4></html>");

        String[] rowOptions = {"Select","Along X-axis","Along Y-axis","Along Z-axis"};
        final JComboBox axisListComboBox = new JComboBox(rowOptions);
        axisListComboBox.setSelectedIndex(0);

        final JLabel selectXLabel = new JLabel("<html><style>h4{color:white;}</style><h4>X Co-ordinate</h4></html>");
        final JTextField selectXTextField= new JTextField(2);
        final JLabel selectYLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Y Co-ordinate</h4></html>");
        final JTextField selectYTextField= new JTextField(2);
        final JLabel selectZLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Z Co-ordinate</h4></html>");
        final JTextField selectZTextField= new JTextField(2);

        final JLabel selectedRowMinXLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Min X</h4></html>");
        final JTextField selectedRowMinXTextField= new JTextField(2);
        final JLabel selectedRowMaxXLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Max X</h4></html>");
        final JTextField selectedRowMaxXTextField= new JTextField(2);
        final JLabel selectedRowMinYLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Min Y</h4></html>");
        final JTextField selectedRowMinYTextField= new JTextField(2);
        final JLabel selectedRowMaxYLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Max Y</h4></html>");
        final JTextField selectedRowMaxYTextField= new JTextField(2);
        final JLabel selectedRowMinZLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Min Z</h4></html>");
        final JTextField selectedRowMinZTextField= new JTextField(2);
        final JLabel selectedRowMaxZLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Max Z</h4></html>");
        final JTextField selectedRowMaxZTextField= new JTextField(2);

        selectXLabel.setEnabled(false);
        selectXTextField.setEnabled(false);
        selectYLabel.setEnabled(false);
        selectYTextField.setEnabled(false);
        selectZLabel.setEnabled(false);
        selectZTextField.setEnabled(false);
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
        okButton.setBackground(Color.DARK_GRAY);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.DARK_GRAY);

        selectedAxisLabel.setToolTipText("Enter the canvas row (X/Y/Z)");

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 0;
        mainPanel.add(selectedAxisLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 0;
        mainPanel.add(axisListComboBox, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 1;
        mainPanel.add(selectXLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 1;
        mainPanel.add(selectXTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 2;
        mainPanel.add(selectYLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 2;
        mainPanel.add(selectYTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 3;
        mainPanel.add(selectZLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 3;
        mainPanel.add(selectZTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 4;
        mainPanel.add(selectedRowMinXLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 4;
        mainPanel.add(selectedRowMinXTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 5;
        mainPanel.add(selectedRowMaxXLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 5;
        mainPanel.add(selectedRowMaxXTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 6;
        mainPanel.add(selectedRowMinYLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 6;
        mainPanel.add(selectedRowMinYTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 7;
        mainPanel.add(selectedRowMaxYLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 7;
        mainPanel.add(selectedRowMaxYTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 8;
        mainPanel.add(selectedRowMinZLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 8;
        mainPanel.add(selectedRowMinZTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 9;
        mainPanel.add(selectedRowMaxZLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 9;
        mainPanel.add(selectedRowMaxZTextField, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 10;
        mainPanel.add(cancelButton, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 10;
        mainPanel.add(okButton, dimensionFrameGridBagConstraints);

        deleteRowFrame.add(mainPanel, BorderLayout.CENTER);
        deleteRowFrame.pack();
        deleteRowFrame.setVisible(true);
        deleteRowFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 4);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isError = false;
                if(selectedRowMinXTextField.isEnabled()) {
                    if(selectedRowMinXTextField.getText().equals("")) {
                        isError = true;
                        errorMissing[0] = new String("minimum X\n");
                    }
                    else {
                        minX = Integer.parseInt(selectedRowMinXTextField.getText());
                        if(minX < 0){
                            errorValue[0] = new String("minimum X can not be less than 0");
                            isError = true;
                        }
                        if(minX >= MainFrame.width){
                            errorValue[0] = new String("minimum X can not be greater than "+(MainFrame.width - 1));
                            isError = true;
                        }
                    }
                    if(selectedRowMaxXTextField.getText().equals("")) {
                        isError = true;
                        errorMissing[1] = new String("Please enter a value for maximum X\n");
                    }
                    else {
                        maxX = Integer.parseInt(selectedRowMaxXTextField.getText());
                        if (maxX < 0) {
                            errorValue[0] = new String("maximum X can not be less than 0");
                            isError = true;
                        }
                        if (maxX >= MainFrame.width) {
                            errorValue[0] = new String("maximum X can not be greater than " + (MainFrame.width - 1));
                            isError = true;
                        }
                        if (maxX < minX){
                            errorValue[0] = new String("maximum X can not be less than minimum X");
                            isError = true;
                        }
                    }

                }
                if(selectedRowMinYTextField.isEnabled()) {
                    if(selectedRowMinYTextField.getText().equals("")) {
                        isError = true;
                        errorMissing[2] = new String("minimum Y\n");
                    }
                    else {
                        minY = Integer.parseInt(selectedRowMinYTextField.getText());
                        if(minY < 0){
                            errorValue[0] = new String("minimum Y can not be less than 0");
                            isError = true;
                        }
                        if(minY >= MainFrame.height){
                            errorValue[0] = new String("minimum Y can not be greater than "+(MainFrame.height - 1));
                            isError = true;
                        }
                    }
                    if(selectedRowMaxYTextField.getText().equals("")) {
                        isError = true;
                        errorMissing[3] = new String("maximum Y\n");
                    }
                    else {
                        maxY = Integer.parseInt(selectedRowMaxYTextField.getText());
                        if (maxY < 0) {
                            errorValue[0] = new String("maximum Y can not be less than 0");
                            isError = true;
                        }
                        if (maxY >= MainFrame.height) {
                            errorValue[0] = new String("maximum Y can not be greater than " + (MainFrame.height - 1));
                            isError = true;
                        }
                        if (maxY < minY){
                            errorValue[0] = new String("maximum Y can not be less than minimum Y");
                            isError = true;
                        }
                    }
                }
                if(selectedRowMinZTextField.isEnabled()) {
                    if(selectedRowMinZTextField.getText().equals("")) {
                        isError = true;
                        errorMissing[4] = new String("min Z\n");
                    }
                    else  {
                        minZ = Integer.parseInt(selectedRowMinZTextField.getText());
                        if(minZ < 1){
                            errorValue[0] = new String("minimum Z can not be less than 1");
                            isError = true;
                        }
                        if(minZ > MainFrame.depth){
                            errorValue[0] = new String("minimum Z can not be greater than "+(MainFrame.depth));
                            isError = true;
                        }
                    }
                    if(selectedRowMaxZTextField.getText().equals("")) {
                        isError = true;
                        errorMissing[3] = new String("maximum Z\n");
                    }
                    else {
                        maxZ = Integer.parseInt(selectedRowMaxZTextField.getText());
                        if (maxZ < 1) {
                            errorValue[0] = new String("maximum Z can not be less than 1");
                            isError = true;
                        }
                        if (maxZ > MainFrame.depth) {
                            errorValue[0] = new String("maximum Z can not be greater than " + (MainFrame.depth));
                            isError = true;
                        }
                        if (maxZ < minZ){
                            errorValue[0] = new String("maximum Z can not be less than minimum Z");
                            isError = true;
                        }
                    }
                }

                if(selectXTextField.isEnabled()) {
                    if(selectXTextField.getText().equals("")) {
                        isError = true;
                        errorMissing[0] = new String("X Co-ordinate \n");
                        errorMissing[1] = new String("");
                        errorMissing[2] = new String("");
                        errorMissing[3] = new String("");
                        errorMissing[4] = new String("");
                        errorMissing[5] = new String("");
                    }
                    else {
                        constantX = Integer.parseInt(selectXTextField.getText());

                    }

                }
                if(selectYTextField.isEnabled()) {
                    if(selectYTextField.getText().equals("")) {
                        isError = true;
                        errorMissing[1] = new String("Y Co-ordinate \n");
                        errorMissing[2] = new String("");
                        errorMissing[3] = new String("");
                        errorMissing[4] = new String("");
                        errorMissing[5] = new String("");
                    }
                    else
                        constantY = Integer.parseInt(selectYTextField.getText());
                }
                if(selectZTextField.isEnabled()) {
                    if(selectZTextField.getText().equals("")) {
                        isError = true;
                        errorMissing[2] = new String("Z Co-ordinate \n");
                        errorMissing[3] = new String("");
                        errorMissing[4] = new String("");
                        errorMissing[5] = new String("");
                    }
                    else
                        constantZ = Integer.parseInt(selectZTextField.getText());
                }

                if(axisListComboBox.getSelectedIndex() == 0) {
                    isError = true;
                    errorMissing[0] = new String("Axis \n");
                    errorMissing[1] = new String("");
                    errorMissing[2] = new String("");
                    errorMissing[3] = new String("");
                    errorMissing[4] = new String("");
                    errorMissing[5] = new String("");
                }
                else {
                    selectedAxis = (String) axisListComboBox.getSelectedItem();
                }

                if(isError == true){
                    errorMessage(errorMissing, errorValue);
                }
                else {
                    CanvasActionListener.deleteRow(selectedAxis, constantX, constantY, constantZ, minX, minY, minZ, maxX, maxY, maxZ);
                    deleteRowFrame.dispose();
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRowFrame.dispose();
                MainFrame.deleteRowColoumnButton.setEnabled(true);
            }
        });
        deleteRowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deleteRowFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
            }
        });
        axisListComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox axisListBox = (JComboBox) e.getSource();
                int selectedItemIndex = axisListBox.getSelectedIndex();

                if(selectedItemIndex==1){
                    selectXLabel.setEnabled(false);
                    selectXTextField.setEnabled(false);
                    selectYLabel.setEnabled(true);
                    selectYTextField.setEnabled(true);
                    selectZLabel.setEnabled(true);
                    selectZTextField.setEnabled(true);
                    selectedRowMinXLabel.setEnabled(true);
                    selectedRowMinXTextField.setEnabled(true);
                    selectedRowMaxXLabel.setEnabled(true);
                    selectedRowMaxXTextField.setEnabled(true);
                    selectedRowMinYLabel.setEnabled(false);
                    selectedRowMinYTextField.setEnabled(false);
                    selectedRowMaxYLabel.setEnabled(false);
                    selectedRowMaxYTextField.setEnabled(false);
                    selectedRowMinZLabel.setEnabled(false);
                    selectedRowMinZTextField.setEnabled(false);
                    selectedRowMaxZLabel.setEnabled(false);
                    selectedRowMaxZTextField.setEnabled(false);
                }
                if(selectedItemIndex==2){
                    selectXLabel.setEnabled(true);
                    selectXTextField.setEnabled(true);
                    selectYLabel.setEnabled(false);
                    selectYTextField.setEnabled(false);
                    selectZLabel.setEnabled(true);
                    selectZTextField.setEnabled(true);
                    selectedRowMinXLabel.setEnabled(false);
                    selectedRowMinXTextField.setEnabled(false);
                    selectedRowMaxXLabel.setEnabled(false);
                    selectedRowMaxXTextField.setEnabled(false);
                    selectedRowMinYLabel.setEnabled(true);
                    selectedRowMinYTextField.setEnabled(true);
                    selectedRowMaxYLabel.setEnabled(true);
                    selectedRowMaxYTextField.setEnabled(true);
                    selectedRowMinZLabel.setEnabled(false);
                    selectedRowMinZTextField.setEnabled(false);
                    selectedRowMaxZLabel.setEnabled(false);
                    selectedRowMaxZTextField.setEnabled(false);

                }
                if(selectedItemIndex==3){
                    selectXLabel.setEnabled(true);
                    selectXTextField.setEnabled(true);
                    selectYLabel.setEnabled(true);
                    selectYTextField.setEnabled(true);
                    selectZLabel.setEnabled(false);
                    selectZTextField.setEnabled(false);
                    selectedRowMinXLabel.setEnabled(false);
                    selectedRowMinXTextField.setEnabled(false);
                    selectedRowMaxXLabel.setEnabled(false);
                    selectedRowMaxXTextField.setEnabled(false);
                    selectedRowMinYLabel.setEnabled(false);
                    selectedRowMinYTextField.setEnabled(false);
                    selectedRowMaxYLabel.setEnabled(false);
                    selectedRowMaxYTextField.setEnabled(false);
                    selectedRowMinZLabel.setEnabled(true);
                    selectedRowMinZTextField.setEnabled(true);
                    selectedRowMaxZLabel.setEnabled(true);
                    selectedRowMaxZTextField.setEnabled(true);
                }
            }
        });
    }
    public void errorMessage(String[] message1, String[] message2){

        final JFrame errorFrame = new JFrame("Error");
        errorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        errorFrame.setBackground(Color.DARK_GRAY);
//        errorFrame.setIconImage(MainFrame.imag);

        JPanel errorPanel = new JPanel();
        errorPanel.setLayout(new GridBagLayout());
        GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
        dimensionFrameGridBagConstraints.insets = new Insets(3, 3, 3, 3);

        errorPanel.setBackground(Color.DARK_GRAY);

        final JLabel errorMissingLabel = new JLabel();
        errorMissingLabel.setText(String.format("<html><style>h4{color:white;}</style><h4>Please enter value for: <br> %s <br>%s <br>%s <br>%s <br>%s</h4></html>", message1[0], message1[1], message1[2], message1[3], message1[4]));
        final JLabel errorValueLabel = new JLabel();
        errorValueLabel.setText(String.format("<html><style>h4{color:white;}</style><h4> %s </h4></html>", message2[0]));
        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.DARK_GRAY);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 0;
        errorPanel.add(errorMissingLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 1;
        errorPanel.add(errorValueLabel, dimensionFrameGridBagConstraints);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 2;
        errorPanel.add(okButton, dimensionFrameGridBagConstraints);

        errorFrame.add(errorPanel, BorderLayout.CENTER);
        errorFrame.pack();
        errorFrame.setVisible(true);
        errorFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 4);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isError = false;
                errorMissing[0] = new String("");
                errorMissing[1] = new String("");
                errorMissing[2] = new String("");
                errorMissing[3] = new String("");
                errorMissing[4] = new String("");
                errorMissing[5] = new String("");
                errorFrame.dispose();
            }
        });

    }
    public String selectedAxis;
    public boolean isError;
    public String[] errorMissing = new String[6];
    public String[] errorValue = new String[6];
    public int constantX, constantY, constantZ;
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;
}
