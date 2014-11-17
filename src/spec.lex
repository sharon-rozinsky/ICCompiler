import java_cup.runtime.*;
 
/**
 * IC compiler lexer
 */

%%
%line
%column
%type Token
%class Lexer
%cup
%yylexthrow LexicalError

%{
	StringBuffer string = new StringBuffer();
	int strColumn = 0;
	private Token token(sym tag, Object value) {
		return new Token(yyline + 1, yycolumn + 1, tag, value.toString());
	}
	
	private Token token(sym tag, Object value,int strColumn) {
		return new Token(yyline + 1, strColumn + 1, tag, value.toString());
	}
%}

%eofval{
  token(sym.EOF, yytext());
%eofval}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* comments */
Comment = {TraditionalComment} | {EndOfLineComment} | {DocumentationComment}

TraditionalComment   	= "/*" [^*] ~"*/" | "/*" "*"+ "/"

// Comment can be the last line of the file, without line terminator.

EndOfLineComment     	= "//" {InputCharacter}* {LineTerminator}?
DocumentationComment 	= "/**" {CommentContent} "*"+ "/"
CommentContent       	= ( [^*] | \*+ [^/*] )*
Class_ID				= [A-Z][a-z|0-9]*
ID 						= [a-z] ([A-Z|a-z|0-9])*
DecIntegerLiteral 		= [0-9]+


%state STRING

%%
 /* keywords */
<YYINITIAL> "return"    	{ return token(sym.RETURN, yytext()); }
<YYINITIAL> "class"         { return token(sym.CLASS, yytext()); }
<YYINITIAL> "extends"       { return token(sym.EXTENDS, yytext()); }
<YYINITIAL> "static"        { return token(sym.STATIC, yytext()); }
<YYINITIAL> "void"          { return token(sym.VOID, yytext()); }
<YYINITIAL> "int"           { return token(sym.INT, yytext()); }
<YYINITIAL> "boolean"       { return token(sym.BOOLEAN, yytext()); }
<YYINITIAL> "string"        { return token(sym.STR, yytext()); }
<YYINITIAL> "if"            { return token(sym.IF, yytext()); }
<YYINITIAL> "else"          { return token(sym.ELSE, yytext()); }
<YYINITIAL> "while"         { return token(sym.WHILE, yytext()); }
<YYINITIAL> "break"         { return token(sym.BREAK, yytext()); }
<YYINITIAL> "continue"      { return token(sym.CONTINUE, yytext()); }
<YYINITIAL> "this"          { return token(sym.THIS, yytext()); }
<YYINITIAL> "new"           { return token(sym.NEW, yytext()); }
<YYINITIAL> "length"        { return token(sym.LENGTH, yytext()); }
<YYINITIAL> "true"          { return token(sym.TRUE, yytext()); }
<YYINITIAL> "false"         { return token(sym.FALSE, yytext()); }
<YYINITIAL> "null"          { return token(sym.NULL, yytext()); }

/* Operators */
<YYINITIAL> "="          	{ return token(sym.EQ, yytext()); }
<YYINITIAL> "+"          	{ return token(sym.PLUS, yytext()); }
<YYINITIAL> "*"          	{ return token(sym.MUL, yytext()); }
<YYINITIAL> "-"          	{ return token(sym.SUB, yytext()); }
<YYINITIAL> "/"          	{ return token(sym.DIV, yytext()); }
<YYINITIAL> ";"             { return token(sym.EOL, yytext()); }
<YYINITIAL> "%"      		{ return token(sym.MOD, yytext()); }
<YYINITIAL> "<"        		{ return token(sym.ST, yytext()); }
<YYINITIAL> "<="          	{ return token(sym.STEQ, yytext()); }
<YYINITIAL> ">"         	{ return token(sym.GT, yytext()); }
<YYINITIAL> ">="          	{ return token(sym.GTEQ, yytext()); }
<YYINITIAL> "=="        	{ return token(sym.EQEQ, yytext()); }
<YYINITIAL> "!="         	{ return token(sym.NOTEQ, yytext()); }
<YYINITIAL> "&&"          	{ return token(sym.AND, yytext()); }
<YYINITIAL> "||"        	{ return token(sym.OR, yytext()); }
<YYINITIAL> ","        		{ return token(sym.COMMA, yytext()); }

/* Symbols */
<YYINITIAL> "."            	{ return token(sym.DOT, yytext()); }
<YYINITIAL> "!"         	{ return token(sym.NOT, yytext()); }
<YYINITIAL> "{"         	{ return token(sym.LP, yytext()); }
<YYINITIAL> "}"         	{ return token(sym.RP, yytext()); }
<YYINITIAL> "("         	{ return token(sym.LRP, yytext()); }
<YYINITIAL> ")"         	{ return token(sym.RRP, yytext()); }
<YYINITIAL> "["         	{ return token(sym.LSP, yytext()); }
<YYINITIAL> "]"         	{ return token(sym.RSP, yytext()); }


<YYINITIAL> {
	
  	/* identifiers */ 
  	{ID}         				{ return token(sym.ID, yytext()); }
 
  	{Class_ID}      			{ return token(sym.CLASS_ID, yytext()); }
  
  	/* literals */
  	{DecIntegerLiteral}         { return token(sym.INTEGER, yytext()); }
  	 \"                         { string.setLength(0);strColumn = yycolumn; string.append('\"'); yybegin(STRING); }

  	/* comments */
  	{Comment}                   { } // *ignore*
 
  	/* whitespace */
  	{WhiteSpace}                { }
		
}

<STRING> {
  \"                             { yybegin(YYINITIAL); return token(sym.STRING, string.toString(),strColumn);} 
  [^\n\r\"\\]+                   { string.append( yytext() ); string.append('\"');}
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }

}

/* error fallback */
[^]                              { throw new LexicalError("Illegal character: " + yytext()); }
