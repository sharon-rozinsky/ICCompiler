package lir;

public class LirConstants {
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
	public static String GT = "GT"; 	// greater than
	public static String GET = "GET"; 	// greater/Equal
	public static String LT = "LT";		// lesser than
	public static String LET = "LET";	// lesser/Equal
	
	// Unary Ops
	public static String Inc = "Inc";	// increment
	public static String Dec = "Dec";	// decrement
	public static String Neg = "Neg";	// unary subtraction - minus
	public static String Not = "Not"; 	// logical negation
	
	// Memory Ops
	public static String Store = "Store";
	public static String Load = "Load";
	
	// 
	public static String Label = "Label";
	public static String Comment = "Comment";
	
	
}
