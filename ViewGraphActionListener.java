import com.sun.j3d.utils.universe.ViewingPlatform;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import javax.swing.*;

/** Author: Shikhar K Gupta, Foram Joshi
 * Project: DNA Pen
 * Mentor: Prof. Manish K Gupta
 */

public class ViewGraphActionListener extends JFrame implements ActionListener {

    // CoordinatesSeqMap c=new CoordinatesSeqMap();
    public static int eightBPSimilarity = 0;
    public static int sevenBPSimilarity = 0;
    public static int sixBPSimilarity = 0;
    public static int domainCount = 0;

    public ViewGraphActionListener(String title) {
        super(title);
        final CategoryDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(500, 270));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset() {

        // row keys...
        final String series1 = "No. of Sequences";

        // column keys...
        final String category1 = "8";
        final String category2 = "7";
        final String category3 = "6";

        // create the dataset...
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        dataset.addValue(eightBPSimilarity, series1, category1);
        dataset.addValue(sevenBPSimilarity, series1, category2);
        dataset.addValue(sixBPSimilarity, series1, category3);

        return dataset;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        createGraph();
        final ViewGraphActionListener viewGraphActionListener = new ViewGraphActionListener("3DNA Domain Graph");
        ImageIcon img = new ImageIcon("images/logod.png");
        Image imag=img.getImage();
        viewGraphActionListener.setIconImage(imag);
        viewGraphActionListener.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewGraphActionListener.pack();
        RefineryUtilities.centerFrameOnScreen(viewGraphActionListener);
        viewGraphActionListener.setVisible(true);
    }

