package IC.lir.lirObject;

import IC.lir.operands.Label;

public class LIRStringLiteral {
	private Label label;
	private String literal;
	
	public LIRStringLiteral(Label label, String literal) {
		super();
		this.label = label;
		this.literal = literal;
	}

	public Label getLabel() {
		return label;
	}

	public String getLiteral() {
		return literal;
	}

	@Override
	public String toString() {
		return label.toString();
	}
}
