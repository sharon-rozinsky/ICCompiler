package lir.instructions;

import lir.LirConstants;

public class BinaryInstruction extends Instruction {

	private Operand op1;
    private Reg op2;
    private LirConstants operation;
    
    public BinaryInstruction(LirConstants operation, Operand op1, Reg op2) {
        super(operation.toString());
        this.op1 = op1;
        this.op2 = op2;
        this.operation = operation;
    }

    public Operand getOp1() {
        return op1;
    }

    public Reg getOp2() {
        return op2;
    }

    public LirConstants getOperation() {
        return operation;
    }
    
    @Override
    public String toString() {
        return getName() + " " + op1 + "," + op2;
    }

}
