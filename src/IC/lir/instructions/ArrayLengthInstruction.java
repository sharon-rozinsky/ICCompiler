package IC.lir.instructions;

public class ArrayLengthInstruction extends Instruction {
	
    private static final String InstructionName = "Array_Length";
    private Operand array;
    private Reg dest;
    
    public ArrayLengthInstruction(Memory memory, Reg dest) {
        super(InstructionName);
        this.array = memory;
        this.dest = dest;
    }
    
    public ArrayLengthInstruction(Reg arrReg, Reg dest) {
        super(InstructionName);
        this.array = arrReg;
        this.dest = dest;
    }

    public Operand getArray() {
        return array;
    }

    public Reg getDest() {
        return dest;
    }

    @Override
    public String toString() {
        return getName() + " " + array + "," + dest;
    }
    
}
}
