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


public class FileSystemBuilder {

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
	
	
	
	public static void main(String[] args)  throws Err{
		
		 A4Reporter rep = new A4Reporter() {
	            // For example, here we choose to display each "warning" by printing it to System.out
	            @Override public void warning(ErrorWarning msg) {
	                System.out.print("Relevance Warning:\n"+(msg.toString().trim())+"\n\n");
	                System.out.flush();
	            }
	        };

	        for(String filename:args) {

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
	            for(Command cmd1: world.getAllCommands()) {
	            	sol = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), cmd1, options);
	            	sol =sol.next();
	            	if (sol.satisfiable()){
	            		buildFileSystem(sol);
	            		sol.writeXML("instance.xml");
	            		new VizGUI(false,"instance.xml",null);
	            	}
	            }
	            
	            }
	        }
	
	
	public static void buildFileSystem(A4Solution sol) throws Err
	{
		
			Map<String,Sig.PrimSig> mapSigs = Helper.atom2sig(sol);
			HashMap<String,Sig> sigs = atom2ObjectMapE(sol.getAllReachableSigs());
			HashMap<String,ExprVar> mapAtoms = atom2ObjectMapE(sol.getAllAtoms());
			ExprVar state0 = mapAtoms.get("State$0");
			Sig node = sigs.get("this/Node");
			Sig dir = sigs.get("this/Dir");
			Sig.Field root = getEFromIterable(dir.getFields(),"field (this/Dir <: root)");
			A4TupleSet rootdir = (A4TupleSet) sol.eval(root.join(state0));	
			Sig.Field parent = getEFromIterable(node.getFields(),"field (this/Node <: parent)");
			A4Tuple tupleroot = rootdir.iterator().next();
			String path = tupleroot.atom(0).replace('$', '_');
		
			Path p = Paths.get(path);
			try {
				Files.createDirectory(p);
				buildTree(sol,path, tupleroot.atom(0), parent,mapAtoms,mapSigs);
			}catch (IOException e) { System.out.println("IOException, provavelmente directoria já existe");}
		}
	
	private static void buildTree(A4Solution sol,String path, String atom, Sig.Field parent,HashMap<String,ExprVar> mapAtom,Map<String,Sig.PrimSig> mapSig) throws Err, IOException
	{
		ExprVar current = mapAtom.get(atom);

		A4TupleSet filhos = (A4TupleSet) sol.eval(parent.join(current));
		for(A4Tuple filho : filhos)
		{
			String newpath = path +"/" +filho.atom(0).replace('$', '_');
			Path p = Paths.get(newpath);
			if(mapSig.get(filho.atom(0)).toString().equals("this/Dir"))
			{
				Files.createDirectory(p);
				buildTree(sol,newpath,filho.atom(0),parent,mapAtom,mapSig);
			}
			else 
				Files.createFile(p);
		}
	}
}

