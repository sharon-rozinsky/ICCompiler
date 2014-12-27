package IC.lir.instructions;

import IC.lir.LIRConstants;
import IC.lir.operands.FieldOperand;
import IC.lir.operands.Operand;


public class MoveFieldInstruction extends Instruction {

    private static final String InstructionName = "MoveField";
    private FieldOperand fieldOp;
    private Operand op;
    private LIRConstants memOp;
    
    public MoveFieldInstruction(FieldOperand fieldOp, Operand op, LIRConstants memOp) {
        super(InstructionName);
        this.fieldOp = fieldOp;
        this.op = op;
        this.memOp = memOp;
    }
    
    public FieldOperand getfieldOp() {
        return fieldOp;
    }

    public Operand getOp() {
        return op;
    }

    public LIRConstants getMemOp() {
        return memOp;
    }
    
    @Override
    public String toString() {
        if (memOp.toString() == "Load") {
            return getName() + " " + fieldOp + "," + op; 
        } else {
            return getName() + " " + op + "," + fieldOp;
        }
    }
	
}
