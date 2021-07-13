package GUI;

import FilePicker.FilePicker;

import javax.swing.*;

public class ProgressBar {
    public static JFrame progressFrame;
    private static JProgressBar proBar;

    public ProgressBar(){
        progressFrame = new JFrame();
        progressFrame.setSize(100, 100);
        progressFrame.setLayout(null);
        progressFrame.setDefaultCloseOperation(3);
        proBar = new JProgressBar(0,100);
        progressFrame.add(proBar);
    }
    public static class Tasks extends Thread{
        public void run(){
            for(int i = 0; i < 100 ; i++){
                proBar.setValue(i);
                try {
                    Thread.sleep(10000000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
