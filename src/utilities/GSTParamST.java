package utilities;

public class GSTParamST {
	
	private static double dPAR1, dPAR2;
	private static double dValorMedio;
	private static double dVarRuido;
	private static int iNoAmostra;
	private static int iTipoST;
	private static double[] vetNumbersST = new double[250]; 
	
	GSTParamST()
	{
		dPAR1=0;
		dPAR2=0;
		dVarRuido=0;
		iNoAmostra=0;
		dValorMedio=0;
		iTipoST=0;
		for(int i=1;i<250;i++)
		{vetNumbersST[i]=0;}
	}

	public static int getTipoST()
	{
		return iTipoST;
	}
	public static double getPAR1()
	{
		return dPAR1;
	}
	
	public static double getPAR2()
	{
		return dPAR2;
	}
	
	public static double getVarRuido()
	{
		return dVarRuido;
	}
	
	public static double getValorMedio()
	{
		return dValorMedio;
	}
	
	public static int getNoAmostra()
	{
		return iNoAmostra;
	
	}
	
	public static void setTipoST(int par)
	{
		iTipoST=par;
	}
	public static void setPAR1(double par)
	{
		dPAR1=par;
	}
	
	public static void setPAR2(double par)
	{
		dPAR2=par;
	}
	
	public static void  setVarRuido(double par)
	{
		dVarRuido=par;
	}
	
	public static void setValorMedio(double par)
	{
		dValorMedio=par;
	}
	
	public static void setNoAmostra(int par)
	{
		iNoAmostra=par;
	
	}
	
	public static void setNumbersST(int idx, double par)
	{
		vetNumbersST[idx]=par;
	
	}
	
	public static double getNumbersST(int idx)
	{
		return vetNumbersST[idx];
	
	}
	
	
}
