import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class buildGitObjects {


	public static  void main(String[] args) throws IOException{
	/*
	//Codigo teste, cuidado se executares que crias os blobs na directoria actual do git e la já tem uma mesmo que é a do vallidAlloy.
	//String msg = gitInit();
	//System.out.println(msg);
		
   	String hashcode = buildGitHashObject("blob4");
   	System.out.println(hashcode);
	String hashcode1 = buildGitHashObject("toupeira");
   	System.out.println(hashcode1);
	String hashcode2 = buildGitHashObject("febras");
   	System.out.println(hashcode2);
	String hashcode3 = buildGitHashObject("oracle");
   	System.out.println(hashcode3);
   	
   	*/
	}
	
	
	
	public static String gitInit(){
		
		String line = new String();
		try{
		
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec("git init");

			OutputStream out = pr.getOutputStream();
			InputStream in = pr.getInputStream();
			InputStream err = pr.getErrorStream();

			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
	
			line = br.readLine();
			br.close();
			pr.destroy();			
	
	}catch(Exception exc){
		exc.printStackTrace();
	}
	return line;
	
	}
	
	public static  String buildGitHashObject(String blob){
		
	
	String line = new String();
	
	try{
		
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec("git hash-object -w --stdin");
		
		OutputStream out = pr.getOutputStream();
		InputStream in = pr.getInputStream();
		InputStream err = pr.getErrorStream();

		InputStreamReader isr = new InputStreamReader(in);
		OutputStreamWriter osr = new OutputStreamWriter(out);
		
		
		BufferedReader br = new BufferedReader(isr);
		BufferedWriter bw = new BufferedWriter(osr);
		
		bw.flush();
		bw.write(blob);
		bw.close();
	
	
		line = br.readLine();
		br.close();
		pr.destroy();
	
	
	}catch(Exception exc){
		exc.printStackTrace();
	}
	return line;
	
	}

	

}
