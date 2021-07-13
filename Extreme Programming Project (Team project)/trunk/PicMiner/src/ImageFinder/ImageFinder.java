package ImageFinder;

import FilePicker.FilePicker;
import HTMLGenerator.HTMLGenerator;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class ImageFinder {
    private final FilePicker filePicker;
    private final HTMLGenerator htmlGenerator;
    private final File inFile;
    private boolean nextImage = true;
    private static final String FILEPROCESSED = "File Processed";



    public ImageFinder(FilePicker filePicker, HTMLGenerator htmlGenerator, File inFile) {
        this.filePicker = filePicker;
        this.htmlGenerator = htmlGenerator;
        this.inFile = inFile;
        htmlGenerator.nextFile(this, inFile.getName());
        ZipSecureFile.setMinInflateRatio(0.1);
    }

    public void fileSent() {
        filePicker.fileSent();
        callExtractionMethod();
        htmlGenerator.finishFile();
    }

    private void callExtractionMethod() {
        String extension = inFile.getAbsolutePath();
        if (extension.endsWith(".docx")) {
            extractFromDocx();
        } else if (extension.endsWith(".pdf")) {
            extractFromPDF();
        } else if (extension.endsWith(".pptx")) {
            extractFromPPTX();
        }
    }

    private void extractFromPDF() {
        try (final PDDocument document = PDDocument.load(inFile)) {
            PDPageTree list = document.getPages();
            for (PDPage page : list) {
                PDResources pdResources = page.getResources();
                readPDFObjects(pdResources);
            }
        } catch (IOException e) {
            System.err.println("Extract from PDF error: " + e);
        }
        System.out.println(FILEPROCESSED);
    }

    private void readPDFObjects(PDResources pdResources) {
        int count = 0;
        try {
            for (COSName name : pdResources.getXObjectNames()) {
                PDXObject object = pdResources.getXObject(name);
                if (object instanceof PDImageXObject) {
                    if(nextImage){
                        PDImageXObject image = (PDImageXObject) object;
                        if (!isImageValid(image.getImage())) continue;
                        outputPDFImage(image, count);
                        count++;
                    }
                    nextImage = false;
                }
            }
        }catch (Exception e) {
            System.out.println("Failed to process image: " + e);
        }
    }

    private void outputPDFImage(PDImageXObject img, int imgNumberCount) throws IOException {
        String imgName = FilenameUtils.removeExtension(inFile.getName());
        File imgPath = new File(FileSystemView.getFileSystemView()
                .getDefaultDirectory().getPath() +
                "\\PicMiner\\images\\" + imgName + "\\" + imgName + "-image" + imgNumberCount + "." + img.getSuffix());
        ImageIO.write(img.getImage(), img.getSuffix(), imgPath);
        htmlGenerator.addImage(imgPath.getAbsolutePath());
    }

    private void extractFromDocx() {
        try (XWPFDocument docx = new XWPFDocument(new FileInputStream(inFile))) {
            List<XWPFPictureData> pictureList = docx.getAllPackagePictures();
            readDocxObjects(pictureList);
        } catch (Exception e) {
            System.err.println("Extract from docx error: " + e);
        }
        System.out.println(FILEPROCESSED);
    }

    private void readDocxObjects(List<XWPFPictureData> pictureList) {
        try {
            for (XWPFPictureData picture : pictureList) {
                if(nextImage){
                    byte[] pictureBytes = picture.getData();
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(pictureBytes));
                if (!isImageValid(image)) continue;
                outputDocxImage(image, picture);
                }
                nextImage = false;
            }
        } catch (Exception e) {
            System.out.println("Failed to process image: Most likely a non bitmap image(.emf) ");
        }
    }

    private void outputDocxImage(BufferedImage img, XWPFPictureData pictureData) throws IOException {
        String imgName = FilenameUtils.removeExtension(inFile.getName());
        File imgPath = new File(FileSystemView.getFileSystemView()
                .getDefaultDirectory().getPath() +
                "\\PicMiner\\images\\" + imgName + "\\" + imgName + "-" + pictureData.getFileName());
        ImageIO.write(img, pictureData.suggestFileExtension(), imgPath);
        htmlGenerator.addImage(imgPath.getAbsolutePath());
    }

    private void extractFromPPTX() {
        try (XMLSlideShow ppt = new XMLSlideShow(new FileInputStream(inFile))) {
            readPPTXObjects(ppt);
        } catch (Exception e) {
            System.err.println("Extract from pptx error: " + e);
        }
        System.out.println(FILEPROCESSED);
    }

    private void readPPTXObjects(XMLSlideShow ppt) {
        try {
            for (XSLFPictureData picture : ppt.getPictureData()) {
                if(nextImage){
                    byte[] pictureBytes = picture.getData();
                    BufferedImage image = ImageIO.read(new ByteArrayInputStream(pictureBytes));
                    outputPPTXImage(image, picture);
                    if (!isImageValid(image)) continue;
                }
                nextImage = false;
            }
        } catch (Exception e) {
            System.out.println("Failed to process image: " + e);
        }
    }

    private void outputPPTXImage(BufferedImage img, XSLFPictureData pictureData) throws IOException {
        String imgName = FilenameUtils.removeExtension(inFile.getName());
        File imgPath = new File(FileSystemView.getFileSystemView()
                .getDefaultDirectory().getPath() +
                "\\PicMiner\\images\\" + imgName + "\\" + imgName + "-" + pictureData.getFileName());
        ImageIO.write(img, pictureData.suggestFileExtension(), imgPath);

        htmlGenerator.addImage(imgPath.getAbsolutePath());
    }

    private boolean isImageValid(BufferedImage img) {
        if (img.getHeight() < 20 && img.getWidth() < 20) return false;

        Color firstPixelColor = new Color(img.getRGB(0, 0));
        for (int height = 0; height < img.getHeight(); height++) {
            for (int width = 0; width < img.getWidth(); width++) {
                if (!(firstPixelColor.equals(new Color(img.getRGB(width, height))))) return true;
            }
        }
        return false;
    }

    public void fileDone() {
        filePicker.nextFile();
    }

    public void nextImage() {
        nextImage = true;
    }
}