import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;





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
	 * @param		Number of filesystem to creat
	 * @see         buildFileSystem
	 */
	public static void buildFileSystem(A4Solution sol,int i,String pred_name,Module world,ExprVar preState,ExprVar posState) throws Err
	{
		 	//Get all Sigs and map them to their names(string)
			Map<String,Sig.PrimSig> mapSigs = Helper.atom2sig(sol);
			
			//Get all reachable sigs and map them to their names(string)
			HashMap<String,Sig> sigs = atom2ObjectMapE(sol.getAllReachableSigs());
		
			//Get all atoms and map them to their names(string)
			HashMap<String,ExprVar> mapAtoms = atom2ObjectMapE(sol.getAllAtoms());
		
			//Get the sig Node
			Sig nodeSig = sigs.get("this/Node");
		
			//Get the sig Dir
			Sig dir = sigs.get("this/Dir");
		
			//Get all Node's Fields and map them to their names(string)
			HashMap<String,Sig.Field> mapNodeFields = atom2ObjectMapE(nodeSig.getFields());
			
			//Get the root relation from the sig Dir
			Sig.Field root = getEFromIterable(dir.getFields(),"field (this/Dir <: root)");
			
		
			//Get the node relation from the sig Node (node maps Node with state)
			Sig.Field nodeField = mapNodeFields.get("field (this/Node <: node)");
		
			//Get blob and name relation
			Expr name = CompUtil.parseOneExpression_fromString(world, "Node <: name");
			Expr blob = CompUtil.parseOneExpression_fromString(world,"blob"); 
			
			//Get the true root
			A4TupleSet preRootDir = (A4TupleSet) sol.eval((root.join(preState)).intersect(nodeField.join(preState)));
			A4TupleSet posRootDir = (A4TupleSet) sol.eval((root.join(posState)).intersect(nodeField.join(posState)));
			A4TupleSet preRootName= (A4TupleSet) sol.eval((root.join(preState)).intersect(nodeField.join(preState)).join(name));
			A4TupleSet posRootName = (A4TupleSet) sol.eval((root.join(posState)).intersect(nodeField.join(posState)).join(name));
			//Get the parent relation
			Sig.Field parent = mapNodeFields.get("field (this/Node <: parent)");
			
			//Get the nodes in state0 that are parents
			Expr preParents = nodeField.join(preState).domain(parent);
			Expr posParents = nodeField.join(posState).domain(parent);
			
			
			//defining output path
			A4Tuple preTupleRoot = preRootDir.iterator().next();
			A4Tuple posTupleRoot = posRootDir.iterator().next();
			A4Tuple preTupleRootName = preRootName.iterator().next();
			A4Tuple posTupleRootName = posRootName.iterator().next();
			
			
			String path = "output/"+pred_name+"/"+i+"/";
			String prePath = path + "pre/" + preTupleRootName.atom(0).replace('$', '_');
			String posPath = path + "pos/" + posTupleRootName.atom(0).replace('$', '_');
			
			Path preP = Paths.get( prePath);
			Path posP = Paths.get(posPath);
			
			try {				
				Files.createDirectories(preP);
				buildTree(sol,prePath, preTupleRoot.atom(0), name,blob,preParents,mapAtoms,mapSigs);
				
				Files.createDirectories(posP);
				buildTree(sol,posPath, posTupleRoot.atom(0), name,blob,posParents,mapAtoms,mapSigs);
				
				
				
			}catch (IOException e) { System.out.println("IOException, Directory already exists!\n");}
		}
	
	
	/**
	 * 
	 * Builds recursively the filesystem tree
	 * Receives a A4Solution, the current path, the current atom, the parents, and the mapped values of all atoms and all sigs
	 * @param  		A4Solution sol
	 * @param		String path
	 * @param	    String Atom
	 * @param		Expr parent
	 * @param		HashMap<String,ExprVar> mapAtom
	 * @param		Map<String,Sig.PrimSig> mapSig
	 * @see         buildTree
	 */
	private static void buildTree(A4Solution sol,String path, String atom, Expr name,Expr blob,Expr parent,HashMap<String,ExprVar> mapAtom,Map<String,Sig.PrimSig> mapSig) throws Err, IOException
	{
		ExprVar current = mapAtom.get(atom);
//TODO
		
	
		A4TupleSet children = (A4TupleSet) sol.eval(parent.join(current));
		for(A4Tuple child : children)
		{
			
			A4TupleSet names = (A4TupleSet) sol.eval(mapAtom.get(child.atom(0)).join(name));
			String newpath = path +"/" +names.iterator().next().atom(0).replace('$', '_');
			Path p = Paths.get(newpath);
			if(mapSig.get(child.atom(0)).toString().equals("this/Dir"))
			{			
				Files.createDirectory(p);
				buildTree(sol,newpath,child.atom(0),name,blob,parent,mapAtom,mapSig);
			}
			else 
			{
				A4TupleSet blobs = (A4TupleSet) sol.eval(mapAtom.get(child.atom(0)).join(blob));
				Files.createFile(p);
				Files.write(p, blobs.iterator().next().atom(0).getBytes("ISO-8859-1"));
			}
			
			
		}
	}
}

