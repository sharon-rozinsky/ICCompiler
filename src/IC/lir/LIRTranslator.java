package IC.lir;

import IC.AST.ASTNode;
import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.Break;
import IC.AST.CallStatement;
import IC.AST.Continue;
import IC.AST.ExpressionBlock;
import IC.AST.Field;
import IC.AST.Formal;
import IC.AST.ICClass;
import IC.AST.If;
import IC.AST.LIRPropagatingVisitor;
import IC.AST.Length;
import IC.AST.LibraryMethod;
import IC.AST.Literal;
import IC.AST.LocalVariable;
import IC.AST.LogicalBinaryOp;
import IC.AST.LogicalUnaryOp;
import IC.AST.MathBinaryOp;
import IC.AST.MathUnaryOp;
import IC.AST.Method;
import IC.AST.NewArray;
import IC.AST.NewClass;
import IC.AST.PrimitiveType;
import IC.AST.Program;
import IC.AST.PropagatingVisitor;
import IC.AST.Return;
import IC.AST.StatementsBlock;
import IC.AST.StaticCall;
import IC.AST.StaticMethod;
import IC.AST.This;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.While;
import IC.SemanticChecks.SemanticError;
import IC.Types.TypeTable;
import IC.lir.instructions.Instruction;
import IC.lir.instructions.LibraryInstruction;
import IC.lir.instructions.ReturnInstruction;
import IC.lir.lirObject.LIRClass;
import IC.lir.lirObject.LIRMethod;
import IC.lir.lirObject.LIRProgram;
import IC.lir.operands.AddressLabel;
import IC.lir.operands.DummyRegister;
import IC.lir.operands.Immediate;
import IC.lir.operands.LibraryLabel;
import IC.lir.operands.Operand;
import IC.lir.operands.Register;

public class LIRTranslator implements LIRPropagatingVisitor<Object, Object>{

	private LIRProgram program;
	
	public LIRTranslator(LIRProgram program){
		this.program = program;
		Register.setRegCount(0);
		AddressLabel.setLabelId(0);
	}
	
	public LIRProgram getProgram(){
		return program;
	}
	
	private Object propagate(ASTNode node, Object scope) {
		if(node != null){
			return node.accept(this, scope);
		}
		return null;
	}
	
	private void propagate(Iterable iterable, Object scope){
		if(iterable != null){
			for(Object node : iterable){
				propagate((ASTNode)node, scope);
			}
		}
	}
	
	@Override
	public Object visit(Program program, Object scope) {
		propagate(program.getClasses(), null);
		return null;
	}

	@Override
	public Object visit(ICClass icClass, Object scope)  {
		LIRClass lirClass = new LIRClass(new AddressLabel(icClass.getName()));
		propagate(icClass.getMethods(), lirClass);
		program.addClass(lirClass);
		return null;
	}

	@Override
	public Object visit(Field field, Object scope)  {
		return null;
	}

	private void visitMethod(Method method, Object scope) {
		String methodName = method.getName();
		LIRClass lirClass = (LIRClass) scope;
		LIRMethod lirMethod = new LIRMethod(lirClass.getClassName(), new AddressLabel(methodName));
		
		propagate(method.getStatements(), lirMethod);
		Instruction inst = null;
		if(method instanceof StaticMethod){
			if(methodName.equals(LIRConstants.MAIN_METHOD_NAME)){
				Operand[] params = new Operand[] { new Immediate(0) };
				LibraryLabel exitLabel = new LibraryLabel(LIRConstants.LIBRARY_EXIT);
				inst = new LibraryInstruction(exitLabel, new DummyRegister(), params);
			} else if(method.getSymbolType() == TypeTable.voidType){
				inst = new ReturnInstruction(new DummyRegister());
			}
			lirMethod.addInstruction(inst);
		} else if(method.getSymbolType() == TypeTable.voidType){
			inst = new ReturnInstruction(new DummyRegister());
			lirMethod.addInstruction(inst);
		}
		
		lirClass.addMethod(lirMethod);
	}
	
	@Override
	public Object visit(VirtualMethod method, Object scope) {
		visitMethod(method, scope);
		return null;
	}

	@Override
	public Object visit(StaticMethod method, Object scope)  {
		visitMethod(method, scope);
		return null;
	}

	@Override
	public Object visit(LibraryMethod method, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Formal formal, Object scope)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(PrimitiveType type, Object scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(UserType type, Object scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Assignment assignment, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(CallStatement callStatement, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Return returnStatement, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(If ifStatement, Object scope)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(While whileStatement, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Break breakStatement, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Continue continueStatement, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VariableLocation location, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ArrayLocation location, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StaticCall call, Object scope)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VirtualCall call, Object scope)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(This thisExpression, Object scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NewClass newClass, Object scope)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NewArray newArray, Object scope)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Length length, Object scope)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp, Object scope)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Literal literal, Object scope)  {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock, Object scope)
			 {
		// TODO Auto-generated method stub
		return null;
	}

}
