import com.sun.j3d.utils.geometry.ColorCube;

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
public class EnableCrystalActionListener implements ActionListener {
    public static int returnValue;
    public static int crystalType;
    @Override
    public void actionPerformed(ActionEvent e) {
        final JFrame enableCrystalFrame = new JFrame("Enable Crystals");
        enableCrystalFrame.setBackground(Color.DARK_GRAY);
        enableCrystalFrame.setResizable(false);
        enableCrystalFrame.setIconImage(MainFrame.imag);
        enableCrystalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
        dimensionFrameGridBagConstraints.insets = new Insets(3, 3, 3, 3);
        JLabel enableCrystalLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Enable Crystals</h4></html>");
        final JCheckBox zCrystalCheckBox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Z-Crystal</h4></html>");
        zCrystalCheckBox.setBackground(Color.DARK_GRAY);
        if (MainFrame.isZCrystalEnabled)
            zCrystalCheckBox.setSelected(true);
        final JCheckBox xCrystalCheckBox = new JCheckBox("<html><style>h4{color:white;}</style><h4>X-Crystal</h4></html>");
        xCrystalCheckBox.setBackground(Color.DARK_GRAY);
        if (MainFrame.isXCrystalEnabled)
            xCrystalCheckBox.setSelected(true);
        final JCheckBox yCrystalCheckBox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Y-Crystal</h4></html>");
        yCrystalCheckBox.setBackground(Color.DARK_GRAY);
        if (MainFrame.isYCrystalEnabled)
            yCrystalCheckBox.setSelected(true);

        JButton okButton = new JButton("OK");
        okButton.setBackground(Color.DARK_GRAY);

        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 0;
        mainPanel.add(enableCrystalLabel, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 1;
        mainPanel.add(xCrystalCheckBox, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 2;
        mainPanel.add(yCrystalCheckBox, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 3;
        mainPanel.add(zCrystalCheckBox, dimensionFrameGridBagConstraints);
        dimensionFrameGridBagConstraints.gridx = 0;
        dimensionFrameGridBagConstraints.gridy = 4;
        mainPanel.add(okButton, dimensionFrameGridBagConstraints);

        enableCrystalFrame.add(mainPanel, BorderLayout.CENTER);
        enableCrystalFrame.pack();
        enableCrystalFrame.setVisible(true);
        enableCrystalFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 4);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (zCrystalCheckBox.isSelected() == true) {
                    crystalType = 1;
                    isCrystalStructure();
                    if (returnValue == 1) { //entire plane not matching
                        MainFrame.isZCrystalEnabled = false;
                        MainFrame.isMaxZProtectorPlaneEnabled = false;
                        MainFrame.isMinZProtectorPlaneEnabled = false;
                        zCrystalCheckBox.setSelected(false);
                        MainFrame.printLog("Z-crystal, Minimum Z Protector Bricks, Maximum Z Protector Bricks not enabled ", Color.WHITE);
                    } else if (returnValue == 2) { //some voxels not matching

                        final JFrame crystalErrorFrame = new JFrame("Output success");
                        crystalErrorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        crystalErrorFrame.setBackground(Color.DARK_GRAY);
                        crystalErrorFrame.setIconImage(MainFrame.imag);

                        crystalErrorFrame.setLayout(new GridBagLayout());
                        GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
                        dimensionFrameGridBagConstraints.insets = new Insets(3, 3, 3, 3);


                        JPanel crystalErrorPanel = new JPanel();
                        crystalErrorPanel.setBackground(Color.DARK_GRAY);

                        final JLabel crystalErrorLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Z-Crystal can not be constructed properly. <br> The highlighted voxels can not be combined to form a crystal. <br> Build crystal anyway?</h4></html>");
                        JButton okButton = new JButton("Continue");
                        okButton.setBackground(Color.DARK_GRAY);
                        JButton cancelButton = new JButton("Cancel");
                        cancelButton.setBackground(Color.DARK_GRAY);

                        dimensionFrameGridBagConstraints.gridx = 0;
                        dimensionFrameGridBagConstraints.gridy = 0;
                        crystalErrorPanel.add(crystalErrorLabel, dimensionFrameGridBagConstraints);
                        dimensionFrameGridBagConstraints.gridx = 0;
                        dimensionFrameGridBagConstraints.gridy = 1;
                        crystalErrorPanel.add(okButton, dimensionFrameGridBagConstraints);
                        dimensionFrameGridBagConstraints.gridx = 1;
                        dimensionFrameGridBagConstraints.gridy = 1;
                        crystalErrorPanel.add(cancelButton, dimensionFrameGridBagConstraints);
                        crystalErrorFrame.add(crystalErrorPanel);
                        crystalErrorFrame.pack();
                        crystalErrorFrame.setVisible(true);
                        crystalErrorFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 3);

                        okButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                MainFrame.isZCrystalEnabled = true;
                                MainFrame.isMaxZProtectorPlaneEnabled = true;
                                MainFrame.isMinZProtectorPlaneEnabled = true;
                                MainFrame.printLog("Z-crystal, Minimum Z Protector Bricks, Maximum Z Protector Bricks enabled ", Color.CYAN);
                                switchToOriginalColor();
                                crystalErrorFrame.dispose();
                            }
                        });
                        cancelButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                MainFrame.isZCrystalEnabled = false;
                                MainFrame.isMaxZProtectorPlaneEnabled = false;
                                MainFrame.isMinZProtectorPlaneEnabled = false;
                                zCrystalCheckBox.setSelected(false);
                                MainFrame.printLog("Z-Crystal, Minimum Z Protector Bricks, Maximum Z Protector Bricks not enabled ", Color.WHITE);
                                switchToOriginalColor();
                                crystalErrorFrame.dispose();
                            }
                        });
                    } else if (returnValue == 0) {
                        MainFrame.isZCrystalEnabled = true;
                        MainFrame.isMaxZProtectorPlaneEnabled = true;
                        MainFrame.isMinZProtectorPlaneEnabled = true;
                        MainFrame.printLog("Z-Crystal, Minimum Z Protector Bricks, Maximum Z Protector Bricks enabled ", Color.CYAN);
                    }
                } else {
                    MainFrame.isZCrystalEnabled = false;
                }

                if (xCrystalCheckBox.isSelected() == true) {
                    crystalType = 2;
                    isCrystalStructure();
                    if (returnValue == 1 || MainFrame.width % 2 == 1) {
                        MainFrame.isXCrystalEnabled = false;
                        xCrystalCheckBox.setSelected(false);
                        MainFrame.printLog("X-Crystal not enabled ", Color.WHITE);
                    } else if (returnValue == 2) {
                        final JFrame crystalErrorFrame = new JFrame("Output success");
                        crystalErrorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        crystalErrorFrame.setBackground(Color.DARK_GRAY);
                        crystalErrorFrame.setIconImage(MainFrame.imag);

                        crystalErrorFrame.setLayout(new GridBagLayout());
                        GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
                        dimensionFrameGridBagConstraints.insets = new Insets(3, 3, 3, 3);

                        JPanel crystalErrorPanel = new JPanel();
                        crystalErrorPanel.setBackground(Color.DARK_GRAY);

                        final JLabel crystalErrorLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Crystal can not be constructed properly. <br> The highlighted voxels can not be combined to form a crystal. <br> Build crystal anyway?</h4></html>");
                        JButton okButton = new JButton("Continue");
                        okButton.setBackground(Color.DARK_GRAY);
                        JButton cancelButton = new JButton("Cancel");
                        cancelButton.setBackground(Color.DARK_GRAY);

                        dimensionFrameGridBagConstraints.gridx = 0;
                        dimensionFrameGridBagConstraints.gridy = 0;
                        crystalErrorPanel.add(crystalErrorLabel, dimensionFrameGridBagConstraints);
                        dimensionFrameGridBagConstraints.gridx = 0;
                        dimensionFrameGridBagConstraints.gridy = 1;
                        crystalErrorPanel.add(okButton, dimensionFrameGridBagConstraints);
                        dimensionFrameGridBagConstraints.gridx = 1;
                        dimensionFrameGridBagConstraints.gridy = 1;
                        crystalErrorPanel.add(cancelButton, dimensionFrameGridBagConstraints);
                        crystalErrorFrame.add(crystalErrorPanel);
                        crystalErrorFrame.pack();
                        crystalErrorFrame.setVisible(true);
                        crystalErrorFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 3);

                        okButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                MainFrame.isXCrystalEnabled = true;
                                MainFrame.printLog("X-Crystal enabled ", Color.CYAN);
                                switchToOriginalColor();
                                crystalErrorFrame.dispose();
                            }
                        });
                        cancelButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                MainFrame.isXCrystalEnabled = false;
                                xCrystalCheckBox.setSelected(false);
                                MainFrame.printLog("X-Crystal not enabled ", Color.WHITE);
                                switchToOriginalColor();
                                crystalErrorFrame.dispose();
                            }
                        });
                    } else if (returnValue == 0) {
                        MainFrame.isXCrystalEnabled = true;
                        MainFrame.printLog("X-Crystal enabled ", Color.CYAN);
                    }
                } else {
                    MainFrame.isXCrystalEnabled = false;
                }

                if (yCrystalCheckBox.isSelected() == true) {
                    crystalType = 3;
                    isCrystalStructure();
                    if (returnValue == 1) { //entire plane not matching
                        MainFrame.isYCrystalEnabled = false;
                        MainFrame.isMaxZProtectorPlaneEnabled = false;
                        MainFrame.isMinZProtectorPlaneEnabled = false;
                        yCrystalCheckBox.setSelected(false);
                        MainFrame.printLog("Y-crystal, Minimum Z Protector Bricks, Maximum Z Protector Bricks not enabled ", Color.WHITE);
                    } else if (returnValue == 2) { //some voxels not matching

                        final JFrame crystalErrorFrame = new JFrame("Output success");
                        crystalErrorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        crystalErrorFrame.setBackground(Color.DARK_GRAY);
                        crystalErrorFrame.setIconImage(MainFrame.imag);

                        crystalErrorFrame.setLayout(new GridBagLayout());
                        GridBagConstraints dimensionFrameGridBagConstraints = new GridBagConstraints();
                        dimensionFrameGridBagConstraints.insets = new Insets(3, 3, 3, 3);


                        JPanel crystalErrorPanel = new JPanel();
                        crystalErrorPanel.setBackground(Color.DARK_GRAY);

                        final JLabel crystalErrorLabel = new JLabel("<html><style>h4{color:white;}</style><h4>Y-Crystal can not be constructed properly. <br> The highlighted voxels can not be combined to form a crystal. <br> Build crystal anyway?</h4></html>");
                        JButton okButton = new JButton("Continue");
                        okButton.setBackground(Color.DARK_GRAY);
                        JButton cancelButton = new JButton("Cancel");
                        cancelButton.setBackground(Color.DARK_GRAY);

                        dimensionFrameGridBagConstraints.gridx = 0;
                        dimensionFrameGridBagConstraints.gridy = 0;
                        crystalErrorPanel.add(crystalErrorLabel, dimensionFrameGridBagConstraints);
                        dimensionFrameGridBagConstraints.gridx = 0;
                        dimensionFrameGridBagConstraints.gridy = 1;
                        crystalErrorPanel.add(okButton, dimensionFrameGridBagConstraints);
                        dimensionFrameGridBagConstraints.gridx = 1;
                        dimensionFrameGridBagConstraints.gridy = 1;
                        crystalErrorPanel.add(cancelButton, dimensionFrameGridBagConstraints);
                        crystalErrorFrame.add(crystalErrorPanel);
                        crystalErrorFrame.pack();
                        crystalErrorFrame.setVisible(true);
                        crystalErrorFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 3);

                        okButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                MainFrame.isYCrystalEnabled = true;
                                MainFrame.printLog("Y-crystal enabled ", Color.CYAN);
                                switchToOriginalColor();
                                crystalErrorFrame.dispose();
                            }
                        });
                        cancelButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                MainFrame.isYCrystalEnabled = false;
                                yCrystalCheckBox.setSelected(false);
                                MainFrame.printLog("Y-Crystal not enabled ", Color.WHITE);
                                switchToOriginalColor();
                                crystalErrorFrame.dispose();
                            }
                        });
                    } else if (returnValue == 0) {
                        MainFrame.isYCrystalEnabled = true;
                        MainFrame.printLog("Y-Crystal enabled ", Color.CYAN);
                    }
                } else {
                    MainFrame.isYCrystalEnabled = false;
                }

                MainFrame.isCSVSaved = false;
                MainFrame.isPDFSaved = false;
                MainFrame.isCSVBoundarySaved = false;
                MainFrame.isPDFBoundarySaved = false;

                enableCrystalFrame.dispose();

            }
        });
        enableCrystalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public void switchToOriginalColor(){
        MainFrame.printLog("swiching back to original color", Color.white);
        Appearance ap = new Appearance();
        for (int z = 0 ; z < CanvasActionListener.colorCubeArrayList.size(); z++) {
                DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(z).canvasDNAColorCube;
                pickedCube.setAppearance(ap);
        }
    }
    public void isCrystalStructure() {
        int[][][] canvasCoordinates = new int[MainFrame.width][MainFrame.height][MainFrame.depth + 1];
        /*
        0 : no error
        X x Y : entire plane(s) to be deleted or added
        less than X x Y : some voxels may have to be deleted or added
        */
        int zCrystalError = 0, xCrystalError = 0, yCrystalError = 0;
        int i, j, k;
        int minZ, maxZ, minX, maxX, minY, maxY;
        int deletedColoumnsAlongXAxis = 0, deletedColoumnsAlongZAxis = 0, deletedColoumnsAlongYAxis = 0;
        String frontPlane = "";
        String backPlane = "";

        for (i = 0; i < MainFrame.width; i++)
            for (j = 0; j < MainFrame.height; j++)
                for (k = 0; k <= MainFrame.depth; k++)
                    canvasCoordinates[i][j][k] = -1; //assign -1 to all initially so that any missing protector head bricks can be assumed to be -1


        for (i = 0; i < MainFrame.width; i++) {
            for (j = 0; j < MainFrame.height; j++) {
                for (k = 0; k <= MainFrame.depth; k++) {
                    if (MainFrame.deletedCoordinates[i][j][k])
                        canvasCoordinates[i][j][k] = -1;
                    else
                        canvasCoordinates[i][j][k] = 1;

                }
            }
        }
        if(crystalType == 1){
            for (i = 0; i < MainFrame.width; i++)
                for (j = 0; j < MainFrame.height; j++) {
                    minZ = 0; //minZ is subtracted later on
                    maxZ = -1;
                    k = MainFrame.depth;
                    while (k > 0) {
                        if (canvasCoordinates[i][j][k] != -1)
                            minZ = k;
                        k--;
                    }
                    k = 1; //start from 1 to leave out head protector bricks
                    while (k <= MainFrame.depth) {
                        if (canvasCoordinates[i][j][k] != -1)
                            maxZ = k;
                        k++;
                    }
                    minZ--; // to include head protector bricks
                    if (minZ == -1 && maxZ == -1) {
                        deletedColoumnsAlongZAxis++;
                    } else if (minZ % 4 == 0 && maxZ % 4 == 3) {
                        // do nothing
                    } else if (minZ % 4 == 1 && maxZ % 4 == 0) {
                        // do nothing
                    } else if (minZ % 4 == 2 && maxZ % 4 == 1) {
                        // do nothing
                    } else if (minZ % 4 == 3 && maxZ % 4 == 2) {
                        // do nothing
                    } else {
                        switch (minZ % 4) {
                            case 0: {
                                frontPlane = "North";
                                break;
                            }
                            case 1: {
                                frontPlane = "West";
                                break;
                            }
                            case 2: {
                                frontPlane = "South";
                                break;
                            }
                            case 3: {
                                frontPlane = "East";
                                break;
                            }
                            default: {
                                frontPlane = "Invalid";
                                break;
                            }
                        }
                            switch (maxZ % 4) {
                                case 0: {
                                    backPlane = "North";
                                    break;
                                }
                                case 1: {
                                    backPlane = "West";
                                    break;
                                }
                                case 2: {
                                    backPlane = "South";
                                    break;
                                }
                                case 3: {
                                    backPlane = "East";
                                    break;
                                }
                                default: {
                                    backPlane = "Invalid";
                                    break;
                                }
                            }

                        Appearance ap = new Appearance();
                        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST, 0.3f);
                        ColoringAttributes coloringAttributes = new ColoringAttributes(1.0f, 1.0f, 0.0f, ColoringAttributes.NICEST);
                        ap.setCapability (Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
                        ap.setTransparencyAttributes(transparencyAttributes);
                        ap.setColoringAttributes(coloringAttributes);
                        for (int z = 0; z < CanvasActionListener.colorCubeArrayList.size(); z++) {
                            if (CanvasActionListener.colorCubeArrayList.get(z).xCord == i &&
                                    CanvasActionListener.colorCubeArrayList.get(z).yCord == j &&
                                    (CanvasActionListener.colorCubeArrayList.get(z).zCord == minZ + 1 || CanvasActionListener.colorCubeArrayList.get(z).zCord == maxZ)) {
                                DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(z).canvasDNAColorCube;
                                pickedCube.setAppearance(ap);
                            }
                        }
                        zCrystalError++;
                    }
                }
        }
        else if(crystalType == 2){
            for (j = 0; j < MainFrame.height; j++)
                for (k = 1; k <= MainFrame.depth; k++) {
                    minX = -1;
                    maxX = -1;
                    i = MainFrame.width - 1;
                    while(i >= 0) {
                        if (canvasCoordinates[i][j][k] != -1)
                            minX = i;
                        i--;
                    }
                    i = 0;
                    while(i < MainFrame.width) {
                        if (canvasCoordinates[i][j][k] != -1)
                            maxX = i;
                        i++;
                    }
                    if( minX == -1 && maxX == -1){
                        deletedColoumnsAlongXAxis ++;
                    }
                    else if(minX % 2 == 1 && maxX % 2 == 0){
                        // do nothing
                    }
                    else if (minX % 2 == 0 && maxX % 2 == 1){
                        // do nothing
                    }
                    else{
                        MainFrame.printLog("minX " + minX + " maxX " + maxX, Color.WHITE);
                        Appearance ap = new Appearance();
                        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.3f);
                        ColoringAttributes coloringAttributes = new ColoringAttributes(1.0f, 1.0f, 0.0f, ColoringAttributes.NICEST);
                        ap.setCapability (Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
                        ap.setTransparencyAttributes( transparencyAttributes );
                        ap.setColoringAttributes(coloringAttributes);
                        for (int z = 0 ; z < CanvasActionListener.colorCubeArrayList.size(); z++) {
                            if (CanvasActionListener.colorCubeArrayList.get(z).zCord == k &&
                                    CanvasActionListener.colorCubeArrayList.get(z).yCord == j &&
                                    (CanvasActionListener.colorCubeArrayList.get(z).xCord == minX  || CanvasActionListener.colorCubeArrayList.get(z).xCord == maxX  )) {
                                DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(z).canvasDNAColorCube;
                                pickedCube.setAppearance(ap);
                            }
                        }
                        xCrystalError ++;
                    }
                }
        }

        else if(crystalType == 3){
            for (i = 0; i < MainFrame.width; i++)
                for (k = 1; k <= MainFrame.depth; k++) {
                    minY = -1;
                    maxY = -1;
                    j = MainFrame.height - 1;
                    while(j >= 0) {
                        if (canvasCoordinates[i][j][k] != -1)
                            minY = j;
                        j--;
                    }
                    j = 0;
                    while(j < MainFrame.height) {
                        if (canvasCoordinates[i][j][k] != -1)
                            maxY = j;
                        j++;
                    }
                    if( minY == -1 && maxY == -1){
                        deletedColoumnsAlongYAxis ++;
                    }
                    else if(minY % 2 == 1 && maxY % 2 == 0){
                        // do nothing
                    }
                    else if (minY % 2 == 0 && maxY % 2 == 1){
                        // do nothing
                    }
                    else{
                        MainFrame.printLog("minY " + minY + " maxY " + maxY, Color.WHITE);
                        Appearance ap = new Appearance();
                        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.3f);
                        ColoringAttributes coloringAttributes = new ColoringAttributes(1.0f, 1.0f, 0.0f, ColoringAttributes.NICEST);
                        ap.setCapability (Appearance.ALLOW_COLORING_ATTRIBUTES_WRITE);
                        ap.setTransparencyAttributes( transparencyAttributes );
                        ap.setColoringAttributes(coloringAttributes);
                        for (int z = 0 ; z < CanvasActionListener.colorCubeArrayList.size(); z++) {
                            if (CanvasActionListener.colorCubeArrayList.get(z).zCord == k &&
                                    CanvasActionListener.colorCubeArrayList.get(z).xCord == i &&
                                    (CanvasActionListener.colorCubeArrayList.get(z).yCord == minY  || CanvasActionListener.colorCubeArrayList.get(z).yCord == maxY  )) {
                                DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(z).canvasDNAColorCube;
                                pickedCube.setAppearance(ap);
                            }
                        }
                        yCrystalError ++;
                    }
                }
        }

        if(crystalType == 1 && zCrystalError == (MainFrame.height * MainFrame.width - deletedColoumnsAlongZAxis)){
            MainFrame.printLog("entire Planes not matching for crystal. Z error count = " + zCrystalError + ", rows missing along Z axis: " + deletedColoumnsAlongZAxis , Color.magenta);
            
            final JFrame crystalErrorFrame = new JFrame("Crystal Error");
            crystalErrorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            crystalErrorFrame.setBackground(Color.DARK_GRAY);
            crystalErrorFrame.setIconImage(MainFrame.imag);
            JPanel crystalErrorPanel = new JPanel();
            crystalErrorPanel.setBackground(Color.DARK_GRAY);
            final JLabel crystalErrorLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Crystal can not be constructed. <br> The plane in the front ("+ frontPlane + ") and back ("+ backPlane + ") of the structure can not be combined to form a crystal.  <br>Tip: try deleting plane(s) from front or back.</h4></html>");

            JButton okButton = new JButton("OK");
            okButton.setBackground(Color.DARK_GRAY);

            crystalErrorPanel.add(crystalErrorLabel);
            crystalErrorPanel.add(okButton);
            crystalErrorFrame.add(crystalErrorPanel, BorderLayout.CENTER);
            crystalErrorFrame.pack();
            crystalErrorFrame.setVisible(true);
            crystalErrorFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 3);

            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    switchToOriginalColor();
                    crystalErrorFrame.dispose();
                }
            });
            returnValue = 1;
        }
        else if(crystalType == 2 && xCrystalError == (MainFrame.depth * MainFrame.height - deletedColoumnsAlongXAxis)){
            MainFrame.printLog("entire Planes not matching for crystal.X error count = " + xCrystalError + ", rows missing along X axis: " + deletedColoumnsAlongXAxis , Color.magenta);

            final JFrame crystalErrorFrame = new JFrame("Crystal Error");
            crystalErrorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            crystalErrorFrame.setBackground(Color.DARK_GRAY);
            crystalErrorFrame.setIconImage(MainFrame.imag);
            JPanel crystalErrorPanel = new JPanel();
            crystalErrorPanel.setBackground(Color.DARK_GRAY);
            final JLabel crystalErrorLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Crystal can not be constructed. <br> The planes at the right and left of the structure can not be combined to form a crystal.  <br> Tip: A structure with even width should be created by adding / deleting a plane at the right or left.</h4></html>");

            JButton okButton = new JButton("OK");
            okButton.setBackground(Color.DARK_GRAY);

            crystalErrorPanel.add(crystalErrorLabel);
            crystalErrorPanel.add(okButton);
            crystalErrorFrame.add(crystalErrorPanel, BorderLayout.CENTER);
            crystalErrorFrame.pack();
            crystalErrorFrame.setVisible(true);
            crystalErrorFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 3);

            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    switchToOriginalColor();
                    crystalErrorFrame.dispose();
                }
            });
            returnValue = 1;
        }
        else if(crystalType == 3 && yCrystalError == (MainFrame.depth * MainFrame.width - deletedColoumnsAlongYAxis)){
            MainFrame.printLog("entire Planes not matching for crystal. Y error count = " + yCrystalError + ", rows missing along Y axis: " + deletedColoumnsAlongYAxis , Color.magenta);

            final JFrame crystalErrorFrame = new JFrame("Crystal Error");
            crystalErrorFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            crystalErrorFrame.setBackground(Color.DARK_GRAY);
            crystalErrorFrame.setIconImage(MainFrame.imag);
            JPanel crystalErrorPanel = new JPanel();
            crystalErrorPanel.setBackground(Color.DARK_GRAY);
            final JLabel crystalErrorLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Crystal can not be constructed. <br> The planes at the top and bottom of the structure can not be combined to form a crystal. <br> Tip: A structure with even height should be created by adding / deleting a plane at the top or bottom.</h4></html>");

            JButton okButton = new JButton("OK");
            okButton.setBackground(Color.DARK_GRAY);

            crystalErrorPanel.add(crystalErrorLabel);
            crystalErrorPanel.add(okButton);
            crystalErrorFrame.add(crystalErrorPanel, BorderLayout.CENTER);
            crystalErrorFrame.pack();
            crystalErrorFrame.setVisible(true);
            crystalErrorFrame.setLocation(MainFrame.screenWidth / 3, MainFrame.screenHeight / 3);

            okButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    switchToOriginalColor();
                    crystalErrorFrame.dispose();
                }
            });
            returnValue = 1;
        }
        else if ((crystalType == 1 && zCrystalError == 0) || (crystalType == 2 && zCrystalError == 0) || (crystalType == 3 && yCrystalError == 0) ){
            MainFrame.printLog("No error. Z error count = " + zCrystalError + ", rows missing along Z axis: " + deletedColoumnsAlongZAxis +
                    "X error count = " + xCrystalError + ", rows missing along X axis: " + deletedColoumnsAlongXAxis +
                    "Y error count = " + yCrystalError + ", rows missing along Y axis: " + deletedColoumnsAlongXAxis, Color.magenta);

            returnValue = 0;
        }
        else {
            MainFrame.printLog("some voxels not matching.Z error count = " + zCrystalError + ", rows missing along Z axis: " + deletedColoumnsAlongZAxis +
                    "X error count = " + xCrystalError + ", rows missing along X axis: " + deletedColoumnsAlongXAxis +
                    "Y error count = " + yCrystalError + ", rows missing along Y axis: " + deletedColoumnsAlongXAxis, Color.magenta);

            returnValue = 2;
        }

    }
}

