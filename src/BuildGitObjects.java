import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.Expr;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Tuple;
import edu.mit.csail.sdg.alloy4compiler.translator.A4TupleSet;


public class BuildGitObjects {


		
	public static String gitInit(String pathindex){
		
		String line = new String();
		try{
    		String newpath = "output/"+pathindex;
			 
			 File path = new File(newpath);
			
			ProcessBuilder pb = new ProcessBuilder("git","init");
			
			pb.directory(path);	
			
			Process pr = pb.start();
			

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
	
	public static  String buildGitHashObject(String blob,String pathindex){
		
	
	String line = new String();
	
	try{
		
		String newpath = "output/"+pathindex;
		 
		 File path = new File(newpath);
		
		ProcessBuilder pb = new ProcessBuilder("git","hash-object","-w","--stdin");
		
		pb.directory(path);	
		
		Process pr = pb.start();
		
	
		
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
		System.out.println(line);
		
		br.close();
		pr.destroy();
	
	
	}catch(Exception exc){
		exc.printStackTrace();
	}
	return line;
	
	}

	public static void buildObjects(A4Solution sol,Module world, String index) throws Err
	{
		Expr e = CompUtil.parseOneExpression_fromString(world, "Blob <: object.State");
		A4TupleSet ts = (A4TupleSet) sol.eval(e);
		gitInit(index);
		for(A4Tuple t :ts )
			buildGitHashObject(t.atom(0),index);
	}		
}
