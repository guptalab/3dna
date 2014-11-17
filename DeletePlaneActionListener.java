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
        final JFrame deletePlaneFrame = new JFrame("Delete Canvas plane");
        deletePlaneFrame.setBackground(Color.DARK_GRAY);
        ImageIcon img = new ImageIcon("icons/software_logo.png");
        Image imag=img.getImage();
        deletePlaneFrame.setIconImage(imag);
        deletePlaneFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY);

        JLabel selectedPlaneLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Select Canvas Plane:  </h4></html>");
        String[] planeOptions = {"Select","X Plane","Y Plane","Z Plane"};

        final JComboBox planeListComboBox = new JComboBox(planeOptions);
        planeListComboBox.setSelectedIndex(0);

        JLabel selectedPlaneNumberLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Enter plane Co-ordinate</h4></html>");
        final JTextField selectedPlaneNumberTextField= new JTextField(2);

        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.DARK_GRAY);

        selectedPlaneLabel.setToolTipText("<html><style>h4{color:white;}</style><h4>Enter the canvas plane (X/Y/Z)</h4></html>");
        selectedPlaneNumberLabel.setToolTipText("<html><style>h4{color:white;}</style><h4>Enter the plane co-ordinate you want to delete</h4></html>");

        mainPanel.add(selectedPlaneLabel);
        mainPanel.add(Box.createHorizontalStrut(10));
        mainPanel.add(planeListComboBox);
        mainPanel.add(Box.createHorizontalStrut(20));
        mainPanel.add(selectedPlaneNumberLabel);
        mainPanel.add(Box.createHorizontalStrut(10));
        mainPanel.add(selectedPlaneNumberTextField);
        mainPanel.add(Box.createHorizontalStrut(20));
        mainPanel.add(okButton);

        deletePlaneFrame.add(mainPanel, BorderLayout.CENTER);
        deletePlaneFrame.pack();
        deletePlaneFrame.setVisible(true);
        deletePlaneFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 4);

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
        deletePlaneFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        deletePlaneFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
            }
        });
    }
    public static int selectedPlaneNumberForPlaneDelete;
}
