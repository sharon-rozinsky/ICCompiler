package IC.lir.instructions;

import IC.lir.operands.Operand;
import IC.lir.operands.*;

public class BinaryInstruction extends Instruction {

	private Operand op1;
    private Register op2;
    private String operation;
    
    public BinaryInstruction(String operation, Operand op1, Register op2) {
        super(operation.toString());
        this.op1 = op1;
        this.op2 = op2;
        this.operation = operation;
    }

    public Operand getOp1() {
        return op1;
    }

    public Register getOp2() {
        return op2;
    }

    public String getOperation() {
        return operation;
    }
    
    @Override
    public String toString() {
        return getName() + " " + op1 + "," + op2;
    }

}
