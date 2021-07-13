package GUI;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;


public class GUIStoryTesting {
    static GUI gui = new GUI();

    //function to get desired gui components for testing
    @BeforeAll
    public static Component getChild(Component parent, String name){
        if(name.equals(parent.getName())){
            return parent;
        }
        if(parent instanceof Container){
            Component[] children = ((Container)parent).getComponents();
            for (Component component : children) {
                Component child = getChild(component, name);
                if (child != null) {
                    return child;
                }
            }
        }
        return null;
    }

    //instantiating bot and clicking on specified coordinate to select folder from panel
    @BeforeEach
    public void botDesign() throws InterruptedException, AWTException {
        Robot bot = new Robot();
        bot.mouseMove(138,77);
        Thread.sleep(500);
        bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(500);
        bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        Thread.sleep(5000);
    }

    //button function which does the onclick event
    @BeforeEach
    public void buttonClickEvent(JButton button) {
        Assert.assertNotNull(button);
        ActionEvent event;
        long when;
        when = System.currentTimeMillis();
        event = new ActionEvent(button, ActionEvent.ACTION_PERFORMED, "Anything", when, 0);
        for (ActionListener listener : button.getActionListeners()) {
            listener.actionPerformed(event);
        }
    }

    @After
    public void pressStartButton() throws InterruptedException {
        Assert.assertNotNull(gui);
        gui.cardLayout.show(gui.panelContainer,"menuPanel");
        //A small delay to show the menu panel
        Thread.sleep(2000);
        JButton startButton = (JButton)getChild(gui.menuPanel, "startButton");
        buttonClickEvent(startButton);
        Thread.sleep(3000);
    }

    // Allowing an option to select a folder/directory from JFile chooser
    @After
    public void thenPressBrowseButton(){
        Assert.assertNotNull(gui);
        gui.cardLayout.show(gui.panelContainer,"Folder File Container Panel");//showing card layout we want seen
        JButton browseButton = (JButton)getChild(gui.buttonPanel, "browseButton");
        buttonClickEvent(browseButton);
    }

    // functions which tests the folder selected from the list by the bot
    @After
    public void AfterSelectedFolderToScan() throws AWTException, InterruptedException {
        Assert.assertNotNull(gui);
        gui.cardLayout.show(gui.panelContainer,"Folder File Container Panel");
        botDesign();
    }


    @After
    public void thenPressScanButton() throws InterruptedException {
        Assert.assertNotNull(gui);
        gui.cardLayout.show(gui.panelContainer,"Folder File Container Panel");
        Thread.sleep(2000);
        JButton scanButton = (JButton)getChild(gui.buttonPanel, "scanButton");
        buttonClickEvent(scanButton);
        Thread.sleep(10000);
    }

    // After files scanned the button will redirect to a HTML file
    @After
    public void openHTMLFileButton () {
        Assert.assertNotNull(gui);
        gui.cardLayout.show(gui.panelContainer,"Folder File Container Panel");
        JButton openHTMLButton = (JButton)getChild(gui.folderChoosePanel, "openHTMLButton");
        buttonClickEvent(openHTMLButton);
    }



    // A function where bot does everything on its own
    @Test
    public void AllComponentsTogether() throws InterruptedException, AWTException {
        pressStartButton();
        thenPressBrowseButton();
        AfterSelectedFolderToScan();
        thenPressScanButton();
        openHTMLFileButton();
    }
}
