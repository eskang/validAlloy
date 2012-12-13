import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;





import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorWarning;
import edu.mit.csail.sdg.alloy4.SafeList;
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


public class FileSystemBuilder {

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
	public static Sig getSigFromSafeList(SafeList<Sig> sl, String name)
	{
		return getEFromIterable(sl,name);
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
	            
	            SafeList<Sig> sigs = world.getAllSigs();
	            Sig state = getSigFromSafeList(sigs,"this/State");
	            
	            Expr expr1 = state.one().and(world.getAllReachableFacts());
	            Command cmd1 = new Command(false,3,-1,-1,expr1);
	             
	           
	            
	            // Choose some default options for how you want to execute the commands
	            A4Options options = new A4Options();
	            options.solver = A4Options.SatSolver.SAT4J;
	            
	            A4Solution sol = TranslateAlloyToKodkod.execute_command(rep, world.getAllReachableSigs(), cmd1, options);
	            buildFileSystem(sol);
	            	
	            	}
	        }
	
	
	public static void buildFileSystem(A4Solution sol) throws Err
	{
		if (sol.satisfiable()){
			SafeList<Sig> sigs = sol.getAllReachableSigs();
			Iterable<ExprVar> it =sol.getAllAtoms();
			ExprVar state0 = getEFromIterable(it,"State$0");
			Sig node = getSigFromSafeList(sigs,"this/Node");
			Sig dir = getSigFromSafeList(sigs,"this/Dir");
			Sig.Field root = getEFromIterable(dir.getFields(),"field (this/Dir <: root)");
			A4TupleSet rootdir = (A4TupleSet) sol.eval(root.join(state0));	            	
			Sig.Field parent = getEFromIterable(node.getFields(),"field (this/Node <: parent");
			A4Tuple tupleroot = rootdir.iterator().next();
			String path = tupleroot.atom(0).replace('$', '_');
			
			Path p = Paths.get(path);
			try {Files.createDirectory(p);
			}catch (Exception e) { ;}
			
			System.out.println(path);
		}
	}
}
