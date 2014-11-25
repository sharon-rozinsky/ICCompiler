/* The following code was generated by JFlex 1.6.0 */

import IC.Error.LexicalError;
import java_cup.runtime.*;
 
/**
 * IC compiler lexer
 */


class Lexer implements java_cup.runtime.Scanner {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int STRING = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\3\1\2\1\0\1\3\1\1\22\0\1\3\1\45\1\57"+
    "\2\0\1\42\1\46\1\0\1\53\1\54\1\5\1\37\1\47\1\40"+
    "\1\50\1\4\12\11\1\0\1\41\1\43\1\36\1\44\2\0\32\6"+
    "\1\55\1\60\1\56\3\0\1\21\1\30\1\17\1\24\1\13\1\32"+
    "\1\31\1\34\1\25\1\10\1\35\1\20\1\10\1\16\1\27\2\10"+
    "\1\12\1\22\1\14\1\15\1\26\1\33\1\23\2\10\1\51\1\7"+
    "\1\52\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uffff\0\uff92\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\1\1\2\2\1\3\1\4\1\5\1\1\1\6"+
    "\1\7\14\6\1\10\1\11\1\12\1\13\1\14\1\15"+
    "\1\16\1\17\1\1\1\20\1\21\1\22\1\23\1\24"+
    "\1\25\1\26\1\27\1\30\1\31\1\32\1\1\1\2"+
    "\1\0\1\33\14\6\1\34\5\6\1\35\1\36\1\37"+
    "\1\40\1\41\1\42\1\43\1\44\1\45\2\0\5\6"+
    "\1\46\6\6\1\47\5\6\1\0\1\2\1\6\1\50"+
    "\1\6\1\51\1\52\1\53\5\6\1\54\6\6\1\55"+
    "\4\6\1\56\1\6\1\57\1\60\1\61\2\6\1\62"+
    "\1\63\1\64\1\6\1\65\1\6\1\66\1\67";

