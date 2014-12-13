package IC.Types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import IC.AST.ICClass;

public class TypeTable { //TODO this is a class template from recitation 7....need to complet.
	
	private static String fileName = "";

	private static final int NULL_TYPE_ID 				= 3;
	private static final int INT_TYPE_ID 				= 1;
	private static final int STRING_TYPE_ID 			= 4;
	private static final int BOOLEAN_TYPE_ID 			= 2;
	private static final int VOID_TYPE_ID				= 5;
	private static final int NUM_OF_PREMITIVE_TYPES		= 6;


	private static int unique_id = NUM_OF_PREMITIVE_TYPES;
	
	// Maps element types to array types
	private static Map<SymbolType,ArrayType> uniqueArrayTypes;
	protected static Map<String,ClassType> uniqueClassTypes;
	private static Map<MethodContent,MethodType> uniqueMethodTypes;

	public static SymbolType boolType = new BoolType(BOOLEAN_TYPE_ID);
	public static SymbolType intType = new IntType(INT_TYPE_ID);
	public static SymbolType strType = new StringType(STRING_TYPE_ID);
	public static SymbolType nullType = new NullType(NULL_TYPE_ID);
	public static SymbolType voidType = new VoidType(VOID_TYPE_ID);
	

	// Returns unique array type object
	public static ArrayType arrayType(SymbolType elemType) {
		
		if (uniqueArrayTypes.containsKey(elemType)) 
		{
			// array type object already created return it
			return uniqueArrayTypes.get(elemType);
		}      
		else 
		{          
			// object doesn't exist create and return it
			ArrayType arrt = new ArrayType(elemType, unique_id++);
			uniqueArrayTypes.put(elemType,arrt);
			return arrt;
		}
	}
	
	// Returns unique class type object
	public static ClassType classType(String classId,String superClassId,ICClass classNode) {
		
		if (uniqueClassTypes.containsKey(classId)) 
		{
			// array type object already created return it
			return uniqueClassTypes.get(classId);
		}      
		else 
		{          
			// object doesn't exist create and return it
			ClassType classType = new ClassType(classId,superClassId,unique_id++, classNode);
			uniqueClassTypes.put(classId,classType);
			return classType;
		}
	}
	
    public static boolean classTypeExists(String classId) {
        return uniqueClassTypes.containsKey(classId);
    }
	
	// Initialize the Table
	public static void typeTableInit(String sourceFile) {
		TypeTable.fileName = sourceFile;
        uniqueArrayTypes = new HashMap<SymbolType,ArrayType>();
        uniqueClassTypes = new HashMap<String, ClassType>();
        uniqueMethodTypes = new HashMap<MethodContent,MethodType>();
	}
	
	// Returns unique method type object
	public static MethodType methodType(MethodContent method) {
		
		if (uniqueMethodTypes.containsKey(method)) 
		{
			// array type object already created return it
			return uniqueMethodTypes.get(method);
		}      
		else 
		{          
			// object doesn't exist create and return it
			MethodType methodType = new MethodType(method,unique_id++);
			uniqueMethodTypes.put(method,methodType);
			return methodType;
		}
	}

	@Override
	public String toString() {
		StringBuilder typeTableStr = new StringBuilder();
		typeTableStr.append("Type Table: " + fileName + "\n");
		typeTableStr.append("\t" + INT_TYPE_ID + ": " + "Primitive type: " + intType.toString() + "\n");
		typeTableStr.append("\t" + BOOLEAN_TYPE_ID + ": " + "Primitive type: " + boolType.toString() + "\n");
		typeTableStr.append("\t" + NULL_TYPE_ID + ": " + "Primitive type: " + nullType.toString() + "\n");
		typeTableStr.append("\t" + STRING_TYPE_ID + ": " + "Primitive type: " + strType.toString() + "\n");
		typeTableStr.append("\t" + VOID_TYPE_ID + ": " + "Primitive type: " + voidType.toString() + "\n");
				
		List<ClassType> classTypesList = new ArrayList<ClassType>(uniqueClassTypes.values());
		Collections.sort(classTypesList);
		
		for(ClassType classType : classTypesList)
		{
			typeTableStr.append("\t" + classType.getTypeID() + ": " + "Class: " + classType.toString() + "\n");
		}
		
		List<ArrayType> arrayTypesList = new ArrayList<ArrayType>(uniqueArrayTypes.values());
		Collections.sort(arrayTypesList);
		
		for(ArrayType arrType : arrayTypesList)
		{
			typeTableStr.append("\t" + arrType.getTypeID() + ": " + "Array type: " + arrType.toString() + "\n");
		}
		
		List<MethodType> mehodsTypesList = new ArrayList<MethodType>(uniqueMethodTypes.values());
		Collections.sort(mehodsTypesList);
		
		for(MethodType methodType : mehodsTypesList)
		{
			typeTableStr.append("\t" + methodType.getTypeID() + ": " + "Method type: " + methodType.toString() + "\n");
		}

		return typeTableStr.toString();
	}

}



