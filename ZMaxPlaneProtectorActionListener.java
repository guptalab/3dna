import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by lenovo on 9/22/2014.
 */
public class ZMaxPlaneProtectorActionListener implements ActionListener {

    public Appearance transparencyAppearance= new Appearance();
    public TransparencyAttributes fullTransparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,1f);

    public Appearance protectorCubeAppearance = new Appearance();
    TransparencyAttributes partialTransparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.6f);

    @Override
    public void actionPerformed(ActionEvent e) {
        if (MainFrame.zMaxPlaneProtectorCheckbox.isSelected() == true) {
            protectorCubeAppearance.setTransparencyAttributes(partialTransparencyAttributes);
            MainFrame.isMaxZProtectorPlaneEnabled = true;
            MainFrame.printLog("Protector Bricks in the Z max has been enabled", Color.CYAN);
            for(int i=0; i <CanvasActionListener.protectorCubeArrayList.size(); i++) {
                if(CanvasActionListener.protectorCubeArrayList.get(i).zCord == 0) {
                    DNAProtectorCube protectorCube = CanvasActionListener.protectorCubeArrayList.get(i).canvasDNAProtectorCube;
                    protectorCube.setAppearance(protectorCubeAppearance);
                }
            }
        }else {
            transparencyAppearance.setTransparencyAttributes(fullTransparencyAttributes);
            MainFrame.isMaxZProtectorPlaneEnabled = false;
            MainFrame.printLog("Protector Bricks in the Z max has been disabled", Color.CYAN);
            for(int i=0; i <CanvasActionListener.protectorCubeArrayList.size(); i++) {
                if(CanvasActionListener.protectorCubeArrayList.get(i).zCord == 0) {
                    DNAProtectorCube protectorCube = CanvasActionListener.protectorCubeArrayList.get(i).canvasDNAProtectorCube;
                    protectorCube.setAppearance(transparencyAppearance);
                }
            }
        }
    }
}