  private static int [] zzUnpackAction() {
    int [] result = new int[134];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\61\0\142\0\223\0\142\0\304\0\142\0\365"+
    "\0\u0126\0\u0157\0\u0188\0\u01b9\0\u01ea\0\u021b\0\u024c\0\u027d"+
    "\0\u02ae\0\u02df\0\u0310\0\u0341\0\u0372\0\u03a3\0\u03d4\0\u0405"+
    "\0\142\0\142\0\142\0\142\0\u0436\0\u0467\0\u0498\0\u04c9"+
    "\0\142\0\142\0\142\0\142\0\142\0\142\0\142\0\142"+
    "\0\142\0\u04fa\0\142\0\u052b\0\u055c\0\u058d\0\142\0\u05be"+
    "\0\u05ef\0\u0620\0\u0651\0\u0682\0\u06b3\0\u06e4\0\u0715\0\u0746"+
    "\0\u0777\0\u07a8\0\u07d9\0\u0157\0\u080a\0\u083b\0\u086c\0\u089d"+
    "\0\u08ce\0\142\0\142\0\142\0\142\0\142\0\142\0\142"+
    "\0\142\0\142\0\u08ff\0\u0930\0\u0961\0\u0992\0\u09c3\0\u09f4"+
    "\0\u0a25\0\u0157\0\u0a56\0\u0a87\0\u0ab8\0\u0ae9\0\u0b1a\0\u0b4b"+
    "\0\u0157\0\u0b7c\0\u0bad\0\u0bde\0\u0c0f\0\u0c40\0\u0c71\0\u08ff"+
    "\0\u0ca2\0\u0157\0\u0cd3\0\u0157\0\u0157\0\u0157\0\u0d04\0\u0d35"+
    "\0\u0d66\0\u0d97\0\u0dc8\0\u0157\0\u0df9\0\u0e2a\0\u0e5b\0\u0e8c"+
    "\0\u0ebd\0\u0eee\0\u0157\0\u0f1f\0\u0f50\0\u0f81\0\u0fb2\0\u0157"+
    "\0\u0fe3\0\u0157\0\u0157\0\u0157\0\u1014\0\u1045\0\u0157\0\u0157"+
    "\0\u0157\0\u1076\0\u0157\0\u10a7\0\u0157\0\u0157";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[134];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\2\5\1\6\1\7\1\10\1\11\1\12"+
    "\1\13\1\14\1\15\1\16\1\12\1\17\1\20\1\21"+
    "\1\12\1\22\2\12\1\23\1\24\1\12\1\25\1\12"+
    "\1\26\1\27\2\12\1\30\1\31\1\32\1\33\1\34"+
    "\1\35\1\36\1\37\1\40\1\41\1\42\1\43\1\44"+
    "\1\45\1\46\1\47\1\50\1\51\1\3\1\52\2\3"+
    "\54\52\1\53\1\54\63\0\1\5\62\0\1\55\1\56"+
    "\62\0\27\10\32\0\1\57\57\0\30\12\34\0\1\13"+
    "\55\0\5\12\1\60\22\12\31\0\12\12\1\61\2\12"+
    "\1\62\12\12\31\0\4\12\1\63\21\12\1\64\1\12"+
    "\31\0\5\12\1\65\1\12\1\66\20\12\31\0\12\12"+
    "\1\67\6\12\1\70\6\12\31\0\5\12\1\71\22\12"+
    "\31\0\6\12\1\72\21\12\31\0\10\12\1\73\13\12"+
    "\1\74\3\12\31\0\21\12\1\75\6\12\31\0\4\12"+
    "\1\76\14\12\1\77\6\12\31\0\13\12\1\100\14\12"+
    "\31\0\26\12\1\101\1\12\61\0\1\102\60\0\1\103"+
    "\60\0\1\104\60\0\1\105\70\0\1\106\12\0\1\52"+
    "\2\0\54\52\14\0\1\107\1\0\1\110\1\0\1\111"+
    "\40\0\1\112\1\0\1\55\1\4\1\5\56\55\5\113"+
    "\1\114\53\113\6\0\6\12\1\115\21\12\31\0\14\12"+
    "\1\116\13\12\31\0\6\12\1\117\21\12\31\0\7\12"+
    "\1\120\20\12\31\0\17\12\1\121\10\12\31\0\25\12"+
    "\1\122\2\12\31\0\12\12\1\123\15\12\31\0\13\12"+
    "\1\124\14\12\31\0\10\12\1\125\17\12\31\0\10\12"+
    "\1\126\17\12\31\0\4\12\1\127\6\12\1\130\14\12"+
    "\31\0\6\12\1\131\21\12\31\0\17\12\1\132\10\12"+
    "\31\0\5\12\1\133\22\12\31\0\21\12\1\134\6\12"+
    "\31\0\12\12\1\135\15\12\31\0\17\12\1\136\10\12"+
    "\23\0\5\113\1\137\57\113\1\140\1\137\53\113\6\0"+
    "\7\12\1\141\20\12\31\0\5\12\1\142\22\12\31\0"+
    "\5\12\1\143\22\12\31\0\5\12\1\144\22\12\31\0"+
    "\14\12\1\145\13\12\31\0\12\12\1\146\15\12\31\0"+
    "\14\12\1\147\13\12\31\0\6\12\1\150\21\12\31\0"+
    "\23\12\1\151\4\12\31\0\17\12\1\152\10\12\31\0"+
    "\6\12\1\153\21\12\31\0\16\12\1\154\11\12\31\0"+
    "\13\12\1\155\14\12\31\0\12\12\1\156\15\12\31\0"+
    "\14\12\1\157\13\12\31\0\12\12\1\160\15\12\23\0"+
    "\4\113\1\5\1\137\53\113\6\0\4\12\1\161\23\12"+
    "\31\0\10\12\1\162\17\12\31\0\14\12\1\163\13\12"+
    "\31\0\17\12\1\164\10\12\31\0\6\12\1\165\21\12"+
    "\31\0\10\12\1\166\17\12\31\0\17\12\1\167\10\12"+
    "\31\0\27\12\1\170\31\0\5\12\1\171\22\12\31\0"+
    "\5\12\1\172\22\12\31\0\5\12\1\173\22\12\31\0"+
    "\10\12\1\174\17\12\31\0\16\12\1\175\11\12\31\0"+
    "\10\12\1\176\17\12\31\0\26\12\1\177\1\12\31\0"+
    "\23\12\1\200\4\12\31\0\11\12\1\201\16\12\31\0"+
    "\13\12\1\202\14\12\31\0\14\12\1\203\13\12\31\0"+
    "\7\12\1\204\20\12\31\0\10\12\1\205\17\12\31\0"+
    "\5\12\1\206\22\12\23\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[4312];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\2\0\1\11\1\1\1\11\1\1\1\11\21\1\4\11"+
    "\4\1\11\11\1\1\1\11\2\1\1\0\1\11\22\1"+
    "\11\11\2\0\22\1\1\0\47\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[134];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;
  
  /** 
   * The number of occupied positions in zzBuffer beyond zzEndRead.
   * When a lead/high surrogate has been read from the input stream
   * into the final zzBuffer position, this will have a value of 1;
   * otherwise, it will have a value of 0.
   */
  private int zzFinalHighSurrogate = 0;

  /* user code: */
	StringBuffer string = new StringBuffer();
	int strColumn = 0;
	private Token token(sym tag, Object value) {
		return new Token(yyline + 1, yycolumn + 1, tag, value.toString());
	}
	
	private Token token(sym tag, Object value,int strColumn) {
		return new Token(yyline + 1, strColumn + 1, tag, value.toString());
	}


  /**
   * Creates a new scanner
   *
   * @param   in  the java.io.Reader to read input from.
   */
  Lexer(java.io.Reader in) {
    this.zzReader = in;
  }


  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x110000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 156) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length - zzFinalHighSurrogate) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzBuffer.length*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
      zzEndRead += zzFinalHighSurrogate;
      zzFinalHighSurrogate = 0;
    }

    /* fill the buffer with new input */
    int requested = zzBuffer.length - zzEndRead;           
    int totalRead = 0;
    while (totalRead < requested) {
      int numRead = zzReader.read(zzBuffer, zzEndRead + totalRead, requested - totalRead);
      if (numRead == -1) {
        break;
      }
      totalRead += numRead;
    }

    if (totalRead > 0) {
      zzEndRead += totalRead;
      if (totalRead == requested) { /* possibly more input available */
        if (Character.isHighSurrogate(zzBuffer[zzEndRead - 1])) {
          --zzEndRead;
          zzFinalHighSurrogate = 1;
        }
      }
      return false;
    }

    // totalRead = 0: End of stream
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * Internal scan buffer is resized down to its initial length, if it has grown.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    zzFinalHighSurrogate = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
    if (zzBuffer.length > ZZ_BUFFERSIZE)
      zzBuffer = new char[ZZ_BUFFERSIZE];
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Contains user EOF-code, which will be executed exactly once,
   * when the end of file is reached
   */
  private void zzDoEOF() throws java.io.IOException {
    if (!zzEOFDone) {
      zzEOFDone = true;
      yyclose();
    }
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public Token next_token() throws java.io.IOException, LexicalError {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      int zzCh;
      int zzCharCount;
      for (zzCurrentPosL = zzStartRead  ;
           zzCurrentPosL < zzMarkedPosL ;
           zzCurrentPosL += zzCharCount ) {
        zzCh = Character.codePointAt(zzBufferL, zzCurrentPosL, zzMarkedPosL);
        zzCharCount = Character.charCount(zzCh);
        switch (zzCh) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn += zzCharCount;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];

      // set up zzAction for empty match case:
      int zzAttributes = zzAttrL[zzState];
      if ( (zzAttributes & 1) == 1 ) {
        zzAction = zzState;
      }


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL) {
            zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
            zzCurrentPosL += Character.charCount(zzInput);
          }
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = Character.codePointAt(zzBufferL, zzCurrentPosL, zzEndReadL);
              zzCurrentPosL += Character.charCount(zzInput);
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 1: 
          { throw new LexicalError("Illegal character: " + yytext());
          }
        case 56: break;
        case 2: 
          { 
          }
        case 57: break;
        case 3: 
          { return token(sym.DIV, yytext());
          }
        case 58: break;
        case 4: 
          { return token(sym.MUL, yytext());
          }
        case 59: break;
        case 5: 
          { return token(sym.CLASS_ID, yytext());
          }
        case 60: break;
        case 6: 
          { return token(sym.ID, yytext());
          }
        case 61: break;
        case 7: 
          { return token(sym.INTEGER, yytext());
          }
        case 62: break;
        case 8: 
          { return token(sym.EQ, yytext());
          }
        case 63: break;
        case 9: 
          { return token(sym.PLUS, yytext());
          }
        case 64: break;
        case 10: 
          { return token(sym.SUB, yytext());
          }
        case 65: break;
        case 11: 
          { return token(sym.EOL, yytext());
          }
        case 66: break;
        case 12: 
          { return token(sym.MOD, yytext());
          }
        case 67: break;
        case 13: 
          { return token(sym.ST, yytext());
          }
        case 68: break;
        case 14: 
          { return token(sym.GT, yytext());
          }
        case 69: break;
        case 15: 
          { return token(sym.NOT, yytext());
          }
        case 70: break;
        case 16: 
          { return token(sym.COMMA, yytext());
          }
        case 71: break;
        case 17: 
          { return token(sym.DOT, yytext());
          }
        case 72: break;
        case 18: 
          { return token(sym.LP, yytext());
          }
        case 73: break;
        case 19: 
          { return token(sym.RP, yytext());
          }
        case 74: break;
        case 20: 
          { return token(sym.LRP, yytext());
          }
        case 75: break;
        case 21: 
          { return token(sym.RRP, yytext());
          }
        case 76: break;
        case 22: 
          { return token(sym.LSP, yytext());
          }
        case 77: break;
        case 23: 
          { return token(sym.RSP, yytext());
          }
        case 78: break;
        case 24: 
          { string.setLength(0);strColumn = yycolumn; string.append('\"'); yybegin(STRING);
          }
        case 79: break;
        case 25: 
          { string.append( yytext() );
          }
        case 80: break;
        case 26: 
          { yybegin(YYINITIAL); return token(sym.STRING, string.append('\"').toString(),strColumn);
          }
        case 81: break;
        case 27: 
          { return token(sym.OR, yytext());
          }
        case 82: break;
        case 28: 
          { return token(sym.IF, yytext());
          }
        case 83: break;
        case 29: 
          { return token(sym.EQEQ, yytext());
          }
        case 84: break;
        case 30: 
          { return token(sym.STEQ, yytext());
          }
        case 85: break;
        case 31: 
          { return token(sym.GTEQ, yytext());
          }
        case 86: break;
        case 32: 
          { return token(sym.NOTEQ, yytext());
          }
        case 87: break;
        case 33: 
          { return token(sym.AND, yytext());
          }
        case 88: break;
        case 34: 
          { string.append("\\r");
          }
        case 89: break;
        case 35: 
          { string.append("\\t");
          }
        case 90: break;
        case 36: 
          { string.append("\\n");
          }
        case 91: break;
        case 37: 
          { string.append('\"');
          }
        case 92: break;
        case 38: 
          { return token(sym.NEW, yytext());
          }
        case 93: break;
        case 39: 
          { return token(sym.INT, yytext());
          }
        case 94: break;
        case 40: 
          { return token(sym.ELSE, yytext());
          }
        case 95: break;
        case 41: 
          { return token(sym.TRUE, yytext());
          }
        case 96: break;
        case 42: 
          { return token(sym.THIS, yytext());
          }
        case 97: break;
        case 43: 
          { return token(sym.NULL, yytext());
          }
        case 98: break;
        case 44: 
          { return token(sym.VOID, yytext());
          }
        case 99: break;
        case 45: 
          { return token(sym.CLASS, yytext());
          }
        case 100: break;
        case 46: 
          { return token(sym.BREAK, yytext());
          }
        case 101: break;
        case 47: 
          { return token(sym.FALSE, yytext());
          }
        case 102: break;
        case 48: 
          { return token(sym.WHILE, yytext());
          }
        case 103: break;
        case 49: 
          { return token(sym.RETURN, yytext());
          }
        case 104: break;
        case 50: 
          { return token(sym.LENGTH, yytext());
          }
        case 105: break;
        case 51: 
          { return token(sym.STR, yytext());
          }
        case 106: break;
        case 52: 
          { return token(sym.STATIC, yytext());
          }
        case 107: break;
        case 53: 
          { return token(sym.EXTENDS, yytext());
          }
        case 108: break;
        case 54: 
          { return token(sym.BOOLEAN, yytext());
          }
        case 109: break;
        case 55: 
          { return token(sym.CONTINUE, yytext());
          }
        case 110: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            zzDoEOF();
              {   return token(sym.EOF, "EOF");
 }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
