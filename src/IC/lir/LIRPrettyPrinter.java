package IC.lir;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import IC.lir.instructions.Instruction;
import IC.lir.lirObject.ClassLayout;
import IC.lir.lirObject.LIRClass;
import IC.lir.lirObject.LIRMethod;
import IC.lir.lirObject.LIRProgram;
import IC.lir.lirObject.LIRStringLiteral;
import IC.lir.operands.AddressLabel;

public class LIRPrettyPrinter {

public static void printLIR(LIRProgram lirProgram, OutputStreamWriter streamWriter) {
        
        PrintWriter printer = new PrintWriter(streamWriter);
        
        Map<String, LIRStringLiteral> stringLiterals = lirProgram.getStringLiterals();
        Map<String, ClassLayout> classesLayouts = lirProgram.getClassesLayout();
        List<LIRClass> lirClasses = lirProgram.getClassList();

        for (LIRStringLiteral stringLiteral : stringLiterals.values()) {
            printer.println(stringLiteral.getLabel() + ": " + stringLiteral.getLiteral());
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
        
    }
}
