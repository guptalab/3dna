/** Author: Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */


public class VoxelToBrick {

    String Domain1;
    String Domain2;
    String Domain3;
    String Domain4;
    String Domain5 = null;//boundary brick attachment
    String Domain6 = null;//boundary brick attachment

    int x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4, x5, y5, z5, x6, y6, z6;
    int helix1, helix2, helix3, helix4, helix5, helix6;

    int strandNumber;
    int plateNumber;
    int position;

    boolean isHeadProtector = false;
    boolean isTailProtector = false;
    boolean isLeftmostHalfBrick = false;
    boolean isRightmostHalfBrick = false;
    boolean isTopmostHalfBrick = false;
    boolean isBottommostHalfBrick = false;
    boolean isAdjacentToCavity = false;
    boolean isRemoved = false;
    boolean isBoundary = false;
    boolean isVisualized = false;
    boolean isDomain1Replaced = false, isDomain2Replaced = false, isDomain3Replaced = false, isDomain4Replaced = false;
    boolean isMapped = true;

    //The constructor initializes all the coordinates and domain sequences for a brick. All helix numbers are initialized to -1.
    //Helix numbers are assigned in function addHelixNumber() later.
    /*
         domain2 (x2, y2, z2)  -------------  domain1 (x1, y1, z1)
                              |
                              |
         domain 3 (x3, y3, z3) -------------> domain4 (x4, y4, z4)
     */
    public VoxelToBrick(int xCoordinate2, int yCoordinate2,int zCoordinate2, int xCoordinate3,int yCoordinate3, int zCoordinate3,String d1, String d2, String d3, String d4){
        this.x2 = xCoordinate2;
        this.x1 = x2;
        this.x3 = xCoordinate3;
        this.x4 = x3;
        this.y2 = yCoordinate2;
        this.y1 = y2;
        this.y3 = yCoordinate3;
        this.y4 = y3;
        this.z2 = zCoordinate2;
        this.z1 = z2 + 1;
        this.z3 = zCoordinate3;
        this.z4 = z3 + 1;
        this.Domain1 = d1;
        this.Domain2 = d2;
        this.Domain3 = d3;
        this.Domain4 = d4;
        helix1 = -1;
        helix2 = -1;
        helix3 = -1;
        helix4 = -1;
        helix5 = -1;
        helix6 = -1;
        this.x5 = -1;
        this.x6 = -1;
    }

    //Boundary brick information is added in this function.
    public void addDomains(int xCoordinate5, int yCoordinate5,int zCoordinate5, String d5, String d6, int h5, int h6, int domains){
        //attaching half bricks to domain4
        if (domains == 34) {
            this.x5 = xCoordinate5;
            this.x6 = x5;
            this.y5 = yCoordinate5;
            this.y6 = y5;
            this.z5 = zCoordinate5;
            this.z6 = z5 + 1;
            this.Domain5 = d5;
            this.Domain6 = d6;
            this.helix5 = h5;
            this.helix6 = h6;
        }
        //attaching half bricks to domain1
        else{
            x6 = x4;
            y6 = y4;
            z6 = z4;
            x5 = x3;
            y5 = y3;
            z5 = z3;
            x4 = x2;
            y4 = y2;
            z4 = z2;
            x3 = x1;
            y3 = y1;
            z3 = z1;
            x2 = xCoordinate5;
            y2 = yCoordinate5;
            z2 = zCoordinate5;
            x1 = x2;
            y1 = y2;
            z1 = z2 + 1;
            helix6 = helix4;
            helix5 = helix3;
            helix4 = helix2;
            helix3 = helix1;
            helix2 = h5;
            helix1 = h6;
            this.Domain6 = Domain4;
            this.Domain5 = Domain3;
            Domain4 = Domain4.replace(Domain4, Domain2);
            Domain3 = Domain3.replace(Domain3, Domain1);
            Domain2 = Domain2.replace(Domain2, d5);
            Domain1 = Domain1.replace(Domain1, d6);

            System.out.print("boundary brick added in voxel to brick domain12:"+ helix1 + " " + z1 + " " + helix2 + " " + z2 + " " + helix3 + " " + z3 + " " + helix4 + " " + z4 + " " + helix5 + " " + z5 + " " + helix6 + " " + z6 );
        }
    }

    public String getCompleteSequence(){
        String BrickSequence;
        if(Domain1==null&&Domain2==null&&Domain3!=null&&Domain4!=null){
            BrickSequence=Domain3;
            BrickSequence=BrickSequence.concat(Domain4);
        }
        else if(Domain1!=null&&Domain2!=null&&Domain3==null&&Domain4==null){
            BrickSequence=Domain1;
            BrickSequence=BrickSequence.concat(Domain2);
        }
        else if(Domain5!=null) {
            BrickSequence = Domain1;
            BrickSequence = BrickSequence.concat(Domain2);
            BrickSequence = BrickSequence.concat(Domain3);
            BrickSequence = BrickSequence.concat(Domain4);
            BrickSequence = BrickSequence.concat(Domain5);
            BrickSequence = BrickSequence.concat(Domain6);
        }
        else {
            BrickSequence = Domain1;
            if(Domain2!=null)
                BrickSequence = BrickSequence.concat(Domain2);
            if(Domain3!=null)
                BrickSequence = BrickSequence.concat(Domain3);
            if(Domain4!=null)
                BrickSequence = BrickSequence.concat(Domain4);

        }
        return(BrickSequence);
    }

    public boolean isHalfBrick(){
        if(x2==-1||x3==-1)
            return true;
        else
            return false;
    }

}
