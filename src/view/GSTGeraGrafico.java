package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Window;
import javax.swing.JDialog;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import utilities.GSTLabels;
import utilities.GSTParamST;
 
public class GSTGeraGrafico extends JDialog   implements GSTLabels {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5201212048918893233L;
	
	//public ConsoleGSTFrame(Window parent, ParametrosGST parGST) {
	public GSTGeraGrafico(Window parent, boolean bModal) {
		
		super(parent);	
 		this.setModal(bModal);
		this.setTitle("Gráfico da ST");	
		//this.setPreferredSize(new Dimension(660,400)); 		
		GSTTelaMenu.SetaPosicaoJanela(this,new Dimension(getPreferredSize()));
		//this.getContentPane().add(new ConsoleGSTPanel());  
		
	    final XYSeries series = new XYSeries("Série Temporal");
	    
	    for(int idx=0;idx < GSTParamST.getNoAmostra(); idx++)
	    {
	    	series.add(idx+1, GSTParamST.getNumbersST(idx));
	    }
        String strTitulo=null;
        
        if (GSTParamST.getTipoST() == GSTLabels.AR1)
        {strTitulo = "Série Temporal AR(1)";}
        else if (GSTParamST.getTipoST() == GSTLabels.AR2)
        {strTitulo = "Série Temporal AR(2)";}
        else if (GSTParamST.getTipoST() == GSTLabels.MA1)
        {strTitulo = "Série Temporal MA(1)";}      
        else if (GSTParamST.getTipoST() == GSTLabels.MA2)
        {strTitulo = "Série Temporal MA(2)";}   
        else if (GSTParamST.getTipoST() == GSTLabels.ARMA1)
        {strTitulo = "Série Temporal ARMA(1,1)";}    
	    
        //XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	    // renderer.setSeriesPaint(0,Color.BLACK);

        final XYSeriesCollection data = new XYSeriesCollection(series);
	    final JFreeChart chart = ChartFactory.createXYLineChart(
	    	strTitulo,
	        "X", 
	        "Y", 
	        data,
	        PlotOrientation.VERTICAL,
	        true,
	        true,
	        false
	    );

	    XYPlot plot = chart.getXYPlot();
	    plot.getRenderer().setSeriesPaint(0, Color.DARK_GRAY);  
	    
	    
	    final ChartPanel chartPanel = new ChartPanel(chart);
	    chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
	    this.setContentPane(chartPanel); 		
 	}
 
 }