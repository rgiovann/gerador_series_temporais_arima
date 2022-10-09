package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

//import view.GSTGeraTabPanel;
import utilities.GSTParamST;
import utilities.GSTLabels;

public class GSTGeraSTPanel extends JPanel implements ActionListener,GSTLabels {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1714517699059743749L;
    // botoes em novo painel layout  GridLayout
  	final JButton buttonGravar   = new JButton(GSTLabels.botaoGravar);
  	final JButton buttonCancelar = new JButton(GSTLabels.botaoCancelar);
  	GSTParamST parGST = null;
  	
	private GSTGeraTabPanel janelaTabST=null;	// devo acessar alguns metodos
	public  GSTGeraSTPanel() {
		
		super(new BorderLayout());

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(1,2,10,0) );
      	painelBotoes.add(buttonGravar); 
      	painelBotoes.add(buttonCancelar);
      	
		//Gera as janelas com os tabs de tipos de ST
		this.setBorder(BorderFactory.createEmptyBorder(15, 15,15, 15));
		janelaTabST = new GSTGeraTabPanel(); 
		add(janelaTabST, BorderLayout.CENTER);
		add(painelBotoes, BorderLayout.SOUTH);
		
      	buttonGravar.addActionListener(this);
      	buttonCancelar.addActionListener(this);
					
	}

	/**
	 * Metodo que chama as janelas relacionadas aos botoes
	 * @param  - Action event (evento relacionado ao botao GRAVA/CANCELA) 
	 * 
	 * @return - NENHUM
	 * 
	 */ 
	@Override
	public void actionPerformed(ActionEvent ae) {
        
    	int iNumeroAbaST = janelaTabST.GetAbaSerieTemporal(); 		
     	janelaTabST.setParametros(iNumeroAbaST); 
		boolean bViolouTot=false;	
		boolean bViolou1=false;	
		boolean bViolou2=false;	
		boolean bViolou3=false;	
		if ( ae.getActionCommand() == botaoGravar )
		{

			String strAviso = "Serie temporal AR(p) não é ESTACIONÁRIA";
			
			if ( GSTParamST.getTipoST() == GSTLabels.AR1 ||
				 GSTParamST.getTipoST() == GSTLabels.MA1  )
			{ bViolouTot = !(Math.abs(GSTParamST.getPAR1()) < 1); 
 			}
			
			if ( GSTParamST.getTipoST() == GSTLabels.AR2 ||
			     GSTParamST.getTipoST() == GSTLabels.MA2 )
			{ bViolou1 = !( (GSTParamST.getPAR2() - GSTParamST.getPAR1()) <1 );
			  bViolou2 = !( (GSTParamST.getPAR2() + GSTParamST.getPAR1()) <1 );
			  bViolou3 = !(Math.abs(GSTParamST.getPAR2()) < 1);
			  bViolouTot = bViolou1 || bViolou2|| bViolou3;
			}	
			
			if (GSTParamST.getTipoST() == GSTLabels.MA1 || 
			    GSTParamST.getTipoST() == GSTLabels.MA2  )
			{ strAviso = "Serie temporal MA(q) não é INVERTÍVEL";}	
			
			else if (GSTParamST.getTipoST() == GSTLabels.ARMA1)
			{   bViolou1 = !(Math.abs(GSTParamST.getPAR1()) < 1);
				if ( bViolou1)
				{strAviso = "Série temporal ARMA(1,1) não é ESTACIONÁRIA";}
				
				bViolou2 = !(Math.abs(GSTParamST.getPAR2()) < 1);
				if ( bViolou2)
				{strAviso = "Série temporal ARMA(1,1) não é INVERTIVEL";}
				
				if (bViolou1&bViolou2)
				{strAviso = "Serie temporal ARMA(1,1) não é INVERTÍVEL e nem ESTACIONÁRIA";}
				bViolouTot = bViolou1 || bViolou2;
			}

			
			if ( bViolouTot )				
			{					
				JOptionPane.showMessageDialog(this,
							strAviso,
						    "Aviso",
						    JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if(GSTParamST.getNoAmostra() <= 0)
			{
				JOptionPane.showMessageDialog(this,
						"Número de amostras nao pode ser menor ou igual a zero.",
					    "Aviso",
					    JOptionPane.WARNING_MESSAGE);
				return;				
			}
			
			if(GSTParamST.getNoAmostra() > GSTLabels.NUMMAXAMOSTRAS)
			{
				strAviso="Número de amostras n�o pode ser maior que " 
			              + GSTLabels.NUMMAXAMOSTRAS
			              + ".";
				JOptionPane.showMessageDialog(this,
						strAviso,
					    "Aviso",
					    JOptionPane.WARNING_MESSAGE);
				return;				
			}
				
			if(GSTParamST.getNoAmostra() < GSTLabels.NUMMINAMOSTRAS)
			{
				strAviso="Número de amostras não pode ser menor que " 
			              + GSTLabels.NUMMINAMOSTRAS
			              + ".";
				JOptionPane.showMessageDialog(this,
						strAviso,
					    "Aviso",
					    JOptionPane.WARNING_MESSAGE);
				return;				
			}
							
			if(GSTParamST.getVarRuido() <= 0)
			{
				JOptionPane.showMessageDialog(this,
						"Variância do ruido  nao pode ser menor ou igual a zero.",
					    "Aviso",
					    JOptionPane.WARNING_MESSAGE);
				return;				
			}			 
 

        	EventQueue.invokeLater(new Runnable() 
        	{  
        		public void run() 
        		{  
        			try 
        			{  
                            	
        				//Create and set up the window.
                        Window parentWindow = SwingUtilities.windowForComponent(buttonGravar);
                        GSTConsoleDialog telaConsoleGST = new GSTConsoleDialog(parentWindow,true);

                        //Display the window.
                        telaConsoleGST.pack();
                        telaConsoleGST.setVisible(true);
                               
                    } 
        			catch (Exception e) 
        			{  
                        e.printStackTrace();  
                            
        			}  
                        
        		}                      
        	});     		             				
		}
		
		else if ( ae.getActionCommand() == botaoCancelar)           		
		
		{
			//this.dispose();
			SwingUtilities.getWindowAncestor(this).dispose();

		}		
	}	
	
	
	
}


