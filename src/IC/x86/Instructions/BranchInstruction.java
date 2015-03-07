package IC.x86.Instructions;

import IC.lir.operands.Label;

public class BranchInstruction extends X86Instruction {
	
	private static final String InstructionName = "Jump";
    private Label label;
    private String condition;
    
    public BranchInstruction(String condition, Label label) {
        super(InstructionName + condition);
        this.label = label;
        this.condition = condition;
    }

    public Label getLabel() {
        return label;
    }

    public String getCondition() {
        return condition;
    }
    
    @Override
    public String toString() {
    	String x86Op = "";
		switch(condition){
		case "Jump":
			x86Op = "jmp";
			break;
		case "JumpTrue":
			x86Op = "je";
			break;
		case "JumpFalse":
			x86Op = "jne";
			break;
		case "JumpG":
			x86Op = "jg";
			break;
		case "JumpGE":
			x86Op = "jge";
			break;
		case "JumpL":
			x86Op = "jl";
			break;
		case "JumpLE":
			x86Op = "jle";
			break;	
		}
		
		String x86Code = "# jump" + condition + "\n";;		
		x86Code += "\t " + x86Op + " _" + label + "\n";
		return x86Code;
    }
}
