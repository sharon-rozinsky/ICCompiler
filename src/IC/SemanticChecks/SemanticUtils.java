package IC.SemanticChecks;

import java.util.Arrays;

import IC.AST.Method;
import IC.AST.StaticMethod;
import IC.Types.MethodContent;
import IC.Types.SymbolType;
import IC.Types.TypeTable;

public class SemanticUtils {
	
	
    public static SymbolType convertNodeTypeToSymType(IC.AST.Type nodeType) {
        
        int dimension = nodeType.getDimension();
        SymbolType elementType;
        
        if (nodeType instanceof IC.AST.PrimitiveType) {
            IC.AST.PrimitiveType pType = (IC.AST.PrimitiveType) nodeType;
            String name = pType.getName();
            
            switch (name) {
    		case "void":
    			elementType = TypeTable.voidType;
    			break;
    		case "int":
    			elementType = TypeTable.intType;
				break;
    		case "boolean":
    			elementType = TypeTable.boolType;
				break;
    		case "string":
    			elementType = TypeTable.strType;
				break;
    		default:
    			return null;
    		}
        } else { // UserType - class
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

	public static SymbolType convertNodeMethodToSymType(Method method) {
		SymbolType retType = convertNodeTypeToSymType(method.getType());
        if (retType == null) {
            return null;
        }
        
        SymbolType[] paramTypes = new SymbolType[method.getFormals().size()];
        for (int i=0; i<paramTypes.length; i++) {
        	SymbolType type =  convertNodeTypeToSymType(method.getFormals().get(i).getType());
            if (type == null) {
                return null;
            }
            
            paramTypes[i] = type;
        }
        boolean staticRef = method instanceof StaticMethod;
        MethodContent methd = new MethodContent(method.getName(), retType, paramTypes, staticRef);
        return TypeTable.methodType(methd);

	}
	
}
