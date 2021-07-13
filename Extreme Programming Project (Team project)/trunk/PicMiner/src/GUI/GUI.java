package GUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.SwingWorker;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import javax.swing.Timer;
import FilePicker.FilePicker;

public class GUI extends JFrame implements ActionListener, ListSelectionListener, PropertyChangeListener {
    static final String fontName = "Arial";
    String pathToIndexFile = "";
    // FilePicker filePicker = new FilePicker(); // need fixed

    //Progress Panel components;
    private Task task;
    private Timer timer;
    private int counter = 10;
    public JProgressBar progressBar = new JProgressBar();
    public boolean done;
    JLabel progressText = new JLabel();

    // All Components
    JPanel panelContainer = new JPanel();
    CardLayout cardLayout = new CardLayout();

    // Components - Menu Panel
    JPanel menuPanel = new JPanel();
    JLabel menuLabel = new JLabel("<html><font color=#5a9bd6>Pic</font><font color=white>Miner</font></html>");
    JButton startButton = new JButton("Start");

    // Components - Folder File Container Panel
    JPanel folderFileContainerPanel = new JPanel();
    JPanel folderFilePanel = new JPanel();
    JPanel folderPanel = new JPanel();
    JPanel filePanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JPanel progressPanel = new JPanel();

    JList<File> folderList = new JList<>();
    DefaultListModel<File> folderModel = new DefaultListModel<>();
    JList<File> fileList = new JList<>();
    DefaultListModel<File> fileModel = new DefaultListModel<>();

    JScrollPane folderListScroller = new JScrollPane(folderList);
    JScrollPane fileListScroller = new JScrollPane(fileList);

    JLabel folderLabel = new JLabel("Folder List:");
    JLabel fileLabel = new JLabel("File List:");

    JButton browseButton = new JButton("Browse...");



    public static JButton scanButton = new JButton("Scan");

