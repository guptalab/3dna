import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class EstimatorActionListener implements ActionListener {

    public CoordinatesSequenceMap c;
    static ArrayList<VoxelToBrick> DNASequenceData=CoordinatesSequenceMap.brickList;
    @Override
    public void actionPerformed(ActionEvent e) {
        c=new CoordinatesSequenceMap();
        String fn=JOptionPane.showInputDialog(null, "Enter cost for a base (USD): ", "3DNA Cost Estimator", JOptionPane.QUESTION_MESSAGE)  ;
        float n1=Float.parseFloat(fn);
        int domainCount=0;
        float totalcost=0;

        for(int i=0;i<DNASequenceData.size();i++){
            if(DNASequenceData.get(i).Domain1!=null)
                domainCount++;
            if(DNASequenceData.get(i).Domain2!=null)
                domainCount++;
            if(DNASequenceData.get(i).Domain3!=null)
                domainCount++;
            if(DNASequenceData.get(i).Domain4!=null)
                domainCount++;
            if(DNASequenceData.get(i).Domain5!=null)
                domainCount++;
            if(DNASequenceData.get(i).Domain6!=null)
                domainCount++;

        }
        totalcost=(domainCount*8)*n1;
        JOptionPane.showMessageDialog(null, "Total cost (USD) for "+DNASequenceData.size()+" sequences with a total of "+
                domainCount*8+" bases :"+totalcost, "3DNA Cost Estimator", JOptionPane.INFORMATION_MESSAGE );


    }

}