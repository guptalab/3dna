/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


public class SaveDetailedBoundaryDataListener implements ActionListener {
	
	public CoordinatesSeqMap c;
	static ArrayList<VoxelToBrick> DNASequenceData=SaveFinalBoundarySequences.FinalData;
	@Override
	public void actionPerformed(ActionEvent e) {
		c=new CoordinatesSeqMap();
		// TODO Auto-generated method stub
		BufferedWriter bufferedWriter = null;
        try {
			bufferedWriter = new BufferedWriter(new FileWriter("3DNA_output" + ".csv"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        System.out.println("csv file created");
		//if file doesnt exists, then create it

		try {
			
		       
	            
	            bufferedWriter.write("DNA Sequences");
	            bufferedWriter.write(",");
	            bufferedWriter.write("Voxels");
	            bufferedWriter.write("\n");
	            for(int i=0;i<DNASequenceData.size();i++){
	            	System.out.println("printing for"+i);
	                  bufferedWriter.write(DNASequenceData.get(i).getCompleteSequence());
	                  bufferedWriter.write(",");
	                  if(DNASequenceData.get(i).voxel1[0]==-1){
	                	  bufferedWriter.write("["+DNASequenceData.get(i).voxel2[0]+""+
	                			  DNASequenceData.get(i).voxel2[1]+""+
	                			  DNASequenceData.get(i).voxel2[2]+"]");
	                	  bufferedWriter.write(",");  
	                  }
	                  else if(DNASequenceData.get(i).voxel2[0]==-1){
	                	  bufferedWriter.write("["+DNASequenceData.get(i).voxel1[0]+""+
	                			  DNASequenceData.get(i).voxel1[1]+""+
	                			  DNASequenceData.get(i).voxel1[2]+"]");
	                	  bufferedWriter.write(",");
	                  }	  
	                  else{
	                	  bufferedWriter.write("["+DNASequenceData.get(i).voxel1[0]+""+
	                			  DNASequenceData.get(i).voxel1[1]+""+
	                			  DNASequenceData.get(i).voxel1[2]+" "+
	                			  DNASequenceData.get(i).voxel2[0]+""+
	                			  DNASequenceData.get(i).voxel2[1]+""+
	                			  DNASequenceData.get(i).voxel2[2]+"]");
	                  }
	            
	                  bufferedWriter.write("\n");
	            }
	            
	                bufferedWriter.write("\n\n Generated using 3DNA");
	                bufferedWriter.write(",");
	                bufferedWriter.write("(http://www.guptalab.org/dnapen/)");
	                bufferedWriter.close();
	                JOptionPane.showMessageDialog(null, "File Saved Successfully !",
                            "Success!", JOptionPane.INFORMATION_MESSAGE);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	   
               
	}
}


