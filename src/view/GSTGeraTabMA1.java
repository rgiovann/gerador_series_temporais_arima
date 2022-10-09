package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
public class GSTGeraTabMA1 extends JPanel   implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 316308848064659639L;


	//Values for the fields
	 
	private double MAPar1 = 0.1;
	private int  NumeroAmo = 50;
	private double ValorMedio = 0;
	private double VarRuido = 1;

	//Labels to identify the fields
 
	private JLabel MAPar1Label;
	private JLabel NumeroAmoLabel;
	private JLabel ValorMedioLabel;
	private JLabel VarRuidoLabel;

	//Strings for the labels
	private static String MAPar1String = "Valor de " + "\u03B8"+"1    |"+ "\u03B8"+"1"+"| <1      : ";
	private static String NumeroAmoString =  "Defina Nr. de Amostras      : ";
	private static String ValorMedioString = "Defina o Valor Médio        : ";
	private static String VarRuidoString =   "Defina a Variância do Ruido : ";

	//Fields for data entry
	private JFormattedTextField MAPar1Field;
	private JFormattedTextField NumeroAmoField;
	private JFormattedTextField ValorMedioField;
	private JFormattedTextField VarRuidoField;

	//Formats to format and parse numbers
	private NumberFormat MAPar1Format;
	private NumberFormat NumeroAmoFormat;
	private NumberFormat ValorMedioFormat;
	private NumberFormat VarRuidoFormat;
	
	
	@SuppressWarnings("removal")
	public GSTGeraTabMA1() {
		
 		super(new MigLayout());
		setUpFormats();

		//Create the labels.
		MAPar1Label = new JLabel(MAPar1String);
		NumeroAmoLabel  = new JLabel(NumeroAmoString);
		ValorMedioLabel = new JLabel(ValorMedioString);
		VarRuidoLabel   = new JLabel(VarRuidoString);
		
		//Create the text fields and set them up.


		MAPar1Field = new JFormattedTextField(MAPar1Format);
		MAPar1Field.setValue(new Double(MAPar1));
		MAPar1Field.setColumns(5);
		MAPar1Field.addPropertyChangeListener("value", this);

		NumeroAmoField = new JFormattedTextField(NumeroAmoFormat);
		NumeroAmoField.setValue(new Double(NumeroAmo));
		NumeroAmoField.setColumns(3);
		NumeroAmoField.addPropertyChangeListener("value", this);

		ValorMedioField = new JFormattedTextField(ValorMedioFormat);
		ValorMedioField.setValue(new Double(ValorMedio));
		ValorMedioField.setColumns(5);
		ValorMedioField.addPropertyChangeListener("value", this);
		
		VarRuidoField = new JFormattedTextField(VarRuidoFormat);
		VarRuidoField.setValue(new Double(VarRuido));
		VarRuidoField.setColumns(5);
		VarRuidoField.addPropertyChangeListener("value", this);


		//Tell accessibility tools about label/textfield pairs.
		MAPar1Label.setLabelFor(MAPar1Field);
		NumeroAmoLabel.setLabelFor(NumeroAmoField);
		ValorMedioLabel.setLabelFor(ValorMedioField);
		VarRuidoLabel.setLabelFor(VarRuidoField);

 		this.setBorder(BorderFactory.createEmptyBorder(15, 15,15, 15));

		add(MAPar1Label);	
		add(MAPar1Field,"wrap");
		add(NumeroAmoLabel);	
		add(NumeroAmoField,"wrap");
		add(ValorMedioLabel);	
		add(ValorMedioField,"wrap");
		add(VarRuidoLabel);	
		add(VarRuidoField,"wrap");
	}

	/** Called when a field's "value" property changes. */
	public void propertyChange(PropertyChangeEvent e) {
	Object source = e.getSource();
	if (source == MAPar1Field) {
	MAPar1 = ((Number)MAPar1Field.getValue()).doubleValue();
	}
	else if 
	(source == NumeroAmoField) {
	NumeroAmo  = ((Number)NumeroAmoField.getValue()).intValue();
	} 
	else if 
	(source == ValorMedioField) {
		ValorMedio  = ((Number)ValorMedioField.getValue()).doubleValue();
	} 
	else if 
	(source == VarRuidoField) {
		VarRuido  = ((Number)VarRuidoField.getValue()).doubleValue();
	} 	 
	}

	//Create and set up number formats. These objects also
	//parse numbers input by user.
	private void setUpFormats() {
	    MAPar1Format = NumberFormat.getNumberInstance();
	    NumeroAmoFormat = NumberFormat.getNumberInstance();
	    ValorMedioFormat = NumberFormat.getNumberInstance();
	    VarRuidoFormat = NumberFormat.getNumberInstance();    
	 }	

	public double getPAR1()
	{
		return MAPar1;
	}
	
	public double getVarRuido()
	{
		return VarRuido;
	}
	
	public double getValorMedio()
	{
		return ValorMedio;
	}
	
	public int getNoAmostra()
	{
		return NumeroAmo;
	
	}
	
}

