package IC.SemanticChecks;

import java.util.Arrays;
import java.util.Collection;

import IC.AST.Method;
import IC.AST.StaticMethod;
import IC.Symbols.Symbol;
import IC.Symbols.SymbolTable;
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
    
    public static MethodContent MethodToMethodContents(Method method){
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
        return new MethodContent(method.getName(), retType, paramTypes, staticRef);

	}
    
	public static SymbolType convertNodeMethodToSymType(Method method) {
        MethodContent methd = MethodToMethodContents(method);
        return TypeTable.methodType(methd);
	}
	
	public static String getSymbolTableDescription(SymbolTable symbolTable, Collection<Symbol>... symbolCollections){
		StringBuilder result = new StringBuilder();
		for(Collection<Symbol> symbolCollection : symbolCollections){
			for(Symbol symbol : symbolCollection){
				result.append("\t");
				result.append(symbol.toString());
				result.append("\n");
			}
		}
		
		Collection<SymbolTable> childSymbolTables = symbolTable.getChildSymbolTables().values();
		if(childSymbolTables != null && childSymbolTables.size() > 0){
			result.append("Children tables: ");
			for(SymbolTable childSymbolTable : childSymbolTables){
				result.append(childSymbolTable.getId() + ", ");
			}
			result.setLength(result.length() - 2); //remove last ','
		}
		return result.toString();
	}
	
}
