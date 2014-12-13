package IC.Types;

public abstract class Type {
	private int typeID;
	
	public Type(int id){
		this.typeID = id;
	}

	public int getTypeID() {
		return typeID;
	}

	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}
	
	public boolean isType(Type type){
		if (this.typeID == type.typeID)
			return true;
		return false;
	}
}
