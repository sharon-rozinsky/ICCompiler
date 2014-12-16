package IC.Types;

public abstract class SymbolType implements Comparable<SymbolType>{
	private int typeID;
	
	public SymbolType(int id){
		this.typeID = id;
	}

	public int getTypeID() {
		return typeID;
	}

	public void setTypeID(int typeID) {
		this.typeID = typeID;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + typeID;
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
		SymbolType other = (SymbolType) obj;
		if (typeID != other.typeID)
			return false;
		return true;
	}
	
	public boolean isSubClass(SymbolType type) {
        if (this == type) {
            return true;
        }
        return false;
    }
	
	@Override
	public int compareTo(SymbolType o) {
		if(this.getTypeID() > o.getTypeID())
		{
			return 1;
		}
		else if(this.getTypeID() < o.getTypeID())
		{
			return -1;
		}
		else
		{
			return 0;
		}
	}


}
