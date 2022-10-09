package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import input_output.GSTTextAreaOutputStream;
import utilities.GSTLabels;
import utilities.GSTParamST;
import utilities.GSTRandoms;


public class GSTConsolePanel extends JPanel implements ActionListener,GSTLabels {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7186409348967603578L;

	private JTextArea textArea = new JTextArea(80, 130);
	private GSTTextAreaOutputStream taOutputStream = new GSTTextAreaOutputStream(textArea);
 
	private GSTRandoms  gaussianOld = new GSTRandoms();	  
    final double ERROMAX = 5;   // erro maximo (%)   
	int iFatorMult = 10; // fator de multiplicacao da amostra original
	// tamanho real da SERIE	  
	int iTamanhoTotaldaSERIE = GSTParamST.getNoAmostra()*iFatorMult;	  
	// tamanho real da SERIE aleatoria N(u,VAR) 
	// tem que ser maior (25x) que iTamanhoTotaldaSERIE, pois 
	// queremos um erro pequeno.
	int iAmostraRandomExtended = iTamanhoTotaldaSERIE*25;    
	// para uso calculo SERIE temporal
	// extrai a SERIE que eu quero do meio da SERIE extendida  		  
	int iLimiteInferior = GSTParamST.getNoAmostra()* (int) (iFatorMult/2);
	int iLimiteSuperior = iLimiteInferior + GSTParamST.getNoAmostra();	
	// Serie ramdomica at() q efetivamente vou utilizar	  
	double[] drandomParcial = new double[iTamanhoTotaldaSERIE]; 
	// Media e Variancia calculada da serie at()
	double dMediaATCalc=0;
	double dVarRuidoCalc=0;
	
    // botoes em novo painel layout  GridLayout
	final JButton buttonResultado  = new JButton(GSTLabels.botaoSalvaResultado);
	final JButton buttonGrafico    = new JButton(GSTLabels.botaoGeraGrafico);        
	final JButton buttonCancelar   = new JButton(GSTLabels.botaoCancelar);        
 	
	public GSTConsolePanel()   
	
	{	
 
		setLayout(new BorderLayout());
		add(new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));//, BorderLayout.CENTER);
	    System.setOut(new PrintStream(taOutputStream));
	

        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(1,2,10,0) );
      	painelBotoes.add(buttonResultado); 
      	painelBotoes.add(buttonGrafico);
      	buttonGrafico.setEnabled(false);
      	buttonResultado.setEnabled(false);
      	painelBotoes.add(buttonCancelar);	 
      	add(painelBotoes, BorderLayout.SOUTH );
      	buttonGrafico.addActionListener(this);
      	buttonCancelar.addActionListener(this);	  
      	buttonResultado.addActionListener(this);      	
		CalculaValores();

	    
	}


private void CalculaValores( )
{		  	  
    
   int iConseguiuSerieAT;
   int iConseguiSerieTemporal=-1;
   int iSTTimeOut=1;

   while (iSTTimeOut < 101 && iConseguiSerieTemporal < 0)
   {
      System.out.printf("Log> Tentando gerar serie at() com Media ~ 0 e Variancia ~ ");
      System.out.format(Locale.FRANCE,"%-6.2f%n", GSTParamST.getVarRuido());
      
      // Gera at()
      iConseguiuSerieAT = GeraAT();
      
      if (iConseguiuSerieAT == -1)
      {
	      System.out.println("Log> Nao conseguiu gerar serie at() apos 10 tentativas. Abortando...");
	      iSTTimeOut = 101;
      }      
      else
      {
         //System.out.println("Serie at() gerada...Media= "+ dMediaATCalc + " Variancia= " + dVarRuidoCalc);

         System.out.println("Log> Calculando as Amostras da Serie Temporal...");
         // Gera AR(p)
         iConseguiSerieTemporal = CalculaST();
         
         if (iConseguiSerieTemporal < 0 && iSTTimeOut < 101)
         {
	         System.out.println("Log> Falhou em gerar a ST..." + "Tentativa "+iSTTimeOut+ "/100");
	         iSTTimeOut++;	         
         }
      }
      
      if (iSTTimeOut >= 101)
      {
	         System.out.println("Log> Falhou em gerar a ST apos 100 tentativas. Abortando...");
      }
   } 
}

