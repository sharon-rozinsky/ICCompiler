package IC.lir.instructions;

import IC.lir.operands.Operand;

public class ReturnInstruction extends Instruction {

	private static final String InstructionName = "Return";
    private Operand returnParam;
    
    public ReturnInstruction(Operand parameter) {
        super(InstructionName);
        this.returnParam = parameter;
    }

    public Operand getParameter() {
        return returnParam;
    }
    
    @Override
    public String toString() {
        return getName() + " " + returnParam;
    }
    
}
