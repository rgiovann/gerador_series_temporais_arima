package view;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import gstjava.GSTGeraSTTabAR1;
import gstjava.GSTGeraTabAR2;
import gstjava.GSTGeraTabARMA1;
import gstjava.GSTGeraTabMA1;
import gstjava.GSTGeraTabMA2;
import utilities.GSTLabels;
import utilities.GSTParamST;

public class GSTGeraTabPanel extends JPanel implements GSTLabels  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7034042695575967511L;
	
    private JTabbedPane tabbedPane=null;
    private GSTGeraSTTabAR1 panel1;
    private GSTGeraTabAR2 panel2;
    private GSTGeraTabMA1 panel3;
    private GSTGeraTabMA2 panel4;
    private GSTGeraTabARMA1 panel5;
	public GSTGeraTabPanel() {		
    super(new GridLayout(1, 1));
        
        tabbedPane = new JTabbedPane();
         
        panel1 = new GSTGeraSTTabAR1();
        tabbedPane.addTab("AR(1)", null, panel1,
                "Gera ST tipo AR(1)");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
         
        panel2 = new GSTGeraTabAR2();
        tabbedPane.addTab("AR(2)", null, panel2,
                "Gera ST tipo AR(2)");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
         
        panel3 = new GSTGeraTabMA1();
        tabbedPane.addTab("MA(1)", null, panel3,
                "Gera ST tipo MA(1)");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_M);
         
        panel4 = new GSTGeraTabMA2();
        tabbedPane.addTab("MA(2)", null, panel4,
                "Gera ST tipo MA(2)");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_A);
        
        panel5 = new GSTGeraTabARMA1();
        tabbedPane.addTab("ARMA(1,1)", null, panel5,
                "Gera ST tipo ARMA(1,1)");
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_R);
         
        //Add the tabbed pane to this panel.
        add(tabbedPane);
         
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
    }		
	 
	public int GetAbaSerieTemporal()
	{
		return tabbedPane.getSelectedIndex();
	}
	
	public void setParametros(int par) 
	{
		GSTParamST.setTipoST(par);
		if (par == GSTLabels.AR1)
			{
			GSTParamST.setPAR1(panel1.getPAR1());
			GSTParamST.setPAR2(0); // zera 2ndo parametro 
			GSTParamST.setValorMedio(panel1.getValorMedio());
			GSTParamST.setVarRuido(panel1.getVarRuido());
			GSTParamST.setNoAmostra(panel1.getNoAmostra());
			}
		else if (par == GSTLabels.AR2)
		{
			GSTParamST.setPAR1(panel2.getPAR1());
			GSTParamST.setPAR2(panel2.getPAR2());		
			GSTParamST.setValorMedio(panel2.getValorMedio());
			GSTParamST.setVarRuido(panel2.getVarRuido());
			GSTParamST.setNoAmostra(panel2.getNoAmostra());
		}		
		else if (par == GSTLabels.MA1)
		{
			GSTParamST.setPAR1(panel3.getPAR1());
			GSTParamST.setPAR2(0); // zera 2ndo parametro 			
			GSTParamST.setValorMedio(panel3.getValorMedio());
			GSTParamST.setVarRuido(panel3.getVarRuido());
			GSTParamST.setNoAmostra(panel3.getNoAmostra());
		}		
		else if (par == GSTLabels.MA2)
		{
			GSTParamST.setPAR1(panel4.getPAR1());
			GSTParamST.setPAR2(panel4.getPAR2());				
			GSTParamST.setValorMedio(panel4.getValorMedio());
			GSTParamST.setVarRuido(panel4.getVarRuido());
			GSTParamST.setNoAmostra(panel4.getNoAmostra());
		}		
		else if (par == GSTLabels.ARMA1)
		{
			GSTParamST.setPAR1(panel5.getPAR1());
			GSTParamST.setPAR2(panel5.getPAR2());				
			GSTParamST.setValorMedio(panel5.getValorMedio());
			GSTParamST.setVarRuido(panel5.getVarRuido());
			GSTParamST.setNoAmostra(panel5.getNoAmostra());
		}
		
	}
	
}
