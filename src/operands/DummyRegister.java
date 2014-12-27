package operands;

public class DummyRegister extends Register {
	
	@Override
	public int getCurrentRegCount() {
		return -1;
	}
	
	public String toString(){
		return "Rdummy";
	}
}
