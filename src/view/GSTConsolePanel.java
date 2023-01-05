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
import java.text.NumberFormat;
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

public class GSTConsolePanel extends JPanel implements ActionListener, GSTLabels {

    /**
     *
     */
    private static final long serialVersionUID = -7186409348967603578L;

    private JTextArea textArea = new JTextArea(80, 130);
    private GSTTextAreaOutputStream taOutputStream = new GSTTextAreaOutputStream(this.textArea);

    private GSTRandoms gaussianOld = new GSTRandoms();
    final double ERROMAX = 5; // erro maximo (%)
    int iFatorMult = 10; // fator de multiplicacao da amostra original
    // tamanho real da SERIE
    int iTamanhoTotaldaSERIE = GSTParamST.getNoAmostra() * this.iFatorMult;
    // tamanho real da SERIE aleatoria N(u,VAR)
    // tem que ser maior (25x) que iTamanhoTotaldaSERIE, pois
    // queremos um erro pequeno.
    int iAmostraRandomExtended = this.iTamanhoTotaldaSERIE * 25;
    // para uso calculo SERIE temporal
    // extrai a SERIE que eu quero do meio da SERIE extendida
    int iLimiteInferior = GSTParamST.getNoAmostra() * (this.iFatorMult / 2);
    int iLimiteSuperior = this.iLimiteInferior + GSTParamST.getNoAmostra();
    // Serie ramdomica at() q efetivamente vou utilizar
    double[] drandomParcial = new double[this.iTamanhoTotaldaSERIE];
    // Media e Variancia calculada da serie at()
    double dMediaATCalc = 0;
    double dVarRuidoCalc = 0;

    // botoes em novo painel layout GridLayout
    final JButton buttonResultado = new JButton(GSTLabels.botaoSalvaResultado);
    final JButton buttonGrafico = new JButton(GSTLabels.botaoGeraGrafico);
    final JButton buttonCancelar = new JButton(GSTLabels.botaoCancelar);

    public GSTConsolePanel()

	{

	    this.setLayout(new BorderLayout());
	    this.add(new JScrollPane(this.textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));// , BorderLayout.CENTER);
	    System.setOut(new PrintStream(this.taOutputStream));

	    JPanel painelBotoes = new JPanel();
	    painelBotoes.setLayout(new GridLayout(1, 2, 10, 0));
	    painelBotoes.add(this.buttonResultado);
	    painelBotoes.add(this.buttonGrafico);
	    this.buttonGrafico.setEnabled(false);
	    this.buttonResultado.setEnabled(false);
	    painelBotoes.add(this.buttonCancelar);
	    this.add(painelBotoes, BorderLayout.SOUTH);
	    this.buttonGrafico.addActionListener(this);
	    this.buttonCancelar.addActionListener(this);
	    this.buttonResultado.addActionListener(this);
	    this.CalculaValores();

	}

    private void CalculaValores()
	{

	    int iConseguiuSerieAT;
	    int iConseguiSerieTemporal = -1;
	    int iSTTimeOut = 1;

	    while (iSTTimeOut < 101 && iConseguiSerieTemporal < 0)
		{
		    System.out.printf(Locale.US,
			    "Log> Trying to generate a base time series at() with mean  ~ %.2f and variance ~ %.2f\n",
			    GSTParamST.getValorMedio(), GSTParamST.getVarRuido());

		    // Gera at()
		    iConseguiuSerieAT = this.GeraAT();

		    if (iConseguiuSerieAT == -1)
			{
			    System.out.println(
				    "Log> Could not generate at() time series after 10 attempts. Aborting...\n");
			    iSTTimeOut = 101;
			} else
			{
			    // System.out.println("Serie at() gerada...Media= "+ dMediaATCalc + " Variancia=
			    // " + dVarRuidoCalc);

			    System.out.println("Log> Calculating values of required X(t) time series...");
			    // Gera AR(p)
			    iConseguiSerieTemporal = this.CalculaST();

			    if (iConseguiSerieTemporal < 0 && iSTTimeOut < 101)
				{
				    System.out.println("Log> Failed to generate required X(t) time series..."
					    + "Attempt " + iSTTimeOut + "/100\n");
				    iSTTimeOut++;
				}
			}

		    if (iSTTimeOut >= 101)
			{
			    System.out.println(
				    "Log> Failed to generate required X(t) time series after 100 attempts. Aborting...\n");
			}
		}
	}

