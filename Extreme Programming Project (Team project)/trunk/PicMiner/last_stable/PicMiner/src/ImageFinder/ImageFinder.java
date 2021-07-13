package ImageFinder;


import FilePicker.FilePicker;
import HTMLGenerator.HTMLGenerator;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class ImageFinder {
    private int count = 0;
    private final FilePicker filePicker;
    private final HTMLGenerator htmlGenerator;
    private final File inFile;
    private boolean nextImage = true;

    public ImageFinder(FilePicker filePicker, HTMLGenerator htmlGenerator, File inFile){
        this.filePicker = filePicker;
        this.htmlGenerator = htmlGenerator;
        this.inFile = inFile;
        htmlGenerator.nextFile(this, inFile.getName());
    }

    public void fileSent(){
        filePicker.fileSent();
        callExtractionMethod();
        htmlGenerator.finishFile();
    }

    private void callExtractionMethod(){
        String extension = inFile.getAbsolutePath();
        if(extension.endsWith(".docx")){
            extractFromDocx();
        }
        if(extension.endsWith(".pdf")){
            extractFromPDF();
        }
        if(extension.endsWith(".pptx")){
            extractFromPPTX();
        }
    }

    public void extractFromPDF() {
        count = 0;
        try (final PDDocument document = PDDocument.load(inFile)) {
            PDPageTree list = document.getPages();
            for (PDPage page : list) {
                PDResources pdResources = page.getResources();
                try {
                    for (COSName name : pdResources.getXObjectNames()) {
                        // Waits until nextImage() method gets called to continue
                        PDXObject object = pdResources.getXObject(name);
                        if (object instanceof PDImageXObject) {
                            PDImageXObject image = (PDImageXObject) object;
                            File imgName = new File(FileSystemView.getFileSystemView()
                                    .getDefaultDirectory().getPath() +
                                    "\\PicMiner\\" + getFolderName() + "\\images\\" + inFile.getName() + "-image" + count + "." + image.getSuffix());
                            ImageIO.write(image.getImage(), image.getSuffix(), imgName);
                            htmlGenerator.addImage(imgName.getAbsolutePath());
                            count++;
                            nextImage = false;
                        }
                    }
                }
                catch(Exception e) {
                    System.out.println("Failed to process image: " + e);
                }
            }
        }
        catch (IOException e) {System.err.println("Extract from PDF error: " + e);}
        System.out.println("File Processed");
    }

    public void extractFromDocx() {
        count = 0;
        try {
            XWPFDocument docx = new XWPFDocument(new FileInputStream(inFile));
            List<XWPFPictureData> pictureList = docx.getAllPictures();
            try {
                for (XWPFPictureData picture : pictureList) {
                    // Waits until nextImage() method gets called to continue
                    byte[] pictureBytes = picture.getData();
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(pictureBytes));
                    File imgName = new File(FileSystemView.getFileSystemView()
                            .getDefaultDirectory().getPath() +
                            "\\PicMiner\\" + getFolderName() + "\\images\\" + inFile.getName() + "-image" + count +
                            "." + picture.suggestFileExtension());
                    ImageIO.write(image, picture.suggestFileExtension(), imgName);
                    htmlGenerator.addImage(imgName.getAbsolutePath());
                    count++;
                    nextImage = false;
                }
            }
            catch(Exception e) {
                System.out.println("Failed to process image: " + e);
            }
        }
        catch (Exception e) {System.err.println("Extract from docx error: " + e);}
        System.out.println("File Processed");
    }

    public void extractFromPPTX() {
        count =0;
        try {
            XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(inFile));
            try{
                for (XSLFPictureData picture : ppt.getPictureData()) {
                    // Waits until nextImage() method gets called to continue
                    byte[] pictureBytes = picture.getData();
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(pictureBytes));
                    File imgName = new File(FileSystemView.getFileSystemView()
                            .getDefaultDirectory().getPath() +
                            "\\PicMiner\\" + getFolderName() + "\\images\\" + inFile.getName() + "-image" + count +
                            "." + picture.suggestFileExtension());
                    ImageIO.write(image, picture.suggestFileExtension(), imgName);
                    htmlGenerator.addImage(imgName.getAbsolutePath());
                    count++;
                    nextImage = false;
                }
            }
            catch(Exception e){
                System.out.println("Failed to process image: " + e);
            }
        }
        catch (Exception e){System.err.println("Extract from pptx error: " + e);}
        System.out.println("File Processed");
    }

    public void fileDone(){
        filePicker.nextFile();
    }

    public void nextImage(){
        nextImage = true;
    }

    public String getFolderName(){
        return inFile.getParentFile().getName();
    }
}