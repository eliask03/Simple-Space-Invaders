package ui;

import model.Event;
import model.EventLog;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;

import static com.sun.java.accessibility.util.AWTEventMonitor.addWindowListener;

public class MainGamePanel {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);

        frame.pack();

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gamePanel.startGameThread();

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Iterator<Event> iterator = EventLog.getInstance().iterator();
                while (iterator.hasNext()) {
                    Event event = iterator.next();
                    System.out.println(event.getDescription());
                }
            }
        });
    }
}
