package operands;

import IC.Symbols.Symbol;

public class Memory implements Operand{
	private Symbol symbol;

	public Memory(Symbol symbol){
		this.symbol = symbol;
	}
	
	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}
	
	@Override
	public String toString(){
		return symbol.getId(); //TODO: + symbol.getScopeId?
	}
}
