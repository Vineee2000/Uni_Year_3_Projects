package FilePicker;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import javax.swing.filechooser.FileSystemView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CheckIndex {
    private static ArrayList<String[]> index = new ArrayList<>();
    private static String csv = FileSystemView.getFileSystemView()
            .getDefaultDirectory().getPath()
            + "/PicMiner/images/img_indexing.csv";

    public void start() {
        createIndexCSV();
        parseIndex();
    }

    public void createIndexCSV() {
        File indexCSV = new File(csv);
        if (!indexCSV.isFile()) {
            try {
                if (indexCSV.createNewFile()) {
                    System.out.println("New img_indexing.csv file created");
                } else {
                    System.out.println("img_indexing.csv file creation failed");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public void parseIndex() {
        try {
            Scanner sc = new Scanner(new File(csv));
            sc.useDelimiter(", ");

            while (sc.hasNextLine()) {
                String[] currentLine = sc.nextLine().split(", ");
                index.add(currentLine);
            }
            sc.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void fileIndexCheck(String file) {
        boolean foundInIndex = false;
        for (String [] currentEntry : index) {
            if (currentEntry[0].equals(file)) {
                foundInIndex = true;
                break;
            }
        }
        if (!foundInIndex) {
            appendToIndex(file);
            System.out.println("NEW FILE: appending to index");
        } else if (!compareMetadata(file)) {
            updateIndex(file);
            System.out.println("FILE MODIFIED: updating index");
        } else if (compareMetadata(file)) {
            System.out.println("FILE NOT MODIFIED: skipped");
        }
    }

    public boolean compareMetadata(String file) {
        String csvFileDate = "";

        for (String [] currentEntry : index) {
            if (currentEntry[0].equals(file)) {
                csvFileDate = currentEntry[1];
                break;
            }
        }

        if (Long.toString(new File(file).lastModified()).equals(csvFileDate)) {
            return true;
        }
        return false;
    }

    public void appendToIndex(String file) {
        File ourFile = new File(file);
        String fileName = ourFile.getName();
        File img_folder_dir = new File(FileSystemView.getFileSystemView()
                .getDefaultDirectory().getPath()
                + "/PicMiner/images/" + FilenameUtils.removeExtension(fileName)); // 2 folders same name issue
        img_folder_dir.mkdirs();

        String[] entry = {file, String.valueOf(ourFile.lastModified()), img_folder_dir.toString()};
        index.add(entry);
    }

    public void updateIndex(String file) {
        for (String [] currentEntry : index) {
            if (currentEntry[0].equals(file)) {

                File dir = new File(currentEntry[2]);
                try {
                    FileUtils.deleteDirectory(dir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dir.mkdirs();

                currentEntry[1] = String.valueOf(new File(file).lastModified());
                break;
            }
        }
    }

    public void outputToCSV() throws IOException {
        File file = new File(csv);
        FileWriter writer = new FileWriter(file);
        BufferedWriter outFile = new BufferedWriter(writer);
        StringBuilder sb = new StringBuilder();
        for (String[] entry : index) {
            for (String str : entry) {
                sb.append(str);
                if(!str.equals(entry[2])) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        outFile.write(String.valueOf(sb));
        outFile.close();
    }

    public String getCsv() {
        return csv;
    }

    public ArrayList<String[]> getIndex() {
        return index;
    }

    public void setCsv(String path) {
        csv = path;
    }

    public void setIndex(ArrayList<String[]> newIndex) {
        index = newIndex;
    }

    public void resetCsv() {
        csv = "";
    }

    public void resetIndex() {
        index = null;
    }
}