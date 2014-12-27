package IC.lir.instructions;

import IC.lir.LirConstants;
import IC.lir.operands.ArrayOperand;
import IC.lir.operands.Operand;

public class MoveArrayInstruction extends Instruction {
	
	private static final String InstructionName = "MoveArray";
    private ArrayOperand arrParam;
    private Operand op;
    private LirConstants memOp;
	
	public MoveArrayInstruction(ArrayOperand arrParam, Operand op, LirConstants memOp) {
        super(InstructionName);
        this.arrParam = arrParam;
        this.op = op;
        this.memOp = memOp;
    }

    public ArrayOperand getarrParam() {
        return arrParam;
    }

    public Operand getOp() {
        return op;
    }

    public LirConstants getMemOp() {
        return memOp;
    }
    
    @Override
    public String toString() {
        if (memOp.toString() == "Load") {
            return getName() + " " + arrParam + "," + op; 
        } else {
            return getName() + " " + op + "," + arrParam;
        }
    }
}
