package IC.AST;

import java.util.ArrayList;
import java.util.List;

public class FieldsAndMethods {
	
	private List<Field> fields;
	
	private List<Method> methods;
	
	public FieldsAndMethods()
	{
		this.setFields(new ArrayList<Field>());
		this.setMethods(new ArrayList<Method>());
	}

	/**
	 * @return the fields
	 */
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	/**
	 * @return the methods
	 */
	public List<Method> getMethods() {
		return methods;
	}

	/**
	 * @param methods the methods to set
	 */
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}

}
