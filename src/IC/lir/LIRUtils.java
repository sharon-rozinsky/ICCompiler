package IC.lir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import IC.AST.Field;
import IC.AST.ICClass;
import IC.AST.Method;
import IC.AST.Program;
import IC.AST.VirtualMethod;
import IC.lir.instructions.Instruction;
import IC.lir.lirObject.LIRClass;
import IC.lir.lirObject.LIRMethod;
import IC.lir.lirObject.LIRProgram;
import IC.lir.lirObject.LIRStringLiteral;
import IC.lir.operands.AddressLabel;

public class LIRUtils {
	
	public static LIRProgram getLIRProgram(Program program){
		Map<String, ClassLayout> classLayouts;
		Map<String, LIRStringLiteral> stringLiterals;
		
		classLayouts = createClassLayout(program);
		StringLiteralExtractor stringLiteralExtractor = new StringLiteralExtractor();
		stringLiteralExtractor.visit(program);
		stringLiterals = stringLiteralExtractor.getStringLiterals();
		
		LIRProgram lirProgram = new LIRProgram(classLayouts, stringLiterals);
		LIRTranslator lirTranslator = new LIRTranslator(lirProgram);
		lirTranslator.visit(program, null);
		return lirProgram;
	}
	/**
	 * Create the class layout map for a given program.
	 * This method will traverse the program classes, methods and fields
	 * and create a mapping between a class name and a ClassLayoutObject
	 * @param program
	 * @return
	 */
	public static Map<String, ClassLayout> createClassLayout(Program program){
		Map<String, ClassLayout> classLayoutMap = new HashMap<String, ClassLayout>();
		
		for(ICClass icClass : program.getClasses()){
			ClassLayout layout;
			String className = icClass.getName();
			String superClassName = icClass.getSuperClassName();
			
			if(className.equals(LIRConstants.LIBRARY)){
				//the Library class doesn't have a class layout
				continue;
			}
			
			// handle super class
			if(icClass.hasSuperClass()){
				layout = new ClassLayout(className, classLayoutMap.get(superClassName));
			} else {
				layout = new ClassLayout(className);
			}
			
			// handle methods
			for(Method method : icClass.getMethods()){
				if(method instanceof VirtualMethod){
					layout.addMethodOffset(method.getName());
				}
			}
			
			// handle fields
			for(Field field : icClass.getFields()){
				layout.addFieldOffset(field.getName());
			}
			
			classLayoutMap.put(className, layout);
		}
		
		return classLayoutMap;
	}
	
	public static String addBackSlash(String str){
    	String literalValue = str;
    	if(str.equals("\"\n\""))
			literalValue = "\"\\n\"";
    	else if(str.equals("\"\t\""))
			literalValue = "\"\\t\"";

    	return literalValue;
    }
	
	public static void printLIR(LIRProgram lirProgram, String fileName) {
		FileWriter fileWriter;
		PrintWriter printer = null;
		
		try{
			fileWriter = new FileWriter(new File(fileName + ".lir"));
			printer = new PrintWriter(fileWriter);
	
			Map<String, LIRStringLiteral> stringLiterals = lirProgram.getStringLiterals();
			Map<String, ClassLayout> classesLayouts = lirProgram.getClassesLayout();
			List<LIRClass> lirClasses = lirProgram.getClassList();
	
			for (LIRStringLiteral stringLiteral : stringLiterals.values()) {
				printer.println(stringLiteral.getLabel() + ": " + addBackSlash(stringLiteral.getLiteral()));
			}
	
			printer.println();
	
			for (ClassLayout classLayout : classesLayouts.values()) {
				printer.println(classLayout.toString());
			}
	
			printer.println();
	
			for (LIRClass lirClass : lirClasses) {
				for (LIRMethod lirMethod : lirClass.getMethods()) {
	
					AddressLabel methodLabel = lirMethod.getMethodName();
					AddressLabel classLabel;
	
					if (methodLabel.getName().equals(LIRConstants.MAIN_LABEL_SUFFIX))
						classLabel = new AddressLabel(LIRConstants.MAIN_LABEL_PREFIX);
					else
						classLabel = lirMethod.getContainingClassName(); 
	
					AddressLabel label = classLabel.append(methodLabel);
	
					printer.println(label.toString() + ":");
	
					for (Instruction instruction : lirMethod.getInstructions()) {
						printer.println(instruction.toString());
					}
	
					printer.println();
	
				}
			}
		} catch (IOException e){
			System.out.println("Error while printing .lir file");
		} finally {
			printer.close();
		}
	}

}
