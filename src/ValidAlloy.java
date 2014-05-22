import java.io.File;

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
import org.apache.commons.io.FileUtils;
import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.LoggingLevel;
import org.pmw.tinylog.writers.FileWriter;




public class ValidAlloy {
	/**
	 * Main method that buils a number of git filesystems, from a number of alloy instances.
	 * It has two inputs, the file with the alloy model and the number of git filesystem to create
	 * @param  		git alloy model(.als)
	 * @param		Number of filesystem to create
	 * @return 
	 * @return      N git filesystems
	 * @throws IOException 
	 * @throws Err 
	 * @see         main
	 */
    public static void executeValidAlloy(String model, String config) throws IOException, Err{
        // Create reporter for alloy
        A4Reporter rep = new A4Reporter() {
                // For example, here we choose to display each "warning" by printing it to System.out
                @Override public void warning(ErrorWarning msg) {
                    System.out.print("Relevance Warning:\n"+(msg.toString().trim())+"\n\n");
                    System.out.flush();
                }
            };
        

        ArrayList<HashMap<String,String>> vars = null ; 
        ArrayList<ArrayList<String>> arg = null;
        ArrayList<ArrayList<String>> opts = null ;
        ArrayList<String> preds = null ;
        ArrayList<String> scopes = null ;
        ArrayList<String> cmds = null ;
        ArrayList<ArrayList<String>> errors = null ;
        int n_cmds = 0 ;
        int n_runs = 0 ;

    	  // Delete "output" directory	
        FileUtils.deleteDirectory(new File("output"));
        System.out.println("===========         Parsing + Typechecking          =========== ");
    	
        // Parse cfg file	
        CfgLexer lex = new CfgLexer(new ANTLRFileStream(config, "UTF8"));
        CommonTokenStream tokens = new CommonTokenStream(lex);

        CfgParser g = new CfgParser(tokens);
                  	
        try {
            Logger.info("Parsing Config File \n");
           
            CfgParser.cfg_return cfg_obj = g.cfg();
            	
            vars = cfg_obj.vars;
            arg = cfg_obj.args;
            opts = cfg_obj.opts;
            preds = cfg_obj.preds;
            scopes = cfg_obj.scopes;
            cmds = cfg_obj.cmds;
            n_cmds = cfg_obj.n_comands;
            n_runs = cfg_obj.n_runs;
            errors = cfg_obj.errors;
            
            System.out.println("Model               : " + model);
            System.out.println("Config File         : " + config);
            System.out.println("Number of runs      : " + n_runs);
            System.out.println("Number of commands  : " + n_cmds);
        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    	
     
        int test_iterations = n_runs;

        
        for(int j = 0 ; j < n_cmds; j++){
	
            // Start logging
            Configurator.defaultConfig().writer(new FileWriter("log.txt")).level(LoggingLevel.TRACE).activate();
            
            // Delete the temporary model 
            Utils.delTemporaryModel(model);	
        
            Logger.info("Preds: " + preds.get(j));
            Logger.info("Scopes: " + scopes.get(j));
            Logger.info("Argument to CompUtil.parseEverything_fromFile: " + Utils.addPred(model, preds.get(j), scopes.get(j))); 
           
            // Creates Module from model, reports to rep 
            Module world = CompUtil.parseEverything_fromFile(rep, null, Utils.addPred(model, preds.get(j), scopes.get(j)));
             
            Logger.info("Parsing Alloy File for "+preds.get(j));
        
            A4Solution sol=null ;
        
            Logger.info("Commands: " + world.getAllCommands());

            // Create A4Options object to solve SAT with SAT4J
            A4Options options = new A4Options();
            options.solver = A4Options.SatSolver.SAT4J;

            // Get the first command in the module
            Command cmd1 = world.getAllCommands().get(world.getAllCommands().size()-1);

            // Execute cmd1 according to options
            sol = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), cmd1, options);
            System.out.println("=========== Getting  solutions from git.als =========== ");
            System.out.println("Variables           : " +vars.get(j));
            System.out.println("Predicate           : " +preds.get(j));
            System.out.println("Arguments           : " +arg.get(j));
            System.out.println("Scope               : " +scopes.get(j));
            System.out.println("Commands            : " +cmds.get(j));
            System.out.println("Options             : " +opts.get(j));
            System.out.println("Expected Errors     : " +errors.get(j));
            System.out.println("===========             Running Instances           =========== ");
            
            boolean flagerr;	
            boolean flagcontinue = true;
            for (int i = 0; i < test_iterations && flagcontinue; i++) {
            	flagerr = false;
        	
            	Logger.info("********* Beginning of Instance : "+i+" *********\n\n\n\n\n ");
  
            	String newpath = null;
            	Path p  = null;
            	Iterable<ExprVar> skolems = null;
            	ExprVar preState = null;
            	ExprVar posState = null;
            	ArrayList<ExprVar> pathSkol = new ArrayList<ExprVar>();
            	ArrayList<String> dirs2remove = new ArrayList<String>();
        	
	            // If there is a solution to the constraints
            	if (sol.satisfiable()) {

                // Turn atoms from solution into a map from their string representation
                // to the ExprVar object
            		HashMap<String,ExprVar> mapAtom = Utils.atom2ObjectMapE(sol.getAllAtoms());
                Logger.info("mapAtom: " + mapAtom);

                // Create the path for the git instances e.g. "output/commit/0"
                newpath = "output/"+preds.get(j)+"/"+Integer.toString(i);
                p = Paths.get(newpath);
                Files.createDirectories(p);

                skolems = sol.getAllSkolems();

                
                // Values for the pre and pos state e.g. "$commit_s" 
                String preS = "$" + preds.get(j) +"_s";
                String posS = "$" + preds.get(j) +"_s'";
                    
                preState = Utils.getEFromIterable(skolems, preS);
                posState = Utils.getEFromIterable(skolems, posS);
                    
                if (arg.get(j) != null) {

                	for (int t=0; t<arg.get(j).size(); t++) {
                            
                        String skol = "$" + preds.get(j) +"_" + arg.get(j).get(t);
                        pathSkol.add(Utils.getEFromIterable(skolems, skol));
                            
                	}
            	  }
               
                // Create folders and files in the pre, precopy, and pos working directories as described
                // by the solution
                FileSystemBuilder.buildFileSystem(sol,i,preds.get(j),world,preState,posState);
                
                // e.g. "output/commit/0"
                String cmdpath = "output/"+preds.get(j)+"/"+Integer.toString(i);
                    

                // Build the git instances in the pre, precopy, and pos directories
                Logger.info("Instance "+i+" preState\n________________________________________________________________");
                BuildGitObjects.buildObjects(sol, world, preds.get(j)+"/"+Integer.toString(i)+"/pre",preState,mapAtom);
                  
                BuildGitObjects.buildObjects(sol, world, preds.get(j)+"/"+Integer.toString(i)+"/precopy",preState,mapAtom);
                    
                Logger.info("\nInstance "+i+" posState\n________________________________________________________________");
                BuildGitObjects.buildObjects(sol, world, preds.get(j)+"/"+Integer.toString(i)+"/pos",posState,mapAtom);
                    
                // Now that the initial git structures have been build, try to run the command we are concerned with
                try {
                    if (!pathSkol.isEmpty())					
                        BuildGitObjects.runCmd(sol, world, cmdpath + "/pre", pathSkol.get(0), mapAtom, cmds.get(j), opts.get(j), vars.get(j));
                    else 
                        BuildGitObjects.runCmd(sol, world, cmdpath + "/pre", null, mapAtom, cmds.get(j), opts.get(j), null);
                } catch (GitException e) {
                    // If git gives us an error message, mark the instance as having an error and write it to git_errors.txt
                    if(!Utils.ContainsExpectedErrors(e.getMessage(),errors.get(j))){
                        Path p_e = Paths.get(cmdpath+"/git_errors.txt");
                        Files.createFile(p_e);
                        Files.write(p_e, e.getMessage().getBytes("ISO-8859-1"));
                        Files.move(Paths.get(cmdpath), Paths.get(cmdpath+"_err"));
                        cmdpath = cmdpath+"_err";
                        flagerr = true;
                        sol.writeXML(cmdpath+"/instance"+i+".xml");
                    } else dirs2remove.add(cmdpath);
                } catch (Exception q){
                    q.printStackTrace();
                }
                if (!flagerr) {
                  // If pos and pre are the same, delete them
                	if (Utils.diffPosPre(preds.get(j)+"/"+Integer.toString(i))) {
                		FileUtils.deleteDirectory(new File(cmdpath)); 	                
                	} else { 
                  // Otherwise write the differences to file
                        try {sol.writeXML("output/"+preds.get(j)+"/"+i+"/instance"+i+".xml");}
                        catch(Err e){e.printStackTrace();System.out.println(e.getMessage());}
                	}
                }
                Logger.info("********* End of Instance       : "+i+" ********* ");		
                sol=sol.next();
            	} else {
                flagcontinue = false;
              }
            	if (!dirs2remove.isEmpty()){
            		Utils.RemoveDirs(dirs2remove);
              }        	
            }
        	FileUtils.moveFileToDirectory(new File("log.txt"), new File("output/"+preds.get(j)),false);
          System.out.println("\n===========             Command terminated          =========== ");
        }
      Utils.delTemporaryModel(model);
    }
    
    
    // Runs executeValidAlloy with the model and configuation file as arguments
    public static void main(String[] args)  throws Err, IOException{
        
        System.out.println("===========                ValidAlloy               =========== ");    
        
        if (args.length == 2) {
            executeValidAlloy(args[0],args[1]);	    	    	
        } else System.out.println("Must provide model and config file to ValidAlloy");
        
        System.out.println("===========           ValidAlloy terminated         =========== ");
    }
}	

	
