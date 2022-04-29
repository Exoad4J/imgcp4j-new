package com.imgpicker.main;

import java.awt.Dimension;

import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.exoad.main.UIFactory;
import com.imgpicker.main.defaults.Size;
import com.imgpicker.main.gui.ControlPanel;
import com.imgpicker.main.gui.HostPanel;

public class Main {
  public static void main(String... args) {
    try {
      UIManager.setLookAndFeel(
          UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    ControlPanel c = new ControlPanel();
    HostPanel h = new HostPanel(c);
    c.addUnit(h.getNewFileButton());
    c.addUnit(h.getCollectorButton());
    c.addViewport();
    JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, h, c);
    jsp.setDividerLocation(Size.WIDTH - 200);
    UIFactory ui = new UIFactory(false, null, jsp);
    ui.setPreferredSize(new Dimension(Size.WIDTH, Size.HEIGHT));
    ui.setLocationByPlatform(true);
    ui.setTitle("~ IMGCP4J");
    ui.setAlwaysOnTop(false);
    ui.run();
  }
}
