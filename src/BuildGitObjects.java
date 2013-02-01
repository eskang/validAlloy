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




import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.Decl;
import edu.mit.csail.sdg.alloy4compiler.ast.Expr;
import edu.mit.csail.sdg.alloy4compiler.ast.ExprVar;
import edu.mit.csail.sdg.alloy4compiler.ast.Module;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
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
		System.out.println("blob hash:  " + line);
		
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
			 
			System.out.println("arvore:\n " + tree.toString());
			 
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
			System.out.println("tree hash (apos mostrar tab)" + hashcode);
			
			br.close();
			pr.destroy();
		
		
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return hashcode;
		
	}
	
	public static String buildCommitTree(String tree_hashcode, String message, ArrayList<String> commits){
		  
		  String hashcode = null;
		 
		  try{
		   
		   String newpath = "output/"+pathindex;
		    
		   File path = new File(newpath);
		   
		   ProcessBuilder pb;
		   
		   if(commits.get(0).compareTo("FIRST_COMMIT")==0){
		    pb = new ProcessBuilder("git","commit-tree",tree_hashcode);
		   }
		   else {
		    
		    ArrayList<String> cmds = new ArrayList<String>();
		    cmds.add("git");
		    cmds.add("commit-tree");
		    cmds.add(tree_hashcode);
		    
		    for(String com : commits){
		     cmds.add("-p");
		     cmds.add(com);
		    }
		    
		    pb = new ProcessBuilder(cmds);
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
		   bw.write(message);
		   bw.close();
		  
		  
		   hashcode = br.readLine();
		   System.out.println("commit hash : "+ hashcode);
		   
		   br.close();
		   pr.destroy();
		  
		  
		  }catch(Exception exc){
		   exc.printStackTrace();
		  }
		  return hashcode;
		  
		 }
	

	
	public static void buildObjects(A4Solution sol,Module world, String index,ExprVar iState) throws Err
	{
		//TODO: Change .State, join an atom instead (only works because there's exactly 1 state in the run command)
		HashMap<String,ExprVar> mapAtom =Utils.atom2ObjectMapE(sol.getAllAtoms());
		HashMap<String,String> mapObjsHash = new HashMap<String,String>();
		pathindex = index;
		Expr domain = CompUtil.parseOneExpression_fromString(world, "object").join(iState);
		Expr blobs = CompUtil.parseOneExpression_fromString(world, "Blob").domain(domain);
		 
		
		
		
		A4TupleSet ts = (A4TupleSet) sol.eval(blobs);
		gitInit();
		for(A4Tuple t :ts )
			mapObjsHash.put(t.atom(0),buildGitHashObject(t.atom(0)));
			
		
		
		treeBuilder(sol,world,mapAtom,mapObjsHash,iState);
		
		commitBuilder(sol,world,mapAtom,mapObjsHash,iState);
		
	}		
	
	public static void treeBuilder(A4Solution sol,Module world,HashMap<String,ExprVar>mapAtom,HashMap<String,String> mapObjsHash, ExprVar iState) throws Err
	{
		Expr domain = CompUtil.parseOneExpression_fromString(world, "object").join(iState);
		Expr content = CompUtil.parseOneExpression_fromString(world, "content");
		Expr Tree = CompUtil.parseOneExpression_fromString(world, "Tree").domain(domain);
		
		LinkedList<ExprVar> aux = new LinkedList<ExprVar>();
		aux.add(ExprVar.make(null, "t",Tree.type()));
		Expr previousTrees = CompUtil.parseOneExpression_fromString(world, "none :> Tree");
		Decl tDecl = new Decl(null,null,null,aux,Tree);
		Expr treeExpr = Sig.UNIV.join(tDecl.get().join(content)).range(Tree).in(previousTrees); 
		treeExpr = treeExpr.comprehensionOver(tDecl);
		
		
		A4TupleSet trees = (A4TupleSet) sol.eval(treeExpr);
		while(trees.size()>0)
		{ 
			buildTrees(sol,trees,content,mapAtom,mapObjsHash);
			for(A4Tuple t : trees)
			{
				previousTrees = previousTrees.plus(mapAtom.get(t.atom(0)));
				tDecl = new Decl(null,null,null,aux,Tree.minus(previousTrees)); 
				treeExpr = Sig.UNIV.join(tDecl.get().join(content)).range(Tree).in(previousTrees); 
				treeExpr = treeExpr.comprehensionOver(tDecl);
			}
			
			trees = (A4TupleSet) sol.eval(treeExpr);
		}
	}
	
	
	public static void commitBuilder(A4Solution sol,Module world,HashMap<String,ExprVar> mapAtom, HashMap<String,String> mapObjsHash,ExprVar iState) throws Err
	{
		Sig Commit = Utils.getEFromIterable(world.getAllSigs(), "this/Commit");
		Expr object = CompUtil.parseOneExpression_fromString(world, "object");
		Expr domain = object.join(iState);
		Expr previousCommits = CompUtil.parseOneExpression_fromString(world, "none :> Commit");
		Expr previous = CompUtil.parseOneExpression_fromString(world, "previous").range(domain);
		Expr c = Commit.decl.get();
		// forall c:Commit :: c in object.State and c.previous in 'previousCommits' and c not in 'previousComits'
		Expr currentCommits = c.in(domain).and(c.join(previous).in(previousCommits).and(c.in(previousCommits).not()));
		currentCommits = currentCommits.comprehensionOver(Commit.decl);
		
		A4TupleSet commits = (A4TupleSet) sol.eval(currentCommits);
		while(commits.size() >0)
		{
			System.out.println(commits.size());
			buildCommits(sol,commits,previous,CompUtil.parseOneExpression_fromString(world, "tree").range(domain),mapAtom,mapObjsHash);
			for(A4Tuple tc : commits)
			{
				previousCommits = previousCommits.plus(mapAtom.get(tc.atom(0)));
				currentCommits = c.in(domain).and(c.join(previous).in(previousCommits).and(c.in(previousCommits).not()));
				currentCommits = currentCommits.comprehensionOver(Commit.decl);
			}
			commits =  (A4TupleSet) sol.eval(currentCommits);
		}
	}
	
	
	
	public static void buildCommits(A4Solution sol, A4TupleSet commits,Expr previous,Expr tree,HashMap<String,ExprVar> mapAtom,HashMap<String,String> mapObjsHash) throws Err
	{
		ArrayList<String> entries;
		ExprVar commit;
		A4TupleSet prevCommits;
		A4Tuple commitTree;
		String treeHash;
		
		for(A4Tuple t : commits)
		{
			entries  = new ArrayList<String>();
			commit = mapAtom.get(t.atom(0));
			System.out.println(t.atom(0));
			commitTree = ((A4TupleSet)sol.eval(commit.join(tree))).iterator().next();
			System.out.println(commitTree.atom(0));
			prevCommits = (A4TupleSet) sol.eval(commit.join(previous));
			
			if(prevCommits.size()>0)
				for(A4Tuple prev: prevCommits)
					entries.add(mapObjsHash.get(prev.atom(0)));
			else
				entries.add("FIRST_COMMIT");
		 
			treeHash =mapObjsHash.get(commitTree.atom(0));
			System.out.println(treeHash);
			mapObjsHash.put(t.atom(0),buildCommitTree(treeHash,"mensage",entries));
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
	
}
