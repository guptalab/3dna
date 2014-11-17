import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.LineArray;
import javax.vecmath.Point3f;

/** Author: Shikhar K Gupta, Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */
public class ArrowFullBrick extends TransformGroup {
    public ArrowFullBrick(){
        super();
        float x1 = 0f;
        float y1 = 0f;
        float z1 = -0.50f;

        float x2 = 0f;
        float y2 = 0f;
        float z2 = 0f;

        float x3 = 0.5f;
        float y3 = 0f;
        float z3 = 0f;

        float x4 = 0.5f;
        float y4 = 0f;
        float z4 = -0.50f;

        float x5 = 0f;
        float y5 = 0.02f;
        float z5 = -0.48f;

        float x6 = 0f;
        float y6 = -0.02f;
        float z6 = -0.48f;

        //Initializing LineArrays for the Molecular Axis
        LineArray l1LineArray = new LineArray(2, LineArray.COORDINATES);
        LineArray l2LineArray = new LineArray(2, LineArray.COORDINATES);
        LineArray l3LineArray = new LineArray(2, LineArray.COORDINATES);
        LineArray l4LineArray = new LineArray(2, LineArray.COORDINATES);
        LineArray l5LineArray = new LineArray(2, LineArray.COORDINATES);

        //Initializing the LineArray(s)

        l1LineArray.setCoordinate(0,new Point3f(x1,y1,z1));
        l1LineArray.setCoordinate(1,new Point3f(x2,y2,z2));

        l2LineArray.setCoordinate(0,new Point3f(x2,y2,z2));
        l2LineArray.setCoordinate(1,new Point3f(x3,y3,z3));

        l3LineArray.setCoordinate(0,new Point3f(x3,y3,z3));
        l3LineArray.setCoordinate(1,new Point3f(x4,y4,z4));

        l4LineArray.setCoordinate(0,new Point3f(x1,y1,z1));
        l4LineArray.setCoordinate(1,new Point3f(x5,y5,z5));

        l5LineArray.setCoordinate(0,new Point3f(x1,y1,z1));
        l5LineArray.setCoordinate(1,new Point3f(x6,y6,z6));

        this.addChild(new Shape3D(l1LineArray));
        this.addChild(new Shape3D(l2LineArray));
        this.addChild(new Shape3D(l3LineArray));
        this.addChild(new Shape3D(l4LineArray));
        this.addChild(new Shape3D(l5LineArray));
    }
}
