/** Author: Shikhar K Gupta, Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */

import javax.media.j3d.*;
import javax.vecmath.Vector3f;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class UndoActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
        if(MainFrame.isImported){
            MainFrame.StoreCoordinates[ImportActionListener.lastx][(ImportActionListener.lasty*-1)][(ImportActionListener.lastz*-1)]=false;
            DNAColorCube c1 = new DNAColorCube(0.05f);
            c1.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
            c1.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
            c1.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
            c1.setCapability(Node.ENABLE_PICK_REPORTING);
            c1.setCordinate(ImportActionListener.lastx, ImportActionListener.lasty-MainFrame.height+1, ImportActionListener.
                    lastz-MainFrame.depth+1);
            TransformGroup tg = new TransformGroup();
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            Transform3D transform = new Transform3D();
            Vector3f vector = new Vector3f(0f+ImportActionListener.lastx*0.105f,0f+(MainFrame.height+ImportActionListener.lasty-1)*0.105f,
                    0f+(MainFrame.depth+ImportActionListener.lastz-1)*0.105f);
            transform.setTranslation(vector);
            tg.addChild(c1);
            tg.setTransform(transform);
            BranchGroup undoGroup=new BranchGroup();
            undoGroup.addChild(tg);
            ImportActionListener.masterTrans.addChild(undoGroup);
        }
        else{
            MainFrame.StoreCoordinates[CanvasActionListener.lastx][(CanvasActionListener.lasty*-1)][(CanvasActionListener.lastz*-1)]=false;
            DNAColorCube c1 = new DNAColorCube(0.05f);
            c1.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
            c1.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
            c1.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
            c1.setCapability(Node.ENABLE_PICK_REPORTING);
            c1.setCordinate(CanvasActionListener.lastx, CanvasActionListener.lasty-MainFrame.height+1, CanvasActionListener.
                    lastz-MainFrame.depth+1);
            TransformGroup tg = new TransformGroup();
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
            tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
            Transform3D transform = new Transform3D();
            Vector3f vector = new Vector3f(0f+CanvasActionListener.lastx*0.105f,0f+(MainFrame.height+CanvasActionListener.lasty-1)*0.105f,
                    0f+(MainFrame.depth+CanvasActionListener.lastz-1)*0.105f);
            transform.setTranslation(vector);
            tg.addChild(c1);
            tg.setTransform(transform);
            BranchGroup undoGroup=new BranchGroup();
            undoGroup.addChild(tg);
            CanvasActionListener.masterTrans.addChild(undoGroup);
        }
    }
}
