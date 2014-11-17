
public class LexicalError extends Exception{
	
	public LexicalError(String error){
		PrintTokenError(error);
	}
	
	
	public static void PrintTokenError(String errMsg) { 
		System.err. println ("Error!\t"+errMsg);
	}

}
