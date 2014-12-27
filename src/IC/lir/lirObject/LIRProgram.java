package IC.lir.lirObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import IC.lir.ClassLayout;

public class LIRProgram {
	private List<LIRClass> classList = new ArrayList<LIRClass>();
	private Map<String, ClassLayout> classesLayout;
	private Map<String, LIRStringLiteral> stringLiterals;
	
	public LIRProgram(Map<String, ClassLayout> classesLayout,
			Map<String, LIRStringLiteral> stringLiterals) {
		this.classesLayout = classesLayout;
		this.stringLiterals = stringLiterals;
	}

	public List<LIRClass> getClassList() {
		return classList;
	}

	public Map<String, ClassLayout> getClassesLayout() {
		return classesLayout;
	}

	public Map<String, LIRStringLiteral> getStringLiterals() {
		return stringLiterals;
	}
}
