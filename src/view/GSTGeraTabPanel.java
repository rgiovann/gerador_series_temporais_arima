package view;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

//import view.GSTGeraSTTabAR1;
//import view.GSTGeraTabAR2;
//import view.GSTGeraTabARMA1;
//import view.GSTGeraTabMA1;
//import view.GSTGeraTabMA2;
import utilities.GSTLabels;
import utilities.GSTParamST;

public class GSTGeraTabPanel extends JPanel implements GSTLabels {

    /**
     * 
     */
    private static final long serialVersionUID = 7034042695575967511L;

    private JTabbedPane tabbedPane = null;
    private GSTGeraSTTabAR1 panel1;
    private GSTGeraTabAR2 panel2;
    private GSTGeraTabMA1 panel3;
    private GSTGeraTabMA2 panel4;
    private GSTGeraTabARMA1 panel5;

    public GSTGeraTabPanel()
	{
	    super(new GridLayout(1, 1));

	    this.tabbedPane = new JTabbedPane();

	    this.panel1 = new GSTGeraSTTabAR1();
	    this.tabbedPane.addTab("AR(1)", null, this.panel1, "Generate AR(1) time series");
	    this.tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

	    this.panel2 = new GSTGeraTabAR2();
	    this.tabbedPane.addTab("AR(2)", null, this.panel2, "Generate AR(2) time series");
	    this.tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

	    this.panel3 = new GSTGeraTabMA1();
	    this.tabbedPane.addTab("MA(1)", null, this.panel3, "Generate MA(1) time series");
	    this.tabbedPane.setMnemonicAt(2, KeyEvent.VK_M);

	    this.panel4 = new GSTGeraTabMA2();
	    this.tabbedPane.addTab("MA(2)", null, this.panel4, "Generate MA(2) time series");
	    this.tabbedPane.setMnemonicAt(3, KeyEvent.VK_A);

	    this.panel5 = new GSTGeraTabARMA1();
	    this.tabbedPane.addTab("ARMA(1,1)", null, this.panel5, "Generate ARMA(1,1) time series");
	    this.tabbedPane.setMnemonicAt(4, KeyEvent.VK_R);

	    // Add the tabbed pane to this panel.
	    this.add(this.tabbedPane);

	    // The following line enables to use scrolling tabs.
	    this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

	}

    public int GetAbaSerieTemporal()
	{
	    return this.tabbedPane.getSelectedIndex();
	}

    public void setParametros(int par)
	{
	    GSTParamST.setTipoST(par);
	    if (par == GSTLabels.AR1)
		{
		    GSTParamST.setPAR1(this.panel1.getPAR1());
		    GSTParamST.setPAR2(0); // zera 2ndo parametro
		    GSTParamST.setValorMedio(this.panel1.getValorMedio());
		    GSTParamST.setVarRuido(this.panel1.getVarRuido());
		    GSTParamST.setNoAmostra(this.panel1.getNoAmostra());
		} else if (par == GSTLabels.AR2)
		{
		    GSTParamST.setPAR1(this.panel2.getPAR1());
		    GSTParamST.setPAR2(this.panel2.getPAR2());
		    GSTParamST.setValorMedio(this.panel2.getValorMedio());
		    GSTParamST.setVarRuido(this.panel2.getVarRuido());
		    GSTParamST.setNoAmostra(this.panel2.getNoAmostra());
		} else if (par == GSTLabels.MA1)
		{
		    GSTParamST.setPAR1(this.panel3.getPAR1());
		    GSTParamST.setPAR2(0); // zera 2ndo parametro
		    GSTParamST.setValorMedio(this.panel3.getValorMedio());
		    GSTParamST.setVarRuido(this.panel3.getVarRuido());
		    GSTParamST.setNoAmostra(this.panel3.getNoAmostra());
		} else if (par == GSTLabels.MA2)
		{
		    GSTParamST.setPAR1(this.panel4.getPAR1());
		    GSTParamST.setPAR2(this.panel4.getPAR2());
		    GSTParamST.setValorMedio(this.panel4.getValorMedio());
		    GSTParamST.setVarRuido(this.panel4.getVarRuido());
		    GSTParamST.setNoAmostra(this.panel4.getNoAmostra());
		} else if (par == GSTLabels.ARMA1)
		{
		    GSTParamST.setPAR1(this.panel5.getPAR1());
		    GSTParamST.setPAR2(this.panel5.getPAR2());
		    GSTParamST.setValorMedio(this.panel5.getValorMedio());
		    GSTParamST.setVarRuido(this.panel5.getVarRuido());
		    GSTParamST.setNoAmostra(this.panel5.getNoAmostra());
		}

	}

}
