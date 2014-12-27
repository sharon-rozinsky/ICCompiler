package IC.lir.instructions;

import java.util.ArrayList;
import java.util.List;
import IC.lir.operands.Label;
import IC.lir.operands.LibraryLabel;
import IC.lir.operands.Operand;
import IC.lir.operands.Register;

public class LibraryInstruction extends Instruction {

    private static final String InstructionName = "Library";
    private List<Operand> params = new ArrayList<Operand>();
    private LibraryLabel function;
    private Register dest;
    
    public LibraryInstruction(LibraryLabel functionName, Register dest, Operand ... params) {
        super(InstructionName);
        this.function = functionName;
        this.dest = dest;
        for (Operand param : params) {
        	this.params.add(param);
        }
    }

    public List<Operand> getParams() {
        return params;
    }

    public Label getFunction() {
        return function;
    }

    public Register getDest() {
        return dest;
    }
    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        sb.append(getName() + " " + function + "(");
        
        if (params.size() > 0) {
            for (Operand param : params) {
                sb.append(param.toString() + ",");
            }
            sb.setLength(sb.length()-1);
        }
        
        sb.append(")," + dest);
        
        return sb.toString();
    }
}
