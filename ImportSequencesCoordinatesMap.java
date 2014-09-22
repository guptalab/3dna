
import sun.applet.Main;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class ImportSequencesCoordinatesMap {

    static ArrayList<HelixAndCoordinates> fullBrickSequencesList = new ArrayList<HelixAndCoordinates>();
    static ArrayList<HelixAndCoordinates> halfBrickSequencesList = new ArrayList<HelixAndCoordinates>();
    static ArrayList<HelixAndCoordinates> boundaryBrickSequencesList = new ArrayList<HelixAndCoordinates>();
    public static ArrayList<XYZCoordinates> coordinatesList = new ArrayList<XYZCoordinates>();
    static ArrayList<VoxelToBrick> finalData=new ArrayList<VoxelToBrick>();
    static ArrayList<VoxelToBrick> finalBoundaryData=new ArrayList<VoxelToBrick>();
    //using a 3d array to store the position for each voxel in the arraylist coordinate-list
    static int[][][] ivalue = new int[20][20][20];
    int[][][] newIndex = new int[MainFrame.width][MainFrame.height][MainFrame.depth+2];
    int x,y,z,indexfront, indexback;
    String Tsequence="TTTTTTTT";



    //constructor for the class
    public ImportSequencesCoordinatesMap() {
        //clear all arraylists
        coordinatesList.clear();
        for (int k = 0; k <= MainFrame.depth; k++)
            for (int j = 0; j < MainFrame.height; j++)
                for (int i = 0; i < MainFrame.width; i++)
                    ivalue[i][j][k] = -1;
        readFullBrickSequences();
        readHalfBrickSequences();
        readBoundaryBrickSequences();
        addCoordinates();
        identifyBoundaryVoxels();
        assignOrientation();
        assignDomain();
        assignNullSequences();

        finalData.clear();
        MainFrame.enableGraph();
        convertToT();
        //removeTVoxels();
        //set all values in newIndex to -1
        for(int i = 0; i<MainFrame.width; i++)
            for(int j = 0; j<MainFrame.height; j++)
                for(int k = 0; k<=MainFrame.depth; k++)
                    newIndex [i][j][k] = -1;
        setBrickList();
        addHelixNumber();
        mapSequencesFromList();
        if(MainFrame.isBoundaryCalled)
            attachBoundaryBricks();

    }

    //STEP 1: Save sequences, helix and coordinates from 16ntFile.csv to an arraylist - FullBrickSequencesList
    public static void readFullBrickSequences() {
        int fullBrickCounter = 0;
        fullBrickSequencesList.clear();
        String csvFile = MainFrame.importedFullBrickFilePath;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use semicolon
                String[] data = line.split(cvsSplitBy);
                HelixAndCoordinates h = new HelixAndCoordinates( Integer.parseInt(data[7]),
                        Integer.parseInt(data[8]), Integer.parseInt(data[9]), Integer.parseInt(data[10]), Integer.parseInt(data[11]),
                        Integer.parseInt(data[12]), Integer.parseInt(data[13]), Integer.parseInt(data[14]),-1, -1, -1, -1,
                        Integer.parseInt(data[15]),  Integer.parseInt(data[16]),  Integer.parseInt(data[17]),  Integer.parseInt(data[18]));
                fullBrickSequencesList.add(h);
                fullBrickSequencesList.get(fullBrickCounter).addData(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                        Integer.parseInt(data[2]),data[3].toString(), data[4].toString(), data[5].toString(), data[6].toString(), null, null);
                fullBrickCounter++;
                //System.out.println("full data added to fullBrickList");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //STEP 2: Save sequences, helix and coordinates from 16ntFile.csv to an arraylist - HalfBrickSequencesList
    public static void readHalfBrickSequences() {

        int halfBrickCounter = 0;
        halfBrickSequencesList.clear();
        String csvFile = MainFrame.importedHalfBrickFilePath;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use semicolon
                String[] data = line.split(cvsSplitBy);
                HelixAndCoordinates h = new HelixAndCoordinates( Integer.parseInt(data[5]),
                        Integer.parseInt(data[6]), Integer.parseInt(data[7]), Integer.parseInt(data[8]), -1, -1, -1, -1, -1, -1, -1, -1, 0, 0, 0, 0);
                halfBrickSequencesList.add(h);
                //System.out.println("new added to halfBrickList");
                halfBrickSequencesList.get(halfBrickCounter).addData(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                        Integer.parseInt(data[2]),data[3].toString(), data[4].toString(), null, null, null, null);
                halfBrickCounter++;
                //System.out.println("more data added to halfBrickList");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //STEP 3: Save sequences, helix and coordinates from 48ntFile.csv to an arraylist - boundaryBrickSequencesList
    public static void readBoundaryBrickSequences() {

        int boundaryBrickCounter = 0;
        boundaryBrickSequencesList.clear();
        String csvFile = MainFrame.importedBoundaryBrickFilePath;
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ";";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                // use semicolon
                String[] data = line.split(cvsSplitBy);
                HelixAndCoordinates h = new HelixAndCoordinates( Integer.parseInt(data[9]),
                        Integer.parseInt(data[10]), Integer.parseInt(data[11]), Integer.parseInt(data[12]), Integer.parseInt(data[13]),
                        Integer.parseInt(data[14]), Integer.parseInt(data[15]), Integer.parseInt(data[16]), Integer.parseInt(data[17]),
                        Integer.parseInt(data[18]), Integer.parseInt(data[19]), Integer.parseInt(data[20]), 0, 0, 0, 0);
                boundaryBrickSequencesList.add(h);
                //System.out.println("new added to halfBrickList");
                boundaryBrickSequencesList.get(boundaryBrickCounter).addData(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                        Integer.parseInt(data[2]),data[3].toString(), data[4].toString(), data[5].toString(), data[6].toString(), data[7].toString(), data[8].toString());
                boundaryBrickCounter++;
                //System.out.println("more data added to halfBrickList");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //STEP 4: Store all coordinates into the arrayList coordinatesList, identify head and tail protector bricks and mark all bricks as removed=true or removed=false.
    public static void addCoordinates() {

        System.out.println("add coordinates called. coordinatesList size:"+ coordinatesList.size());
        int count = 0;
        int x, y, z;
        for (int k = 0; k <= MainFrame.depth; k++)
            for (int j = 0; j < MainFrame.height; j++)
                for (int i = 0; i < MainFrame.width; i++) {

                    coordinatesList.add(new XYZCoordinates(i, j, k));
                    ivalue[i][j][k] = count;
                    if(k == 0){
                        MainFrame.deletedCoordinates[i][j][k] = true;
                    }
                    //while storing the voxel coordinates, find out if it has been deleted
                    if (MainFrame.deletedCoordinates[i][j][k] == true) {
                        coordinatesList.get(count).isRemoved = true;
                        System.out.println("removed coordinates: "+i+""+j+""+k);
                    }
                    count++;
                }

        System.out.println("after adding, coordinatesList size:"+ coordinatesList.size());
        //now we identify the head bricks i.e. the coordinates for min. z for each (x,y) pair so that protector bricks may be attached to them.
        z = 1;
        for (int i = 0; i < MainFrame.width; i++)
            for (int j = 0; j < MainFrame.height; j++) {
                while (coordinatesList.get(ivalue[i][j][z]).isRemoved == true && z < MainFrame.depth) {
                    z++;
                }
                coordinatesList.get(ivalue[i][j][z]).isHeadBrick = true;
            }
        ////now we identify the tail bricks i.e. the coordinates for max. z for each (x,y) pair so that protector bricks may be added to them.
        z = MainFrame.depth;
        for (int i = MainFrame.width - 1; i >= 0; i--)
            for (int j = MainFrame.height - 1; j >= 0; j--) {
                while (coordinatesList.get(ivalue[i][j][z]).isRemoved == true && z > 0) {
                    z--;
                }
                coordinatesList.get(ivalue[i][j][z]).isTailBrick = true;
            }
        //adding the protector bricks for the head and tail bricks
        int s = coordinatesList.size();
        if (MainFrame.isMinZProtectorPlaneEnabled == true) {
            System.out.println("function called to Head protector bricks");
            for (int i = 0; i < s; i++) {
                if (coordinatesList.get(i).isHeadBrick && coordinatesList.get(i).isRemoved == false) {
                    x = coordinatesList.get(i).x;
                    y = coordinatesList.get(i).y;
                    z = coordinatesList.get(i).z - 1;
                    ///when a protector brick is added in place of a removed brick,
                    // in order to avoid adding a new element to the arraylist,
                    // we simply set the removed brick to head brick.
                    if (coordinatesList.get(ivalue[x][y][z]).isRemoved == true) {
                        coordinatesList.get(ivalue[x][y][z]).isHeadBrick = true;
                        System.out.println("Head protector brick added at " + x + " " + y + " " + z);
                    }
                    else {
                        System.out.println("error in adding head protector brick at " + x + " " + y + " " + z);
                    }
                    //the previously assigned head brick is now set to false as now the protector brick is the new head brick.
                    coordinatesList.get(i).isHeadBrick = false;
                }
            }
            //when a protector brick is added in place of a removed brick, the isRemoved condition is set to false.
            for (int i = 0; i < coordinatesList.size(); i++) {
                if (coordinatesList.get(i).isHeadBrick && coordinatesList.get(i).isRemoved == true) {
                    coordinatesList.get(i).isRemoved = false;
                }
            }
        }
        if (MainFrame.isMaxZProtectorPlaneEnabled == true) {
            //when a protector brick is added in place of a removed brick, the isRemoved condition is set to false.
            for (int i = 0; i < coordinatesList.size(); i++) {
                if (coordinatesList.get(i).isTailBrick && coordinatesList.get(i).isRemoved == true) {
                    coordinatesList.get(i).isRemoved = false;
                }
            }
        }
    }

    //STEP 5: identify all the half bricks that can be converted to boundary bricks
    public void identifyBoundaryVoxels() {
        int minX = 0, minY = 0, x, y;
        int maxX = MainFrame.width - 1;
        int maxY = MainFrame.height - 1;
        int maxZ = MainFrame.depth + 1;
        int minZ = 1;

        if (MainFrame.isMaxZProtectorPlaneEnabled)
            maxZ = MainFrame.depth + 1;
        if (MainFrame.isMinZProtectorPlaneEnabled)
            minZ = 0;

        for (int z = minZ; z < maxZ; z++)
            for (y = 0; y < MainFrame.height; y++) {

                while (coordinatesList.get(ivalue[minX][y][z]).isRemoved == true && minX < MainFrame.width) {
                    minX++;
                }
                if (coordinatesList.get(ivalue[minX][y][z]).isHeadBrick == false) {
                    coordinatesList.get(ivalue[minX][y][z]).isLeftmostBrick = true;
                    //System.out.println("Leftmost brick at " + minX + " " + y + " " + z);
                }

                while (coordinatesList.get(ivalue[maxX][y][z]).isRemoved == true && maxX > 0) {
                    maxX--;
                }
                if (coordinatesList.get(ivalue[maxX][y][z]).isHeadBrick == false) {
                    coordinatesList.get(ivalue[maxX][y][z]).isRightmostBrick = true;
                    //System.out.println("Rightmost brick at " + maxX + " " + y + " " + z);
                }

            }

        for (int z = minZ; z < maxZ; z++)
            for (x = 0; x < MainFrame.width; x++) {
                while (coordinatesList.get(ivalue[x][minY][z]).isRemoved == true && minY < MainFrame.height) {
                    minY++;
                }
                if (coordinatesList.get(ivalue[x][minY][z]).isHeadBrick == false) {
                    coordinatesList.get(ivalue[x][minY][z]).isTopmostBrick = true;
                    //System.out.println("Topmost brick at " + x + " " + minY + " " + z);
                }

                while (coordinatesList.get(ivalue[x][maxY][z]).isRemoved == true && maxY > 0) {
                    maxY--;
                }
                if (coordinatesList.get(ivalue[x][maxY][z]).isHeadBrick == false) {
                    coordinatesList.get(ivalue[x][maxY][z]).isBottommostBrick = true;
                    //System.out.println("Bottommost brick at " + x + " " + maxY + " " + z);
                }
            }
    }

    //STEP 6: Assign orientation for all voxels in the coordinatesList
    public static void assignOrientation() {
        for (int i = 0; i < coordinatesList.size(); i++) {
            if (coordinatesList.get(i).z % 4 == 0) {
                coordinatesList.get(i).orientation = 'n';
            } else if (coordinatesList.get(i).z % 4 == 1) {
                coordinatesList.get(i).orientation = 'w';
            } else if (coordinatesList.get(i).z % 4 == 2) {
                coordinatesList.get(i).orientation = 's';
            } else {
                coordinatesList.get(i).orientation = 'e';
            }

        }
    }

    //STEP 7: Assign domain numbers (1-2 or 3-4) to each voxel in the coordinate-list
    public static void assignDomain() {

        for (int i = 0; i < coordinatesList.size(); i++) {
            //Identification of north-bricks
            if (coordinatesList.get(i).orientation == 'n') {
                //identifying half-north-bricks
                // 1. when height is even half-brick is at coordinates x%2=0 and y=0 or at y=height-1 and x%2=0
                // 2. when height is odd half-brick is at coordinates x%2=0 and y=0 or at y=height-1 and x%2=1
                if (coordinatesList.get(i).y == 0 && coordinatesList.get(i).x % 2 == 0) {
                    coordinatesList.get(i).editdomain(12);
                    coordinatesList.get(i).isHalfBrick = true;
                } else if (MainFrame.height % 2 == 0 && coordinatesList.get(i).y == MainFrame.height - 1 && coordinatesList.get(i).x % 2 == 0) {
                    coordinatesList.get(i).editdomain(34);
                    coordinatesList.get(i).isHalfBrick = true;
                } else if (MainFrame.height % 2 == 1 && coordinatesList.get(i).y == MainFrame.height - 1 && coordinatesList.get(i).x % 2 == 1) {
                    coordinatesList.get(i).editdomain(34);
                    coordinatesList.get(i).isHalfBrick = true;
                }

                //identifying other full-north-bricks
                else if (((coordinatesList.get(i).y % 2 == 1 && coordinatesList.get(i).x % 2 == 0) &&
                        (coordinatesList.get(i).y != MainFrame.height - 1)) ||
                        (coordinatesList.get(i).y % 2 == 0 && coordinatesList.get(i).x % 2 == 1) &&
                                (coordinatesList.get(i).y != MainFrame.height - 1)) {
                    coordinatesList.get(i).editdomain(34);
                } else
                    coordinatesList.get(i).editdomain(12);
            }
            //Identifying west-bricks
            else if (coordinatesList.get(i).orientation == 'w') {
                //Identifying half-west-bricks
                // 1. when width is even, half-bricks are at coordinates x=0 and y%2=1 or at y%2=1 and x=width-1
                // 2. when width is odd, half-bricks are at coordinates x=0 and y%2=1 or at y%2=0 and x=width-1
                if (coordinatesList.get(i).x == 0 && coordinatesList.get(i).y % 2 == 1) {
                    coordinatesList.get(i).editdomain(12);
                    coordinatesList.get(i).isHalfBrick = true;
                } else if (MainFrame.width % 2 == 0 && coordinatesList.get(i).x == MainFrame.width - 1 && coordinatesList.get(i).y % 2 == 1) {
                    coordinatesList.get(i).editdomain(34);
                    coordinatesList.get(i).isHalfBrick = true;
                } else if (MainFrame.width % 2 == 1 && coordinatesList.get(i).x == MainFrame.width - 1 && coordinatesList.get(i).y % 2 == 0) {
                    coordinatesList.get(i).editdomain(34);
                    coordinatesList.get(i).isHalfBrick = true;
                }

                //Identifying other full-west-bricks
                else if (((coordinatesList.get(i).y % 2 == 1 && coordinatesList.get(i).x % 2 == 0) &&
                        (coordinatesList.get(i).x != 0)) ||
                        (coordinatesList.get(i).y % 2 == 0 && coordinatesList.get(i).x % 2 == 1)) {
                    coordinatesList.get(i).editdomain(12);
                } else
                    coordinatesList.get(i).editdomain(34);
            }
            //Identifying south-bricks
            else if (coordinatesList.get(i).orientation == 's') {
                //Identifying half-south-bricks
                // 1. when height is even , half-bricks are at coordinates x%2=1 and y=0 or at y=height-1 and x%2=1
                // 2. when height is odd , half-bricks are at coordinates x%2=1 and y=0 or at y=height-1 and x%2=0
                if (coordinatesList.get(i).y == 0 && coordinatesList.get(i).x % 2 == 1) {
                    coordinatesList.get(i).editdomain(34);
                    coordinatesList.get(i).isHalfBrick = true;
                } else if (MainFrame.height % 2 == 0 && coordinatesList.get(i).y == MainFrame.height - 1 && coordinatesList.get(i).x % 2 == 1) {
                    coordinatesList.get(i).editdomain(12);
                    coordinatesList.get(i).isHalfBrick = true;
                } else if (MainFrame.height % 2 == 1 && coordinatesList.get(i).y == MainFrame.height - 1 && coordinatesList.get(i).x % 2 == 0) {
                    coordinatesList.get(i).editdomain(12);
                    coordinatesList.get(i).isHalfBrick = true;
                }

                //Identifying other full-south-bricks
                else if (((coordinatesList.get(i).y % 2 == 1 && coordinatesList.get(i).x % 2 == 1) &&
                        (coordinatesList.get(i).y != MainFrame.height - 1)) ||
                        (coordinatesList.get(i).y % 2 == 0 && coordinatesList.get(i).x % 2 == 0) &&
                                (coordinatesList.get(i).y != MainFrame.height - 1)) {
                    coordinatesList.get(i).editdomain(12);
                } else
                    coordinatesList.get(i).editdomain(34);
            }
            //Identifying east-bricks
            else if (coordinatesList.get(i).orientation == 'e') {
                //Identifying half-east-bricks
                // 1. when width is even ,the half-bricks are at coordinates x=0 and y%2=0 or at y%2=0 and x=width-1
                // 2. when width is odd ,the half-bricks are at coordinates x=0 and y%2=0 or at y%2=1 and x=width-1
                if (coordinatesList.get(i).x == 0 && coordinatesList.get(i).y % 2 == 0) {
                    coordinatesList.get(i).editdomain(34);
                    coordinatesList.get(i).isHalfBrick = true;
                } else if (MainFrame.width % 2 == 0 && coordinatesList.get(i).x == MainFrame.width - 1 && coordinatesList.get(i).y % 2 == 0) {
                    coordinatesList.get(i).editdomain(12);
                    coordinatesList.get(i).isHalfBrick = true;
                } else if (MainFrame.width % 2 == 1 && coordinatesList.get(i).x == MainFrame.width - 1 && coordinatesList.get(i).y % 2 == 1) {
                    coordinatesList.get(i).editdomain(12);
                    coordinatesList.get(i).isHalfBrick = true;
                }

                //Identifying other full-east-bricks
                else if (((coordinatesList.get(i).y % 2 == 0 && coordinatesList.get(i).x % 2 == 1) &&
                        (coordinatesList.get(i).x != MainFrame.width - 1)) ||
                        (coordinatesList.get(i).y % 2 == 1 && coordinatesList.get(i).x % 2 == 0) &&
                                (coordinatesList.get(i).x != MainFrame.width - 1)) {
                    coordinatesList.get(i).editdomain(12);
                } else
                    coordinatesList.get(i).editdomain(34);
            }
        //if(coordinatesList.get(i).isHalfBrick && (coordinatesList.get(i).isHeadBrick || coordinatesList.get(i).isTailBrick))
        //    coordinatesList.get(i).isRemoved = true;
        }

    }

    //STEP 8: Assign all sequences as null by default
    public static void assignNullSequences(){

        for(int i=0;i<coordinatesList.size();i++){

            coordinatesList.get(i).Domain1=null;
            coordinatesList.get(i).Domain2=null;
            coordinatesList.get(i).Domain3=null;
            coordinatesList.get(i).Domain4=null;

        }
    }

    //STEP 9: Replace the remaining complementary strands of the deleted voxels with (8-length) poly-T sequences.
    public void convertToT(){
        for(int i=0;i<coordinatesList.size();i++){
            x=coordinatesList.get(i).x;
            y=coordinatesList.get(i).y;
            z=coordinatesList.get(i).z;

            if(coordinatesList.get(i).isRemoved==true ){
                System.out.println("Removed found in convert-to-T:"+x+""+y+""+z);
                if(z > 0&& MainFrame.isMinZProtectorPlaneEnabled || z>1&&!MainFrame.isMinZProtectorPlaneEnabled){
                    indexback=ivalue[x][y][z-1];
                    if(coordinatesList.get(indexback).isRemoved == false){
                        System.out.println("Searching in convert-to-T:"+x+""+y+""+(z-1));
                        if(coordinatesList.get(i).domain==34)
                            coordinatesList.get(indexback).isDomain1Replaced = true;

                        else
                            coordinatesList.get(indexback).isDomain4Replaced = true;
                    }
                }
                if(z <MainFrame.depth){
                    indexfront=ivalue[x][y][z+1];
                    if(coordinatesList.get(indexfront).isRemoved == false){
                        System.out.println("Searching in convert-to-T:"+x+""+y+""+(z+1));
                        if(coordinatesList.get(i).domain==34)
                            coordinatesList.get(indexfront).isDomain2Replaced = true;

                        else
                            coordinatesList.get(indexfront).isDomain3Replaced = true;
                    }
                }
            }
        }
    }

    //STEP 10: Identify and remove those voxels which have d3&d4 OR d1&d2 as T-sequences (i.e., when an entire voxel is composed of only Thymidine).
    public void removeTVoxels(){
        for(int i=0;i<coordinatesList.size();i++){

            x=coordinatesList.get(i).x;
            y=coordinatesList.get(i).y;
            z=coordinatesList.get(i).z;

            if(coordinatesList.get(i).isRemoved==false){
                if(coordinatesList.get(i).domain==34){
                    if(coordinatesList.get(i).Domain3.equals(Tsequence)&&
                            coordinatesList.get(i).Domain4.equals(Tsequence)){
                        System.out.println("removed in removeTVoxels:"+x+""+y+""+z);
                        coordinatesList.get(i).isRemoved=true;
                    }
                }
                else if(coordinatesList.get(i).domain==12){
                    if(coordinatesList.get(i).Domain1.equals(Tsequence)&&
                            coordinatesList.get(i).Domain2.equals(Tsequence)){
                        System.out.println("removed in removeTVoxels:"+x+""+y+""+z);
                        coordinatesList.get(i).isRemoved=true;
                    }
                }
            }
        }
    }

    //STEP 11: Combine voxels to form bricks of size=2 voxels, set them as full bricks and save them in the finalData list.
    public void setBrickList(){
        System.out.println("SetbrickList called");
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
                        finalData.get(brickCounter).isDomain3Replaced = coordinatesList.get(i).isDomain3Replaced;
                        finalData.get(brickCounter).isDomain4Replaced = coordinatesList.get(i).isDomain4Replaced;
                    }
                    else {
                        finalData.add(new VoxelToBrick(x, y, z, -1, -1, -1, coordinatesList.get(i).Domain1,
                                coordinatesList.get(i).Domain2, null, null));
                        finalData.get(brickCounter).isDomain1Replaced = coordinatesList.get(i).isDomain1Replaced;
                        finalData.get(brickCounter).isDomain2Replaced = coordinatesList.get(i).isDomain2Replaced;
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
                                finalData.get(brickCounter).isDomain1Replaced = coordinatesList.get(ivalue[x][y+1][z]).isDomain1Replaced;
                                finalData.get(brickCounter).isDomain2Replaced = coordinatesList.get(ivalue[x][y+1][z]).isDomain2Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        coordinatesList.get(i).Domain3, coordinatesList.get(i).Domain4));
                            }
                            finalData.get(brickCounter).isDomain3Replaced = coordinatesList.get(i).isDomain3Replaced;
                            finalData.get(brickCounter).isDomain4Replaced = coordinatesList.get(i).isDomain4Replaced;
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
                                finalData.get(brickCounter).isDomain3Replaced = coordinatesList.get(ivalue[x][y-1][z]).isDomain3Replaced;
                                finalData.get(brickCounter).isDomain4Replaced = coordinatesList.get(ivalue[x][y-1][z]).isDomain4Replaced;

                            }
                            else{
                                finalData.add(new VoxelToBrick(x,y,z,-1,-1,-1,coordinatesList.get(i).Domain1,
                                        coordinatesList.get(i).Domain2, null, null));
                            }

                            finalData.get(brickCounter).isDomain1Replaced = coordinatesList.get(i).isDomain1Replaced;
                            finalData.get(brickCounter).isDomain2Replaced = coordinatesList.get(i).isDomain2Replaced;

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
                                finalData.get(brickCounter).isDomain1Replaced = coordinatesList.get(ivalue[x+1][y][z]).isDomain1Replaced;
                                finalData.get(brickCounter).isDomain2Replaced = coordinatesList.get(ivalue[x+1][y][z]).isDomain2Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        coordinatesList.get(i).Domain3, coordinatesList.get(i).Domain4));
                            }
                            finalData.get(brickCounter).isDomain3Replaced = coordinatesList.get(i).isDomain3Replaced;
                            finalData.get(brickCounter).isDomain4Replaced = coordinatesList.get(i).isDomain4Replaced;
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
                                finalData.get(brickCounter).isDomain3Replaced = coordinatesList.get(ivalue[x-1][y][z]).isDomain3Replaced;
                                finalData.get(brickCounter).isDomain4Replaced = coordinatesList.get(ivalue[x-1][y][z]).isDomain4Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(x,y,z,-1,-1,-1,coordinatesList.get(i).Domain1,
                                        coordinatesList.get(i).Domain2, null, null));
                            }
                            finalData.get(brickCounter).isDomain1Replaced = coordinatesList.get(i).isDomain1Replaced;
                            finalData.get(brickCounter).isDomain2Replaced = coordinatesList.get(i).isDomain2Replaced;
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
                                finalData.get(brickCounter).isDomain3Replaced = coordinatesList.get(ivalue[x][y+1][z]).isDomain3Replaced;
                                finalData.get(brickCounter).isDomain4Replaced = coordinatesList.get(ivalue[x][y+1][z]).isDomain4Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,
                                        coordinatesList.get(i).Domain1, coordinatesList.get(i).Domain2));
                            }
                            finalData.get(brickCounter).isDomain1Replaced = coordinatesList.get(i).isDomain1Replaced;
                            finalData.get(brickCounter).isDomain2Replaced = coordinatesList.get(i).isDomain2Replaced;
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
                                finalData.get(brickCounter).isDomain1Replaced = coordinatesList.get(ivalue[x][y-1][z]).isDomain1Replaced;
                                finalData.get(brickCounter).isDomain2Replaced = coordinatesList.get(ivalue[x][y-1][z]).isDomain2Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z, null, null,coordinatesList.get(i).Domain3,
                                        coordinatesList.get(i).Domain4));
                            }
                            finalData.get(brickCounter).isDomain3Replaced = coordinatesList.get(i).isDomain3Replaced;
                            finalData.get(brickCounter).isDomain4Replaced = coordinatesList.get(i).isDomain4Replaced;
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
                                finalData.get(brickCounter).isDomain3Replaced = coordinatesList.get(ivalue[x+1][y][z]).isDomain3Replaced;
                                finalData.get(brickCounter).isDomain4Replaced = coordinatesList.get(ivalue[x+1][y][z]).isDomain4Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(x,y,z,-1,-1,-1,null, null,
                                        coordinatesList.get(i).Domain1, coordinatesList.get(i).Domain2));
                            }
                            finalData.get(brickCounter).isDomain1Replaced = coordinatesList.get(i).isDomain1Replaced;
                            finalData.get(brickCounter).isDomain2Replaced = coordinatesList.get(i).isDomain2Replaced;

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
                                finalData.get(brickCounter).isDomain1Replaced = coordinatesList.get(ivalue[x-1][y][z]).isDomain1Replaced;
                                finalData.get(brickCounter).isDomain2Replaced = coordinatesList.get(ivalue[x-1][y][z]).isDomain2Replaced;
                            }
                            else{
                                finalData.add(new VoxelToBrick(-1,-1,-1,x,y,z,null, null,coordinatesList.get(i).Domain3,
                                        coordinatesList.get(i).Domain4));
                            }
                            finalData.get(brickCounter).isDomain3Replaced = coordinatesList.get(i).isDomain3Replaced;
                            finalData.get(brickCounter).isDomain4Replaced = coordinatesList.get(i).isDomain4Replaced;

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

    //STEP 12: Assign helix number to each voxel
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

    //STEP 13: Map each coordinate to DNA sequence fetched from the imported file
    public void mapSequencesFromList(){
        int index = -1;
        int h1, h2, h3, h4, z1, z2, z3, z4;
        boolean domain1Replacement, domain2Replacement, domain3Replacement, domain4Replacement;

        for(int i = 0; i<finalData.size(); i++){
            index = -1;
            //identifying half-bricks having domains 34
            if(finalData.get(i).x2==-1){
                h3 = finalData.get(i).helix3;
                h4 = h3;
                z3 = finalData.get(i).z3;
                z4 = z3+1;

                for(int j = 0; j<halfBrickSequencesList.size(); j++){
                    if (halfBrickSequencesList.get(j).helix1 == h3&&
                            halfBrickSequencesList.get(j).zCoordinate1 == z3&&
                            halfBrickSequencesList.get(j).zCoordinate2 == z4&&
                            halfBrickSequencesList.get(j).helix2 == h3) {
                        index = j;

                        finalData.get(i).Domain3 = halfBrickSequencesList.get(index).domain1;
                        finalData.get(i).Domain4 = halfBrickSequencesList.get(index).domain2;
                        finalData.get(i).strandNumber = halfBrickSequencesList.get(index).strandNumber;
                        finalData.get(i).plateNumber = halfBrickSequencesList.get(index).plateNumber;
                        finalData.get(i).position = halfBrickSequencesList.get(index).position;

                        //System.out.println("halfbrick data added for domains 3 and 4 "+h3+" "+z3+" "+h4+" "+z4+" "+
                        //        finalData.get(i).Domain3+" "+finalData.get(i).Domain4);
                    }
                }
                if(index==-1) {
                    System.out.println("############ not found for " + -1 + " " + -1 + " " + -1 + " " + -1 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + "?" + index);
                    finalData.get(i).isMapped = false;
                }
                else {
                    //System.out.println("*************found for "+-1+" "+-1+" "+-1+" "+-1+" "+h3+" "+z3+" "+h4+" "+z4+"?"+index);
                }
            }
            //identifying half bricks having domains 1 and 2
            else if(finalData.get(i).x3==-1){
                h1 = finalData.get(i).helix1;
                h2 = h1;
                z1 = finalData.get(i).z1;
                z2 = finalData.get(i).z2;

                for(int j = 0; j<halfBrickSequencesList.size(); j++){
                    if (halfBrickSequencesList.get(j).helix2 == h1&&
                            halfBrickSequencesList.get(j).zCoordinate1 == z2&&
                            halfBrickSequencesList.get(j).zCoordinate2 == z1&&
                            halfBrickSequencesList.get(j).helix1 == h2) {
                        index = j;
                        String d1 = new StringBuilder(halfBrickSequencesList.get(index).domain2).reverse().toString();
                        String d2 = new StringBuilder(halfBrickSequencesList.get(index).domain1).reverse().toString();
                        finalData.get(i).Domain1 = d1;
                        finalData.get(i).Domain2 = d2;
                        finalData.get(i).strandNumber = halfBrickSequencesList.get(index).strandNumber;
                        finalData.get(i).plateNumber = halfBrickSequencesList.get(index).plateNumber;
                        finalData.get(i).position = halfBrickSequencesList.get(index).position;

                        //System.out.println("halfbrick data added for doimans 1 and 2 "+h1+" "+z1+" "+h2+" "+z2+" "+
                        //        finalData.get(i).Domain1+" "+finalData.get(i).Domain2);
                    }
                }
                if(index==-1) {
                    System.out.println("############ not found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + -1 + " " + -1 + " " + -1 + " " + -1 + "?" + index);
                    finalData.get(i).isMapped = false;
                }
                else{
                    //System.out.println("*************found for "+h1+" "+z1+" "+h2+" "+z2+" "+-1+" "+-1+" "+-1+" "+-1+"?"+index);
                }
            }
            //identifying full bricks
            else if((finalData.get(i).x2!=-1)&&(finalData.get(i).x3!=-1)){
                h1 = finalData.get(i).helix1;
                h2 = h1;
                h3 = finalData.get(i).helix3;
                h4 = h3;
                z1 = finalData.get(i).z1;
                z2 = z1 - 1;
                z4 = finalData.get(i).z4;
                z3 = z4 - 1;
                domain1Replacement = finalData.get(i).isDomain1Replaced ;
                domain2Replacement = finalData.get(i).isDomain2Replaced ;
                domain3Replacement = finalData.get(i).isDomain3Replaced ;
                domain4Replacement = finalData.get(i).isDomain4Replaced ;

                for(int j = 0; j<fullBrickSequencesList.size(); j++){
                    if (fullBrickSequencesList.get(j).helix1 == h1 &&
                            fullBrickSequencesList.get(j).zCoordinate1 == z1 &&
                            fullBrickSequencesList.get(j).zCoordinate2 == z2 &&
                            fullBrickSequencesList.get(j).helix3 == h3 &&
                            fullBrickSequencesList.get(j).helix2 == h2 &&
                            fullBrickSequencesList.get(j).helix4 == h4 &&
                            fullBrickSequencesList.get(j).zCoordinate3 == z3 &&
                            fullBrickSequencesList.get(j).zCoordinate4 == z4 &&
                            domain1Replacement == fullBrickSequencesList.get(j).isDomain1Replaced &&
                            domain2Replacement == fullBrickSequencesList.get(j).isDomain2Replaced &&
                            domain3Replacement == fullBrickSequencesList.get(j).isDomain3Replaced &&
                            domain4Replacement == fullBrickSequencesList.get(j).isDomain4Replaced ) {
                        index = j;
                    }
                }
                if(index!=-1){
                    finalData.get(i).Domain1 = fullBrickSequencesList.get(index).domain1;
                    finalData.get(i).Domain2 = fullBrickSequencesList.get(index).domain2;
                    finalData.get(i).Domain3 = fullBrickSequencesList.get(index).domain3;
                    finalData.get(i).Domain4 = fullBrickSequencesList.get(index).domain4;
                    finalData.get(i).strandNumber = fullBrickSequencesList.get(index).strandNumber;
                    finalData.get(i).plateNumber = fullBrickSequencesList.get(index).plateNumber;
                    finalData.get(i).position = fullBrickSequencesList.get(index).position;

                    /*System.out.println("fullbrick data added "+h1+" "+z1+" "+h2+" "+z2+" "+h3+" "+z3+" "+h4+" "+z4+" "+
                            finalData.get(i).Domain1+" "+finalData.get(i).Domain2
                            +" "+
                            finalData.get(i).Domain3+" "+finalData.get(i).Domain4);*/
                }
                if(index==-1) {
                    System.out.println("############ not found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + "?" + index);
                    finalData.get(i).isMapped = false;
                }
                else {
                    // System.out.println("*************found for "+h1+" "+z1+" "+h2+" "+z2+" "+h3+" "+z3+" "+h4+" "+z4+"?"+index);
                }
            }
        }
    }

    //STEP 14: If set boundary bricks option is enabled, combine bricks to form 48-nt boundary bricks
    // and re-map the coordinates to DNA sequence fetched from the imported file
    public void attachBoundaryBricks(){
        int x, y, z1, z2, z3, z4, z5, z6, h1, h2, h3, h4, h5, h6;
        int fullBrickIndex;
        int domains;
        String d1,d2;
        finalBoundaryData = finalData;

        for(int i = 0; i < finalBoundaryData.size() ; i++){
            if(finalBoundaryData.get(i).isMapped == true &&
                    (finalBoundaryData.get(i).isLeftmostHalfBrick ||
                            finalBoundaryData.get(i).isBottommostHalfBrick ||
                            finalBoundaryData.get(i).isTopmostHalfBrick ||
                            finalBoundaryData.get(i).isRightmostHalfBrick)) {
                if (finalBoundaryData.get(i).x3 == -1) {
                    x = finalBoundaryData.get(i).x2;
                    y = finalBoundaryData.get(i).y2;
                    z6 = finalBoundaryData.get(i).z1;
                    z5 = z6 - 1;
                    d1 = new String(finalBoundaryData.get(i).Domain1);
                    d2 = new String(finalBoundaryData.get(i).Domain2);
                    h5 = finalBoundaryData.get(i).helix1;
                    h6 = h5;
                    domains = 12;
                } else {
                    x = finalBoundaryData.get(i).x3;
                    y = finalBoundaryData.get(i).y3;
                    z5 = finalBoundaryData.get(i).y3;
                    z6 = z5 + 1;
                    d1 = new String(finalBoundaryData.get(i).Domain3);
                    d2 = new String(finalBoundaryData.get(i).Domain4);
                    h5 = finalBoundaryData.get(i).helix3;
                    h6 = h5;
                    domains = 34;

                }

                if (MainFrame.isMinXBoundaryPlaneEnabled &&
                        finalBoundaryData.get(i).isLeftmostHalfBrick &&
                        z5 > 1 &&
                        z5 != MainFrame.depth){//half bricks missing in Yonggang Ke's file

                    fullBrickIndex = newIndex[x][y][z5 - 2];
                    if (fullBrickIndex != -1) {
                        h1 = finalBoundaryData.get(fullBrickIndex).helix1;
                        h2 = h1;
                        h3 = finalBoundaryData.get(fullBrickIndex).helix3;
                        h4 = h3;
                        z1 = finalBoundaryData.get(fullBrickIndex).z1;
                        z2 = z1 - 1;
                        z3 = finalBoundaryData.get(fullBrickIndex).z3;
                        z4 = z3 + 1;

                        int index = -1;

                        for (int j = 0; j < boundaryBrickSequencesList.size(); j++) {
                            if (boundaryBrickSequencesList.get(j).helix1 == h1 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate1 == z1 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate2 == z2 &&
                                    boundaryBrickSequencesList.get(j).helix3 == h3 &&
                                    boundaryBrickSequencesList.get(j).helix2 == h2 &&
                                    boundaryBrickSequencesList.get(j).helix4 == h4 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate3 == z3 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate4 == z4 &&
                                    boundaryBrickSequencesList.get(j).helix5 == h5 &&
                                    boundaryBrickSequencesList.get(j).helix6 == h6 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate5 == z5 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate6 == z6) {
                                index = j;
                            }
                        }
                        if (index != -1) {
                            finalBoundaryData.get(fullBrickIndex).addDomains(x, y, z5, boundaryBrickSequencesList.get(index).domain5, boundaryBrickSequencesList.get(index).domain6, h5, h6, domains);
                            finalBoundaryData.get(fullBrickIndex).strandNumber = boundaryBrickSequencesList.get(index).strandNumber;
                            finalBoundaryData.get(fullBrickIndex).plateNumber = boundaryBrickSequencesList.get(index).plateNumber;
                            finalBoundaryData.get(fullBrickIndex).position = boundaryBrickSequencesList.get(index).position;
                            finalBoundaryData.get(fullBrickIndex).isBoundary = true;
                            finalBoundaryData.get(i).isMapped = false;
                            finalBoundaryData.get(i).isRemovedAsBoundary = true;

                            //System.out.println("Boundary brick data added " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                            //        +h5 + " " + z5 + " " + h6 + " " + z6 + " ");
                        }
                        else if (index == -1) {
                            System.out.println("############ Boundary brick not found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                                    +h5 + " " + z5 + " " + h6 + " " + z6 + "?" + domains);
                            finalBoundaryData.get(i).isMapped = true;
                            finalBoundaryData.get(i).isRemovedAsBoundary = false;
                        }
                        finalBoundaryData.get(i).isLeftmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for min x at" + x + " " + y + " " + z5);
                    }
                }
                if (MainFrame.isMaxXBoundaryPlaneEnabled &&
                        finalBoundaryData.get(i).isRightmostHalfBrick &&
                        z5 > 1 &&
                        //half bricks missing in Yonggang Ke's file
                        z5 != MainFrame.depth ) {
                    fullBrickIndex = newIndex[x][y][z5 - 2];
                    if (fullBrickIndex != -1) {
                        h1 = finalBoundaryData.get(fullBrickIndex).helix1;
                        h2 = h1;
                        h3 = finalBoundaryData.get(fullBrickIndex).helix3;
                        h4 = h3;
                        z1 = finalBoundaryData.get(fullBrickIndex).z1;
                        z2 = z1 - 1;
                        z3 = finalBoundaryData.get(fullBrickIndex).z3;
                        z4 = z3 + 1;

                        int index = -1;

                        for (int j = 0; j < boundaryBrickSequencesList.size(); j++) {
                            if (boundaryBrickSequencesList.get(j).helix1 == h1 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate1 == z1 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate2 == z2 &&
                                    boundaryBrickSequencesList.get(j).helix3 == h3 &&
                                    boundaryBrickSequencesList.get(j).helix2 == h2 &&
                                    boundaryBrickSequencesList.get(j).helix4 == h4 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate3 == z3 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate4 == z4 &&
                                    boundaryBrickSequencesList.get(j).helix5 == h5 &&
                                    boundaryBrickSequencesList.get(j).helix6 == h6 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate5 == z5 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate6 == z6) {
                                index = j;
                            }
                        }
                        if (index != -1) {
                            finalBoundaryData.get(fullBrickIndex).addDomains(x, y, z5, boundaryBrickSequencesList.get(index).domain5, boundaryBrickSequencesList.get(index).domain6, h5, h6, domains);
                            finalBoundaryData.get(fullBrickIndex).strandNumber = boundaryBrickSequencesList.get(index).strandNumber;
                            finalBoundaryData.get(fullBrickIndex).plateNumber = boundaryBrickSequencesList.get(index).plateNumber;
                            finalBoundaryData.get(fullBrickIndex).position = boundaryBrickSequencesList.get(index).position;
                            finalBoundaryData.get(fullBrickIndex).isBoundary = true;
                            finalBoundaryData.get(i).isMapped = false;
                            finalBoundaryData.get(i).isRemovedAsBoundary = true;

                            //System.out.println("Boundary brick data added " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                            //        +h5 + " " + z5 + " " + h6 + " " + z6 + " ");
                        }
                        else if (index == -1) {
                            System.out.println("############ Boundary brick not found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                                    +h5 + " " + z5 + " " + h6 + " " + z6 + "?" +domains);
                            finalBoundaryData.get(i).isMapped = true;
                            finalBoundaryData.get(i).isRemovedAsBoundary = false;
                        }
                        finalBoundaryData.get(i).isRightmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for max x at" + x + " " + y + " " + z2);
                    }
                }
                if (MainFrame.isMinYBoundaryPlaneEnabled && finalBoundaryData.get(i).isTopmostHalfBrick &&
                        z5 > 1 &&
                        //half bricks missing in Yonggang Ke's file
                        z5 != MainFrame.depth ) {

                    fullBrickIndex = newIndex[x][y][z5 - 2];
                    if (fullBrickIndex != -1) {
                        h1 = finalBoundaryData.get(fullBrickIndex).helix1;
                        h2 = h1;
                        h3 = finalBoundaryData.get(fullBrickIndex).helix3;
                        h4 = h3;
                        z1 = finalBoundaryData.get(fullBrickIndex).z1;
                        z2 = z1 - 1;
                        z3 = finalBoundaryData.get(fullBrickIndex).z3;
                        z4 = z3 + 1;

                        int index = -1;

                        for (int j = 0; j < boundaryBrickSequencesList.size(); j++) {
                            if (boundaryBrickSequencesList.get(j).helix1 == h1 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate1 == z1 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate2 == z2 &&
                                    boundaryBrickSequencesList.get(j).helix3 == h3 &&
                                    boundaryBrickSequencesList.get(j).helix2 == h2 &&
                                    boundaryBrickSequencesList.get(j).helix4 == h4 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate3 == z3 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate4 == z4 &&
                                    boundaryBrickSequencesList.get(j).helix5 == h5 &&
                                    boundaryBrickSequencesList.get(j).helix6 == h6 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate5 == z5 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate6 == z6) {
                                index = j;
                            }
                        }
                        if (index != -1) {
                            finalBoundaryData.get(fullBrickIndex).addDomains(x, y, z5, boundaryBrickSequencesList.get(index).domain5, boundaryBrickSequencesList.get(index).domain6, h5, h6, domains);
                            finalBoundaryData.get(fullBrickIndex).strandNumber = boundaryBrickSequencesList.get(index).strandNumber;
                            finalBoundaryData.get(fullBrickIndex).plateNumber = boundaryBrickSequencesList.get(index).plateNumber;
                            finalBoundaryData.get(fullBrickIndex).position = boundaryBrickSequencesList.get(index).position;
                            finalBoundaryData.get(fullBrickIndex).isBoundary = true;
                            finalBoundaryData.get(i).isMapped = false;
                            finalBoundaryData.get(i).isRemovedAsBoundary = true;

                            //System.out.println("Boundary brick data added " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                            //        +h5 + " " + z5 + " " + h6 + " " + z6 + " ");
                        }
                        else if (index == -1) {
                            System.out.println("############ Boundary brick not found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                                    +h5 + " " + z5 + " " + h6 + " " + z6 + "?" +domains);
                            finalBoundaryData.get(i).isMapped = true;
                            finalBoundaryData.get(i).isRemovedAsBoundary = false;
                        }

                        finalBoundaryData.get(i).isTopmostHalfBrick = false;
                        //System.out.println("BoundaryBrick for min y at" + x + " " + y + " " + z2);
                    }
                }
                if (MainFrame.isMaxYBoundaryPlaneEnabled && finalBoundaryData.get(i).isBottommostHalfBrick &&
                        z5 > 1 &&
                        //half bricks missing in Yonggang Ke's file
                        z5 != MainFrame.depth ) {

                    fullBrickIndex = newIndex[x][y][z5 - 2];
                    if (fullBrickIndex != -1) {
                        h1 = finalBoundaryData.get(fullBrickIndex).helix1;
                        h2 = h1;
                        h3 = finalBoundaryData.get(fullBrickIndex).helix3;
                        h4 = h3;
                        z1 = finalBoundaryData.get(fullBrickIndex).z1;
                        z2 = z1 - 1;
                        z3 = finalBoundaryData.get(fullBrickIndex).z3;
                        z4 = z3 + 1;

                        int index = -1;

                        for (int j = 0; j < boundaryBrickSequencesList.size(); j++) {
                            if (boundaryBrickSequencesList.get(j).helix1 == h1 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate1 == z1 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate2 == z2 &&
                                    boundaryBrickSequencesList.get(j).helix3 == h3 &&
                                    boundaryBrickSequencesList.get(j).helix2 == h2 &&
                                    boundaryBrickSequencesList.get(j).helix4 == h4 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate3 == z3 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate4 == z4 &&
                                    boundaryBrickSequencesList.get(j).helix5 == h5 &&
                                    boundaryBrickSequencesList.get(j).helix6 == h6 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate5 == z5 &&
                                    boundaryBrickSequencesList.get(j).zCoordinate6 == z6) {
                                index = j;
                            }
                        }
                        if (index != -1) {
                            finalBoundaryData.get(fullBrickIndex).addDomains(x, y, z5, boundaryBrickSequencesList.get(index).domain5, boundaryBrickSequencesList.get(index).domain6, h5, h6, domains);
                            finalBoundaryData.get(fullBrickIndex).strandNumber = boundaryBrickSequencesList.get(index).strandNumber;
                            finalBoundaryData.get(fullBrickIndex).plateNumber = boundaryBrickSequencesList.get(index).plateNumber;
                            finalBoundaryData.get(fullBrickIndex).position = boundaryBrickSequencesList.get(index).position;
                            finalBoundaryData.get(fullBrickIndex).isBoundary = true;
                            finalBoundaryData.get(i).isMapped = false;
                            finalBoundaryData.get(i).isRemovedAsBoundary = true;

                            //System.out.println("Boundary brick data added " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                            //        +h5 + " " + z5 + " " + h6 + " " + z6 + " ");
                        }
                        else if (index == -1) {
                            System.out.println("############ Boundary brick not found for " + h1 + " " + z1 + " " + h2 + " " + z2 + " " + h3 + " " + z3 + " " + h4 + " " + z4 + " " +
                                    +h5 + " " + z5 + " " + h6 + " " + z6 + "?" + domains);
                            finalBoundaryData.get(i).isMapped = true;
                            finalBoundaryData.get(i).isRemovedAsBoundary = false;
                        }

                        finalBoundaryData.get(i).isBottommostHalfBrick = false;
                        System.out.println("BoundaryBrick for max y at" + x + " " + y + " " + z2);
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