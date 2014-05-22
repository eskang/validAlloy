import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.pmw.tinylog.Logger;

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
import edu.mit.csail.sdg.alloy4whole.Helper;


public class BuildGitObjects {

	private static File path;

  /**
   * Executes shell commands
   * @param command: A list of the commands
   * @param path: Working directory for the command
   * @param message: Log message
   * @param input: Input to the command 
   * @return Command output
   * @throws GitException
   */
	public static String exec(List<String> command, File path, String message, String input) throws GitException {
		String result = null;
		try {
      // Start a new process
			ProcessBuilder pb = new ProcessBuilder(command);
			Logger.trace(message + " on " + path.getPath() + "\n   with " + command);
			pb.directory(path);

      // Set environment variables so git hashes will be the same
			Map<String, String> env = pb.environment();
      env.put("GIT_AUTHOR_NAME", "johndoe");
      env.put("GIT_AUTHOR_EMAIL", "jdoe@example.com");
			env.put("GIT_AUTHOR_DATE", "Wed Feb 16 14:00 2037 +0100");
      env.put("GIT_COMMITTER_NAME", "johndoe");
      env.put("GIT_COMMITTER_EMAIL", "jdoe@example.com");
			env.put("GIT_COMMITTER_DATE", "Wed Feb 16 14:00 2037 +0100");

			Process pr = pb.start();

      // If there's input, write it to the process
			if (input != null) {
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(pr.getOutputStream()));
				bw.flush();
				bw.write(input);
				bw.close();
			}

      // Wait for process to complete and return response
			pr.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			BufferedReader be = new BufferedReader(new InputStreamReader(pr.getErrorStream()));
			if (br.ready()) {
				String line = br.readLine();
				StringBuilder lines = new StringBuilder();
				while (line != null) {
					lines.append(line);
					line = br.readLine();
					if (line != null) lines.append('\n');
				}
				result = lines.toString();			
			}
			br.close();
			if (be.ready()) {
				String line = be.readLine();
				StringBuilder lines = new StringBuilder();
				while (line != null) {
					lines.append(line);
					line = be.readLine();
					if (line != null) lines.append('\n');
				}
				be.close();
        // Check if git complains about an error
        if (!lines.toString().startsWith("Switched to branch") &&
            !lines.toString().startsWith("Already on")) {
				  Logger.error(lines.toString());
				  throw new GitException(lines.toString());
        }
			}
		} catch (IOException exc) {
			exc.printStackTrace();
		} catch (InterruptedException exc) {
			exc.printStackTrace();
		}
		return result;
	}
	
	public static String exec(List<String> command, String message, String input) throws GitException {
		return exec(command,path,message,input);
	}

	public static String exec(List<String> command, String message) throws GitException {
		return exec(command,path,message,null);
	}

	public static String exec(List<String> command, File path, String message) throws GitException {
		return exec(command,path,message,null);
	}

  /**
   * Runs "git init"
   */
	public static void gitInit() {
		ArrayList<String> cmds = new ArrayList<String>();
	 	cmds.add(Utils.GIT_CMD);
	 	cmds.add("init");
		try {
			exec(cmds,"Git init");
		} catch (GitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
  // Stores blob as an object in git and returns it's hash
  /**
   * Creates a blob object in git and returns it's hash 
   * @param blob: content of blob
   * @return hash of the blob object
   */
	public static String buildGitHashObject(String blob) {
		ArrayList<String> cmds = new ArrayList<String>();
	 	cmds.add(Utils.GIT_CMD);
	 	cmds.add("hash-object");
	 	cmds.add("-w");
	 	cmds.add("--stdin");
	 	String hash = null;
		try {
			hash = exec(cmds, "Creating blob", blob);
		 	Logger.trace("Blob hash : " + hash);
		} catch (GitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hash;
	}

  /**
   * Formats content (blob or tree object) to be stored in a git tree 
   * @param type: type of content object (blob/tree)
   * @param hashcode hash of the content object 
   * @return Formatted string representing content
   */
	public static String buildTreeEntry(String type, String hashcode, String ref) {
		String mode;
		if (type.equals("this/Blob")) {
			mode = "100644";
			type = "blob";
		} else {
			mode = "040000";
			type = "tree";
		}
		String entry = mode + " " + type + " " + hashcode + "\t" + ref + "\n";
		return entry;
	}
	
  /**
   * Creates a tree object in git and returns it's hash 
   * @param entrys: Content of the tree
   * @return hash of the tree object
   */
	public static String buildGitTree(ArrayList<String> entrys) {	
		StringBuilder tree = new StringBuilder();
		for (String line : entrys) tree.append(line);
		
		ArrayList<String> cmds = new ArrayList<String>();
	 	cmds.add(Utils.GIT_CMD);
	 	cmds.add("mktree");
	 	String hash = null;
		try {
			hash = exec(cmds,"Building tree",tree.toString());
		 	Logger.trace("Tree hash : " + hash);
		} catch (GitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hash;		
	}
	
  // Creates and stores a new commit object with 
  /**
   * Creates a commit object in git and returns it's hash 
   * @param tree_hashcode: Hashcode of the tree pointed to by the commit
   * @param message: Commit message
   * @param commits: Parent commits
   * @return Hash of the commit
   */
	public static String buildCommitTree(String tree_hashcode, String message, ArrayList<String> commits) {
		ArrayList<String> cmds = new ArrayList<String>();	
		if (commits.isEmpty()) {
			cmds.add(Utils.GIT_CMD);
			cmds.add("commit-tree");
			cmds.add(tree_hashcode);
		} else {
			cmds.add(Utils.GIT_CMD);
			cmds.add("commit-tree");
			cmds.add(tree_hashcode);				 
			for(String com : commits){
				cmds.add("-p");
				cmds.add(com);
			}
		}
		String hash = null;
		try {
			hash = exec(cmds, "Building commit with tree " +  tree_hashcode, message);
			Logger.trace("Commit hash : "+ hash);
		} catch (GitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hash;
	}

  /**
   * Sets HEAD to point to a new reference
   * @param path_name: Reference to point at
   * @return null
   */
	public static String setHead(String path_name) {
		ArrayList<String> cmds = new ArrayList<String>();
	 	cmds.add(Utils.GIT_CMD);
	 	cmds.add("symbolic-ref");
	 	cmds.add("HEAD");
	 	cmds.add(path_name);
	 	try {
			return exec(cmds,"Setting HEAD to "+path_name);
		} catch (GitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

  /**
   * Creates a reference in git
   * @param commit_hashcode: Commit the reference points to
   * @param path_name: path for the reference to be stored
   * @return path_name
   */
	public static String buildGitRef(String commit_hashcode, String path_name) {	
		ArrayList<String> cmds = new ArrayList<String>();
		cmds.add(Utils.GIT_CMD);
		cmds.add("update-ref");
		cmds.add(path_name);
		cmds.add(commit_hashcode);
		try {
			exec(cmds,"Setting head " + path_name + " to " + commit_hashcode);
		} catch (GitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path_name;
	}

  /**
   * Updates index with files to be included in the next commit
   * @param object_hash: Hash of object to be added ot the index
   * @param file_name: Name of the file to be added to the index
   * @return file_name
   */
	public static String buildGitIndex(String object_hash, String file_name) {	
		ArrayList<String> cmds = new ArrayList<String>();
		cmds.add(Utils.GIT_CMD);
		cmds.add("update-index");
		cmds.add("--add");
		cmds.add("--cacheinfo");
		cmds.add("100644");
		cmds.add(object_hash);
		cmds.add(file_name);
		try {
			exec(cmds,"Building index entry with " + file_name + " pointing to " + object_hash);
		} catch (GitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file_name;	
	}

  // For every file in state, run buildGitIndexEntry on each file's content 
	/**
	 * Adds nodes that satisfy the index relation to the index 
	 * @param sol: Alloy solution
	 * @param world: Alloy module
	 * @param mapAtom:
	 * @param mapObjsHash: map from object to hash value
	 * @param iState: current state
	 * @throws Err
   */
	public static void buildIndex(A4Solution sol, Module world, HashMap<String, ExprVar> mapAtom,
    HashMap<String, String> mapObjsHash, ExprVar iState) throws Err {

     //Get all Sigs and map them to their names(string)
     Map<String,Sig.PrimSig> mapSig = Helper.atom2sig(sol);

		Expr files = CompUtil.parseOneExpression_fromString(world, "index").join(iState);
		Expr parent =  CompUtil.parseOneExpression_fromString(world,"Node <: parent");
		Expr content = CompUtil.parseOneExpression_fromString(world, "File <: content");

    // Tuple of all files in the state
		A4TupleSet ts =  (A4TupleSet) sol.eval(files);

		String path;

		for (A4Tuple t : ts) {
			String a = t.atom(0);
      path = buildPath(sol, world, parent, mapAtom.get(a), mapAtom);
      // Only files are indexed
      if (mapSig.get(a).toString().equals("this/File")) {

        String blobStr = null;
        // Iterate through blobs owned by file t
        // TODO: Why is it necessary to do this if a file can only own one blob? 
        for (A4Tuple blob: (A4TupleSet)sol.eval(mapAtom.get(a).join(content))){
          blobStr = blob.atom(0);
        }

        Logger.trace("Res map   :" + mapObjsHash.get(blobStr));
        Logger.trace("Res path  :" + path);
        Logger.trace("Index res : "+ buildGitIndex(mapObjsHash.get(blobStr),path));
      }
		}
	}
	
	/**
	 * Builds the path of a node in the working directory by recursively walking up the
   * parent relation until no further parents 
	 * @param sol
	 * @param parent: Expr representing relation of type Path -> Path
	 * @param name: Expr representing relation of type Path -> Path
	 * @param current: Expr representing relation of type Path
	 * @param mapAtom
	 * @return path to the node
	 * @throws Err
	 */
	private static String buildPath(A4Solution sol, Module world, Expr parent, ExprVar current, HashMap<String, ExprVar> mapAtom) throws Err {
    // Expression for the name of nodes (files and dirs) in state
		Expr name = CompUtil.parseOneExpression_fromString(world, "Node <: name");

    // Names of nodes in 'current'
		A4TupleSet ts = (A4TupleSet) sol.eval(current.join(name));
    // Get the name of the current node
		String it = ts.iterator().next().atom(0).replace("$", "_");
    // Parent(s) of current node(s)
		A4TupleSet tsp = (A4TupleSet) sol.eval(current.join(parent));

		if(tsp.size() == 0)
			return null;
		else {
      // Run buildPath on parent of current node
			String res = buildPath(sol,world,parent,mapAtom.get(tsp.iterator().next().atom(0)),mapAtom);
			if (res == null)
        // No parents, return the name of the current node
				return it; 
			else 
				return res + "/" + it;
		}
	}

	public static String buildBranch(A4Solution sol, ExprVar branch) throws Err {
		A4TupleSet ts = (A4TupleSet) sol.eval(branch);
		A4Tuple t = ts.iterator().next();
		return t.atom(0).replace("$","_");
	}	

	public static String buildReference(A4Solution sol, ExprVar ref) throws Err {
		A4TupleSet ts = (A4TupleSet) sol.eval(ref);
		A4Tuple t = ts.iterator().next();
		return t.atom(0).replace("$", "_");
	}

	/**
   * Finds the string representing the input to the git operation being tested
	 * @param sol: Alloy solution
	 * @param world: Alloy module
	 * @param path: path name
	 * @param mapAtom: map from atoms to names
	 * @param cmd: command name
	 * @param vars: map from command to variable
   * @param parent: expr for parent relation
	 * @throws GitException,Err
   */
	public static String buildType(A4Solution sol, Module world, ExprVar path, HashMap<String, ExprVar> mapAtom,
    String cmd, HashMap<String, String> vars, Expr parent) throws Err, GitException {		

		String type = null;
		if (vars.get(cmd).equals("path")) {
			type = buildPath(sol,world, parent, path, mapAtom);
            if (type == null) type = "."; // hack - buildPath must be changed
		} else if (vars.get(cmd).equals("branch")) {
			type = buildBranch(sol,path);
    } else if (vars.get(cmd).equals("name")) {
			type = buildReference(sol, path);
		} else {			
			type = buildPath(sol,world,parent, path,mapAtom);
            if (type == null) type = "."; // idem
		}
		return type;
	}
	

	/**
   * Runs the git operation being tested 
	 * @param sol: Alloy solution
	 * @param world: Alloy module
	 * @param p: path to run command on
	 * @param path: path expression
	 * @param mapAtom: map from atoms to names
	 * @param cmd: command name
	 * @param options: arguments to the command 
   * @param parent: expr for parent relation
	 * @throws GitException,Err
   */
	public static void runCmd(A4Solution sol, Module world, String p, ExprVar path, HashMap<String, ExprVar> mapAtom,
    String cmd, ArrayList<String> options, HashMap<String,String> vars) throws GitException, Err { 

    // Expression for parent relation
		Expr parent =  CompUtil.parseOneExpression_fromString(world," Node <: parent");

		ArrayList<String> n_cmds = new ArrayList<String>();
		n_cmds.add(Utils.GIT_CMD);
		n_cmds.add(cmd);
				
		for (String n_cmd : options) {
			if(n_cmd.matches("#[a-zA-Z0-9]*")){
				n_cmds.add(buildType(sol, world, path, mapAtom, n_cmd, vars, parent));
			}else n_cmds.add(n_cmd);
		};	
	    
		try {
			String result = exec(n_cmds,new File(p),"Running "+n_cmds);
			Logger.trace("Output: " + result);
		} catch (GitException e) {
			throw new GitException("Result from "+ n_cmds+" on path "+p+":\n\n"+e.getMessage() );
		}	
	}

	/**
   * Iterates over references in iState and creates them in the current git instance
	 * @param sol: Alloy solution
	 * @param world: Alloy module
	 * @param mapAtom: map from atoms to names
	 * @param mapObjsHash: map from object to hash value
   * @param iState: current state 
	 * @throws GitException,Err
   */
	public static void buildRefs(A4Solution sol, Module world, HashMap<String, ExprVar> mapAtom, HashMap<String, String> mapObjsHash, ExprVar iState) throws Err {
    // Expression for all refs in iState
		Expr refs = CompUtil.parseOneExpression_fromString(world, "refs").join(iState);
		A4TupleSet ts = (A4TupleSet) sol.eval(refs);
		for(A4Tuple t : ts) {
			buildGitRef(mapObjsHash.get(t.atom(1)),"refs/heads/"+t.atom(0).replace("$", "_"));
		}
	}

	/**
   * Performs a BFS through the trees along the content relation, starting at the root,
   * creating the each tree in the current git instance
	 * @param sol: Alloy solution
	 * @param world: Alloy module
	 * @param mapAtom: map from atoms to names
	 * @param mapObjsHash: map from object to hash value
   * @param iState: current state 
	 * @throws GitException,Err
   */
	public static void treeBuilder(A4Solution sol, Module world, HashMap<String, ExprVar> mapAtom, HashMap<String, String> mapObjsHash, ExprVar iState) throws Err {
		Expr domain = CompUtil.parseOneExpression_fromString(world, "stored").join(iState);
		Expr content = CompUtil.parseOneExpression_fromString(world, "Tree <: content");
		Expr Tree = CompUtil.parseOneExpression_fromString(world, "Tree").domain(domain);
		
		LinkedList<ExprVar> aux = new LinkedList<ExprVar>();
		aux.add(ExprVar.make(null, "t",Tree.type()));
    // Get empty tree
		Expr previousTrees = CompUtil.parseOneExpression_fromString(world, "none :> Tree");
    // Declare tree variable
		Decl tDecl = new Decl(null,null,null,aux,Tree);

    // Get expression for trees with content of type tree that is in previousTrees
		Expr treeExpr = Sig.UNIV.join(tDecl.get().join(content)).range(Tree).in(previousTrees); 
		treeExpr = treeExpr.comprehensionOver(tDecl);
	  
    // Get trees from solution
		A4TupleSet trees = (A4TupleSet) sol.eval(treeExpr);

    // BFS through trees
		while (trees.size() > 0) { 
      // Add tree to git instance
			buildTrees(sol,trees,content,mapAtom,mapObjsHash);
      // Make sure we don't see the same tree again
			for (A4Tuple t : trees) {
				previousTrees = previousTrees.plus(mapAtom.get(t.atom(0)));
				tDecl = new Decl(null,null,null,aux,Tree.minus(previousTrees)); 
				treeExpr = Sig.UNIV.join(tDecl.get().join(content)).range(Tree).in(previousTrees); 
				treeExpr = treeExpr.comprehensionOver(tDecl);
			}
			trees = (A4TupleSet) sol.eval(treeExpr);
		}
	}
	
	/**
   * Performs a BFS through the commits along the previous relation, starting at the root,
   * creating each commit in the current git instance
	 * @param sol: Alloy solution
	 * @param world: Alloy module
	 * @param mapAtom: map from atoms to names
	 * @param mapObjsHash: map from object to hash value
   * @param iState: current state 
	 * @throws GitException,Err
   */
	public static void commitBuilder(A4Solution sol, Module world, HashMap<String, ExprVar> mapAtom, HashMap<String, String> mapObjsHash, ExprVar iState) throws Err {
		Sig Commit = Utils.getEFromIterable(world.getAllSigs(), "this/Commit");
		Expr object = CompUtil.parseOneExpression_fromString(world,"stored");

		Expr domain = object.join(iState);

    // Initially empty
		Expr previousCommits = CompUtil.parseOneExpression_fromString(world, "none :> Commit");

    // Relation matching commits to their previous commit
		Expr previous = CompUtil.parseOneExpression_fromString(world, "previous").range(domain);

		Expr c = Commit.decl.get();
    
		// forall c:Commit :: c in object.State and c.previous in 'previousCommits' and c not in 'previousComits'
		Expr currentCommits = c.in(domain).and(c.join(previous).in(previousCommits).and(c.in(previousCommits).not()));
		currentCommits = currentCommits.comprehensionOver(Commit.decl);

    // Initially gives us the first commit because previous commits is currently empty
		A4TupleSet commits = (A4TupleSet) sol.eval(currentCommits);

    // BFS through commits
		while (commits.size() >0) {
      // Add commit to git instance
			buildCommits(sol, commits, previous, CompUtil.parseOneExpression_fromString(world, "tree").range(domain), mapAtom, mapObjsHash);
      // Make sure we don't see the same commit again
			for (A4Tuple tc : commits) {
				previousCommits = previousCommits.plus(mapAtom.get(tc.atom(0)));
				currentCommits = c.in(domain).and(c.join(previous).in(previousCommits).and(c.in(previousCommits).not()));
				currentCommits = currentCommits.comprehensionOver(Commit.decl);
			}
			commits =  (A4TupleSet) sol.eval(currentCommits);
		}
	}
	
	/**
   * Points the HEAD to a commit 
	 * @param sol: Alloy solution
	 * @param world: Alloy module
   * @param iState: current state 
	 * @throws GitException,Err
   */
	public static void placeHEAD(A4Solution sol, Module world, Expr iState) throws Err {
		Expr HEAD = CompUtil.parseOneExpression_fromString(world, "HEAD");
		A4TupleSet res = (A4TupleSet) sol.eval(HEAD.join(iState));
    if (res.size() > 0) {
      A4Tuple tup = res.iterator().next();
      Logger.trace(setHead("refs/heads/" + tup.atom(0).replace("$", "_")));
    } else {
    	Logger.trace(setHead("refs/heads/master"));
    }		
	}
	
	
	/**
   * Creates a commit in the current git instance 
	 * @param sol: Alloy solution
   * @param commits: commits to be built
   * @param previous: Previous relation for commit
	 * @param tree: tree to store in commit
	 * @param mapAtom: map from atoms to names
	 * @param mapObjsHash: map from object to hash value
	 * @throws GitException,Err
   */
	public static void buildCommits(A4Solution sol, A4TupleSet commits, Expr previous, Expr tree, HashMap<String, ExprVar> mapAtom, HashMap<String, String> mapObjsHash) throws Err {
		ArrayList<String> entries;
		ExprVar commit;
		A4TupleSet prevCommits;
		A4Tuple commitTree;
		String treeHash;
		
		for (A4Tuple t : commits) {
			entries  = new ArrayList<String>();
      // Get the commit
			commit = mapAtom.get(t.atom(0));
      // Get commit.previous
			prevCommits = (A4TupleSet) sol.eval(commit.join(previous));

      // Format commit tree content
			commitTree = ((A4TupleSet)sol.eval(commit.join(tree))).iterator().next();

      // Find parents of commit t
			for (A4Tuple prev: prevCommits){
				// TODO: Not sure this is safe 
				// Don't add duplicate commit 

        // Get the previous commit
				String h = mapObjsHash.get(prev.atom(0));
        // If entries already doesn't contain the previous commit, add it 
				if (!entries.contains(h)) entries.add(h);
			}

			treeHash = mapObjsHash.get(commitTree.atom(0));
			mapObjsHash.put(t.atom(0), buildCommitTree(treeHash,"\"test commit\"\n",entries));
		}
	}

	/**
   * Creates a tree in the current git instance  
	 * @param sol: Alloy solution
   * @param trees: trees to be built
   * @param content: Content relation for trees
	 * @param mapAtom: map from atoms to names
	 * @param mapObjsHash: map from object to hash value
	 * @throws GitException,Err
   */
	public static void buildTrees(A4Solution sol, A4TupleSet trees, Expr content, HashMap<String, ExprVar> mapAtoms,
			HashMap<String, String> mapObjsHash) throws Err {
		ArrayList<String> entries;
		ExprVar tree;
		A4TupleSet lines;
		for (A4Tuple t : trees) {
			entries = new ArrayList<String>();
			tree = mapAtoms.get(t.atom(0));

      // Get content from trees
			lines = (A4TupleSet) sol.eval(tree.join(content));
        
      // Build string representation of tree content 
			for (A4Tuple line : lines) {
				entries.add(buildTreeEntry(line.sig(1).toString(),
						mapObjsHash.get(line.atom(1)),
						line.atom(0).replace("$", "_")));
			}
      // Build the tree t
			mapObjsHash.put(t.atom(0), buildGitTree(entries));
		}
	}

	/**
   * Creates the git instance
	 * @param sol: Alloy solution
   * @param world: Alloy module
   * @param index: iteration
   * @param iState: current state
	 * @param mapAtom: map from atoms to names
	 * @throws Err
   */
	public static void buildObjects(A4Solution sol, Module world, String index,
			ExprVar iState, HashMap<String, ExprVar> mapAtom) throws Err {

		HashMap<String, String> mapObjsHash = new HashMap<String, String>();
		path = new File("output/" + index);
    
		Expr domain = CompUtil.parseOneExpression_fromString(world, "stored")
				.join(iState);

    // Blobs are git representations of files
		Expr blobs = CompUtil.parseOneExpression_fromString(world, "Blob")
				.domain(domain);
    
		A4TupleSet ts = (A4TupleSet) sol.eval(blobs);

    // Runs "git init"
		gitInit();

    // Run "git hash-object" 
		for (A4Tuple t : ts)
			mapObjsHash.put(t.atom(0), buildGitHashObject(t.atom(0)));

    // Build tree objects
		treeBuilder(sol, world, mapAtom, mapObjsHash, iState);
 
    // Build commit objects
		commitBuilder(sol, world, mapAtom, mapObjsHash, iState);

    // Add files to the index
		buildIndex(sol, world, mapAtom, mapObjsHash, iState);
    
    // Build references
		buildRefs(sol, world, mapAtom, mapObjsHash, iState);

		placeHEAD(sol, world, iState);
	}
}
