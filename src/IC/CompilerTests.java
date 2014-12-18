package IC;

import java.io.File;
import java.util.ArrayList;
import java.util.List;





import org.junit.Assert;
import org.junit.Test;

import IC.Parser.LexicalError;
import IC.Parser.SyntaxError;
import IC.SemanticChecks.SemanticError;
import IC.Types.TypeTable;


public class CompilerTests {

	private static String libraryPath = "-Ltests\\libic.sig.txt";
	private static String testsPath = "tests\\test_files_pa3";
	private static String[] args = {"",libraryPath,Compiler.PRINT_SYMTAB_OPTION};

	//	public void runICFIle(String filePath, String libraryPath) throws Exception{
	//		Program root = Utils.parseProgram(filePath);
	//		if(!libraryPath.equals("")){
	//			Utils.parseLibrary(libraryPath, root);
	//		}
	//		TypeTableBuilder ttb = new TypeTableBuilder(filePath);				
	//		ttb.visit((Program) root);
	//
	//		SymbolTableBuilder symbolBuilder = new SymbolTableBuilder(filePath);
	//		root.accept(symbolBuilder, null);
	//
	//		Utils.printSymbolTable(root);
	//		Utils.printTypeTable();
	//
	//		//testing symbol scope
	//		SymbolTableChecker scopeCheck = new SymbolTableChecker(symbolBuilder.getUnresolvedReferences());
	//		scopeCheck.visit((Program) root, null);
	//
	//		//testing type check
	//		TypesCheck typeCheck = new TypesCheck();
	//		typeCheck.visit((Program) root);
	//
	//		//testing breakCont
	//		BreakContinueChecker breakCont = new BreakContinueChecker();
	//		breakCont.visit((Program) root, null);
	//
	//		SpecialSemanticChecks.allNoneVoidMethodReturnsNoneVoidType((Program) root);
	//		SpecialSemanticChecks.validateMainFunction((Program) root);
	//	}
	//
	//	@Test
	//	public void checkParser() throws Exception {
	//		runICFIle("tests/test_files_pa3/example1.ic", "libic.sig.txt");
	//		//runICFIle("tests/test_files_pa3/example2.ic");
	//	}


	/**
	 * Thsi function create Junit tests code for all IC files under the specified path.
	 * @throws Exception
	 */
	public void TestCodeGenerator() throws Exception {

		final File folder = new File(testsPath);
		List<String> icTestFiles = listFilesForFolder(folder);

		int testNum = 1;
		for(String icFile : icTestFiles)
		{
			String icFilePath = "";
			String[] pathParts = icFile.split("\\\\");
			int i = 0;
			for(String pathPart:pathParts)
			{
				if(i < pathParts.length - 1)
				{
					icFilePath += pathPart + "\\\\";
				}
				else
				{
					icFilePath += pathPart;
					icFile = pathPart;
				}
				i++;
			}
			//print header
			System.out.println("@Test\n/**\n* Test number " + testNum + ".\n* @throws Exception\n*/");
			//print method signature
			System.out.println("public void SemanticCheck_Test_" + testNum +  "() throws Exception {\n");
			//Set compiler arguments.
			System.out.println("\targs[0] = \"" + icFilePath + "\";");
			//run compiler and close function.
			System.out.println("\tCompiler.Compile(args);");
			//Init type table for next test.
			System.out.println("\t//Init type table for next test.\n\tTypeTable.typeTableInit(\""+ icFile +"\");\n}\n\n");
			testNum++;
		}
	}

