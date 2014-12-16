package IC.SemanticChecks;

public class SemanticError extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1577148699654799621L;
	
	SemanticError(int line,String msg)
	{
		super(String.format("semantic error at line %d: %s", line,msg));
	}

}
