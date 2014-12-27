package IC.lir;

import java.util.HashMap;
import java.util.Map;

import IC.AST.Field;
import IC.AST.ICClass;
import IC.AST.Method;
import IC.AST.Program;
import IC.AST.VirtualMethod;

public class LIRUtils {
	
	/**
	 * Create the class layout map for a given program.
	 * This method will traverse the program classes, methods and fields
	 * and create a mapping between a class name and a ClassLayoutObject
	 * @param program
	 * @return
	 */
	public static Map<String, ClassLayout> createClassLayout(Program program){
		Map<String, ClassLayout> classLayoutMap = new HashMap<String, ClassLayout>();
		
		for(ICClass icClass : program.getClasses()){
			ClassLayout layout;
			String className = icClass.getName();
			String superClassName = icClass.getSuperClassName();
			
			if(className.equals(LIRConstants.LIBRARY)){
				//the Library class doesn't have a class layout
				continue;
			}
			
			// handle super class
			if(icClass.hasSuperClass()){
				layout = new ClassLayout(className, classLayoutMap.get(superClassName));
			} else {
				layout = new ClassLayout(className);
			}
			
			// handle methods
			for(Method method : icClass.getMethods()){
				if(method instanceof VirtualMethod){
					layout.addMethodOffset(method.getName());
				}
			}
			
			// handle fields
			for(Field field : icClass.getFields()){
				layout.addFieldOffset(field.getName());
			}
			
			classLayoutMap.put(className, layout);
		}
		
		return classLayoutMap;
	}
}
