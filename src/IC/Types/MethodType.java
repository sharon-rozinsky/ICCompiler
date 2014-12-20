package IC.Types;

public class MethodType extends SymbolType {
	
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
		if (method!=null)
		{
			int numP = method.getParams().length;
			for (int i=0; i<numP; i++)
			{
				methodStr.append(method.getParams()[i].toString());
				if (i!= numP-1)
				{
					methodStr.append(", ");
				}
			}
		
//		for(SymbolType t:method.getParams())
//		{
//			methodStr.append(t.toString()+", ");
//		}
			methodStr.append(" -> " + method.getRetType().toString());
		}
		methodStr.append("}");
		return methodStr.toString();
	}
	
	

}
