import javax.media.j3d.Appearance;
import javax.media.j3d.TransparencyAttributes;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by lenovo on 10/2/2014.
 */
public class ChangeDNAColorCubeAppearance implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e){
        Appearance normalAppearance= new Appearance();
        Appearance transparentApearance = new Appearance();
        TransparencyAttributes transparencyAttributes = new TransparencyAttributes(TransparencyAttributes.NICEST,0.8f);
        transparentApearance.setTransparencyAttributes( transparencyAttributes );
        if(!MainFrame.isAppearanceTransparent){
            MainFrame.isAppearanceTransparent=true;
            for (int i = 0; i < CanvasActionListener.colorCubeArrayList.size(); i++) {
                DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(i).canvasDNAColorCube;
                pickedCube.setAppearance(transparentApearance);
            }
        }else{
            MainFrame.isAppearanceTransparent=false;
            for (int i = 0; i < CanvasActionListener.colorCubeArrayList.size(); i++) {
                DNAColorCube pickedCube = CanvasActionListener.colorCubeArrayList.get(i).canvasDNAColorCube;
                pickedCube.setAppearance(normalAppearance);
            }
        }
    }
}
