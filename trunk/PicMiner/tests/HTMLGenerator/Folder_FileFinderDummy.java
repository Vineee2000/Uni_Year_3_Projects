package HTMLGenerator;

import FilePicker.FilePicker;
import GUI.GUI;

public class Folder_FileFinderDummy extends FilePicker {
    protected boolean startSendingRecieved = false;
    protected boolean htmlDocDoneRecieved = false;
    protected String docLocation = null;

    public Folder_FileFinderDummy() {
        super(new GUI());
    }

    public void startSending() {
        startSendingRecieved = true;
    }

    void htmlDocDone(String sentDocLocation) {
        htmlDocDoneRecieved = true;
        docLocation = sentDocLocation;
    }
}
