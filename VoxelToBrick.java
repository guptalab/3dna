/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */


public class VoxelToBrick {

	String Domain1;
	String Domain2;
	String Domain3;
	String Domain4;
	String Domain5=null;//boundary brick attachment
	String Domain6=null;//boundary brick attachment
	
	//a brick can consist of as many as 4 voxels
	int[] voxel1=new int[3];
	int[] voxel2=new int[3];
	int[] voxel3=new int[3];
	int[] voxel4=null;


	boolean isBoundary=false;
    boolean isHeadProtector=false;
    boolean isVisualized=false;
	
	public VoxelToBrick(int v1, int v2,int v3, int v4,int v5, int v6,String d1, String d2, String d3, String d4, boolean headProtector){
		this.voxel1[0]=v1;
		this.voxel2[0]=v4;
		this.voxel1[1]=v2;
		this.voxel2[1]=v5;
		this.voxel1[2]=v3;
		this.voxel2[2]=v6;
		this.Domain1=d1;
		this.Domain2=d2;
		this.Domain3=d3;
		this.Domain4=d4;
        this.isHeadProtector=headProtector;

        this.voxel3[0]=-1;
	}
	
	public void addDomains(int v1, int v2,int v3, String d5, String d6){
		this.voxel3[0]=v1;
		this.voxel3[1]=v2;
		this.voxel3[2]=v3;
		this.Domain5=d5;
		this.Domain6=d6;
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
            BrickSequence = BrickSequence.concat(Domain2);
            BrickSequence = BrickSequence.concat(Domain3);
            BrickSequence = BrickSequence.concat(Domain4);

        }
		return(BrickSequence);	
	}
	
	
	
}
