package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
public class GSTGeraTabAR2 extends JPanel   implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 316308848064659639L;


	//Values for the fields
	private double ARPar1 = 0.1;
	private double ARPar2 = 0.1;  
	private int    NumeroAmo = 50;
	private double ValorMedio = 0;
	private double VarRuido = 1;
	
	//Labels to identify the fields
	private JLabel ARPar1Label;
	private JLabel ARPar2Label;
	private JLabel NumeroAmoLabel;	
	private JLabel ValorMedioLabel;
	private JLabel VarRuidoLabel;

	//Strings for the labels
	private static String ARPar1String = "Valor de " + "\u03A6"+"1    |"+ "\u03A6"+"1"+"| <1      : ";
	private static String ARPar2String = "Valor de " + "\u03A6"+"2    |"+ "\u03A6"+"2"+"| <1      : ";
	private static String NumeroAmoString =  "Defina Nr. de Amostras      : ";
	private static String ValorMedioString = "Defina o Valor Médio        : ";
	private static String VarRuidoString =   "Defina a Variância do Ruido : ";
	
	//Fields for data entry
	private JFormattedTextField ARPar1Field;
	private JFormattedTextField ARPar2Field;
	private JFormattedTextField NumeroAmoField;
	private JFormattedTextField ValorMedioField;
	private JFormattedTextField VarRuidoField;
	
	//Formats to format and parse numbers
	private NumberFormat ARPar1Format;
	private NumberFormat ARPar2Format;
	private NumberFormat NumeroAmoFormat;
	private NumberFormat ValorMedioFormat;
	private NumberFormat VarRuidoFormat;			
	
	
	@SuppressWarnings("removal")
	public GSTGeraTabAR2() {
		
 		super(new MigLayout());
		setUpFormats();

		//Create the labels.
		ARPar1Label = new JLabel(ARPar1String);
		ARPar2Label = new JLabel(ARPar2String);
		NumeroAmoLabel  = new JLabel(NumeroAmoString);
		ValorMedioLabel = new JLabel(ValorMedioString);
		VarRuidoLabel   = new JLabel(VarRuidoString);

		//Create the text fields and set them up.
		ARPar1Field = new JFormattedTextField(ARPar1Format);
		ARPar1Field.setValue(new Double(ARPar1));
		ARPar1Field.setColumns(5);
		ARPar1Field.addPropertyChangeListener("value", this);

		ARPar2Field = new JFormattedTextField(ARPar2Format);
		ARPar2Field.setValue(new Double(ARPar2));
		ARPar2Field.setColumns(5);
		ARPar2Field.addPropertyChangeListener("value", this);

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
		ARPar1Label.setLabelFor(ARPar1Field);
		ARPar2Label.setLabelFor(ARPar2Field);
		NumeroAmoLabel.setLabelFor(NumeroAmoField);
		ValorMedioLabel.setLabelFor(ValorMedioField);
		VarRuidoLabel.setLabelFor(VarRuidoField);
		
 		this.setBorder(BorderFactory.createEmptyBorder(15, 15,15, 15));
		add(ARPar1Label);	
		add(ARPar1Field,"wrap");
		add(ARPar2Label);	
		add(ARPar2Field,"wrap");
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
	if (source == ARPar1Field) {
	ARPar1 = ((Number)ARPar1Field.getValue()).doubleValue();
	} else if (source == ARPar2Field) {
	ARPar2 = ((Number)ARPar2Field.getValue()).doubleValue();
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
	    ARPar1Format = NumberFormat.getNumberInstance();
	    ARPar2Format = NumberFormat.getNumberInstance();
	    NumeroAmoFormat = NumberFormat.getNumberInstance();
	    ValorMedioFormat = NumberFormat.getNumberInstance();
	    VarRuidoFormat = NumberFormat.getNumberInstance();
	    
	 }	

	
	public double getPAR1()
	{
		return ARPar1;
	}
	
	public double getPAR2()
	{
		return ARPar2;
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

