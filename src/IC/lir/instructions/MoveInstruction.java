package IC.lir.instructions;

import IC.lir.operands.Memory;
import IC.lir.operands.Operand;
import IC.lir.operands.Register;

public class MoveInstruction extends Instruction {

    private static final String InstructionName = "Move";
    private Operand op1;
    private Operand op2;
    
    public MoveInstruction(Operand op1, Memory memory) {
        super(InstructionName);
        this.op1 = op1;
        this.op2 = memory;
    }
    
    public MoveInstruction(Operand op1, Register reg) {
        super(InstructionName);
        this.op1 = op1;
        this.op2 = reg;
    }
    
    public Operand getOp1() {
        return op1;
    }

    public Operand getOp2() {
        return op2;
    }
    
    @Override
    public String toString() {
        return getName() + " " + op1 + "," + op2;
    }
}
