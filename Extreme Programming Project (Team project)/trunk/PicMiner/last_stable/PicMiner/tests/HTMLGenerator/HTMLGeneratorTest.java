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

    @BeforeClass
    public static void classSetUp() {
        fileDummy = new Folder_FileFinderDummy();
        htmlFile = new HTMLGenerator(fileDummy);
        imageDummy = new ImageFinderDummy(htmlFile);
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

    @AfterClass
    public static void classTearDown() {
        htmlFile = null;
        fileDummy = null;
        imageDummy = null;
    }
}