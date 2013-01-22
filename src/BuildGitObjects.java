import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


import edu.mit.csail.sdg.alloy4.ConstList;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.Decl;
import edu.mit.csail.sdg.alloy4compiler.ast.Expr;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprVar;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Tuple;
import edu.mit.csail.sdg.alloy4compiler.translator.A4TupleSet;


public class BuildGitObjects {

	private static String pathindex;
		
	public static String gitInit(){
		
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
	
	public static  String buildGitHashObject(String blob){
		
	
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

	
	public static String buildTreeEntry(String type,String hashcode,String ref){
		String mode;
		if(type.equals("this/Blob")){
			mode = "100644";
			type = "blob";
		}
		else{
			mode = "040000";
			type = "tree";
		}
		String entry = mode + " " + type + " " + hashcode + "\t" + ref + "\n";
		
		return entry;
		
	}
	
	
	
	
	public static String buildGitTree(ArrayList<String> entrys){
	
		String hashcode = null;
	
		try{
			
			StringBuilder tree = new StringBuilder();
			
			for(String line : entrys){
				tree.append(line);
			}
			 
			System.out.println(tree.toString());
			 
			String newpath = "output/"+pathindex;
			 
			File path = new File(newpath);
			
			ProcessBuilder pb = new ProcessBuilder("git","mktree");
			
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
			bw.write(tree.toString());
			bw.close();
		
		
			hashcode = br.readLine();
			System.out.println(hashcode);
			
			br.close();
			pr.destroy();
		
		
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return hashcode;
		
	}
	
	public static String buildCommitTree(String tree_hashcode, String message, String commit_hashcode){
		
		String hashcode = null;
	
		try{
			
			String newpath = "output/"+pathindex;
			 
			File path = new File(newpath);
			
			ProcessBuilder pb;
			
			if(commit_hashcode.compareTo("FIRST_COMMIT")==0){
				pb = new ProcessBuilder("git","commit-tree",tree_hashcode);
			}
			else {
				pb = new ProcessBuilder("git","commit-tree",tree_hashcode,"-p",commit_hashcode);
			}
			
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
			bw.write(hashcode);
			bw.close();
		
		
			hashcode = br.readLine();
			System.out.println(hashcode);
			
			br.close();
			pr.destroy();
		
		
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return hashcode;
		
	}
	

	/*
	public static void buildObjects(A4Solution sol,Module world, String index) throws Err
	{
		//TODO: Change .State join an atom instead (only works because there's exactly 1 state in the run command)
		HashMap<String,ExprVar> mapAtom =Utils.atom2ObjectMapE(sol.getAllAtoms());
		HashMap<String,String> mapObjsHash = new HashMap<String,String>();
		pathindex = index;
		Expr blobs = CompUtil.parseOneExpression_fromString(world, "Blob <: object.State");
		Expr content = CompUtil.parseOneExpression_fromString(world, "content");
		//String starter= " (object.State <:Tree - content.Tree.univ)";
		Expr Tree = CompUtil.parseOneExpression_fromString(world, "object.State <:Tree");
		Expr univ = CompUtil.parseOneExpression_fromString(world, "univ");
		 
		//"object.State <: {t : Tree - previousTrees | univ.(t.content) :> Tree in  previousTrees}"
		LinkedList<ExprVar> aux = new LinkedList<ExprVar>();
		aux.add(ExprVar.make(null, "t",Tree.type()));
		Expr previousTrees = CompUtil.parseOneExpression_fromString(world, "none :> Tree");
		Decl tDecl = new Decl(null,null,null,aux,Tree);
		Expr treeExpr = univ.join(tDecl.get().join(content)).range(Tree).in(previousTrees); 
		treeExpr = treeExpr.comprehensionOver(tDecl);
		
		A4TupleSet ts = (A4TupleSet) sol.eval(blobs);
		gitInit();
		for(A4Tuple t :ts )
			mapObjsHash.put(t.atom(0),buildGitHashObject(t.atom(0)));
			
		
		A4TupleSet trees = (A4TupleSet) sol.eval(treeExpr);
		while(trees.size()>0)
		{ 
			buildTrees(sol,trees,content,mapAtom,mapObjsHash);
			for(A4Tuple t : trees)
			{
				previousTrees = previousTrees.plus(mapAtom.get(t.atom(0)));
				tDecl = new Decl(null,null,null,aux,Tree.minus(previousTrees)); 
				treeExpr = univ.join(tDecl.get().join(content)).range(Tree).in(previousTrees); 
				treeExpr = treeExpr.comprehensionOver(tDecl);
			}
			
			trees = (A4TupleSet) sol.eval(treeExpr);
		}
	}		
	
	public static void buildTrees(A4Solution sol, A4TupleSet trees,Expr content,HashMap<String,ExprVar> mapAtoms,HashMap<String,String> mapObjsHash)throws Err
	{
		ArrayList<String> entries;
		ExprVar tree;
		A4TupleSet lines;
		for(A4Tuple t : trees)
		{
			entries  = new ArrayList<String>();
			tree = mapAtoms.get(t.atom(0));
			lines = (A4TupleSet) sol.eval(tree.join(content));
			for(A4Tuple line: lines)
				entries.add(buildTreeEntry(line.sig(1).toString(),mapObjsHash.get(line.atom(1)),line.atom(0)));
		 mapObjsHash.put(t.atom(0),buildGitTree(entries));
		}
	}
	*/
	
}
