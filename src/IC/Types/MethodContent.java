package IC.Types;

import java.util.Arrays;

public class MethodContent {
	
	private String methodName;
	private SymbolType retType;
	private SymbolType[] params;
	private boolean StaticRef;
	
	
	public MethodContent(String methodName, SymbolType retType, SymbolType[] params, boolean staticRef) {
		super();
		this.methodName = methodName;
		this.retType = retType;
		this.params = params;
		StaticRef = staticRef;
	}


	public String getMethodName() {
		return methodName;
	}


	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}


	public SymbolType getRetType() {
		return retType;
	}


	public void setRetType(SymbolType retType) {
		this.retType = retType;
	}


	public SymbolType[] getParams() {
		return params;
	}


	public void setParams(SymbolType[] params) {
		this.params = params;
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
		MethodContent other = (MethodContent) obj;
		if (StaticRef != other.StaticRef)
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
