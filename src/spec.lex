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

TraditionalComment   = "/*" [^*] ~"*/" | "/*" "*"+ "/"
// Comment can be the last line of the file, without line terminator.
EndOfLineComment     = "//" {InputCharacter}* {LineTerminator}?
DocumentationComment = "/**" {CommentContent} "*"+ "/"
CommentContent       = ( [^*] | \*+ [^/*] )*

ID = [:jletter:] [:jletterdigit:]*

DecIntegerLiteral = 0 | [1-9][0-9]*
%state STRING

%%
 /* keywords */
<YYINITIAL> {ID}           { return token("ID", yytext()); }