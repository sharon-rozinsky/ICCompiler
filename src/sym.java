
public enum sym {
    EOF("EOF"),
    ID("ID"),
    CLASS_ID("CLASS ID"),
    INTEGER("INTEGER"),
    STRING("STRING"),
    RETURN("return"),
    CLASS("class"),
    EXTENDS("extends"),
    STATIC("static"),
    VOID("void"),
    INT("int"),
    BOOLEAN("boolean"),
    STR("string"),
    IF("if"),
    ELSE("else"),
    WHILE("while"),
    BREAK("break"),
    CONTINUE("continue"),
    THIS("this"),
    NEW("new"),
    LENGTH("length"),
    TRUE("true"),
    FALSE("false"),
    NULL("null"),
    EQ("="),
    PLUS("+"),
    MUL("*"),
    SUB("/"),
    DIV("-"),
    EOL(";"),
    MOD("%"),
    ST("<"),
    STEQ("<="),
    GT(">"),
    GTEQ(">="),
    EQEQ("=="),
    NOTEQ("!="),
    AND("&&"),
    OR("||"),
    COMMA(","),
    DOT("."),
    NOT("!"),
    LP("{"),
    RP("}"),
    LRP("("),
    RRP(")"),
    LSP("["),
    RSP("]");
        
    sym(String symbolDesc){
    	this.symbolDesc = symbolDesc;
    }
    
    private String symbolDesc;
    public String getSymbolDesc(){
    	return symbolDesc;
    }
}