	public List<String> listFilesForFolder(final File folder) {
		List<String> output = new ArrayList<>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				output.addAll(listFilesForFolder(fileEntry));
			} else {
				String filePath = fileEntry.getPath();
				String[] fileType = filePath.split("\\.");

				if(fileType.length > 0)
				{
					if(fileType[fileType.length-1].equals("ic"))
					{
						output.add(fileEntry.getPath());
					}
				}
			}
		}
		return output;
	}

	@Test
	/**
	 * Test number 1.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_1() throws Exception {

		args[0] = "tests\\test_files_pa3\\AnotherMethodReturnValueTest.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("AnotherMethodReturnValueTest.ic");
	}


	@Test
	/**
	 * Test number 2.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_2() throws Exception {

		args[0] = "tests\\test_files_pa3\\ArrayTest.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("ArrayTest.ic");
	}


	@Test
	/**
	 * Test number 3.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_3() throws Exception {
		try {
			args[0] = "tests\\test_files_pa3\\bad_assignment.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail. 
		} catch (SemanticError e)
		{
			TypeTable.typeTableInit("bad_assignment.ic");
			String lineErorr = e.getMessage().split(":")[0];
			Assert.assertTrue("semantic error at line 22".equals(lineErorr));
		}
	}


	@Test
	/**
	 * Test number 4.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_4() throws Exception {

		try {
			args[0] = "tests\\test_files_pa3\\bad_assignment2.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail. 
		} catch (SemanticError e)
		{
			TypeTable.typeTableInit("bad_assignment2.ic");
			Assert.assertTrue("semantic error at line 16".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 5.
	 * 
	 * Here we got some issues TODO check this again!
	 * @throws Exception
	 */
	public void SemanticCheck_Test_5() throws Exception {

		try {
			args[0] = "tests\\test_files_pa3\\bad_assignment3.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail. 
		} catch (SemanticError e)
		{
			TypeTable.typeTableInit("bad_assignment3.ic");
			Assert.assertTrue("semantic error at line 27".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 6.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_6() throws Exception {
		try
		{
			args[0] = "tests\\test_files_pa3\\bad_scope1.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail

		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("bad_scope1.ic");
			Assert.assertTrue("semantic error at line 12".equals(e.getMessage().split(":")[0]));
		}

	}


	@Test
	/**
	 * Test number 7.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_7() throws Exception {
		try
		{
			args[0] = "tests\\test_files_pa3\\bad_scope2.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 12".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 8.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_8() throws Exception {

		args[0] = "tests\\test_files_pa3\\CallStatement.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("");
	}


	@Test
	/**
	 * Test number 9.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_9() throws Exception {
		args[0] = "tests\\test_files_pa3\\ContBreakInWhile.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("");
	}


	@Test
	/**
	 * Test number 10.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_10() throws Exception {

		try
		{
			args[0] = "tests\\test_files_pa3\\errors\\ArrayTestBad.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 12".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 11.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_11() throws Exception {
		try
		{
			args[0] = "tests\\test_files_pa3\\errors\\ClassRedefinition.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 12".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 12.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_12() throws Exception {

		args[0] = "tests\\test_files_pa3\\errors\\ContBreakOutsideWhile.ic";
		Compiler.Compile(args);
		Assert.assertTrue(false); //Compile should fail
		//Init type table for next test.
		TypeTable.typeTableInit("ContBreakOutsideWhile.ic");
	}


	@Test
	/**
	 * Test number 13.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_13() throws Exception {

		args[0] = "tests\\test_files_pa3\\errors\\ContBreakOutsideWhile2.ic";
		Compiler.Compile(args);
		Assert.assertTrue(false); //Compile should fail
		//Init type table for next test.
		TypeTable.typeTableInit("ContBreakOutsideWhile2.ic");
	}


	@Test
	/**
	 * Test number 14.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_14() throws Exception {

		args[0] = "tests\\test_files_pa3\\errors\\ContBreakOutsideWhile3.ic";
		Compiler.Compile(args);
		Assert.assertTrue(false); //Compile should fail
		//Init type table for next test.
		TypeTable.typeTableInit("ContBreakOutsideWhile3.ic");
	}


	@Test
	/**
	 * Test number 15.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_15() throws Exception {
		try
		{
			args[0] = "tests\\test_files_pa3\\errors\\IfElseWhileBad.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 16".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 16.
	 *  TODO check why this one suppose to fail...
	 * @throws Exception
	 */
	public void SemanticCheck_Test_16() throws Exception {

		args[0] = "tests\\test_files_pa3\\errors\\IfElseWhileScope.ic";
		Compiler.Compile(args);
		Assert.assertTrue(false); //Compile should fail
		//Init type table for next test.
		TypeTable.typeTableInit("IfElseWhileScope.ic");
	}


	@Test
	/**
	 * Test number 17.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_17() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\InvalidIntegerLiteral.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (LexicalError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
		}
	}


	@Test
	/**
	 * Test number 18.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_18() throws Exception {

		args[0] = "tests\\test_files_pa3\\errors\\LocalVariableAssigmentFromFieldWithBadType.ic";
		Compiler.Compile(args);
		Assert.assertTrue(false); //Compile should fail
		//Init type table for next test.
		TypeTable.typeTableInit("LocalVariableAssigmentFromFieldWithBadType.ic");
	}


	@Test
	/**
	 * Test number 19.
	 * Bonus test!!!
	 * @throws Exception
	 */
	public void SemanticCheck_Test_19() throws Exception {

		args[0] = "tests\\test_files_pa3\\errors\\LocalVarsNotInitB4Use.ic";
		Compiler.Compile(args);
		Assert.assertTrue(false); //Compile should fail
		//Init type table for next test.
		TypeTable.typeTableInit("LocalVarsNotInitB4Use.ic");
	}


	@Test
	/**
	 * Test number 20.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_20() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\LocationsTest.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 35".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 21.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_21() throws Exception {
		
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\LogicalBinaryOpsBad.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 15".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 22.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_22() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\MathBinaryOpsBad.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 11".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 23.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_23() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\MethodsCallTestBad.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 36".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 24.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_24() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\MethodsNotReturningValue.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 3".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 25.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_25() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\SingleValidMainInvalidSignature.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 3".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 26.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_26() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\SingleValidMainMoreThanOne.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 6".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 27.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_27() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\SingleValidMainNoMain.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 0".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 28.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_28() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\SymbolTablesBuilderTest.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 4".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 29.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_29() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\SymbolTablesScopeCheckTest.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 6".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 30.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_30() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\UnaryOpsBad.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 12".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 31.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_31() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\UndefineType.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 32.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_32() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\errors\\VariableLocationTestBad.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 33.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_33() throws Exception {

		args[0] = "tests\\test_files_pa3\\example1.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("example1.ic");
	}


	@Test
	/**
	 * Test number 34.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_34() throws Exception {

		args[0] = "tests\\test_files_pa3\\example2.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("example2.ic");
	}


	@Test
	/**
	 * Test number 35.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_35() throws Exception {

		args[0] = "tests\\test_files_pa3\\external\\Break,Continue.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("Break,Continue.ic");
	}


	@Test
	/**
	 * Test number 36.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_36() throws Exception {

		args[0] = "tests\\test_files_pa3\\external\\ComplexAssignment1.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("ComplexAssignment1.ic");
	}


	@Test
	/**
	 * Test number 37.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_37() throws Exception {
		
		try
		{
			args[0] = "tests\\test_files_pa3\\external\\ComplexAssignment2.ic";
			Compiler.Compile(args);
			//Init type table for next test.
			TypeTable.typeTableInit("ComplexAssignment2.ic");
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue(false); //Compile should pass
		}
		
	}


	@Test
	/**
	 * Test number 38.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_38() throws Exception {

		args[0] = "tests\\test_files_pa3\\external\\ComplexAssignment3.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("ComplexAssignment3.ic");
	}


	@Test
	/**
	 * Test number 39.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_39() throws Exception {

		try
		{
			args[0] = "tests\\test_files_pa3\\external\\ComplexAssignment4.ic";
			Compiler.Compile(args);
			//Init type table for next test.
			TypeTable.typeTableInit("ComplexAssignment4.ic");
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue(false); //Compile should pass
		}

	}


	@Test
	/**
	 * Test number 40.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_40() throws Exception {

		args[0] = "tests\\test_files_pa3\\external\\ComplexInit.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("ComplexInit.ic");
	}


	@Test
	/**
	 * Test number 41.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_41() throws Exception {

		args[0] = "tests\\test_files_pa3\\external\\ComplexReturn.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("ComplexReturn.ic");
	}


	@Test
	/**
	 * Test number 42.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_42() throws Exception {

		args[0] = "tests\\test_files_pa3\\external\\ComplexScope1.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("ComplexScope1.ic");
	}


	@Test
	/**
	 * Test number 43.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_43() throws Exception {
		try
		{
			args[0] = "tests\\test_files_pa3\\external\\ComplexScope2.ic";
			Compiler.Compile(args);
			//Init type table for next test.
			TypeTable.typeTableInit("ComplexScope2.ic");
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue(false); //Compile should pass
		}
	}


	@Test
	/**
	 * Test number 44.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_44() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\ClassGraphError.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 45.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_45() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\ComplexAssignmentError1.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 21".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 46.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_46() throws Exception {

		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\ComplexAssignmentError2.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 22".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 47.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_47() throws Exception {

		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\ComplexAssignmentError3.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 27".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 48.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_48() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\ComplexInitError.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 49.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_49() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\ComplexReturnError.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 6".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 50.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_50() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\ComplexScopeError1.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 51.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_51() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\ComplexScopeError2.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 52.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_52() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\LocalVarShadowsParamError.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 8".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 53.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_53() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\NoReturnError.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 30".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 54.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_54() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\SameIDinExtendingError.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 16".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 55.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_55() throws Exception {

		args[0] = "tests\\test_files_pa3\\external\\errors\\SomeSemanticErrors.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("SomeSemanticErrors.ic");
	}


	@Test
	/**
	 * Test number 56.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_56() throws Exception {
		
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\ThisTestError.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 57.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_57() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\TwoMainsInSameClass.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 16".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 58.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_58() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\TwoMainsInSameProgram.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 21".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 59.
	 * BonusCheck!!
	 * @throws Exception
	 */
	public void SemanticCheck_Test_59() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\VarNotInitError.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 60.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_60() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\WierdExpError1.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 20".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 61.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_61() throws Exception {

		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\WierdExpError2.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 62.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_62() throws Exception {

		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\WierdExpError3.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 20".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 63.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_63() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\WrongLibraryName.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 64.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_64() throws Exception {
		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\WrongMainSignature1.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 8".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 65.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_65() throws Exception {

		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\WrongMainSignature2.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SyntaxError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
		}
	}


	@Test
	/**
	 * Test number 66.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_66() throws Exception {

		try 
		{
			args[0] = "tests\\test_files_pa3\\external\\errors\\WrongMainSignature3.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 8".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 67.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_67() throws Exception {

		args[0] = "tests\\test_files_pa3\\external\\example1.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("example1.ic");
	}


	@Test
	/**
	 * Test number 68.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_68() throws Exception {

		args[0] = "tests\\test_files_pa3\\external\\example2.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("example2.ic");
	}


	@Test
	/**
	 * Test number 69.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_69() throws Exception {

		args[0] = "tests\\test_files_pa3\\external\\LocalVar,FieldWithSameName.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("LocalVar,FieldWithSameName.ic");
	}


	@Test
	/**
	 * Test number 70.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_70() throws Exception {

		args[0] = "tests\\test_files_pa3\\external\\ThisTest.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("ThisTest.ic");
	}


	@Test
	/**
	 * Test number 71.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_71() throws Exception {
		try
		{
			args[0] = "tests\\test_files_pa3\\IfElseWhile.ic";
			Compiler.Compile(args);
			//Init type table for next test.
			TypeTable.typeTableInit("");
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue(false); //Compile should pass
		}
	}


	@Test
	/**
	 * Test number 72.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_72() throws Exception {

		args[0] = "tests\\test_files_pa3\\inheritance.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("inheritance.ic");
	}


	@Test
	/**
	 * Test number 73.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_73() throws Exception {

		args[0] = "tests\\test_files_pa3\\LocalVariableAssigmentFromFieldWithGoodType.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("LocalVariableAssigmentFromFieldWithGoodType.ic");
	}


	@Test
	/**
	 * Test number 74.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_74() throws Exception {
		try
		{
			args[0] = "tests\\test_files_pa3\\LocalVariableShadowsFieldType.ic";
			Compiler.Compile(args);
			//Init type table for next test.
			TypeTable.typeTableInit("");
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue(false); //Compile should pass
		}
	}


	@Test
	/**
	 * Test number 75.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_75() throws Exception {

		args[0] = "tests\\test_files_pa3\\LocalVarsInitB4Use.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("LocalVarsInitB4Use.ic");
	}


	@Test
	/**
	 * Test number 76.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_76() throws Exception {

		args[0] = "tests\\test_files_pa3\\LogicalBinaryOps.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("LogicalBinaryOps.ic");
	}


	@Test
	/**
	 * Test number 77.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_77() throws Exception {

		args[0] = "tests\\test_files_pa3\\MathBinaryOps.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("MathBinaryOps.ic");
	}


	@Test
	/**
	 * Test number 78.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_78() throws Exception {

		args[0] = "tests\\test_files_pa3\\MethodsCallTest.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("MethodsCallTest.ic");
	}


	@Test
	/**
	 * Test number 79.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_79() throws Exception {
		try
		{
			args[0] = "tests\\test_files_pa3\\MethodsReturningValue.ic";
			Compiler.Compile(args);
			//Init type table for next test.
			TypeTable.typeTableInit("");
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue(false); //Compile should pass
		}
	}


	@Test
	/**
	 * Test number 80.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_80() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\AllTokensTest.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("AllTokensTest.ic");
	}


	@Test
	/**
	 * Test number 81.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_81() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\Break,Continue.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("Break,Continue.ic");
	}


	@Test
	/**
	 * Test number 82.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_82() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\ComplexExp.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("ComplexExp.ic");
	}


	@Test
	/**
	 * Test number 83.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_83() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\ComplexIf.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("ComplexIf.ic");
	}


	@Test
	/**
	 * Test number 84.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_84() throws Exception {
		try
		{
			args[0] = "tests\\test_files_pa3\\old\\CubesTest.ic";
			Compiler.Compile(args);
			//Init type table for next test.
			TypeTable.typeTableInit("");
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue(false); //Compile should pass
		}
	}


	@Test
	/**
	 * Test number 85.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_85() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\EmptyMethod.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("EmptyMethod.ic");
	}


	@Test
	/**
	 * Test number 86.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_86() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\EmptyThingsAndDifferentOrderTest.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("EmptyThingsAndDifferentOrderTest.ic");
	}


	@Test
	/**
	 * Test number 87.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_87() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\Fields,Formals,Ops,This.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("Fields,Formals,Ops,This.ic");
	}


	@Test
	/**
	 * Test number 88.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_88() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\IfElseTest.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("IfElseTest.ic");
	}


	@Test
	/**
	 * Test number 89.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_89() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\LocationsAndArrays.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("LocationsAndArrays.ic");
	}


	@Test
	/**
	 * Test number 90.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_90() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\ManyClasses.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("ManyClasses.ic");
	}


	@Test
	/**
	 * Test number 91.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_91() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\Multi-class.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("Multi-class.ic");
	}


	@Test
	/**
	 * Test number 92.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_92() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\OpsPresedences.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("OpsPresedences.ic");
	}


	@Test
	/**
	 * Test number 93.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_93() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\PrecedenceUMINUS.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("PrecedenceUMINUS.ic");
	}


	@Test
	/**
	 * Test number 94.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_94() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\Quicksort.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("Quicksort.ic");
	}


	@Test
	/**
	 * Test number 95.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_95() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\Sieve.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("Sieve.ic");
	}


	@Test
	/**
	 * Test number 96.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_96() throws Exception {

		try
		{
			args[0] = "tests\\test_files_pa3\\old\\SynTest1.ic";
			Compiler.Compile(args);
			//Init type table for next test.
			TypeTable.typeTableInit("");
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue(false); //Compile should pass
		}
	}


	@Test
	/**
	 * Test number 97.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_97() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\SynTest2.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("SynTest2.ic");
	}


	@Test
	/**
	 * Test number 98.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_98() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\UnaryBinaryOpTest.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("UnaryBinaryOpTest.ic");
	}


	@Test
	/**
	 * Test number 99.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_99() throws Exception {

		args[0] = "tests\\test_files_pa3\\old\\WhileWithinWhile.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("WhileWithinWhile.ic");
	}


	@Test
	/**
	 * Test number 100.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_100() throws Exception {

		args[0] = "tests\\test_files_pa3\\ThisInVirtualMethods.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("ThisInVirtualMethods.ic");
	}


	@Test
	/**
	 * Test number 101.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_101() throws Exception {

		args[0] = "tests\\test_files_pa3\\UnaryOps.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("UnaryOps.ic");
	}


	@Test
	/**
	 * Test number 102.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_102() throws Exception {

		args[0] = "tests\\test_files_pa3\\VariableLocationTest.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("VariableLocationTest.ic");
	}




}