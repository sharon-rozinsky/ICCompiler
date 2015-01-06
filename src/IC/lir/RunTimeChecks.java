package IC.lir;

public class RunTimeChecks {
	
	private static boolean addCheckNullRefFunction = false;
	private static boolean addCheckArrayAccessFunction = false;
	private static boolean addCheckSizeFunction = false;
	private static boolean addCheckZeroFunction = false;
	
	public static String addCheckNullRefInstruction(String reg) {
		addCheckNullRefFunction = true;
		return "StaticCall __checkNullRef(a=" + reg + "),Rdummy\n";
	}

	public static String addCheckArrayAccessInstruction(String arr, String index) {
		addCheckArrayAccessFunction = true;
		return "StaticCall __checkArrayAccess(a=" + arr + ", i=" + index + "),Rdummy\n";
	}

	public static String addCheckSizeInstruction(String size) {
		addCheckSizeFunction = true;
		return "StaticCall __checkSize(n=" + size + "),Rdummy\n";
	}

	public static String addCheckZeroInstruction(String reg) {
		addCheckZeroFunction = true;
		return "StaticCall __checkZero(a=" + reg + "),Rdummy\n";
	}
	
	public static void addRunTimeCheckFunctions(){
		if(addCheckNullRefFunction){
			
		}
		
		if(addCheckArrayAccessFunction){
			
		}
		
		if(addCheckSizeFunction){
			
		}
		
		if(addCheckZeroFunction){
			
		}
	}
}
