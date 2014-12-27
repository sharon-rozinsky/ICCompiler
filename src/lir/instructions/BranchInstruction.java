package lir.instructions;

import lir.LirConstants;

public class BranchInstruction extends Instruction {
	
	private static final String InstructionName = "Branch";
    private Label label;
    private LirConstants condition;
    
    public BranchInstruction(LirConstants condition, Label label) {
        super(InstructionName + condition.toString());
        this.label = label;
        this.condition = condition;
    }

    public Label getLabel() {
        return label;
    }

    public LirConstants getCondition() {
        return condition;
    }
    
    @Override
    public String toString() {
        return getName() + " " + label;
    }
}
