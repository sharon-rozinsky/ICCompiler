package IC.SymbolTable;

class TypeTable { //TODO this is a class template from recitation 7....need to complet.
	// Maps element types to array types
	private Map<Type,ArrayType> uniqueArrayTypes;
	private Map<String,ClassType> uniqueClassTypes;

	public static Type boolType = new BoolType();
	public static Type intType = new IntType();

	// Returns unique array type object
	public static ArrayType arrayType(Type elemType) {
		
		if (uniqueArrayTypes.containsKey(elemType)) 
		{
			// array type object already created – return it
			return uniqueArrayTypes.get(elemType);
		}      
		else 
		{          
			// object doesn’t exist – create and return it
			ArrayType arrt = new ArrayType(elemType);
			uniqueArrayTypes.put(elemType,ArrayType);
			return arrt;
		}
	}
}



