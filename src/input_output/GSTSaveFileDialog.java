package input_output;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import view.GSTTelaMenu;

public class GSTSaveFileDialog extends JDialog  
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5179974962349703251L;

	public GSTSaveFileDialog(Window parent, boolean bModal)
	{
		super(parent);	
 		this.setModal(bModal);
 		this.setLayout(new BorderLayout());
 		this.setPreferredSize(new Dimension(660,400)); 		
 		GSTTelaMenu.SetaPosicaoJanela(this,new Dimension(getPreferredSize()));
 		
 		JButton saveBtn = new JButton("Save");

        saveBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser saveFile = new JFileChooser();
                saveFile.showSaveDialog(null);
            }
        });
         
        this.add(new JLabel("File Chooser"), BorderLayout.NORTH);
        this.add(saveBtn, BorderLayout.CENTER);
        this.setTitle("File Chooser");
        this.setVisible(true);
	}
	
}
