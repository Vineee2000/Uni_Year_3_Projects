package GUI;


import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;


public class GUIIndividualTesting {
    static GUI gui = new GUI();

    @BeforeAll
    //function to get desired gui components for testing
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

    @Test
    public void ClickStartButton() throws InterruptedException {
        Assert.assertNotNull(gui);
        gui.cardLayout.show(gui.panelContainer,"menuPanel");
        //A small delay to show the menu panel
        Thread.sleep(2000);
        JButton startButton = (JButton)getChild(gui.menuPanel, "startButton");
        buttonClickEvent(startButton);
        Thread.sleep(5000);
    }


    // Allowing an option to select a folder/directory from JFile chooser
    @Test
    public void ClickBrowseButton(){
        Assert.assertNotNull(gui);
        gui.cardLayout.show(gui.panelContainer,"Folder File Container Panel");//showing card layout we want seen
        JButton browseButton = (JButton)getChild(gui.buttonPanel, "browseButton");
        buttonClickEvent(browseButton);
    }

    // functions which tests the folder selected from the list by the bot
    @Test
    public void FolderListSelection() throws AWTException, InterruptedException {
        Assert.assertNotNull(gui);
        gui.cardLayout.show(gui.panelContainer,"Folder File Container Panel");
        JButton browseButton = (JButton)getChild(gui.buttonPanel, "browseButton");
        buttonClickEvent(browseButton);
        botDesign();
    }


    @Test
    public void ClickScanButton() throws AWTException, InterruptedException {
        Assert.assertNotNull(gui);
        gui.cardLayout.show(gui.panelContainer,"Folder File Container Panel");
        JButton browseButton = (JButton)getChild(gui.buttonPanel, "browseButton");
        buttonClickEvent(browseButton);
        Thread.sleep(2000);
        botDesign();
        JButton scanButton = (JButton)getChild(gui.buttonPanel, "scanButton");
        buttonClickEvent(scanButton);
    }

    // After files scanned the button will redirect to a HTML file
    @Test
    public void ClickOpenHTMLButton() throws AWTException, InterruptedException {
        Assert.assertNotNull(gui);
        gui.cardLayout.show(gui.panelContainer,"Folder File Container Panel");
        JButton browseButton = (JButton)getChild(gui.buttonPanel, "browseButton");
        buttonClickEvent(browseButton);
        //selects the folder
        botDesign();
        JButton scanButton = (JButton)getChild(gui.buttonPanel, "scanButton");
        buttonClickEvent(scanButton);
        JButton openHTMLButton = (JButton)getChild(gui.folderChoosePanel, "openHTMLButton");
        buttonClickEvent(openHTMLButton);
    }

    //instantiating bot and clicking on specified coordinate to select folder from panel
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
    public void buttonClickEvent(JButton button){
        Assert.assertNotNull(button);
        ActionEvent event;
        long when;
        when = System.currentTimeMillis();
        event = new ActionEvent(button, ActionEvent.ACTION_PERFORMED,"Anything", when, 0);
        for(ActionListener listener : button.getActionListeners()){
            listener.actionPerformed(event);
        }
    }

}
