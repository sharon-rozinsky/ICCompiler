package IC.lir;

public class LIRError extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8265374664307982268L;

	LIRError(String msg)
	{
		super(String.format("Runtime Error: %s!",msg));
	}
}