    // Components - Folder Choose Panel
    JPanel folderChoosePanel = new JPanel();
    JLabel folderChooseLabel = new JLabel();
    JButton openHTMLButton = new JButton("Open HTML");
    JButton backButton = new JButton("Back");


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI());
    }
    // Frame
    public GUI() {
        setSize(600, 600);
        setTitle("PicMiner");
        PanelContainer();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void PanelContainer() {
        // Setting up Panel Container
        panelContainer.setLayout(cardLayout);

        // Menu Panel, setting up the menu panel
        menuPanel.setName("menuPanel");
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(Color.black);

        GridBagConstraints gbc = new GridBagConstraints();

        // Setting up Menu Label
        menuLabel.setName("menuLabel");
        menuLabel.setFont(new Font("Berlin Sans FB", Font.PLAIN, 50));
        gbc.gridx = 3;
        gbc.gridy = 3;
        menuPanel.add(menuLabel, gbc);

        // Setting up Start Button
        startButton.setName("startButton");
        startButton.setFont(new Font(fontName, Font.PLAIN, 20));
        startButton.setFocusable(false);
        // Action Listener for start button
        startButton.addActionListener(this);
        gbc.gridx = 3;
        gbc.gridy = 4;
        menuPanel.add(startButton, gbc);

        // Folder File Container Panel
        // Setting up folder list and folder model
        folderList.setModel(folderModel);
        folderList.addListSelectionListener(this);
        folderList.setName("folderList");

        // Setting up file list and file model
        fileList.setModel(fileModel);

        // Setting up Folder List Scroller
        folderListScroller.setPreferredSize(new Dimension(250,300));
        folderListScroller.setAlignmentX(LEFT_ALIGNMENT);

        // Setting up File List Scroller
        folderListScroller.setPreferredSize(new Dimension(250,300));
        folderListScroller.setAlignmentX(LEFT_ALIGNMENT);

        // Setting up Folder Label
        folderLabel.setName("folderLabel");
        folderLabel.setFont(new Font(fontName, Font.PLAIN, 20));
        folderLabel.setForeground(Color.white);

        // Setting up File Label
        fileLabel.setName("fileLabel");
        fileLabel.setFont(new Font(fontName, Font.PLAIN, 20));
        fileLabel.setForeground(Color.white);

        // Setting up Folder Panel
        addComponentsToLayout(folderPanel, folderLabel, folderList, folderListScroller);
        folderPanel.setBorder(BorderFactory.createEmptyBorder(0,5,10,3));

        // Setting up File Panel
        filePanel.setName("filePanel");
        addComponentsToLayout(filePanel, fileLabel, fileList, fileListScroller);
        filePanel.setBorder(BorderFactory.createEmptyBorder(0,3,10,5));

        // Setting up Folder File Panel
        folderFilePanel.setLayout(new BoxLayout(folderFilePanel,BoxLayout.X_AXIS));
        folderFilePanel.setBackground(Color.black);
        folderFilePanel.add(folderPanel);
        folderFilePanel.add(filePanel);
        folderFilePanel.setBorder(BorderFactory.createEmptyBorder(10,5,10,5));

        // Setting up Browse More Button
        browseButton.setName("browseButton");
        browseButton.setFont(new Font(fontName, Font.PLAIN, 15));
        browseButton.setFocusable(false);
        browseButton.addActionListener(this);

        // Setting up Scan Button
        scanButton.setName("scanButton");
        scanButton.setFont(new Font(fontName, Font.PLAIN, 15));
        scanButton.setFocusable(false);
        scanButton.addActionListener(this);

        //Setting up the JProgress panel
        progressBar.setName("progressBar");
        progressBar.setStringPainted(true);
        progressPanel.setLayout(new BoxLayout(progressPanel, BoxLayout.LINE_AXIS));
        progressPanel.setBounds(0,50,300,200);
        progressPanel.setBackground(Color.red);
        progressPanel.add(progressBar);
        progressText.setFont(new Font("Serif", Font.BOLD, 20));
        progressText.setForeground(Color.white);
        progressPanel.add(progressText);
        buttonPanel.add(progressPanel);

        // Setting up Button Panel
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        buttonPanel.setBackground(Color.black);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(browseButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(scanButton);

        // Setting up Folder File Container Panel
        folderFileContainerPanel.setLayout(new BoxLayout(folderFileContainerPanel, BoxLayout.Y_AXIS));
        folderFileContainerPanel.setBackground(Color.black);
        folderFileContainerPanel.add(folderFilePanel);
        folderFileContainerPanel.add(buttonPanel);
        folderFileContainerPanel.setBorder(BorderFactory.createEmptyBorder(0,10,10,10));

        //Folder Choose Panel
        //Setting up Folder Choose Panel
        folderChoosePanel.setLayout(new GridBagLayout());
        folderChoosePanel.setBackground(Color.black);
        folderChoosePanel.setBounds(0,50,300,200);




        GridBagConstraints gbcFolderChoosePanel = new GridBagConstraints();
        //Setting up Folder Choose Label
        folderChooseLabel.setForeground(Color.white);
        folderChooseLabel.setFont(new Font(fontName, Font.PLAIN, 20));
        gbcFolderChoosePanel.gridx = 3;
        gbcFolderChoosePanel.gridy = 2;
        folderChoosePanel.add(folderChooseLabel,gbcFolderChoosePanel);

        //Setting up open HTML method
        openHTMLButton.setName("openHTMLButton");
        openHTMLButton.setFont(new Font(fontName, Font.PLAIN, 15));
        openHTMLButton.setFocusable(false);
        gbcFolderChoosePanel.gridx = 3;
        gbcFolderChoosePanel.gridy = 5;
        openHTMLButton.addActionListener(this);
        folderChoosePanel.add(openHTMLButton,gbcFolderChoosePanel);


        //Setting up Backbutton method
        gbcFolderChoosePanel.gridx = 3;
        gbcFolderChoosePanel.gridy =  10;
        backButton.setBackground(Color.black);
        backButton.setBorder(new LineBorder(Color.BLACK));
        backButton.setForeground(Color.white);
        backButton.addActionListener(this);
        folderChoosePanel.add(backButton,gbcFolderChoosePanel);

        // Adding Mouse listener event on the button
        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setForeground(Color.GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(Color.black);
                backButton.setBorder(new LineBorder(Color.BLACK));
                backButton.setForeground(Color.white);
            }
        });


        //Adding panels to panel container
        panelContainer.add(menuPanel, "Menu Panel");
        panelContainer.add(folderFileContainerPanel, "Folder File Container Panel");
        panelContainer.add(folderChoosePanel,"Folder Choose Panel");
        cardLayout.show(panelContainer,"Menu Panel");

        //Adding panel container to the frame
        add(panelContainer);
    }




    // Implementing swingWorker to update progress background
    public class Task extends SwingWorker<Void, Void> {
        @Override
        public Void doInBackground(){
            int progress = 0;
            while (progress < 100){
                try{
                    Thread.sleep(500);
                }
                catch (InterruptedException ignore){}
                progress += 10;
                setProgress(progress);
            }
            return null;
        }
        @Override
        // Displaying string Message after progress bar is full
        public void done(){
            done = true;
            Toolkit.getDefaultToolkit().beep();
        }
    }

    // Updating progress bar when the progress is changed
    public void propertyChange(PropertyChangeEvent evt){
        if(!done){
            int progress = task.getProgress();
            progressBar.setValue(progress);
        }
    }

    // Function to add different panel, lists inside the main frame
    private void addComponentsToLayout(JPanel folderPanel, JLabel folderLabel, JList<File> folderList, JScrollPane folderListScroller) {
        folderPanel.setLayout(new BoxLayout(folderPanel, BoxLayout.PAGE_AXIS));
        folderPanel.setBackground(Color.black);
        folderLabel.setLabelFor(folderList);
        folderLabel.setName("folderLabel");
        folderPanel.add(folderLabel);
        folderPanel.add(Box.createRigidArea(new Dimension(0,5)));
        folderPanel.add(folderListScroller);
    }

    // Action Events for buttons on the frame
    public void actionPerformed(ActionEvent e) {
        startButtonClick(e);
        browseButtonClick(e);
        scanButtonClick(e);
        openHTMLButtonClick(e);
        backButtonClick(e);
    }

    // Changing panel when button clicked
    public void startButtonClick(ActionEvent e){
        if (e.getSource() == startButton) {
            cardLayout.show(panelContainer,"Folder File Container Panel");
        }
    }

    // Opening a File chooser to allow selecting a folder/directory
    public void browseButtonClick(ActionEvent e){
        if (e.getSource() == browseButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                folderModel.addElement(file);
            }
            else{
                JOptionPane.showMessageDialog(this,"No folder selected");
            }
        }
    }

    // Begins scanning the folder selected
    public void scanButtonClick (ActionEvent e){
        if (e.getSource() == scanButton){
            //Scan Folder here
            try{
                File folder = folderList.getSelectedValue();
                FilePicker filePicker = new FilePicker(this);
                String folderPath = folder.getPath();
                filePicker.scanFolder(folderPath);
                done = false;
                task = new Task();
                task.addPropertyChangeListener(this);
                task.execute();
                if (folderChosen()){
                    ActionListener action = e1 -> {
                        if(counter == 0){
                            timer.stop();
                            JOptionPane.showMessageDialog(this,"Folder Scan Completed");
                            progressBar.setValue(0);
                            cardLayout.show(panelContainer,"Folder Choose Panel");
                            folderChooseLabel.setText("Folder Chosen : "+folder.getName());

                        }else{
                            counter--;
                        }
                    };
                    int delay = 700;
                    timer = new Timer(delay,action);
                    timer.setInitialDelay(0);
                    timer.start();
                }

            }
            catch (Exception ed){
                JOptionPane.showMessageDialog(this,"Please select a folder from the list");
            }
        }

    }

    // Redirects user to an HTML file generated
    public void openHTMLButtonClick(ActionEvent e){
        if (e.getSource() == openHTMLButton) {
            //Show HTML doc
            File htmlFile = showHtmlDoc(pathToIndexFile);
            try {
                Desktop.getDesktop().open(htmlFile);
            } catch (IOException exp) {
                exp.printStackTrace();
            }
        }
    }

    public void backButtonClick(ActionEvent e){
        if(e.getSource() == backButton){
            cardLayout.show(panelContainer, "Folder File Container Panel");
            counter = 10;
        }
    }

    //  Getting selected folder on the JList
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == folderList){
            File folder = folderList.getSelectedValue();
            displayFolder(folder);
        }
    }

    // Display folder if recognised file is is not displayed
    public void displayFolder(File folder){
        File[] fileList = folder.listFiles();
        for (File file : fileList) {
            if(!fileModel.contains(file)){
                fileModel.addElement(file);
            }
        }
    }

    public boolean folderChosen(){
        return true;
    }

    public boolean noFilesToScan(){
        return true;
    }

    public File showHtmlDoc(String docLocation){
        pathToIndexFile = docLocation;
        File htmlFile = new File(docLocation);
        return htmlFile;
    }
}