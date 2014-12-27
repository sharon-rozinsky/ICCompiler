package IC.lir;

import IC.lir.operands.AddressLabel;

public class Offset {
	private int offset;
	private AddressLabel classLabel;
	private String className;
	
	public Offset(int offset, String className) {
		this.offset = offset;
		this.className = className;
		this.classLabel = new AddressLabel(className);
	}

	public int getOffset() {
		return offset;
	}

	public AddressLabel getClassLabel() {
		return classLabel;
	}

	public String getClassName() {
		return className;
	}
}
