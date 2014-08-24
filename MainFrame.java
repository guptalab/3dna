/** Author: Shikhar K Gupta, Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URISyntaxException;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

@SuppressWarnings("serial")
// This is the main class of the software that invokes the main GUI.

public class MainFrame extends JFrame{

    public MainFrame(){
        super("3DNA");
        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //adding JPanels
        JPanel basePanel=new JPanel(); //Base JPanel on which all the other Split Panes/Toolbars/children JPanels are added
        basePanel.setLayout(new BorderLayout()); //Adding basePanel to the Frame
        getContentPane().add(basePanel);
        addPanels(); // calling function addPanels() that declares and adds other panels on the basePanel

        //configure Panels and Split Panes
        hPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT );
        basePanel.add(hPanel,BorderLayout.CENTER );
        vPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT );
        hPanel.setLeftComponent(leftJPanel );
        hPanel.setRightComponent(vPanel);
        vPanel.setTopComponent(topRightJPanel);
        vPanel.setBottomComponent(bottomRightJPanel );
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

        //add JMenuBar and JToolBar
        createAndShowGUI();
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
    }
    public void addPanels(){

        leftJPanel = new JPanel(); //leftJPanel supports all the buttons that are used to move around in the canvas
        leftJPanel.setLayout( new BorderLayout() );
        leftJPanel.setBackground(Color.DARK_GRAY);
        // Add leftJPanel Images and Buttons
        ImageIcon zoominIcon = new ImageIcon("icons/zoomin.png");
        ImageIcon zoomoutIcon = new ImageIcon("icons/zoomout.png");
        ImageIcon upArrowIcon = new ImageIcon("icons/arrow-up.png");
        ImageIcon downArrowIcon = new ImageIcon("icons/arrow-down.png");
        ImageIcon leftArrowIcon = new ImageIcon("icons/arrow-left.png");
        ImageIcon rightArrowIcon = new ImageIcon("icons/arrow-right.png");
        ImageIcon resetIcon = new ImageIcon("icons/reset.png");
        ImageIcon undoIcon = new ImageIcon("icons/undo.png");

        zoomInButton = new JButton(zoominIcon);
        zoomOutButton = new JButton(zoomoutIcon);
        upButton = new JButton(upArrowIcon);
        downButton = new JButton(downArrowIcon);
        leftButton = new JButton(leftArrowIcon);
        rightButton = new JButton(rightArrowIcon);
        resetButton = new JButton(resetIcon);
        undoButton = new JButton(undoIcon);

        zoomInButton.addActionListener(new ZoomInActionListener());
        zoomOutButton.addActionListener(new ZoomOutActionListener());
        upButton.addActionListener(new UpButtonActionListener());
        downButton.addActionListener(new DownButtonActionListener());
        leftButton.addActionListener(new LeftButtonActionListener());
        rightButton.addActionListener(new RightButtonActionListener());
        resetButton.addActionListener(new ResetButtonActionListener());
        undoButton.addActionListener(new UndoActionListener());

        JPanel mainPanel=new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.DARK_GRAY);
        GridBagConstraints gridBagConstraints=new GridBagConstraints();
        gridBagConstraints.insets=new Insets(10,10,5,5);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=0;
        mainPanel.add(zoomInButton,gridBagConstraints);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=1;
        mainPanel.add(zoomOutButton,gridBagConstraints);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=2;
        mainPanel.add(upButton,gridBagConstraints);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=3;
        mainPanel.add(leftButton,gridBagConstraints);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=4;
        mainPanel.add(rightButton,gridBagConstraints);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=5;
        mainPanel.add(downButton,gridBagConstraints);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=6;
        mainPanel.add(resetButton,gridBagConstraints);

        gridBagConstraints.gridx=0;
        gridBagConstraints.gridy=7;
        mainPanel.add(undoButton,gridBagConstraints);

        leftJPanel.add(mainPanel,BorderLayout.PAGE_START);

        //Main Display Panel
        topRightJPanel = new JPanel();
        topRightJPanel.setLayout( new BorderLayout() );
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setText("<html><body background=\"file:icons/background.jpg\" style=\"font-family:Century Gothic\"><img src='file:icons/logo_large.png' width='200' height='200'>" +
                "<h1>&nbsp;3DNA : A tool for DNA Sculpting</h1>" +
                "<h3>&nbsp;3DNA can be used to draw complex shapes on a 3D molecular canvas and provide stable,<br>" +
                "&nbsp;error-free sequences to envisage the structure drawn at nano-scale.<br> " +
                "&nbsp;The software is based on the research led by " +
                "<a href=\"http://yin.hms.harvard.edu/people/ke.yonggang/index.html\">Yonggang Ke</a>, " +
                "<a href=\"http://yin.hms.harvard.edu/people/ong.luvena/index.html\">Luvena L. Ong</a>," +
                "<a href=\"http://wyss.harvard.edu/viewpage/127/william-shih\">William M. Shih</a> and<br>&nbsp  " +
                "<a href=\"http://yin.hms.harvard.edu/people/yin.peng/index.html\">Peng Yin</a> (Harvard University) on <a href=\"http://www.sciencemag.org/content/338/6111/1177.short\">" +
                "Three-Dimensional Structures Self-Assembled from DNA Bricks.</a></h3>" +
                "<h3>&nbsp;3DNA has been developed for the sole-purpose of generating a user-friendly, " +
                "interactive<br> &nbsp;environment for users to envisage DNA structures, and get the " +
                "actual DNA sequences required<br> &nbsp;to make the structure physically.</h3>" +
                "</body></html>");
        editorPane.addHyperlinkListener(
                new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent hyperLinkEvent) {
                // TODO Auto-generated method stub
                if(hyperLinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        Desktop.getDesktop().browse(hyperLinkEvent.getURL().toURI());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        topRightJPanel.add(editorScrollPane);
    }
    public void createAndShowGUI(){
        //add ToolBar
        JToolBar toolBar=new JToolBar("Quick Links");
        toolBar.setBackground(Color.DARK_GRAY);
        this.add(toolBar,BorderLayout.NORTH);

        ImageIcon newi = new ImageIcon("icons/new-canvas.png");
        ImageIcon estimate = new ImageIcon("icons/estimator.png");
        ImageIcon importData = new ImageIcon("icons/import.png");
        ImageIcon exportData = new ImageIcon("icons/export.png");
        ImageIcon SaveAsPDF = new ImageIcon("icons/pdf.png");
        ImageIcon SaveAsLATEX = new ImageIcon("icons/latex.png");
        ImageIcon viewGraph = new ImageIcon("icons/analysis.png");
        ImageIcon SaveAsCSV = new ImageIcon("icons/csv.png");
        ImageIcon visualize = new ImageIcon("icons/visualize.png");
        ImageIcon ytube = new ImageIcon("icons/youtube.png");
        ImageIcon twit = new ImageIcon("icons/twitter.png");
        ImageIcon fb = new ImageIcon("icons/facebook.png");
        ImageIcon quora = new ImageIcon("icons/quora.png");
        ImageIcon about = new ImageIcon("icons/about.png");
        ImageIcon usermanual = new ImageIcon("icons/usermanual.png");
        ImageIcon feedback = new ImageIcon("icons/feedback.png");
        ImageIcon demo = new ImageIcon("icons/demo.png");

        JButton newButton = new JButton(newi);
        newButton.setBackground(Color.DARK_GRAY);

        estimateButton = new JButton(estimate);
        estimateButton.setBackground(Color.DARK_GRAY);

        JButton importButton = new JButton(importData);
        importButton.setBackground(Color.DARK_GRAY);

        exportButton = new JButton(exportData);
        exportButton.setBackground(Color.DARK_GRAY);

		latexButton = new JButton(SaveAsLATEX);
        latexButton.setBackground(Color.DARK_GRAY);

        pdfButton = new JButton(SaveAsPDF);
        pdfButton.setBackground(Color.DARK_GRAY);

        csvButton = new JButton(SaveAsCSV);
        csvButton.setBackground(Color.DARK_GRAY);

        graphButton = new JButton(viewGraph);
        graphButton.setBackground(Color.DARK_GRAY);

        visualizeButton = new JButton(visualize);
        visualizeButton.setBackground(Color.DARK_GRAY);

        JButton youtubeButton = new JButton(ytube);
        youtubeButton.setBackground(Color.DARK_GRAY);

        JButton fbButton = new JButton(fb);
        fbButton.setBackground(Color.DARK_GRAY);

        JButton twitterButton = new JButton(twit);
        twitterButton.setBackground(Color.DARK_GRAY);

        JButton quoraButton = new JButton(quora);
        quoraButton.setBackground(Color.DARK_GRAY);

        JButton aboutButton = new JButton(about);
        aboutButton.setBackground(Color.DARK_GRAY);

        JButton feedbackButton = new JButton(feedback);
        feedbackButton.setBackground(Color.DARK_GRAY);

        JButton userManualButton = new JButton(usermanual);
        userManualButton.setBackground(Color.DARK_GRAY);

        JButton demoButton = new JButton(demo);
        demoButton.setBackground(Color.DARK_GRAY);


        newButton.setToolTipText("Open New Canvas");
        estimateButton.setToolTipText("Estimator");
        importButton.setToolTipText("Import File");
        exportButton.setToolTipText("Export File");
		latexButton.setToolTipText("Save as LateX source file");
        pdfButton.setToolTipText("Save as PDF");
        graphButton.setToolTipText("3DNA domain analysis");
        csvButton.setToolTipText("Save as .csv");
        visualizeButton.setToolTipText("visualize");
        demoButton.setToolTipText("Software Demo");
        aboutButton.setToolTipText("About 3DNA");
        feedbackButton.setToolTipText("3DNA Feedback Page");
        userManualButton.setToolTipText("3DNA User Manual");

        //adding different buttons to the toolbar
        toolBar.add(newButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(importButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(exportButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(latexButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(pdfButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(csvButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.addSeparator();
        toolBar.add(estimateButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalStrut(80));
        toolBar.addSeparator();
        toolBar.add(graphButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(aboutButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(userManualButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(feedbackButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(demoButton);
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalStrut(80));
        toolBar.addSeparator();
        toolBar.add(fbButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(youtubeButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(quoraButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(twitterButton);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.addSeparator();
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(visualizeButton);

        FalseEnableContent();

        estimateButton.addActionListener(new EstimatorActionListener());
        newButton.addActionListener(new CanvasActionListener());
		latexButton.addActionListener(new SaveLatexActionListener());
        pdfButton.addActionListener(new SavePDFListener());
        csvButton.addActionListener(new SaveDetailedDataListener());
        importButton.addActionListener(new ImportActionListener());
        exportButton.addActionListener(new ExportActionListener());
        youtubeButton.addActionListener(new YoutubeActionListener());
        fbButton.addActionListener(new FacebookActionListener());
        twitterButton.addActionListener(new TwitterActionListener());
        quoraButton.addActionListener(new QuoraActionListener());
        //visualizeButton.addActionListener(new VisualizeActionListener());
        //visualizeButton.addActionListener(new WavefrontObjectLoader());
        userManualButton.addActionListener(new UserManualActionListener());
        feedbackButton.addActionListener(new ProductFeedbackActionListener());
        aboutButton.addActionListener(new AboutActionListener());
        demoButton.addActionListener(new ProductDemoActionListener());
        graphButton.addActionListener(new ViewGraphActionListener("3DNA Domain Analysis"));
    }

    public static void FalseEnableContent(){
        exportButton.setEnabled(false);
        latexButton.setEnabled(false);
		csvButton.setEnabled(false);
        pdfButton.setEnabled(false);
        estimateButton.setEnabled(false);
        zoomInButton.setEnabled(false);
        zoomOutButton.setEnabled(false);
        upButton.setEnabled(false);
        downButton.setEnabled(false);
        leftButton.setEnabled(false);
        rightButton.setEnabled(false);
        visualizeButton.setEnabled(false);
        resetButton.setEnabled(false);
        graphButton.setEnabled(false);
        undoButton.setEnabled(false);
    }

    public static void TrueEnableContent(){
        exportButton.setEnabled(true);
		latexButton.setEnabled(true);
        csvButton.setEnabled(true);
        pdfButton.setEnabled(true);
        estimateButton.setEnabled(true);
        zoomInButton.setEnabled(true);
        zoomOutButton.setEnabled(true);
        upButton.setEnabled(true);
        downButton.setEnabled(true);
        leftButton.setEnabled(true);
        rightButton.setEnabled(true);
        visualizeButton.setEnabled(true);
        resetButton.setEnabled(true);
        undoButton.setEnabled(true);
    }

    public static void enableGraph(){
        graphButton.setEnabled(true);
    }

    //License Accept Code
    public void acceptRejectLicense() {
        try {
            File tempFile = new File("TempFile");
            FileWriter fileWriter = new FileWriter(tempFile);
            fileWriter.write("TempFile");
            fileWriter.close();

            String filePath = tempFile.getAbsolutePath();
            System.out.println(filePath);
            filePath = filePath.substring(0, filePath.indexOf("TempFile"));

            if (osName.toLowerCase().contains("mac")) {
                filePath = filePath.concat("DNA Pen.app/Contents/Resources/DNA Pen/configFile");
                configFile = new File(filePath);
            } else {
                filePath = filePath.concat("configFile");
                configFile = new File(filePath);
            }

            FileReader frConfig = new FileReader(configFile);
            BufferedReader bufferedReader = new BufferedReader(frConfig);

            String configFileData = "";

            while ((configFileData = bufferedReader.readLine()) != null) {
                System.out.println(configFileData);
                if (configFileData.contains("License:1")) {
                    licenseAccepted = true;
                }
            }
            bufferedReader.close();
            tempFile.delete();
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(null, "Exception occurred." + e1,
                    "Error!", JOptionPane.INFORMATION_MESSAGE);
        }

        if (!licenseAccepted) {
            JFrame licenseFrame = new JFrame("License");
            JLabel licenseText = new JLabel("Sample License");
            JScrollPane jScrollPane = new JScrollPane(licenseText);
            JPanel jPanel = new JPanel();

            final JButton acceptButton = new JButton("Accept");
            final JButton rejectButton = new JButton("Reject");

            WindowListener exitListener = new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    int userChoice = JOptionPane.showConfirmDialog(null, "Are you sure ?",
                            "License Acceptance", JOptionPane.YES_NO_OPTION);
                    if (userChoice == 0)
                    {
                        System.exit(0);
                    }
                }
            };

            licenseFrame.addWindowListener(exitListener);

            try {
                File tempFile = new File("TempFile");
                FileWriter fileWriter = new FileWriter(tempFile);
                fileWriter.write("TempFile");
                fileWriter.close();

                String filePath = tempFile.getAbsolutePath();
                System.out.println(filePath);
                filePath = filePath.substring(0, filePath.indexOf("TempFile"));

                if (osName.toLowerCase().contains("mac")) {
                    filePath = filePath.concat("DNA Pen.app/Contents/Resources/DNA Pen/docs/License.txt");
                } else {
                    filePath = filePath.concat("/docs/License.txt");
                }

                FileReader fileReader = new FileReader(new File(filePath));
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String tempString, licenseTextString = "<html><br/>";

                // Write File content in JLabel
                while ((tempString = bufferedReader.readLine()) != null) {
                    licenseTextString = licenseTextString.concat(tempString + "<br/>");
                }
                licenseTextString = licenseTextString.concat("<br/><br/></html>");
                licenseText.setText(licenseTextString);
                fileReader.close();
                tempFile.delete();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace(System.out);
                JOptionPane.showMessageDialog(null, "Exception occurred.",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e2) {
                e2.printStackTrace(System.out);
                JOptionPane.showMessageDialog(null, "Exception occurred.",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
            }

            jPanel.setLayout(new GridLayout(1, 2));
            jPanel.add(acceptButton);
            jPanel.add(rejectButton);

            jScrollPane.getViewport().setBackground(Color.white);

            licenseFrame.getContentPane().add(jScrollPane,BorderLayout.CENTER);
            licenseFrame.getContentPane().add(jPanel,BorderLayout.PAGE_END);

            Dimension screenDimensions = Toolkit.getDefaultToolkit().getScreenSize();

            licenseFrame.setBounds(screenDimensions.width / 4, screenDimensions.height/ 4,
                    screenDimensions.width / 2, screenDimensions.height / 2);
            licenseFrame.setResizable(false);
            licenseFrame.setVisible(true);

            acceptButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                    try {
                        FileWriter fileWriter = new FileWriter(configFile);
                        fileWriter.write("License:1");
                        fileWriter.close();

                        JOptionPane.showMessageDialog(null, "Thank you for accepting the license.",
                                "Success!", JOptionPane.INFORMATION_MESSAGE);
                        acceptButton.setEnabled(false);
                        licenseAccepted = true;
                        File projectDirectory=new File(System.getProperty("user.home")+"/Desktop/3DNA_Workspace");
                        projectDirectory.mkdirs();
                        JOptionPane.showMessageDialog(null,"A new workspace has been created at: "+System.getProperty("user.home")
                        +"Desktop/3DNA_Workspace");
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(null, "Exception occurred.",
                                "Error!", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            rejectButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                    int userChoice = JOptionPane.showConfirmDialog(null, "Are you sure ?",
                            "License Acceptance", JOptionPane.YES_NO_OPTION);
                    if (userChoice == 0)
                    {
                        System.exit(0);
                    }
                }
            });

            while (acceptButton.isEnabled()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace(System.out);
                    JOptionPane.showMessageDialog(null, "Exception occurred.",
                            "Error!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            licenseFrame.dispose();
        }
    }
    @SuppressWarnings("unused")
    public static void main(String []args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception es){
            JOptionPane.showMessageDialog(null, "Exception Occurred with the GUI Toolkit","Exception",JOptionPane.ERROR_MESSAGE);
        }
        mainFrame=new MainFrame();
        ImageIcon img = new ImageIcon("icons/logo_half.png");
        mainFrame.setIconImage(img.getImage());
        mainFrame.acceptRejectLicense();
        if (!licenseAccepted) {
            System.exit(0);
        }
    }

    public static JSplitPane vPanel;
    public static JSplitPane hPanel;
    public static JPanel leftJPanel;
    public static JPanel topRightJPanel;
    public static JPanel bottomRightJPanel;
    public static String osName = System.getProperty("os.name");
    public static File configFile;
    public static MainFrame mainFrame;
    public static String projectName;
    public static String projectPath;
    public static boolean isProjectCreated;
    public static boolean isImported=false;
    public static JButton estimateButton;
    public static JButton exportButton;
    public static JButton pdfButton;
    public static JButton csvButton;
    public static JButton zoomInButton;
    public static JButton zoomOutButton;
    public static JButton upButton;
    public static JButton downButton;
    public static JButton rightButton;
    public static JButton leftButton;
    public static JButton undoButton;
    public static JButton visualizeButton;
    public static JButton resetButton;
    public static JButton latexButton;
    public static JButton graphButton;
    public static boolean[][][]StoreCoordinates=new boolean[20][20][20];
    public static int height=0; //height of the canvas
    public static int width=0;  //width of the canvas
    public static int depth=0;  //depth of the canvas
    public static boolean licenseAccepted = false;
    public static boolean isBoundaryCalled=false;
    public static boolean isPDFBoundarySaved=false;
    public static boolean isCSVBoundarySaved=false;
    public static boolean isPDFSaved=false;
    public static boolean isCSVSaved=false;
    public static int GClowerLimit=6;
    public static int GCupperLimit=10;
}
