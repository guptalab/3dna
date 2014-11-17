/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA
 * Mentor: Prof. Manish K Gupta
 */

import sun.applet.Main;

import java.awt.*;
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
    public static ArrayList<XYZCoordinates> voxelList= new ArrayList<XYZCoordinates>();
    static ArrayList<VoxelToBrick> brickList=new ArrayList<VoxelToBrick>();
    static ArrayList<VoxelToBrick> boundaryBrickList=new ArrayList<VoxelToBrick>();
    //using a 3d array to store the position for each voxel in the arraylist coordinate-list
    static int [][][]ivalue=new int[20][20][20];
    int[][][] newIndex = new int[MainFrame.width][MainFrame.height][MainFrame.depth+2];
    static int i1,i2,i3,i4;
    int x,y,z,indexfront, indexback;
    String poly1T="TTTTTTTT";


    //constructor for the class
    public CoordinatesSequenceMap() {

        //clear all arraylists
        voxelList.clear();
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
        brickList.clear();
        boundaryBrickList.clear();
        MainFrame.enableGraph();
        convertToT();
        //set all values in newIndex to -1
        for(int i = 0; i<MainFrame.width; i++)
            for(int j = 0; j<MainFrame.height; j++)
                for(int k = 0; k<=MainFrame.depth; k++)
                    newIndex [i][j][k] = -1;
        setBrickList();
        addHelixNumber();

        if(MainFrame.isBoundaryCalled)
            attachBoundaryBricks();

        if(MainFrame.isXCrystalEnabled || MainFrame.isYCrystalEnabled || MainFrame.isZCrystalEnabled)
            createCrystal();

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
            sequencesList.add(new DNASequence(changeSize(randomSequencesList.get(i).getSequence())));
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
    public static String changeSize(String domainSeq) {
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

    //STEP 4: save the coordinates of the 3D canvas into the arraylist - voxelList and identify the head and tail protector bricks (if enabled)
    public static void addCoordinates() {

        int count = 0;
        int x, y, z;
        for (int k = 0; k <= MainFrame.depth; k++)
            for (int j = 0; j < MainFrame.height; j++)
                for (int i = 0; i < MainFrame.width; i++) {

                    voxelList.add(new XYZCoordinates(i, j, k));
                    ivalue[i][j][k] = count;
                    if(k == 0){
                        MainFrame.deletedCoordinates[i][j][k] = true;
                    }
                    //while storing the voxel coordinates, find out if it has been deleted
                    if (MainFrame.deletedCoordinates[i][j][k] == true) {
                        voxelList.get(count).isRemoved = true;
                    }
                    count++;
                }

        //now we identify the head bricks i.e. the coordinates for min. z for each (x,y) pair so that protector bricks may be attached to them.
        for (int i = 0; i < MainFrame.width; i++)
            for (int j = 0; j < MainFrame.height; j++) {
                z = 1;
                while (voxelList.get(ivalue[i][j][z]).isRemoved == true && z < MainFrame.depth) {
                    z++;
                }
                if(z < MainFrame.depth) // if z >= means that an entire row along z axis has been removed
                    voxelList.get(ivalue[i][j][z]).isHeadBrick = true;
            }
        ////now we identify the tail bricks i.e. the coordinates for max. z for each (x,y) pair so that protector bricks may be added to them.
        for (int i = MainFrame.width - 1; i >= 0; i--)
            for (int j = MainFrame.height - 1; j >= 0; j--) {
                z = MainFrame.depth;
                while (voxelList.get(ivalue[i][j][z]).isRemoved == true && z > 0) {
                    z--;
                }
                if (voxelList.get(ivalue[i][j][z]).isRemoved == false) { // since no new brick is added for tail brick
                    voxelList.get(ivalue[i][j][z]).isTailBrick = true;
                }
            }
        //adding the protector bricks for the head and tail bricks
        int s = voxelList.size();
        if (MainFrame.isMinZProtectorPlaneEnabled == true) {
            for (int i = 0; i < s; i++) {
                if (voxelList.get(i).isHeadBrick && voxelList.get(i).isRemoved == false) {
                    x = voxelList.get(i).x;
                    y = voxelList.get(i).y;
                    z = voxelList.get(i).z - 1;
                    ///when a protector brick is added in place of a removed brick,
                    // in order to avoid adding a new element to the arraylist,
                    // we simply set the removed brick to head brick.
                    if (voxelList.get(ivalue[x][y][z]).isRemoved == true) {
                        voxelList.get(ivalue[x][y][z]).isHeadBrick = true;
                    }

                    //the previously assigned head brick is now set to false as now the protector brick is the new head brick.
                    voxelList.get(i).isHeadBrick = false;
                }
            }
            //when a protector brick is added in place of a removed brick, the isRemoved condition is set to false.
            for (int i = 0; i < voxelList.size(); i++) {
                if (voxelList.get(i).isHeadBrick && voxelList.get(i).isRemoved == true) {
                    voxelList.get(i).isRemoved = false;
                }
            }
        }
        if (MainFrame.isMaxZProtectorPlaneEnabled == true) {
            //do nothing

        }
    }

    //STEP 5: identify all the half bricks that can be converted to boundary bricks
    public void identifyBoundaryVoxels() {
        int minX = 0, minY = 0, x, y;
        int maxX = MainFrame.width - 1;
        int maxY = MainFrame.height - 1;
        int maxZ = MainFrame.depth;
        int minZ = 1;

        if (MainFrame.isMaxZProtectorPlaneEnabled)
            maxZ = MainFrame.depth;
        if (MainFrame.isMinZProtectorPlaneEnabled)
            minZ = 0;

        for (int z = minZ; z <= maxZ; z++)
            for (y = 0; y <= MainFrame.height; y++) {
                minX = 0;
                maxX = MainFrame.width - 1;
                while (voxelList.get(ivalue[minX][y][z]).isRemoved == true && minX < MainFrame.width) {
                    minX++;
                }
                if (voxelList.get(ivalue[minX][y][z]).isRemoved == false) {
                    voxelList.get(ivalue[minX][y][z]).isLeftmostBrick = true;
                }

                while (voxelList.get(ivalue[maxX][y][z]).isRemoved == true && maxX > 0) {
                    maxX--;
                }
                if (voxelList.get(ivalue[maxX][y][z]).isRemoved == false) {
                    voxelList.get(ivalue[maxX][y][z]).isRightmostBrick = true;
                }

            }

        for (int z = minZ; z <= maxZ; z++)
            for (x = 0; x < MainFrame.width; x++) {
                minY = 0;
                maxY = MainFrame.height - 1;
                while (voxelList.get(ivalue[x][minY][z]).isRemoved == true && minY < MainFrame.height) {
                    minY++;
                }
                if (voxelList.get(ivalue[x][minY][z]).isRemoved == false) {
                    voxelList.get(ivalue[x][minY][z]).isTopmostBrick = true;
                }

                while (voxelList.get(ivalue[x][maxY][z]).isRemoved == true && maxY > 0) {
                    maxY--;
                }
                if (voxelList.get(ivalue[x][maxY][z]).isRemoved == false) {
                    voxelList.get(ivalue[x][maxY][z]).isBottommostBrick = true;
                }
            }
    }

    //STEP 6: Assign orientation for all voxels in the voxelList
    public static void assignOrientation(){
        for(int i=0;i<voxelList.size();i++){
            if(voxelList.get(i).z%4==0){
                voxelList.get(i).orientation='n';
            }
            else if(voxelList.get(i).z%4==1){
                voxelList.get(i).orientation='w';
            }
            else if(voxelList.get(i).z%4==2){
                voxelList.get(i).orientation='s';
            }
            else{
                voxelList.get(i).orientation='e';
            }

        }
    }

    //STEP 7: Assign domain numbers (1-2 or 3-4) to each voxel in the coordinate-list
    public static void assignDomain() {

        for (int i = 0; i < voxelList.size(); i++) {
            //Identification of north-bricks
            if (voxelList.get(i).orientation == 'n') {
                //identifying half-north-bricks
                // 1. when height is even half-brick is at coordinates x%2=0 and y=0 or at y=height-1 and x%2=0
                // 2. when height is odd half-brick is at coordinates x%2=0 and y=0 or at y=height-1 and x%2=1
                if (voxelList.get(i).y == 0 && voxelList.get(i).x % 2 == 0) {
                    voxelList.get(i).editdomain(12);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.height % 2 == 0 && voxelList.get(i).y == MainFrame.height - 1 && voxelList.get(i).x % 2 == 0) {
                    voxelList.get(i).editdomain(34);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.height % 2 == 1 && voxelList.get(i).y == MainFrame.height - 1 && voxelList.get(i).x % 2 == 1) {
                    voxelList.get(i).editdomain(34);
                    voxelList.get(i).isHalfBrick = true;
                }

                //identifying other full-north-bricks
                else if (((voxelList.get(i).y % 2 == 1 && voxelList.get(i).x % 2 == 0)) ||
                        (voxelList.get(i).y % 2 == 0 && voxelList.get(i).x % 2 == 1)) {
                    voxelList.get(i).editdomain(34);
                } else
                    voxelList.get(i).editdomain(12);
            }
            //Identifying west-bricks
            else if (voxelList.get(i).orientation == 'w') {
                //Identifying half-west-bricks
                // 1. when width is even, half-bricks are at coordinates x=0 and y%2=1 or at y%2=1 and x=width-1
                // 2. when width is odd, half-bricks are at coordinates x=0 and y%2=1 or at y%2=0 and x=width-1
                if (voxelList.get(i).x == 0 && voxelList.get(i).y % 2 == 1) {
                    voxelList.get(i).editdomain(12);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.width % 2 == 0 && voxelList.get(i).x == MainFrame.width - 1 && voxelList.get(i).y % 2 == 1) {
                    voxelList.get(i).editdomain(34);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.width % 2 == 1 && voxelList.get(i).x == MainFrame.width - 1 && voxelList.get(i).y % 2 == 0) {
                    voxelList.get(i).editdomain(34);
                    voxelList.get(i).isHalfBrick = true;
                }

                //Identifying other full-west-bricks
                else if (((voxelList.get(i).y % 2 == 1 && voxelList.get(i).x % 2 == 0) &&
                        (voxelList.get(i).x != 0)) ||
                        (voxelList.get(i).y % 2 == 0 && voxelList.get(i).x % 2 == 1)) {
                    voxelList.get(i).editdomain(12);
                } else
                    voxelList.get(i).editdomain(34);
            }
            //Identifying south-bricks
            else if (voxelList.get(i).orientation == 's') {
                //Identifying half-south-bricks
                // 1. when height is even , half-bricks are at coordinates x%2=1 and y=0 or at y=height-1 and x%2=1
                // 2. when height is odd , half-bricks are at coordinates x%2=1 and y=0 or at y=height-1 and x%2=0
                if (voxelList.get(i).y == 0 && voxelList.get(i).x % 2 == 1) {
                    voxelList.get(i).editdomain(34);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.height % 2 == 0 && voxelList.get(i).y == MainFrame.height - 1 && voxelList.get(i).x % 2 == 1) {
                    voxelList.get(i).editdomain(12);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.height % 2 == 1 && voxelList.get(i).y == MainFrame.height - 1 && voxelList.get(i).x % 2 == 0) {
                    voxelList.get(i).editdomain(12);
                    voxelList.get(i).isHalfBrick = true;
                }

                //Identifying other full-south-bricks
                else if ((voxelList.get(i).y % 2 == 1 && voxelList.get(i).x % 2 == 1) ||
                        (voxelList.get(i).y % 2 == 0 && voxelList.get(i).x % 2 == 0)) {
                    voxelList.get(i).editdomain(12);
                } else
                    voxelList.get(i).editdomain(34);
            }
            //Identifying east-bricks
            else if (voxelList.get(i).orientation == 'e') {
                //Identifying half-east-bricks
                // 1. when width is even ,the half-bricks are at coordinates x=0 and y%2=0 or at y%2=0 and x=width-1
                // 2. when width is odd ,the half-bricks are at coordinates x=0 and y%2=0 or at y%2=1 and x=width-1
                if (voxelList.get(i).x == 0 && voxelList.get(i).y % 2 == 0) {
                    voxelList.get(i).editdomain(34);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.width % 2 == 0 && voxelList.get(i).x == MainFrame.width - 1 && voxelList.get(i).y % 2 == 0) {
                    voxelList.get(i).editdomain(12);
                    voxelList.get(i).isHalfBrick = true;
                } else if (MainFrame.width % 2 == 1 && voxelList.get(i).x == MainFrame.width - 1 && voxelList.get(i).y % 2 == 1) {
                    voxelList.get(i).editdomain(12);
                    voxelList.get(i).isHalfBrick = true;
                }

                //Identifying other full-east-bricks
                else if (((voxelList.get(i).y % 2 == 0 && voxelList.get(i).x % 2 == 1)) ||
                        (voxelList.get(i).y % 2 == 1 && voxelList.get(i).x % 2 == 0)) {
                    voxelList.get(i).editdomain(12);
                } else
                    voxelList.get(i).editdomain(34);
            }
        }
    }

    //STEP 8: Assign sequences to the domains in the coordinate-list after checking the GC content.
    public static void assignSequences(){
        //NOTE: all domains are assigned sequences whether they have been deleted or not.
        for(int i=0;i<voxelList.size();i++){

            //System.out.println("assigning for "+voxelList.get(i).x+""+voxelList.get(i).y+
            //	""+voxelList.get(i).z);

            //Assign complete random sequences for the first plane of bricks (z=1) since protector bricks start at z = 0
            if(voxelList.get(i).z==1){
                if(voxelList.get(i).domain==12){
                    do {
                        voxelList.get(i).Domain1 = AssignRandom(1);
                        voxelList.get(i).Domain2 = AssignRandom(2);
                        if(!checkGCContent(voxelList.get(i).Domain1 ,voxelList.get(i).Domain2)){
                            sequencesList.get(i1).isUsed=false;
                            sequencesList.get(i2).isUsed=false;
                        }
                    }while(!checkGCContent(voxelList.get(i).Domain1 ,voxelList.get(i).Domain2));
                    //System.out.println("assigned 1");
                    voxelList.get(i).Domain4=null;
                    voxelList.get(i).Domain3=null;
                    //System.out.println("assigned seq "+voxelList.get(i).x+""+voxelList.get(i).y+
                    //""+voxelList.get(i).z+"for coordinates"+voxelList.get(i).domain);
                }
                else if(voxelList.get(i).domain==34){
                    do {
                        voxelList.get(i).Domain3 = AssignRandom(3);
                        voxelList.get(i).Domain4 = AssignRandom(4);
                        if(!checkGCContent(voxelList.get(i).Domain3 ,voxelList.get(i).Domain4)){
                            sequencesList.get(i3).isUsed=false;
                            sequencesList.get(i4).isUsed=false;
                        }
                    }while (!checkGCContent(voxelList.get(i).Domain3 ,voxelList.get(i).Domain4));
                    //System.out.println("assigned 2");
                    voxelList.get(i).Domain1=null;
                    voxelList.get(i).Domain2=null;

                    //System.out.println("assigned seq "+voxelList.get(i).x+""+voxelList.get(i).y+
                    //		""+voxelList.get(i).z+"for coordinates"+voxelList.get(i).domain);
                }
            }

            //Assigning sequences for the rest of the coordinates
            else if(voxelList.get(i).z>1){
                //System.out.println("assigning for "+voxelList.get(i).x+""+voxelList.get(i).y+
                //		""+voxelList.get(i).z+"for domain "+voxelList.get(i).domain);
                int x,y,z,ival;
                x=voxelList.get(i).x;
                y=voxelList.get(i).y;
                z=voxelList.get(i).z;
                ival=ivalue[x][y][z-1];

                //System.out.println("front sequence for"+voxelList.get(ival).x+""+voxelList.get(ival).y+
                //		""+voxelList.get(ival).z);

                //System.out.print(" is"+voxelList.get(i).Domain1);
                if(voxelList.get(i).domain==34){
                    if(voxelList.get(ival).Domain1!=null)
                        voxelList.get(i).Domain3 = negateSeqRev(voxelList.get(ival).Domain1);
                    else
                        voxelList.get(i).Domain3 = AssignRandom(3);
                    do {
                        voxelList.get(i).Domain4 = AssignRandom(4);
                        if(!checkGCContent(voxelList.get(i).Domain3 ,voxelList.get(i).Domain4)){
                            sequencesList.get(i4).isUsed=false;
                            if(voxelList.get(ival).Domain1==null)
                                sequencesList.get(i3).isUsed=false;
                        }
                    }while(!checkGCContent(voxelList.get(i).Domain3 ,voxelList.get(i).Domain4));
                    //System.out.println("assigned 3");
                }
                else if(voxelList.get(i).domain==12){

                    if(voxelList.get(ival).Domain1!=null)
                        voxelList.get(i).Domain2 = negateSeqRev(voxelList.get(ival).Domain4);
                    else
                        voxelList.get(i).Domain2 = AssignRandom(2);
                    do {
                        voxelList.get(i).Domain1 = AssignRandom(1);
                        if(!checkGCContent(voxelList.get(i).Domain1 ,voxelList.get(i).Domain2)){
                            sequencesList.get(i1).isUsed=false;
                            if(voxelList.get(ival).Domain4==null)
                                sequencesList.get(i2).isUsed=false;
                        }
                    }while(!checkGCContent(voxelList.get(i).Domain1 ,voxelList.get(i).Domain2));
                    // System.out.println("assigned 4");
                }
            }
        }
    }

    //STEP 11: Replace the remaining complementary strands of the deleted voxels with (8-length) poly-T sequences.
    public void convertToT() {

        String Tsequence = "TTTTTTTT";
        for (int i = 0; i < voxelList.size(); i++) {
            x = voxelList.get(i).x;
            y = voxelList.get(i).y;
            z = voxelList.get(i).z;

            //assign poly-1T domain to tail protector bricks
            if (MainFrame.isMaxZProtectorPlaneEnabled && MainFrame.isZCrystalEnabled == false) {
                if (voxelList.get(i).isTailBrick) {
                    int x = voxelList.get(i).x;
                    int y = voxelList.get(i).y;
                    int z = voxelList.get(i).z;
                    if (voxelList.get(i).domain == 12) {
                        voxelList.get(i).Domain1 = voxelList.get(i).Domain1.replace(voxelList.get(i).Domain1, Tsequence);
                    } else if (voxelList.get(i).domain == 34) {
                        voxelList.get(i).Domain4 = voxelList.get(i).Domain4.replace(voxelList.get(i).Domain4, Tsequence);
                    }
                    //System.out.println("found tail protector sequence at"+voxelList.get(i).x+","+voxelList.get(i).y+","+voxelList.get(i).z+
                    //        "with sequence"+voxelList.get(i).Domain1+" "+voxelList.get(i).Domain2+" "+voxelList.get(i).Domain3+" "+voxelList.get(i).Domain4);
                }
            }

            //assign poly-1T domain to head protector bricks
            if (MainFrame.isMinZProtectorPlaneEnabled) {   // if z crystal is enabled, first convert arr head bricks to poly-1t sequences
                if (voxelList.get(i).isHeadBrick) {
                    int x = voxelList.get(i).x;
                    int y = voxelList.get(i).y;
                    int z = voxelList.get(i).z;

                    if (voxelList.get(i).domain == 12) {
                        voxelList.get(i).Domain2 = Tsequence;
                        if (ivalue[x][y][z + 1] != -1)
                            voxelList.get(i).Domain1 = negateSeqRev(voxelList.get(ivalue[x][y][z + 1]).Domain3);
                        else
                            voxelList.get(i).Domain1 = Tsequence;
                    } else if (voxelList.get(i).domain == 34) {
                        voxelList.get(i).Domain3 = Tsequence;
                        if (ivalue[x][y][z + 1] != -1)
                            voxelList.get(i).Domain4 = negateSeqRev(voxelList.get(ivalue[x][y][z + 1]).Domain2);
                        else
                            voxelList.get(i).Domain4 = Tsequence;
                    }
                }
            }

            //if a voxel is adjacent to cavity, do not convert it to poly-1T strands
            if (voxelList.get(i).isRemoved) {
                if (z > 0 && MainFrame.isMinZProtectorPlaneEnabled || z > 1 && !MainFrame.isMinZProtectorPlaneEnabled) {
                    indexback = ivalue[x][y][z - 1];
                    if (indexback != -1 && voxelList.get(indexback).isRemoved == false && voxelList.get(indexback).isHeadBrick == false && voxelList.get(indexback).isTailBrick == false) {
                        if (MainFrame.isCavityEnabled) {
                            voxelList.get(indexback).isAdjacentToCavity = true;
                        } else {
                            if(voxelList.get(i).domain==34)
                                voxelList.get(indexback).Domain1=voxelList.get(indexback).Domain1.replace(voxelList.get(indexback).Domain1, Tsequence);

                            else
                                voxelList.get(indexback).Domain4=voxelList.get(indexback).Domain4.replace(voxelList.get(indexback).Domain4, Tsequence);
                        }
                    }
                }
                if ((z > 1 && !MainFrame.isMinZProtectorPlaneEnabled || z > 0 && MainFrame.isMinZProtectorPlaneEnabled) && z < MainFrame.depth) {
                    indexfront = ivalue[x][y][z + 1];

                    if (indexfront != -1 && voxelList.get(indexfront).isRemoved == false && voxelList.get(indexfront).isHeadBrick == false && voxelList.get(indexfront).isTailBrick == false) {
                        if (MainFrame.isCavityEnabled) {
                            voxelList.get(indexfront).isAdjacentToCavity = true;
                        } else {
                            if(voxelList.get(i).domain==34)
                                voxelList.get(indexfront).Domain2=voxelList.get(indexfront).Domain2.replace
                                        (voxelList.get(indexfront).Domain2, Tsequence);
                            else
                                voxelList.get(indexfront).Domain3=voxelList.get(indexfront).Domain3.replace
                                        (voxelList.get(indexfront).Domain3, Tsequence);
                        }
                    }
                }
                //identify the voxels on the top and bottom of the cavity and mark it as adjacentToCavity
                if (y > 0 && ivalue[x][y - 1][z] != -1 ) {
                    voxelList.get(ivalue[x][y - 1][z]).isAdjacentToCavity = true;
                }
                if (y < MainFrame.height - 1 && ivalue[x][y + 1][z] != -1 ) {
                    voxelList.get(ivalue[x][y + 1][z]).isAdjacentToCavity = true;
                }
                if (x > 0 && ivalue[x - 1][y][z] != -1 ) {
                    voxelList.get(ivalue[x - 1][y][z]).isAdjacentToCavity = true;
                }
                if (x < MainFrame.width - 1 && ivalue[x + 1][y][z] != -1 ) {
                    voxelList.get(ivalue[x + 1][y][z]).isAdjacentToCavity = true;
                }

            }
        }
    }



    //STEP 12: Identify and remove those voxels which have d3&d4 OR d1&d2 as T-sequences (i.e., when an entire voxel is composed of only Thymidine).
    public void removeTVoxels(){
        //System.out.println("Size of the arraylist: "+voxelList.size());
        for(int i=0;i<voxelList.size();i++){

            x=voxelList.get(i).x;
            y=voxelList.get(i).y;
            z=voxelList.get(i).z;
            //System.out.println("trying to access "+x+""+y+""+z);
            if(voxelList.get(i).isRemoved == false){
                if(voxelList.get(i).domain == 34){
                    if(voxelList.get(i).Domain3.equals(poly1T)&&
                            voxelList.get(i).Domain4.equals(poly1T)){
                        voxelList.get(i).isRemoved=true;
                    }
                }
                else if(voxelList.get(i).domain == 12){
                    if(voxelList.get(i).Domain1.equals(poly1T)&&
                            voxelList.get(i).Domain2.equals(poly1T)){
                        voxelList.get(i).isRemoved=true;
                    }
                }
            }
        }
    }

    //STEP 13: Combine voxels to form bricks of size=2 voxels, set them as full bricks and save them in the brickList list.
    public void setBrickList(){
        System.out.println("SetbrickList called");
        int x,y,z;
        int brickCounter = 0;
        for(int i=0;i<voxelList.size();i++){

            if(voxelList.get(i).isRemoved == false){

                x = voxelList.get(i).x;
                y = voxelList.get(i).y;
                z = voxelList.get(i).z;
                //Adding half-bricks with domains 3 and 4 to the final-coordinate-list.
                if(voxelList.get(i).isHalfBrick){
                    if(voxelList.get(i).domain == 34) {
                        brickList.add(new VoxelToBrick(-1, -1, -1, x, y, z, null, null,
                                voxelList.get(i).Domain3, voxelList.get(i).Domain4));
                    }
                    else {
                        brickList.add(new VoxelToBrick(x, y, z, -1, -1, -1, voxelList.get(i).Domain1,
                                voxelList.get(i).Domain2, null, null));
                    }
                    brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                    brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                    brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                    brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                    brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                    brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                    brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity; //this check is added only to the half bricks
                }
                else{
                    //Identifying full and half north-bricks and adding them to the final-coordinate-list.
                    if(voxelList.get(i).orientation == 'n'){
                        if(voxelList.get(i).domain == 34){
                            if(voxelList.get(ivalue[x][y+1][z]).isRemoved == false){

                                brickList.add(new VoxelToBrick(x,y+1,z,x,y,z,
                                        voxelList.get(ivalue[x][y+1][z]).Domain1,
                                        voxelList.get(ivalue[x][y+1][z]).Domain2,
                                        voxelList.get(i).Domain3, voxelList.get(i).Domain4));

                                voxelList.get(ivalue[x][y+1][z]).isRemoved = true;
                                newIndex[x][y+1][z] = brickCounter;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x][y+1][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x][y+1][z]).isTailBrick || voxelList.get(i).isTailBrick;

                            }
                            else{
                                brickList.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        voxelList.get(i).Domain3, voxelList.get(i).Domain4));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                        }
                        else{
                            if(voxelList.get(ivalue[x][y-1][z]).isRemoved == false){

                                brickList.add(new VoxelToBrick(x,y,z,x,y-1,z,
                                        voxelList.get(i).Domain1,
                                        voxelList.get(i).Domain2,
                                        voxelList.get(ivalue[x][y-1][z]).Domain3,
                                        voxelList.get(ivalue[x][y-1][z]).Domain4));

                                voxelList.get(ivalue[x][y-1][z]).isRemoved = true;
                                newIndex[x][y-1][z] = brickCounter;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x][y-1][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x][y-1][z]).isTailBrick || voxelList.get(i).isTailBrick;

                            }
                            else{
                                brickList.add(new VoxelToBrick(x,y,z,-1,-1,-1,voxelList.get(i).Domain1,
                                        voxelList.get(i).Domain2, null, null));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                        }
                    }

                    //Identifying full and half west-bricks and adding them to the final-coordinate-list.
                    if(voxelList.get(i).orientation=='w'){
                        if(voxelList.get(i).domain==34){
                            if(voxelList.get(ivalue[x+1][y][z]).isRemoved == false){
                                brickList.add(new VoxelToBrick(x+1,y,z,x,y,z,
                                        voxelList.get(ivalue[x+1][y][z]).Domain1,
                                        voxelList.get(ivalue[x+1][y][z]).Domain2,
                                        voxelList.get(i).Domain3, voxelList.get(i).Domain4));

                                voxelList.get(ivalue[x+1][y][z]).isRemoved = true;
                                newIndex[x+1][y][z] = brickCounter;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x+1][y][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x+1][y][z]).isTailBrick || voxelList.get(i).isTailBrick;
                            }
                            else{
                                brickList.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        voxelList.get(i).Domain3, voxelList.get(i).Domain4));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                        }
                        else{
                            if(voxelList.get(ivalue[x-1][y][z]).isRemoved == false){
                                brickList.add(new VoxelToBrick(x,y,z,x-1,y,z,
                                        voxelList.get(i).Domain1,
                                        voxelList.get(i).Domain2,
                                        voxelList.get(ivalue[x-1][y][z]).Domain3,
                                        voxelList.get(ivalue[x-1][y][z]).Domain4));

                                voxelList.get(ivalue[x-1][y][z]).isRemoved = true;
                                newIndex[x-1][y][z] = brickCounter;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x-1][y][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x-1][y][z]).isTailBrick || voxelList.get(i).isTailBrick;
                            }
                            else{
                                brickList.add(new VoxelToBrick(x,y,z,-1,-1,-1,voxelList.get(i).Domain1,
                                        voxelList.get(i).Domain2, null, null));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                        }
                    }

                    //Identifying full and half south-bricks and adding them to the final-coordinate-list.
                    if(voxelList.get(i).orientation=='s'){
                        if(voxelList.get(i).domain==12){
                            if(voxelList.get(ivalue[x][y+1][z]).isRemoved == false){
                                brickList.add(new VoxelToBrick(x,y,z,x,y+1,z,
                                        voxelList.get(i).Domain1, voxelList.get(i).Domain2,
                                        voxelList.get(ivalue[x][y+1][z]).Domain3,
                                        voxelList.get(ivalue[x][y+1][z]).Domain4
                                ));

                                voxelList.get(ivalue[x][y+1][z]).isRemoved = true;
                                newIndex[x][y+1][z] = brickCounter;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x][y+1][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x][y+1][z]).isTailBrick || voxelList.get(i).isTailBrick;
                            }
                            else{
                                brickList.add(new VoxelToBrick(x,y,z,-1,-1,-1,null, null,
                                        voxelList.get(i).Domain1, voxelList.get(i).Domain2));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                        }
                        else{
                            if(voxelList.get(ivalue[x][y-1][z]).isRemoved == false){
                                brickList.add(new VoxelToBrick(x,y-1,z,x,y,z,
                                        voxelList.get(ivalue[x][y-1][z]).Domain1,
                                        voxelList.get(ivalue[x][y-1][z]).Domain2,
                                        voxelList.get(i).Domain3,
                                        voxelList.get(i).Domain4
                                ));

                                voxelList.get(ivalue[x][y-1][z]).isRemoved = true;
                                newIndex[x][y-1][z] = brickCounter;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x][y-1][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x][y-1][z]).isTailBrick || voxelList.get(i).isTailBrick;
                            }
                            else{
                                brickList.add(new VoxelToBrick(-1,-1,-1,x,y,z, null, null,voxelList.get(i).Domain3,
                                        voxelList.get(i).Domain4));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                        }
                    }
                    //Identifying full and half east-bricks and adding them to the final-coordinate-list.
                    if(voxelList.get(i).orientation=='e'){
                        if(voxelList.get(i).domain==12){
                            if(voxelList.get(ivalue[x+1][y][z]).isRemoved == false){
                                brickList.add(new VoxelToBrick(x,y,z,x+1,y,z,
                                        voxelList.get(i).Domain1, voxelList.get(i).Domain2,
                                        voxelList.get(ivalue[x+1][y][z]).Domain3,
                                        voxelList.get(ivalue[x+1][y][z]).Domain4
                                ));

                                voxelList.get(ivalue[x+1][y][z]).isRemoved = true;
                                newIndex[x+1][y][z] = brickCounter;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x+1][y][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x+1][y][z]).isTailBrick || voxelList.get(i).isTailBrick;
                            }
                            else{
                                brickList.add(new VoxelToBrick(x,y,z,-1,-1,-1,null, null,
                                        voxelList.get(i).Domain1, voxelList.get(i).Domain2));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                        }
                        else{
                            if(voxelList.get(ivalue[x-1][y][z]).isRemoved == false){
                                brickList.add(new VoxelToBrick(x-1,y,z,x,y,z,
                                        voxelList.get(ivalue[x-1][y][z]).Domain1,
                                        voxelList.get(ivalue[x-1][y][z]).Domain2,
                                        voxelList.get(i).Domain3,
                                        voxelList.get(i).Domain4
                                ));

                                voxelList.get(ivalue[x-1][y][z]).isRemoved = true;
                                newIndex[x-1][y][z] = brickCounter;
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(ivalue[x-1][y][z]).isHeadBrick || voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(ivalue[x-1][y][z]).isTailBrick || voxelList.get(i).isTailBrick;
                            }
                            else{
                                brickList.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,voxelList.get(i).Domain3,
                                        voxelList.get(i).Domain4));
                                brickList.get(brickCounter).isHeadProtector = voxelList.get(i).isHeadBrick;
                                brickList.get(brickCounter).isTailProtector = voxelList.get(i).isTailBrick;
                                brickList.get(brickCounter).isAdjacentToCavity = voxelList.get(i).isAdjacentToCavity;
                                brickList.get(brickCounter).isBottommostHalfBrick = voxelList.get(i).isBottommostBrick;
                                brickList.get(brickCounter).isTopmostHalfBrick = voxelList.get(i).isTopmostBrick;
                                brickList.get(brickCounter).isLeftmostHalfBrick = voxelList.get(i).isLeftmostBrick;
                                brickList.get(brickCounter).isRightmostHalfBrick = voxelList.get(i).isRightmostBrick;
                            }
                        }
                    }
                }
                //MainFrame.printLog("Added in set brick list: " + x + " " + y + " " + z + " head : " + brickList.get(brickCounter).isHeadProtector + " tail : " + brickList.get(brickCounter).isTailProtector, Color.WHITE);
                //Remove the individual voxel after combining it as a full brick in the final-coordinate-list.
                voxelList.get(i).isRemoved = true;
                newIndex[x][y][z] = brickCounter;
                brickCounter++;
            }

        }
    }

    //STEP 14: Assign helix number to each voxel
    public void addHelixNumber(){
        int x2, x3, y2, y3;

        for (int i = 0; i<brickList.size(); i++) {
            x2= brickList.get(i).x2;
            x3= brickList.get(i).x3;
            y2= brickList.get(i).y2;
            y3= brickList.get(i).y3;
            if (x2!=-1) {
                if(y2==0) {
                    brickList.get(i).helix1 = x2;
                    brickList.get(i).helix2 = x2;
                }
                else if (y2%2==1) {
                    brickList.get(i).helix1 = (y2 + 1) * MainFrame.width - (x2 + 1);
                    brickList.get(i).helix2 = brickList.get(i).helix1;

                }
                else {
                    brickList.get(i).helix1 = MainFrame.width * y2 + x2;
                    brickList.get(i).helix2 = brickList.get(i).helix1;
                }
            }
            if (x3!=-1){
                if(y3==0) {
                    brickList.get(i).helix3 = x3;
                    brickList.get(i).helix4 = brickList.get(i).helix3;
                }
                else if (y3%2==1) {
                    brickList.get(i).helix3 = (y3 + 1) * MainFrame.width - (x3 + 1);
                    brickList.get(i).helix4 = brickList.get(i).helix3;
                }
                else {
                    brickList.get(i).helix3 = MainFrame.width * y3 + x3;
                    brickList.get(i).helix4 = brickList.get(i).helix3;
                }
            }
        }
    }

    //STEP 15: If set boundary bricks option is enabled, combine bricks to form 48-nt boundary bricks
    public void attachBoundaryBricks(){

        int x,y,z;
        int fullBrickIndex;
        String d1,d2;
        int  h5, h6;
        int domains;

        boundaryBrickList = brickList;

        for(int i = 0; i < boundaryBrickList.size(); i++){
            if(boundaryBrickList.get(i).isLeftmostHalfBrick || boundaryBrickList.get(i).isBottommostHalfBrick || boundaryBrickList.get(i).isTopmostHalfBrick ||
                    boundaryBrickList.get(i).isRightmostHalfBrick) {
                if (boundaryBrickList.get(i).x2 != -1) {
                    x = boundaryBrickList.get(i).x2;
                    y = boundaryBrickList.get(i).y2;
                    z = boundaryBrickList.get(i).z2;
                    System.out.println(x + " " + y + " " + z + "  " + boundaryBrickList.get(i).Domain1);
                    d1 = new String(boundaryBrickList.get(i).Domain1);
                    d2 = new String(boundaryBrickList.get(i).Domain2);
                    h5 = boundaryBrickList.get(i).helix2;
                    h6 = boundaryBrickList.get(i).helix1;
                } else {
                    x = boundaryBrickList.get(i).x3;
                    y = boundaryBrickList.get(i).y3;
                    z = boundaryBrickList.get(i).z3;
                    d1 = new String(boundaryBrickList.get(i).Domain3);
                    d2 = new String(boundaryBrickList.get(i).Domain4);
                    h5 = boundaryBrickList.get(i).helix3;
                    h6 = boundaryBrickList.get(i).helix4;
                }
                if (MainFrame.isMinXBoundaryPlaneEnabled && boundaryBrickList.get(i).isLeftmostHalfBrick && z > 1 && boundaryBrickList.get(i).isRemoved == false) {
                    fullBrickIndex = newIndex[x][y][z - 2];
                    if (fullBrickIndex != -1) {

                        if (boundaryBrickList.get(fullBrickIndex).x1 == x && boundaryBrickList.get(fullBrickIndex).y1 == y)
                            boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 12);

                        else
                            boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 34);

                        boundaryBrickList.get(fullBrickIndex).isBoundary = true;
                        boundaryBrickList.get(i).isRemoved = true;
                        newIndex[x][y][z] = newIndex[x][y][z - 2];
                        newIndex[x][y][z + 1] = newIndex[x][y][z - 2];
                        boundaryBrickList.get(i).isLeftmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for min x at" + x + " " + y + " " + z);
                    }
                }
                if (MainFrame.isMaxXBoundaryPlaneEnabled && boundaryBrickList.get(i).isRightmostHalfBrick && z > 1 && boundaryBrickList.get(i).isRemoved == false) {
                    fullBrickIndex = newIndex[x][y][z - 2];
                    if (fullBrickIndex != -1) {
                        if (boundaryBrickList.get(fullBrickIndex).x1 == x && boundaryBrickList.get(fullBrickIndex).y1 == y)
                            boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 12);

                        else
                            boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 34);
                        boundaryBrickList.get(fullBrickIndex).isBoundary = true;
                        boundaryBrickList.get(i).isRemoved = true;
                        newIndex[x][y][z] = newIndex[x][y][z - 2];
                        newIndex[x][y][z + 1] = newIndex[x][y][z - 2];
                        boundaryBrickList.get(i).isLeftmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for max x at" + x + " " + y + " " + z);
                    }
                }
                if (MainFrame.isMinYBoundaryPlaneEnabled && boundaryBrickList.get(i).isTopmostHalfBrick && z > 1 && boundaryBrickList.get(i).isRemoved == false) {
                    fullBrickIndex = newIndex[x][y][z - 2];
                    if (fullBrickIndex != -1) {
                        if (boundaryBrickList.get(fullBrickIndex).x1 == x && boundaryBrickList.get(fullBrickIndex).y1 == y)
                            boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 12);

                        else
                            boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 34);
                        boundaryBrickList.get(fullBrickIndex).isBoundary = true;
                        boundaryBrickList.get(i).isRemoved = true;
                        newIndex[x][y][z] = newIndex[x][y][z - 2];
                        newIndex[x][y][z + 1] = newIndex[x][y][z - 2];
                        boundaryBrickList.get(i).isLeftmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for min y at" + x + " " + y + " " + z);
                    }
                }
                if (MainFrame.isMaxYBoundaryPlaneEnabled && boundaryBrickList.get(i).isBottommostHalfBrick && z > 1 && boundaryBrickList.get(i).isRemoved == false) {
                    fullBrickIndex = newIndex[x][y][z - 2];
                    if (fullBrickIndex != -1) {
                        if (boundaryBrickList.get(fullBrickIndex).x1 == x && boundaryBrickList.get(fullBrickIndex).y1 == y)
                            boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 12);

                        else
                            boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 34);
                        boundaryBrickList.get(fullBrickIndex).isBoundary = true;
                        newIndex[x][y][z] = newIndex[x][y][z - 2];
                        newIndex[x][y][z + 1] = newIndex[x][y][z - 2];
                        boundaryBrickList.get(i).isRemoved = true;
                        boundaryBrickList.get(i).isLeftmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for max y at" + x + " " + y + " " + z);
                    }
                }
            }
        }

        if (MainFrame.isCavityEnabled) {
            for (int i = 0; i < boundaryBrickList.size(); i++) {
                if (boundaryBrickList.get(i).isAdjacentToCavity) {

                    if (boundaryBrickList.get(i).x3 == -1) {
                        x = boundaryBrickList.get(i).x2;
                        y = boundaryBrickList.get(i).y2;
                        z = boundaryBrickList.get(i).z2;
                        d1 = new String(boundaryBrickList.get(i).Domain1);
                        d2 = new String(boundaryBrickList.get(i).Domain2);
                        h5 = boundaryBrickList.get(i).helix1;
                        h6 = h5;
                        domains = 12;
                    } else {
                        x = boundaryBrickList.get(i).x3;
                        y = boundaryBrickList.get(i).y3;
                        z = boundaryBrickList.get(i).z3;
                        d1 = new String(boundaryBrickList.get(i).Domain3);
                        d2 = new String(boundaryBrickList.get(i).Domain4);
                        h5 = boundaryBrickList.get(i).helix3;
                        h6 = h5;
                        domains = 34;

                    }

                    if (z > 1) {

                        fullBrickIndex = newIndex[x][y][z - 2];
                        if (fullBrickIndex != -1 && boundaryBrickList.get(fullBrickIndex).isBoundary == false) {

                            if (boundaryBrickList.get(fullBrickIndex).x1 == x && boundaryBrickList.get(fullBrickIndex).y1 == y)
                                boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 12);

                            else
                                boundaryBrickList.get(fullBrickIndex).addDomains(x, y, z, d1, d2, h5, h6, 34);

                            boundaryBrickList.get(fullBrickIndex).isBoundary = true;
                            newIndex[x][y][z] = newIndex[x][y][z - 2];
                            newIndex[x][y][z + 1] = newIndex[x][y][z - 2];
                            boundaryBrickList.get(i).isRemoved = true;
                            boundaryBrickList.get(i).isAdjacentToCavity = false;

                        }
                    }
                }
            }
        }
        int s = boundaryBrickList.size();
        for(int i=0;i<s;i++){
            if(boundaryBrickList.get(i).isRemoved == true){
                boundaryBrickList.remove(i);
                s--;
            }
        }
    }

    public void createCrystal(){
        int x, y, z, tailZ, headZ;
        int domain2Index = -1, domain3Index = -1;
        boolean foundDomains24 =  false;
        boolean foundDomains13 =  false;
        runRandomSequences();

        if (MainFrame.isZCrystalEnabled) {
            for (int i = 0; i < brickList.size(); i++) {
                if (brickList.get(i).isTailProtector) {
                    if (brickList.get(i).Domain2 != null) {
                        x = brickList.get(i).x2;
                        y = brickList.get(i).y2;
                        tailZ = brickList.get(i).z2; //to see compatibility of bricks for crystal
                        headZ = 0;
                        while ((newIndex[x][y][headZ] == -1 || brickList.get(newIndex[x][y][headZ]).isRemoved) && headZ < brickList.get(i).z2) {
                            headZ++;
                        }

                        if (brickList.get(newIndex[x][y][headZ]).isHeadProtector &&
                                ((headZ % 4 == 0 && tailZ % 4 == 3) || (headZ % 4 == 1 && tailZ % 4 == 0)
                                        || (headZ % 4 == 2 && tailZ % 4 == 1) || (headZ % 4 == 3 && tailZ % 4 == 2))) {

                            foundDomains13 = true;
                            domain3Index = newIndex[x][y][headZ];
                        }
                    }
                    if (brickList.get(i).Domain3 != null) {
                        x = brickList.get(i).x3;
                        y = brickList.get(i).y3;
                        tailZ = brickList.get(i).z3; //to see compatibility of bricks for crystal
                        headZ = 0;
                        while ((newIndex[x][y][headZ] == -1 || brickList.get(newIndex[x][y][headZ]).isRemoved) && headZ < brickList.get(i).z3) {
                            headZ++;
                        }

                        if (brickList.get(newIndex[x][y][headZ]).isHeadProtector &&
                                ((headZ % 4 == 0 && tailZ % 4 == 3) || (headZ % 4 == 1 && tailZ % 4 == 0)
                                        || (headZ % 4 == 2 && tailZ % 4 == 1) || (headZ % 4 == 3 && tailZ % 4 == 2))) {

                            foundDomains24 = true;
                            domain2Index = newIndex[x][y][headZ];
                        }
                    }
                    if (foundDomains13) {
                        foundDomains13 = false;
                        if (brickList.get(i).Domain1.equals(poly1T)) {
                            do {
                                brickList.get(i).Domain1 = AssignRandom(1);
                                if (!checkGCContent(brickList.get(i).Domain1, brickList.get(i).Domain3)) {
                                    sequencesList.get(i1).isUsed = false;
                                }
                            }
                            while (!checkGCContent(brickList.get(i).Domain1, brickList.get(i).Domain3));
                        }
                        brickList.get(domain3Index).Domain3 = negateSeqRev(brickList.get(i).Domain1);
                    }
                    if (foundDomains24) {
                        foundDomains24 = false;
                        if (brickList.get(i).Domain4.equals(poly1T)) {
                            do {
                                brickList.get(i).Domain4 = AssignRandom(4);
                                if (!checkGCContent(brickList.get(i).Domain2, brickList.get(i).Domain4)) {
                                    sequencesList.get(i4).isUsed = false;
                                }
                            }
                            while (!checkGCContent(brickList.get(i).Domain2, brickList.get(i).Domain4));
                        }
                        brickList.get(domain2Index).Domain2 = negateSeqRev(brickList.get(i).Domain4);
                    }
                }
            }
        }

        if (MainFrame.isXCrystalEnabled){
            for(int i = 0; i < brickList.size(); i++){
                //checking for west half brick
                if (brickList.get(i).isLeftmostHalfBrick && brickList.get(i).z2 % 4 == 1){
                    x = brickList.get(i).x2 + 1; //start searching for rightmost half brick from x + 1
                    y = brickList.get(i).y2;
                    z = brickList.get(i).z2;
                    while ( x < MainFrame.width ){
                        if (newIndex[x][y][z] != -1 && !brickList.get(newIndex[x][y][z]).isRemoved && brickList.get(newIndex[x][y][z]).isRightmostHalfBrick)
                            break;
                        else if ((newIndex[x][y][z] == -1 || brickList.get(newIndex[x][y][z]).isRemoved) || (newIndex[x][y][z] != -1 && brickList.get(newIndex[x][y][z]).isRightmostHalfBrick == false))
                            x++;
                    }
                    if (brickList.get(newIndex[x][y][z]).isRightmostHalfBrick) {
                        brickList.get(i).Domain3 = brickList.get(newIndex[x][y][z]).Domain3;
                        brickList.get(i).Domain4 = brickList.get(newIndex[x][y][z]).Domain4;
                        brickList.get(i).x3 = brickList.get(newIndex[x][y][z]).x3;
                        brickList.get(i).x4 = brickList.get(newIndex[x][y][z]).x4;
                        brickList.get(i).y3 = brickList.get(newIndex[x][y][z]).y3;
                        brickList.get(i).y4 = brickList.get(newIndex[x][y][z]).y4;
                        brickList.get(i).z3 = brickList.get(newIndex[x][y][z]).z3;
                        brickList.get(i).z4 = brickList.get(newIndex[x][y][z]).z4;
                        brickList.get(i).helix3 = brickList.get(newIndex[x][y][z]).helix3;
                        brickList.get(i).helix4 = brickList.get(newIndex[x][y][z]).helix4;

                        brickList.get(i).isLeftmostHalfBrick = false;
                        brickList.get(i).isTopmostHalfBrick = false;
                        brickList.get(i).isBottommostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isRightmostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isRemoved = true;
                        newIndex[x][y][z] = newIndex[brickList.get(i).x2][y][z];
                        newIndex[x][y][z + 1] = newIndex[brickList.get(i).x2][y][z];
                    }
                }

                //checking for east half brick
                if (brickList.get(i).isLeftmostHalfBrick && brickList.get(i).z3 % 4 == 3){
                    x = brickList.get(i).x3 + 1;
                    y = brickList.get(i).y3;
                    z = brickList.get(i).z3;
                    while ( x < MainFrame.width ){
                        if (newIndex[x][y][z] != -1 && !brickList.get(newIndex[x][y][z]).isRemoved && brickList.get(newIndex[x][y][z]).isRightmostHalfBrick)
                            break;
                        else if ((newIndex[x][y][z] == -1 || brickList.get(newIndex[x][y][z]).isRemoved) || (newIndex[x][y][z] != -1 && brickList.get(newIndex[x][y][z]).isRightmostHalfBrick == false))
                            x++;
                    }
                    if (brickList.get(newIndex[x][y][z]).isRightmostHalfBrick) {
                        brickList.get(i).Domain2 = brickList.get(newIndex[x][y][z]).Domain2;
                        brickList.get(i).Domain1 = brickList.get(newIndex[x][y][z]).Domain1;
                        brickList.get(i).x1 = brickList.get(newIndex[x][y][z]).x1;
                        brickList.get(i).x2 = brickList.get(newIndex[x][y][z]).x2;
                        brickList.get(i).y1 = brickList.get(newIndex[x][y][z]).y1;
                        brickList.get(i).y2 = brickList.get(newIndex[x][y][z]).y2;
                        brickList.get(i).z1 = brickList.get(newIndex[x][y][z]).z1;
                        brickList.get(i).z2 = brickList.get(newIndex[x][y][z]).z2;
                        brickList.get(i).helix1 = brickList.get(newIndex[x][y][z]).helix1;
                        brickList.get(i).helix2 = brickList.get(newIndex[x][y][z]).helix2;

                        brickList.get(i).isLeftmostHalfBrick = false;
                        brickList.get(i).isTopmostHalfBrick = false;
                        brickList.get(i).isBottommostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isRightmostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isRemoved = true;
                        newIndex[x][y][z] = newIndex[brickList.get(i).x3][y][z];
                        newIndex[x][y][z + 1] = newIndex[brickList.get(i).x3][y][z];

                    }
                }
            }
        }

        if (MainFrame.isYCrystalEnabled){
            for(int i = 0; i < brickList.size(); i++){
                //checking for north half brick
                if (brickList.get(i).isTopmostHalfBrick && brickList.get(i).z2 % 4 == 0){
                    x = brickList.get(i).x2;
                    y = brickList.get(i).y2 + 1; //start searching for bottommost half brick from y + 1
                    z = brickList.get(i).z2;
                    while ( y < MainFrame.height){
                        if (newIndex[x][y][z] != -1 && !brickList.get(newIndex[x][y][z]).isRemoved && brickList.get(newIndex[x][y][z]).isBottommostHalfBrick)
                            break;
                        else if ((newIndex[x][y][z] == -1 || brickList.get(newIndex[x][y][z]).isRemoved) || (newIndex[x][y][z] != -1 && brickList.get(newIndex[x][y][z]).isBottommostHalfBrick == false))
                            y++;
                    }
                    if (brickList.get(newIndex[x][y][z]).isBottommostHalfBrick) {
                        brickList.get(i).Domain3 = brickList.get(newIndex[x][y][z]).Domain3;
                        brickList.get(i).Domain4 = brickList.get(newIndex[x][y][z]).Domain4;
                        brickList.get(i).x3 = brickList.get(newIndex[x][y][z]).x3;
                        brickList.get(i).x4 = brickList.get(newIndex[x][y][z]).x4;
                        brickList.get(i).y3 = brickList.get(newIndex[x][y][z]).y3;
                        brickList.get(i).y4 = brickList.get(newIndex[x][y][z]).y4;
                        brickList.get(i).z3 = brickList.get(newIndex[x][y][z]).z3;
                        brickList.get(i).z4 = brickList.get(newIndex[x][y][z]).z4;
                        brickList.get(i).helix3 = brickList.get(newIndex[x][y][z]).helix3;
                        brickList.get(i).helix4 = brickList.get(newIndex[x][y][z]).helix4;

                        brickList.get(i).isLeftmostHalfBrick = false;
                        brickList.get(i).isTopmostHalfBrick = false;
                        brickList.get(i).isRightmostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isBottommostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isRemoved = true;
                        newIndex[x][y][z] = newIndex[x][brickList.get(i).y2][z];
                        newIndex[x][y][z + 1] = newIndex[x][brickList.get(i).y2][z];
                    }
                }

                //checking for south half brick
                if (brickList.get(i).isTopmostHalfBrick && brickList.get(i).z3 % 4 == 2){
                    x = brickList.get(i).x3;
                    y = brickList.get(i).y3 + 1;
                    z = brickList.get(i).z3;
                    while ( y < MainFrame.height){
                        if (newIndex[x][y][z] != -1 && !brickList.get(newIndex[x][y][z]).isRemoved && brickList.get(newIndex[x][y][z]).isBottommostHalfBrick)
                            break;
                        else if ((newIndex[x][y][z] == -1  || brickList.get(newIndex[x][y][z]).isRemoved) || (newIndex[x][y][z] != -1 && brickList.get(newIndex[x][y][z]).isBottommostHalfBrick == false))
                            y++;
                    }
                    if (brickList.get(newIndex[x][y][z]).isBottommostHalfBrick) {
                        brickList.get(i).Domain2 = brickList.get(newIndex[x][y][z]).Domain2;
                        brickList.get(i).Domain1 = brickList.get(newIndex[x][y][z]).Domain1;
                        brickList.get(i).x1 = brickList.get(newIndex[x][y][z]).x1;
                        brickList.get(i).x2 = brickList.get(newIndex[x][y][z]).x2;
                        brickList.get(i).y1 = brickList.get(newIndex[x][y][z]).y1;
                        brickList.get(i).y2 = brickList.get(newIndex[x][y][z]).y2;
                        brickList.get(i).z1 = brickList.get(newIndex[x][y][z]).z1;
                        brickList.get(i).z2 = brickList.get(newIndex[x][y][z]).z2;
                        brickList.get(i).helix1 = brickList.get(newIndex[x][y][z]).helix1;
                        brickList.get(i).helix2 = brickList.get(newIndex[x][y][z]).helix2;

                        brickList.get(i).isLeftmostHalfBrick = false;
                        brickList.get(i).isTopmostHalfBrick = false;
                        brickList.get(i).isRightmostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isBottommostHalfBrick = false;
                        brickList.get(newIndex[x][y][z]).isRemoved = true;
                        newIndex[x][y][z] = newIndex[x][brickList.get(i).y3][z];
                        newIndex[x][y][z + 1] = newIndex[x][brickList.get(i).y3][z];
                    }
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
            while(sequencesList.get(randomIndex).isUsed==true){
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

        for (int i = 0; i < voxelList.size(); i++) {
            if( (domain.equals(voxelList.get(i).Domain1) )||( domain.equals(voxelList.get(i).Domain2)) ||
                    (domain.equals(voxelList.get(i).Domain3)) || (domain.equals(voxelList.get(i).Domain4))) {
                isMatching = true;
                System.out.println(domain+" matches with"+voxelList.get(i).Domain1+" or"+voxelList.get(i).Domain2+" or"
                        +voxelList.get(i).Domain3+" or"+voxelList.get(i).Domain4);
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

}
