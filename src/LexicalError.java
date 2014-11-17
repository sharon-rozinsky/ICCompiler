
public class LexicalError extends Exception{
	private String message;
	
	public LexicalError(String error){
		super(error);
		message = error;
	}
}
