package IC.Types;

public class ArrayType extends Type{

	private Type elementsType;
	private int arraySize;
	
	public ArrayType(Type elemType, int size, int unique_id) {
		super(unique_id);
		this.elementsType = elemType;
		this.arraySize = size;
	}

	public Type getElementsType() {
		return elementsType;
	}

	public void setElementsType(Type elementsType) {
		this.elementsType = elementsType;
	}
	
	
	public String toString(){   	
		return elementsType.toString()+" array";    	
    }

	public int getDimentions() {
		return arraySize;
	}

	public void setDimentions(int size) {
		arraySize = size;
	}

}
