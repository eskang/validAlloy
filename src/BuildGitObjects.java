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
			pr.waitFor();			
	
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
		pr.waitFor();
	
	
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
			pr.waitFor();
		
		
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
		   
			  String tree = "tree  " + tree_hashcode + "\n";
			  String author = "author mr.git <mr.git@gmail.com> 1234567890 +0000" +"\n";
			  String commiter = "committer mr.git <mr.git@gmail.com> 1234567890 +0000" +"\n";
		   
			  String finalcommit = null;
			  
		   if(commits.get(0).compareTo("FIRST_COMMIT")==0){
	
			  finalcommit = tree + author + commiter + "\n" + message + "\n";
		    
		   } else {
			   
			   
			   StringBuilder commit = new StringBuilder();
			   
			   commit.append(tree);
			   
			   for (String com: commits){
				   commit.append("parent " +com +"\n");
			   }
			   commit.append(author);
			   commit.append(commiter);
			   commit.append("\n" + message + "\n");
			   
			   finalcommit = commit.toString();
		   }
			   
		    
		    ArrayList<String> cmds = new ArrayList<String>();
		    cmds.add("git");
		    cmds.add("hash-object");
		    cmds.add("commit");
		    cmds.add("-w");
		    cmds.add("--stdin");
	
		    
		    pb = new ProcessBuilder(cmds);
		   
		   
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
		   bw.write(finalcommit);
		   bw.close();
		  
		  
		   hashcode = br.readLine();
		   System.out.println("commit hash : "+ hashcode);
		   
		   br.close();
		   pr.waitFor();
		  
		  
		  }catch(Exception exc){
		   exc.printStackTrace();
		  }
		  return hashcode;
		  
		 }
	
	public static String setHead(String path_name){
		
		String line = new String();
		try{
    		String newpath = "output/"+pathindex;
			 
			File path = new File(newpath);
			
			ProcessBuilder pb = new ProcessBuilder("git","symbolic-ref","HEAD",path_name);
			
			pb.directory(path);	
			
			Process pr = pb.start();
			

			OutputStream out = pr.getOutputStream();
			InputStream in = pr.getInputStream();
			InputStream err = pr.getErrorStream();

			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
	
			line = br.readLine();
			br.close();
			pr.waitFor();			
	
	}catch(Exception exc){
		exc.printStackTrace();
	}
	return line;
}

	public static String buildGitRef(String commit_hashcode,String path_name){
	
	try{
		
		String newpath = "output/"+pathindex;
		 
		File path = new File(newpath);
		
		ProcessBuilder pb;
			
			ArrayList<String> cmds = new ArrayList<String>();
			cmds.add("git");
			cmds.add("update-ref");
			cmds.add(path_name);
			cmds.add(commit_hashcode);
			
			pb = new ProcessBuilder(cmds);
		
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
		//bw.write();
		bw.close();
	
	
		br.close();
		pr.waitFor();
	
	
	}catch(Exception exc){
		exc.printStackTrace();
	}
	return path_name;
	
}




	public static String buildGitIndexEntry(String object_hash,String file_name){
	
	try{
		
		String newpath = "output/"+pathindex;
		 
		File path = new File(newpath);
		
		ProcessBuilder pb;
			
			ArrayList<String> cmds = new ArrayList<String>();
			cmds.add("git");
			cmds.add("update-index");
			cmds.add("--add");
			cmds.add("--cacheinfo");
			cmds.add("100644");
			cmds.add(object_hash);
			cmds.add(file_name);
			
			pb = new ProcessBuilder(cmds);
		
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
		//bw.write();
		bw.close();
	
	
		br.close();
		pr.waitFor();
	
	
	}catch(Exception exc){
		exc.printStackTrace();
	}
	return file_name;
	
}
	
	public static String gitAdd(String file_name,String p){
		
	try{
		
		
		 
		File path = new File(p);
		
		ProcessBuilder pb;
			
			ArrayList<String> cmds = new ArrayList<String>();
			cmds.add("git");
			cmds.add("add");
			cmds.add(file_name);
			System.out.println("filename: " + file_name + "\npath: " + path);

			
			
			pb = new ProcessBuilder(cmds);
		
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
		//bw.write();
		bw.close();
		System.out.println(br.readLine());
	
		br.close();
		
		
		
		pr.waitFor();
	
	
	}catch(Exception exc){
		exc.printStackTrace();
	}
	return file_name;
	
}
	public static String gitCmd(ArrayList<String> cmds,String p){
		
		String return_string = null;
		
		System.out.println(cmds);
		System.out.println(p);
		
		try{
			
			String line;
			
			StringBuilder lines = null;
			 
			File path = new File(p);
			
			ProcessBuilder pb;
		
			pb = new ProcessBuilder(cmds);
			
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
			//bw.write();
			bw.close();
			System.out.println(br.readLine());
		
			br.close();
		
			/*if(br.ready()){
			
			line = br.readLine();
			
			if(line != null) lines = new StringBuilder();
			
			while(line != null){
				lines.append(line+"\n");
				
				line = br.readLine();
				}
			}
			pr.waitFor();
		
			return_string = lines.toString();
			*/
		}catch(Exception exc){
			exc.printStackTrace();
		}
		return return_string;
		
	}

	private static String buildPath(A4Solution sol, Expr parent,Expr name,ExprVar current, HashMap<String,ExprVar> mapAtom) throws Err
	{//TODO: current.name
		A4TupleSet ts = (A4TupleSet) sol.eval(current.join(name));
		String it  = ts.iterator().next().atom(0).replace("$", "_");
		A4TupleSet ts2 = (A4TupleSet) sol.eval(current.join(parent));
		if(ts2.size() == 0)
			return it;
		else
			return buildPath(sol,parent,name,mapAtom.get(ts2.iterator().next().atom(0)),mapAtom) + "/"+ it;
	}
	
	
	public static void runAdd(A4Solution sol, Module world, String p,ExprVar path, HashMap<String,ExprVar> mapAtom) throws Err
	{ 
		Expr parent =  CompUtil.parseOneExpression_fromString(world," Path <: parent");
		Expr name =  CompUtil.parseOneExpression_fromString(world," Path <: name");
		String filePath = buildPath(sol,parent,name,path,mapAtom);
		System.out.println("fp: "+ filePath +"\np: "+ p);
		gitAdd(filePath,p);
	}
	
	public static void runCmd(A4Solution sol, Module world, String p,ExprVar path, HashMap<String,ExprVar> mapAtom,String cmd,ArrayList<String> options) throws Err
	{ 
		Expr parent =  CompUtil.parseOneExpression_fromString(world," Path <: parent");
		Expr name =  CompUtil.parseOneExpression_fromString(world," Path <: name");
		String filePath = buildPath(sol,parent,name,path,mapAtom);
		System.out.println("fp: "+ filePath +"\np: "+ p);
		
		ArrayList<String> n_cmds = new ArrayList<String>();
		
		n_cmds.add("git");
		n_cmds.add(cmd);
		n_cmds.add(filePath);
		
		for (String n_cmd : options)
		{
			n_cmds.add(n_cmd);
		}
		
		gitCmd(n_cmds,p);
	}
	
	public static void buildIndex(A4Solution sol, Module world, HashMap<String,String> mapObjsHash,HashMap<String,ExprVar>mapAtom, Expr state) throws Err
	{
		Expr nodeBlob = CompUtil.parseOneExpression_fromString(world, "index").join(state);
		Expr parent =  CompUtil.parseOneExpression_fromString(world,"Path <: parent");
		Expr name =  CompUtil.parseOneExpression_fromString(world,"Path <: name");
		A4TupleSet ts =  (A4TupleSet) sol.eval(nodeBlob);
		String path;
		for (A4Tuple t : ts){
			path = buildPath(sol,parent,name,mapAtom.get(t.atom(0)),mapAtom);
			System.out.println("res map:" +mapObjsHash.get(t.atom(1)));
			System.out.println("res path:" +path);
			System.out.println("index res: "+ buildGitIndexEntry(mapObjsHash.get(t.atom(1)),path));
		}
	}


	public static void buildObjects(A4Solution sol,Module world, String index,ExprVar iState, HashMap<String,ExprVar> mapAtom) throws Err
	{
		
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
		
		buildIndex(sol,world,mapObjsHash,mapAtom,iState);
		
		placeHEAD(sol,world,iState);
	}		
	
	public static void treeBuilder(A4Solution sol,Module world,HashMap<String,ExprVar>mapAtom,HashMap<String,String> mapObjsHash, ExprVar iState) throws Err
	{
		Expr domain = CompUtil.parseOneExpression_fromString(world, "object").join(iState);
		Expr content = CompUtil.parseOneExpression_fromString(world, "content");
		Expr Tree = CompUtil.parseOneExpression_fromString(world, "Tree").domain(domain);
		Expr parent =  CompUtil.parseOneExpression_fromString(world," Path <: parent");
		Expr name =  CompUtil.parseOneExpression_fromString(world," Path <: name");
		
		LinkedList<ExprVar> aux = new LinkedList<ExprVar>();
		aux.add(ExprVar.make(null, "t",Tree.type()));
		Expr previousTrees = CompUtil.parseOneExpression_fromString(world, "none :> Tree");
		Decl tDecl = new Decl(null,null,null,aux,Tree);
		Expr treeExpr = Sig.UNIV.join(tDecl.get().join(content)).range(Tree).in(previousTrees); 
		treeExpr = treeExpr.comprehensionOver(tDecl);
		
		
		A4TupleSet trees = (A4TupleSet) sol.eval(treeExpr);
		while(trees.size()>0)
		{ 
			buildTrees(sol,parent,name,trees,content,mapAtom,mapObjsHash);
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
	
	public static void placeHEAD(A4Solution sol,Module world,Expr iState) throws Err
	{
		Expr HEAD = CompUtil.parseOneExpression_fromString(world, "HEAD");
		A4TupleSet res = (A4TupleSet) sol.eval(HEAD.join(iState));
		A4Tuple tup = res.iterator().next();
		System.out.println(setHead("refs/heads/" + tup.atom(0).replace("$", "_")));
		
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
	
	public static void buildTrees(A4Solution sol,Expr parent, Expr name,A4TupleSet trees,Expr content,HashMap<String,ExprVar> mapAtoms,HashMap<String,String> mapObjsHash)throws Err
	{
		ArrayList<String> entries;
		ExprVar tree;
		A4TupleSet lines;
		//String path;
		for(A4Tuple t : trees)
		{
			entries  = new ArrayList<String>();
			tree = mapAtoms.get(t.atom(0));
			lines = (A4TupleSet) sol.eval(tree.join(content));
			for(A4Tuple line: lines)
			{
				//path = buildPath(sol,parent,name,mapAtoms.get(line.atom(0)),mapAtoms);
				entries.add(buildTreeEntry(line.sig(1).toString(),mapObjsHash.get(line.atom(1)),line.atom(0)));
			}
			mapObjsHash.put(t.atom(0),buildGitTree(entries));
		}
	}	
	
}
