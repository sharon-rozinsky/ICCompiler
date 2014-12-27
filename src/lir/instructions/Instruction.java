package lir.instructions;

public class Instruction {
	
	private String name;
    
    public Instruction(String instructionName) {
        this.name = instructionName;
    }

    public String getName() {
        return name;
    }
}
