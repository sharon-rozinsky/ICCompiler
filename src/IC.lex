import java_cup.runtime.*;
import IC.Error.*;
 
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
	private Token token(int tag, Object value) {
		return new Token(yyline + 1, yycolumn + 1, tag, value.toString());
	}
	
	private Token token(int tag, Object value,int strColumn) {
		return new Token(yyline + 1, strColumn + 1, tag, value.toString());
	}
%}

WhiteSpace=[ \t\n\r]

/* comments */
ONELINECOMMENTSIGN ="//"
MULTIPLELINECOMMENTSIGN =	"/*"

QUOTE=\"
PRINTABLE_STRING = 		[\040\041\043-\133\135-\176]
SEQUENCE_STRING = 		\\[nt\"\\]
VALID_STRING_LETTER = 	{PRINTABLE_STRING}|{SEQUENCE_STRING}

UPPERCASE =		[A-Z]
LOWERCASE =		[a-z]
LETTER =		{UPPERCASE}|{LOWERCASE}
DIGIT = 		[0-9]
ALPHA_NUMERIC = {DIGIT}|{LETTER}
ID_SUFFIX = 	({ALPHA_NUMERIC}|_)*

Class_ID =		{UPPERCASE}{ID_SUFFIX}
ID 		= 		{LOWERCASE}{ID_SUFFIX}
INTEGER = 		[0-9]+

/* 
        NOTE: accepted range here is [-2147483648,2147483648]. 
        Min/Max.INT will be handled within the parser.
        Single line Comment can be the last line of the file, without line terminator.
*/
OUT_OF_RANGE_INTEGER=
        [1-9][0-9]{10}[0-9]*|
        [3-9][0-9]{9}[0-9]*|
        2[2-9][0-9]{8}[0-9]*|
        21[5-8][0-9]{7}[0-9]*|
        214[8-9][0-9]{6}[0-9]*|
        2147[5-9][0-9]{5}[0-9]*|
        21474[9-9][0-9]{4}[0-9]*|
        214748[4-9][0-9]{3}[0-9]*|
        2147483[7-9][0-9]{2}[0-9]*|
        21474836[5-9][0-9]{1}[0-9]*|
        214748364[9-9][0-9]*
        
LITERAL_ERROR={DIGIT}+({LETTER}|_)+

%state STRING
%state SINGLELINECOMMENT
%state MULTILINECOMMENT
%state MULTILINECOMMENTASTERISK


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
  	{LITERAL_ERROR}     		{ throw new LexicalError("bad format token: " + yytext());}
    {OUT_OF_RANGE_INTEGER} 		{ throw new LexicalError("number is out of valid range: " + yytext()); }
  	
  	/* literals */
  	{INTEGER}         			{ return token(sym.INTEGER, yytext()); }
  	{QUOTE}                     { 	
  									string.setLength(0);
  									strColumn = yycolumn; 
  									string.append('\"'); 
  									yybegin(STRING); 
  								}
  	/* comments */
  	{ONELINECOMMENTSIGN}    	{ yybegin(SINGLELINECOMMENT);   }
    {MULTIPLELINECOMMENTSIGN} 	{ yybegin(MULTILINECOMMENT);    }
 
  	/* whitespace */
  	{WhiteSpace}                { }
		
}

<SINGLELINECOMMENT> {
    [\n]            			{ yybegin(YYINITIAL); }
    [^\n]           			{ }
}

<MULTILINECOMMENT> {
    [*]             			{ yybegin(MULTILINECOMMENTASTERISK); }
    [^*]            			{ }
    <<EOF>>         			{ throw new LexicalError("Unclosed comment"); }
}

<MULTILINECOMMENTASTERISK> {
	[*]             			{ }
    [/]            				{ yybegin(YYINITIAL); }
    [^/*]           			{ yybegin(MULTILINECOMMENT); }
    <<EOF>>         			{ throw new LexicalError("Unclosed comment"); }
}

<STRING> {
 	{QUOTE}             	{ 
								yybegin(YYINITIAL); 
							  	return token(sym.STRING, string.append('\"').toString(),strColumn);
						 	} 
  	{VALID_STRING_LETTER}+  { string.append(yytext()); }       
	<<EOF>>         		{ throw new LexicalError("Ununclosed string literal"); }
    \r                      { throw new LexicalError("Illegal character in string literal '\\r'"); }
    \n                      { throw new LexicalError("Illegal character in string literal '\\n'"); }  
    \t                      { throw new LexicalError("Illegal character in string literal '\\t'"); }  
    .                       { throw new LexicalError("Illegal character in string literal '" + yytext() + "'"); }  

}

<<EOF>>	{ return token(sym.EOF, "EOF");}

/* error fallback */
[^]                          { throw new LexicalError("Illegal character: " + yytext()); }
