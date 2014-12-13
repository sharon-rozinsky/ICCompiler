package IC.Types;

public class ClassType extends Type{
	private String classUniqueName;
	private String superClassName;

	public ClassType(String className, String superClassName, int unique_id) {
		super(unique_id);
		this.classUniqueName = className;
		this.superClassName = superClassName;
	}

	public String getClassName() {
		return classUniqueName;
	}

	public void setClassId(String className) {
		this.classUniqueName = className;
	}

	public String getSuperClassId() {
		return superClassName;
	}

	public void setSuperClassId(String superClassName) {
		this.superClassName = superClassName;
	}

    public String toString() {
        return classUniqueName+", Superclass ID: "+ TypeTable.uniqueClassTypes.get(superClassName).getTypeID();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((classUniqueName == null) ? 0 : classUniqueName.hashCode());
		result = prime * result
				+ ((superClassName == null) ? 0 : superClassName.hashCode());
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
		ClassType other = (ClassType) obj;
		if (classUniqueName == null) {
			if (other.classUniqueName != null)
				return false;
		} else if (!classUniqueName.equals(other.classUniqueName))
			return false;
		if (superClassName == null) {
			if (other.superClassName != null)
				return false;
		} else if (!superClassName.equals(other.superClassName))
			return false;
		return true;
	}
	
	public boolean isSubClass(Type type){
		ClassType cType = (ClassType) type;
		if (super.getTypeID() == type.getTypeID()) return true; // good?
		
		
		return false;
	}
    

}
