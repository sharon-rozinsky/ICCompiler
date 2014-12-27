package IC.lir;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import IC.lir.operands.AddressLabel;

public class ClassLayout {
	
	private static final int BYTE_MULTIPLIER = 4;
	private String className;
	private AddressLabel dispatchTableLabel;
	private Map<String, Integer> fieldsOffset = new HashMap<String, Integer>();
	private Map<String, Offset> methodsOffset = new HashMap<String, Offset>();
	
	public ClassLayout(String className){
		this.className = className;
		AddressLabel classLabel = new AddressLabel(className);
		AddressLabel dispatchTable = new AddressLabel(LIRConstants.DV_LABEL);
		this.dispatchTableLabel = dispatchTable.append(classLabel);
	}
	
	public ClassLayout(String className, ClassLayout parentLayout){
		this(className);
		//propagate the offsets from the parent method
		fieldsOffset = new HashMap<String, Integer>(parentLayout.fieldsOffset);
		methodsOffset = new HashMap<String, Offset>(parentLayout.methodsOffset);
	}
	
	public void addFieldOffset(String field){
		fieldsOffset.put(field, fieldsOffset.size() + 1);
	}
	
	public void addMethodOffset(String method){
		int offset;
		
		if(methodsOffset.containsKey(method)){
			offset = methodsOffset.get(method).getOffset();
		} else {
			offset = methodsOffset.size();
		}
		
		methodsOffset.put(method, new Offset(offset, className));
	}

	public String getClassName() {
		return className;
	}

	public AddressLabel getDispatchTableLabel() {
		return dispatchTableLabel;
	}

	public int getFieldsOffset(String field) {
		if(!fieldsOffset.containsKey(field)){
			return -1;
		}
		return fieldsOffset.get(field);
	}

	public int getMethodsOffset(String method) {
		if(!methodContained(method)){
			return -1;
		}
		return methodsOffset.get(method).getOffset();
	}
	
	public AddressLabel getMethodAddressLabel(String method){
		if(!methodContained(method)){
			return null;
		}
		
		AddressLabel classLabel = methodsOffset.get(method).getClassLabel();
		AddressLabel methodLabel = new AddressLabel(method);
		return classLabel.append(methodLabel);
	}
	
	public String getContainerClassName(String method){
		if(!methodContained(method)){
			return null;
		}
		
		return methodsOffset.get(method).getClassName();
	}

	public int getClassLayoutSize(){
		return (fieldsOffset.size() + 1) * BYTE_MULTIPLIER;
	}
	
	public boolean methodContained(String method) {
		return methodsOffset.containsKey(method);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("# ").append(className).append(": fields offsets:\n");
		for(Entry<String, Integer> entry : fieldsOffset.entrySet()){
			sb.append("# field: ").append(entry.getKey()).append("  offset: ").append(entry.getValue()).append("\n");
		}
		sb.append("\n");
		
		sb.append(dispatchTableLabel.toString()).append(": [");
		if(methodsOffset.size() > 0){
			for(String method : methodsOffset.keySet()){
				sb.append(getMethodAddressLabel(method).toString()).append(",");
			}
			sb.setLength(sb.length() - 1);
		}
		sb.append("]");
		return sb.toString();
	}
	
	
}
