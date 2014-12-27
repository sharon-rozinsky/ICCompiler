package IC.lir.instructions;

import java.util.ArrayList;
import java.util.List;

import IC.lir.operands.Immediate;
import IC.lir.operands.ParameterOperand;
import IC.lir.operands.Register;

public class VirtualCallInstruction extends Instruction {

	private static final String InstructionName = "VirtualCall";
    private List<ParameterOperand> params = new ArrayList<ParameterOperand>();
    private Register location;
    private Immediate offset;
    private Register dest;
    
    public VirtualCallInstruction(Register location, Immediate offset, Register dest, ParameterOperand ... params) {
        super(InstructionName);
        this.location = location;
        this.offset = offset;
        this.dest = dest;
        for (ParameterOperand param : params) {
        	this.params.add(param);
        }
    }

    public List<ParameterOperand> getParameters() {
        return params;
    }

    public Register getLocation() {
        return location;
    }

    public Immediate getOffset() {
        return offset;
    }

    public Register getDest() {
        return dest;
    }
    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        sb.append(getName() + " " + location + "." + offset + "(");
        
        if (params.size() > 0) {
            for (ParameterOperand pair : params) {
                sb.append(pair.toString() + ",");
            }
            sb.setLength(sb.length()-1);
        }
        
        sb.append(")," + dest);
        
        return sb.toString();
    }
}
