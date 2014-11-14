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

  private Token token(String tag, Object value) {
    return new Token(yyline, yycolumn, tag, value.toString());
  }
  private Token token(Object value) {
    return new Token(yyline, yycolumn, value.toString(), value.toString());
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
ID 						= [a-z] ([a-z|0-9])*

//ID = [:jletter:] [:jletterdigit:]*

DecIntegerLiteral = 0|[1-9][0-9]*
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
<YYINITIAL> "/s"          	{/* ignore */}

<YYINITIAL> {
  /* identifiers */ 
  {ID}         		{ return token("ID", yytext()); }
 
 {Class_ID}      { return token("CLASS_ID", yytext()); }
  
  /* literals */
  {DecIntegerLiteral}            { return token("INTEGER", yytext()); }

  /* comments */
  {Comment}                      { return token("Comment", yytext());} // *ignore*
 
  /* whitespace */
  {WhiteSpace}                   { return token("WS", yytext());}
}



