package CheckIndex;

import FilePicker.CheckIndex;
import org.junit.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class CheckIndexTest {
    private CheckIndex checkIndex;
    private final String csvLocation = "test_repository/img_indexing.csv";

    @Before
    public void setUp() throws Exception {
        checkIndex = new CheckIndex();
        checkIndex.setCsv(csvLocation);
        ArrayList<String[]> newIndex = new ArrayList<>();
        String[] entry = {"C:\\ce320\\test123.docx", "1606243365884", "C:\\Documents\\PicMiner\\images\\test123"};
        newIndex.add(entry);
        checkIndex.setIndex(newIndex);
        checkIndex.start();
    }

    @After
    public void tearDown() throws Exception {
        checkIndex.resetCsv();
        checkIndex.resetIndex();

        String resetIndex = "C:\\ce320\\test123.docx, 1606243365884, C:\\Documents\\PicMiner\\images\\test123";
        File file = new File(csvLocation);
        FileWriter writer = new FileWriter(file);
        BufferedWriter outFile = new BufferedWriter(writer);
        outFile.write(resetIndex);
        outFile.close();
    }

    @Test
    public void createIndexCSV() {
        File tempCSVFile = new File(csvLocation);
        boolean fileExists = tempCSVFile.exists();
        assertTrue(fileExists);

        checkIndex.createIndexCSV();
        assertEquals(checkIndex.getCsv(), csvLocation);
        assertNotNull(checkIndex.getCsv());
    }

    @Test
    public void parseIndex() {
        checkIndex.parseIndex();
        assertEquals(checkIndex.getIndex().get(0)[0], "C:\\ce320\\test123.docx");
        assertNotEquals(checkIndex.getIndex().get(0)[0], "incorrect");
        assertEquals(checkIndex.getIndex().get(0)[1], "1606243365884");
        assertNotEquals(checkIndex.getIndex().get(0)[1], "incorrect");
        assertEquals(checkIndex.getIndex().get(0)[2], "C:\\Documents\\PicMiner\\images\\test123");
        assertNotEquals(checkIndex.getIndex().get(0)[2], "incorrect");
    }

    @Test
    public void compareMetadata() {
        assertFalse(checkIndex.compareMetadata("1234"));
    }

    @Test
    public void updateIndex() {
        String previousFilePath = checkIndex.getIndex().get(0)[0];
        String previousFileDate = checkIndex.getIndex().get(0)[1];
        String previousImgOutputFolderPath = checkIndex.getIndex().get(0)[2];

        assertNotNull(previousFilePath);
        assertNotNull(previousFileDate);
        assertNotNull(previousImgOutputFolderPath);

        checkIndex.updateIndex(csvLocation);

        assertEquals(checkIndex.getIndex().get(0)[0], previousFilePath);
        assertNotEquals(checkIndex.getIndex().get(0)[0], "incorrect");
        assertNotNull(checkIndex.getIndex().get(0)[0]);

        assertEquals(checkIndex.getIndex().get(0)[1], previousFileDate);
        assertNotEquals(checkIndex.getIndex().get(0)[1], "incorrect");
        assertNotNull(checkIndex.getIndex().get(0)[1]);

        assertEquals(checkIndex.getIndex().get(0)[2], previousImgOutputFolderPath);
        assertNotEquals(checkIndex.getIndex().get(0)[2], "incorrect");
        assertNotNull(checkIndex.getIndex().get(0)[2]);
    }

    @Test
    public void outputToCSV() throws IOException {
        checkIndex.parseIndex();
        assertNotNull(checkIndex.getIndex());
        assertNotNull(checkIndex.getCsv());
        assertEquals(checkIndex.getCsv(), csvLocation);

        ArrayList<String[]> previousIndex;
        previousIndex = checkIndex.getIndex();
        checkIndex.outputToCSV();
        checkIndex.start();
        assertEquals(checkIndex.getIndex(), previousIndex);
    }
}