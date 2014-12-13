package IC.Types;

import java.util.Arrays;

public class Method {
	
	private String methodName;
	private Type retType;
	private Type[] params;
	private Type context; //The scope in which this method is defined (classType). 
	private boolean StaticRef;
	
	
	public Method(String methodName, Type retType, Type[] params, Type context,
			boolean staticRef) {
		super();
		this.methodName = methodName;
		this.retType = retType;
		this.params = params;
		this.context = context;
		StaticRef = staticRef;
	}


	public String getMethodName() {
		return methodName;
	}


	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}


	public Type getRetType() {
		return retType;
	}


	public void setRetType(Type retType) {
		this.retType = retType;
	}


	public Type[] getParams() {
		return params;
	}


	public void setParams(Type[] params) {
		this.params = params;
	}


	public Type getContext() {
		return context;
	}


	public void setContext(Type context) {
		this.context = context;
	}


	public boolean isStaticRef() {
		return StaticRef;
	}


	public void setStaticRef(boolean staticRef) {
		StaticRef = staticRef;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (StaticRef ? 1231 : 1237);
		result = prime * result + ((context == null) ? 0 : context.hashCode());
		result = prime * result
				+ ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + Arrays.hashCode(params);
		result = prime * result + ((retType == null) ? 0 : retType.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Method other = (Method) obj;
		if (StaticRef != other.StaticRef)
			return false;
		if (context == null) {
			if (other.context != null)
				return false;
		} else if (!context.equals(other.context))
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (!Arrays.equals(params, other.params))
			return false;
		if (retType == null) {
			if (other.retType != null)
				return false;
		} else if (!retType.equals(other.retType))
			return false;
		return true;
	}
	
	

}
