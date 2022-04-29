package com.imgpicker.main.gui;

import javax.swing.JScrollPane;

import com.imgpicker.main.defaults.ResourceGiver;
import com.imgpicker.main.defaults.Size;
import com.imgpicker.main.gui.listener.Listeners;

import java.awt.Dimension;
import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.Point;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.RenderingHints;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;

public class HostPanel extends JScrollPane implements MouseWheelListener, MouseMotionListener {
  private File f = new File(ResourceGiver.retrieveLocal("resource/color_wheel.jpg"));
  private transient BufferedImage icon;
  {
    try {
      icon = ImageIO.read(f);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private JPanel jp;
  private Point current;
  private transient Thread worker;
  private ControlPanel cc;
  private double lastZoomLevel = 1;
  private transient Robot r;

  public HostPanel(ControlPanel c) {
    super();
    this.cc = c;
    try {
      r = new Robot();
    } catch (AWTException e1) {
      e1.printStackTrace();
    }
    setPreferredSize(new Dimension(Size.WIDTH - 200, Size.HEIGHT));
    current = new Point(icon.getWidth() / 2, icon.getHeight() / 2);
    jp = new JPanel() {
      @Override
      public synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        g2.setRenderingHints(rh);
        g2.drawImage(icon, (int) (current.getX() - (icon.getWidth() * lastZoomLevel) / 2),
            (int) (current.getY() - (icon.getHeight() * lastZoomLevel) / 2), (int) (icon.getWidth() * lastZoomLevel),
            (int) (icon.getHeight() * lastZoomLevel), null);

      }
    };
    jp.setPreferredSize(new Dimension(icon.getWidth(), icon.getHeight()));
    jp.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    jp.addMouseWheelListener(this);
    jp.addMouseMotionListener(this);
    getVerticalScrollBar().setBlockIncrement(20);
    getVerticalScrollBar().setUnitIncrement(20);
    getHorizontalScrollBar().setBlockIncrement(20);
    getHorizontalScrollBar().setUnitIncrement(20);
    setViewportView(jp);
    worker = new Thread(() -> {
      while (true) {
        try {
          Thread.sleep(30);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        if (isVisible() && isShowing()) {
          if (MouseInfo.getPointerInfo().getLocation().x > getLocationOnScreen().x
              && MouseInfo.getPointerInfo().getLocation().x < getLocationOnScreen().x + getWidth()
              && MouseInfo.getPointerInfo().getLocation().y > getLocationOnScreen().y
              && MouseInfo.getPointerInfo().getLocation().y < getLocationOnScreen().y + getHeight()) {
            cc.updatePosition(MouseInfo.getPointerInfo().getLocation().x, MouseInfo.getPointerInfo().getLocation().x);
            Point p = MouseInfo.getPointerInfo().getLocation();
            cc.updateColor(getColor(p).getRed(), getColor(p).getGreen(), getColor(p).getBlue());
          }
        }

      }
    });
    worker.start();
  }

  public Color getColor(Point p) {
    return r.getPixelColor(p.x, p.y);
  }

  @Override
  public synchronized void mouseWheelMoved(MouseWheelEvent e) {
    if (e.isControlDown()) {
      if (e.getWheelRotation() < 0) {
        lastZoomLevel *= 1.1;
        jp.repaint();
      } else if (e.getWheelRotation() > 0) {
        lastZoomLevel /= 1.1;
        jp.repaint();
      }
      cc.updateZoom(lastZoomLevel);
      jp.repaint();
    } else {
      getParent().dispatchEvent(e);
    }
  }

  public JButton getNewFileButton() {
    JButton b = new JButton("New File");
    b.setIcon(new ImageIcon(ResourceGiver.retrieveNewIMGIco()));
    b.addActionListener(new Listeners.NewFileListener(this));
    b.setAlignmentX(Component.CENTER_ALIGNMENT);
    return b;
  }

  public File getIcon() {
    return f;
  }

  public void pokeFile(File f) {
    this.f = f;
  }

  public JButton getCollectorButton() {
    JButton b = new JButton("Collector");
    b.setIcon(new ImageIcon(ResourceGiver.retrieveCollectorIco()));
    b.addActionListener(new Listeners.CollectorListener(this));
    b.setAlignmentX(Component.CENTER_ALIGNMENT);
    return b;
  }

  public void setImage(File f) {
    try {
      icon = ImageIO.read(f);
    } catch (IOException e) {
      e.printStackTrace();
    }
    lastZoomLevel = 1;
    current = new Point(0,0);
    jp.setPreferredSize(new Dimension(icon.getWidth(), icon.getHeight()));
    jp.repaint();
  }

  @Override
  public void mouseDragged(MouseEvent arg0) {
    current = arg0.getPoint();
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    current = e.getPoint();
  }
}