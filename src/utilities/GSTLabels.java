package utilities;

/*
 *  HIST�RICO
 *  V1.0 - release liberado
 *  V1.1 - pergunta agora antes de sobre-escrever se arquivo j� existe.
 */

public interface GSTLabels {
	
	public static String  janelaPrincipal     = "Gerador de Séries Temporais V1.1";
	public static String  janelaCriarRNA      = "Gerar Série Temporal" ;
	public static String  janelaAtualizarRNA  = "ALTERAR REDE NEURAL ARTIFICIAL" ;
	public static String janelatabelaRNA      = "TABELA CADASTRO REDES NEURAIS";
	
	public static String  menuItemGeraST      = "Gerar Série Temporal";	
	public static  String menuItemSelecionar  = "Selecionar RNA";
	public static  String menuItemSair 	      = "Sair";
	public static  String menuItemConfDados   = "Diretório de Saída";
	public static  String menuItemSobre       = "Sobre este programa";
	public static  String menuItemHelp        =  "Como funciona";
	
	public static String  botaoGravar  		  = "Calcula ST";
	public static String  botaoCancelar       = "Sair";
	public static String  botaoSalvaResultado = "Grava em Arquivo";
	public static String  botaoGeraGrafico    = "Mostra Gráfico";
	public static int      NUMMAXAMOSTRAS      = 250;
	public static int      NUMMINAMOSTRAS      = 50;


	public static    String botaoINCLUIR  = "INCLUIR";
	public static    String botaoALTERAR  = "ALTERAR";
	public static    String botaoEXCLUIR  = "EXCLUIR";
	public static    String botaoSAIR     = "CANCELA E SAI";
	public static    String botaoGO       = "GO...";
	
	public static String  campoNomeRNA             = "rna_name";
	public static String  campoLayersEntrada       = "rna_entrada";
	public static String  campoLayersEscondido     = "rna_escondido";
	public static String  campoLayersSaida         = "rna_saida";
	public static String  campoValorTeta           = "rna_teta";	
	public static String  campoValorGama           = "rna_gamma";	
	public static String  campoValorAlfa           = "rna_alpha";
	public static String  tabelaCadastroRNA        = "rnaprofile";
	final static int AR1 	= 0;
	final static int AR2 	= 1;
	final static int MA1 	= 2;
	final static int MA2 	= 3;
	final static int ARMA1 	= 4;
	/*
	 CREATE TABLE rnaprofile
	(
  	rna_name character(30) NOT NULL,
  	rna_entrada integer,
  	rna_escondido integer,
  	rna_saida integer,
  	rna_teta numeric(5,3),
  	rna_gamma numeric(5,3),
  	rna_alpha numeric(5,3),
  	CONSTRAINT pk_rna_nome PRIMARY KEY (rna_name)
	)
	WITH (
  	OIDS=FALSE
	);
	ALTER TABLE rnaprofile
  	OWNER TO postgres;

	 * 
	 */
	public static String  comandoSqlInsert         = "INSERT INTO ";
	public static String  comandoSqlUpdate         = "UPDATE ";
	public static String  comandoSqlSelect         = "SELECT ";
	public static String  comandoSqlClausulaWhere  = "WHERE ";
	public static String  comandoSqlClausulaSet    = "SET ";
	public static String  comandoSqlClausulaFrom   = "FROM ";
	public static String  comandoSqlClausulaValues = "VALUES ";


	public static String  strOneSpace              = " ";
	public static String  strVirgula               = ",";
	public static String  strAspaSimples           = "'";
	public static String  strParentesisEsq         = "(";
	public static String  strParentesisDir         = ")";
	public static String  strInterrogacao          = "?";
	public static String  strSinalIgual            = "=";
	
	public static String  msgRegistroGravadoOK     = "O registro foi inserido com sucesso.";
	public static String  msgRegistroAtualizadoOK  = "O registro foi atualizado com sucesso.";	
	public static String  msgRegistroDuplicado     = "Nome da RNA já existe no banco de dados!";
	public static String  msgTipoErro              = "ERRO";
	public static String  msgTipoInfo              = "INFO";


	

}
