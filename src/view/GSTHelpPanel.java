package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import utilities.GSTLabels;

public class GSTHelpPanel extends JPanel  implements ActionListener,GSTLabels 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6481389176764172269L;
	/**
	 * 
	 */
	final JButton buttonSair   = new JButton(GSTLabels.botaoCancelar); 
	
	public GSTHelpPanel()
	{
	super(new BorderLayout());
	
    ImageIcon imagemFundoRNA = new ImageIcon(this.getClass().getResource("image/help.jpg"));
    //imagemFundoRNA.setImage(imagemFundoRNA.getImage().getScaledInstance(GSTJava.getTelaLargura()-50, GSTJava.getTelaAltura()-50, 100));
    JLabel labelRNA = new JLabel(imagemFundoRNA);
    this.add(labelRNA,BorderLayout.CENTER);     

	//this.add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));//, BorderLayout.CENTER);
    //System.setOut(new PrintStream(taOutputStream));	

	JPanel painelBotoes = new JPanel();
    painelBotoes.setLayout(new BorderLayout() );
    painelBotoes.add(buttonSair,BorderLayout.CENTER); 
    this.add(painelBotoes,BorderLayout.SOUTH);
    buttonSair.addActionListener(this);
  
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		// TODO Auto-generated method stub
		
		
		if ( ae.getActionCommand() == botaoCancelar)           		
			
		{
			//this.dispose();
			SwingUtilities.getWindowAncestor(this).dispose();

		}	
		
	}
	


}
