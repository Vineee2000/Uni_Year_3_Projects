package HTMLGenerator;

import FilePicker.FilePicker;
import GUI.GUI;
import ImageFinder.ImageFinder;

import java.io.File;

public class ImageFinderDummy extends ImageFinder {
    ImageFinderDummy(HTMLGenerator htmlGenerator) {
        super(new FilePicker(new GUI()), htmlGenerator, new File("test"));
    }
    protected boolean recievedSendFile = false;
    protected boolean recievedNextImg = false;
    protected boolean recievedFileDone = false;

    void sendFile() {
        recievedSendFile = true;
    }

    public void nextImage() {
        recievedNextImg = true;
    }

    public void fileDone() {
        recievedFileDone = true;
    }
}
