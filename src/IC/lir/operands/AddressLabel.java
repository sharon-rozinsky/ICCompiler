package IC.lir.operands;

public class AddressLabel extends Label {

	private static int labelId = 0;
	
	public AddressLabel(String name) {
		super("_" + name);
	}

	public AddressLabel(String firstLabelName, String secondLabelName) {
		super("_" + firstLabelName + "_" + secondLabelName);
	}
	
	public static int getLabelId(){
		return labelId;
	}
	
	public static void setLabelId(int id){
		labelId = id;
	}
	
	public static void incLabelId(int size){
		setLabelId(labelId + size);
	}
	
	public AddressLabel append(AddressLabel label){
		String prefix = getName().replaceFirst("_", "");
		return new AddressLabel(prefix + label.getName());
	}
}
