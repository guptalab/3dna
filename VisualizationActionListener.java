import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * Created by foram.joshi on 03.11.2014.
 */
public class VisualizationActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e){
        final JFrame visualizationFrame = new JFrame("Visualization");
        visualizationFrame.setBackground(Color.DARK_GRAY);
        visualizationFrame.setResizable(false);
        visualizationFrame.setIconImage(MainFrame.imag);
        visualizationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
        dimensionFrameGridBagConstraints.insets = new Insets(3, 3, 3, 3);
        JLabel visualizationLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Select Visualization:</h4></html>");
        final JRadioButton fullCanvasVisualizationRadioButton = new JRadioButton("<html><style>h4{color:white;}</style><h4>Full Canvas Visualization</h4></html>");
        fullCanvasVisualizationRadioButton.setBackground(Color.DARK_GRAY);
        final JRadioButton planeVisualizationRadioButton = new JRadioButton("<html><style>h4{color:white;}</style><h4>Plane Visualization</h4></html>");
        planeVisualizationRadioButton.setBackground(Color.DARK_GRAY);
        final JRadioButton elementaryVisualizationRadioButton = new JRadioButton("<html><style>h4{color:white;}</style><h4>Elementary Visualization</h4></html>");
        elementaryVisualizationRadioButton.setBackground(Color.DARK_GRAY);

        ButtonGroup sequencesListGroup = new ButtonGroup();
        sequencesListGroup.add(fullCanvasVisualizationRadioButton);
        sequencesListGroup.add(planeVisualizationRadioButton);
        sequencesListGroup.add(elementaryVisualizationRadioButton);

        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.DARK_GRAY);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 0;
        mainPanel.add(visualizationLabel, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 1;
        mainPanel.add(fullCanvasVisualizationRadioButton, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 2;
        mainPanel.add(planeVisualizationRadioButton, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 3;
        mainPanel.add(elementaryVisualizationRadioButton, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 4;
        mainPanel.add(okButton, dimensionFrameGridBagConstraints);

        visualizationFrame.add(mainPanel, BorderLayout.CENTER);
        visualizationFrame.pack();
        visualizationFrame.setVisible(true);
        visualizationFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 4);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*visualizePlaneLoaderFrame = new JFrame("Animation");
                visualizePlaneLoaderFrame.getContentPane().add(loadingLabel);
                visualizePlaneLoaderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                visualizePlaneLoaderFrame.pack();
                visualizePlaneLoaderFrame.setLocationRelativeTo(null);
                visualizePlaneLoaderFrame.setVisible(true);*/

                if (fullCanvasVisualizationRadioButton.isSelected()) {
                    new VisualizeActionListener();
                } else if (planeVisualizationRadioButton.isSelected()) {
                    new VisualizePlaneActionListener();

                } else if (elementaryVisualizationRadioButton.isSelected()) {
                    // add actionlistener
                    VisualizeArrowsActionListener visualizeArrowsActionListener = new VisualizeArrowsActionListener();

                }
                visualizationFrame.dispose();
            }
        });
        visualizationFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
    public static ImageIcon loading = new ImageIcon("images/loading.gif");
    public static JLabel loadingLabel = new JLabel("loading... ", loading, JLabel.CENTER);
    public static JPanel visualizePlaneLoaderFrameBasePanel;
    public static JFrame visualizePlaneLoaderFrame;
}
