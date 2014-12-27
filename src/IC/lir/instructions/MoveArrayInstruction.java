package IC.lir.instructions;

import IC.lir.LirConstants;

public class MoveArrayInstruction extends Instruction {
	
	private static final String InstructionName = "MoveArray";
    private ArrayPair pair;
    private Operand op;
    private LirConstants memOp;
	
	public MoveArrayInstruction(ArrayPair pair, Operand op, LirConstants memOp) {
        super(InstructionName);
        this.pair = pair;
        this.op = op;
        this.memOp = memOp;
    }

    public ArrayPair getPair() {
        return pair;
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
            return getName() + " " + pair + "," + op; 
        } else {
            return getName() + " " + op + "," + pair;
        }
    }
}