public int GeraAT()
{

   int iPonteiroAmostra=0;
   int intCodigoResultado=0;	// 0=nao consegui, 1=conseguiu, -1=timeout	  
   int iTimeOut = 10; //tento 10 vezes
   // SERIE randomica at() extendida
   double[] drandomCalculado = new double[iAmostraRandomExtended]; 
	  	     
   for(int idx=0; idx < iAmostraRandomExtended; idx++ )
   {
      drandomCalculado[idx] = gaussianOld.nextGaussian(0,GSTParamST.getVarRuido());	
   }   
   
   while (intCodigoResultado == 0)
   {
	   iPonteiroAmostra++;
		// tenta ate (25-1)*iTamanhoTotaldaSERIE
   		if ( iPonteiroAmostra <= iTamanhoTotaldaSERIE*24) // para no n-1
   		{
   			for (int idx = 0; idx < iTamanhoTotaldaSERIE; idx++)
	          {     
	    	  drandomParcial[idx] = drandomCalculado[idx+iPonteiroAmostra];
	          }
   		}	    
   		else
   			// nao achou uma amostra ramdomica com erro menor que ERROMAX
   	   {
          //System.out.println("***********************************************************************************");
          //System.out.println("Nao achou valor VAR com erro menor ou igual a ERROMAX %, gera nova amostra tenta novamente");
          //System.out.println("***********************************************************************************");	               
            iPonteiroAmostra=0;
            iTimeOut++;
   	   }
	       
	   // estima a media da amostra
	   dMediaATCalc=0;
	       
	   for (int idx = 0; idx < iTamanhoTotaldaSERIE; ++idx)
	   {			           	  
		   dMediaATCalc = dMediaATCalc + drandomParcial[idx];         
	   }
	
	   dMediaATCalc = dMediaATCalc/(iTamanhoTotaldaSERIE);

	   dVarRuidoCalc=0;
	       
	   for (int idx = 0; idx < iTamanhoTotaldaSERIE; ++idx)
	   {			      	   
		   dVarRuidoCalc = dVarRuidoCalc + ( (drandomParcial[idx] -dMediaATCalc)*(drandomParcial[idx] -dMediaATCalc)) ;
	   }
	    
	   dVarRuidoCalc = dVarRuidoCalc/(iTamanhoTotaldaSERIE);
	          
	   if (Math.abs(dMediaATCalc) <= 0.01 && Math.abs((dVarRuidoCalc-GSTParamST.getVarRuido())/GSTParamST.getVarRuido()) <= (ERROMAX/100) )
	   {	
	      //System.out.println("***********************************************************************************");			    	   			    	  
          System.out.printf("Log> Sucesso! at() com ");
          System.out.format(Locale.FRANCE,"%d", iTamanhoTotaldaSERIE);
          System.out.printf(" amostras de Media= ");
          System.out.format(Locale.FRANCE,"%-4.2f", dMediaATCalc );   
          System.out.printf(" e Var= "); 
          System.out.format(Locale.FRANCE,"%-4.2f%n", dVarRuidoCalc );
          
	      //System.out.println("***********************************************************************************");			    	   		              
	      intCodigoResultado = 1;		          
	   }		           		    	
	   if (iTimeOut > 10)
	   {
		   intCodigoResultado = -1;
	   }
   }
	
   return intCodigoResultado;
	
}

