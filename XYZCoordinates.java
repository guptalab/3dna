/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */

 public class XYZCoordinates {
	
	public int x,y,z;
	public boolean isRemoved=false;
    public boolean isHeadBrick=false;
	public char orientation;
	public int domain;
	//public boolean isRepeat=false;
	public boolean isHalfBrick=false;
	public String Domain1;
	public String Domain2;
	public String Domain3;
	public String Domain4;
	
	
	 public XYZCoordinates(int xcoord, int ycoord, int zcoord) {
	        this.x = xcoord;
	        this.y = ycoord;
	        this.z = zcoord;
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
