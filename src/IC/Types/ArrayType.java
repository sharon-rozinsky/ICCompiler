package IC.Types;

public class ArrayType extends Type{

	private Type elementsType;
	
	public ArrayType(Type elementsType, int unique_id) {
		super(unique_id);
		this.elementsType = elementsType;
	}

    public Type getElementsType() {
            return elementsType;
    }

	public void setElementsType(Type elementsType) {
		this.elementsType = elementsType;
	}
	
    @Override
    public String toString() {       
        return elementsType.toString() + "[]";
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((elementsType == null) ? 0 : elementsType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArrayType other = (ArrayType) obj;
		if (elementsType == null) {
			if (other.elementsType != null)
				return false;
		} else if (!elementsType.equals(other.elementsType))
			return false;
		return true;
	}


}
