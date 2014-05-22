import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.nio.charset.Charset;
import java.util.List;



import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.Expr;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprVar;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Tuple;
import edu.mit.csail.sdg.alloy4compiler.translator.A4TupleSet;
import edu.mit.csail.sdg.alloy4whole.Helper;
import org.pmw.tinylog.Logger;





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
	 * 
	 * Builds a filesystem from a solution.
	 * Receives a A4Solution, and a number to create the directory 
	 * @param  		A4Solution sol
	 * @param		Number of filesystem to create
	 * @see         buildFileSystem
	 */
	public static void buildFileSystem(A4Solution sol, int i, String pred_name,
    Module world, ExprVar preState, ExprVar posState) throws Err {

		 	//Get all Sigs and map them to their names(string)
			Map<String,Sig.PrimSig> mapSigs = Helper.atom2sig(sol);
			
			//Get all reachable sigs and map them to their names(string)
			HashMap<String,Sig> sigs = atom2ObjectMapE(sol.getAllReachableSigs());
		
			//Get all atoms and map them to their names(string)
			HashMap<String,ExprVar> mapAtoms = atom2ObjectMapE(sol.getAllAtoms());
		
			Sig nodeSig = sigs.get("this/Node");
			Sig dir = sigs.get("this/Dir");
		
			//Get all Node's Fields and map them to their names(string)
			HashMap<String,Sig.Field> mapNodeFields = atom2ObjectMapE(nodeSig.getFields());
			
			Expr nodeField = CompUtil.parseOneExpression_fromString(world, "current");
			Expr name = CompUtil.parseOneExpression_fromString(world,"Node <: name");
			Expr blob = CompUtil.parseOneExpression_fromString(world,"File <: content"); 
      Expr parent = CompUtil.parseOneExpression_fromString(world, "parent");
			
      // Find root node using the comprehension
			// x:Node | no (x.parent) (find nodes with no parent)
      Expr rootNode = (nodeSig.decl.get().join(parent).no()).comprehensionOver(nodeSig.decl);
			A4TupleSet rootTuple = (A4TupleSet) sol.eval(rootNode);
			
			//Get the nodes that are parents and currently in the system
			Expr preParents = nodeField.join(preState).domain(parent);
			Expr posParents = nodeField.join(posState).domain(parent);
						
			String path = "output/"+pred_name+"/"+i+"/";
			
			try{				
			  // Create pre directories and write blobs to files	
				Files.createDirectories(Paths.get(path+"pre"));
				makeRoots(sol, rootTuple, path + "pre", preParents, blob, name, mapAtoms, mapSigs);
			
			  // Create precopy directories and write blobs to files	
				Files.createDirectories(Paths.get(path+"precopy"));
				makeRoots(sol, rootTuple, path + "precopy", preParents, blob, name, mapAtoms, mapSigs);
				
			  // Create pos directories and write blobs to files	
				Files.createDirectories(Paths.get(path+"pos"));
				makeRoots(sol, rootTuple, path + "pos", posParents, blob, name, mapAtoms, mapSigs);
				
			}catch(IOException e){e.printStackTrace();}
			
		}
	
	public static void makeRoots(A4Solution sol, A4TupleSet roots, String path,
      Expr parents, Expr blob, Expr name, HashMap<String, ExprVar> mapAtoms,
      Map<String, Sig.PrimSig> mapSigs) throws Err, IOException {

		Path p = null;
		String newpath = null;
		for (A4Tuple tuple : roots) {
			A4TupleSet names = (A4TupleSet) sol.eval(mapAtoms.get(tuple.atom(0)).join(name));
			buildTree(sol,path, tuple.atom(0), name, blob, parents, mapAtoms, mapSigs);
		}
	}

	/**
	 * 
	 * Recursively builds the filesystem tree
	 * @param  		A4Solution sol
	 * @param		String path
	 * @param	    String Atom
	 * @param		Expr parent
	 * @param		HashMap<String,ExprVar> mapAtom
	 * @param		Map<String,Sig.PrimSig> mapSig
	 * @see         buildTree
	 */
	private static void buildTree(A4Solution sol, String path, String atom,
			Expr name, Expr blob, Expr parent, HashMap<String, ExprVar> mapAtom,
      Map<String, Sig.PrimSig> mapSig) throws Err, IOException {

		ExprVar current = mapAtom.get(atom);

		A4TupleSet children = (A4TupleSet) sol.eval(parent.join(current));

		for (A4Tuple child : children) {
			A4TupleSet names = (A4TupleSet) sol.eval(mapAtom.get(child.atom(0))
					.join(name));
			String newpath = path + "/"
					+ names.iterator().next().atom(0).replace('$', '_');
			Path p = Paths.get(newpath);

      // If the child is a directory, recursee
			if (mapSig.get(child.atom(0)).toString().equals("this/Dir")) {
				Files.createDirectory(p);
				buildTree(sol, newpath, child.atom(0), name, blob, parent, mapAtom, mapSig); 
			} else {
      // If the child is a file, create it and write some content
				A4TupleSet blobs = (A4TupleSet) sol.eval(mapAtom.get(
						child.atom(0)).join(blob));

				Files.createFile(p);
						
				Files.write(p, blobs.iterator().next().atom(0).getBytes("ISO-8859-1"));
        List<String> contents = Files.readAllLines(p, Charset.defaultCharset());
			}

		}
	}
}

