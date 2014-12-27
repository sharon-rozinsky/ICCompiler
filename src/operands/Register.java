package operands;

public class Register implements Operand{
	protected int currentRegCount;
	protected static int regCount = 0;
	
	public Register(){
		this.currentRegCount = regCount;
	}
	
	public Register(int offset){
		if(regCount + offset < 0){
			throw new RuntimeException("Invalid register count - cannot be negative");
		}
		this.currentRegCount = regCount + offset;
	}

	public int getCurrentRegCount() {
		return currentRegCount;
	}

	public void setCurrentRegCount(int currentRegCount) {
		this.currentRegCount = currentRegCount;
	}

	public static int getRegCount() {
		return regCount;
	}

	public static void setRegCount(int regCount) {
		Register.regCount = regCount;
	}
	
	public static void incRegisterCounter(int size){
		setRegCount(regCount + size);
	}
	
	public static void decRegisterCounter(int size){
		if(regCount - size < 0){
			throw new RuntimeException("Invalid register count - cannot be negative");
		}
		setRegCount(regCount - size);
	}
	
	public String toString(){
		return "R" + currentRegCount;
	}
}
