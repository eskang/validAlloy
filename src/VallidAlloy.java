import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;




public class VallidAlloy {
	
	
	/**
	 * Main method that buils a number of git filesystems, from a number of alloy instances.
	 * It has two inputs, the file with the alloy model and the number of git filesystem to create
	 * @param  		git alloy model(.als)
	 * @param		Number of filesystem to create
	 * @return      N git filesystems
	 * @throws IOException 
	 * @see         main
	 */
	public static void main(String[] args)  throws Err, IOException{
		
		 A4Reporter rep = new A4Reporter() {
	            // For example, here we choose to display each "warning" by printing it to System.out
	            @Override public void warning(ErrorWarning msg) {
	                System.out.print("Relevance Warning:\n"+(msg.toString().trim())+"\n\n");
	                System.out.flush();
	            }
	        };

	        String filename = args[0];
	        int test_iterations = Integer.parseInt(args[1]);
	        // Parse+typecheck the model
	        System.out.println("=========== Parsing + Typechecking "+filename+" =============");
	        Module world = CompUtil.parseEverything_fromFile(rep, null, filename);
	            
	            
	            A4Solution sol=null ;
	            
	            // Choose some default options for how you want to execute the commands
	            A4Options options = new A4Options();
	            options.solver = A4Options.SatSolver.SAT4J;
	            Command cmd1 = world.getAllCommands().get(0);
	            sol = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), cmd1, options);
	            System.out.println("=========== Getting "+ test_iterations +" Solutions from "+filename+" =============");
	            for(int i =0; i< test_iterations;i++){
	            	if (sol.satisfiable())
	            	{	
	            		
	            		
	            		String newpath = "output/"+Integer.toString(i);
	        			Path p = Paths.get(newpath);
	        			Files.createDirectories(p);
	        			FileSystemBuilder.buildFileSystem(sol,i);
	        			BuildGitObjects.buildObjects(sol, world, Integer.toString(i));
	            		System.out.println(i);
	            		
	            		sol.writeXML("output/"+i+"/instance"+i+".xml");
	            	}
	            	sol=sol.next();
	            }
	        }
	
}
