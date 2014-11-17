/** Author: Shikhar K Gupta, Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */

import javax.media.j3d.*;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class UndoActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
        Point3f undoCoOrdinates;
        if(MainFrame.isImported){
            MainFrame.deletedCoordinates[ImportActionListener.lastx][(ImportActionListener.lasty*-1)][(ImportActionListener.lastz*-1)]=false;
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
            if(!CanvasActionListener.undoStack.isEmpty()){
                undoCoOrdinates=(Point3f)CanvasActionListener.undoStack.pop();
                DNAColorCube c1 = new DNAColorCube(0.05f);
                c1.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
                c1.setCapability(Shape3D.ALLOW_APPEARANCE_OVERRIDE_WRITE);
                c1.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);
                c1.setCapability(Node.ENABLE_PICK_REPORTING);
                c1.setCordinate(CanvasActionListener.lastx, CanvasActionListener.lasty,CanvasActionListener.lastz);
                TransformGroup tg = new TransformGroup();
                tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
                tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
                Transform3D transform = new Transform3D();
                Vector3f vector = new Vector3f(undoCoOrdinates.getX()*0.105f,undoCoOrdinates.getY()*-0.105f,undoCoOrdinates.getZ()*-0.105f);
                transform.setTranslation(vector);
                tg.addChild(c1);
                tg.setTransform(transform);
                BranchGroup undoGroup=new BranchGroup();
                undoGroup.addChild(tg);
                CanvasActionListener.masterTrans.addChild(undoGroup);
                MainFrame.deletedCoordinates[CanvasActionListener.lastx][CanvasActionListener.lasty][CanvasActionListener.lastz]=false;
                MainFrame.printLog("Voxel added at coordinates: [" + undoCoOrdinates.getX()+ ", " + undoCoOrdinates.getY()+ ", " + undoCoOrdinates.getZ()+ "]" +
                        " [" + undoCoOrdinates.getX()+ ", " + undoCoOrdinates.getY()+ ", " + (undoCoOrdinates.getZ()+ 1) + "]", Color.cyan);

            }else {
                MainFrame.printLog("Nothing to Undo",Color.PINK);
            }
        }
    }
}