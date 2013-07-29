/**
	* Model of the Git
	* Authors: Jose Pinheiro, Tiago, Guimaraes, Eunsuk Kang, Alcino Cunha
	*
	* Changes from git_dynamics.als:
	* - removed "name" from Path and Node, simplified relevant constraints
	**/

open util/ordering[State] as SO

sig State {}

/**
	* Paths
	*/
sig Path {
	parent : lone Path,
	index : Blob lone -> State
}

// File system
abstract sig Node {
	node : set State,
	path : Path,				-- auxiliary relation
	obj : Object
}

sig Dir extends Node {}{
	obj in Tree
}
sig File extends Node {}{
	obj in Blob
}

fact FilesystemConstraints {
	-- Paths are acyclic
	no p : Path | p in p.^parent

	-- Only directories can contain other objects
	all p1, p2 : Path |
		p1 -> p2 in parent implies path.p2 in Dir

	-- For every non-root node in a state, there must be exactly one parent node in the state
	all s : State, n : node.s | 
		let parentPath = n.path.parent | 
			some parentPath => one path.parentPath & node.s
}

// Git objects and index (see path)
abstract sig Object {}
fun object : Object -> State {
	// object exists in a state iff
	{o : Object, s : State |
		// it belongs to the tree of the current head commit or 
		(let c = head.s | o in c.tree + c.tree.^children) or
		// it belongs to the index
		o in Path.index.s
	}
}

sig Blob extends Object {}
sig Tree extends Object {
	content : Path -> lone (Blob+Tree)
}
fun findObjAtPath[top : Tree, p : Path] : Object {
	Tree.((top.^children + top) <: content)[p]
}
fun children : Object -> Object {
	{t : Tree, o : Object | some p : Path | t->p->o in content} 
}
sig Commit extends Object {
	head : set State,
	previous : set Commit,
	tree : Tree
}
fact CommitConstraints {
	-- commit trees have no cycles
	no iden & ^children
	-- objects in each commit maps to nodes that actually exist in the file system
	// Eunsuk: Too strong! Not necessarily true if a file is removed from index before changes are committed
	//all s : State | 
	//	let headCommit = head.s,
	//		objs = headCommit.tree.^children |
	//			obj.objs in node.s
	-- there always exists one head
	all s : State | one head.s
}

sig Tag extends Object {
	commit : Commit
}

pred pathExists [s : State, p : Path] {
	p in (node.s).path
}

/**
	* Add operation
	*/
pred add [s,s' : State,p : Path] {
	// precondition
	s != s'
	pathExists[s, p]
	// postcondition
	index.s' = index.s ++ p->(path.p).obj
	head.s' = head.s
	// working directory doesn't change
	node.s' = node.s
}

// returns the path of the node that corresponds to the object
fun pathOf[o : Object, s : State] : Path {
	(((node.s) <: obj).o).path
}

/**
	* Commit operation
	*/
pred commit[s, s' : State] {
	s != s'
	// precondition
	// There are some changes to be committed in the index
	some index.s
	let oldHead = head.s,
		newHead = head.s',
		newCommitObjs = newHead.tree.^children {
			oldHead in newHead.previous
			// the set of blobs that appear in commit are exactly those from the index			
			newCommitObjs & Blob = Path.index.s
			
			all o : newCommitObjs |
				let p = pathOf[o, s],
					p2 = p.parent |
						// if the path of the obj has a parent
						some p2 implies {
							// then there must be some tree that acts as its parent
							some o2 : newCommitObjs |
								o2.content[p] = p.index.s
						}
			
			all t : newCommitObjs + newHead.tree |
				all p : t.content.Object |
					// if t is a tree that acts as o's parent
					let o = t.content[p],
						p1 = pathOf[o, s],
						p2 = pathOf[t, s] {
							// then t's path must be a parent of o's path
							p2 = p1.parent
							// then correct path must be used 
							p = p1 
						}
	}
	// postcondiiton
	// The index is empty
	no index.s'
	// working directory doesn't change
	node.s' = node.s
}

pred equalToHeadCommit[s : State, p : Path] {
	let c = head.s,
		indexObj = p.index.s,
		workingObj = ((node.s <: path).p).obj, 
		commitObj = findObjAtPath[c.tree, p] |
			// file being removed have to be identical to the tip of the branch (i.e. obj in head commit)
			workingObj = commitObj and  
			// no updates to the file's content can be staged in index
			indexObj = workingObj 
}

/**
	* Remove operation
	*/
pred rm [s,s' : State, p : Path] {
	// precondition
	s != s'
	pathExists[s, p]
	equalToHeadCommit[s, p]
	// postcondition
	index.s' = index.s - p->(path.p).obj
	head.s' = head.s
	node.s' = node.s - path.p
}

/**
	* Older stuff
	*/
fun siblings : Path -> Path{
	{p1:Path, p2:Path | some p1.parent and p1.parent = p2.parent and p1 != p2}
}

fun points : Object -> Object {
	children + previous + tree + commit
}

fun staged : Path -> State {
	{p : Path, s : State | p in (index.s).Object}
}

fun modified : Path -> State {
	{p : Path, s : State | p in staged.s and p.index.s != (path.p & node.s).obj}
}

fun deleted : Path -> State {
	{p : Path, s : State | p in staged.s and no path.p & File & node.s}
}

fun untracked : Path -> State {
	{p : Path, s : State | some path.p & File & node.s and no p.index.s}
}

fact {
	// object is closed under refs
	//all s : State | (object.s).points in object.s
	// no different trees with the same content
	//no disj t1,t2 : Tree | t1.content = t2.content
	// no different commits with the same previous and tree
	//no disj c1,c2 : Commit | c1.previous = c2.previous and c1.tree = c2.tree
	// no different tags with the same commit and name
	//no disj t1,t2 : Tag | t1.commit = t2.commit and t1.name = t2.name
	// all refs are acyclic
	//no ^points & iden
	// indexed blobs are in the objects
	//all s : State | index.s in Path -> object.s
	// if a path is in the index none of its ancestors can be
	all s : State | all p1 : staged.s | no p2 : staged.s | p2 in p1.^parent
}
