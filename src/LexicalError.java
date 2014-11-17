
@SuppressWarnings("serial")
public class LexicalError extends Exception{
	String errMsg;
	
	public LexicalError(String error){
		errMsg = error;
	}
	
	
	public static void toString(String errMsg) { 
		System.err. println ("Error!\t"+errMsg);
	}

}
