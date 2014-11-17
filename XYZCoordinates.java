/** Author: Foram Joshi
 * Project: 3Pen
 * Mentor: Prof. Manish K Gupta
 */

 public class XYZCoordinates {
	
	public int x,y,z;
	public boolean isRemoved = false;
    public boolean isHeadBrick = false;
    public boolean isTailBrick = false;
    public boolean isLeftmostBrick = false;
    public boolean isRightmostBrick = false;
    public boolean isTopmostBrick = false;
    public boolean isBottommostBrick = false;
    public boolean isDomain1Replaced = false, isDomain2Replaced = false, isDomain3Replaced = false, isDomain4Replaced = false;
    public boolean isAdjacentToCavity = false;


    public char orientation;
	public int domain;
	//public boolean isRepeat=false;
	public boolean isHalfBrick=false;
	public String Domain1;
	public String Domain2;
	public String Domain3;
	public String Domain4;
	
	
	 public XYZCoordinates(int xCoordinate, int yCoordinate, int zCoordinate ) {
	     this.x = xCoordinate;
	     this.y = yCoordinate;
	     this.z = zCoordinate;
	 }

	 public void editBrickOrientation(char o){
		 this.orientation=o;
	 }
	 
	 public void editdomain (int d){
		 this.domain = d;
	 }


	 public int getxCoordinate(){
		 return(x);
	 }

	 public int getyCoordinate(){
		 return(y);
	 }

	 public int getzCoordinate(){
		 return(z);
	 }

}
