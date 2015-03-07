package IC.x86.Instructions;

public class X86Instruction {
	
	private String name;
    
    public X86Instruction(String instructionName) {
        this.name = instructionName;
    }

    public String getName() {
        return name;
    }
}
