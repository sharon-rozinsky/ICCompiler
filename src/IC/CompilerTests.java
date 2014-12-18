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
	private static String DIR_SEPARATOR = "\\";
	static{
		String osName = System.getProperty("os.name").toLowerCase();
		if(osName.contains("mac os x"))
			DIR_SEPARATOR = "/";
		else
			DIR_SEPARATOR = "\\";
	}
	private static String libraryPath = "-Ltests"+DIR_SEPARATOR+"libic.sig.txt";
	private static String testsPath = "tests"+DIR_SEPARATOR+"test_files_pa3";
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
			String[] pathParts = icFile.split(""+DIR_SEPARATOR+""+DIR_SEPARATOR+"");
			int i = 0;
			for(String pathPart:pathParts)
			{
				if(i < pathParts.length - 1)
				{
					icFilePath += pathPart + ""+DIR_SEPARATOR+""+DIR_SEPARATOR+"";
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
				String[] fileType = filePath.split(""+DIR_SEPARATOR+".");

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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"AnotherMethodReturnValueTest.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"ArrayTest.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"bad_assignment.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"bad_assignment2.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"bad_assignment3.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"bad_scope1.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"bad_scope2.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"CallStatement.ic";
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
		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"ContBreakInWhile.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ArrayTestBad.ic";
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
	 * Test number 11.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_11() throws Exception {
		try
		{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ClassRedefinition.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 5".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 12.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_12() throws Exception {
		try {
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ContBreakOutsideWhile.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); // Compile should fail
		} catch (SemanticError e) {
			// Init type table for next test.
			TypeTable.typeTableInit("ContBreakOutsideWhile.ic");
			Assert.assertTrue("semantic error at line 12".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 13.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_13() throws Exception {
		try{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ContBreakOutsideWhile2.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e){
			//Init type table for next test.
			TypeTable.typeTableInit("ContBreakOutsideWhile2.ic");
			Assert.assertTrue("semantic error at line 9".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 14.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_14() throws Exception {
		try{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ContBreakOutsideWhile3.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e){
			//Init type table for next test.
			TypeTable.typeTableInit("ContBreakOutsideWhile3.ic");
			Assert.assertTrue("semantic error at line 18".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 15.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_15() throws Exception {
		try
		{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"IfElseWhileBad.ic";
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
		try
		{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"IfElseWhileScope.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 10".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 17.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_17() throws Exception {
		try 
		{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"InvalidIntegerLiteral.ic";
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
		try
		{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"LocalVariableAssigmentFromFieldWithBadType.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 5".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 19.
	 * Bonus test!!!
	 * @throws Exception
	 */
	public void SemanticCheck_Test_19() throws Exception {
		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"LocalVarsNotInitB4Use.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"LocationsTest.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"LogicalBinaryOpsBad.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"MathBinaryOpsBad.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"MethodsCallTestBad.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"MethodsNotReturningValue.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"SingleValidMainInvalidSignature.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"SingleValidMainMoreThanOne.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"SingleValidMainNoMain.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"SymbolTablesBuilderTest.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"SymbolTablesScopeCheckTest.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"UnaryOpsBad.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"UndefineType.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 9".equals(e.getMessage().split(":")[0]));
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"VariableLocationTestBad.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 38".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 33.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_33() throws Exception {

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"example1.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"example2.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"Break,Continue.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"ComplexAssignment1.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"ComplexAssignment2.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"ComplexAssignment3.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"ComplexAssignment4.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"ComplexInit.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"ComplexReturn.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"ComplexScope1.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"ComplexScope2.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ClassGraphError.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ComplexAssignmentError1.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ComplexAssignmentError2.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ComplexAssignmentError3.ic";
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
	public void SemanticCheck_Test_48() throws Exception { //TODO : why fail ?!
		try 
		{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ComplexInitError.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ComplexReturnError.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ComplexScopeError1.ic";
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
	 * Test number 51.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_51() throws Exception {
		try 
		{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ComplexScopeError2.ic";
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
	 * Test number 52.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_52() throws Exception {
		try 
		{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"LocalVarShadowsParamError.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"NoReturnError.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"SameIDinExtendingError.ic";
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
		try 
		{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"SomeSemanticErrors.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 14".equals(e.getMessage().split(":")[0]));
		}
	}


	@Test
	/**
	 * Test number 56.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_56() throws Exception {
		
		try 
		{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"ThisTestError.ic";
			Compiler.Compile(args);
			Assert.assertTrue(false); //Compile should fail
		} catch (SemanticError e) {
			//Init type table for next test.
			TypeTable.typeTableInit("");
			Assert.assertTrue("semantic error at line 9".equals(e.getMessage().split(":")[0]));
		} //TODO : make sure why..
	}


	@Test
	/**
	 * Test number 57.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_57() throws Exception {
		try 
		{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"TwoMainsInSameClass.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"TwoMainsInSameProgram.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"VarNotInitError.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"WierdExpError1.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"WierdExpError2.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"WierdExpError3.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"WrongLibraryName.ic";
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
	 * Test number 64.
	 * @throws Exception
	 */
	public void SemanticCheck_Test_64() throws Exception {
		try 
		{
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"WrongMainSignature1.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"WrongMainSignature2.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"errors"+DIR_SEPARATOR+"WrongMainSignature3.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"example1.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"example2.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"LocalVar,FieldWithSameName.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"external"+DIR_SEPARATOR+"ThisTest.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"IfElseWhile.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"inheritance.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"LocalVariableAssigmentFromFieldWithGoodType.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"LocalVariableShadowsFieldType.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"LocalVarsInitB4Use.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"LogicalBinaryOps.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"MathBinaryOps.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"MethodsCallTest.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"MethodsReturningValue.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"AllTokensTest.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"Break,Continue.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"ComplexExp.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"ComplexIf.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"CubesTest.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"EmptyMethod.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"EmptyThingsAndDifferentOrderTest.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"Fields,Formals,Ops,This.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"IfElseTest.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"LocationsAndArrays.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"ManyClasses.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"Multi-class.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"OpsPresedences.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"PrecedenceUMINUS.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"Quicksort.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"Sieve.ic";
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
			args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"SynTest1.ic";
			Compiler.Compile(args);
			//Init type table for next test.
			TypeTable.typeTableInit("");
		} catch (SemanticError e) {
			e.printStackTrace();
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"SynTest2.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"UnaryBinaryOpTest.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"old"+DIR_SEPARATOR+"WhileWithinWhile.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"ThisInVirtualMethods.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"UnaryOps.ic";
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

		args[0] = "tests"+DIR_SEPARATOR+"test_files_pa3"+DIR_SEPARATOR+"VariableLocationTest.ic";
		Compiler.Compile(args);
		//Init type table for next test.
		TypeTable.typeTableInit("VariableLocationTest.ic");
	}




}