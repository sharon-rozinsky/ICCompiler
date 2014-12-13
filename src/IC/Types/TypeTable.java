package IC.Types;

import java.util.Map;

class TypeTable { //TODO this is a class template from recitation 7....need to complet.
	
	private static final int NULL_TYPE_ID 				= 0;
	private static final int INT_TYPE_ID 				= 1;
	private static final int STRING_TYPE_ID 			= 2;
	private static final int BOOLEAN_TYPE_ID 			= 3;
	private static final int VOID_TYPE_ID				= 4;
	private static final int NUM_OF_PREMITIVE_TYPES		= 5;


	private static int unique_id = NUM_OF_PREMITIVE_TYPES;
	
	// Maps element types to array types
	private static Map<Type,ArrayType> uniqueArrayTypes;
	private static Map<String,ClassType> uniqueClassTypes;

	public static Type boolType = new BoolType();
	public static Type intType = new IntType();
	

	// Returns unique array type object
	public static ArrayType arrayType(Type elemType) {
		
		if (uniqueArrayTypes.containsKey(elemType)) 
		{
			// array type object already created return it
			return uniqueArrayTypes.get(elemType);
		}      
		else 
		{          
			// object doesn't exist create and return it
			ArrayType arrt = new ArrayType(elemType,unique_id++);
			uniqueArrayTypes.put(elemType,arrt);
			return arrt;
		}
	}
	
	// Returns unique class type object
	public static ClassType classType(String classId) {
		
		if (uniqueClassTypes.containsKey(classId)) 
		{
			// array type object already created return it
			return uniqueClassTypes.get(classId);
		}      
		else 
		{          
			// object doesn't exist create and return it
			ClassType classType = new ClassType(classId,unique_id++);
			uniqueClassTypes.put(classId,classType);
			return classType;
		}
	}
}



