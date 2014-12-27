package IC.lir.instructions;

import IC.lir.LIRConstants;
import IC.lir.operands.Label;

public class BranchInstruction extends Instruction {
	
	private static final String InstructionName = "Branch";
    private Label label;
    private LIRConstants condition;
    
    public BranchInstruction(LIRConstants condition, Label label) {
        super(InstructionName + condition.toString());
        this.label = label;
        this.condition = condition;
    }

    public Label getLabel() {
        return label;
    }

    public LIRConstants getCondition() {
        return condition;
    }
    
    @Override
    public String toString() {
        return getName() + " " + label;
    }
}
