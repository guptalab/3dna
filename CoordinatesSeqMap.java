/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class CoordinatesSeqMap {

	static ArrayList<DNASequence> RandomSeqList= new ArrayList<DNASequence>();
	static ArrayList<DNASequence> SeqList= new ArrayList<DNASequence>();
	public static ArrayList<XYZCoordinates> CoordinateList= new ArrayList<XYZCoordinates>();
	
	int size=SeqList.size();
	
	//using a 3d array to store the 'i' value for each voxel in the arraylist
	static int [][][]ivalue=new int[20][20][20];
	static int[] removed=new int[8000];
	static SaveFinalSequences s;
//	static SaveExcelFile excel;

    static int i1,i2,i3,i4;
	public CoordinatesSeqMap() {

        System.out.println("Coord seq map called");
		CoordinateList.clear();
        RandomSeqList.clear();
        SeqList.clear();
		for(int h=0;h<8000;h++){
			removed[h]=-1;
		}
		runRandomSequences();
		removeComplement();
		assignOrientation();
		assignDomain();
		
		AssignSequences();	
		s=new SaveFinalSequences();
		
		
		
		for(int h=0;h<8000;h++){
			if(removed[h]!=-1)
			System.out.println("removed List:"+removed[h]);
		}
		
	}


	  public static void runRandomSequences() {
	 
		String csvFile = "SequencesFile.csv";
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
	 
		try {
	 
			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {
	 
			        // use comma as separator
				RandomSeqList.add(new DNASequence(line.toString()));
		
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
	  
	  public static void removeComplement(){
		  int i,k;
		  String complement="";
		  System.out.println("size of list"+RandomSeqList.size());
		  for(i=0;i<RandomSeqList.size();i++){
			  SeqList.add(new DNASequence(changesize(RandomSeqList.get(i).getSequence())));
		  }
            /*
          for(i=0;i<SeqList.size();i++)
              for(int j=i+1;j<SeqList.size();j++) {
                  System.out.print("checking for"+i+" "+j);
                  if (SeqList.get(i).equals(negateSeqRev(SeqList.get(j).getSequence()))) {
                      SeqList.remove(j);
                      System.out.print("deleted from SeqList" + SeqList.get(j).getSequence());
                      j--;
                  }
              }*/
	  }
	  
//reducing size of sequence length to 8
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
	  //      System.out.println(""+Seq);
	        return (Seq);
	    }
	
	public static void assignOrientation(){
		int count=0;
		int h=0;
		for(int k=0;k<MainFrame.depth;k++)
			for(int j=0;j<MainFrame.height;j++)
				for(int i=0;i<MainFrame.width;i++){
					
					CoordinateList.add(new XYZCoordinates(i, j, k));
					ivalue[i][j][k]=count;
					System.out.println("saving from coord seq map :"+i+""+j+""+k);
					if(MainFrame.StoreCoordinates[i][j][k]==true){
						CoordinateList.get(count).isRemoved=true;
						removed[h]=i;
						removed[h+1]=j;
						removed[h+2]=k;
						h=h+3;
						System.out.println("removed:"+CoordinateList.get(count).x+""+CoordinateList.get(count).y+""+CoordinateList.get(count).z);
					}
					count++;
				}
				
		for(int i=0;i<CoordinateList.size();i++){
			if(CoordinateList.get(i).z%4==0){
				CoordinateList.get(i).orientation='n';
			}
			else if(CoordinateList.get(i).z%4==1){
				CoordinateList.get(i).orientation='w';
			}
			else if(CoordinateList.get(i).z%4==2){
				CoordinateList.get(i).orientation='s';
			}
			else{
				CoordinateList.get(i).orientation='e';
			}
			
		}
	}
	
	
	//reading random dna sequences from file
	public static void assignDomain(){
		for(int i=0;i<CoordinateList.size();i++){
			
			//identification of north-bricks
			if(CoordinateList.get(i).orientation=='n'){
				
				//marking half-bricks
				if(CoordinateList.get(i).y==0&&CoordinateList.get(i).x%2==0){
					CoordinateList.get(i).editdomain(12);
					CoordinateList.get(i).isHalfBrick=true;
				}
				else if(CoordinateList.get(i).y==MainFrame.height-1&&CoordinateList.get(i).x%2==0){
							CoordinateList.get(i).editdomain(34);
							CoordinateList.get(i).isHalfBrick=true;
						}
						
				//marking other full bricks
				else if(((CoordinateList.get(i).y%2==1&&CoordinateList.get(i).x%2==0)&&
						(CoordinateList.get(i).y!=MainFrame.height-1))||
						(CoordinateList.get(i).y%2==0&&CoordinateList.get(i).x%2==1)){
					CoordinateList.get(i).editdomain(34);
				}
				
				else
					CoordinateList.get(i).editdomain(12);
			}
			//checking for west bricks
			else if(CoordinateList.get(i).orientation=='w'){
				//marking half-bricks
				if(CoordinateList.get(i).x==0&&CoordinateList.get(i).y%2==1){
					CoordinateList.get(i).editdomain(12);
					CoordinateList.get(i).isHalfBrick=true;
				}
				
				else if(CoordinateList.get(i).x==MainFrame.width-1&&CoordinateList.get(i).y%2==1){
							CoordinateList.get(i).editdomain(34);
							CoordinateList.get(i).isHalfBrick=true;
						}
						
				//marking other full bricks
				else if(((CoordinateList.get(i).y%2==1&&CoordinateList.get(i).x%2==0)&&
						(CoordinateList.get(i).x!=0))||
						(CoordinateList.get(i).y%2==0&&CoordinateList.get(i).x%2==1)){
					CoordinateList.get(i).editdomain(12);
				}
				
				else
					CoordinateList.get(i).editdomain(34);	
			}
			//checking for south bricks
			else if(CoordinateList.get(i).orientation=='s'){
				//marking half-bricks
				if(CoordinateList.get(i).y==0&&CoordinateList.get(i).x%2==1){
					CoordinateList.get(i).editdomain(34);
					CoordinateList.get(i).isHalfBrick=true;
				}
				else if(CoordinateList.get(i).y==MainFrame.height-1&&CoordinateList.get(i).x%2==1){
							CoordinateList.get(i).editdomain(12);
							CoordinateList.get(i).isHalfBrick=true;
						}
				
				//marking other full bricks
				else if(((CoordinateList.get(i).y%2==1&&CoordinateList.get(i).x%2==1)&&
						(CoordinateList.get(i).y!=MainFrame.height-1))||
						(CoordinateList.get(i).y%2==0&&CoordinateList.get(i).x%2==0)){
					CoordinateList.get(i).editdomain(12);
				}
				
				else
					CoordinateList.get(i).editdomain(34);	
			}
			//checking for east bricks
			else if(CoordinateList.get(i).orientation=='e'){
				//marking half-bricks
				if(CoordinateList.get(i).x==0&&CoordinateList.get(i).y%2==0){
					CoordinateList.get(i).editdomain(34);
					CoordinateList.get(i).isHalfBrick=true;
				}
				if(CoordinateList.get(i).x==MainFrame.width-1&&CoordinateList.get(i).y%2==0){
							CoordinateList.get(i).editdomain(12);
							CoordinateList.get(i).isHalfBrick=true;
						}
				
				//marking other full bricks
				else if(((CoordinateList.get(i).y%2==0&&CoordinateList.get(i).x%2==1)&&
						(CoordinateList.get(i).x!=MainFrame.width-1))||
						(CoordinateList.get(i).y%2==1&&CoordinateList.get(i).x%2==0)){
					CoordinateList.get(i).editdomain(12);
				}
				
				else
					CoordinateList.get(i).editdomain(34);	
			}
		}
	}
	
	public static void AssignSequences(){

		for(int i=0;i<CoordinateList.size();i++){
			//complete random sequences for the first set of north bricks (z=0)
	//		System.out.println("assigning for "+CoordinateList.get(i).x+""+CoordinateList.get(i).y+
	//				""+CoordinateList.get(i).z);
			if(CoordinateList.get(i).orientation=='n'&&CoordinateList.get(i).z==0){
				if(CoordinateList.get(i).domain==12){
                    do {
                        CoordinateList.get(i).Domain1 = AssignRandom(1);
                        CoordinateList.get(i).Domain2 = AssignRandom(2);
                        if(!checkGCContent(CoordinateList.get(i).Domain1 ,CoordinateList.get(i).Domain2)){
                            SeqList.get(i1).isUsed=false;
                            SeqList.get(i2).isUsed=false;
                        }
                    }while(!checkGCContent(CoordinateList.get(i).Domain1 ,CoordinateList.get(i).Domain2));
                  //  System.out.println("assigned 1");
					CoordinateList.get(i).Domain4=null;
					CoordinateList.get(i).Domain3=null;
	//				System.out.println("assigned seq "+CoordinateList.get(i).x+""+CoordinateList.get(i).y+
	//						""+CoordinateList.get(i).z+"for coordinates"+CoordinateList.get(i).domain);
					
				}
				else if(CoordinateList.get(i).domain==34){
                    do {
                        CoordinateList.get(i).Domain3 = AssignRandom(3);
                        CoordinateList.get(i).Domain4 = AssignRandom(4);
                        if(!checkGCContent(CoordinateList.get(i).Domain3 ,CoordinateList.get(i).Domain4)){
                            SeqList.get(i3).isUsed=false;
                            SeqList.get(i4).isUsed=false;
                        }
                    }while (!checkGCContent(CoordinateList.get(i).Domain3 ,CoordinateList.get(i).Domain4));
                    System.out.println("assigned 2");
					CoordinateList.get(i).Domain1=null;
					CoordinateList.get(i).Domain2=null;
					
	//				System.out.println("assigned seq "+CoordinateList.get(i).x+""+CoordinateList.get(i).y+
		//					""+CoordinateList.get(i).z+"for coordinates"+CoordinateList.get(i).domain);
					
					
				}
			}
			
			else if(CoordinateList.get(i).z!=0){
	//			System.out.println("assigning for "+CoordinateList.get(i).x+""+CoordinateList.get(i).y+
	//					""+CoordinateList.get(i).z+"for domain "+CoordinateList.get(i).domain);
				int x,y,z,ival;
				x=CoordinateList.get(i).x;
				y=CoordinateList.get(i).y;
				z=CoordinateList.get(i).z;
				ival=ivalue[x][y][z-1];

		//		System.out.println("front sequence for"+CoordinateList.get(ival).x+""+CoordinateList.get(ival).y+
		//				""+CoordinateList.get(ival).z);

		//		System.out.print(" is"+CoordinateList.get(i).Domain1);
					if(CoordinateList.get(i).domain==34){

                           CoordinateList.get(i).Domain3 = negateSeqRev(CoordinateList.get(ival).Domain1);
                            do {
                            CoordinateList.get(i).Domain4 = AssignRandom(4);
                            if(!checkGCContent(CoordinateList.get(i).Domain3 ,CoordinateList.get(i).Domain4)){
                                SeqList.get(i4).isUsed=false;
                            }
                        }while(!checkGCContent(CoordinateList.get(i).Domain3 ,CoordinateList.get(i).Domain4));
                   //     System.out.println("assigned 3");
					}
					else if(CoordinateList.get(i).domain==12){

                        CoordinateList.get(i).Domain2 = negateSeqRev(CoordinateList.get(ival).Domain4);
                        do {
                            CoordinateList.get(i).Domain1 = AssignRandom(1);
                            if(!checkGCContent(CoordinateList.get(i).Domain1 ,CoordinateList.get(i).Domain2)){
                                SeqList.get(i1).isUsed=false;
                            }
                        }while(!checkGCContent(CoordinateList.get(i).Domain1 ,CoordinateList.get(i).Domain2));
                       // System.out.println("assigned 4");
					}
				
			}
		}
	}
	
	public static String AssignRandom(int domain){

		Random rand = new Random(); 
		int randomIndex;
		
		randomIndex= rand.nextInt(SeqList.size()); 
	    if(SeqList.get(randomIndex).isUsed==true)
		while(SeqList.get(randomIndex).isUsed==true||isMatching(SeqList.get(randomIndex).getSequence())==true){
			randomIndex= rand.nextInt(SeqList.size()); 
		}

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
		
		SeqList.get(randomIndex).isUsed=true;

		return(SeqList.get(randomIndex).getSequence());
	    
	}

    public static boolean isMatching(String domain){
        boolean isMatching=false;

            for (int i = 0; i < CoordinateList.size(); i++) {
                if( (domain.equals(CoordinateList.get(i).Domain1) )||( domain.equals(CoordinateList.get(i).Domain2)) ||
                        (domain.equals(CoordinateList.get(i).Domain3)) || (domain.equals(CoordinateList.get(i).Domain4))) {
                    isMatching = true;
                    System.out.println(domain+" matches with"+CoordinateList.get(i).Domain1+" or"+CoordinateList.get(i).Domain2+" or"
                            +CoordinateList.get(i).Domain3+" or"+CoordinateList.get(i).Domain4);
                }

        }
        return isMatching;
    }

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
