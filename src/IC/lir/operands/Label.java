package IC.lir.operands;

public class Label implements Operand{
	private String name;
	
	public Label(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}
}
