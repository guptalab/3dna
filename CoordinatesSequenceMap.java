/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA
 * Mentor: Prof. Manish K Gupta
 */

import sun.applet.Main;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class CoordinatesSequenceMap {

	static ArrayList<DNASequence> randomSequencesList= new ArrayList<DNASequence>();
	static ArrayList<DNASequence> sequencesList= new ArrayList<DNASequence>();
	public static ArrayList<XYZCoordinates> coordinatesList= new ArrayList<XYZCoordinates>();
    static ArrayList<VoxelToBrick> finalData=new ArrayList<VoxelToBrick>();
    static ArrayList<VoxelToBrick> finalBoundaryData=new ArrayList<VoxelToBrick>();
	//using a 3d array to store the position for each voxel in the arraylist coordinate-list
	static int [][][]ivalue=new int[20][20][20];
    int[][][] newIndex = new int[MainFrame.width][MainFrame.height][MainFrame.depth+2];
    static int i1,i2,i3,i4;
    int x,y,z,indexfront, indexback;
    String Tsequence="TTTTTTTT";

    //constructor for the class
	public CoordinatesSequenceMap() {

    //System.out.println("Coordinate-sequence-map called");
	//clear all arraylists
		coordinatesList.clear();
        randomSequencesList.clear();
        sequencesList.clear();
        for (int k = 0; k <= MainFrame.depth; k++)
            for (int j = 0; j < MainFrame.height; j++)
                for (int i = 0; i < MainFrame.width; i++)
                    ivalue[i][j][k] = -1;

		runRandomSequences();
		removeComplement();
        addCoordinates();
        identifyBoundaryVoxels();
		assignOrientation();
		assignDomain();
		assignSequences();
        assignProtectorSequences();
        finalData.clear();
        finalBoundaryData.clear();
        MainFrame.enableGraph();
        convertToT();
        removeTVoxels();
        //set all values in newIndex to -1
        for(int i = 0; i<MainFrame.width; i++)
            for(int j = 0; j<MainFrame.height; j++)
                for(int k = 0; k<=MainFrame.depth; k++)
                    newIndex [i][j][k] = -1;
        setBrickList();
        addHelixNumber();

        if(MainFrame.isBoundaryCalled)
            attachBoundaryBricks();
		
	}

    //STEP 1: fetch random sequences from SequencesFile.csv and save to the arraylist - randomSequencesList
    public static void runRandomSequences() {
	 
		String csvFile = "SequencesFile.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	 		    // use comma as separator
                randomSequencesList.add(new DNASequence(line.toString()));
			}
		}
        catch (FileNotFoundException e) {
			e.printStackTrace();
		}
        catch (IOException e) {
			e.printStackTrace();
		}
        finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	  }

    //STEP 2: (optional) check if any of the random sequences is a complement of the other
	public static void removeComplement(){
        //This function checks the random-sequence-list for complementary strands of each domain.
        //If any such strand is found, its complementary strand is removed from the arraylist.
        //After checking, all the strands are added to the sequence-list (after changing the length of the strands)
        //and are ready to be assigned to voxels.
		  int i,k;
		  String complement="";
		  System.out.println("size of list"+ randomSequencesList.size());
		  for(i=0;i< randomSequencesList.size();i++){
			  sequencesList.add(new DNASequence(changesize(randomSequencesList.get(i).getSequence())));
		  }
            /*
          for(i=0;i<sequencesList.size();i++)
              for(int j=i+1;j<sequencesList.size();j++) {
                  System.out.print("checking for"+i+" "+j);
                  if (sequencesList.get(i).equals(negateSeqRev(sequencesList.get(j).getSequence()))) {
                      sequencesList.remove(j);
                      System.out.print("deleted from sequencesList" + sequencesList.get(j).getSequence());
                      j--;
                  }
              }*/
	  }

    //STEP 3: get rid of null pointer by reducing size of sequence length to 8
	public static String changesize(String domainSeq) {
	        String Seq = "";
	        int stringLength = domainSeq.length();

	        for (int i = 0; i <8; i++) {
	            if (domainSeq.charAt(i) == 'A') {
	                Seq += "A";
	            } else if (domainSeq.charAt(i) == 'T') {
	                Seq += "T";
	            } else if (domainSeq.charAt(i) == 'G') {
	                Seq += "G";
	            } else if (domainSeq.charAt(i) == 'C') {
	                Seq += "C";
	            }
	        }
	        //System.out.println(""+Seq);
	        return (Seq);
	    }

    //STEP 4: save the coordinates of the 3D canvas into the arraylist - coordinatesList and identify the head and tail protector bricks (if enabled)
    public static void addCoordinates(){
        //this function stores all coordinates into the arrayList coordinatesList and marks them as removed=true or removed=false
        int count=0;
        int x,y,z;
        for(int k = 0; k <= MainFrame.depth; k++)
            for(int j = 0; j < MainFrame.height; j++)
                for(int i = 0; i<MainFrame.width; i++){

                    coordinatesList.add(new XYZCoordinates(i, j, k));
                    ivalue[i][j][k] = count;
                    //by default, assume all min-z protector bricks to be absent
                    if(k == 0){
                        MainFrame.deletedCoordinates[i][j][k] = true;
                    }
                    //while storing the voxel coordinates, find out if it has been deleted
                    if(MainFrame.deletedCoordinates[i][j][k] == true){
                        coordinatesList.get(count).isRemoved = true;
                    }
                    count++;
                }
        //now we identify the head bricks i.e. the coordinates for min. z for each (x,y) pair so that protector bricks may be attached to them.
        z = 1;
        for(int i = 0; i<MainFrame.width; i++)
            for(int j = 0; j<MainFrame.height; j++){
                while(coordinatesList.get(ivalue[i][j][z]).isRemoved == true && z < MainFrame.depth){
                    z++;
                }
                coordinatesList.get(ivalue[i][j][z]).isHeadBrick=true;
            }
        ////now we identify the tail bricks i.e. the coordinates for max. z for each (x,y) pair so that protector bricks may be added to them.
        z = MainFrame.depth;
        for(int i = MainFrame.width-1;i>=0;i--)
            for(int j = MainFrame.height-1;j>=0;j--) {
                while (coordinatesList.get(ivalue[i][j][z]).isRemoved == true && z > 0) {
                    z--;
                }
                coordinatesList.get(ivalue[i][j][z]).isTailBrick = true;
            }
        //adding the protector bricks for the head and tail bricks
        int s=coordinatesList.size();
        if(MainFrame.isMinZProtectorPlaneEnabled==true){
            for(int i = 0; i<s; i++){

                if(coordinatesList.get(i).isHeadBrick && coordinatesList.get(i).isRemoved == false){
                    x = coordinatesList.get(i).x;
                    y = coordinatesList.get(i).y;
                    z = coordinatesList.get(i).z-1;
                    System.out.println("Head protector brick to be added at " + x + " " + y + " " + z+ "and ivalue is"+ivalue[x][y][z]);
                    ///when a protector brick is added in place of a removed brick,
                    // in order to avoid adding a new element to the arraylist,
                    // we simply set the removed brick to head brick.
                    if(coordinatesList.get(ivalue[x][y][z]).isRemoved == true) {
                        coordinatesList.get(ivalue[x][y][z]).isHeadBrick = true;
                    //    System.out.println("Head protector brick added at " + x + " " + y + " " + z);
                    }
                    else{
                        System.out.println("error in finding head brick at " + x + " " + y + " " + z);
                    }
                    //the previously assigned head brick is now set to false as now the protector brick is the new head brick.
                    coordinatesList.get(i).isHeadBrick = false;
                }
            }
            //when a protector brick is added in place of a removed brick, the isRemoved condition is set to false.
            for(int i = 0; i<coordinatesList.size(); i++) {
                if (coordinatesList.get(i).isHeadBrick && coordinatesList.get(i).isRemoved == true) {
                    coordinatesList.get(i).isRemoved = false;
                }
            }
        }
        if(MainFrame.isMaxZProtectorPlaneEnabled==true){
            for(int i = 0; i<s; i++){
                if(coordinatesList.get(i).isTailBrick && coordinatesList.get(i).isRemoved==false){
                    // nothing to do
                }
            }
        }
    }

    //STEP 5: identify all the half bricks that can be converted to boundary bricks
    public void identifyBoundaryVoxels(){
        int minX = 0, minY = 0, x, y;
        int maxX = MainFrame.width-1;
        int maxY = MainFrame.height-1;
        int maxZ = MainFrame.depth+1;
        int minZ = 1;

        //no use
        if (MainFrame.isMaxZProtectorPlaneEnabled)
            maxZ = MainFrame.depth+1;
        if (MainFrame.isMinZProtectorPlaneEnabled)
            minZ = 0;

        for(int z = minZ; z<maxZ; z++)
            for(y = 0; y<MainFrame.height; y++){
                while (coordinatesList.get(ivalue[minX][y][z]).isRemoved == true && minX < MainFrame.width) {
                    minX++;
                }
                if(coordinatesList.get(ivalue[minX][y][z]).isHeadBrick == false) {
                    coordinatesList.get(ivalue[minX][y][z]).isLeftmostBrick = true;
                    //System.out.println("Leftmost brick at " + minX + " " + y + " " + z);
                }

                while (coordinatesList.get(ivalue[maxX][y][z]).isRemoved == true&&maxX>0) {
                    maxX--;
                }
                if(coordinatesList.get(ivalue[maxX][y][z]).isHeadBrick == false) {
                    coordinatesList.get(ivalue[maxX][y][z]).isRightmostBrick = true;
                    //System.out.println("Rightmost brick at " + maxX + " " + y + " " + z);
                }

            }

        for(int z = minZ; z<maxZ; z++)
            for(x = 0; x<MainFrame.width; x++){
                while (coordinatesList.get(ivalue[x][minY][z]).isRemoved == true&&minY<MainFrame.height) {
                    minY++;
                }
                if(coordinatesList.get(ivalue[x][minY][z]).isHeadBrick == false) {
                    coordinatesList.get(ivalue[x][minY][z]).isTopmostBrick = true;
                    //System.out.println("Topmost brick at " + x + " " + minY + " " + z);
                }

                while (coordinatesList.get(ivalue[x][maxY][z]).isRemoved == true&&maxY>0) {
                    maxY--;
                }
                if(coordinatesList.get(ivalue[x][maxY][z]).isHeadBrick == false) {
                    coordinatesList.get(ivalue[x][maxY][z]).isBottommostBrick = true;
                    //System.out.println("Bottommost brick at " + x + " " + maxY + " " + z);
                }
            }
    }

    //STEP 6: Assign orientation for all voxels in the coordinatesList
	public static void assignOrientation(){
		for(int i=0;i<coordinatesList.size();i++){
			if(coordinatesList.get(i).z%4==0){
				coordinatesList.get(i).orientation='n';
			}
			else if(coordinatesList.get(i).z%4==1){
				coordinatesList.get(i).orientation='w';
			}
			else if(coordinatesList.get(i).z%4==2){
				coordinatesList.get(i).orientation='s';
			}
			else{
				coordinatesList.get(i).orientation='e';
			}
			
		}
	}

	//STEP 7: Assign domain numbers (1-2 or 3-4) to each voxel in the coordinate-list
	public static void assignDomain(){

		for(int i=0;i<coordinatesList.size();i++){
			//Identification of north-bricks
			if(coordinatesList.get(i).orientation=='n'){
				//identifying half-north-bricks
				// 1. when height is even half-brick is at coordinates x%2=0 and y=0 or at y=height-1 and x%2=0
                // 2. when height is odd half-brick is at coordinates x%2=0 and y=0 or at y=height-1 and x%2=1
				if(coordinatesList.get(i).y==0&&coordinatesList.get(i).x%2==0){
					coordinatesList.get(i).editdomain(12);
					coordinatesList.get(i).isHalfBrick=true;
				}
				else if(MainFrame.height%2==0&&coordinatesList.get(i).y==MainFrame.height-1&&coordinatesList.get(i).x%2==0){
							coordinatesList.get(i).editdomain(34);
							coordinatesList.get(i).isHalfBrick=true;
				}
                else if(MainFrame.height%2==1&&coordinatesList.get(i).y==MainFrame.height-1&&coordinatesList.get(i).x%2==1){
                    coordinatesList.get(i).editdomain(34);
                    coordinatesList.get(i).isHalfBrick=true;
                }
						
				//identifying other full-north-bricks
				else if(((coordinatesList.get(i).y%2==1&&coordinatesList.get(i).x%2==0)&&
						(coordinatesList.get(i).y!=MainFrame.height-1))||
						(coordinatesList.get(i).y%2==0&&coordinatesList.get(i).x%2==1)&&
                                (coordinatesList.get(i).y!=MainFrame.height-1)){
					coordinatesList.get(i).editdomain(34);
				}
				else
					coordinatesList.get(i).editdomain(12);
			}
			//Identifying west-bricks
			else if(coordinatesList.get(i).orientation=='w'){
				//Identifying half-west-bricks
				// 1. when width is even, half-bricks are at coordinates x=0 and y%2=1 or at y%2=1 and x=width-1
                // 2. when width is odd, half-bricks are at coordinates x=0 and y%2=1 or at y%2=0 and x=width-1
                if(coordinatesList.get(i).x==0&&coordinatesList.get(i).y%2==1){
					coordinatesList.get(i).editdomain(12);
					coordinatesList.get(i).isHalfBrick=true;
				}
                else if(MainFrame.width%2==0&&coordinatesList.get(i).x==MainFrame.width-1&&coordinatesList.get(i).y%2==1){
                    coordinatesList.get(i).editdomain(34);
                    coordinatesList.get(i).isHalfBrick=true;
                }
				else if(MainFrame.width%2==1&&coordinatesList.get(i).x==MainFrame.width-1&&coordinatesList.get(i).y%2==0){
							coordinatesList.get(i).editdomain(34);
							coordinatesList.get(i).isHalfBrick=true;
						}
						
				//Identifying other full-west-bricks
				else if(((coordinatesList.get(i).y%2==1&&coordinatesList.get(i).x%2==0)&&
						(coordinatesList.get(i).x!=0))||
						(coordinatesList.get(i).y%2==0&&coordinatesList.get(i).x%2==1)){
					coordinatesList.get(i).editdomain(12);
				}
				
				else
					coordinatesList.get(i).editdomain(34);
			}
			//Identifying south-bricks
			else if(coordinatesList.get(i).orientation=='s'){
				//Identifying half-south-bricks
				// 1. when height is even , half-bricks are at coordinates x%2=1 and y=0 or at y=height-1 and x%2=1
                // 2. when height is odd , half-bricks are at coordinates x%2=1 and y=0 or at y=height-1 and x%2=0
                if(coordinatesList.get(i).y==0&&coordinatesList.get(i).x%2==1){
					coordinatesList.get(i).editdomain(34);
					coordinatesList.get(i).isHalfBrick=true;
				}
				else if(MainFrame.height%2==0&&coordinatesList.get(i).y==MainFrame.height-1&&coordinatesList.get(i).x%2==1){
							coordinatesList.get(i).editdomain(12);
							coordinatesList.get(i).isHalfBrick=true;
				}
                else if(MainFrame.height%2==1&&coordinatesList.get(i).y==MainFrame.height-1&&coordinatesList.get(i).x%2==0){
                    coordinatesList.get(i).editdomain(12);
                    coordinatesList.get(i).isHalfBrick=true;
                }
				
				//Identifying other full-south-bricks
				else if(((coordinatesList.get(i).y%2==1&&coordinatesList.get(i).x%2==1)&&
						(coordinatesList.get(i).y!=MainFrame.height-1))||
						(coordinatesList.get(i).y%2==0&&coordinatesList.get(i).x%2==0)&&
                                (coordinatesList.get(i).y!=MainFrame.height-1)){
					coordinatesList.get(i).editdomain(12);
				}
				
				else
					coordinatesList.get(i).editdomain(34);
			}
			//Identifying east-bricks
			else if(coordinatesList.get(i).orientation=='e'){
				//Identifying half-east-bricks
				// 1. when width is even ,the half-bricks are at coordinates x=0 and y%2=0 or at y%2=0 and x=width-1
                // 2. when width is odd ,the half-bricks are at coordinates x=0 and y%2=0 or at y%2=1 and x=width-1
				if(coordinatesList.get(i).x==0&&coordinatesList.get(i).y%2==0){
					coordinatesList.get(i).editdomain(34);
					coordinatesList.get(i).isHalfBrick=true;
				}
				else if(MainFrame.width%2==0&&coordinatesList.get(i).x==MainFrame.width-1&&coordinatesList.get(i).y%2==0){
							coordinatesList.get(i).editdomain(12);
							coordinatesList.get(i).isHalfBrick=true;
						}
                else if(MainFrame.width%2==1&&coordinatesList.get(i).x==MainFrame.width-1&&coordinatesList.get(i).y%2==1){
                    coordinatesList.get(i).editdomain(12);
                    coordinatesList.get(i).isHalfBrick=true;
                }
				
				//Identifying other full-east-bricks
				else if(((coordinatesList.get(i).y%2==0&&coordinatesList.get(i).x%2==1)&&
						(coordinatesList.get(i).x!=MainFrame.width-1))||
						(coordinatesList.get(i).y%2==1&&coordinatesList.get(i).x%2==0)&&
                                (coordinatesList.get(i).x!=MainFrame.width-1)){
					coordinatesList.get(i).editdomain(12);
				}
				
				else
					coordinatesList.get(i).editdomain(34);
			}
		}
	}

    //STEP 8: Assign sequences to the domains in the coordinate-list after checking the GC content.
	public static void assignSequences(){
    //NOTE: all domains are assigned sequences whether they have been deleted or not.
		for(int i=0;i<coordinatesList.size();i++){

	        //System.out.println("assigning for "+coordinatesList.get(i).x+""+coordinatesList.get(i).y+
	        //	""+coordinatesList.get(i).z);

            //Assign complete random sequences for the first plane of bricks (z=1) since protector bricks start at z = 0
			if(coordinatesList.get(i).z==1){
				if(coordinatesList.get(i).domain==12){
                    do {
                        coordinatesList.get(i).Domain1 = AssignRandom(1);
                        coordinatesList.get(i).Domain2 = AssignRandom(2);
                        if(!checkGCContent(coordinatesList.get(i).Domain1 ,coordinatesList.get(i).Domain2)){
                            sequencesList.get(i1).isUsed=false;
                            sequencesList.get(i2).isUsed=false;
                        }
                    }while(!checkGCContent(coordinatesList.get(i).Domain1 ,coordinatesList.get(i).Domain2));
                    //System.out.println("assigned 1");
					coordinatesList.get(i).Domain4=null;
					coordinatesList.get(i).Domain3=null;
	                //System.out.println("assigned seq "+coordinatesList.get(i).x+""+coordinatesList.get(i).y+
	                //""+coordinatesList.get(i).z+"for coordinates"+coordinatesList.get(i).domain);
				}
				else if(coordinatesList.get(i).domain==34){
                    do {
                        coordinatesList.get(i).Domain3 = AssignRandom(3);
                        coordinatesList.get(i).Domain4 = AssignRandom(4);
                        if(!checkGCContent(coordinatesList.get(i).Domain3 ,coordinatesList.get(i).Domain4)){
                            sequencesList.get(i3).isUsed=false;
                            sequencesList.get(i4).isUsed=false;
                        }
                    }while (!checkGCContent(coordinatesList.get(i).Domain3 ,coordinatesList.get(i).Domain4));
                    //System.out.println("assigned 2");
					coordinatesList.get(i).Domain1=null;
					coordinatesList.get(i).Domain2=null;
					
					//System.out.println("assigned seq "+coordinatesList.get(i).x+""+coordinatesList.get(i).y+
					//		""+coordinatesList.get(i).z+"for coordinates"+coordinatesList.get(i).domain);
				}
			}

            //Assigning sequences for the rest of the coordinates
			else if(coordinatesList.get(i).z>1){
				//System.out.println("assigning for "+coordinatesList.get(i).x+""+coordinatesList.get(i).y+
				//		""+coordinatesList.get(i).z+"for domain "+coordinatesList.get(i).domain);
				int x,y,z,ival;
				x=coordinatesList.get(i).x;
				y=coordinatesList.get(i).y;
				z=coordinatesList.get(i).z;
				ival=ivalue[x][y][z-1];

				//System.out.println("front sequence for"+coordinatesList.get(ival).x+""+coordinatesList.get(ival).y+
				//		""+coordinatesList.get(ival).z);

				//System.out.print(" is"+coordinatesList.get(i).Domain1);
					if(coordinatesList.get(i).domain==34){
                        if(coordinatesList.get(ival).Domain1!=null)
                            coordinatesList.get(i).Domain3 = negateSeqRev(coordinatesList.get(ival).Domain1);
                        else
                            coordinatesList.get(i).Domain3 = AssignRandom(3);
                        do {
                            coordinatesList.get(i).Domain4 = AssignRandom(4);
                            if(!checkGCContent(coordinatesList.get(i).Domain3 ,coordinatesList.get(i).Domain4)){
                                sequencesList.get(i4).isUsed=false;
                                if(coordinatesList.get(ival).Domain1==null)
                                    sequencesList.get(i3).isUsed=false;
                            }
                        }while(!checkGCContent(coordinatesList.get(i).Domain3 ,coordinatesList.get(i).Domain4));
                   //System.out.println("assigned 3");
					}
					else if(coordinatesList.get(i).domain==12){

                        if(coordinatesList.get(ival).Domain1!=null)
                            coordinatesList.get(i).Domain2 = negateSeqRev(coordinatesList.get(ival).Domain4);
                        else
                            coordinatesList.get(i).Domain2 = AssignRandom(2);
                        do {
                            coordinatesList.get(i).Domain1 = AssignRandom(1);
                            if(!checkGCContent(coordinatesList.get(i).Domain1 ,coordinatesList.get(i).Domain2)){
                                sequencesList.get(i1).isUsed=false;
                                if(coordinatesList.get(ival).Domain4==null)
                                    sequencesList.get(i2).isUsed=false;
                            }
                        }while(!checkGCContent(coordinatesList.get(i).Domain1 ,coordinatesList.get(i).Domain2));
                    // System.out.println("assigned 4");
					}
			}
		}
	}

    //STEP 9: Assign T sequences to protector bricks
    public static void assignProtectorSequences(){
        String Tsequence="TTTTTTTT";
        if(MainFrame.isMinZProtectorPlaneEnabled){
            for(int i = 0; i<coordinatesList.size(); i++){
                if(coordinatesList.get(i).isHeadBrick){
                    int x=coordinatesList.get(i).x;
                    int y=coordinatesList.get(i).y;
                    int z=coordinatesList.get(i).z;

                    if(coordinatesList.get(i).domain==12) {
                        coordinatesList.get(i).Domain2 = Tsequence;
                        coordinatesList.get(i).Domain1 = negateSeqRev(coordinatesList.get(ivalue[x][y][z+1]).Domain3);
                    }
                    else if(coordinatesList.get(i).domain==34) {
                        coordinatesList.get(i).Domain3 = Tsequence;
                        coordinatesList.get(i).Domain4 = negateSeqRev(coordinatesList.get(ivalue[x][y][z+1]).Domain2);
                    }

                    //System.out.println("found head protector sequence at"+coordinatesList.get(i).x+","+coordinatesList.get(i).y+","+coordinatesList.get(i).z+
                    //        "with sequence"+coordinatesList.get(i).Domain1+" "+coordinatesList.get(i).Domain2+" "+coordinatesList.get(i).Domain3+" "+coordinatesList.get(i).Domain4);
                }
            }
        }

        if(MainFrame.isMaxZProtectorPlaneEnabled){
            for(int i = 0; i<coordinatesList.size(); i++){
                if(coordinatesList.get(i).isTailBrick){
                    int x=coordinatesList.get(i).x;
                    int y=coordinatesList.get(i).y;
                    int z=coordinatesList.get(i).z;
                    if(coordinatesList.get(i).domain==12) {
                        coordinatesList.get(i).Domain1 = coordinatesList.get(i).Domain1.replace(coordinatesList.get(i).Domain1,Tsequence);
                        //not needed as no new plane is added
                        coordinatesList.get(i).Domain2 = negateSeqRev(coordinatesList.get(ivalue[x][y][z-1]).Domain4);
                    }
                    else if(coordinatesList.get(i).domain==34) {
                        coordinatesList.get(i).Domain4 = coordinatesList.get(i).Domain4.replace(coordinatesList.get(i).Domain4,Tsequence);
                        //not needed as no new plane is added
                        coordinatesList.get(i).Domain3 = negateSeqRev(coordinatesList.get(ivalue[x][y][z-1]).Domain1);
                    }
                    //System.out.println("found tail protector sequence at"+coordinatesList.get(i).x+","+coordinatesList.get(i).y+","+coordinatesList.get(i).z+
                    //        "with sequence"+coordinatesList.get(i).Domain1+" "+coordinatesList.get(i).Domain2+" "+coordinatesList.get(i).Domain3+" "+coordinatesList.get(i).Domain4);
                }
            }
        }

    }

    //STEP 10: Get a random DNA sequence from the seq-list and assign it to a voxel in the coordinate-list.
	public static String AssignRandom(int domain){

		Random rand = new Random(); 
		int randomIndex;
		
		randomIndex= rand.nextInt(sequencesList.size());
	    if(sequencesList.get(randomIndex).isUsed==true)
		while(sequencesList.get(randomIndex).isUsed==true||isMatching(sequencesList.get(randomIndex).getSequence())==true){
			randomIndex= rand.nextInt(sequencesList.size());
		}

        //i1, i2, i3, i4 are used to keep track of the index for setting the isUsed parameter to false,
        // if the sequence does not satisfy the GC constraint.
        if(domain==1) {
            i1 = randomIndex;
        }
        else if(domain==2) {
            i2 = randomIndex;
        }
        else if(domain==3) {
            i3 = randomIndex;
        }
        else {
            i4 = randomIndex;
        }
		
		sequencesList.get(randomIndex).isUsed=true;
		return(sequencesList.get(randomIndex).getSequence());
	    
	}

    //not being used yet!!!
    public static boolean isMatching(String domain){
        boolean isMatching=false;

            for (int i = 0; i < coordinatesList.size(); i++) {
                if( (domain.equals(coordinatesList.get(i).Domain1) )||( domain.equals(coordinatesList.get(i).Domain2)) ||
                        (domain.equals(coordinatesList.get(i).Domain3)) || (domain.equals(coordinatesList.get(i).Domain4))) {
                    isMatching = true;
                    System.out.println(domain+" matches with"+coordinatesList.get(i).Domain1+" or"+coordinatesList.get(i).Domain2+" or"
                            +coordinatesList.get(i).Domain3+" or"+coordinatesList.get(i).Domain4);
                }

        }
        return isMatching;
    }

    //Used in STEP 8. This function checks the GC content of the DNA sequence
    public static boolean checkGCContent(String d1, String d2){

        int l1=d1.length();
        int l2=d2.length();

        int GCcount=0,i;
        boolean isGCsatisfied=true;

            for (i=0; i<l1; i++) {
                if ((d1.charAt(i) == 'G')||(d1.charAt(i) == 'C'))
                    GCcount+=1;
            }
            for (i=0; i<l2; i++) {
                if ((d2.charAt(i) == 'G')||(d2.charAt(i) == 'C'))
                    GCcount+=1;
            }


            if(!(GCcount<=MainFrame.GCupperLimit&&GCcount>=MainFrame.GClowerLimit)){

                isGCsatisfied=false;

            }
       // System.out.println("GC Content:" + GCcount);
        return(isGCsatisfied);

    }

    //Used in STEP 8. This function returns the complementary reverse sequence of a given DNA string.
	public static String negateSeqRev(String domainSeq) {
	        String Seq = "";
	        int stringLength = domainSeq.length();

	        for (int i = stringLength-1; i >=0; i--) {
	            if (domainSeq.charAt(i) == 'A') {
	                Seq += "T";
	            } else if (domainSeq.charAt(i) == 'T') {
	                Seq += "A";
	            } else if (domainSeq.charAt(i) == 'G') {
	                Seq += "C";
	            } else if (domainSeq.charAt(i) == 'C') {
	                Seq += "G";
	            }
	        }
	        return (Seq);
	    }

    //this function is not being used !!!
	public static String negateSeq(String domainSeq) {
	        String negateSeq = "";
	        int stringLength = domainSeq.length();
	        System.out.println("StringLength:"+stringLength);
	        for (int i = 0; i <stringLength; i++) {
	            if (domainSeq.charAt(i) == 'A') {
	                negateSeq += "T";
	            } else if (domainSeq.charAt(i) == 'T') {
	                negateSeq += "A";
	            } else if (domainSeq.charAt(i) == 'G') {
	                negateSeq += "C";
	            } else if (domainSeq.charAt(i) == 'C') {
	                negateSeq += "G";
	            }
	        }
	        return (negateSeq);
	    }

    //STEP 11: Replace the remaining complementary strands of the deleted voxels with (8-length) poly-T sequences.
    public void convertToT(){
        for(int i=0;i<coordinatesList.size();i++){
            x=coordinatesList.get(i).x;
            y=coordinatesList.get(i).y;
            z=coordinatesList.get(i).z;

            if(coordinatesList.get(i).isRemoved==true ){
                //System.out.println("Removed found in convert-to-T:"+x+""+y+""+z);
                if(z > 0&& MainFrame.isMinZProtectorPlaneEnabled || z>1&&!MainFrame.isMinZProtectorPlaneEnabled){
                    indexback=ivalue[x][y][z-1];
                    if(coordinatesList.get(indexback).isRemoved == false){
                        //System.out.println("Searching in convert-to-T:"+x+""+y+""+(z-1));
                        if(coordinatesList.get(i).domain==34)
                            coordinatesList.get(indexback).Domain1=coordinatesList.
                                    get(indexback).Domain1.replace(coordinatesList.get(indexback).Domain1, Tsequence);

                        else
                            coordinatesList.get(indexback).Domain4=coordinatesList.
                                    get(indexback).Domain4.replace(coordinatesList.get(indexback).Domain4, Tsequence);
                    }
                }
                if(z <MainFrame.depth){
                    indexfront=ivalue[x][y][z+1];
                    if(coordinatesList.get(indexfront).isRemoved == false){
                        //System.out.println("Searching in convert-to-T:"+x+""+y+""+(z+1));
                        if(coordinatesList.get(i).domain==34)
                            coordinatesList.get(indexfront).Domain2=coordinatesList.get(indexfront).Domain2.replace
                                    (coordinatesList.get(indexfront).Domain2, Tsequence);

                        else
                            coordinatesList.get(indexfront).Domain3=coordinatesList.get(indexfront).Domain3.replace
                                    (coordinatesList.get(indexfront).Domain3, Tsequence);
                    }
                }
            }
        }
    }

    //STEP 12: Identify and remove those voxels which have d3&d4 OR d1&d2 as T-sequences (i.e., when an entire voxel is composed of only Thymidine).
    public void removeTVoxels(){
        //System.out.println("Size of the arraylist: "+coordinatesList.size());
        for(int i=0;i<coordinatesList.size();i++){

            x=coordinatesList.get(i).x;
            y=coordinatesList.get(i).y;
            z=coordinatesList.get(i).z;
            //System.out.println("trying to access "+x+""+y+""+z);
            if(coordinatesList.get(i).isRemoved==false){
                if(coordinatesList.get(i).domain==34){
                    if(coordinatesList.get(i).Domain3.equals(Tsequence)&&
                            coordinatesList.get(i).Domain4.equals(Tsequence)){
                        //System.out.println("removed in removeTVoxels:"+x+""+y+""+z);
                        coordinatesList.get(i).isRemoved=true;
                    }
                }
                else if(coordinatesList.get(i).domain==12){
                    if(coordinatesList.get(i).Domain1.equals(Tsequence)&&
                            coordinatesList.get(i).Domain2.equals(Tsequence)){
                        //System.out.println("removed in removeTVoxels:"+x+""+y+""+z);
                        coordinatesList.get(i).isRemoved=true;
                    }
                }
            }
        }
    }

    //STEP 13: Combine voxels to form bricks of size=2 voxels, set them as full bricks and save them in the finalData list.
    public void setBrickList(){
        int x,y,z;
        int brickCounter = 0;
        for(int i=0;i<coordinatesList.size();i++){

            if(coordinatesList.get(i).isRemoved==false){

                x = coordinatesList.get(i).x;
                y = coordinatesList.get(i).y;
                z = coordinatesList.get(i).z;

                //Adding half-bricks with domains 3 and 4 to the final-coordinate-list.
                if(coordinatesList.get(i).isHalfBrick){
                    if(coordinatesList.get(i).domain==34) {
                        finalData.add(new VoxelToBrick(-1, -1, -1, x, y, z, null, null,
                                coordinatesList.get(i).Domain3, coordinatesList.get(i).Domain4));
                    }
                    else {
                        finalData.add(new VoxelToBrick(x, y, z, -1, -1, -1, coordinatesList.get(i).Domain1,
                                coordinatesList.get(i).Domain2, null, null));
                    }
                    finalData.get(brickCounter).isBottommostHalfBrick = coordinatesList.get(i).isBottommostBrick;
                    finalData.get(brickCounter).isTopmostHalfBrick = coordinatesList.get(i).isTopmostBrick;
                    finalData.get(brickCounter).isLeftmostHalfBrick = coordinatesList.get(i).isLeftmostBrick;
                    finalData.get(brickCounter).isRightmostHalfBrick = coordinatesList.get(i).isRightmostBrick;
                    /*System.out.println("%%%%%% half brick identified at ["+finalData.get(brickCounter).x2+" "
                            +finalData.get(brickCounter).y2+" "+finalData.get(brickCounter).z2+" "
                            +finalData.get(brickCounter).x3+" "+finalData.get(brickCounter).y3+" "
                            +finalData.get(brickCounter).z3+"] %%%%%%%");*/
                }
                else{
                    //Identifying full and half north-bricks and adding them to the final-coordinate-list.
                    if(coordinatesList.get(i).orientation=='n'){
                        if(coordinatesList.get(i).domain==34){
                            if(coordinatesList.get(ivalue[x][y+1][z]).isRemoved==false){

                                finalData.add(new VoxelToBrick(x,y+1,z,x,y,z,
                                        coordinatesList.get(ivalue[x][y+1][z]).Domain1,
                                        coordinatesList.get(ivalue[x][y+1][z]).Domain2,
                                        coordinatesList.get(i).Domain3, coordinatesList.get(i).Domain4));

                                coordinatesList.get(ivalue[x][y+1][z]).isRemoved=true;
                                newIndex[x][y+1][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        coordinatesList.get(i).Domain3, coordinatesList.get(i).Domain4));
                                /*System.out.println("??????not half brick identified at ["+finalData.get(brickCounter).x2+" "
                                        +finalData.get(brickCounter).y2+" "+finalData.get(brickCounter).z2+" "
                                        +finalData.get(brickCounter).x3+" "+finalData.get(brickCounter).y3+" "
                                        +finalData.get(brickCounter).z3+"] ??????");*/
                            }
                        }
                        else{
                            if(coordinatesList.get(ivalue[x][y-1][z]).isRemoved==false){

                                finalData.add(new VoxelToBrick(x,y,z,x,y-1,z,
                                        coordinatesList.get(i).Domain1,
                                        coordinatesList.get(i).Domain2,
                                        coordinatesList.get(ivalue[x][y-1][z]).Domain3,
                                        coordinatesList.get(ivalue[x][y-1][z]).Domain4));

                                coordinatesList.get(ivalue[x][y-1][z]).isRemoved=true;
                                newIndex[x][y-1][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(x,y,z,-1,-1,-1,coordinatesList.get(i).Domain1,
                                        coordinatesList.get(i).Domain2, null, null));
                                /*System.out.println("??????not half brick identified at ["+finalData.get(brickCounter).x2+" "
                                        +finalData.get(brickCounter).y2+" "+finalData.get(brickCounter).z2+" "
                                        +finalData.get(brickCounter).x3+" "+finalData.get(brickCounter).y3+" "
                                        +finalData.get(brickCounter).z3+"] ??????");*/
                            }

                        }
                    }

                    //Identifying full and half west-bricks and adding them to the final-coordinate-list.
                    if(coordinatesList.get(i).orientation=='w'){
                        if(coordinatesList.get(i).domain==34){
                            if(coordinatesList.get(ivalue[x+1][y][z]).isRemoved==false){
                                finalData.add(new VoxelToBrick(x+1,y,z,x,y,z,
                                        coordinatesList.get(ivalue[x+1][y][z]).Domain1,
                                        coordinatesList.get(ivalue[x+1][y][z]).Domain2,
                                        coordinatesList.get(i).Domain3, coordinatesList.get(i).Domain4));

                                coordinatesList.get(ivalue[x+1][y][z]).isRemoved=true;
                                newIndex[x+1][y][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        coordinatesList.get(i).Domain3, coordinatesList.get(i).Domain4));
                                /*System.out.println("??????not half brick identified at ["+finalData.get(brickCounter).x2+" "
                                        +finalData.get(brickCounter).y2+" "+finalData.get(brickCounter).z2+" "
                                        +finalData.get(brickCounter).x3+" "+finalData.get(brickCounter).y3+" "
                                        +finalData.get(brickCounter).z3+"] ??????");*/
                            }
                        }
                        else{
                            if(coordinatesList.get(ivalue[x-1][y][z]).isRemoved==false){
                                finalData.add(new VoxelToBrick(x,y,z,x-1,y,z,
                                        coordinatesList.get(i).Domain1,
                                        coordinatesList.get(i).Domain2,
                                        coordinatesList.get(ivalue[x-1][y][z]).Domain3,
                                        coordinatesList.get(ivalue[x-1][y][z]).Domain4));

                                coordinatesList.get(ivalue[x-1][y][z]).isRemoved=true;
                                newIndex[x-1][y][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(x,y,z,-1,-1,-1,coordinatesList.get(i).Domain1,
                                        coordinatesList.get(i).Domain2, null, null));
                            }
                        }
                    }

                    //Identifying full and half south-bricks and adding them to the final-coordinate-list.
                    if(coordinatesList.get(i).orientation=='s'){
                        if(coordinatesList.get(i).domain==12){
                            if(coordinatesList.get(ivalue[x][y+1][z]).isRemoved==false){
                                finalData.add(new VoxelToBrick(x,y,z,x,y+1,z,
                                        coordinatesList.get(i).Domain1, coordinatesList.get(i).Domain2,
                                        coordinatesList.get(ivalue[x][y+1][z]).Domain3,
                                        coordinatesList.get(ivalue[x][y+1][z]).Domain4
                                ));

                                coordinatesList.get(ivalue[x][y+1][z]).isRemoved=true;
                                newIndex[x][y+1][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        coordinatesList.get(i).Domain1, coordinatesList.get(i).Domain2));
                            }
                        }
                        else{
                            if(coordinatesList.get(ivalue[x][y-1][z]).isRemoved==false){
                                finalData.add(new VoxelToBrick(x,y-1,z,x,y,z,
                                        coordinatesList.get(ivalue[x][y-1][z]).Domain1,
                                        coordinatesList.get(ivalue[x][y-1][z]).Domain2,
                                        coordinatesList.get(i).Domain3,
                                        coordinatesList.get(i).Domain4
                                ));

                                coordinatesList.get(ivalue[x][y-1][z]).isRemoved=true;
                                newIndex[x][y-1][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z, null, null,coordinatesList.get(i).Domain3,
                                        coordinatesList.get(i).Domain4));
                            }
                        }
                    }
                    //Identifying full and half east-bricks and adding them to the final-coordinate-list.
                    if(coordinatesList.get(i).orientation=='e'){
                        if(coordinatesList.get(i).domain==12){
                            if(coordinatesList.get(ivalue[x+1][y][z]).isRemoved==false){
                                finalData.add(new VoxelToBrick(x,y,z,x+1,y,z,
                                        coordinatesList.get(i).Domain1, coordinatesList.get(i).Domain2,
                                        coordinatesList.get(ivalue[x+1][y][z]).Domain3,
                                        coordinatesList.get(ivalue[x+1][y][z]).Domain4
                                ));

                                coordinatesList.get(ivalue[x+1][y][z]).isRemoved=true;
                                newIndex[x+1][y][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(x,y,z,-1,-1,-1,null, null,
                                        coordinatesList.get(i).Domain1, coordinatesList.get(i).Domain2));
                            }
                        }
                        else{
                            if(coordinatesList.get(ivalue[x-1][y][z]).isRemoved==false){
                                finalData.add(new VoxelToBrick(x-1,y,z,x,y,z,
                                        coordinatesList.get(ivalue[x-1][y][z]).Domain1,
                                        coordinatesList.get(ivalue[x-1][y][z]).Domain2,
                                        coordinatesList.get(i).Domain3,
                                        coordinatesList.get(i).Domain4
                                ));

                                coordinatesList.get(ivalue[x-1][y][z]).isRemoved=true;
                                newIndex[x-1][y][z] = brickCounter;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,coordinatesList.get(i).Domain3,
                                        coordinatesList.get(i).Domain4));
                            }

                        }
                    }
                }
                //Remove the individual voxel after combining it as a full brick in the final-coordinate-list.
                coordinatesList.get(i).isRemoved=true;
                newIndex[x][y][z] = brickCounter;
                brickCounter++;
            }

        }
    }

    //STEP 14: Assign helix number to each voxel
    public void addHelixNumber(){
        int x2, x3, y2, y3;

        for (int i = 0; i<finalData.size(); i++) {
            x2= finalData.get(i).x2;
            x3= finalData.get(i).x3;
            y2= finalData.get(i).y2;
            y3= finalData.get(i).y3;
            if (x2!=-1) {
                if(y2==0) {
                    finalData.get(i).helix1 = x2;
                    finalData.get(i).helix2 = x2;
                }
                else if (y2%2==1) {
                    finalData.get(i).helix1 = (y2 + 1) * MainFrame.width - (x2 + 1);
                    finalData.get(i).helix2 = finalData.get(i).helix1;

                }
                else {
                    finalData.get(i).helix1 = MainFrame.width * y2 + x2;
                    finalData.get(i).helix2 = finalData.get(i).helix1;
                }
            }
            if (x3!=-1){
                if(y3==0) {
                    finalData.get(i).helix3 = x3;
                    finalData.get(i).helix4 = finalData.get(i).helix3;
                }
                else if (y3%2==1) {
                    finalData.get(i).helix3 = (y3 + 1) * MainFrame.width - (x3 + 1);
                    finalData.get(i).helix4 = finalData.get(i).helix3;
                }
                else {
                    finalData.get(i).helix3 = MainFrame.width * y3 + x3;
                    finalData.get(i).helix4 = finalData.get(i).helix3;
                }
            }
        }
    }

    //STEP 15: If set boundary bricks option is enabled, combine bricks to form 48-nt boundary bricks
    public void attachBoundaryBricks(){

        int x,y,z;
        int fullBrickIndex;
        String d1,d2;
        int h5, h6;

        finalBoundaryData = finalData;
        for(int i = 0; i < finalBoundaryData.size(); i++){
            if(finalBoundaryData.get(i).isLeftmostHalfBrick || finalBoundaryData.get(i).isBottommostHalfBrick || finalBoundaryData.get(i).isTopmostHalfBrick ||
                    finalBoundaryData.get(i).isRightmostHalfBrick) {
                if (finalBoundaryData.get(i).x2 != -1) {
                    x = finalBoundaryData.get(i).x2;
                    y = finalBoundaryData.get(i).y2;
                    z = finalBoundaryData.get(i).z2;
                    System.out.println(x + " " + y + " " + z + "  " + finalBoundaryData.get(i).Domain1);
                    d1 = new String(finalBoundaryData.get(i).Domain1);
                    d2 = new String(finalBoundaryData.get(i).Domain2);
                    h5 = finalBoundaryData.get(i).helix2;
                    h6 = finalBoundaryData.get(i).helix1;
                } else {
                    x = finalBoundaryData.get(i).x3;
                    y = finalBoundaryData.get(i).y3;
                    z = finalBoundaryData.get(i).z3;
                    d1 = new String(finalBoundaryData.get(i).Domain3);
                    d2 = new String(finalBoundaryData.get(i).Domain4);
                    h5 = finalBoundaryData.get(i).helix3;
                    h6 = finalBoundaryData.get(i).helix4;
                }
                if (MainFrame.isMinXBoundaryPlaneEnabled && finalBoundaryData.get(i).isLeftmostHalfBrick && z != 1 && finalBoundaryData.get(i).isRemovedAsBoundary == false) {
                    fullBrickIndex = newIndex[x][y][z - 2];
                    if (fullBrickIndex != -1) {

                        if (finalBoundaryData.get(fullBrickIndex).x1 == x && finalBoundaryData.get(fullBrickIndex).y1 == y)
                            finalBoundaryData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 12);

                        else
                            finalBoundaryData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 34);

                        finalBoundaryData.get(fullBrickIndex).isBoundary = true;
                        finalBoundaryData.get(i).isRemovedAsBoundary = true;
                        finalBoundaryData.get(i).isLeftmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for min x at" + x + " " + y + " " + z);
                    }
                }
                if (MainFrame.isMaxXBoundaryPlaneEnabled && finalBoundaryData.get(i).isRightmostHalfBrick && z != 1 && finalBoundaryData.get(i).isRemovedAsBoundary == false) {
                    fullBrickIndex = newIndex[x][y][z - 2];
                    if (fullBrickIndex != -1) {
                        if (finalBoundaryData.get(fullBrickIndex).x1 == x && finalBoundaryData.get(fullBrickIndex).y1 == y)
                            finalBoundaryData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 12);

                        else
                            finalBoundaryData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 34);
                        finalBoundaryData.get(fullBrickIndex).isBoundary = true;
                        finalBoundaryData.get(i).isRemovedAsBoundary = true;
                        finalBoundaryData.get(i).isLeftmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for max x at" + x + " " + y + " " + z);
                    }
                }
                if (MainFrame.isMinYBoundaryPlaneEnabled && finalBoundaryData.get(i).isTopmostHalfBrick && z != 1 && finalBoundaryData.get(i).isRemovedAsBoundary == false) {
                    fullBrickIndex = newIndex[x][y][z - 2];
                    if (fullBrickIndex != -1) {
                        if (finalBoundaryData.get(fullBrickIndex).x1 == x && finalBoundaryData.get(fullBrickIndex).y1 == y)
                            finalBoundaryData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 12);

                        else
                            finalBoundaryData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 34);
                        finalBoundaryData.get(fullBrickIndex).isBoundary = true;
                        finalBoundaryData.get(i).isRemovedAsBoundary = true;
                        finalBoundaryData.get(i).isLeftmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for min y at" + x + " " + y + " " + z);
                    }
                }
                if (MainFrame.isMaxYBoundaryPlaneEnabled && finalBoundaryData.get(i).isBottommostHalfBrick && z != 1 && finalBoundaryData.get(i).isRemovedAsBoundary == false) {
                    fullBrickIndex = newIndex[x][y][z - 2];
                    if (fullBrickIndex != -1) {
                        if (finalBoundaryData.get(fullBrickIndex).x1 == x && finalBoundaryData.get(fullBrickIndex).y1 == y)
                            finalBoundaryData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 12);

                        else
                            finalBoundaryData.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 34);
                        finalBoundaryData.get(fullBrickIndex).isBoundary = true;
                        finalBoundaryData.get(i).isRemovedAsBoundary = true;
                        finalBoundaryData.get(i).isLeftmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for max y at" + x + " " + y + " " + z);
                    }
                }
            }
        }

        int s = finalBoundaryData.size();
        for(int i=0;i<s;i++){
            if(finalBoundaryData.get(i).isRemovedAsBoundary == true){
                finalBoundaryData.remove(i);
                s--;
            }
        }
    }
}
