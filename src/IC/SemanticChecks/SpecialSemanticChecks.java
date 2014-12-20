package IC.SemanticChecks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.BinaryOp;
import IC.AST.Call;
import IC.AST.CallStatement;
import IC.AST.Expression;
import IC.AST.ExpressionBlock;
import IC.AST.ICClass;
import IC.AST.If;
import IC.AST.Length;
import IC.AST.LibraryMethod;
import IC.AST.LocalVariable;
import IC.AST.Location;
import IC.AST.Method;
import IC.AST.New;
import IC.AST.NewArray;
import IC.AST.Program;
import IC.AST.Return;
import IC.AST.Statement;
import IC.AST.StatementsBlock;
import IC.AST.UnaryOp;
import IC.AST.VariableLocation;
import IC.AST.While;
import IC.Symbols.ClassSymbolTable;
import IC.Symbols.Symbol;
import IC.Types.Kind;
import IC.Types.MethodContent;
import IC.Types.MethodType;
import IC.Types.SymbolType;
import IC.Types.TypeTable;

/**
 * this class include all the "none visitor" checks:
 * 1. one main function check.
 * 2. check correctness of library name.
 * 3. check that all none void methods returns values. (bonus).
 * 4. check that all local variables are initialized before used. (bonus).
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
					if((mainType.getMethod().getRetType().equals(TypeTable.voidType))
							&& (mainType.getMethod().getParams().length == 1)
							&& (mainType.getMethod().getParams()[0].equals(TypeTable.uniqueArrayTypes.get(TypeTable.strType))))
					{
						mainMethodCount++;   
						if(mainMethodCount >1)
						{
							throw new SemanticError(getMethodFromClass(icClass, "main").getLine(), 
									String.format("more than one main function is not allowed"));
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
		
		if(mainMethodCount < 1)
		{
			throw new SemanticError(program.getLine(), 
					String.format("program miss main method"));
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

				SymbolType retType= ((MethodType) m.getSymbolType()).getMethod().getRetType();
				if(!retType.equals(TypeTable.voidType))
				{
					if(!allPathsReturnNoneVoidValue(m.getStatements()))
					{
						throw new SemanticError(m.getLine(), 
								String.format("Not all paths returns values in None void method"));
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
				 if(allPathsReturnNoneVoidValue(((StatementsBlock) stmnt).getStatements()))
				 {
					 return true;
				 }
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
	
    public static void allLocalVarsInit(Program program) throws SemanticError
    {       
        for (ICClass icClass : program.getClasses())
        {
                for (Method method : icClass.getMethods())
                {                       
                	int ret = allMethodLocalVarsInit(method.getStatements());
                	if ((ret != -1) && (ret != -2))
                	{
                		throw new SemanticError(ret, String.format("using a non initialised variable"));
                	}
                		
                }
        }
        
    }
     
    private static int allMethodLocalVarsInit(List<Statement> statements) throws SemanticError
    {
        int stmtCnt = 0;
        
        for (Statement statement : statements)
        {
        	stmtCnt++;
        	if (statement instanceof LocalVariable)
            {       
	            if (((LocalVariable) statement).hasInitValue())
	            {
	            	continue; //The variable is initialized upon definition - no need to continue checking it.
	            }
	            else
	            {
                    List<Statement> varStmts = collectStmtsForSpecificLocalVar(statements, stmtCnt);
                    int ret = isVarInit(varStmts, ((LocalVariable)statement).getName());
                    if ((ret != -1 ) && (ret != -2)){
                		return ret;
                	}
	            }                               
            }
            else if (statement instanceof StatementsBlock)
            {
            	int ret = allMethodLocalVarsInit(((StatementsBlock)statement).getStatements());
            	if ((ret != -1 ) && (ret != -2)){
            		return ret;
            	}
            }            
            else if (statement instanceof While)
            {
                    List<Statement> newList = new LinkedList<Statement>();
                    newList.add(((While)statement).getOperation());    
                    int ret = allMethodLocalVarsInit(newList);
                    if ((ret != -1 ) && (ret != -2)){
                		return ret;
                	}
            }           
            else if (statement instanceof If)
            {
                    List<Statement> newList = new LinkedList<Statement>();
                    newList.add(((If)statement).getOperation());                               
                    int ret = allMethodLocalVarsInit(newList);
                    if ((ret != -1 ) && (ret != -2)){
                		return ret;
                	}
                    if (((If)statement).hasElse())
                    {
                            newList.clear();
                            newList.add(((If)statement).getElseOperation());                             
                            ret = allMethodLocalVarsInit(newList);
                            if ((ret != -1 ) && (ret != -2)){
                        		return ret;
                        	}
                    }
            }
        }
        return -2;
    }
    
    
    private static List<Statement> collectStmtsForSpecificLocalVar(List<Statement> statements, int startStmtIndex)
    {	// Collects all "forward" statements - from the var and forward
        int stmtCounter = 0;
        List<Statement> newList = new LinkedList<Statement>();
    
        for (Statement statement : statements)
        {   
                stmtCounter++;
                if (stmtCounter > startStmtIndex)
                {
                        newList.add(statement);
                }
        }
        
        return newList;
    }
    
    
    /**  Returns line number for Error - Used before Init.
      *  Returns -1 for definite init before all uses,
      *  Returns -2 for never used or for when only sometimes the variable gets init, but never gets used without init.. 
      */
    private static int isVarInit(List<Statement> statements, String varName)
    {   
    	for (Statement statement : statements)
    	{
    		if (statement instanceof Assignment)
    		{
    			if (!isExpContainsVar(((Assignment)statement).getAssignment(), varName)) //make sure it's not used at the right hand side
    			{                                  				
    				Location var = ((Assignment)statement).getVariable(); //check if used left hand side - it's the assignment we're looking for

    				if ((var instanceof VariableLocation) && (varName.equals(((VariableLocation)var).getName())))                                   
    				{
    					return -1; //it's been assigned
    				}                               
    				else if (var instanceof ArrayLocation)
    				{
    					if (isExpContainsVar(((ArrayLocation)var).getArray(), varName) || 
    							isExpContainsVar(((ArrayLocation)var).getIndex(), varName))
    					{
    						return statement.getLine();
    					}
    				}
    			}
    			else
    			{
    				return statement.getLine();
    			}
    		}

    		else if (statement instanceof Return)
    		{
    			if (isExpContainsVar(((Return)statement).getValue(), varName))
    			{
    				return statement.getLine();
    			}
    		}

    		else if (statement instanceof LocalVariable)
    		{       
    			if (isExpContainsVar(((LocalVariable)statement).getInitValue(), varName))
    			{
    				return statement.getLine();
    			}

    			else if (varName.equals(((LocalVariable)statement).getName()))
    			{   				
    				return -2; //we found a local variable. no need to continue checking this section.
    			}
    		}

    		else if (statement instanceof CallStatement)
    		{
    			if (isExpContainsVar(((CallStatement)statement).getCall(), varName))
    			{
    				return statement.getLine();
    			}
    		}

    		else if (statement instanceof StatementsBlock)
    		{
    			int stmtBlockResult = isVarInit(((StatementsBlock)statement).getStatements(), varName);
    			if (stmtBlockResult != -2)  //only return definitive result, otherwise we should continue checking
    				return stmtBlockResult;
    		}

    		else if (statement instanceof While)
    		{
    			if (isExpContainsVar(((While)statement).getCondition(), varName))
    			{
    				return statement.getLine();
    			}
    			else
    			{
    				List<Statement> whileStatements = new LinkedList<Statement>();
    				whileStatements.add(((While)statement).getOperation());
    				
    				int ret = isVarInit(whileStatements, varName);
    				if ((ret != -1 ) && (ret != -2)){
                		return ret;
                	}
    			}
    		}
    		else if (statement instanceof If)
    		{
    			if (isExpContainsVar(((If)statement).getCondition(), varName))
    			{
    				return statement.getLine();
    			}
    			else
    			{
    				List<Statement> ifStatements = new LinkedList<Statement>();
    				ifStatements.add(((If)statement).getOperation());

    				int ifsResult = isVarInit(ifStatements, varName);                                   
    				if ((ifsResult != -1 ) && (ifsResult != -2))
    					return ifsResult; //error

    				if (((If)statement).hasElse())
    				{
    					List<Statement> elseStatements = new LinkedList<Statement>();
    					elseStatements.add(((If)statement).getElseOperation());

    					int elseResult = isVarInit(elseStatements, varName);
    					if ((elseResult != -1 ) && (elseResult != -2))
    						return elseResult; //error

    					else if (elseResult == -1 && ifsResult == -1)
    						return -1; // defined both in if and in else so defined!!
    				}
    			}
    		}
    	}
    	return -2;
    }
    
    private static boolean isExpContainsVar(Expression expression, String id)
    {
        if (expression instanceof BinaryOp)
        {
            return (isExpContainsVar(((BinaryOp)expression).getFirstOperand(), id) || isExpContainsVar(((BinaryOp)expression).getSecondOperand(), id));
        }
        
        else if (expression instanceof UnaryOp)
        {
            return isExpContainsVar(((UnaryOp)expression).getOperand(), id);
        }
        
        else if (expression instanceof Call)
        {
	        for (Expression exp : ((Call)expression).getArguments())
	        {
	                if (isExpContainsVar(exp, id))
	                        return true;
	        }
	        
	        return false;
        }
        
        else if (expression instanceof Length)
        {
            return isExpContainsVar(((Length)expression).getArray(), id);
        }
        
        else if (expression instanceof ExpressionBlock)
        {
            return isExpContainsVar(((ExpressionBlock)expression).getExpression(), id);
        }
        
        else if (expression instanceof New)
        {
            if (expression instanceof NewArray)
            {
                    return isExpContainsVar(((NewArray)expression).getSize(), id);
            }
        }
        
        else if (expression instanceof Location)
        {
            if (expression instanceof ArrayLocation)
            {
                    return (isExpContainsVar(((ArrayLocation)expression).getArray(), id) || isExpContainsVar(((ArrayLocation)expression).getIndex(), id));                            
            }
            
            else
            {
                    if (id.equals(((VariableLocation)expression).getName()))
                            return true;
                    else
                            return false;
            }
        }       
        
        return false;
    }

}
