import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;


public class Main {

	public static void main(String[] args) {
		Reader reader;
		try {
			reader = new FileReader(new File("text.txt"));
			Lexer lexer = new Lexer(reader);
			//int c = 0;
			Token t = lexer.next_token();
			while(t.getTag() != "EOF"){
				PrintToken(t.getValue(), t.getTag(), t.getLine(), t.getColumn());
				t = lexer.next_token();
				//System.out.println(c++);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e){
			
		}
	}
	
	public static void PrintToken(String token, String tag, int line, int column){
		System.out.println(token + "\t" + tag + "\t" + line + ":" + column);
	}

}
