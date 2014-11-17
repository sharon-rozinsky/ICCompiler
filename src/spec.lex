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

%{
	StringBuffer string = new StringBuffer();
	private Token token(sym tag, Object value) {
		return new Token(yyline + 1, yycolumn + 1, tag, value.toString());
	}
	private Token token(Object value) {
		return new Token(yyline + 1, yycolumn + 1, value.toString(), value.toString());
	}
%}

%eofval{
  return token("EOF");
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
<YYINITIAL> "return"    	{ return token("return", yytext()); }
<YYINITIAL> "class"         { return token("class", yytext()); }
<YYINITIAL> "extends"       { return token("extends", yytext()); }
<YYINITIAL> "static"        { return token("static", yytext()); }
<YYINITIAL> "void"          { return token("void", yytext()); }
<YYINITIAL> "int"           { return token("int", yytext()); }
<YYINITIAL> "boolean"       { return token("boolean", yytext()); }
<YYINITIAL> "string"        { return token("string", yytext()); }
<YYINITIAL> "if"            { return token("if", yytext()); }
<YYINITIAL> "else"          { return token("else", yytext()); }
<YYINITIAL> "while"         { return token("while", yytext()); }
<YYINITIAL> "break"         { return token("break", yytext()); }
<YYINITIAL> "continue"      { return token("continue", yytext()); }
<YYINITIAL> "this"          { return token("this", yytext()); }
<YYINITIAL> "new"           { return token("new", yytext()); }
<YYINITIAL> "length"        { return token("length", yytext()); }
<YYINITIAL> "true"          { return token("true", yytext()); }
<YYINITIAL> "false"         { return token("false", yytext()); }
<YYINITIAL> "null"          { return token("null", yytext()); }

/* Operators */
<YYINITIAL> "="          	{ return token("=", yytext()); }
<YYINITIAL> "+"          	{ return token("+", yytext()); }
<YYINITIAL> "*"          	{ return token("*", yytext()); }
<YYINITIAL> "-"          	{ return token("-", yytext()); }
<YYINITIAL> "/"          	{ return token("/", yytext()); }
<YYINITIAL> ";"             { return token(";", yytext()); }
<YYINITIAL> "%"      		{ return token("%", yytext()); }
<YYINITIAL> "<"        		{ return token("<", yytext()); }
<YYINITIAL> "<="          	{ return token("<=", yytext()); }
<YYINITIAL> ">"         	{ return token(">", yytext()); }
<YYINITIAL> ">="          	{ return token(">=", yytext()); }
<YYINITIAL> "=="        	{ return token("==", yytext()); }
<YYINITIAL> "!="         	{ return token("!=", yytext()); }
<YYINITIAL> "&&"          	{ return token("&&", yytext()); }
<YYINITIAL> "||"        	{ return token("||", yytext()); }
<YYINITIAL> ","        		{ return token(",", yytext()); }

/* Symbols */
<YYINITIAL> "."            	{ return token(".", yytext()); }
<YYINITIAL> "!"         	{ return token("!", yytext()); }
<YYINITIAL> "{"         	{ return token("{", yytext()); }
<YYINITIAL> "}"         	{ return token("}", yytext()); }
<YYINITIAL> "("         	{ return token("(", yytext()); }
<YYINITIAL> ")"         	{ return token(")", yytext()); }
<YYINITIAL> "["         	{ return token("[", yytext()); }
<YYINITIAL> "]"         	{ return token("]", yytext()); }


<YYINITIAL> {
	
  	/* identifiers */ 
  	{ID}         				{ return token("ID", yytext()); }
 
  	{Class_ID}      			{ return token("CLASS_ID", yytext()); }
  
  	/* literals */
  	{DecIntegerLiteral}         { return token("INTEGER", yytext()); }
  	 \"                         { string.setLength(0); string.append('\"'); yybegin(STRING); }

  	/* comments */
  	{Comment}                   { } // *ignore*
 
  	/* whitespace */
  	{WhiteSpace}                { }
		
}

<STRING> {
  \"                             { yybegin(YYINITIAL); return token("STRING", string.toString());} 
  [^\n\r\"\\]+                   { string.append( yytext() ); string.append('\"');}
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }

  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }

}

/* error fallback */
[^]                              { throw new Error("Illegal character <"+
                                                    yytext()+">"); }
