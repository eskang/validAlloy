import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprVar;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;

import org.antlr.runtime.*;



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
	@SuppressWarnings("null")
	public static void main(String[] args)  throws Err, IOException{
		
		 A4Reporter rep = new A4Reporter() {
	            // For example, here we choose to display each "warning" by printing it to System.out
	            @Override public void warning(ErrorWarning msg) {
	                System.out.print("Relevance Warning:\n"+(msg.toString().trim())+"\n\n");
	                System.out.flush();
	            }
	        };
	        
	    	if(args.length == 1){
	    		String input_text = args[0];
	    		
	    		System.out.println("=========== Parsing Config File + Typechecking "+input_text+" =============");
	    		
	    		CfgLexer lex = new CfgLexer(new ANTLRFileStream(input_text, "UTF8"));
	            	CommonTokenStream tokens = new CommonTokenStream(lex);

	            	CfgParser g = new CfgParser(tokens);
	            	
	            	ArrayList<HashMap<String,String>> vars ; 
	    			ArrayList<ArrayList<String>> arg ;
	    			ArrayList<ArrayList<String>> opts = null ;
	    			ArrayList<String> preds ;
	    			ArrayList<String> scopes ;
	    			ArrayList<String> cmds = null ;
	    			int n_cmds ;
	    			int n_runs = 0 ;
	                  	
	            	try {
	            		
	            		CfgParser.cfg_return cfg_obj = g.cfg();
	            	
	            		vars = cfg_obj.vars;
	            		arg = cfg_obj.args;
	            		opts = cfg_obj.opts;
	            		preds = cfg_obj.preds;
	            		scopes = cfg_obj.scopes;
	            		cmds = cfg_obj.cmds;
	            		n_cmds = cfg_obj.n_comands;
	            		n_runs = cfg_obj.n_runs;
	    			
	            	System.out.println(vars);
	    			System.out.println(arg);
	    			System.out.println(opts);
	    			System.out.println(preds);
	    			System.out.println(scopes);
	    			System.out.println(cmds);
	    			System.out.println(n_runs);
	    			System.out.println(n_cmds);
	    			
	            	} catch (RecognitionException e) {
	                	e.printStackTrace();
	            	}
	    	
	     
	        int test_iterations = n_runs;
	        
	        
	        
	
	        Module world = CompUtil.parseEverything_fromFile(rep, null, "src/git_dynamic.als");
	        
	        A4Solution sol=null ;
	        
	        // Choose some default options for how you want to execute the commands
	        A4Options options = new A4Options();
	        options.solver = A4Options.SatSolver.SAT4J;
	        Command cmd1 = world.getAllCommands().get(0);
	        sol = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), cmd1, options);
	        System.out.println("=========== Getting "+ test_iterations +" Solutions from "+"git_dynamic.als"+" =============");
	        String newpath = null;
	        Path p  = null;
	        Iterable<ExprVar> skolems = null;
	        ExprVar preState = null;
	        ExprVar posState = null;
	        ExprVar pathSkol = null;
	        for(int i =0; i< test_iterations;i++){
	        	if (sol.satisfiable())
	        	{
	        		HashMap<String,ExprVar> mapAtom =Utils.atom2ObjectMapE(sol.getAllAtoms());
	        		newpath = "output/"+Integer.toString(i);
	        		p = Paths.get(newpath);skolems = sol.getAllSkolems();
	        		Files.createDirectories(p);
	        		
	        		preState= Utils.getEFromIterable(skolems, "$show_s");
	        		posState = Utils.getEFromIterable(skolems, "$show_s'");
	        		pathSkol = Utils.getEFromIterable(skolems, "$show_p");
	        		
	        		System.out.println(preState);
	        		System.out.println(posState);
	        		FileSystemBuilder.buildFileSystem(sol,i,world,preState,posState);
	        		
	        		System.out.println("Instance "+i+" preState\n__________________________________________________________________");
	        		BuildGitObjects.buildObjects(sol, world, Integer.toString(i)+"/pre",preState,mapAtom);
	        		
	        		System.out.println("Instance "+i+" posState\n__________________________________________________________________");
	        		BuildGitObjects.buildObjects(sol, world, Integer.toString(i)+"/pos",posState,mapAtom);
	        		
	        		BuildGitObjects.runCmd(sol,world,"output/"+Integer.toString(i)+"/pre",pathSkol,mapAtom,cmds.get(0),opts.get(0));
	        		
	        		Utils.diffIndex(Integer.toString(i));
	        		Utils.diffPosPre(Integer.toString(i));
	        		
	        		System.out.println("End of Instance: "+i+"\n__________________________________________________________________");
	        		sol.writeXML("output/"+i+"/instance"+i+".xml");
	        		}
	        	sol=sol.next();
	        	
	        } 
	    }
	  }
   }
	
	
