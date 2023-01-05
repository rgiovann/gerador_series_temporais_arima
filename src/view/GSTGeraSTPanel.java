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

import utilities.GSTLabels;
//import view.GSTGeraTabPanel;
import utilities.GSTParamST;

public class GSTGeraSTPanel extends JPanel implements ActionListener, GSTLabels {

    /**
     *
     */
    private static final long serialVersionUID = -1714517699059743749L;
    // botoes em novo painel layout GridLayout
    final JButton buttonGravar = new JButton(GSTLabels.botaoGravar);
    final JButton buttonCancelar = new JButton(GSTLabels.botaoCancelar);
    GSTParamST parGST = null;

    private GSTGeraTabPanel janelaTabST = null; // devo acessar alguns metodos

    public GSTGeraSTPanel()
	{

	    super(new BorderLayout());

	    JPanel painelBotoes = new JPanel();
	    painelBotoes.setLayout(new GridLayout(1, 2, 10, 0));
	    painelBotoes.add(this.buttonGravar);
	    painelBotoes.add(this.buttonCancelar);

	    // Gera as janelas com os tabs de tipos de ST
	    this.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	    this.janelaTabST = new GSTGeraTabPanel();
	    this.add(this.janelaTabST, BorderLayout.CENTER);
	    this.add(painelBotoes, BorderLayout.SOUTH);

	    this.buttonGravar.addActionListener(this);
	    this.buttonCancelar.addActionListener(this);

	}

    /**
     * Metodo que chama as janelas relacionadas aos botoes
     *
     * @param - Action event (evento relacionado ao botao GRAVA/CANCELA)
     *
     * @return - NENHUM
     *
     */
    @Override
    public void actionPerformed(ActionEvent ae)
	{

	    int iNumeroAbaST = this.janelaTabST.GetAbaSerieTemporal();
	    this.janelaTabST.setParametros(iNumeroAbaST);
	    boolean bViolouTot = false;
	    boolean bViolou1 = false;
	    boolean bViolou2 = false;
	    boolean bViolou3 = false;
	    if (ae.getActionCommand() == botaoGravar)
		{

		    String strAviso = "Time series AR(p) is not estacionary.";

		    if (GSTParamST.getTipoST() == GSTLabels.AR1 || GSTParamST.getTipoST() == GSTLabels.MA1)
			{
			    bViolouTot = !(Math.abs(GSTParamST.getPAR1()) < 1);
			}

		    if (GSTParamST.getTipoST() == GSTLabels.AR2 || GSTParamST.getTipoST() == GSTLabels.MA2)
			{
			    bViolou1 = !(GSTParamST.getPAR2() - GSTParamST.getPAR1() < 1);
			    bViolou2 = !(GSTParamST.getPAR2() + GSTParamST.getPAR1() < 1);
			    bViolou3 = !(Math.abs(GSTParamST.getPAR2()) < 1);
			    bViolouTot = bViolou1 || bViolou2 || bViolou3;
			}

		    if (GSTParamST.getTipoST() == GSTLabels.MA1 || GSTParamST.getTipoST() == GSTLabels.MA2)
			{
			    strAviso = "Time series MA(q) is not invertible.";
			}

		    else if (GSTParamST.getTipoST() == GSTLabels.ARMA1)
			{
			    bViolou1 = !(Math.abs(GSTParamST.getPAR1()) < 1);
			    if (bViolou1)
				{
				    strAviso = "Time series ARMA(1,1) is not estacionary.";
				}

			    bViolou2 = !(Math.abs(GSTParamST.getPAR2()) < 1);
			    if (bViolou2)
				{
				    strAviso = "Time series ARMA(1,1) is not invertible.";
				}

			    if (bViolou1 & bViolou2)
				{
				    strAviso = "Time series ARMA(1,1) is neither invertible nor estacionary.";
				}
			    bViolouTot = bViolou1 || bViolou2;
			}

		    if (bViolouTot)
			{
			    JOptionPane.showMessageDialog(this, strAviso, "Aviso", JOptionPane.WARNING_MESSAGE);
			    return;
			}

		    if (GSTParamST.getNoAmostra() <= 0)
			{
			    JOptionPane.showMessageDialog(this, "Sample size cannot be less or equal zero.", "WARNING",
				    JOptionPane.WARNING_MESSAGE);
			    return;
			}

		    if (GSTParamST.getNoAmostra() > GSTLabels.NUMMAXAMOSTRAS)
			{
			    strAviso = "Sample size cannot be greater than " + GSTLabels.NUMMAXAMOSTRAS + ".";
			    JOptionPane.showMessageDialog(this, strAviso, "WARNING", JOptionPane.WARNING_MESSAGE);
			    return;
			}

		    if (GSTParamST.getNoAmostra() < GSTLabels.NUMMINAMOSTRAS)
			{
			    strAviso = "Sample size cannot be greater than " + GSTLabels.NUMMINAMOSTRAS + ".";
			    JOptionPane.showMessageDialog(this, strAviso, "WARNING", JOptionPane.WARNING_MESSAGE);
			    return;
			}

		    if (GSTParamST.getVarRuido() <= 0)
			{
			    JOptionPane.showMessageDialog(this, "Noise variance cannot be less or equal zero.",
				    "WARNING", JOptionPane.WARNING_MESSAGE);
			    return;
			}

		    EventQueue.invokeLater(new Runnable()
			{
			    @Override
			    public void run()
				{
				    try
					{

					    // Create and set up the window.
					    Window parentWindow = SwingUtilities
						    .windowForComponent(GSTGeraSTPanel.this.buttonGravar);
					    GSTConsoleDialog telaConsoleGST = new GSTConsoleDialog(parentWindow, true);

					    // Display the window.
					    telaConsoleGST.pack();
					    telaConsoleGST.setVisible(true);

					} catch (Exception e)
					{
					    e.printStackTrace();

					}

				}
			});
		}

	    else if (ae.getActionCommand() == botaoCancelar)

		{
		    // this.dispose();
		    SwingUtilities.getWindowAncestor(this).dispose();

		}
	}

}
