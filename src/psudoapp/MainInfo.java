package psudoapp;

public class MainInfo {
	public static void main(String[] args) 
	{
		Info inf = new Info();
		String sAns ="";
		Boolean bAns = false;
		
		sAns = inf.GetInfoFromKey("Especialidades");
		System.out.println("Especialidades = " + sAns);
		sAns = inf.GetInfoFromKey("Direccion");
		System.out.println("Direccion = " + sAns);
		sAns = inf.GetInfoFromKey("Banco de sangre");
		System.out.println("Banco de sandre = " + sAns);
		sAns = inf.GetInfoFromKey("Nada");
		System.out.println("Nada = " + sAns);
		
		bAns = inf.FindDataFromKey("Especialidades", "Urologia");
		System.out.println("Urologia = " + bAns);
		bAns = inf.FindDataFromKey("Especialidades", "Necrologia");
		System.out.println("Necrologia = " + bAns);
		bAns = inf.FindDataFromKey("Banco de sangre", "z");
		System.out.println("z = " + bAns);
		bAns = inf.FindDataFromKey("Banco de sangre", "i++");
		System.out.println("i++ = " + bAns);
		bAns = inf.FindDataFromKey("Banco de sangre", "c#");
		System.out.println("c# = " + bAns);
		
	}
}
