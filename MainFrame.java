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
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.vecmath.Vector3f;

import static java.awt.BorderLayout.NORTH;
@SuppressWarnings("serial")
// This is the main class of the software that invokes the main GUI.

public class MainFrame extends JFrame {

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
        hPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,false );
        basePanel.add(hPanel,BorderLayout.CENTER );
        vPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT,false );
        hPanel.setRightComponent(vPanel);
        vPanel.setTopComponent(topRightJPanel);
        vPanel.setBottomComponent(bottomRightJPanel);
        vPanel.setDividerLocation(0.7);
        //add JMenuBar and JToolBar
        createAndShowGUI();
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
    }
    public static void  printLog (String text, Color textColor){
        style.addAttribute(StyleConstants.FontFamily, "Lucida Console");
        style.addAttribute(StyleConstants.FontSize, new Integer(14));
        StyleConstants.setForeground(style, textColor);
        try { doc.insertString(doc.getLength(), ">>>" +text + "\n",style);
            log.setCaretPosition(log.getDocument().getLength());}
        catch (BadLocationException e){}
    }

    public void definePanels(){
        bottomRightJPanel = new JPanel();
        bottomRightJPanel.setLayout(new BorderLayout());
        log = new JTextPane();
        log.setEditable(false);
        doc = log.getStyledDocument();
        style = log.addStyle("3DNA Style", null);
        log.setBackground(Color.DARK_GRAY);
        logScrollPane = new JScrollPane(log);
        logScrollPane.setBackground(Color.DARK_GRAY);
        bottomRightJPanel.add(logScrollPane);
        bottomRightJPanel.setVisible(false);

        //leftJToolBar supports all the buttons that are used to move around in the canvas
        leftJToolBar = new JToolBar("Canvas Control", JToolBar.VERTICAL);
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
        ImageIcon rotateClockwise = new ImageIcon("icons/next.png");
        ImageIcon rotateAntiClockwise = new ImageIcon("icons/previous.png");

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
        rotateClockwiseButton = new JButton(rotateClockwise);
        rotateClockwiseButton.setBackground(Color.DARK_GRAY);
        rotateAntiClockwiseButton = new JButton(rotateAntiClockwise);
        rotateAntiClockwiseButton.setBackground(Color.DARK_GRAY);

        zoomInButton.addActionListener(new ZoomInActionListener());
        zoomOutButton.addActionListener(new ZoomOutActionListener());
        upButton.addActionListener(new UpButtonActionListener());
        downButton.addActionListener(new DownButtonActionListener());
        leftButton.addActionListener(new LeftButtonActionListener());
        rightButton.addActionListener(new RightButtonActionListener());
        resetButton.addActionListener(new ResetButtonActionListener());
        undoButton.addActionListener(new UndoActionListener());
        rotateAntiClockwiseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CanvasActionListener.rotateCanvasAntiClockWise();
            }
        });
        rotateClockwiseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CanvasActionListener.rotateCanvasClockWise();
            }
        });


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
        leftJToolBar.add(rotateClockwiseButton);
        leftJToolBar.add(Box.createVerticalStrut(10));
        leftJToolBar.add(rotateAntiClockwiseButton);

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
                "&nbsp;The software is based on <a href=\"http://www.sciencemag.org/content/338/6111/1177.short\">" +
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
        deletePlaneButton = new JButton("<html><style>h4{color:white;}</style><h4>Delete Plane </h4></html>", deletePlaneIcon);
        deletePlaneButton.setBackground(Color.DARK_GRAY);
        ImageIcon deleteRowColoumnIcon = new ImageIcon("icons/plane.png");
        deleteRowColoumnButton = new JButton("<html><style>h4{color:white;}</style><h4>Delete Row/Column </h4></html>", deleteRowColoumnIcon);
        deleteRowColoumnButton.setBackground(Color.DARK_GRAY);
        deleteRowColoumnButton.addActionListener(new DeleteRowActionListener());
        deletePlaneButton.addActionListener(new DeletePlaneActionListener());

        ButtonGroup deleteButtonGroup = new ButtonGroup();
        deleteButtonGroup.add(deletePlaneButton);
        deleteButtonGroup.add(deleteRowColoumnButton);

        JLabel chooseSequencesLabel = new JLabel("<html><style>h4{color:white;}</style><h4> Choose DNA Sequences List: </h4></html>");
        ImageIcon chooseSequencesIcon = new ImageIcon("icons/csv.png");
        JButton chooseSequencesButton = new JButton("<html><style>h4{color:white;}</style><h4>Change List</h4></html>",chooseSequencesIcon);
        chooseSequencesButton.setBackground(Color.DARK_GRAY);
        chooseSequencesButton.addActionListener(new UploadSequenceActionListener());

        JLabel otherFeatures = new JLabel("<html><style>h4{color:white;}</style><h4> Others: </h4></html>");
        otherFeatures.setBackground(Color.DARK_GRAY);
        JButton enableBoundarySequencesButton = new JButton("<html><style>h4{color:white;}</style><h4>Boundary Bricks</h4></html>");
        enableBoundarySequencesButton.setBackground(Color.DARK_GRAY);
        enableBoundarySequencesButton.addActionListener((ActionListener) new EnableBoundarySequencesActionListener());

        JButton enableProtectorSequencesButton = new JButton("<html><style>h4{color:white;}</style><h4>Protector Bricks</h4></html>");
        enableProtectorSequencesButton.setBackground(Color.DARK_GRAY);
        enableProtectorSequencesButton.addActionListener((ActionListener) new EnableProtectorSequencesActionListener());

        JButton enableCrystalButton = new JButton("<html><style>h4{color:white;}</style><h4>Crystal</h4></html>");
        enableCrystalButton.setBackground(Color.DARK_GRAY);
        enableCrystalButton.addActionListener((ActionListener) new EnableCrystalActionListener());

        ButtonGroup otherButtonGroup = new ButtonGroup();
        otherButtonGroup.add(enableBoundarySequencesButton);
        otherButtonGroup.add(enableProtectorSequencesButton);
        otherButtonGroup.add(enableCrystalButton);

        final JCheckBox cavityCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Boundary Bricks around Cavity</h4></html>");
        cavityCheckbox.setBackground(Color.DARK_GRAY);
        final JCheckBox replaceWithTCheckbox = new JCheckBox("<html><style>h4{color:white;}</style><h4>Poly-1T domains around missing voxels</h4></html>");
        replaceWithTCheckbox.setBackground(Color.DARK_GRAY);


        replaceWithTCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (replaceWithTCheckbox.isSelected() == true)
                    isReplaceWithTEnabled = true;
                else
                    isReplaceWithTEnabled = false;
                MainFrame.isCSVSaved = false;
                MainFrame.isPDFSaved = false;
                MainFrame.isCSVBoundarySaved = false;
                MainFrame.isPDFBoundarySaved = false;
            }
        });
        cavityCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cavityCheckbox.isSelected() == true)
                    isCavityEnabled = true;
                else
                    isCavityEnabled = false;
            }
        });
        rightJToolBar.add(multipleDeletionLabel);
        rightJToolBar.add(deletePlaneButton);
        rightJToolBar.add(deleteRowColoumnButton);
        rightJToolBar.add(new JToolBar.Separator());
        rightJToolBar.add(chooseSequencesLabel);
        rightJToolBar.add(chooseSequencesButton);
        rightJToolBar.add(new JToolBar.Separator());
        rightJToolBar.add(otherFeatures);
        rightJToolBar.add(enableBoundarySequencesButton);
        rightJToolBar.add(enableProtectorSequencesButton);
        rightJToolBar.add(enableCrystalButton);
        rightJToolBar.add(new JToolBar.Separator());
        rightJToolBar.add(cavityCheckbox);
        rightJToolBar.add(new JToolBar.Separator());
        rightJToolBar.add(replaceWithTCheckbox);
        rightJToolBar.add(new JToolBar.Separator());

        rightJToolBar.setVisible(false);


        //Initialize bottomRightJPanel here for its functionalities
    }
    public void createAndShowGUI(){
        //add ToolBar
        JToolBar topJToolBar=new JToolBar("Quick Links");

        topJToolBar.setBackground(Color.DARK_GRAY);
        this.add(topJToolBar, NORTH);
        ImageIcon newi = new ImageIcon("icons/new-canvas-icon.png");
        ImageIcon estimate = new ImageIcon("icons/estimator.png");
        ImageIcon importData = new ImageIcon("icons/import.png");
        ImageIcon exportData = new ImageIcon("icons/export.png");
        ImageIcon SavingOptions = new ImageIcon("icons/arrow-down.png");
        ImageIcon viewGraph = new ImageIcon("icons/analysis.png");
        ImageIcon advancedoptionIcon = new ImageIcon("icons/advanced-panel-icon.png");
        ImageIcon visualize = new ImageIcon("icons/visualize.png");
        ImageIcon youtube = new ImageIcon("icons/youtube-icon.png");
        ImageIcon twitter = new ImageIcon("icons/twitter-icon.png");
        ImageIcon facebook = new ImageIcon("icons/facebook-icon.png");
        ImageIcon gitHub = new ImageIcon("icons/gitHub-icon.png");
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

        JButton gitHubButton = new JButton(gitHub);
        gitHubButton.setBackground(Color.DARK_GRAY);

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
        visualizeButton.setToolTipText("Visualize Structure");
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
        topJToolBar.add(Box.createHorizontalStrut(10));
        topJToolBar.add(advancedOptionButton);
//        topJToolBar.add(Box.createHorizontalStrut(10));
//        topJToolBar.addSeparator();
//        topJToolBar.add(graphButton);
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
        topJToolBar.add(gitHubButton);
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
        gitHubButton.addActionListener(new GitHubActionListener());
        visualizeButton.addActionListener(new VisualizationActionListener());
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
        bottomRightJPanel.setEnabled(true);
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
        MainFrame.printLog("Welcome",Color.GREEN);
        MainFrame.printLog("System Langaugae: "+ osLanguage,Color.GREEN);
        if(osLanguage.equals("de")){
            delimiter = ";";
        } else {
            delimiter = ",";
        }if (!licenseAccepted) {
            System.exit(0);
        }
    }
    public static ImageIcon img = new ImageIcon("icons/software_logo.png");
    public static java.awt.Image imag=img.getImage();
    public static Toolkit toolkit=Toolkit.getDefaultToolkit();
    public static int screenWidth = toolkit.getScreenSize().width;
    public static int screenHeight = toolkit.getScreenSize().height;
    public static JTextPane log;
    public static StyledDocument doc;
    public static Style style;
    public static JScrollPane logScrollPane;
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
    public static JButton changeAppearanceButton;
    public static String osName = System.getProperty("os.name");
    public static String osLanguage = System.getProperty("user.language");
    public static String delimiter;
    public static String projectName;
    public static String projectPath;
    public static String importedHalfBrickFilePath = "16ntFile.csv";
    public static String importedFullBrickFilePath = "32ntFile.csv";
    public static String importedBoundaryBrickFilePath = "48ntFile.csv";
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
    public static JButton rotateClockwiseButton;
    public static JButton rotateAntiClockwiseButton;
    public static int height=0; //height of the canvas
    public static int width=0;  //width of the canvas
    public static int depth=0;  //depth of the canvas
    public static int GClowerLimit=6;
    public static int GCupperLimit=10;
    public static boolean[][][]deletedCoordinates=new boolean[20][20][20];
    public static boolean isProjectCreated;
    public static boolean isImported=false;
    public static boolean isRandomSequencesListEnabled = true;
    public static boolean isYonggangSequencesListEnabled = false;
    public static boolean isImportSequencesListEnabled = false;
    public static boolean licenseAccepted = false;
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
    public static boolean isZCrystalEnabled = false;
    public static boolean isXCrystalEnabled = false;
    public static boolean isYCrystalEnabled = false;
    public static boolean isReplaceWithTEnabled = false;
    public static boolean isCavityEnabled = false;
    public static Appearance protectorCubeAppearance;
    public static JCheckBox zMinPlaneProtectorCheckbox;
    public static JCheckBox zMaxPlaneProtectorCheckbox;
    public static boolean isAppearanceTransparent=false;

}
