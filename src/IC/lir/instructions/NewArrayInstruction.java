package IC.lir.instructions;

import IC.lir.operands.LibraryLabel;
import IC.lir.operands.Operand;
import IC.lir.operands.Register;

public class NewArrayInstruction extends LibraryInstruction {

	private static final String InstructionName = "allocateArray";
    private static final LibraryLabel function = new LibraryLabel(InstructionName);
    
    public NewArrayInstruction(Register dest, Operand numberOfElements) {
        super(function, dest, numberOfElements);
    }
}
