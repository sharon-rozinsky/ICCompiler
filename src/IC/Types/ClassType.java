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
        return classUniqueName;
    }
    

}
