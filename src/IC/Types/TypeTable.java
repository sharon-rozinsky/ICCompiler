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
	protected static Map<String,ClassType> uniqueClassTypes;
	private static Map<Method,MethodType> uniqueMethodTypes;

	public static Type boolType = new BoolType(BOOLEAN_TYPE_ID);
	public static Type intType = new IntType(INT_TYPE_ID);
	public static Type strType = new StringType(STRING_TYPE_ID);
	public static Type nullType = new NullType(NULL_TYPE_ID);
	public static Type voideType = new VoidType(VOID_TYPE_ID);
	

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
	public static ClassType classType(String classId,String superClassId) {
		
		if (uniqueClassTypes.containsKey(classId)) 
		{
			// array type object already created return it
			return uniqueClassTypes.get(classId);
		}      
		else 
		{          
			// object doesn't exist create and return it
			ClassType classType = new ClassType(classId,superClassId,unique_id++);
			uniqueClassTypes.put(classId,classType);
			return classType;
		}
	}
	
	// Returns unique method type object
	public static MethodType methodType(Method method) {
		
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
}



