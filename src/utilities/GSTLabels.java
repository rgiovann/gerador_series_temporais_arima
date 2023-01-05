package utilities;

/*
 *  HISTÓRICO
 *  V1.0 - release liberado
 *  V1.1 - pergunta agora antes de sobre-escrever se arquivo j� existe.
 *  V1.2 - translated to English
 */

public interface GSTLabels {

    public static String janelaPrincipal = "Time series generator V1.2";

    public static String menuItemGeraST = "Generate time series";

    public static String menuItemSair = "Exit";

    public static String menuItemHelp = "How it works";

    public static String botaoGravar = "Generate TS";
    public static String botaoCancelar = "Cancel";
    public static String botaoSalvaResultado = "Save to file";
    public static String botaoGeraGrafico = "Display chart";
    public static int NUMMAXAMOSTRAS = 250;
    public static int NUMMINAMOSTRAS = 50;

    final static int AR1 = 0;
    final static int AR2 = 1;
    final static int MA1 = 2;
    final static int MA2 = 3;
    final static int ARMA1 = 4;

}
