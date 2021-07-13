package ImageFinder;

import static org.mockito.Mockito.*;
import FilePicker.FilePicker;
import HTMLGenerator.HTMLGenerator;
import org.junit.Test;
import java.io.File;

public class ImageFinderTest {
    ImageFinder imageFinder;
    FilePicker filePicker;
    HTMLGenerator htmlGenerator;
    File inFile;

    public ImageFinderTest() {
        filePicker = mock(FilePicker.class);
        htmlGenerator = mock(HTMLGenerator.class);
        inFile = new File(System.getProperty("user.dir") + "\\test_repository\\test_doc\\ce320_word_test.docx");
        imageFinder = new ImageFinder(filePicker, htmlGenerator, inFile);
    }

    @Test
    public void fileSent() {
        imageFinder.fileSent();
        verify(filePicker).fileSent();
    }

    @Test
    public void fileDone() {
        filePicker.nextFile();
        verify(filePicker).nextFile();
    }

    @Test
    public void nextImage(){
        int count = 0;
        while (count < 1){
            verify(htmlGenerator).addImage(inFile.getPath());
            count++;
        }
        verify(htmlGenerator).finishFile();
        verify(filePicker).fileSent();
    }
}