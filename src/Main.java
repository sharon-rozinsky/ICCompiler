import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;


public class Main {

	public static void main(String[] args) {
		Reader reader;
		ArrayList<Token> tokArr = new ArrayList<Token>();
		try {
			reader = new FileReader(new File("text.txt"));
			Lexer lexer = new Lexer(reader);
			Token t = lexer.next_token();
			PrintHeader();
			while(t.getTag() != "EOF"){
				PrintToken(t.getValue(), t.getTag(), t.getLine(), t.getColumn());
				tokArr.add(t);
				t = lexer.next_token();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			
		}
		System.out.println("Done!");
	}
	
	public static void PrintToken(String token, String tag, int line, int column){
		System.out.println(token + "\t" + tag + "\t" + line + ":" + column);
	}
	
	public static void PrintHeader(){
		System.out.println("token\ttag\tline :column");
	}
	
	

}

// Command to run from terminal to create: Lexer.java