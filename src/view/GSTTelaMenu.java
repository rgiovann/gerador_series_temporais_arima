package view;
 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import application.Program;
import resources.ResourceLoader;
import utilities.GSTLabels;
  
/**
 * Classe que constroi a janela de menu do programa e tamb�m estancia 
 * as classes relacionadas com cada intem de menu
 * @param  - NENHUM 
 * @return - NENHUM
 */
public class GSTTelaMenu extends JFrame implements ActionListener,ItemListener,GSTLabels {
	/**
	 * Menu Principal do programa
	 */
	private static final long serialVersionUID = -5843691501282232343L;
	
    JMenuBar menuBar=null;
    JMenu menu=null;
    JMenuItem menuItem=null;

    
    public GSTTelaMenu() throws IOException {


        // Create the menu bar
        menuBar = new JMenuBar();
     
        // Setup the frame accordingly to video resolution
        // This is assuming you are extending the JFrame //class
        this.setSize(Program.getTelaLargura()-00, Program.getTelaAltura()-00);
        this.setLocationRelativeTo(null);
        
        // *** NOTE ***
        //Since Java SE 9, invoking getResourceXXX on a class in a named module will only locate the resource in that module, 
        // it will not search the class path as it did in previous release. 
        //
        // Create icon image (fundo do frame)
        // debug
        //ImageIcon imagemFundoRNA = new ImageIcon(this.getClass().getResource("correlograma.jpg"));
        
        
        //ImageIcon imagemFundoRNA = new ImageIcon(this.getClass().getResource("/image/correlograma.jpg")); 
        ImageIcon imagemFundoRNA = new ImageIcon(ResourceLoader.loadImage("correlograma.jpg"));
        

        // Create icon image (�cone do frame)
        //Toolkit kit = Toolkit.getDefaultToolkit();
        //Image imagemFrame = kit.getImage("image/gstjava.gif");
        
        // Torna a imagem escal�vel de acordo com a resolu��o de tela
        imagemFundoRNA.setImage(imagemFundoRNA.getImage().getScaledInstance(Program.getTelaLargura()-00, Program.getTelaAltura()-00, 100));
        
        // Declare panels and content panels
        JPanel contentPane = new JPanel(new GridLayout(1,1));
        JLabel labelRNA = new JLabel(imagemFundoRNA);
        //Constroe o menu
        
        menu = new JMenu("Série Temporal");
        menu.setMnemonic(KeyEvent.VK_T);
        menu.setToolTipText("Gera Série Temporal");
        
        menuBar.add(menu);
        
        //a group of JMenuItems
        menuItem = new JMenuItem(GSTLabels.menuItemGeraST); 
        menuItem.setMnemonic(KeyEvent.VK_G); //used constructor instead
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.ALT_MASK));
        
        //...for each JMenuItem instance:
        menuItem.addActionListener(this);      
        
        menu.add(menuItem);         
        menu.addSeparator();

        menuItem = new JMenuItem(GSTLabels.menuItemSair); 
        menuItem.setMnemonic(KeyEvent.VK_S); //used constructor instead
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        
        //...for each JMenuItem instance:
        menuItem.addActionListener(this);  
        
        menu.add(menuItem);
    
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menu.setToolTipText("Sobre este programa");
        
        menuItem = new JMenuItem(GSTLabels.menuItemHelp); 
        menuItem.setMnemonic(KeyEvent.VK_C); //used constructor instead
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        menu.add(menuItem); 
        
        //...for each JMenuItem instance:
        menuItem.addActionListener(this);  
        
        //menuItem = new JMenuItem(GSTLabels.menuItemSobre,KeyEvent.VK_S);
        //menuItem.setMnemonic(KeyEvent.VK_B); //used constructor instead
        // menu.add(menuItem); 
        
        //...for each JMenuItem instance:
        //menuItem.addActionListener(this);          

        menuBar.add(menu); 
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setIconImage(imagemFrame);
 
        //Create and set up the content pane.
        this.setJMenuBar(menuBar);
        contentPane.setBackground(Color.BLACK);
        contentPane.add(labelRNA);     
        this.add(contentPane);
    
        //Display the window.
        GSTSplashWindow.disposeSplash();
        this.setTitle(GSTLabels.janelaPrincipal);
        this.setResizable(true);
        //this.setLocationRelativeTo(null);
        
    	}
    /**
     *  metodo que trata as chamadas de submenu
     * @param  - evento que indica qual submenu foi ativado 
     * 
     */ 
        public void actionPerformed(ActionEvent ae) {
        //...Get information from the action event...
        //...Display it in the text area...
        	
            if ( ae.getActionCommand() == GSTLabels.menuItemSair )
            		{ System.exit(0); }
            
            else if ( ae.getActionCommand() == GSTLabels.menuItemHelp )           		
            		{
            	     // inicializa janela About
            		 	//TelaAbout panelJanelaAbout = new TelaAbout();
            		 	//panelJanelaAbout.setVisible(true);
    			EventQueue.invokeLater(new Runnable() {  
                    public void run() {  
                        try {  
                        	//
                            //Create and set up the window.
                            Window parentWindow = SwingUtilities.windowForComponent(menuItem);
                            GSTHelpDialog frame = new GSTHelpDialog(parentWindow, true);

                            //Display the window.
                            frame.pack();
                            frame.setVisible(true);       
                        
                        } catch (Exception e) {  
                            e.printStackTrace();  
                        }  
                    }  
                });
    			}
            
            else if ( ae.getActionCommand() == GSTLabels.menuItemGeraST )
            		{             			
            			EventQueue.invokeLater(new Runnable() {  
                            public void run() {  
                                try {  
                                	
                                   //Create and set up the window.
                                   Window parentWindow = SwingUtilities.windowForComponent(menuItem);
                                   GSTGeraSTDialog frame = new GSTGeraSTDialog(parentWindow, true);

                                   //Display the window.
                                   frame.pack();
                                   frame.setVisible(true);
                                   
                                } catch (Exception e) {  
                                    e.printStackTrace();  
                                }  
                            }  
                        });            			
            			
            		}
                 
        }

        public void itemStateChanged(ItemEvent e) {
         //...Get information from the item event...
        //...Display it in the text area...
        }   
        
    	public static void SetaPosicaoJanela(JDialog frameJanela,Dimension windowSize ){
    		
    		Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
            int wdwLeft = screenSize.width / 2 - windowSize.width / 2 - 0;
            int wdwTop =  screenSize.height / 2 - windowSize.height / 2 - 0;  
            frameJanela.setLocation(wdwLeft, wdwTop);
    	} 
    	
    	public static void SetaPosicaoJanela(JFrame frameJanela,Dimension windowSize ){
    		
    		Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
            int wdwLeft = screenSize.width / 2 - windowSize.width / 2 - 0;
            int wdwTop =  screenSize.height / 2 - windowSize.height / 2 - 0;  
            frameJanela.setLocation(wdwLeft, wdwTop);
    	}   
    	

}