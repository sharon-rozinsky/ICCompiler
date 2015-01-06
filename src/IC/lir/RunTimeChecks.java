package IC.lir;

public class RunTimeChecks {
	
	public static String addCheckNullRefInstruction(String reg) {
		return "StaticCall __checkNullRef(a=" + reg + "),Rdummy\n";
	}

	public static String addCheckArrayAccessInstruction(String arr, String index) {
		return "StaticCall __checkArrayAccess(a=" + arr + ", i=" + index + "),Rdummy\n";
	}

	public static String addCheckSizeInstruction(String size) {
		return "StaticCall __checkSize(n=" + size + "),Rdummy\n";
	}

	public static String addCheckZeroInstruction(String reg) {
		return "StaticCall __checkZero(b=" + reg + "),Rdummy\n";
	}
	
}
