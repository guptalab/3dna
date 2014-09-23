/** Author: Shikhar K Gupta, Foram Joshi
 * Project: 3DNA
 * Mentor: Prof. Manish K Gupta
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URISyntaxException;
import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.TransparencyAttributes;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.vecmath.Color3f;

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
        definePanels(); // calling function definePanels() that declares leftJToolBar, topRightJPanel, bottomRightJPanel

        //configure Panels and Split Panes
        hPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT );
        basePanel.add(hPanel,BorderLayout.CENTER );
        vPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT );
        hPanel.setRightComponent(vPanel);
        vPanel.setTopComponent(topRightJPanel);
        vPanel.setBottomComponent(bottomRightJPanel );
        //add JMenuBar and JToolBar
        createAndShowGUI();
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
    }
    public void definePanels(){

        leftJToolBar = new JToolBar("Canvas Control", JToolBar.VERTICAL); //leftJToolBar supports all the buttons that are used to move around in the canvas
        leftJToolBar.setBackground(Color.DARK_GRAY);
        this.add(leftJToolBar,BorderLayout.WEST);
        leftJToolBar.setVisible(false);

        // Add leftJToolBar Images and Buttons
        ImageIcon zoominIcon = new ImageIcon("icons/zoomin.png");
        ImageIcon zoomoutIcon = new ImageIcon("icons/zoomout.png");
        ImageIcon upArrowIcon = new ImageIcon("icons/arrow-up.png");
        ImageIcon downArrowIcon = new ImageIcon("icons/arrow-down.png");
        ImageIcon leftArrowIcon = new ImageIcon("icons/arrow-left.png");
        ImageIcon rightArrowIcon = new ImageIcon("icons/arrow-right.png");
        ImageIcon resetIcon = new ImageIcon("icons/reset.png");
        ImageIcon undoIcon = new ImageIcon("icons/undo.png");

        zoomInButton = new JButton(zoominIcon);
        zoomInButton.setBackground(Color.DARK_GRAY);
        zoomOutButton = new JButton(zoomoutIcon);
        zoomOutButton.setBackground(Color.DARK_GRAY);
        upButton = new JButton(upArrowIcon);
        upButton.setBackground(Color.DARK_GRAY);
        downButton = new JButton(downArrowIcon);
        downButton.setBackground(Color.DARK_GRAY);
        leftButton = new JButton(leftArrowIcon);
        leftButton.setBackground(Color.DARK_GRAY);
        rightButton = new JButton(rightArrowIcon);
        rightButton.setBackground(Color.DARK_GRAY);
        resetButton = new JButton(resetIcon);
        resetButton.setBackground(Color.DARK_GRAY);
        undoButton = new JButton(undoIcon);
        undoButton.setBackground(Color.DARK_GRAY);

        zoomInButton.addActionListener(new ZoomInActionListener());
        zoomOutButton.addActionListener(new ZoomOutActionListener());
        upButton.addActionListener(new UpButtonActionListener());
        downButton.addActionListener(new DownButtonActionListener());
        leftButton.addActionListener(new LeftButtonActionListener());
        rightButton.addActionListener(new RightButtonActionListener());
        resetButton.addActionListener(new ResetButtonActionListener());
        undoButton.addActionListener(new UndoActionListener());

        leftJToolBar.add(Box.createVerticalStrut(20));
        leftJToolBar.add(zoomInButton);
        leftJToolBar.add(Box.createVerticalStrut(10));
        leftJToolBar.add(zoomOutButton);
        leftJToolBar.add(Box.createVerticalStrut(10));
        leftJToolBar.addSeparator();
        leftJToolBar.add(Box.createVerticalStrut(10));
        leftJToolBar.add(upButton);
        leftJToolBar.add(Box.createVerticalStrut(10));
        leftJToolBar.add(leftButton);
        leftJToolBar.add(Box.createVerticalStrut(10));
        leftJToolBar.add(rightButton);
        leftJToolBar.add(Box.createVerticalStrut(10));
        leftJToolBar.add(downButton);
        leftJToolBar.add(Box.createVerticalStrut(10));
        leftJToolBar.addSeparator();
        leftJToolBar.add(Box.createVerticalStrut(10));
        leftJToolBar.add(resetButton);
        leftJToolBar.add(Box.createVerticalStrut(10));
        leftJToolBar.add(undoButton);
        leftJToolBar.add(Box.createVerticalStrut(10));
        leftJToolBar.addSeparator();

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


        //Advanced panel toolbar
        rightJToolBar = new JToolBar("Advanced Options",JToolBar.VERTICAL);
        rightJToolBar.setBackground(Color.DARK_GRAY);
        this.add(rightJToolBar, BorderLayout.EAST);

        JLabel multipleDeletionLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Delete multiple voxels: </h4></html>");
        ImageIcon deletePlaneIcon = new ImageIcon("icons/plane.png");
        deletePlaneButton = new JButton("<html><style>h4{color:white;}</style><h4> Plane </h4></html>", deletePlaneIcon);
        deletePlaneButton.setBackground(Color.DARK_GRAY);
        ImageIcon deleteRowColoumnIcon = new ImageIcon("icons/plane.png");
        deleteRowColoumnButton = new JButton("<html><style>h4{color:white;}</style><h4> Row/Column </h4></html>", deleteRowColoumnIcon);
        deleteRowColoumnButton.setBackground(Color.DARK_GRAY);
        deleteRowColoumnButton.addActionListener(new DeleteRowActionListener());
        deletePlaneButton.addActionListener(new DeletePlaneActionListener());

        ButtonGroup deleteButtonGroup = new ButtonGroup();
        deleteButtonGroup.add(deletePlaneButton);
        deleteButtonGroup.add(deleteRowColoumnButton);

        JLabel uploadSequencesLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Upload DNA Sequence Files: </h4></html>");

        ImageIcon uploadSequencesIcon = new ImageIcon("icons/csv.png");
        JButton uploadSequencesButton = new JButton("<html><style>h4{color:white;}</style><h4>Choose Files</h4></html>",uploadSequencesIcon);
        uploadSequencesButton.setBackground(Color.DARK_GRAY);
        uploadSequencesButton.addActionListener(new UploadSequenceActionListener());

        JLabel enableBoundary = new JLabel("<html><style>h4{color:white;}</style><h4> Enable boundary bricks in: </h4></html>");
        enableBoundary.setBackground(Color.DARK_GRAY);

        final JCheckBox xMinPlaneBoundaryCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Min-X Plane</h4></html>");
        xMinPlaneBoundaryCheckbox.setBackground(Color.DARK_GRAY);
        final JCheckBox xMaxPlaneBoundaryCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Max-X Plane</h4></html>");
        xMaxPlaneBoundaryCheckbox.setBackground(Color.DARK_GRAY);
        final JCheckBox yMinPlaneBoundaryCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Min-Y Plane</h4></html>");
        yMinPlaneBoundaryCheckbox.setBackground(Color.DARK_GRAY);
        final JCheckBox yMaxPlaneBoundaryCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Max-Y Plane</h4></html>");
        yMaxPlaneBoundaryCheckbox.setBackground(Color.DARK_GRAY);
        JLabel enableProtector = new JLabel("<html><style>h4{color:white;}</style><h4> Enable protector bricks in: </h4></html>");
        enableProtector.setBackground(Color.DARK_GRAY);

        zMinPlaneProtectorCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Min-Z Plane</h4></html>");
        zMinPlaneProtectorCheckbox.setBackground(Color.DARK_GRAY);

        zMaxPlaneProtectorCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Max-Z Plane</h4></html>");
        zMaxPlaneProtectorCheckbox.setBackground(Color.DARK_GRAY);

        xMinPlaneBoundaryCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (xMinPlaneBoundaryCheckbox.isSelected() == true)
                    isMinXBoundaryPlaneEnabled = true;
                else
                    isMinXBoundaryPlaneEnabled = false;
            }
        });
        xMaxPlaneBoundaryCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (xMaxPlaneBoundaryCheckbox.isSelected() == true)
                    isMaxXBoundaryPlaneEnabled = true;
                else
                    isMaxXBoundaryPlaneEnabled = false;
            }
        });

        yMinPlaneBoundaryCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (yMinPlaneBoundaryCheckbox.isSelected() == true)
                    isMinYBoundaryPlaneEnabled = true;
                else
                    isMinYBoundaryPlaneEnabled = false;
            }
        });
        yMaxPlaneBoundaryCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (yMaxPlaneBoundaryCheckbox.isSelected() == true)
                    isMaxYBoundaryPlaneEnabled = true;
                else
                    isMaxYBoundaryPlaneEnabled = false;
            }
        });

        zMaxPlaneProtectorCheckbox.addActionListener(new ZMaxPlaneProtectorActionListener());
        zMinPlaneProtectorCheckbox.addActionListener(new ZMinPlaneProtectorActionListener());

        rightJToolBar.add(multipleDeletionLabel);
        rightJToolBar.add(deletePlaneButton);
        rightJToolBar.add(deleteRowColoumnButton);
        rightJToolBar.add(Box.createVerticalStrut(10));
        rightJToolBar.add(new JToolBar.Separator());
        rightJToolBar.add(Box.createVerticalStrut(10));
        rightJToolBar.add(uploadSequencesLabel);
        rightJToolBar.add(uploadSequencesButton);
        rightJToolBar.add(Box.createVerticalStrut(10));
        rightJToolBar.add(new JToolBar.Separator());
        rightJToolBar.add(Box.createVerticalStrut(10));
        rightJToolBar.add(enableBoundary);
        rightJToolBar.add(xMinPlaneBoundaryCheckbox);
        rightJToolBar.add(xMaxPlaneBoundaryCheckbox);
        rightJToolBar.add(yMinPlaneBoundaryCheckbox);
        rightJToolBar.add(yMaxPlaneBoundaryCheckbox);
        rightJToolBar.add(Box.createVerticalStrut(10));
        rightJToolBar.add(new JToolBar.Separator());
        rightJToolBar.add(Box.createVerticalStrut(10));
        rightJToolBar.add(enableProtector);
        rightJToolBar.add(zMinPlaneProtectorCheckbox);
        rightJToolBar.add(zMaxPlaneProtectorCheckbox);
        rightJToolBar.add(Box.createVerticalStrut(10));
        rightJToolBar.add(new JToolBar.Separator());

        rightJToolBar.setVisible(false);


        //Initialize bottomRightJPanel here for its functionalities
    }
    public void createAndShowGUI(){
        //add ToolBar
        JToolBar topJToolBar=new JToolBar("Quick Links");

        topJToolBar.setBackground(Color.DARK_GRAY);
        this.add(topJToolBar,BorderLayout.NORTH);
        ImageIcon newi = new ImageIcon("icons/new-canvas.png");
        ImageIcon estimate = new ImageIcon("icons/estimator.png");
        ImageIcon importData = new ImageIcon("icons/import.png");
        ImageIcon exportData = new ImageIcon("icons/export.png");
        ImageIcon SavingOptions = new ImageIcon("icons/arrow-down.png");
        ImageIcon viewGraph = new ImageIcon("icons/analysis.png");
        ImageIcon advancedoptionIcon = new ImageIcon("icons/zoomin.png");
        ImageIcon visualize = new ImageIcon("icons/visualize.png");
        ImageIcon youtube = new ImageIcon("icons/youtube.png");
        ImageIcon twitter = new ImageIcon("icons/twitter.png");
        ImageIcon facebook = new ImageIcon("icons/facebook.png");
        ImageIcon quora = new ImageIcon("icons/quora.png");
        ImageIcon about = new ImageIcon("icons/about.png");
        ImageIcon usermanual = new ImageIcon("icons/usermanual.png");
        ImageIcon feedback = new ImageIcon("icons/feedback.png");
        ImageIcon demo = new ImageIcon("icons/demo.png");

        JButton newButton = new JButton(newi);
        newButton.setBackground(Color.DARK_GRAY);

        advancedOptionButton = new JButton(advancedoptionIcon);
        advancedOptionButton.setBackground(Color.DARK_GRAY);

        estimateButton = new JButton(estimate);
        estimateButton.setBackground(Color.DARK_GRAY);

        JButton importButton = new JButton(importData);
        importButton.setBackground(Color.DARK_GRAY);

        exportButton = new JButton(exportData);
        exportButton.setBackground(Color.DARK_GRAY);

        savingOptionsButton = new JButton(SavingOptions);
        savingOptionsButton.setBackground(Color.DARK_GRAY);

        graphButton = new JButton(viewGraph);
        graphButton.setBackground(Color.DARK_GRAY);

        visualizeButton = new JButton(visualize);
        visualizeButton.setBackground(Color.DARK_GRAY);

        JButton youtubeButton = new JButton(youtube);
        youtubeButton.setBackground(Color.DARK_GRAY);

        JButton fbButton = new JButton(facebook);
        fbButton.setBackground(Color.DARK_GRAY);

        JButton twitterButton = new JButton(twitter);
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
        savingOptionsButton.setToolTipText("Save DNA sequences");
        graphButton.setToolTipText("3DNA domain analysis");
        visualizeButton.setToolTipText("Visualize Shape");
        demoButton.setToolTipText("Software Demo");
        aboutButton.setToolTipText("About 3DNA");
        feedbackButton.setToolTipText("3DNA Feedback Page");
        userManualButton.setToolTipText("3DNA User Manual");
        advancedOptionButton.setToolTipText("Toggle Advanced Option Plane");

        //adding different buttons to the toolbar
        topJToolBar.add(newButton);
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.addSeparator();
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.add(importButton);
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.add(exportButton);
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.addSeparator();
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.add(savingOptionsButton);
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.addSeparator();
        topJToolBar.add(estimateButton);
        topJToolBar.addSeparator();
        topJToolBar.add(Box.createHorizontalStrut(80));
        topJToolBar.add(advancedOptionButton);
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.addSeparator();
        topJToolBar.add(graphButton);
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.addSeparator();
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.add(aboutButton);
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.add(userManualButton);
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.add(feedbackButton);
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.add(demoButton);
        topJToolBar.addSeparator();
        topJToolBar.add(Box.createHorizontalStrut(80));
        topJToolBar.addSeparator();
        topJToolBar.add(fbButton);
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.add(youtubeButton);
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.add(quoraButton);
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.add(twitterButton);
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.addSeparator();
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.add(visualizeButton);

        FalseEnableContent();

        estimateButton.addActionListener(new EstimatorActionListener());
        newButton.addActionListener(new CanvasActionListener());
        savingOptionsButton.addActionListener(new SavingOptionsActionListener());
        importButton.addActionListener(new ImportActionListener());
        exportButton.addActionListener(new ExportActionListener());
        youtubeButton.addActionListener(new YoutubeActionListener());
        fbButton.addActionListener(new FacebookActionListener());
        twitterButton.addActionListener(new TwitterActionListener());
        quoraButton.addActionListener(new QuoraActionListener());
        visualizeButton.addActionListener(new VisualizeActionListener());
        userManualButton.addActionListener(new UserManualActionListener());
        feedbackButton.addActionListener(new ProductFeedbackActionListener());
        aboutButton.addActionListener(new AboutActionListener());
        demoButton.addActionListener(new ProductDemoActionListener());
        graphButton.addActionListener(new ViewGraphActionListener("3DNA Domain Analysis"));
        advancedOptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Advanced panel has been called");
                rightJToolBar.setVisible(true);
            }
        });
    }

    public static void FalseEnableContent(){
        exportButton.setEnabled(false);
        savingOptionsButton.setEnabled(false);
        estimateButton.setEnabled(false);
        zoomInButton.setEnabled(false);
        zoomOutButton.setEnabled(false);
        upButton.setEnabled(false);
        downButton.setEnabled(false);
        leftButton.setEnabled(false);
        rightButton.setEnabled(false);
        resetButton.setEnabled(false);
        advancedOptionButton.setEnabled(false);

        graphButton.setEnabled(false);
        undoButton.setEnabled(false);

    }

    public static void TrueEnableContent(){
        exportButton.setEnabled(true);
        savingOptionsButton.setEnabled(true);
        estimateButton.setEnabled(true);
        zoomInButton.setEnabled(true);
        zoomOutButton.setEnabled(true);
        upButton.setEnabled(true);
        downButton.setEnabled(true);
        leftButton.setEnabled(true);
        rightButton.setEnabled(true);
        resetButton.setEnabled(true);
        undoButton.setEnabled(true);
        leftJToolBar.setVisible(true);
        advancedOptionButton.setEnabled(true);
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
    public static Toolkit toolkit=Toolkit.getDefaultToolkit();
    public static int screenWidth = toolkit.getScreenSize().width;
    public static int screenHeight = toolkit.getScreenSize().height;

    public static File configFile;
    public static MainFrame mainFrame;
    public static JSplitPane vPanel;
    public static JSplitPane hPanel;
    public static JToolBar leftJToolBar;
    public static JToolBar rightJToolBar;
    public static JPanel topRightJPanel;
    public static JPanel bottomRightJPanel;
    public static JButton deleteRowColoumnButton;
    public static JButton deletePlaneButton;
    public static String osName = System.getProperty("os.name");
    public static String projectName;
    public static String projectPath;
    public static String importedHalfBrickFilePath;
    public static String importedFullBrickFilePath;
    public static String importedBoundaryBrickFilePath;
    public static JButton estimateButton;
    public static JButton exportButton;
    public static JButton savingOptionsButton;
    public static JButton zoomInButton;
    public static JButton zoomOutButton;
    public static JButton upButton;
    public static JButton downButton;
    public static JButton rightButton;
    public static JButton leftButton;
    public static JButton undoButton;
    public static JButton visualizeButton;
    public static JButton resetButton;
    public static JButton graphButton;
    public static JButton advancedOptionButton;
    public static int height=0; //height of the canvas
    public static int width=0;  //width of the canvas
    public static int depth=0;  //depth of the canvas
    public static int GClowerLimit=6;
    public static int GCupperLimit=10;
    public static boolean[][][]deletedCoordinates=new boolean[20][20][20];
    public static boolean isProjectCreated;
    public static boolean isImported=false;
    public static boolean licenseAccepted = false;
    public static boolean isDNASequencesFileImported = false;
    public static boolean isBoundaryCalled=false;
    public static boolean isPDFBoundarySaved=false;
    public static boolean isCSVBoundarySaved=false;
    public static boolean isPDFSaved=false;
    public static boolean isCSVSaved=false;
    public static boolean isMinZProtectorPlaneEnabled = false;
    public static boolean isMaxZProtectorPlaneEnabled = false;
    public static boolean isMinXBoundaryPlaneEnabled = false;
    public static boolean isMinYBoundaryPlaneEnabled = false;
    public static boolean isMaxXBoundaryPlaneEnabled = false;
    public static boolean isMaxYBoundaryPlaneEnabled = false;
    public static Appearance protectorCubeAppearance;
    public static JCheckBox zMinPlaneProtectorCheckbox;
    public static JCheckBox zMaxPlaneProtectorCheckbox;
}
