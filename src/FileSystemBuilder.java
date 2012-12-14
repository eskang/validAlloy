import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;





import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.Expr;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprVar;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Tuple;
import edu.mit.csail.sdg.alloy4compiler.translator.A4TupleSet;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.alloy4viz.VizGUI;
import edu.mit.csail.sdg.alloy4whole.Helper;





/**
 * Builds a filesystem from an alloy instance. 
 */
public class FileSystemBuilder {

	/**
	 * Returns an HashMap object that the key is a string and the value is an object. 
	 * The iterable argument must be of Iterable Class.
	 * @param  		iterable  object
	 * @return      HashMap object that the key is a string and the value is an object
	 * @see         atom2ObjectMapE
	 */
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
	
	
	/**
	 * Main method that buils a number of filesystem from an alloy
	 * The iterable argument must be of Iterable Class, and the name a String
	 * @param  		iterable  object
	 * @param		String name
	 * @return      Object that has the same name
	 * @see         getEFromIterable
	 */
	public static void main(String[] args)  throws Err{
		
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
	        System.out.println("=========== Parsing+Typechecking "+filename+" =============");
	        Module world = CompUtil.parseEverything_fromFile(rep, null, filename);
	            
	            /*
	            SafeList<Sig> sigs = world.getAllSigs();
	            Sig state = getEFromIterable(sigs,"this/State");
	            
	           Expr expr1 = state.one().and(world.getAllReachableFacts());
	            System.out.println(expr1);
	            Command cmd1 = new Command(false,5,-1,-1,expr1);
	             */
	            A4Solution sol=null ;
	            
	            // Choose some default options for how you want to execute the commands
	            A4Options options = new A4Options();
	            options.solver = A4Options.SatSolver.SAT4J;
	            Command cmd1 = world.getAllCommands().get(0);
	            sol = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), cmd1, options);
	            for(int i =0; i< test_iterations;i++){
	            	if (sol.satisfiable())
	            	{
	            		buildFileSystem(sol,i);
	            		sol.writeXML("output/"+i+"/instance"+i+".xml");
	            	}
	            	sol=sol.next();
	            }
	        }
	
	
	public static void buildFileSystem(A4Solution sol,int i) throws Err
	{
			Map<String,Sig.PrimSig> mapSigs = Helper.atom2sig(sol);
			HashMap<String,Sig> sigs = atom2ObjectMapE(sol.getAllReachableSigs());
			HashMap<String,ExprVar> mapAtoms = atom2ObjectMapE(sol.getAllAtoms());
			
			ExprVar state0 = mapAtoms.get("State$0");
			Sig nodeSig = sigs.get("this/Node");
			Sig dir = sigs.get("this/Dir");
			HashMap<String,Sig.Field> mapNodeFields = atom2ObjectMapE(nodeSig.getFields());
			Sig.Field root = getEFromIterable(dir.getFields(),"field (this/Dir <: root)");
			Sig.Field nodeField = mapNodeFields.get("field (this/Node <: node)");
			//System.out.println(nodeField);
			A4TupleSet rootdir = (A4TupleSet) sol.eval((root.join(state0)).intersect(nodeField.join(state0)));
			Sig.Field parent = mapNodeFields.get("field (this/Node <: parent)");
			A4Tuple tupleroot = rootdir.iterator().next();
			String path = tupleroot.atom(0).replace('$', '_');
			
			Expr parents = nodeField.join(state0).domain(parent);
	
			path = "output/"+i+"/"+path;
			Path p = Paths.get( path);
			try {				
				Files.createDirectories(p);
				buildTree(sol,path, tupleroot.atom(0), parents,mapAtoms,mapSigs);
			}catch (IOException e) { System.out.println("IOException, Directory already exists!\n");}
		}
	
	private static void buildTree(A4Solution sol,String path, String atom, Expr parent,HashMap<String,ExprVar> mapAtom,Map<String,Sig.PrimSig> mapSig) throws Err, IOException
	{
		ExprVar current = mapAtom.get(atom);

		A4TupleSet children = (A4TupleSet) sol.eval(parent.join(current));
		for(A4Tuple child : children)
		{
			String newpath = path +"/" +child.atom(0).replace('$', '_');
			Path p = Paths.get(newpath);
			if(mapSig.get(child.atom(0)).toString().equals("this/Dir"))
			{
				Files.createDirectory(p);
				buildTree(sol,newpath,child.atom(0),parent,mapAtom,mapSig);
			}
			else 
				Files.createFile(p);
		}
	}
}

