package IC.lir.instructions;

import java.util.ArrayList;
import java.util.List;

import IC.lir.operands.AddressLabel;
import IC.lir.operands.ParameterOperand;
import IC.lir.operands.Register;

public class StaticCallInstruction extends Instruction {

	private static final String InstructionName = "StaticCall";
	private List<ParameterOperand> params = new ArrayList<ParameterOperand>();
	private AddressLabel function;
	private Register dest;

	public StaticCallInstruction(AddressLabel functionName, Register dest, ParameterOperand ... params) {
		super(InstructionName);
		this.function = functionName;
		this.dest = dest;
		for (ParameterOperand param : params) {
			this.params.add(param);
		}
	}

	public List<ParameterOperand> getParameters() {
		return params;
	}

	public AddressLabel getFunction() {
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
			for (ParameterOperand param : params) {
				sb.append(param.toString() + ",");
			}
			sb.setLength(sb.length()-1);
		}

		sb.append(")," + dest);

		return sb.toString();
	}
}
