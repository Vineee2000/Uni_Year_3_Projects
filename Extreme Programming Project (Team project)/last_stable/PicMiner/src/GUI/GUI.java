package GUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import FilePicker.FilePicker;

public class GUI extends JFrame implements ActionListener, ListSelectionListener {

    // FilePicker filePicker = new FilePicker(); // need fixed

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

    JList<File> folderList = new JList<>();
    DefaultListModel<File> folderModel = new DefaultListModel<>();
    JList<File> fileList = new JList<>();
    DefaultListModel<File> fileModel = new DefaultListModel<>();

    JScrollPane folderListScroller = new JScrollPane(folderList);
    JScrollPane fileListScroller = new JScrollPane(fileList);

    JLabel folderLabel = new JLabel("Folder List:");
    JLabel fileLabel = new JLabel("File List:");

    JButton browseButton = new JButton("Browse...");
    JButton scanButton = new JButton("Scan");

    // Components - Folder Choose Panel
    JPanel folderChoosePanel = new JPanel();
    JLabel folderChooseLabel = new JLabel();
    JButton openHTMLButton = new JButton("Open HTML");

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
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(Color.black);

        GridBagConstraints gbc = new GridBagConstraints();

        // Setting up Menu Label
        menuLabel.setFont(new Font("Berlin Sans FB", Font.PLAIN, 50));
        gbc.gridx = 3;
        gbc.gridy = 3;
        menuPanel.add(menuLabel, gbc);

        // Setting up Start Button
        startButton.setFont(new Font("Arial", Font.PLAIN, 20));
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

        // Setting up file list and file model
        fileList.setModel(fileModel);

        // Setting up Folder List Scroller
        folderListScroller.setPreferredSize(new Dimension(250,300));
        folderListScroller.setAlignmentX(LEFT_ALIGNMENT);

        // Setting up File List Scroller
        folderListScroller.setPreferredSize(new Dimension(250,300));
        folderListScroller.setAlignmentX(LEFT_ALIGNMENT);

        // Setting up Folder Label
        folderLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        folderLabel.setForeground(Color.white);

        // Setting up File Label
        fileLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        fileLabel.setForeground(Color.white);

        // Setting up Folder Panel
        addComponentsToLayout(folderPanel, folderLabel, folderList, folderListScroller);
        folderPanel.setBorder(BorderFactory.createEmptyBorder(0,5,10,3));

        // Setting up File Panel
        addComponentsToLayout(filePanel, fileLabel, fileList, fileListScroller);
        filePanel.setBorder(BorderFactory.createEmptyBorder(0,3,10,5));

        // Setting up Folder File Panel
        folderFilePanel.setLayout(new BoxLayout(folderFilePanel,BoxLayout.X_AXIS));
        folderFilePanel.setBackground(Color.black);
        folderFilePanel.add(folderPanel);
        folderFilePanel.add(filePanel);
        folderFilePanel.setBorder(BorderFactory.createEmptyBorder(10,5,10,5));

        // Setting up Browse More Button
        browseButton.setFont(new Font("Arial", Font.PLAIN, 15));
        browseButton.setFocusable(false);
        browseButton.addActionListener(this);

        // Setting up Scan Button
        scanButton.setFont(new Font("Arial", Font.PLAIN, 15));
        scanButton.setFocusable(false);
        scanButton.addActionListener(this);

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

        GridBagConstraints gbcFolderChoosePanel = new GridBagConstraints();

        //Setting up Folder Choose Label
        folderChooseLabel.setForeground(Color.white);
        folderChooseLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        gbcFolderChoosePanel.gridx = 3;
        gbcFolderChoosePanel.gridy = 2;
        folderChoosePanel.add(folderChooseLabel,gbcFolderChoosePanel);

        //Setting up open HTML method
        openHTMLButton.setFont(new Font("Arial", Font.PLAIN, 15));
        openHTMLButton.setFocusable(false);
        gbcFolderChoosePanel.gridx = 3;
        gbcFolderChoosePanel.gridy = 5;
        openHTMLButton.addActionListener(this);
        folderChoosePanel.add(openHTMLButton,gbcFolderChoosePanel);

        //Adding panels to panel container
        panelContainer.add(menuPanel, "Menu Panel");
        panelContainer.add(folderFileContainerPanel, "Folder File Container Panel");
        panelContainer.add(folderChoosePanel,"Folder Choose Panel");
        cardLayout.show(panelContainer,"Menu Panel");

        //Adding panel container to the frame
        add(panelContainer);
    }

    private void addComponentsToLayout(JPanel folderPanel, JLabel folderLabel, JList<File> folderList, JScrollPane folderListScroller) {
        folderPanel.setLayout(new BoxLayout(folderPanel, BoxLayout.PAGE_AXIS));
        folderPanel.setBackground(Color.black);
        folderLabel.setLabelFor(folderList);
        folderPanel.add(folderLabel);
        folderPanel.add(Box.createRigidArea(new Dimension(0,5)));
        folderPanel.add(folderListScroller);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            cardLayout.show(panelContainer,"Folder File Container Panel");
        }
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
        if (e.getSource() == scanButton){
            //Scan Folder here
            File folder = folderList.getSelectedValue();
            FilePicker filePicker = new FilePicker(this);
            String folderPath = folder.getPath();
            filePicker.scanFolder(folderPath);
            if (folderChosen()){
                cardLayout.show(panelContainer,"Folder Choose Panel");
                folderChooseLabel.setText("Folder Choose : "+folder.getName());
//                if(noFilesToScan()){
//                    JOptionPane.showMessageDialog(this,"No Files to Scan");
//                    cardLayout.show(panelContainer,"Menu Panel");
//                }
            }
        }
        if (e.getSource() == openHTMLButton){
            //Show HTML doc
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() == folderList){
            File folder = folderList.getSelectedValue();
            displayFolder(folder);
        }
    }

    public void displayFolder(File folder){
        File fileList[] = folder.listFiles();
        for (File file : fileList) {
            fileModel.addElement(file);
        }
    }

    public boolean folderChosen(){
        return true;
    }

    public boolean noFilesToScan(){
        return true;
    }

    public void showHtmlDoc(String docLocation){
        File htmlFile = new File(docLocation);
        try {
            Desktop.getDesktop().open(htmlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}