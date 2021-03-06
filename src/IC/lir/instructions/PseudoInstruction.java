package IC.lir.instructions;

import IC.lir.operands.AddressLabel;


public class PseudoInstruction extends Instruction {
	// Not a real instruction- used to represent a label between instructions or comments
	private static final String InstructionName = "PseudoInstruction";
    private AddressLabel label;
    private String type;
    
    public PseudoInstruction(AddressLabel label, String type) {
        super(InstructionName);
        this.label = label;
        this.type = type;
    }

    @Override
    public String toString() {
        switch(type) {
            case "Comment":
                return  "# " + label.toString();
            case "Label":
                return label.toString() + ":";
            default: 
                throw new IllegalStateException("This type is not defined");
        }
        
    }
    
    public AddressLabel getLabel() {
        return label;
    }
}
