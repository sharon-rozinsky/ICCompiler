package IC.Types;

public class MethodType extends Type {
	
	private Method method;

	public MethodType(Method method, int id) {
		super(id);
		this.method = method;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
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
