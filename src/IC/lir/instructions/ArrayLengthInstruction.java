package IC.lir.instructions;

import IC.lir.operands.Operand;
import IC.lir.operands.*;

public class ArrayLengthInstruction extends Instruction {
	
    private static final String InstructionName = "Array_Length";
    private Operand array;
    private Register dest;
    
    public ArrayLengthInstruction(Memory memory, Register dest) {
        super(InstructionName);
        this.array = memory;
        this.dest = dest;
    }
    
    public ArrayLengthInstruction(Register arrReg, Register dest) {
        super(InstructionName);
        this.array = arrReg;
        this.dest = dest;
    }

    public Operand getArray() {
        return array;
    }

    public Register getDest() {
        return dest;
    }

    @Override
    public String toString() {
        return getName() + " " + array + "," + dest;
    }
}
