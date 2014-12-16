package IC.Types;

public class NullType extends SymbolType{

	public NullType(int id) {
		super(id);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString(){
		return "null";  	
    }
	
	public boolean isSubClass(SymbolType type) {
        return true;
    }
        
}