public int CalculaST()
{
	double dm3;
	double dm4;
	double dskewness;
	double dkurtosis;
	// constroi uma SERIE maior do  que o pedido
	double[] numbers = new double[iTamanhoTotaldaSERIE]; 
	//k=1...10
	double[] vetCorrela = new double[10];                
	double[] vetAutoCov = new double[10];                

	double dConstante=0;
		  
	double dMediaCalcST;
	double dCorrelaNUM;
	double dCorrelaDEN;		
	double dPar2Estimado;
	double dPar1Estimado;
	int iConseguiuST;
	double dErroPar2;
	double dErroPar1;
	double dErroMedia;
	int iPonteiroInicio;
	int iPonteiroRelativo=0;
	
	
	numbers[0]=GSTParamST.getValorMedio()/2;
	numbers[1]=GSTParamST.getValorMedio();    
	
	// autoregressiva
	if ( GSTParamST.getTipoST() == GSTLabels.AR1 || GSTParamST.getTipoST() == GSTLabels.AR2  )
	{
		dConstante =GSTParamST.getValorMedio()*(1-GSTParamST.getPAR1() - GSTParamST.getPAR2());
		for (int idx = 2; idx < iTamanhoTotaldaSERIE; ++idx)
		{
			numbers[idx]= dConstante + numbers[idx-1]*GSTParamST.getPAR1() + numbers[idx-2]*GSTParamST.getPAR2() + drandomParcial[idx-2];
	       
		}
	} 
	// m�dias moveis
	else if ( GSTParamST.getTipoST() == GSTLabels.MA1 || GSTParamST.getTipoST() == GSTLabels.MA2  )
	{
		dConstante =GSTParamST.getValorMedio();
		for (int idx = 2; idx < iTamanhoTotaldaSERIE; ++idx)
		{   	
			numbers[idx]= dConstante - drandomParcial[idx-1]*GSTParamST.getPAR1() - drandomParcial[idx-2]*GSTParamST.getPAR2() + drandomParcial[idx];
	       
		}	
	}
	// m�dias moveis & autoregressivas ARMA(1,1)
	else if ( GSTParamST.getTipoST() == GSTLabels.ARMA1)
	{
		dConstante =GSTParamST.getValorMedio()*(1-GSTParamST.getPAR1());
		for (int idx = 2; idx < iTamanhoTotaldaSERIE; ++idx)
		{			
			numbers[idx]= dConstante + numbers[idx-1]*GSTParamST.getPAR1() - drandomParcial[idx-1]*GSTParamST.getPAR2() + drandomParcial[idx];
		}
	}

	// estima a media da amostra, a partir do meio da SERIE, e vai montando uma
	// amostra de ParametrosGST.getNoAmostra(), com offset de +1 caso erro > ERROMAX
	// ate iFatorMult*ParametrosGST.getNoAmostra()-1

	iConseguiuST=-1; // -1 para entrar no loop
					 //  1 sucesso
					 // -3 n�o conseguiu tenta de novo
	                 // -2 sai do loop por time-out
	                 // -4 violacao de condicoes estac. invert.
    
	while (	iConseguiuST < 0 && iConseguiuST != -2 && iConseguiuST != -4 )
    {
		
	   iPonteiroInicio = iLimiteInferior + iPonteiroRelativo;
	   //System.out.println("DEBUG> iPonteiroInicio = iLimiteInferior + iPonteiroRelativo " + iLimiteInferior +" + " + iPonteiroRelativo);
	   dErroPar1=0;
	   dErroPar2=0;
	   iConseguiuST=1;
	   dPar1Estimado=0;
	   dPar2Estimado=0;
	   
	   // Calculando a Media da Serie Temporal ); 

	   dMediaCalcST=0;
	   for (int idx = iPonteiroInicio; idx < (iPonteiroInicio + GSTParamST.getNoAmostra() ); ++idx)
	   { 
		   //System.out.println("DEBUG> iPonteiroInicio " + iPonteiroInicio);
	   
		   dMediaCalcST = dMediaCalcST + numbers[idx];
	   }
	  
	   dMediaCalcST = dMediaCalcST/GSTParamST.getNoAmostra();
	  	 
	   // estima a correlacao k=1 e a Variancia
	   //System.out.println("Calculando a Correlacao da Serie Temporal...");

	   dCorrelaDEN=0;
	   dm3 =0;
	   dm4 = 0;
	  
	   for (int idx = iPonteiroInicio; idx < (iPonteiroInicio + GSTParamST.getNoAmostra()); ++idx)
	   {
	      
		   dCorrelaDEN = dCorrelaDEN + Math.pow((numbers[idx] - dMediaCalcST),2) ;
		   dm3         = dm3 +  Math.pow((numbers[idx] - dMediaCalcST),3) ;
		   dm4         = dm4 +  Math.pow((numbers[idx] - dMediaCalcST),4) ;
	   }
	  
	   // k=0
	   vetCorrela[0] = 1; // autocorrelacao k=0 � 1
	   vetAutoCov[0] = dCorrelaDEN/GSTParamST.getNoAmostra(); // variancia da amostra
	   
	   dm3 = dm3/GSTParamST.getNoAmostra();
	   dm4 = dm4/GSTParamST.getNoAmostra();
	  
	   dskewness = dm3/( Math.pow(Math.sqrt(vetAutoCov[0]),3) );
	   dkurtosis = dm4/( Math.pow(vetAutoCov[0],2) ) - 3;	
	   
	   // estima a autocorrelacao e covariancia k=1 
	   dCorrelaNUM=0;
	   for (int idx = iPonteiroInicio; idx < (iPonteiroInicio + GSTParamST.getNoAmostra() - 1); ++idx)
	   {     
		   dCorrelaNUM = dCorrelaNUM + ( (numbers[idx] -dMediaCalcST )*(numbers[idx+1] -dMediaCalcST )) ;
	   }
	  
	   vetCorrela[1] = dCorrelaNUM/dCorrelaDEN;
	   vetAutoCov[1] = dCorrelaNUM/GSTParamST.getNoAmostra(); // autocov k=1
	  
	   // estima a correlacao k=2

	   dCorrelaNUM=0;
	   for (int idx = iPonteiroInicio; idx < (iPonteiroInicio + GSTParamST.getNoAmostra() -2); ++idx)
	   {      
		   dCorrelaNUM = dCorrelaNUM + ( (numbers[idx] -dMediaCalcST)*(numbers[idx+2] -dMediaCalcST)) ;
	   }
	  
	   //k=2
	   vetCorrela[2] = dCorrelaNUM/dCorrelaDEN;
	  
	   //********************** CALCULA O ERRO ***********************
	   
	   if (GSTParamST.getValorMedio() == 0)
	   {
		   dErroMedia = Math.abs(dMediaCalcST);
	   }
	   else
	   {
		   dErroMedia = Math.abs( (GSTParamST.getValorMedio() - dMediaCalcST)/(GSTParamST.getValorMedio()) );
	   }
	   
	   if (dErroMedia > ERROMAX/100)  { iConseguiuST=-3; }// falhou, tenta de novo
	   
	   if (GSTParamST.getTipoST() == GSTLabels.AR1) //  AR(1)
	   {
		  // parametro � o PHI 1 (k=1) para AR(1)
		   dPar1Estimado = vetCorrela[1];
		   
		   dErroPar1 = Math.abs((dPar1Estimado-GSTParamST.getPAR1())/Math.abs(GSTParamST.getPAR1()));
		   if (dErroPar1 > ERROMAX/100)  { iConseguiuST=-3; }// falhou, tenta de novo
	   }
	   else if (GSTParamST.getTipoST() == GSTLabels.MA1)  //  MA(1)
	   {
		   // parametro � a autocorrelacao (k=1) para MA(1)
		   dPar1Estimado = 
				   (-1*GSTParamST.getPAR1())/
				   ( 1+(GSTParamST.getPAR1()*GSTParamST.getPAR1()) ) ;	
		   
		   dErroPar1 = Math.abs( (vetCorrela[1]-dPar1Estimado)/vetCorrela[1]);	
		   if (dErroPar1 > ERROMAX/100)  { iConseguiuST=-3; }// falhou, tenta de novo
	   }
	   else if ( GSTParamST.getTipoST() == GSTLabels.AR2 ) // AR(2)
	   {
		   // parametro � o PHI 1 para AR(2)
		   dPar1Estimado = (vetCorrela[1]*(1 - vetCorrela[2]))/(1-vetCorrela[1]*vetCorrela[1]) ;
		   
		   dErroPar1 = Math.abs((dPar1Estimado-GSTParamST.getPAR1())/Math.abs(GSTParamST.getPAR1()));
		   if (dErroPar1 > ERROMAX/100)  { iConseguiuST=-3; }// falhou, tenta de novo
		   
		   // parametro � o PHI 2 para AR(2)
		   dPar2Estimado = (vetCorrela[2]-vetCorrela[1]*vetCorrela[1])/(1-vetCorrela[1]*vetCorrela[1]) ;
		   
		   dErroPar2 = Math.abs((dPar2Estimado-GSTParamST.getPAR2())/Math.abs(GSTParamST.getPAR2()));		  	
		   if (dErroPar2 > ERROMAX/100)  { iConseguiuST=-3; }// falhou, tenta de novo
	   } 
	   else if (GSTParamST.getTipoST() == GSTLabels.MA2)  //  MA(2)
	   {
		   // parametro � a autocorrelacao (k=1) para MA(2)
		   dPar1Estimado = 
				   ((-1*GSTParamST.getPAR1())*(1-GSTParamST.getPAR2()))/
				   (1+(GSTParamST.getPAR1()*GSTParamST.getPAR1())+(GSTParamST.getPAR2()*GSTParamST.getPAR2()));
		   
		   dErroPar1 = Math.abs( (vetCorrela[1]-dPar1Estimado)/vetCorrela[1]);
		   if (dErroPar1 > ERROMAX/100)  { iConseguiuST=-3; }// falhou, tenta de novo
		  		   
		   // parametro � a autocorrelacao (k=2) para MA(2)
		   dPar2Estimado =  
				   (-1*GSTParamST.getPAR2())/
				   (1+(GSTParamST.getPAR1()*GSTParamST.getPAR1())+(GSTParamST.getPAR2()*GSTParamST.getPAR2())); 	 		    	
		   
		   dErroPar2 =  Math.abs( (vetCorrela[2]-dPar2Estimado)/vetCorrela[2]);
		   if (dErroPar2 > ERROMAX/100)  { iConseguiuST=-3; }// falhou, tenta de novo
	   }
	   else if (GSTParamST.getTipoST() == GSTLabels.ARMA1)  //  ARMA(1,1)
	   {
			   // parametro � a autocorrelacao (k=1) para ARMA(1,1)
			   dPar1Estimado = (1 - GSTParamST.getPAR1()*GSTParamST.getPAR2() );
			   dPar1Estimado = dPar1Estimado*(GSTParamST.getPAR1() - GSTParamST.getPAR2());
			   dPar1Estimado = dPar1Estimado/(1 + Math.pow(GSTParamST.getPAR2(),2) - 2*GSTParamST.getPAR1()*GSTParamST.getPAR2() );
		   
			   dErroPar1 = Math.abs( (vetCorrela[1]-dPar1Estimado)/vetCorrela[1]);		   
			   if (dErroPar1 > ERROMAX/100)  { iConseguiuST=-3; }// falhou, tenta de novo
		    
			   // parametro � a autocorrelacao (k=2) para ARMA(1,1)		   
			   dPar2Estimado = GSTParamST.getPAR1()*dErroPar1;
		 	   
			   dErroPar2 = Math.abs( (vetCorrela[2]-dPar2Estimado)/vetCorrela[2]);
			   if (dErroPar2 > ERROMAX/100)  { iConseguiuST=-3; }// falhou, tenta de novo    
	   }
	 
  	  
	   if(iConseguiuST > 0)
	   {
	       buttonGrafico.setEnabled(true);
	       buttonResultado.setEnabled(true);
		   System.out.println("Sucesso!");
		   if      ( GSTParamST.getTipoST() == GSTLabels.AR1 )
		   {
			   System.out.printf("**** GERANDO A ST AUTOREGRESSIVA X(t) = ");
			   System.out.format(Locale.FRANCE,"%-4.2f", dConstante);
			   System.out.printf(" + ");			   
			   System.out.format(Locale.FRANCE,"%-4.2f", GSTParamST.getPAR1());
			   System.out.printf("*Xt-1 + at() *********");
		   } 
		   else if ( GSTParamST.getTipoST() == GSTLabels.AR2 )
		   {
			 System.out.printf("**** GERANDO A ST AUTOREGRESSIVA X(t) = ");
			 System.out.format(Locale.FRANCE,"%-4.2f", dConstante);
			 System.out.printf(" + ");					 
		     System.out.format(Locale.FRANCE,"%-4.2f", GSTParamST.getPAR1());
		     System.out.printf("*Xt-1 + ");	
		     System.out.format(Locale.FRANCE,"%-4.2f", GSTParamST.getPAR2());		     
		     System.out.printf("*Xt-2 + at() *********");		
		   }
		   else if	( GSTParamST.getTipoST() == GSTLabels.MA1 )
		   {
			 System.out.printf("**** GERANDO A ST MEDIA MOVEL X(t) = ");
			 System.out.format(Locale.FRANCE,"%-4.2f", dConstante);
			 System.out.printf(" + at - ");					 			 
		     System.out.format(Locale.FRANCE,"%-4.2f", GSTParamST.getPAR1());		     
		     System.out.printf("*at-1  *********");		
		   }	
		   else if	( GSTParamST.getTipoST() == GSTLabels.MA2 )
		   {
			 System.out.printf("**** GERANDO A ST MEDIA MOVEL X(t) = ");
			 System.out.format(Locale.FRANCE,"%-4.2f", dConstante);
			 System.out.printf(" + at - ");					 			 
		     System.out.format(Locale.FRANCE,"%-4.2f", GSTParamST.getPAR1());
		     System.out.printf("*at-1 - ");	
		     System.out.format(Locale.FRANCE,"%-4.2f", GSTParamST.getPAR2());		     
		     System.out.printf("*at-2  *********");		
		   }
		   else if	( GSTParamST.getTipoST() == GSTLabels.ARMA1 )
		   {
			 System.out.printf("**** GERANDO A ST AUTOREGRESSIVA-MEDIA MOVEL X(t) = ");
			 System.out.format(Locale.FRANCE,"%-4.2f", dConstante);
			 System.out.printf(" + at + ");					 			 
		     System.out.format(Locale.FRANCE,"%-4.2f", GSTParamST.getPAR1());
		     System.out.printf("*Xt-1 - ");	
		     System.out.format(Locale.FRANCE,"%-4.2f", GSTParamST.getPAR2());		     
		     System.out.printf("*at-1  *********");		
		   }		   
		   System.out.println();
		   System.out.println();
		   
		   System.out.println("---Inicio---"); 
		
		   // Imprime a ST em tela
   
		   for (int idx = iPonteiroInicio; idx < (iPonteiroInicio + GSTParamST.getNoAmostra()); ++idx)
	       { 
			   System.out.format(Locale.FRANCE,"%8.3f%n", numbers[idx]);
			   GSTParamST.setNumbersST(idx-iPonteiroInicio,numbers[idx]);
	  	   }
		   
		   System.out.println("---Fim---");  
		   System.out.println();
		   System.out.println("            --- RELATORIO ---");
		   
		   if ( GSTParamST.getTipoST() == GSTLabels.AR1 || GSTParamST.getTipoST() == GSTLabels.AR2 )
		   {
			   	System.out.printf("PHI 1 digitado              ---> "); 
			   	System.out.format(Locale.FRANCE,"%-6.3f%n", GSTParamST.getPAR1());
			   	System.out.printf("PHI 1 calculado (E<=");
			   	System.out.format(Locale.FRANCE,"%-3.1f%%", ERROMAX);
			   	System.out.printf(")   ---> ");     
			   	System.out.format(Locale.FRANCE,"%-6.3f%n", dPar1Estimado);			   
		   }
		   
		   if ( GSTParamST.getTipoST() == GSTLabels.AR2 )
		   {
			   	System.out.printf("PHI 2 digitado              ---> "); 
		     	System.out.format(Locale.FRANCE,"%-6.3f%n", GSTParamST.getPAR2());
		     	System.out.printf("PHI 2 calculado (E<=");
		     	System.out.format(Locale.FRANCE,"%-3.1f%%", ERROMAX);
		     	System.out.printf(")   ---> ");     
		     	System.out.format(Locale.FRANCE,"%-6.3f%n", dPar2Estimado);	    	 
		   }		   
		   if ( GSTParamST.getTipoST() == GSTLabels.MA1 || GSTParamST.getTipoST() == GSTLabels.MA2 || GSTParamST.getTipoST() == GSTLabels.ARMA1 )
		   {   
			   if(	   GSTParamST.getTipoST() == GSTLabels.MA1 
				    || GSTParamST.getTipoST() == GSTLabels.MA2 )
			   {
					System.out.printf("TETA 1 digitado             ---> "); 
					System.out.format(Locale.FRANCE,"%-6.3f%n", GSTParamST.getPAR1());					   
			   }
			   else if(GSTParamST.getTipoST() == GSTLabels.ARMA1)
		   	   {
				   	System.out.printf("PHI 1 digitado              ---> "); 
			   		System.out.format(Locale.FRANCE,"%-6.3f%n", GSTParamST.getPAR1());
			   		System.out.printf("TETA 1 digitado             ---> "); 
			   		System.out.format(Locale.FRANCE,"%-6.3f%n", GSTParamST.getPAR2());			   	
		   	   }
		   
		   }		   
		   if ( GSTParamST.getTipoST() == GSTLabels.MA2 )
		   {
			   System.out.printf("TETA 2 digitado             ---> "); 
			   System.out.format(Locale.FRANCE,"%-6.3f%n", GSTParamST.getPAR2());	    	 
		   }
		  
		   System.out.printf("Quantidade de amostras      ---> ");
		   System.out.format(Locale.FRANCE,"%d%n", GSTParamST.getNoAmostra());
		   System.out.printf("Media calculada da amostra  ---> ");
		   System.out.format(Locale.FRANCE,"%-6.2f%n", dMediaCalcST);
		   System.out.printf("Media digitada              ---> ");
		   System.out.format(Locale.FRANCE,"%-6.2f%n", GSTParamST.getValorMedio());	     
		   System.out.printf("Variancia de a(t) calculada ---> " );
		   System.out.format(Locale.FRANCE,"%-6.2f%n", dVarRuidoCalc);
		   System.out.printf("Variancia de a(t) digitada  ---> ");
		   System.out.format(Locale.FRANCE,"%-6.2f%n",GSTParamST.getVarRuido());
		   
		   if (       GSTParamST.getTipoST() == GSTLabels.AR1 
				   || GSTParamST.getTipoST() == GSTLabels.AR2  )
		   {
			   System.out.printf("Correlacao k=1              ---> ");
			   System.out.format(Locale.FRANCE,"%-6.3f%n", vetCorrela[1]);
		   }
		   
		   if (       GSTParamST.getTipoST() == GSTLabels.MA1 
				   || GSTParamST.getTipoST() == GSTLabels.MA2 
				   || GSTParamST.getTipoST() == GSTLabels.ARMA1 )
		   {   
			   if ( GSTParamST.getTipoST() == GSTLabels.MA1)
		   	   {
			   		System.out.printf("Corr. k=1 (calc. de TETA 1) ---> ");
		   	   }
			   else if ( GSTParamST.getTipoST() == GSTLabels.MA2)
		   	   {
				   System.out.printf("Corr. k=1 (calc. de TETA 1/2)--> ");		   		   
		   	   }
			   else if ( GSTParamST.getTipoST() == GSTLabels.ARMA1)
		   	   {
				   System.out.printf("Corr. k=1  (calc. TETA/PHI) ---> ");		   		   
		   	   }

			   System.out.format(Locale.FRANCE,"%-6.3f%n", dPar1Estimado);				   
			   
			   if ( GSTParamST.getTipoST() == GSTLabels.ARMA1)
		   	   {
				   System.out.printf("Corr. k=1 amostra  (E<=");
				   System.out.format(Locale.FRANCE,"%-3.1f%%", ERROMAX);
				   System.out.printf(")---> ");     
				   System.out.format(Locale.FRANCE,"%-6.3f%n", vetCorrela[1]);				   
		   	   }
			   else  // MA(1) e (2)
			   {
				   System.out.printf("Corr. k=1 amostra (E<=");
				   System.out.format(Locale.FRANCE,"%-3.1f%%", ERROMAX);
				   System.out.printf(") ---> ");     
				   System.out.format(Locale.FRANCE,"%-6.3f%n", vetCorrela[1]);
			   }
		   }
		   
		   if ( 	GSTParamST.getTipoST() == GSTLabels.AR1 
				 || GSTParamST.getTipoST() == GSTLabels.AR2  )
		   {
			   System.out.printf("Correlacao k=2              ---> ");
			   System.out.format(Locale.FRANCE,"%-6.3f%n", vetCorrela[2]);
		   }
		   
		   if ( GSTParamST.getTipoST() == GSTLabels.MA2  )
		   {
			   System.out.printf("Corr. k=2 (calc. de TETA 1/2)--> ");
			   System.out.format(Locale.FRANCE,"%-6.3f%n", dPar2Estimado);			   
 			   System.out.printf("Corr. k=2 amostra (E<=");
			   System.out.format(Locale.FRANCE,"%-3.1f%%", ERROMAX);
			   System.out.printf(") ---> ");     
			   System.out.format(Locale.FRANCE,"%-6.3f%n",vetCorrela[2] );			   
		   }	
		   else if ( GSTParamST.getTipoST() == GSTLabels.ARMA1  )
		   {
			   System.out.printf("Corr. k=2  (calc. TETA/PHI) ---> ");
			   System.out.format(Locale.FRANCE,"%-6.3f%n", dPar2Estimado);			   
 			   System.out.printf("Corr. k=2  amostra (E<=");
			   System.out.format(Locale.FRANCE,"%-3.1f%%", ERROMAX);
			   System.out.printf(")---> ");     
			   System.out.format(Locale.FRANCE,"%-6.3f%n",vetCorrela[2]);				   
		   }
		   System.out.printf("Kurtosis                    ---> ");
		   System.out.format(Locale.FRANCE,"%-6.3f%n", dkurtosis);
		   System.out.printf("Skewness                    ---> ");
		   System.out.format(Locale.FRANCE,"%-6.3f%n", dskewness);
		   System.out.println();
		   System.out.println("***********************************************************");
	   }
	  
	   //System.out.println("DEBUG> iConseguiuST "+iConseguiuST);

	   iPonteiroRelativo++;
	   //System.out.println("DEBUG> iPonteiroRelativo " + iPonteiroRelativo);
	  
	   if( (iPonteiroInicio + GSTParamST.getNoAmostra()) >= (iFatorMult*GSTParamST.getNoAmostra()) ) 
	   {
		   //System.out.println("DEBUG> iPonteiroRelativo � -2!!! " + iPonteiroRelativo);		   
		   iConseguiuST=-2; // chegou no fim da amostra, retorna
	   }
    }
	
	return iConseguiuST;
}


