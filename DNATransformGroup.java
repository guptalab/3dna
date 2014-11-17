import javax.media.j3d.TransformGroup;

/**
 * Created by lenovo on 9/25/2014.
 */
public class DNATransformGroup extends TransformGroup {
    public DNATransformGroup(){
        super();
    }
    public void setSequence(String sequence){
        domainSequence=sequence;
    }
    public String getSequence(){
        return domainSequence;
    }
    public void setCordinate(float x, float y, float z){
        xCord=x;
        yCord=y;
        zCord=z;
    }
    public String domainSequence;

    public float xCord;
    public float yCord;
    public float zCord;
    public boolean isHalfBrick;
    public boolean isDomain12;
    public boolean isDomain34;
    public boolean isNorthBrick;
    public boolean isWestBrick;
    public boolean isSouthBrick;
    public boolean isEastBrick;
}
