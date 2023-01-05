package view;

import java.awt.Dimension;
import java.awt.Window;
import java.io.IOException;

import javax.swing.JDialog;

public class GSTHelpDialog extends JDialog {

    /**
     *
     */
    private static final long serialVersionUID = -7740413892526455853L;

    // public ConsoleGSTFrame(Window parent, ParametrosGST parGST) {
    public GSTHelpDialog(Window parent, boolean bModal) throws IOException
	{
	    super(parent);
	    this.setModal(bModal);
	    this.setTitle("Help GSTJava");
	    this.setPreferredSize(new Dimension(920, 580));
	    GSTTelaMenu.SetaPosicaoJanela(this, new Dimension(this.getPreferredSize()));
	    this.setResizable(false);
	    this.getContentPane().add(new GSTHelpPanel());

	}

}
