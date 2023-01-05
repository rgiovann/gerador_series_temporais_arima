package application;

import java.awt.EventQueue;
import java.awt.Font;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import resources.ResourceLoader;
import view.GSTSplashWindow;
import view.GSTTelaMenu;

/**
 * Classe inicial do programa, instancia a janela principal
 *
 * @param args
 */
public class Program {
    private static int ScreenWIDTH = 0; // variavel estatica largura da tela
    private static int ScreenHEIGHT = 0; // variavel estatica altura da tela

    public Program()
	{

	    // Qual � a resolu��o ?

	    // Toolkit tk = Toolkit.getDefaultToolkit();
	    // Dimension screenSize = tk.getScreenSize();
	    // ScreenWIDTH = screenSize.width;
	    // ScreenHEIGHT = screenSize.height;
	    ScreenWIDTH = 450;
	    ScreenHEIGHT = 450;
	}

    // metodo principal main
    public static void main(String[] args)
	{

	    try
		{
		    SwingUtilities.invokeAndWait(new Runnable()
			{
			    @Override
			    public void run()
				{

				    GSTSplashWindow.splash(ResourceLoader.loadImage("splashscreen.jpg"));
				}
			});
		} catch (InvocationTargetException e1)
		{
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		} catch (InterruptedException e1)
		{
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}

	    EventQueue.invokeLater(new Runnable()
		{
		    @Override
		    public void run()
			{
			    try
				{
				    new Program();
				    // Cria Janela do menu principal
				    GSTTelaMenu frameTelaPrincipal = new GSTTelaMenu();
				    // Close Splash window
				    // GSTSplashWindow.disposeSplash();

				    try
					{

					    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

					} catch (ClassNotFoundException e)
					{
					    e.printStackTrace();
					} catch (InstantiationException e)
					{
					    e.printStackTrace();
					} catch (IllegalAccessException e)
					{
					    e.printStackTrace();
					} catch (UnsupportedLookAndFeelException e)
					{
					    e.printStackTrace();
					}
				    setUIFont(
					    new javax.swing.plaf.FontUIResource("Courier New", Font.TRUETYPE_FONT, 12));
				    SwingUtilities.updateComponentTreeUI(frameTelaPrincipal);
				    frameTelaPrincipal.setVisible(true);
				    frameTelaPrincipal.setResizable(false);
				} catch (Exception e)
				{
				    e.printStackTrace();
				}
			}
		});
	}

    /**
     * Seta a fonte default de todos os componentes Swing
     *
     * ex. setUIFont (new javax.swing.plaf.FontUIResource("Serif",Font.ITALIC,12));
     *
     * @param args
     */

    /**
     * Retorn Screen HEIGHT (altura)
     *
     * @param args
     */
    public static int getTelaAltura()
	{
	    return ScreenHEIGHT;
	}

    /**
     * Retorn Screen WIDTH (largura)
     *
     * @param args
     */
    public static int getTelaLargura()
	{
	    return ScreenWIDTH;
	}

    private static void setUIFont(javax.swing.plaf.FontUIResource f)
	{
	    Enumeration<Object> keys = UIManager.getDefaults().keys();
	    while (keys.hasMoreElements())
		{
		    Object key = keys.nextElement();
		    Object value = UIManager.get(key);
		    if (value instanceof javax.swing.plaf.FontUIResource)
			{
			    UIManager.put(key, f);
			}
		}
	}

}
