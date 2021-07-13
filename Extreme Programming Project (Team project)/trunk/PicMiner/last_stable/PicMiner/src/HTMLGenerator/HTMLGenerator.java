package HTMLGenerator;

import FilePicker.FilePicker;
import ImageFinder.ImageFinder;
import javax.swing.filechooser.FileSystemView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class HTMLGenerator {
    private FilePicker filePicker;
    private String folderName;
    private ImageFinder imageFinder;
    private String fileName;
    private StringBuilder outputHtml = new StringBuilder();

     public HTMLGenerator(FilePicker filePicker) {
        this.filePicker = filePicker;
    }

    public void start(String folderName) {
        this.folderName = folderName;

        File dir = new File(FileSystemView.getFileSystemView()
                .getDefaultDirectory().getPath() +
                "\\PicMiner\\" + getFolderName());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File imgDir = new File(FileSystemView.getFileSystemView()
                .getDefaultDirectory().getPath() +
                "\\PicMiner\\" + getFolderName() + "\\images");
        if (!imgDir.exists()) {
            imgDir.mkdirs();
        }

        outputHtml.append(
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<meta charset='utf-8'/>\n" +
                        "<title>" + folderName + "</title>\n" +
                        "</head>\n" +
                        "<body>\n"
        );

        filePicker.startSending();
    }

    public void nextFile(ImageFinder imageFinder, String fileName) {
        this.imageFinder = imageFinder;
        this.fileName = fileName;
        addHeading("h1", fileName);
        imageFinder.fileSent();
    }

    public void addImage(String imagePath) {
        outputHtml.append("<img src='" + imagePath + "'><br>\n");
        imageFinder.nextImage();
    }

    public void finishFile() {
        addHeading("h2", "-------------------------");
        imageFinder.fileDone();
    }

    public void endDocument() throws IOException {
        File file = new File(FileSystemView.getFileSystemView()
                .getDefaultDirectory().getPath() +
                "\\PicMiner\\" + getFolderName() + "\\index.html");
        FileWriter writer = new FileWriter(file);
        BufferedWriter outFile = new BufferedWriter(writer);
        outputHtml.append("</body>\n</html>");
        outFile.write(outputHtml.toString());
        outFile.close();
        filePicker.htmlGeneratorDone(file.getAbsolutePath());
    }

    private void addHeading(String headingType, String heading) {
        outputHtml.append("<" + headingType + ">" +
                heading + "</" + headingType + ">\n" );
    }

    private void addParagraph(String paragraph) {
        outputHtml.append("<p>" + paragraph + "</p>\n");
    }

    protected FilePicker getFileLocator() {
        return filePicker;
    }

    protected String getFolderName() {
        return folderName;
    }

    protected String getOutputHtml() {
        return outputHtml.toString();
    }

    protected ImageFinder getImageFinder() {
        return imageFinder;
    }

    protected String getFileName() {
        return fileName;
    }

    protected void setOutputHtml(String newText) {
        outputHtml.delete(0, outputHtml.length());
        outputHtml.append(newText);
    }
}