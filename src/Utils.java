import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import static java.nio.file.StandardCopyOption.*;
import org.pmw.tinylog.Logger;


public class Utils {

	public static<E> HashMap<String,E> atom2ObjectMapE(Iterable<E> iterable){
		E e = null;
		
		HashMap<String, E> e_Map = new HashMap<String, E>();
		Iterator<E> it = iterable.iterator();
		while(it.hasNext()){
			e = it.next();
			e_Map.put(e.toString(), e);
		}
		return e_Map;
	}
	
	
	
	/**
	 * Returns an object from a Iterable object that has the same name. 
	 * The iterable argument must be of Iterable Class, and the name a String
	 * @param  		iterable  object
	 * @param		String name
	 * @return      Object that has the same name
	 * @see         getEFromIterable
	 */
	public static <E> E getEFromIterable(Iterable<E> iterable, String name)
	{
		E res = null;
		E i = null;
		boolean found = false;
		Iterator<E> it = iterable.iterator();
		while(it.hasNext() && !found)
		{
			i=it.next();
			if(i.toString().equals(name))
				found = true;
		}
		if(found)
			res = i;
		return res;
	}
	
public static void setGitDate(String pathindex){
		
		try{
			
			String newpath = "output/"+pathindex;
			 
			File path = new File(newpath);
			System.out.println(newpath);
			
			ProcessBuilder pbA;
			pbA = new ProcessBuilder("export GIT_AUTHOR_DATE='Wed Feb 16 14:00 2037 +0100'");
			
			pbA.directory(path);	
			
			Process prA = pbA.start();
			prA.waitFor();
		/*	
			ProcessBuilder pbC;
			pbC = new ProcessBuilder("export GIT_COMMITTER_DATE='Wed Feb 16 14:00 2037 +0100'");
			
			pbC.directory(path);	
			
			Process prC = pbC.start();
			prC.waitFor();
		*/

		
		
		}catch(Exception exc){
			exc.printStackTrace();
		}

		
	}	
	
public static boolean diffPosPre(String pathindex){
		
		String line=null;
		
		StringBuilder lines = null;
		
		try{
			
			String newpath = "output/"+pathindex;
			 
			File path = new File(newpath);
			
			ProcessBuilder pb;
				
				ArrayList<String> cmds = new ArrayList<String>();
				cmds.add("diff");
				cmds.add("-r");
				cmds.add("--exclude=index");
				cmds.add("pre");
				cmds.add("pos");
				
				pb = new ProcessBuilder(cmds);
			
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
			bw.close();
		
			
			pr.waitFor();
			
			line = br.readLine();
			
			if(line != null) lines = new StringBuilder();
			
			while(line != null){
				lines.append(line+"\n");
				
				line = br.readLine();
				}
			
	
			
			
		
			
			
			if(lines != null){
				
				BufferedWriter writer = null;
				
				try
				{
					writer = new BufferedWriter( new FileWriter(newpath +"/diff.txt"));
					writer.write(lines.toString());

				}
				catch (IOException e){}
				
				finally
				{
				try
					{
						if ( writer != null)
							writer.close( );
					}
					catch ( IOException e){}
			     }
		
			br.close();
			
			}
		
		
		}catch(Exception exc){
			exc.printStackTrace();
		}


		return (lines == null) ? true : false;
		
	}

public static void diffIndex(String pathindex){
	printindex(pathindex+"/pre");
	printindex(pathindex+"/pos");
}

public static String printindex(String pathindex){
	
	String line=null;
	
	StringBuilder lines = null;
	
	try{
		
		String newpath = "output/"+pathindex;
		 
		File path = new File(newpath);
		
		ProcessBuilder pb;
			
			ArrayList<String> cmds = new ArrayList<String>();
			cmds.add("git");
			cmds.add("ls-files");
			cmds.add("--stage");
			
			pb = new ProcessBuilder(cmds);
		
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
		bw.close();
	
		
		line = br.readLine();
		
		if(line != null) lines = new StringBuilder();
		
		while(line != null){
			lines.append(line+"\n");
			
			line = br.readLine();
			}
		
		
		if(lines != null){
			
			BufferedWriter writer = null;
			
			try
			{
				writer = new BufferedWriter( new FileWriter(newpath +"/diff_index.txt"));
				writer.write(lines.toString());

			}
			catch (IOException e){}
			
			finally
			{
			try
				{
					if ( writer != null)
						writer.close( );
				}
				catch ( IOException e){}
		     }
			}
		
		br.close();
		
		pr.waitFor();
	
	
	}catch(Exception exc){
		exc.printStackTrace();
	}
	

	
	return line;
	
}

 public static String addPred(String file, String pred, String scope) throws IOException{

	 StringBuilder run = new StringBuilder();
	 run.append("run ");
	 run.append(pred);
	 run.append(" ");
	 run.append(scope);
	 
	 Path model  = Paths.get(file);
	 Path tmodel = Paths.get(file+".tmp");
	 

	try {
		Files.createFile(tmodel);
		Files.copy(model, tmodel,REPLACE_EXISTING);
	} catch (FileAlreadyExistsException e1) {
		System.out.println("Model Temporary File Already Exists \n Replacing With New Content");
	}
	 
	

	 try {
		    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file+".tmp", true)));
		    out.println(run.toString());
		    out.close();
		} catch (IOException e) {
		}
	 
	 
	 return file+".tmp";
 }
 
 public static void delTemporaryModel(String file) throws IOException{
	 
	Path tmodel = Paths.get(file+".tmp");
	
	Files.deleteIfExists(tmodel);
 }
}
