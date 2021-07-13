package FilePicker;


import java.io.*;
import java.util.HashSet;
import java.util.Set;
import GUI.GUI;
import HTMLGenerator.HTMLGenerator;
import ImageFinder.ImageFinder;


public class FilePicker {
    protected Set<String> documentSet = new HashSet<>();
    protected ImageFinder imageFinder;
    protected HTMLGenerator htmlGenerator;
    protected boolean fileSentBool;
    protected GUI mainGUI;
    private File currentFile;



    public FilePicker(GUI gui){
        this.mainGUI = gui;
    }

    public void scanFolder(String directory){
        File folder = new File(directory);
        try {
            File[] files = folder.listFiles();
            if (files == null) return;
            for (File file : files) {
                String path = file.getAbsolutePath();
                // if its a file lets read it
                if (!file.isDirectory()) {
                    // checking the specific file extensions
                    if (path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".pdf") || path.endsWith(".ppt") || path.endsWith(".pptx")) {
                        documentSet.add(path);
                    }
                }
                else {
                    // recursive search for sub directories
                    scanFolder(path);

                }
                if (documentSet.isEmpty()) {
                    throw new IOException("No Valid Files");
                }
                mainGUI.folderChosen();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            mainGUI.noFilesToScan(); //************************************************************************
            System.exit(1); //adding kill

        }

        String folderName = folder.getName();
        htmlGenerator = new HTMLGenerator (this);
        htmlGenerator.start(folderName);
    }

    public void startSending() {

        for (String temp : documentSet) {
            currentFile = new File(temp);
            imageFinder = new ImageFinder(this, htmlGenerator, currentFile);
            break;
        }



    }

    public void fileSent() {
        System.out.println("FileSent received by FilePicker");
        documentSet.remove(currentFile.getAbsolutePath());
    }

    public void nextFile() {
        // once set is empty
        if (documentSet.isEmpty()) {
            try {
                htmlGenerator.endDocument();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            startSending();
        }
    }

    public void htmlGeneratorDone(String docLocation) {
        mainGUI.showHtmlDoc(docLocation);
    }

    public void folderProcessed() {
        System.out.print("folderProcessedFileSent received by FilePicker");
    }

}