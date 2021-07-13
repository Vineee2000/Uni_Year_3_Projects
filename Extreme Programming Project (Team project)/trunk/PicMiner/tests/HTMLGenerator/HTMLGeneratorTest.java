package HTMLGenerator;
import org.junit.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import static org.junit.Assert.*;

@Ignore
public class HTMLGeneratorTest {

    static HTMLGenerator htmlFile;
    static Folder_FileFinderDummy fileDummy;
    static ImageFinderDummy imageDummy;
    static String folderName;

    @BeforeClass
    public static void classSetUp() {

        fileDummy = new Folder_FileFinderDummy();
        htmlFile = new HTMLGenerator(fileDummy);
        imageDummy = new ImageFinderDummy(htmlFile);
        folderName = htmlFile.getFolderName();
    }

    @After
    public void tearDown() {
        htmlFile.setOutputHtml("");
    }

    @Test
    public void testConstructor() {
        assertNotNull(htmlFile);
        assertNotNull(htmlFile.getFileLocator());
        assertNotNull(htmlFile.getFolderName());
        assertEquals(htmlFile.getFolderName(), "FolderName");
        assertEquals(htmlFile.getFileLocator(), fileDummy);
        File expectedDir = new File(FileSystemView.getFileSystemView()
                .getDefaultDirectory().getPath() +
                "\\PicMiner\\" + htmlFile.getFolderName());
        File expectedImgDir = new File(FileSystemView.getFileSystemView()
                .getDefaultDirectory().getPath() +
                "\\PicMiner\\" + htmlFile.getFolderName() + "\\images");
        assertTrue(expectedDir.exists());
        assertTrue(expectedImgDir.exists());
        assertTrue(fileDummy.startSendingRecieved);
        String expectedString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='utf-8'/>\n" +
                "<title> FolderName </title>\n" +
                "</head>\n" +
                "<body>\n";
        assertEquals(htmlFile.getOutputHtml(), expectedString);
    }

    @Test
    public void testConstructor2() {
        assertNotNull(htmlFile);
        assertNotNull(htmlFile.getFileLocator());
        assertNotNull(htmlFile.getFolderName());
        assertEquals(htmlFile.getFolderName(), "FolderName");
        assertEquals(htmlFile.getFileLocator(), fileDummy);
        File expectedDir = new File(FileSystemView.getFileSystemView()
                .getDefaultDirectory().getPath() +
                "\\PicMiner\\" + htmlFile.getFolderName());
        File expectedImgDir = new File(FileSystemView.getFileSystemView()
                .getDefaultDirectory().getPath() +
                "\\PicMiner\\" + htmlFile.getFolderName() + "\\images");
        assertTrue(expectedDir.exists());
        assertTrue(expectedImgDir.exists());
        assertTrue(fileDummy.startSendingRecieved);
        String expectedString = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset='utf-8'/>\n" +
                "<title>" + folderName + "</title>\n" +
                "<script>\n" +
                "function showRelevantImage(){" +
                "}" +
                "</script>\n" +
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
                "    color: white;\n" +
                "    text-decoration: none;\n" +
                "    line-height: 150%;\n" +
                "}\n" +
                        /*".sidebar a:visited { color: purple; }" +
                        ".sidebar a:active { color: purple; }" +*/
                ".sidebar a:link { color: blue; }" +
                ".sidebar a:hover { color: aqua; }\n" +
                ".content {\n" +
                "    margin-left: 350px;\n" +
                "    padding-top: 5px;\n" +
                "}\n" +
                ".content img { padding: 5px; width: 150px; height: 100px; border: 3px solid #ddd; border-radius: 6px; margin-left: 2px;}\n" +
                ".content img:hover { box-shadow: 0 0 5px 5px rgba(0, 191, 255, 0.5)}\n" +
                "</style>\n" +
                "</head>\n" +
                "<body style = 'background-color:black'>\n" +
                "<h1 class='header'><font color=#5a9bd6>Pic</font><font color=white>Miner</font></h1>\n" +
                "<div class='sidebar'>\n" +
                "<p>Files: </p>\n";
        assertEquals(htmlFile.getOutputHtml(), expectedString);
    }

    @Test
    public void addFileInSideBar() {
        assertNotNull(htmlFile.getOutputHtml());
    }

    @Test
    public void nextFile() {
        htmlFile.nextFile(imageDummy, "FileName");
        assertNotNull(htmlFile.getImageFinder());
        assertEquals(htmlFile.getImageFinder(), imageDummy);
        assertNotNull(htmlFile.getFileName());
        assertEquals(htmlFile.getFileName(), "FileName");
        assertTrue(imageDummy.recievedSendFile);
        String expectedString = "<h1> FileName </h1>\n";
        assertEquals(htmlFile.getOutputHtml(), expectedString);
    }

    @Test
    public void addImage() {
        String image = "test_repository\\test_img\\test.png";
        htmlFile.addImage(image);
        assertTrue(imageDummy.recievedNextImg);
        String expectedString = "<img src = '" + FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +
                "\\PicMiner\\" + htmlFile.getFolderName() + "\\images\\image1.png'>\n";
        assertEquals(htmlFile.getOutputHtml(), expectedString);
    }

    @Test
    public void finishFile() {
        htmlFile.finishFile();
        assertTrue(imageDummy.recievedFileDone);
        String expectedString = "<h2>-------------------------</h2>\n";
        assertEquals(htmlFile.getOutputHtml(), expectedString);
    }

    @Test
    public void endDocument() throws IOException {
        htmlFile.endDocument();
        assertTrue(fileDummy.htmlDocDoneRecieved);
        assertNotNull(fileDummy.docLocation);
        assertEquals(fileDummy.docLocation,
                FileSystemView.getFileSystemView().getDefaultDirectory().getPath() +
                        "\\PicMiner\\" + htmlFile.getFolderName() + "\\index.html");
        String expectedString = "</body>\n" +
                "</html>";
        assertEquals(htmlFile.getOutputHtml(), expectedString);
    }

    @Test
    public void getFileLocation() {
        assertNotNull(fileDummy.docLocation);
    }


//    @Test
//    public Object getFileLocator() throws Exception {
//        Exception exception = new Exception("Exception triggered.");
//        assertNotNull((String) getFileLocator(), exception);
//        return null;
//    }

    @AfterClass
    public static void classTearDown() {
        htmlFile = null;
        fileDummy = null;
        imageDummy = null;
    }

    @AfterClass
    public static void classTearDown2() {
        htmlFile = null;
        fileDummy = null;
        imageDummy = null;
        folderName = null;
    }
}