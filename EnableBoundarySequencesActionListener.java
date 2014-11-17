import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by foram.joshi on 13.10.2014.
 */
public class EnableBoundarySequencesActionListener implements ActionListener {
    int[][][] canvasCoordinates = new int[MainFrame.width][MainFrame.height][MainFrame.depth + 1];
    int x, y, z;
    @Override
    public void actionPerformed(ActionEvent e) {
        final JFrame enableBoundaryPlaneFrame = new JFrame("Enable Boundary Bricks");
        enableBoundaryPlaneFrame.setBackground(Color.DARK_GRAY);
        ImageIcon img = new ImageIcon("icons/software_logo.png");
        Image imag=img.getImage();
        enableBoundaryPlaneFrame.setIconImage(imag);
        enableBoundaryPlaneFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
        dimensionFrameGridBagConstraints.insets = new Insets(3, 3, 3, 3);

        JLabel enableBoundaryPlaneLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Select Canvas Plane:  </h4></html>");
        final JCheckBox xMinPlaneBoundaryCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Min-X Plane</h4></html>");
        xMinPlaneBoundaryCheckbox.setBackground(Color.DARK_GRAY);

        Icon minXBoundaryCubeIcon = new ImageIcon("icons/minXBoundaryCubeIcon.png");
        JLabel minXBoundaryCubeIconLabel = new JLabel("", minXBoundaryCubeIcon, JLabel.LEFT);
        if ( MainFrame.isMinXBoundaryPlaneEnabled )
            xMinPlaneBoundaryCheckbox.setSelected(true);

        Icon maxXBoundaryCubeIcon = new ImageIcon("icons/maxXBoundaryCubeIcon.png");
        JLabel maxXBoundaryCubeIconLabel = new JLabel("", maxXBoundaryCubeIcon, JLabel.LEFT);
        final JCheckBox xMaxPlaneBoundaryCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Max-X Plane</h4></html>");
        xMaxPlaneBoundaryCheckbox.setBackground(Color.DARK_GRAY);
        if ( MainFrame.isMaxXBoundaryPlaneEnabled )
            xMaxPlaneBoundaryCheckbox.setSelected(true);

        Icon minYBoundaryCubeIcon = new ImageIcon("icons/minYBoundaryCubeIcon.png");
        JLabel minYBoundaryCubeIconLabel = new JLabel("", minYBoundaryCubeIcon, JLabel.LEFT);
        final JCheckBox yMinPlaneBoundaryCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Min-Y Plane</h4></html>");
        yMinPlaneBoundaryCheckbox.setBackground(Color.DARK_GRAY);
        if ( MainFrame.isMinYBoundaryPlaneEnabled )
            yMinPlaneBoundaryCheckbox.setSelected(true);

        Icon maxYBoundaryCubeIcon = new ImageIcon("icons/maxYBoundaryCubeIcon.png");
        JLabel maxYBoundaryCubeIconLabel = new JLabel("", maxYBoundaryCubeIcon, JLabel.LEFT);
        final JCheckBox yMaxPlaneBoundaryCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Max-Y Plane</h4></html>");
        yMaxPlaneBoundaryCheckbox.setBackground(Color.DARK_GRAY);
        if ( MainFrame.isMaxYBoundaryPlaneEnabled )
            yMaxPlaneBoundaryCheckbox.setSelected(true);


        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.DARK_GRAY);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 0;
        mainPanel.add(enableBoundaryPlaneLabel, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 1;
        mainPanel.add(xMinPlaneBoundaryCheckbox, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 1;
        mainPanel.add(minXBoundaryCubeIconLabel, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 2;
        mainPanel.add(xMaxPlaneBoundaryCheckbox, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 2;
        mainPanel.add(maxXBoundaryCubeIconLabel, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 3;
        mainPanel.add(yMinPlaneBoundaryCheckbox, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 3;
        mainPanel.add(minYBoundaryCubeIconLabel, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 4;
        mainPanel.add(yMaxPlaneBoundaryCheckbox, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 1;
        dimensionFrameGridBagConstraints.gridy = 4;
        mainPanel.add(maxYBoundaryCubeIconLabel, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 5;
        mainPanel.add(okButton, dimensionFrameGridBagConstraints);

        enableBoundaryPlaneFrame.add(mainPanel, BorderLayout.CENTER);
        enableBoundaryPlaneFrame.pack();
        enableBoundaryPlaneFrame.setVisible(true);
        enableBoundaryPlaneFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 4);

        xMinPlaneBoundaryCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (xMinPlaneBoundaryCheckbox.isSelected() == true){
                    IdentifyBoundaryAndProtectorBricks identifyMinXBoundary = new IdentifyBoundaryAndProtectorBricks('b');
                    Appearance ap = new Appearance();
                    TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.7f);
                    ColoringAttributes coloringAttributes = new ColoringAttributes(1.0f, 1.0f, 0.0f, ColoringAttributes.NICEST);
                    ap.setTransparencyAttributes( transparencyAttributes );
                    ap.setColoringAttributes(coloringAttributes);
                    for (int j = 0 ; j < CanvasActionListener.colorCubeArrayList.size(); j++) {
                        x = CanvasActionListener.colorCubeArrayList.get(j).xCord;
                        y = CanvasActionListener.colorCubeArrayList.get(j).yCord;
                        z = CanvasActionListener.colorCubeArrayList.get(j).zCord;

                        if (identifyMinXBoundary.newIndex[x][y][z]!=-1 && identifyMinXBoundary.boundaryBrickList.get(identifyMinXBoundary.newIndex[x][y][z]).isLeftmostHalfBrick) {
                            DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(j).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                        }
                    }
                }
                else{
                    IdentifyBoundaryAndProtectorBricks identifyMinXBoundary = new IdentifyBoundaryAndProtectorBricks('b');
                    Appearance ap = new Appearance();
                    for (int j = 0 ; j < CanvasActionListener.colorCubeArrayList.size(); j++) {
                        x = CanvasActionListener.colorCubeArrayList.get(j).xCord;
                        y = CanvasActionListener.colorCubeArrayList.get(j).yCord;
                        z = CanvasActionListener.colorCubeArrayList.get(j).zCord;

                        if (identifyMinXBoundary.newIndex[x][y][z]!=-1 && identifyMinXBoundary.boundaryBrickList.get(identifyMinXBoundary.newIndex[x][y][z]).isLeftmostHalfBrick) {
                            DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(j).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                        }
                    }
                }
            }
        });

        xMaxPlaneBoundaryCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (xMaxPlaneBoundaryCheckbox.isSelected() == true){
                    IdentifyBoundaryAndProtectorBricks identifyMaxXBoundary = new IdentifyBoundaryAndProtectorBricks('b');
                    Appearance ap = new Appearance();
                    TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.7f);
                    ColoringAttributes coloringAttributes = new ColoringAttributes(1.0f, 1.0f, 0.0f, ColoringAttributes.NICEST);
                    ap.setTransparencyAttributes( transparencyAttributes );
                    ap.setColoringAttributes(coloringAttributes);
                    for (int j = 0 ; j < CanvasActionListener.colorCubeArrayList.size(); j++) {
                        x = CanvasActionListener.colorCubeArrayList.get(j).xCord;
                        y = CanvasActionListener.colorCubeArrayList.get(j).yCord;
                        z = CanvasActionListener.colorCubeArrayList.get(j).zCord;

                        if (identifyMaxXBoundary.newIndex[x][y][z]!=-1 && identifyMaxXBoundary.boundaryBrickList.get(identifyMaxXBoundary.newIndex[x][y][z]).isRightmostHalfBrick) {
                            DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(j).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                        }
                    }
                }
                else{
                    IdentifyBoundaryAndProtectorBricks identifyMaxXBoundary = new IdentifyBoundaryAndProtectorBricks('b');
                    Appearance ap = new Appearance();
                    for (int j = 0 ; j < CanvasActionListener.colorCubeArrayList.size(); j++) {
                        x = CanvasActionListener.colorCubeArrayList.get(j).xCord;
                        y = CanvasActionListener.colorCubeArrayList.get(j).yCord;
                        z = CanvasActionListener.colorCubeArrayList.get(j).zCord;

                        if (identifyMaxXBoundary.newIndex[x][y][z]!=-1 && identifyMaxXBoundary.boundaryBrickList.get(identifyMaxXBoundary.newIndex[x][y][z]).isRightmostHalfBrick) {
                            DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(j).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                        }
                    }
                }
            }
        });

        yMinPlaneBoundaryCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (yMinPlaneBoundaryCheckbox.isSelected() == true){
                    IdentifyBoundaryAndProtectorBricks identifyMinYBoundary = new IdentifyBoundaryAndProtectorBricks('b');
                    Appearance ap = new Appearance();
                    TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.7f);
                    ColoringAttributes coloringAttributes = new ColoringAttributes(1.0f, 1.0f, 0.0f, ColoringAttributes.NICEST);
                    ap.setTransparencyAttributes( transparencyAttributes );
                    ap.setColoringAttributes(coloringAttributes);
                    for (int j = 0 ; j < CanvasActionListener.colorCubeArrayList.size(); j++) {
                        x = CanvasActionListener.colorCubeArrayList.get(j).xCord;
                        y = CanvasActionListener.colorCubeArrayList.get(j).yCord;
                        z = CanvasActionListener.colorCubeArrayList.get(j).zCord;

                        if (identifyMinYBoundary.newIndex[x][y][z]!=-1 && identifyMinYBoundary.boundaryBrickList.get(identifyMinYBoundary.newIndex[x][y][z]).isTopmostHalfBrick) {
                            DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(j).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                        }
                    }
                }
                else{
                    IdentifyBoundaryAndProtectorBricks identifyMinYBoundary = new IdentifyBoundaryAndProtectorBricks('b');
                    Appearance ap = new Appearance();
                    for (int j = 0 ; j < CanvasActionListener.colorCubeArrayList.size(); j++) {
                        x = CanvasActionListener.colorCubeArrayList.get(j).xCord;
                        y = CanvasActionListener.colorCubeArrayList.get(j).yCord;
                        z = CanvasActionListener.colorCubeArrayList.get(j).zCord;

                        if (identifyMinYBoundary.newIndex[x][y][z]!=-1 && identifyMinYBoundary.boundaryBrickList.get(identifyMinYBoundary.newIndex[x][y][z]).isTopmostHalfBrick) {
                            DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(j).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                        }
                    }
                }

            }
        });

        yMaxPlaneBoundaryCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (yMaxPlaneBoundaryCheckbox.isSelected() == true){
                    IdentifyBoundaryAndProtectorBricks identifyMaxYBoundary = new IdentifyBoundaryAndProtectorBricks('b');
                    Appearance ap = new Appearance();
                    TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.7f);
                    ColoringAttributes coloringAttributes = new ColoringAttributes(1.0f, 1.0f, 0.0f, ColoringAttributes.NICEST);
                    ap.setTransparencyAttributes( transparencyAttributes );
                    ap.setColoringAttributes(coloringAttributes);
                    for (int j = 0 ; j < CanvasActionListener.colorCubeArrayList.size(); j++) {
                        x = CanvasActionListener.colorCubeArrayList.get(j).xCord;
                        y = CanvasActionListener.colorCubeArrayList.get(j).yCord;
                        z = CanvasActionListener.colorCubeArrayList.get(j).zCord;

                        if (identifyMaxYBoundary.newIndex[x][y][z]!=-1 && identifyMaxYBoundary.boundaryBrickList.get(identifyMaxYBoundary.newIndex[x][y][z]).isBottommostHalfBrick) {
                            DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(j).canvasDNAColorCube;
                            pickedCube.setAppearance(ap);
                        }
                    }
                }
                else {
                    IdentifyBoundaryAndProtectorBricks identifyMaxYBoundary = new IdentifyBoundaryAndProtectorBricks('b');
                    Appearance ap = new Appearance();
                    for (int j = 0 ; j < CanvasActionListener.colorCubeArrayList.size(); j++) {
                        x = CanvasActionListener.colorCubeArrayList.get(j).xCord;
                        y = CanvasActionListener.colorCubeArrayList.get(j).yCord;
                        z = CanvasActionListener.colorCubeArrayList.get(j).zCord;

                        if (identifyMaxYBoundary.newIndex[x][y][z]!=-1 && identifyMaxYBoundary.boundaryBrickList.get(identifyMaxYBoundary.newIndex[x][y][z]).isBottommostHalfBrick) {
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
                        if (xMinPlaneBoundaryCheckbox.isSelected() == true) {
                            MainFrame.isMinXBoundaryPlaneEnabled = true;
                            MainFrame.printLog("minimum X boundary plane enabled", Color.cyan);
                        }
                        else {
                            MainFrame.isMinXBoundaryPlaneEnabled = false;
                        }

                        if (xMaxPlaneBoundaryCheckbox.isSelected() == true) {
                            MainFrame.isMaxXBoundaryPlaneEnabled = true;
                            MainFrame.printLog("maximum X boundary plane enabled", Color.cyan);
                        }
                        else {
                            MainFrame.isMaxXBoundaryPlaneEnabled = false;
                        }
                        if (yMinPlaneBoundaryCheckbox.isSelected() == true) {
                            MainFrame.isMinYBoundaryPlaneEnabled = true;
                            MainFrame.printLog("minimum Y boundary plane enabled", Color.cyan);

                        }
                        else {
                            MainFrame.isMinYBoundaryPlaneEnabled = false;
                        }

                        if (yMaxPlaneBoundaryCheckbox.isSelected() == true) {
                            MainFrame.isMaxYBoundaryPlaneEnabled = true;
                            MainFrame.printLog("maximum Y boundary plane enabled", Color.cyan);
                        }
                        else {
                            MainFrame.isMaxYBoundaryPlaneEnabled = false;
                        }
                        MainFrame.isCSVSaved = false;
                        MainFrame.isPDFSaved = false;
                        MainFrame.isCSVBoundarySaved = false;
                        MainFrame.isPDFBoundarySaved = false;

                switchToOriginalColor();
                enableBoundaryPlaneFrame.dispose();

            }
        });
        enableBoundaryPlaneFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        enableBoundaryPlaneFrame.addWindowListener(new WindowAdapter() {
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
