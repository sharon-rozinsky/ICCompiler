package IC;

import static org.junit.Assert.*;

import org.junit.Test;

import com.sun.xml.internal.ws.util.StringUtils;

import IC.AST.Program;
import IC.SemanticChecks.BreakContinueChecker;
import IC.SemanticChecks.SpecialSemanticChecks;
import IC.SemanticChecks.SymbolTableBuilder;
import IC.SemanticChecks.SymbolTableChecker;
import IC.SemanticChecks.TypeTableBuilder;
import IC.SemanticChecks.TypesCheck;

public class CompilerTests {

	public void runICFIle(String filePath, String libraryPath) throws Exception{
		Program root = Utils.parseProgram(filePath);
		if(!libraryPath.equals("")){
			Utils.parseLibrary(libraryPath, root);
		}
		TypeTableBuilder ttb = new TypeTableBuilder(filePath);				
		ttb.visit((Program) root);
		
		SymbolTableBuilder symbolBuilder = new SymbolTableBuilder(filePath);
        root.accept(symbolBuilder, null);
        
        Utils.printSymbolTable(root);
        Utils.printTypeTable();
        
        //testing symbol scope
        SymbolTableChecker scopeCheck = new SymbolTableChecker(symbolBuilder.getUnresolvedReferences());
        scopeCheck.visit((Program) root, null);
        
        //testing type check
        TypesCheck typeCheck = new TypesCheck();
        typeCheck.visit((Program) root);
        
        //testing breakCont
        BreakContinueChecker breakCont = new BreakContinueChecker();
        breakCont.visit((Program) root, null);
        
        SpecialSemanticChecks.allNoneVoidMethodReturnsNoneVoidType((Program) root);
        SpecialSemanticChecks.validateMainFunction((Program) root);
	}
	
	@Test
	public void checkParser() throws Exception {
		runICFIle("tests/test_files_pa3/example1.ic", "");
		//runICFIle("tests/test_files_pa3/example2.ic");
	}

}
