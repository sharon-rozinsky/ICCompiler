package IC.SemanticChecks;

import IC.Types.SymbolType;
import IC.Types.TypeTable;

public class SemanticUtils {
	
	
    public static SymbolType convertNodeTypeToSymType(IC.AST.Type nodeType) {
        
        int dimension = nodeType.getDimension();
        SymbolType elementType;
        
        if (nodeType instanceof IC.AST.PrimitiveType) {
            IC.AST.PrimitiveType ASTPType = (IC.AST.PrimitiveType) nodeType;
            if (ASTPType.getName().equals("void")) {
                elementType = TypeTable.voidType;
            } else if (ASTPType.getName().equals("int")) {
                elementType = TypeTable.intType;
            } else if (ASTPType.getName().equals("boolean")) {
                elementType = TypeTable.boolType;
            } else { 
                elementType = TypeTable.strType;
            }
        } else { // UserType 
            IC.AST.UserType type = (IC.AST.UserType) nodeType;
            String className = type.getName();
            if (TypeTable.classTypeExists(className)) {
                elementType = TypeTable.classType(className, null, null);
            } else {
                return null;
            }
        }
        // insert lower dimensions to the table
        for (int i = dimension; i >= 1; i--) {
        	elementType = TypeTable.arrayType(elementType);
        }
      
        return elementType;

    }
	
}
