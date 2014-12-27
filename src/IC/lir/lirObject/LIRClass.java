package IC.lir.lirObject;

import java.util.ArrayList;
import java.util.List;

import IC.lir.operands.AddressLabel;

public class LIRClass {
	private AddressLabel className;
	private List<LIRMethod> methods = new ArrayList<LIRMethod>();
	
	public LIRClass(AddressLabel className) {
		this.className = className;
	}

	public AddressLabel getClassName() {
		return className;
	}

	public List<LIRMethod> getMethods() {
		return methods;
	}
}
