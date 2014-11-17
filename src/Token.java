import java_cup.runtime.Symbol;

public class Token extends Symbol{

	public Token(int sym_num) {
		super(sym_num);
		// TODO Auto-generated constructor stub
	}
	
	public Token(int line, int column, sym tag, String value){
		super(line);
		this.line = line;
		this.column = column;
		this.tag = tag;
		this.value = value;
	}

	private int line;
	private int column;
	private sym tag;
	private String value;
	
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public String getTag() {
		return tag.getSymbolDesc();
	}
	public void setTag(sym tag) {
		this.tag = tag;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
