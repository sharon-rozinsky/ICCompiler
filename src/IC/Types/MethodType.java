package IC.Types;

public class MethodType {
	
	private int id;
	private Method method;

	public MethodType(Method method, int id) {
		this.id = id;
		this.method = method;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

}
