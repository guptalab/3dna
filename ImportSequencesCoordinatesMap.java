
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

    //using a 3d array to store the position for each voxel in the arraylist coordinate-list
    static int[][][] ivalue = new int[20][20][20];
    static SetSequencesFromFile s;
//	static SaveExcelFile excel;

    //constructor for the class
    public ImportSequencesCoordinatesMap() {


        //System.out.println("Coordinate-sequence-map called");
        //clear all arraylists
        coordinatesList.clear();
        for (int k = 0; k <= MainFrame.depth; k++)
            for (int j = 0; j < MainFrame.height; j++)
                for (int i = 0; i < MainFrame.width; i++)
                    ivalue[i][j][k] = -1;
        System.out.println("*********ImportSequencesCoordinatesMap called");
        readFullBrickSequences();
        readHalfBrickSequences();
        readBoundaryBrickSequences();
        addCoordinates();
        identifyBoundaryVoxels();
        assignOrientation();
        assignDomain();
        assignNullSequences();
        s = new SetSequencesFromFile();
        System.out.println("*********ImportSequencesCoordinatesMap completed");

    }

    //This function saves sequences, helix and coordinates from 16ntFile.csv to an arraylist - FullBrickSequencesList
    public static void readFullBrickSequences() {
        int fullBrickCounter = 0;
        fullBrickSequencesList.clear();
        String csvFile = "32ntFile.csv";
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

    //This function saves sequences, helix and coordinates from 16ntFile.csv to an arraylist - HalfBrickSequencesList
    public static void readHalfBrickSequences() {

        int halfBrickCounter = 0;
        halfBrickSequencesList.clear();
        String csvFile = "16ntFile.csv";
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

    //This function saves sequences, helix and coordinates from 48ntFile.csv to an arraylist - boundaryBrickSequencesList
    public static void readBoundaryBrickSequences() {

        int boundaryBrickCounter = 0;
        boundaryBrickSequencesList.clear();
        String csvFile = "48ntFile.csv";
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

    //this function stores all coordinates into the arrayList coordinatesList and marks them as removed=true or removed=false
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

    //This function assigns orientation for all voxels in the coordinate-list
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

    //This function assigns domain numbers (1-2 or 3-4) to each voxel in the coordinate-list
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
        if(coordinatesList.get(i).isHalfBrick && (coordinatesList.get(i).isHeadBrick || coordinatesList.get(i).isTailBrick))
            coordinatesList.get(i).isRemoved = true;
        }

    }

    public static void assignNullSequences(){

        for(int i=0;i<coordinatesList.size();i++){

            coordinatesList.get(i).Domain1=null;
            coordinatesList.get(i).Domain2=null;
            coordinatesList.get(i).Domain3=null;
            coordinatesList.get(i).Domain4=null;

        }
    }

}