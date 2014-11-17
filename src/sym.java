
public enum sym {
    EOF("EOF"),
    ID("ID"),
    CLASS_ID("CLASS ID"),
    INTEGER("INTEGER"),
    STRING("STRING"),
    LP("{");
    
    sym(String symbolDesc){
    	this.symbolDesc = symbolDesc;
    }
    
    private String symbolDesc;
    public String getSymbolDesc(){
    	return symbolDesc;
    }
}