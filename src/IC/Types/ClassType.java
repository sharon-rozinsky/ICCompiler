package IC.Types;

import IC.AST.ICClass;

public class ClassType extends SymbolType {
	private String classUniqueName;
	private String superClassName;
	private ICClass classNode;

	public ClassType(String className, String superClassName, int unique_id, ICClass classNode) {
		super(unique_id);
		this.classUniqueName = className;
		this.superClassName = superClassName;
		this.classNode = classNode;
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
		String ret;
		if (superClassName != null){
			ret = classUniqueName+", Superclass ID: "+ TypeTable.uniqueClassTypes.get(superClassName).getTypeID();
		}
		else
		{
			ret = classUniqueName;
		}
		return ret;
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
	
	@Override
    public boolean isSubClass(SymbolType type) {
        if (this.equals(type)||(type==null)) {
            return true;
        }

        if (!(type instanceof ClassType) || superClassName == null) {
            return false;
        }
        
        return TypeTable.uniqueClassTypes.get(superClassName).isSubClass(type);
            
    }

	public ICClass getClassNode() {
		return classNode;
	}

	public void setClassNode(ICClass classNode) {
		this.classNode = classNode;
	}

}
