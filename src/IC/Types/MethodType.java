package IC.Types;

public class MethodType extends Type {
	
	private MethodContent method;

	public MethodType(MethodContent method, int id) {
		super(id);
		this.method = method;
	}

	public MethodContent getMethod() {
		return method;
	}

	public void setMethod(MethodContent method) {
		this.method = method;
	}

	@Override
	public String toString() {
		StringBuilder methodStr = new StringBuilder();
		methodStr.append("{");
		for(Type t:method.getParams())
		{
			methodStr.append(t.toString());
		}
		methodStr.append(" -> " + method.getRetType().toString() + "\n");
		
		return methodStr.toString();
	}
	
	

}