    public int GeraAT()
	{

	    int iPonteiroAmostra = 0;
	    int intCodigoResultado = 0; // 0=nao consegui, 1=conseguiu, -1=timeout
	    int iTimeOut = 10; // tento 10 vezes
	    // SERIE randomica at() extendida
	    double[] drandomCalculado = new double[this.iAmostraRandomExtended];

	    for (int idx = 0; idx < this.iAmostraRandomExtended; idx++)
		{
		    drandomCalculado[idx] = this.gaussianOld.nextGaussian(0, GSTParamST.getVarRuido());
		}

	    while (intCodigoResultado == 0)
		{
		    iPonteiroAmostra++;
		    // tenta ate (25-1)*iTamanhoTotaldaSERIE
		    if (iPonteiroAmostra <= this.iTamanhoTotaldaSERIE * 24) // para no n-1
			{
			    for (int idx = 0; idx < this.iTamanhoTotaldaSERIE; idx++)
				{
				    this.drandomParcial[idx] = drandomCalculado[idx + iPonteiroAmostra];
				}
			} else
		    // nao achou uma amostra ramdomica com erro menor que ERROMAX
			{
			    // System.out.println("***********************************************************************************");
			    // System.out.println("Nao achou valor VAR com erro menor ou igual a ERROMAX %,
			    // gera nova amostra tenta novamente");
			    // System.out.println("***********************************************************************************");
			    iPonteiroAmostra = 0;
			    iTimeOut++;
			}

		    // estima a media da amostra
		    this.dMediaATCalc = 0;

		    for (int idx = 0; idx < this.iTamanhoTotaldaSERIE; ++idx)
			{
			    this.dMediaATCalc = this.dMediaATCalc + this.drandomParcial[idx];
			}

		    this.dMediaATCalc = this.dMediaATCalc / this.iTamanhoTotaldaSERIE;

		    this.dVarRuidoCalc = 0;

		    for (int idx = 0; idx < this.iTamanhoTotaldaSERIE; ++idx)
			{
			    this.dVarRuidoCalc = this.dVarRuidoCalc + (this.drandomParcial[idx] - this.dMediaATCalc)
				    * (this.drandomParcial[idx] - this.dMediaATCalc);
			}

		    this.dVarRuidoCalc = this.dVarRuidoCalc / this.iTamanhoTotaldaSERIE;

		    if (Math.abs(this.dMediaATCalc) <= 0.01 && Math.abs((this.dVarRuidoCalc - GSTParamST.getVarRuido())
			    / GSTParamST.getVarRuido()) <= this.ERROMAX / 100)
			{
			    // System.out.println("***********************************************************************************");
			    System.out.printf("Log> Success! at() time series with ");
			    System.out.format(Locale.US, "%d", this.iTamanhoTotaldaSERIE);
			    System.out.printf(" total samples (greater than input sample size)\n");
			    System.out.printf("Log> with estimated noise mean= ");
			    System.out.format(Locale.US, "%-4.2f", this.dMediaATCalc);
			    System.out.printf(" and estimated noise variance= ");
			    System.out.format(Locale.US, "%-4.2f%n", this.dVarRuidoCalc);

			    // System.out.println("***********************************************************************************");
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
	    // constroi uma SERIE maior do que o pedido
	    double[] numbers = new double[this.iTamanhoTotaldaSERIE];
	    // k=1...10
	    double[] vetCorrela = new double[10];
	    double[] vetAutoCov = new double[10];

	    double dConstante = 0;

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
	    int iPonteiroRelativo = 0;

	    numbers[0] = GSTParamST.getValorMedio() / 2;
	    numbers[1] = GSTParamST.getValorMedio();

	    // autoregressiva
	    if (GSTParamST.getTipoST() == GSTLabels.AR1 || GSTParamST.getTipoST() == GSTLabels.AR2)
		{
		    dConstante = GSTParamST.getValorMedio() * (1 - GSTParamST.getPAR1() - GSTParamST.getPAR2());
		    for (int idx = 2; idx < this.iTamanhoTotaldaSERIE; ++idx)
			{
			    numbers[idx] = dConstante + numbers[idx - 1] * GSTParamST.getPAR1()
				    + numbers[idx - 2] * GSTParamST.getPAR2() + this.drandomParcial[idx - 2];

			}
		}
	    // m�dias moveis
	    else if (GSTParamST.getTipoST() == GSTLabels.MA1 || GSTParamST.getTipoST() == GSTLabels.MA2)
		{
		    dConstante = GSTParamST.getValorMedio();
		    for (int idx = 2; idx < this.iTamanhoTotaldaSERIE; ++idx)
			{
			    numbers[idx] = dConstante - this.drandomParcial[idx - 1] * GSTParamST.getPAR1()
				    - this.drandomParcial[idx - 2] * GSTParamST.getPAR2() + this.drandomParcial[idx];

			}
		}
	    // m�dias moveis & autoregressivas ARMA(1,1)
	    else if (GSTParamST.getTipoST() == GSTLabels.ARMA1)
		{
		    dConstante = GSTParamST.getValorMedio() * (1 - GSTParamST.getPAR1());
		    for (int idx = 2; idx < this.iTamanhoTotaldaSERIE; ++idx)
			{
			    numbers[idx] = dConstante + numbers[idx - 1] * GSTParamST.getPAR1()
				    - this.drandomParcial[idx - 1] * GSTParamST.getPAR2() + this.drandomParcial[idx];
			}
		}

	    // estima a media da amostra, a partir do meio da SERIE, e vai montando uma
	    // amostra de ParametrosGST.getNoAmostra(), com offset de +1 caso erro > ERROMAX
	    // ate iFatorMult*ParametrosGST.getNoAmostra()-1

	    iConseguiuST = -1; // -1 para entrar no loop
			       // 1 sucesso
			       // -3 n�o conseguiu tenta de novo
			       // -2 sai do loop por time-out
			       // -4 violacao de condicoes estac. invert.

	    while (iConseguiuST < 0 && iConseguiuST != -2 && iConseguiuST != -4)
		{

		    iPonteiroInicio = this.iLimiteInferior + iPonteiroRelativo;
		    // System.out.println("DEBUG> iPonteiroInicio = iLimiteInferior +
		    // iPonteiroRelativo " + iLimiteInferior +" + " + iPonteiroRelativo);
		    dErroPar1 = 0;
		    dErroPar2 = 0;
		    iConseguiuST = 1;
		    dPar1Estimado = 0;
		    dPar2Estimado = 0;

		    // Calculando a Media da Serie Temporal );

		    dMediaCalcST = 0;
		    for (int idx = iPonteiroInicio; idx < iPonteiroInicio + GSTParamST.getNoAmostra(); ++idx)
			{
			    // System.out.println("DEBUG> iPonteiroInicio " + iPonteiroInicio);

			    dMediaCalcST = dMediaCalcST + numbers[idx];
			}

		    dMediaCalcST = dMediaCalcST / GSTParamST.getNoAmostra();

		    // estima a correlacao k=1 e a Variancia
		    // System.out.println("Calculando a Correlacao da Serie Temporal...");

		    dCorrelaDEN = 0;
		    dm3 = 0;
		    dm4 = 0;

		    for (int idx = iPonteiroInicio; idx < iPonteiroInicio + GSTParamST.getNoAmostra(); ++idx)
			{

			    dCorrelaDEN = dCorrelaDEN + Math.pow(numbers[idx] - dMediaCalcST, 2);
			    dm3 = dm3 + Math.pow(numbers[idx] - dMediaCalcST, 3);
			    dm4 = dm4 + Math.pow(numbers[idx] - dMediaCalcST, 4);
			}

		    // k=0
		    vetCorrela[0] = 1; // autocorrelacao k=0 � 1
		    vetAutoCov[0] = dCorrelaDEN / GSTParamST.getNoAmostra(); // variancia da amostra

		    dm3 = dm3 / GSTParamST.getNoAmostra();
		    dm4 = dm4 / GSTParamST.getNoAmostra();

		    dskewness = dm3 / Math.pow(Math.sqrt(vetAutoCov[0]), 3);
		    dkurtosis = dm4 / Math.pow(vetAutoCov[0], 2) - 3;

		    // estima a autocorrelacao e covariancia k=1
		    dCorrelaNUM = 0;
		    for (int idx = iPonteiroInicio; idx < iPonteiroInicio + GSTParamST.getNoAmostra() - 1; ++idx)
			{
			    dCorrelaNUM = dCorrelaNUM
				    + (numbers[idx] - dMediaCalcST) * (numbers[idx + 1] - dMediaCalcST);
			}

		    vetCorrela[1] = dCorrelaNUM / dCorrelaDEN;
		    vetAutoCov[1] = dCorrelaNUM / GSTParamST.getNoAmostra(); // autocov k=1

		    // estima a correlacao k=2

		    dCorrelaNUM = 0;
		    for (int idx = iPonteiroInicio; idx < iPonteiroInicio + GSTParamST.getNoAmostra() - 2; ++idx)
			{
			    dCorrelaNUM = dCorrelaNUM
				    + (numbers[idx] - dMediaCalcST) * (numbers[idx + 2] - dMediaCalcST);
			}

		    // k=2
		    vetCorrela[2] = dCorrelaNUM / dCorrelaDEN;

		    // ********************** CALCULA O ERRO ***********************

		    if (GSTParamST.getValorMedio() == 0)
			{
			    dErroMedia = Math.abs(dMediaCalcST);
			} else
			{
			    dErroMedia = Math
				    .abs((GSTParamST.getValorMedio() - dMediaCalcST) / GSTParamST.getValorMedio());
			}

		    if (dErroMedia > this.ERROMAX / 100)
			{
			    iConseguiuST = -3;
			} // falhou, tenta de novo

		    if (GSTParamST.getTipoST() == GSTLabels.AR1) // AR(1)
			{
			    // parametro � o PHI_1 (k=1) para AR(1)
			    dPar1Estimado = vetCorrela[1];

			    dErroPar1 = Math
				    .abs((dPar1Estimado - GSTParamST.getPAR1()) / Math.abs(GSTParamST.getPAR1()));
			    if (dErroPar1 > this.ERROMAX / 100)
				{
				    iConseguiuST = -3;
				} // falhou, tenta de novo
			} else if (GSTParamST.getTipoST() == GSTLabels.MA1) // MA(1)
			{
			    // parametro � a autocorrelacao (k=1) para MA(1)
			    dPar1Estimado = -1 * GSTParamST.getPAR1()
				    / (1 + GSTParamST.getPAR1() * GSTParamST.getPAR1());

			    dErroPar1 = Math.abs((vetCorrela[1] - dPar1Estimado) / vetCorrela[1]);
			    if (dErroPar1 > this.ERROMAX / 100)
				{
				    iConseguiuST = -3;
				} // falhou, tenta de novo
			} else if (GSTParamST.getTipoST() == GSTLabels.AR2) // AR(2)
			{
			    // parametro � o PHI_1 para AR(2)
			    dPar1Estimado = vetCorrela[1] * (1 - vetCorrela[2]) / (1 - vetCorrela[1] * vetCorrela[1]);

			    dErroPar1 = Math
				    .abs((dPar1Estimado - GSTParamST.getPAR1()) / Math.abs(GSTParamST.getPAR1()));
			    if (dErroPar1 > this.ERROMAX / 100)
				{
				    iConseguiuST = -3;
				} // falhou, tenta de novo

			    // parametro � o PHI_2 para AR(2)
			    dPar2Estimado = (vetCorrela[2] - vetCorrela[1] * vetCorrela[1])
				    / (1 - vetCorrela[1] * vetCorrela[1]);

			    dErroPar2 = Math
				    .abs((dPar2Estimado - GSTParamST.getPAR2()) / Math.abs(GSTParamST.getPAR2()));
			    if (dErroPar2 > this.ERROMAX / 100)
				{
				    iConseguiuST = -3;
				} // falhou, tenta de novo
			} else if (GSTParamST.getTipoST() == GSTLabels.MA2) // MA(2)
			{
			    // parametro � a autocorrelacao (k=1) para MA(2)
			    dPar1Estimado = -1 * GSTParamST.getPAR1() * (1 - GSTParamST.getPAR2())
				    / (1 + GSTParamST.getPAR1() * GSTParamST.getPAR1()
					    + GSTParamST.getPAR2() * GSTParamST.getPAR2());

			    dErroPar1 = Math.abs((vetCorrela[1] - dPar1Estimado) / vetCorrela[1]);
			    if (dErroPar1 > this.ERROMAX / 100)
				{
				    iConseguiuST = -3;
				} // falhou, tenta de novo

			    // parametro � a autocorrelacao (k=2) para MA(2)
			    dPar2Estimado = -1 * GSTParamST.getPAR2() / (1 + GSTParamST.getPAR1() * GSTParamST.getPAR1()
				    + GSTParamST.getPAR2() * GSTParamST.getPAR2());

			    dErroPar2 = Math.abs((vetCorrela[2] - dPar2Estimado) / vetCorrela[2]);
			    if (dErroPar2 > this.ERROMAX / 100)
				{
				    iConseguiuST = -3;
				} // falhou, tenta de novo
			} else if (GSTParamST.getTipoST() == GSTLabels.ARMA1) // ARMA(1,1)
			{
			    // parametro � a autocorrelacao (k=1) para ARMA(1,1)
			    dPar1Estimado = 1 - GSTParamST.getPAR1() * GSTParamST.getPAR2();
			    dPar1Estimado = dPar1Estimado * (GSTParamST.getPAR1() - GSTParamST.getPAR2());
			    dPar1Estimado = dPar1Estimado / (1 + Math.pow(GSTParamST.getPAR2(), 2)
				    - 2 * GSTParamST.getPAR1() * GSTParamST.getPAR2());

			    dErroPar1 = Math.abs((vetCorrela[1] - dPar1Estimado) / vetCorrela[1]);
			    if (dErroPar1 > this.ERROMAX / 100)
				{
				    iConseguiuST = -3;
				} // falhou, tenta de novo

			    // parametro � a autocorrelacao (k=2) para ARMA(1,1)
			    dPar2Estimado = GSTParamST.getPAR1() * dErroPar1;

			    dErroPar2 = Math.abs((vetCorrela[2] - dPar2Estimado) / vetCorrela[2]);
			    if (dErroPar2 > this.ERROMAX / 100)
				{
				    iConseguiuST = -3;
				} // falhou, tenta de novo
			}

		    if (iConseguiuST > 0)
			{
			    this.buttonGrafico.setEnabled(true);
			    this.buttonResultado.setEnabled(true);
			    System.out.println("SUCCESS GENERATING X(t) time series !\n");
			    if (GSTParamST.getTipoST() == GSTLabels.AR1)
				{
				    System.out.printf("**** GENERATING TS AUTOREGRESSIVE X(t) = ");
				    System.out.format(Locale.US, "%-4.2f", dConstante);
				    System.out.printf(" + ");
				    System.out.format(Locale.US, "%-4.2f", GSTParamST.getPAR1());
				    System.out.printf("*Xt-1 + at() *********");
				} else if (GSTParamST.getTipoST() == GSTLabels.AR2)
				{
				    System.out.printf("**** GENERATING TS AUTOREGRESSIVE X(t) = ");
				    System.out.format(Locale.US, "%-4.2f", dConstante);
				    System.out.printf(" + ");
				    System.out.format(Locale.US, "%-4.2f", GSTParamST.getPAR1());
				    System.out.printf("*Xt-1 + ");
				    System.out.format(Locale.US, "%-4.2f", GSTParamST.getPAR2());
				    System.out.printf("*Xt-2 + at() *********");
				} else if (GSTParamST.getTipoST() == GSTLabels.MA1)
				{
				    System.out.printf("**** GENERATING TS MOVING AVERAGE X(t) = ");
				    System.out.format(Locale.US, "%-4.2f", dConstante);
				    System.out.printf(" + at - ");
				    System.out.format(Locale.US, "%-4.2f", GSTParamST.getPAR1());
				    System.out.printf("*at-1  *********");
				} else if (GSTParamST.getTipoST() == GSTLabels.MA2)
				{
				    System.out.printf("**** GENERATING TS MOVING AVERAGE X(t) = ");
				    System.out.format(Locale.US, "%-4.2f", dConstante);
				    System.out.printf(" + at - ");
				    System.out.format(Locale.US, "%-4.2f", GSTParamST.getPAR1());
				    System.out.printf("*at-1 - ");
				    System.out.format(Locale.US, "%-4.2f", GSTParamST.getPAR2());
				    System.out.printf("*at-2  *********");
				} else if (GSTParamST.getTipoST() == GSTLabels.ARMA1)
				{
				    System.out.printf("**** GENERATING TS AUTOREGRESIVE/MOVING AVERAGE X(t) X(t) = ");
				    System.out.format(Locale.US, "%-4.2f", dConstante);
				    System.out.printf(" + at + ");
				    System.out.format(Locale.US, "%-4.2f", GSTParamST.getPAR1());
				    System.out.printf("*Xt-1 - ");
				    System.out.format(Locale.US, "%-4.2f", GSTParamST.getPAR2());
				    System.out.printf("*at-1  *********");
				}
			    System.out.println();
			    System.out.println();

			    System.out.println("---BEGINNING---");

			    // Imprime a ST em tela

			    for (int idx = iPonteiroInicio; idx < iPonteiroInicio + GSTParamST.getNoAmostra(); ++idx)
				{
				    System.out.format(Locale.US, "%8.3f%n", numbers[idx]);
				    GSTParamST.setNumbersST(idx - iPonteiroInicio, numbers[idx]);
				}

			    System.out.println("---END---");
			    System.out.println();
			    System.out.println("            --- REPORT ---");

			    if (GSTParamST.getTipoST() == GSTLabels.AR1 || GSTParamST.getTipoST() == GSTLabels.AR2)
				{
				    System.out.printf("PHI_1 input                 ---> ");
				    System.out.format(Locale.US, "%-6.3f%n", GSTParamST.getPAR1());
				    System.out.printf("PHI_1 calculated (E<=");
				    System.out.format(Locale.US, "%-3.1f%%", this.ERROMAX);
				    System.out.printf(")  ---> ");
				    System.out.format(Locale.US, "%-6.3f%n", dPar1Estimado);
				}

			    if (GSTParamST.getTipoST() == GSTLabels.AR2)
				{
				    System.out.printf("PHI_2 input                 ---> ");
				    System.out.format(Locale.US, "%-6.3f%n", GSTParamST.getPAR2());
				    System.out.printf("PHI_2 calculated (E<=");
				    System.out.format(Locale.US, "%-3.1f%%", this.ERROMAX);
				    System.out.printf(")  ---> ");
				    System.out.format(Locale.US, "%-6.3f%n", dPar2Estimado);
				}
			    if (GSTParamST.getTipoST() == GSTLabels.MA1 || GSTParamST.getTipoST() == GSTLabels.MA2
				    || GSTParamST.getTipoST() == GSTLabels.ARMA1)
				{
				    if (GSTParamST.getTipoST() == GSTLabels.MA1
					    || GSTParamST.getTipoST() == GSTLabels.MA2)
					{
					    System.out.printf("TETA_1 input                ---> ");
					    System.out.format(Locale.US, "%-6.3f%n", GSTParamST.getPAR1());
					} else if (GSTParamST.getTipoST() == GSTLabels.ARMA1)
					{
					    System.out.printf("PHI_1 input                 ---> ");
					    System.out.format(Locale.US, "%-6.3f%n", GSTParamST.getPAR1());
					    System.out.printf("TETA_1 input                ---> ");
					    System.out.format(Locale.US, "%-6.3f%n", GSTParamST.getPAR2());
					}

				}
			    if (GSTParamST.getTipoST() == GSTLabels.MA2)
				{
				    System.out.printf("TETA_2 input                ---> ");
				    System.out.format(Locale.US, "%-6.3f%n", GSTParamST.getPAR2());
				}

			    System.out.printf("Sample size                 ---> ");
			    System.out.format(Locale.US, "%d%n", GSTParamST.getNoAmostra());
			    System.out.printf("Mean estimated from sample  ---> ");
			    System.out.format(Locale.US, "%-6.2f%n", dMediaCalcST);
			    System.out.printf("Mean input                  ---> ");
			    System.out.format(Locale.US, "%-6.2f%n", GSTParamST.getValorMedio());
			    System.out.printf("A(t) variance estimated     ---> ");
			    System.out.format(Locale.US, "%-6.2f%n", this.dVarRuidoCalc);
			    System.out.printf("A(t) variance input         ---> ");
			    System.out.format(Locale.US, "%-6.2f%n", GSTParamST.getVarRuido());

			    if (GSTParamST.getTipoST() == GSTLabels.AR1 || GSTParamST.getTipoST() == GSTLabels.AR2)
				{
				    System.out.printf("Correlation k=1             ---> ");
				    System.out.format(Locale.US, "%-6.3f%n", vetCorrela[1]);
				}

			    if (GSTParamST.getTipoST() == GSTLabels.MA1 || GSTParamST.getTipoST() == GSTLabels.MA2
				    || GSTParamST.getTipoST() == GSTLabels.ARMA1)
				{
				    if (GSTParamST.getTipoST() == GSTLabels.MA1)
					{
					    System.out.printf("Corr. k=1 (from TETA_1)     ---> ");
					} else if (GSTParamST.getTipoST() == GSTLabels.MA2)
					{
					    System.out.printf("Corr. k=1 (from TETA_1/2)   ---> ");
					} else if (GSTParamST.getTipoST() == GSTLabels.ARMA1)
					{
					    System.out.printf("Corr. k=1 (from TETA/PHI)   ---> ");
					}

				    System.out.format(Locale.US, "%-6.3f%n", dPar1Estimado);

				    if (GSTParamST.getTipoST() == GSTLabels.ARMA1)
					{
					    System.out.printf("Corr. k=1 sample   (E<=");
					    System.out.format(Locale.US, "%-3.1f%%", this.ERROMAX);
					    System.out.printf(")---> ");
					    System.out.format(Locale.US, "%-6.3f%n", vetCorrela[1]);
					} else // MA(1) e (2)
					{
					    System.out.printf("Corr. k=1 sample  (E<=");
					    System.out.format(Locale.US, "%-3.1f%%", this.ERROMAX);
					    System.out.printf(") ---> ");
					    System.out.format(Locale.US, "%-6.3f%n", vetCorrela[1]);
					}
				}

			    if (GSTParamST.getTipoST() == GSTLabels.AR1 || GSTParamST.getTipoST() == GSTLabels.AR2)
				{
				    System.out.printf("Correlation k=2             ---> ");
				    System.out.format(Locale.US, "%-6.3f%n", vetCorrela[2]);
				}

			    if (GSTParamST.getTipoST() == GSTLabels.MA2)
				{
				    System.out.printf("Corr. k=2 (from TETA_1/2)   ---> ");
				    System.out.format(Locale.US, "%-6.3f%n", dPar2Estimado);
				    System.out.printf("Corr. k=2 sample  (E<=");
				    System.out.format(Locale.US, "%-3.1f%%", this.ERROMAX);
				    System.out.printf(") ---> ");
				    System.out.format(Locale.US, "%-6.3f%n", vetCorrela[2]);
				} else if (GSTParamST.getTipoST() == GSTLabels.ARMA1)
				{
				    System.out.printf("Corr. k=2 (from TETA/PHI)   ---> ");
				    System.out.format(Locale.US, "%-6.3f%n", dPar2Estimado);
				    System.out.printf("Corr. k=2  sample  (E<=");
				    System.out.format(Locale.US, "%-3.1f%%", this.ERROMAX);
				    System.out.printf(")---> ");
				    System.out.format(Locale.US, "%-6.3f%n", vetCorrela[2]);
				}
			    System.out.printf("Kurtosis                    ---> ");
			    System.out.format(Locale.US, "%-6.3f%n", dkurtosis);
			    System.out.printf("Skewness                    ---> ");
			    System.out.format(Locale.US, "%-6.3f%n", dskewness);
			    System.out.println();
			    System.out.println("***********************************************************");
			}

		    // System.out.println("DEBUG> iConseguiuST "+iConseguiuST);

		    iPonteiroRelativo++;
		    // System.out.println("DEBUG> iPonteiroRelativo " + iPonteiroRelativo);

		    if (iPonteiroInicio + GSTParamST.getNoAmostra() >= this.iFatorMult * GSTParamST.getNoAmostra())
			{
			    // System.out.println("DEBUG> iPonteiroRelativo � -2!!! " + iPonteiroRelativo);
			    iConseguiuST = -2; // chegou no fim da amostra, retorna
			}
		}

	    return iConseguiuST;
	}

    @Override
    public void actionPerformed(ActionEvent ae)
	{

	    if (ae.getActionCommand() == botaoCancelar)

		{
		    // this.dispose();
		    SwingUtilities.getWindowAncestor(this).dispose();

		} else if (ae.getActionCommand() == botaoGeraGrafico)

		{

		    EventQueue.invokeLater(new Runnable()
			{
			    @Override
			    public void run()
				{
				    try
					{
					    Window parentWindow = SwingUtilities
						    .windowForComponent(GSTConsolePanel.this.buttonGrafico);
					    GSTGeraGrafico telaGraficoleGST = new GSTGeraGrafico(parentWindow, true);
					    telaGraficoleGST.pack();
					    telaGraficoleGST.setVisible(true);

					} catch (Exception e)
					{
					    e.printStackTrace();
					}
				}
			});

		} else if (ae.getActionCommand() == botaoSalvaResultado)
		{

		    JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel Files", "xls");
		    chooser.setFileFilter(filter);
		    String strNomeArq = null;
		    String strTipoST = null;
		    int returnVal = chooser.showSaveDialog(SwingUtilities.windowForComponent(this.buttonResultado));

		    if (returnVal == JFileChooser.APPROVE_OPTION)
			{
			    // System.out.println("DEBUG> You chose to save this file: " +
			    // chooser.getSelectedFile().getName() +" at " +
			    // chooser.getSelectedFile().getAbsolutePath());
			    strNomeArq = chooser.getSelectedFile().getAbsolutePath();
			    boolean bcontemExtensao = false;
			    bcontemExtensao = strNomeArq.contains(".xls");

			    if (!bcontemExtensao)
				{
				    strNomeArq = strNomeArq + ".xls";
				}

			    if (GSTParamST.getTipoST() == GSTLabels.AR1)
				{
				    strTipoST = "AR(1) time series PHI_1= " + GSTParamST.getPAR1();
				} else if (GSTParamST.getTipoST() == GSTLabels.AR2)
				{
				    strTipoST = "AR(2) time series PHI_1= " + GSTParamST.getPAR1() + " PHI_2="
					    + GSTParamST.getPAR2();
				} else if (GSTParamST.getTipoST() == GSTLabels.MA1)
				{
				    strTipoST = "MA(1) time series TETA_1= " + GSTParamST.getPAR1();
				} else if (GSTParamST.getTipoST() == GSTLabels.MA2)
				{
				    strTipoST = "MA(2) time series TETA_1= " + GSTParamST.getPAR1() + " TETA_2="
					    + GSTParamST.getPAR2();
				} else if (GSTParamST.getTipoST() == GSTLabels.ARMA1)
				{
				    strTipoST = "ARMA(1) time series PHI_1= " + GSTParamST.getPAR1() + " TETA_1="
					    + GSTParamST.getPAR2();
				}
			    strTipoST = strTipoST + " Mean =" + GSTParamST.getValorMedio();
			    strTipoST = strTipoST + " Variance =" + GSTParamST.getVarRuido();

			    File fArquivo = new File(strNomeArq);
			    int reply = JOptionPane.YES_OPTION;
			    if (fArquivo.isFile())
				{
				    reply = JOptionPane.showConfirmDialog(null,
					    "File " + strNomeArq + " already exists, override ?", "WARNING",
					    JOptionPane.YES_NO_OPTION);
				}
			    if (reply == JOptionPane.YES_OPTION)
				{
				    try
					{
					    Workbook wb = new HSSFWorkbook();
					    org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("Time Series");
					    FileOutputStream fileOut = new FileOutputStream(strNomeArq);
					    // Create a row and put some cells in it. Rows are 0 based.
					    Row row = sheet.createRow((short) 0);
					    // Create a cell and put a value in it.
					    Cell cell = row.createCell(0); // linha 1 vai titulo
					    cell.setCellValue(strTipoST);
					    // format US local
					    NumberFormat usFormat = NumberFormat.getInstance(new Locale("en", "US"));
					    // linha 2 em diante vai a serie
					    for (int idx = 1; idx <= GSTParamST.getNoAmostra(); idx++)
						{
						    row = sheet.createRow((short) idx);
						    cell = row.createCell(0);
						    cell.setCellValue(
							    usFormat.format(GSTParamST.getNumbersST(idx - 1)));
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