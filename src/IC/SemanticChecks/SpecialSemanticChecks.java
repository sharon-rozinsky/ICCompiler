package IC.SemanticChecks;

import java.util.ArrayList;
import java.util.List;

import IC.AST.ICClass;
import IC.AST.If;
import IC.AST.LibraryMethod;
import IC.AST.Method;
import IC.AST.Program;
import IC.AST.Return;
import IC.AST.Statement;
import IC.AST.StatementsBlock;
import IC.Symbols.ClassSymbolTable;
import IC.Symbols.Symbol;
import IC.Types.Kind;
import IC.Types.MethodType;
import IC.Types.SymbolType;
import IC.Types.TypeTable;

/**
 * this class include all the "none visitor" checks:
 * 1. one main function check.
 * 2. check correctness of library name.
 * 3. check that all local variables are initialized before used. (bonus).
 * 4. check that all none void methods returns values. (bonus).
 */
public class SpecialSemanticChecks {
	
	public static void validateMainFunction(Program program) throws SemanticError {
		
		int mainMethodCount = 0;
		
		for(ICClass icClass : program.getClasses())
		{
			Symbol mainSymbol = ((ClassSymbolTable) icClass.getEnclosingScopeSymTable()).getMethods().get("main");
			if(mainSymbol != null)
			{
				if(mainSymbol.getKind() == Kind.MemberVariable)
				{
					continue;
				}
				else if(mainSymbol.getKind() == Kind.Method) //virtual call
				{
					
					throw new SemanticError(getMethodFromClass(icClass, "main").getLine(), 
							String.format("invalid main method signeture"));
				}
				else if(mainSymbol.getKind() == Kind.StaticMethod)
				{
					MethodType mainType = (MethodType) mainSymbol.getType();
					if((mainType.getMethod().getRetType() == TypeTable.voidType)
							&& (mainType.getMethod().getParams().length == 1)
							&& (mainType.getMethod().getParams()[1] == TypeTable.uniqueArrayTypes.get(TypeTable.strType)))
					{
						mainMethodCount++;   
						if(mainMethodCount >1)
						{
							throw new SemanticError(getMethodFromClass(icClass, "main").getLine(), 
									String.format("more the single main function is not allowed"));
						}
					}
					else
					{
						throw new SemanticError(getMethodFromClass(icClass, "main").getLine(), 
								String.format("invalid main method signeture"));
					}
				}
			}
		}
	}
	/**  @return Method
	  *  
	  */
	private static Method getMethodFromClass(ICClass icClass,String methodName) {
		for(Method m : icClass.getMethods())
		{
			if(m.getName().equals(methodName))
			{
				return m;
			}
		}
		
		return null;
	}
	
	public static void checkLibraryNameCorrectness(ICClass libraryClass) throws SemanticError {
        if (!libraryClass.getName().equals("Library")) {
        	throw new SemanticError(libraryClass.getLine(), 
					String.format("Library class name must be \"Library\""));
        }
    }
	
	public static void allNoneVoidMethodReturnsNoneVoidType(Program program) throws SemanticError {
		
		for(ICClass icClass : program.getClasses())
		{
			for(Method m : icClass.getMethods())
			{
				//ignore Library method
				if(m instanceof LibraryMethod)
				{
					continue;
				}
				
				SymbolType retType= TypeTable.uniqueMethodTypes.get(m.getName()).getMethod().getRetType();
				if(retType != TypeTable.voidType)
				{
					if(!allPathsReturnNoneVoidValue(m.getStatements()))
					{
						throw new SemanticError(m.getLine(), 
								String.format("Library class name must be \"Library\""));
					}
				}
			}
			
		}
	}

	private static boolean allPathsReturnNoneVoidValue(List<Statement> statementlist) {
		
		//search unconditional return.

		for(Statement stmnt : statementlist)
		{
			if(stmnt instanceof Return)
			{
				return true;
			}
			
			if(stmnt instanceof StatementsBlock)
			{
				allPathsReturnNoneVoidValue(((StatementsBlock) stmnt).getStatements());
			}
			
		}
		
		//search unconditional return failed. now search if-else pair and check if both contains return value
		for(Statement stmnt : statementlist)
		{
			if((stmnt instanceof If) && ((If) stmnt).hasElse())
			{
				If ifStmnt = (If) stmnt;
				if(allPathsReturnNoneVoidValue(ifStmnt.getOperation())
						&& allPathsReturnNoneVoidValue(ifStmnt.getElseOperation()))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	private static boolean allPathsReturnNoneVoidValue(Statement operation) {
		List<Statement> operationlistWraper = new ArrayList<Statement>();
		operationlistWraper.add(operation);
		return allPathsReturnNoneVoidValue(operationlistWraper);
	}
	
	

}
