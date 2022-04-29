package com.imgpicker.main.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Shape;
import java.awt.RenderingHints;
import java.awt.BorderLayout;
import java.awt.geom.AffineTransform;
import java.util.function.Consumer;
import java.awt.Rectangle;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoaderWindow extends JPanel {
  private double lastRotation = 0.0111111, dualAxisOther = -0.03;
  private final JFrame jf;
  private transient Thread worker;
  public LoaderWindow() {
    jf = new JFrame();
    jf.setTitle("Loading...");
    jf.setPreferredSize(new Dimension(400, 200));
    jf.setResizable(false);
    jf.setLocationByPlatform(true);
    jf.setLayout(new BorderLayout());
    JLabel jl = new JLabel("Loading...     ");
    jl.setForeground(new Color(56 , 56 , 56));
    jl.setAlignmentX(Component.RIGHT_ALIGNMENT);
    jl.setAlignmentY(Component.CENTER_ALIGNMENT);
    jl.setFont(jl.getFont().deriveFont(30f));
    jf.getContentPane().add(this);
    jf.add(jl, BorderLayout.EAST);
    new Thread(() -> {
      while (true) {
        repaint();
        try {
          Thread.sleep(30);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
  }

  @Override
  public synchronized void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    AffineTransform at = new AffineTransform();
    AffineTransform at2 = new AffineTransform();
    Rectangle shape = new Rectangle(-100, 0, 200, 200);
    Rectangle shapeWireframe = new Rectangle(-100, 0, 210, 210);
    at.rotate(lastRotation, shape.getX() + shape.getWidth() / 2, shape.getY() + shape.getHeight() / 2);
    at2.rotate(dualAxisOther, shapeWireframe.getX() + shapeWireframe.getWidth() / 2,
        shapeWireframe.getY() + shapeWireframe.getHeight() / 2);
    dualAxisOther -= 3.141592653589793 / 100;
    lastRotation += 3.141592653589793 / 150;
    g2.setColor(new Color(247, 170, 69));
    Shape s = at.createTransformedShape(shape);
    g2.fill(s);
    g2.setColor(new Color(56, 56, 56));
    Shape s2Shape = at2.createTransformedShape(shapeWireframe);
    g2.setStroke(new BasicStroke(3));
    g2.draw(s2Shape);
  }

  public synchronized void start(Runnable r) {
    jf.pack();
    jf.setVisible(true);
    worker = new Thread(r);
    try {
      worker.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public synchronized void start() {
    jf.pack();
    jf.setVisible(true);
  }

  public synchronized void kill() {
    jf.dispose();
    if(worker != null)
      worker.interrupt();
  }

  public static void main(String... args) {
    LoaderWindow lw = new LoaderWindow();
    lw.start(() -> {
      while(true) {
        System.out.println("Hello World");
      }
    });
  }
}
