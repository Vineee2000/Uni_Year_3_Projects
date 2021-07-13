package FilePicker;

import java.io.*;
import java.util.Arrays;
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
    public static int FileCount;
    public static int scannedFileCount = 0;
    private CheckIndex checkIndex = new CheckIndex();

    public FilePicker(GUI gui){
        this.mainGUI = gui;
        checkIndex.start();
    }

    public void scanFolder(String directory){
        File folder = new File(directory);
        try {
            File[] files = folder.listFiles();
            if (files == null) return;
            for (File file : files) {
                String path = file.getAbsolutePath();
                // if its a file lets read it
                readThisFile(file,path);
                if (!documentSet.isEmpty()) {
                    FileCount = documentSet.size();
                    mainGUI.folderChosen();
                }
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
            checkIndex.fileIndexCheck(currentFile.getAbsolutePath());
            imageFinder = new ImageFinder(this, htmlGenerator, currentFile);
            break;
        }
    }

    public void fileSent() {
        System.out.println("FileSent received by FilePicker");
        documentSet.remove(currentFile.getAbsolutePath());
        scannedFileCount += 1;
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
        CheckIndex checkIndex = new CheckIndex();
        try {
            checkIndex.outputToCSV();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainGUI.showHtmlDoc(docLocation);
    }

    public void readThisFile(File file, String path){
        // if its a file lets read it
        if (!file.isDirectory()) {
            // checking the specific file extensions
            if (Arrays.asList("doc","docx","pdf","ppt","pptx").contains(path.substring(path.lastIndexOf(".")+1))) {
                documentSet.add(path);
            }
        }
        else {
            // recursive search for sub directories
            scanFolder(path);
        }
    }

    public void folderProcessed() {
        System.out.print("folderProcessedFileSent received by FilePicker");
    }

    public Set<String> getDocumentSet(){
        return documentSet;
    }
}