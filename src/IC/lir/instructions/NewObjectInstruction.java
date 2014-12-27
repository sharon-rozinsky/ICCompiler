package IC.lir.instructions;

import IC.lir.operands.Immediate;
import IC.lir.operands.LibraryLabel;
import IC.lir.operands.Register;

public class NewObjectInstruction extends LibraryInstruction {

	private static final String InstructionName = "allocateObject";
    private static final LibraryLabel function = new LibraryLabel(InstructionName);
    
    public NewObjectInstruction(Register dest, Immediate byteSize) {
        super(function, dest, byteSize);
    }
    
}
