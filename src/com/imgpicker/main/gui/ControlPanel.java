package com.imgpicker.main.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
  private JLabel currentPos, currentColorRGB, currentColorHEX, currentZoomLevel;
  private JPanel currentColorViewport;

  public ControlPanel() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    currentPos = new JLabel("Cursor Position:" + " " + "0" + " " + "0");
    currentPos.setAlignmentX(Component.CENTER_ALIGNMENT);

    currentColorRGB = new JLabel("Color_RGB: " + " " + "0" + " " + "0" + " " + "0");
    currentColorRGB.setAlignmentX(Component.CENTER_ALIGNMENT);

    currentColorHEX = new JLabel("Color_HEX: #000000");
    currentColorHEX.setAlignmentX(Component.CENTER_ALIGNMENT);

    currentColorViewport = new JPanel();
    currentColorViewport.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    currentColorViewport.setOpaque(true);
    currentColorViewport.setBackground(Color.BLACK);
    currentColorViewport.setAlignmentX(Component.CENTER_ALIGNMENT);
    currentColorViewport.setAlignmentY(Component.BOTTOM_ALIGNMENT);
    currentColorViewport.setPreferredSize(new Dimension(100, 100));

    currentZoomLevel = new JLabel("Zoom Level: " + "0");
    currentZoomLevel.setAlignmentX(Component.CENTER_ALIGNMENT);

    add(currentPos);
    add(currentColorRGB);
    add(currentColorHEX);
    add(currentZoomLevel);
  }

  public void addUnit(JComponent j) {
    add(j);
  }

  public void addViewport() {
    add(currentColorViewport);
  }

  private String getHex(int r, int g, int b) {
    String hex = "#";
    hex += Integer.toHexString(r);
    hex += Integer.toHexString(g);
    hex += Integer.toHexString(b);
    return hex;
  }

  public synchronized void updatePosition(int x, int y) {
    currentPos.setText("Cursor Position:" + " " + x + " " + y);
  }

  public synchronized void updateZoom(double zoom) {
    currentZoomLevel.setText("Zoom Level: " + Math.round(zoom * 1000) / 1000.0);
  }

  public synchronized void updateColor(int r, int g, int b) {
    currentColorRGB.setText("Color_RGB: " + " " + r + " " + g + " " + b);
    currentColorHEX.setText("Color_HEX: " + getHex(r, g, b));
    currentColorViewport.setBackground(new Color(r, g, b));
  }
}