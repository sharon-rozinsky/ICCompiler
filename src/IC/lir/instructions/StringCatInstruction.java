package IC.lir.instructions;

import IC.lir.operands.LibraryLabel;
import IC.lir.operands.Operand;
import IC.lir.operands.Register;

public class StringCatInstruction extends LibraryInstruction {

	private static final String InstructionName = "stringCat";
    private static final LibraryLabel function = new LibraryLabel(InstructionName);
    
    public StringCatInstruction(Register dest, Operand leftParam, Operand rightParam) {
        super(function, dest, leftParam, rightParam);
    }
}