@Override
public void actionPerformed(ActionEvent ae)
	{
	
	if ( ae.getActionCommand() == botaoCancelar)           		
		
	{
		//this.dispose();
		SwingUtilities.getWindowAncestor(this).dispose();

	}	
	else if ( ae.getActionCommand() == botaoGeraGrafico)           		
		
	{
 
		EventQueue.invokeLater(new Runnable() {  
            public void run() {  
                try {  
                	Window parentWindow = SwingUtilities.windowForComponent(buttonGrafico);
                	GSTGeraGrafico telaGraficoleGST = new GSTGeraGrafico(parentWindow,true);
                  	telaGraficoleGST.pack();
                	telaGraficoleGST.setVisible(true);
                   
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        });	
		
		
		
	}
	else if ( ae.getActionCommand() == botaoSalvaResultado)           		
	{
 
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(
	        "Excel Files", "xls");
	    chooser.setFileFilter(filter);
	    String strNomeArq  = null;
	    String strTipoST = null;
	    int returnVal = chooser.showSaveDialog(SwingUtilities.windowForComponent(buttonResultado));
        
	    if(returnVal == JFileChooser.APPROVE_OPTION) 
	    {
	        //System.out.println("DEBUG> You chose to save this file: " +
	        //     chooser.getSelectedFile().getName() +" at " + chooser.getSelectedFile().getAbsolutePath());
	    	strNomeArq  = chooser.getSelectedFile().getAbsolutePath();
	    	boolean bcontemExtensao = false;
 	    	bcontemExtensao = strNomeArq.contains(".xls");
 
	    	if(!bcontemExtensao)
            {
            	strNomeArq = strNomeArq + ".xls"; 
            }	
	     
		   	if      ( GSTParamST.getTipoST() == GSTLabels.AR1 )
			{
		   		strTipoST ="Tipo AR(1) PHI 1= " + GSTParamST.getPAR1();
			} 
		    else if ( GSTParamST.getTipoST() == GSTLabels.AR2 )
			{
		    	strTipoST ="Tipo AR(2) PHI 1= " + GSTParamST.getPAR1() + " PHI 2="+GSTParamST.getPAR2();		
			}
			else if	( GSTParamST.getTipoST() == GSTLabels.MA1 )
			{
	   			strTipoST ="Tipo MA(1) TETA 1= " + GSTParamST.getPAR1();	
			}	
			else if	( GSTParamST.getTipoST() == GSTLabels.MA2 )
			{
				strTipoST ="Tipo MA(2) TETA 1= " + GSTParamST.getPAR1() + " TETA 2="+GSTParamST.getPAR2();		
			}
		    else if	( GSTParamST.getTipoST() == GSTLabels.ARMA1 )
			{
		    	strTipoST ="Tipo ARMA(1) PHI 1= " + GSTParamST.getPAR1() + " TETA 1="+GSTParamST.getPAR2();		
			}	            
	   		strTipoST = strTipoST + " Valor Médio =" + GSTParamST.getValorMedio();
	   		strTipoST = strTipoST + " Var. Ruído =" + GSTParamST.getVarRuido();    
	   	
	   		File fArquivo =  new File(strNomeArq);
	   		int reply=JOptionPane.YES_OPTION;
	   		if ( fArquivo.isFile() )
	   		{
	   		     reply = JOptionPane.showConfirmDialog(null, "Arquivo " + strNomeArq + " já existe, confirma gravar ?", "AVISO", JOptionPane.YES_NO_OPTION);
	   		}
	        if (reply == JOptionPane.YES_OPTION) 
	         {
	        	try
	        	{  
            	 	Workbook wb = new HSSFWorkbook();
            	 	org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("Serie Temporal");
            	 	FileOutputStream fileOut = new FileOutputStream(strNomeArq);
            	 	// Create a row and put some cells in it. Rows are 0 based.
            	 	Row row = sheet.createRow((short) 0);
            	 	// Create a cell and put a value in it.
            	 	Cell cell = row.createCell(0); // linha 1 vai titulo
            	 	cell.setCellValue(strTipoST);
            	 	// linha 2 em diante vai a serie
            	 	for (int idx=1;idx<= GSTParamST.getNoAmostra();idx++)
            	 	{
            	 		row = sheet.createRow((short) idx);
            	 		cell = row.createCell(0);
            	 		cell.setCellValue(GSTParamST.getNumbersST(idx-1));
            	 	}
            	 	wb.write(fileOut);
            	 	fileOut.close();    
	        	}  
                 
	        	catch (IOException e) 
	        	{
	        		e.printStackTrace();
	        	}
	         }
	    } 
	
		}
	}

 
}