import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.*;
import javax.vecmath.Color3f;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by foram.joshi on 13.10.2014.
 */
public class EnableProtectorSequencesActionListener implements ActionListener {
    int x, y, z;
    @Override
    public void actionPerformed(ActionEvent e) {
        final JFrame enableProtectorFrame = new JFrame("Enable Protector Bricks");
        enableProtectorFrame.setBackground(Color.DARK_GRAY);
        ImageIcon img = new ImageIcon("icons/software_logo.png");
        Image imag=img.getImage();
        enableProtectorFrame.setIconImage(imag);
        enableProtectorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
        dimensionFrameGridBagConstraints.insets = new Insets(3, 3, 3, 3);
        JLabel enableProtectorLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Enable Protector Bricks</h4></html>");

        Icon minZProtectorCubeIcon = new ImageIcon("icons/minZProtectorCubeIcon.png");
        JLabel minZProtectorCubeIconLabel = new JLabel("", minZProtectorCubeIcon, JLabel.LEFT);
        final JCheckBox zMinPlaneProtectorCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Min-Z Plane</h4></html>");
        zMinPlaneProtectorCheckbox.setBackground(Color.DARK_GRAY);
        if ( MainFrame.isMinZProtectorPlaneEnabled )
            zMinPlaneProtectorCheckbox.setSelected(true);

        Icon maxZBoundaryCubeIcon = new ImageIcon("icons/maxZProtectorCubeIcon.png");
        JLabel maxZProtectorCubeIconLabel = new JLabel("", maxZBoundaryCubeIcon, JLabel.LEFT);
        final JCheckBox zMaxPlaneProtectorCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Max-Z Plane</h4></html>");
        zMaxPlaneProtectorCheckbox.setBackground(Color.DARK_GRAY);
        if ( MainFrame.isMaxZProtectorPlaneEnabled )
            zMaxPlaneProtectorCheckbox.setSelected(true);

        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.DARK_GRAY);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 0;
        mainPanel.add(enableProtectorLabel, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 1;
        mainPanel.add(zMinPlaneProtectorCheckbox, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 1;
        mainPanel.add(minZProtectorCubeIconLabel, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 2;
        mainPanel.add(zMaxPlaneProtectorCheckbox, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 2;
        mainPanel.add(maxZProtectorCubeIconLabel, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 3;
        mainPanel.add(okButton, dimensionFrameGridBagConstraints);

        enableProtectorFrame.add(mainPanel, BorderLayout.CENTER);
        enableProtectorFrame.pack();
        enableProtectorFrame.setVisible(true);
        enableProtectorFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 4);

        zMaxPlaneProtectorCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (zMaxPlaneProtectorCheckbox.isSelected() == true) {
                    IdentifyBoundaryAndProtectorBricks identifyMaxZProtector = new IdentifyBoundaryAndProtectorBricks('p');
                    Appearance ap = new Appearance();
                    TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.7f);
                    ColoringAttributes coloringAttributes = new ColoringAttributes(new Color3f(1.0f, 1.0f, 0.0f), ColoringAttributes.NICEST);
                    ap.setCapability (Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
                    ap.setTransparencyAttributes( transparencyAttributes );
                    ap.setColoringAttributes(coloringAttributes);
                    for (int j = 0 ; j < CanvasActionListener.colorCubeArrayList.size(); j++) {
                        x = CanvasActionListener.colorCubeArrayList.get(j).xCord;
                        y = CanvasActionListener.colorCubeArrayList.get(j).yCord;
                        z = CanvasActionListener.colorCubeArrayList.get(j).zCord;

                        if (identifyMaxZProtector.voxelList.get(identifyMaxZProtector.ivalue[x][y][z]).isTailBrick) {
                            DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(j).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                        }
                    }
                }
                else{
                    IdentifyBoundaryAndProtectorBricks identifyMaxZProtector = new IdentifyBoundaryAndProtectorBricks('p');
                    Appearance ap = new Appearance();
                    for (int j = 0 ; j < CanvasActionListener.colorCubeArrayList.size(); j++) {
                        x = CanvasActionListener.colorCubeArrayList.get(j).xCord;
                        y = CanvasActionListener.colorCubeArrayList.get(j).yCord;
                        z = CanvasActionListener.colorCubeArrayList.get(j).zCord;

                        if (identifyMaxZProtector.voxelList.get(identifyMaxZProtector.ivalue[x][y][z]).isTailBrick) {
                            DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(j).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                        }
                    }
                }
            }
        });

        zMinPlaneProtectorCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (zMinPlaneProtectorCheckbox.isSelected() == true) {
                    IdentifyBoundaryAndProtectorBricks identifyMinZProtector = new IdentifyBoundaryAndProtectorBricks('p');
                    Appearance ap = new Appearance();
                    TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.7f);
                    ColoringAttributes coloringAttributes = new ColoringAttributes(new Color3f(1.0f, 0.0f, 0.0f), ColoringAttributes.NICEST);
                    ap.setCapability (Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
                    ap.setTransparencyAttributes( transparencyAttributes );
                    ap.setColoringAttributes(coloringAttributes);
                    for (int j = 0 ; j < CanvasActionListener.colorCubeArrayList.size(); j++) {
                        x = CanvasActionListener.colorCubeArrayList.get(j).xCord;
                        y = CanvasActionListener.colorCubeArrayList.get(j).yCord;
                        z = CanvasActionListener.colorCubeArrayList.get(j).zCord;

                        if (identifyMinZProtector.voxelList.get(identifyMinZProtector.ivalue[x][y][z -1]).isHeadBrick) {
                            DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(j).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                        }
                    }
                }
                else{
                    IdentifyBoundaryAndProtectorBricks identifyMinZProtector = new IdentifyBoundaryAndProtectorBricks('p');
                    Appearance ap = new Appearance();
                    for (int j = 0 ; j < CanvasActionListener.colorCubeArrayList.size(); j++) {
                        x = CanvasActionListener.colorCubeArrayList.get(j).xCord;
                        y = CanvasActionListener.colorCubeArrayList.get(j).yCord;
                        z = CanvasActionListener.colorCubeArrayList.get(j).zCord;

                        if (identifyMinZProtector.voxelList.get(identifyMinZProtector.ivalue[x][y][z -1]).isHeadBrick) {
                            DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(j).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                        }
                    }
                }
            }
        });


        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (zMaxPlaneProtectorCheckbox.isSelected() == true) {
                    MainFrame.isMaxZProtectorPlaneEnabled = true;
                    MainFrame.printLog("Protector bricks enabled in Maximum Z ", Color.CYAN);
                }
                else {
                    MainFrame.isMaxZProtectorPlaneEnabled = false;
                }
                if (zMinPlaneProtectorCheckbox.isSelected() == true) {
                    MainFrame.isMinZProtectorPlaneEnabled = true;
                    MainFrame.printLog("Protector bricks enabled in Minimum Z ", Color.CYAN);
                }
                else {
                    MainFrame.isMinZProtectorPlaneEnabled = false;
                }
                MainFrame.isCSVSaved = false;
                MainFrame.isPDFSaved = false;
                MainFrame.isCSVBoundarySaved = false;
                MainFrame.isPDFBoundarySaved = false;
                switchToOriginalColor();
                enableProtectorFrame.dispose();

            }
        });


        enableProtectorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        enableProtectorFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                switchToOriginalColor();
            }
        });
    }

    public void switchToOriginalColor(){
        MainFrame.printLog("swiching back to original color", Color.white);
        Appearance ap = new Appearance();
        for (int z = 0 ; z < CanvasActionListener.colorCubeArrayList.size(); z++) {
            DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(z).canvasDNAColorCube;
            pickedCube.setAppearance(ap);
        }
    }
}


