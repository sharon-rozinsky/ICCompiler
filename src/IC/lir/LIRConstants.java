package IC.lir;

public class LIRConstants {
	// Binary Ops
	public static String Add = "Add";
	public static String Sub = "Sub";
	public static String Mul = "Mul";
	public static String Div = "Div";
	public static String Mod = "Mod";
	public static String And = "And";
	public static String Or = "Or";
	public static String Xor = "Xor";
	public static String Compare = "Compare";
	// Binary Ops- Branch Conditions
	public static String Do = ""; 		// Always do
	public static String True = "True";
	public static String False = "False";
	public static String GT = "G"; 	// greater than
	public static String GET = "GE"; 	// greater/Equal
	public static String LT = "L";		// lesser than
	public static String LET = "LE";	// lesser/Equal
	
	// Unary Ops
	public static String Inc = "Inc";	// increment
	public static String Dec = "Dec";	// decrement
	public static String Neg = "Neg";	// unary subtraction - minus
	public static String Not = "Not"; 	// logical negation
	
	// Memory Ops
	public static String Store = "Store";
	public static int STORE = 1;
	public static String Load = "Load";
	public static int LOAD = 2;
	
	// 
	public static String Label = "Label";
	public static String Comment = "Comment";
	

	// 
	public static String MAIN_LABEL_PREFIX = "ic";
	public static String MAIN_LABEL_SUFFIX = "_main";
	public static String STRING_LITERAL_PREFIX = "stringLiteral";

	public static final String DV_LABEL = "DV";
	public static final String LIBRARY = "Library";
	
	public static final String END_BOOL_LABEL_PREFIX = "end_bool_";
	public static final String TEST_COND_LABEL_PREFIX = "test_cond_label_";
	public static final String END_LABEL_PREFIX = "end_label_";
    public static final String FALSE_LABEL_PREFIX = "false_label_";
    public static final String LIBRARY_EXIT = "exit";
    public static final String MAIN_METHOD_NAME = "main";
    
	public static final String RUN_TIME_STRING_LITERALS = "stringLiteral0: \"Runtime Error: Null pointer dereference!\n\""
			+ "stringLiteral1: \"Runtime Error: Array index out of bounds!\n\""
			+ "stringLiteral2: \"Runtime Error: Array allocation with negative array size!\n\""
			+ "stringLiteral3: \"Runtime Error: Division by zero!\"";
}