    private JFreeChart createChart(final CategoryDataset dataset) {
        domainCount=0;


        for (int i = 0; i < CoordinatesSequenceMap.brickList.size(); i++) {
            if (CoordinatesSequenceMap.brickList.get(i).Domain1 != null)
                domainCount++;
            if (CoordinatesSequenceMap.brickList.get(i).Domain2 != null)
                domainCount++;
            if (CoordinatesSequenceMap.brickList.get(i).Domain3 != null)
                domainCount++;
            if (CoordinatesSequenceMap.brickList.get(i).Domain4 != null)
                domainCount++;
            if (CoordinatesSequenceMap.brickList.get(i).Domain5 != null)
                domainCount++;
            if (CoordinatesSequenceMap.brickList.get(i).Domain6 != null)
                domainCount++;
        }


        // create the chart...
        final JFreeChart chart = ChartFactory.createBarChart(
                "3DNA: Analysis of Identical Bases in " + domainCount + " Domains",         // chart title
                "No. of Identical bases",               // domain axis label
                "Pairs of Domains",                  // range axis label
                dataset,                 // data
                PlotOrientation.VERTICAL, // orientation
                true,                     // include legend
                true,                     // tooltips?
                false                     // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        final CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.black);
        plot.setRangeGridlinePaint(Color.black);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);
        renderer.setBarPainter(new StandardBarPainter());
        // set up gradient paints for series...
        final GradientPaint gp0 = new GradientPaint(
                0.0f, 0.0f, Color.blue,
                0.0f, 0.0f, Color.blue
        );
        final GradientPaint gp1 = new GradientPaint(
                0.0f, 0.0f, Color.green,
                0.0f, 0.0f, Color.green
        );
        final GradientPaint gp2 = new GradientPaint(
                0.0f, 0.0f, Color.red,
                0.0f, 0.0f, Color.red
        );
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, gp1);
        renderer.setSeriesPaint(2, gp2);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }

    public void createGraph() {

        eightBPSimilarity = 0;
        sevenBPSimilarity = 0;
        sixBPSimilarity = 0;
        int size=CoordinatesSequenceMap.brickList.size()*4;
        String[] Domains = new String[size];
        int counter = 0;

        for (int i = 0; i < CoordinatesSequenceMap.brickList.size(); i++) {
            if (CoordinatesSequenceMap.brickList.get(i).Domain1 != null && !(CoordinatesSequenceMap.brickList.get(i).Domain1.equals("TTTTTTTT"))) {
                Domains[counter] = CoordinatesSequenceMap.brickList.get(i).Domain1;
                counter++;
            }
            if (CoordinatesSequenceMap.brickList.get(i).Domain2 != null && !(CoordinatesSequenceMap.brickList.get(i).Domain2.equals("TTTTTTTT"))) {
                Domains[counter] = CoordinatesSequenceMap.brickList.get(i).Domain2;
                counter++;
            }
            if (CoordinatesSequenceMap.brickList.get(i).Domain3 != null && !(CoordinatesSequenceMap.brickList.get(i).Domain3.equals("TTTTTTTT"))) {
                Domains[counter] = CoordinatesSequenceMap.brickList.get(i).Domain3;
                counter++;
            }
            if (CoordinatesSequenceMap.brickList.get(i).Domain4 != null && !(CoordinatesSequenceMap.brickList.get(i).Domain4.equals("TTTTTTTT"))) {
                Domains[counter] = CoordinatesSequenceMap.brickList.get(i).Domain4;
                counter++;
            }
            if (CoordinatesSequenceMap.brickList.get(i).Domain5 != null && !(CoordinatesSequenceMap.brickList.get(i).Domain5.equals("TTTTTTTT"))) {
                Domains[counter] = CoordinatesSequenceMap.brickList.get(i).Domain5;
                counter++;
            }
            if (CoordinatesSequenceMap.brickList.get(i).Domain6 != null && !(CoordinatesSequenceMap.brickList.get(i).Domain6.equals("TTTTTTTT"))) {
                Domains[counter] = CoordinatesSequenceMap.brickList.get(i).Domain6;
                counter++;
            }
        }


        for (int i = 0; i < counter; i++) {
            for (int j = i + 1; j < counter; j++) {
                if (Domains[i].equals(Domains[j])) {
                    eightBPSimilarity++;
                    System.out.println(Domains[i] + "&& " + Domains[j]);

                }
                else if ((Domains[i].charAt(0) == Domains[j].charAt(0) && Domains[i].charAt(1) == Domains[j].charAt(1) &&
                        Domains[i].charAt(2) == Domains[j].charAt(2) && Domains[i].charAt(3) == Domains[j].charAt(3) &&
                        Domains[i].charAt(4) == Domains[j].charAt(4) && Domains[i].charAt(5) == Domains[j].charAt(5) &&
                        Domains[i].charAt(6) == Domains[j].charAt(6)) ||
                        (Domains[i].charAt(0) == Domains[j].charAt(1) && Domains[i].charAt(1) == Domains[j].charAt(2) &&
                                Domains[i].charAt(2) == Domains[j].charAt(3) && Domains[i].charAt(3) == Domains[j].charAt(4) &&
                                Domains[i].charAt(4) == Domains[j].charAt(5) && Domains[i].charAt(5) == Domains[j].charAt(6) &&
                                Domains[i].charAt(6) == Domains[j].charAt(7)) ||
                        (Domains[i].charAt(7) == Domains[j].charAt(7) && Domains[i].charAt(1) == Domains[j].charAt(1) &&
                                Domains[i].charAt(2) == Domains[j].charAt(2) && Domains[i].charAt(3) == Domains[j].charAt(3) &&
                                Domains[i].charAt(4) == Domains[j].charAt(4) && Domains[i].charAt(5) == Domains[j].charAt(5) &&
                                Domains[i].charAt(6) == Domains[j].charAt(6)) ||
                        (Domains[i].charAt(1) == Domains[j].charAt(0) && Domains[i].charAt(2) == Domains[j].charAt(1) &&
                                Domains[i].charAt(3) == Domains[j].charAt(2) && Domains[i].charAt(4) == Domains[j].charAt(3) &&
                                Domains[i].charAt(5) == Domains[j].charAt(4) && Domains[i].charAt(6) == Domains[j].charAt(5) &&
                                Domains[i].charAt(7) == Domains[j].charAt(6))) {
                    sevenBPSimilarity++;

                } else if (
                    //0-5=0-5
                        (Domains[i].charAt(0) == Domains[j].charAt(0) && Domains[i].charAt(1) == Domains[j].charAt(1) && Domains[i].charAt(2) == Domains[j].charAt(2) &&
                                Domains[i].charAt(3) == Domains[j].charAt(3) && Domains[i].charAt(4) == Domains[j].charAt(4) && Domains[i].charAt(5) == Domains[j].charAt(5)) ||
                                //0-5=1-6
                                (Domains[i].charAt(0) == Domains[j].charAt(1) && Domains[i].charAt(1) == Domains[j].charAt(2) && Domains[i].charAt(2) == Domains[j].charAt(3) &&
                                        Domains[i].charAt(3) == Domains[j].charAt(4) && Domains[i].charAt(4) == Domains[j].charAt(5) && Domains[i].charAt(5) == Domains[j].charAt(6)) ||
                                //1-6=1-6
                                (Domains[i].charAt(6) == Domains[j].charAt(6) && Domains[i].charAt(1) == Domains[j].charAt(1) && Domains[i].charAt(2) == Domains[j].charAt(2) &&
                                        Domains[i].charAt(3) == Domains[j].charAt(3) && Domains[i].charAt(4) == Domains[j].charAt(4) && Domains[i].charAt(5) == Domains[j].charAt(5)) ||
                                //2-7=2-7
                                (Domains[i].charAt(2) == Domains[j].charAt(2) && Domains[i].charAt(3) == Domains[j].charAt(3) && Domains[i].charAt(4) == Domains[j].charAt(4) &&
                                        Domains[i].charAt(5) == Domains[j].charAt(5) && Domains[i].charAt(6) == Domains[j].charAt(6) && Domains[i].charAt(7) == Domains[j].charAt(7)) ||
                                //1-6=0-5
                                (Domains[i].charAt(1) == Domains[j].charAt(0) && Domains[i].charAt(2) == Domains[j].charAt(1) && Domains[i].charAt(3) == Domains[j].charAt(2) &&
                                        Domains[i].charAt(4) == Domains[j].charAt(3) && Domains[i].charAt(5) == Domains[j].charAt(4) && Domains[i].charAt(6) == Domains[j].charAt(5)) ||
                                //0-5=2-7
                                (Domains[i].charAt(0) == Domains[j].charAt(2) && Domains[i].charAt(1) == Domains[j].charAt(3) && Domains[i].charAt(2) == Domains[j].charAt(4) &&
                                        Domains[i].charAt(3) == Domains[j].charAt(5) && Domains[i].charAt(4) == Domains[j].charAt(6) && Domains[i].charAt(5) == Domains[j].charAt(7)) ||
                                //0-5=2-7
                                (Domains[i].charAt(1) == Domains[j].charAt(2) && Domains[i].charAt(2) == Domains[j].charAt(3) && Domains[i].charAt(3) == Domains[j].charAt(4) &&
                                        Domains[i].charAt(4) == Domains[j].charAt(5) && Domains[i].charAt(5) == Domains[j].charAt(6) && Domains[i].charAt(6) == Domains[j].charAt(7)) ||
                                //2-7=0-5
                                (Domains[i].charAt(2) == Domains[j].charAt(0) && Domains[i].charAt(3) == Domains[j].charAt(1) && Domains[i].charAt(4) == Domains[j].charAt(2) &&
                                        Domains[i].charAt(5) == Domains[j].charAt(3) && Domains[i].charAt(6) == Domains[j].charAt(4) && Domains[i].charAt(7) == Domains[j].charAt(5)) ||
                                //2-7=1-6
                                (Domains[i].charAt(2) == Domains[j].charAt(1) && Domains[i].charAt(3) == Domains[j].charAt(2) && Domains[i].charAt(4) == Domains[j].charAt(3) &&
                                        Domains[i].charAt(5) == Domains[j].charAt(4) && Domains[i].charAt(6) == Domains[j].charAt(5) && Domains[i].charAt(7) == Domains[j].charAt(6)))
                    sixBPSimilarity++;
            }

        }

    }
}