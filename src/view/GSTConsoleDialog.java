package view;

import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JDialog;

import utilities.GSTLabels;

public class GSTConsoleDialog extends JDialog implements GSTLabels {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8208899131056172974L;

	
	//public ConsoleGSTFrame(Window parent, ParametrosGST parGST) {
	public GSTConsoleDialog(Window parent, boolean bModal) {
		super(parent);	
 		this.setModal(bModal);
		this.setTitle("Sumário dos Cálculos da Série Temportal");	
		this.setPreferredSize(new Dimension(660,400)); 		
		GSTTelaMenu.SetaPosicaoJanela(this,new Dimension(getPreferredSize()));
		this.setResizable(false);    
		this.getContentPane().add(new GSTConsolePanel());  
 		
 	}

}
