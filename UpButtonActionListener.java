/** Author: Shikhar K Gupta, Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */

import com.sun.j3d.utils.universe.ViewingPlatform;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpButtonActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
        float step=0.25f;
        if(MainFrame.isImported){
            ImportActionListener.canvasy=ImportActionListener.canvasy-step;
            ViewingPlatform vp = ImportActionListener.simpleU.getViewingPlatform(); // get the ViewingPlatform of the SimpleUniverse

            TransformGroup View_TransformGroup = vp.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated

            Transform3D View_Transform3D = new Transform3D();	// create a Transform3D for the ViewingPlatform
            View_TransformGroup.getTransform(View_Transform3D); // get the current 3d from the ViewingPlatform

            View_Transform3D.setTranslation(new Vector3f(ImportActionListener.canvasx,
                    ImportActionListener.canvasy,ImportActionListener.canvasz)); // set 3d to  x=0, y=-1, z= 5
            View_TransformGroup.setTransform(View_Transform3D);  // assign Transform3D to ViewPlatform
        }
        else{
            CanvasActionListener.canvasy=CanvasActionListener.canvasy-step;
            ViewingPlatform vp = CanvasActionListener.simpleU.getViewingPlatform(); // get the ViewingPlatform of the SimpleUniverse

            TransformGroup View_TransformGroup = vp.getMultiTransformGroup().getTransformGroup(0); // get the TransformGroup associated

            Transform3D View_Transform3D = new Transform3D();	// create a Transform3D for the ViewingPlatform
            View_TransformGroup.getTransform(View_Transform3D); // get the current 3d from the ViewingPlatform

            View_Transform3D.setTranslation(new Vector3f(CanvasActionListener.canvasx,
                    CanvasActionListener.canvasy,CanvasActionListener.canvasz)); // set 3d to  x=0, y=-1, z= 5
            View_TransformGroup.setTransform(View_Transform3D);  // assign Transform3D to ViewPlatform
        }
    }
}
