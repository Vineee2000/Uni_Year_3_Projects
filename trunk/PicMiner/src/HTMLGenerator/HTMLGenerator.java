package HTMLGenerator;

import FilePicker.FilePicker;
import ImageFinder.ImageFinder;
import javax.swing.filechooser.FileSystemView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class HTMLGenerator {
    private final FilePicker filePicker;
    private String folderName;
    private ImageFinder imageFinder;
    private String fileName;
    private final StringBuilder outputHtml = new StringBuilder();

    public HTMLGenerator(FilePicker filePicker) {
        this.filePicker = filePicker;
    }

    public void start(String folderName) {
        this.folderName = folderName;

        outputHtml.append(
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>\n" +
                        "<meta charset='utf-8'/>\n" +
                        "<title>" + folderName + "</title>\n" +
                        "<style>\n" +
                        ".header {\n" +
                        "    margin-left: 600px;\n" +
                        "    top: 0;\n" +
                        "    position: relative;\n" +
                        "}\n" +
                        "h1 { color: white; font-size: 90px;}\n" +
                        ".sidebar {\n" +
                        "    height: 100%;\n" +
                        "    position: fixed;\n" +
                        "    left: 0;\n" +
                        "    top: 0;\n" +
                        "    width: 270px;\n" +
                        "    z-index: 1;\n" +
                        "    padding-top: 10px;\n" +
                        "    background-color: grey;\n" +
                        "}\n" +
                        ".sidebar p {\n" +
                        "    margin-left: 30px;\n" +
                        "    font-size: 30px;\n" +
                        "    color: white;\n" +
                        "    font-family: \"Arial\";\n" +
                        "}\n" +
                        ".sidebar a {\n" +
                        "    margin-left: 30px;\n" +
                        "    font-size: 20px;\n" +
                        "    font-family: \"Arial\";\n" +
                        "    text-decoration: none;\n" +
                        "    line-height: 150%;\n" +
                        "}\n" +
                        /*".sidebar a:visited { color: purple; }" +
                        ".sidebar a:active { color: purple; }" +*/
                        ".sidebar a:link { color: blue; }\n" +
                        ".sidebar a:hover { color: aqua; }\n" +
                        ".content {\n" +
                        "    margin-left: 350px;\n" +
                        "    padding-top: 5px;\n" +
                        "}\n" +
                        ".content img { \n" +
                        "   padding: 5px;\n" +
                        "   width: 150px;\n" +
                        "   height: 100px;\n" +
                        "   border: 3px solid #ddd;\n" +
                        "   border-radius: 6px;\n" +
                        "   margin-left: 2px;\n" +
                        "}\n" +
                        ".content img:hover { box-shadow: 0 0 5px 5px rgba(0, 191, 255, 0.5);}\n" +
                        ".content img:hover #fileName { display: block;}\n" +
                        "</style>\n" +
                        "</head>\n" +
                        "<body style = 'background-color:black'>\n" +
                        "<h1 class='header'><font color=#5a9bd6>Pic</font><font color=white>Miner</font></h1>\n" +
                        "<div class='sidebar'>\n" +
                        "<p>Files: </p>\n"
        );
        addFileNameInSideBar(filePicker.getDocumentSet());
        outputHtml.append("<div class='content' id='imgContent'>\n");
        filePicker.startSending();
    }

    private void addFileNameInSideBar(Set<String> documentSet) {
        for (String file: documentSet) {
            Path path = Paths.get(file);
            Path fileName = path.getFileName();
            String newFile = file.replace("'","&#39;");
            if (fileName.toString().length()>20){
                fileName = Paths.get(fileName.toString().substring(0, 20));
            }
            outputHtml.append("<a href='" + "file:///" + newFile + "' id='"+ fileName +"'>" +
                    fileName + "</a>\n<br>\n");
        }
        outputHtml.append("</div>\n");
    }

    public void nextFile(ImageFinder imageFinder, String fileName) {
        this.imageFinder = imageFinder;
        this.fileName = fileName;
        //addHeading("h2", fileName);
        imageFinder.fileSent();
    }

    public void addImage(String imagePath) {
        String newImagePath = imagePath.replace("'","&#39;");
        outputHtml.append(
                "<a href='" + newImagePath + "' id='"+ newImagePath +"'>\n" +
                        "<img src='" + newImagePath + "'>\n" +
                        "</a>\n"
        );
        imageFinder.nextImage();
    }

    public void finishFile() {
        //addHeading("h3", "-------------------------");
        imageFinder.fileDone();
    }

    public void endDocument() throws IOException {
        File file = new File(FileSystemView.getFileSystemView()
                .getDefaultDirectory().getPath() +
                "\\PicMiner\\" + getFolderName() + ".html");
        FileWriter writer = new FileWriter(file);
        BufferedWriter outFile = new BufferedWriter(writer);
        outputHtml.append("</div>\n</body>\n</html>");
        outFile.write(outputHtml.toString());
        outFile.close();
        filePicker.htmlGeneratorDone(file.getAbsolutePath());
    }

    private void addHeading(String headingType, String heading) {
        outputHtml.append("<" + headingType + ">" +
                heading + "</" + headingType + ">\n" );
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