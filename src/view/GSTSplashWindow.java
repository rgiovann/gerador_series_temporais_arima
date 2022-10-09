package view;
 
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class GSTSplashWindow extends JFrame {

  private static final long serialVersionUID = 9090438525613758648L;

  private static GSTSplashWindow instance;

  private boolean paintCalled = false;

  private Image image;

  private GSTSplashWindow(Image image) {
    super();
    this.image = image;
    JLabel label = new JLabel();
    label.setIcon(new ImageIcon(image));
    this.add(label);    
    this.setUndecorated(true);
    this.setAlwaysOnTop(true);
    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  public static void splash(URL imageURL) {
    if (imageURL != null) {
      splash(Toolkit.getDefaultToolkit().createImage(imageURL));
    }
  }

  public static void splash(Image image) {
	  
    if (instance == null && image != null) {
      instance = new GSTSplashWindow(image);
      instance.setVisible(true);
          
      if (!EventQueue.isDispatchThread() && Runtime.getRuntime().availableProcessors() == 1) {

        synchronized (instance) {
          while (!instance.paintCalled) {
            try {
              instance.wait();
            } catch (InterruptedException e) {
            }
          }
        }
      }
    }
  }

  @Override
  public void update(Graphics g) {
    paint(g);
  }

  @Override
  public void paint(Graphics g) {
    g.drawImage(image, 0, 0, this);
    if (!paintCalled) {
      paintCalled = true;
      synchronized (this) 
      {
      try { Thread.sleep(1500);  
    	    } 
      catch (Exception e) {}  
         notifyAll();
      }
    }
  }

  // #debug
  public static void disposeSplash() {
    instance.setVisible(false);
    instance.dispose();
  }
  
}