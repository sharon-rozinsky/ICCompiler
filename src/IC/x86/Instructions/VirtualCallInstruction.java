package IC.x86.Instructions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import IC.lir.operands.Immediate;
import IC.lir.operands.ParameterOperand;
import IC.lir.operands.Register;
import IC.x86.frame;

public class VirtualCallInstruction extends X86Instruction {

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
        
        int offset = params.size() * 4;
        StringBuilder sb = new StringBuilder();
        sb.append("# " + InstructionName + "\n");
        sb.append("\t # push caller-saved registers \n");
        sb.append("\t push %eax \n");
        sb.append("\t push %ecx \n");
        sb.append("\t push %edx \n");
        sb.append("\t # push parameters \n");
		
		Collections.reverse(this.params);
		for (ParameterOperand reg : params){
			if (reg.toString().charAt(0)=='R' || reg.toString().charAt(0)=='v')
				sb.append("\t movl " + frame.getVariableStackOffset(reg) + "(%ebp), %eax \n");
			else 
				sb.append("\t movl $" + reg + ", %eax \n");
			sb.append("\t push %eax \n");
		}
				
		sb.append("\t movl " + frame.getVariableStackOffset(location) + "(%ebp), %eax \n");
		sb.append("\t push %eax \n");
		sb.append("\t movl 0(%eax), %eax \n");
		sb.append("\t call *" + (offset*4)+ "(%eax) \n");
		
		if (!dest.equals("Rdummy")){
			sb.append("\t mov %eax, " + frame.getVariableStackOffset(dest) + "(%ebp) \n");
		}
		
		sb.append("\t # pop parameters \n");
		sb.append("\t add $" + offset + ", %esp \n");
		
		sb.append("\t # pop caller-saved registers \n");
		sb.append("\t pop %edx \n");
		sb.append("\t pop %ecx \n");
		sb.append("\t pop %eax \n");
		return sb.toString();
    }
}
