package view;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JDialog;

public class GSTGeraSTDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6584881320030975901L;

	public GSTGeraSTDialog(Window parent, boolean bModal) {
		super(parent);	
		this.setModal(bModal);
		this.setTitle("Gera Série Temporal");	
		this.setPreferredSize(new Dimension(350,300)); 		
		GSTTelaMenu.SetaPosicaoJanela(this,new Dimension(getPreferredSize()));
		this.setResizable(false);
		
        //Add contents to the window.
        this.add(new GSTGeraSTPanel());
         
		
	}
	
}
