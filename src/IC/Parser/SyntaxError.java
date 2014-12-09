package IC.Parser;

import java_cup.sym;

public class SyntaxError extends Exception {
	private String message;
	
	public SyntaxError(String error){
		super(error);
		message = error;
	}
}
