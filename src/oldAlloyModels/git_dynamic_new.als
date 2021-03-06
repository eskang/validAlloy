/**
	* Model of the Git
	* Authors: Jose Pinheiro, Tiago Guimaraes, Eunsuk Kang, Alcino Cunha
	*
	* Changes from git_dynamics.als:
	* - removed "name" from Path and Node, simplified relevant constraints
	**/

-- Alcino: Commit also goes in object

open util/ordering[State] as SO

fact {
	Node = node.State
	Object = object.State
	Reference = (HEAD+refs).State
}

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

// Git objects and index (see path)
abstract sig Object {
	object : set State
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
	previous : lone Commit,
	tree : Tree
}

pred pathExists [s : State, p : Path] {
	p in (node.s).path
}

fun path2node[s : State, p : Path] : Node {
	node.s & path.p
}

pred pointsToFile[s : State, p : Path] {
	path2node[s, p] in File
}

pred isDescendant[p1, p2 : Path] {
	p2 in p1.^parent
}

// returns the set of all path-blob pairs for every file that resides under the directory pointed by "p"
fun descendantPairs[s : State, p : Path] : Path -> Blob {
	{p2 : Path, o : Object |
		pointsToFile[s, p2] and 
		isDescendant[p2, p] and
		o = path2node[s, p2].obj
	}
}

/**
	* Git references (branches + tag)
	*/
sig Reference {
	refs : set State,
	HEAD : set State,
	c : Commit
}


/**
	* Invariant
	*/
pred invariant[s : State] {
	fsysInvariant[s]
	objectInvariant[s]
	commitInvariant[s]
}

pred fsysInvariant[s : State] {
	no disj n1, n2 : node.s | n1.path = n2.path

	-- Paths are acyclic
	no p : Path | p in p.^parent

	-- Only directories can contain other objects
	all p1, p2 : Path |
		p1 -> p2 in parent implies path.p2 in Dir

	-- For every non-root node in a state, there must be exactly one parent node in the state
	all n : node.s | 
		let parentPath = n.path.parent | 
			some parentPath => one path.parentPath & node.s
}

fun getHEAD[s : State] : Commit {
	(HEAD.s).c
}

pred objectInvariant[s : State] {
	Path.index in object
	// All commits in the objects point to trees and previous commits in the objects
	all s : State, c : object.s & Commit | c.tree in object.s and c.previous in object.s	
	// All descendants of a tree in the objects must belong to the objects
	all s : State, t : object.s | t.*children in object.s
	// Refs point to valid commits
	all s : State | (refs.s).c in object.s
	// HEAD must be one of the refs
	all s : State | (HEAD.s) in refs.s
	// if a path is in the index none of its ancestors can be
	all s : State | all p1 : staged.s | no p2 : staged.s | p2 in p1.^parent
}

pred commitInvariant[s : State] {
	no c : Commit | c in c.^previous
	-- commit trees have no cycles
	no iden & ^children
	lone HEAD.s
}

/**
	* Add operation
	*/
pred add [s,s' : State,p : Path] {
	// precondition
	invariant[s]
	s != s'
	pathExists[s, p]
	// postcondition
	let path2obj = pointsToFile[s, p] implies p -> (path.p).obj else descendantPairs[s, p] {
		-- entries in index that point to files that no longer exist in the file system
		let danglingEntries = { badPath : Path, o : Blob | 
												isDescendant[badPath, p] and badPath -> o in index.s and not pathExists[s, badPath]} |
			index.s' = (index.s ++ path2obj) - danglingEntries
		// add new objects to git database
		object.s' = object.s + Path.path2obj
	}
	// HEAD doesn't change
	HEAD.s' = HEAD.s
	// working directory doesn't change
	node.s' = node.s
	// refs don't change
	refs.s' = refs.s
}

run {
	some p : Path | add[SO/first, SO/first.next, p]
} for 5 but 2 State

pred add_pathExists [s,s' : State,p : Path] {
	invariant[s]
	not pathExists[s, p]
	s' = s
}

// Unit tests

// Not allowed because object must always contain at least one tree, since commits must point 
// to one even if they do not belong to object, and since HEAD must point to a commit that might not
// belong to object (the initial dettached HEAD, for example)...
// Eunsuk: Fixed by making "HEAD" optional
run add1 {
	some f : File {
		node.first = f
		no object.first
		no index.first
		add[first, first.next, f.path]
	}
}

// Not allowed because the path cannot belong to a Dir due to first postcondition: it tries to add a Tree
// object to the index, which is not possible
// Eunsuk: Fixed add operation to work over directories as well
run add2 {
	some d : Dir {
		node.first = d
		no object.first
		no index.first
		add[first, first.next, d.path]
	}
}

// Similar to the previous, to remind us that we need to recursively add to the index all pairs path->blob
// that descend from the given directory.
run add3 {
	some disj d1, d2 : Dir, disj f1, f2 : File {
		node.first = d1+d2+f1+f2
		f1.path.parent = d1.path
		d2.path.parent = d1.path
		f2.path.parent = d2.path
		no object.first
		no index.first
		add[first, first.next, d1.path]
	}
} for 5

// Adding something that is already in the index should do nothing
run add4 {
	some f : File {
		node.first = f
		object.first = f.obj
		index.first = f.path -> f.obj
		add[first, first.next, f.path]
	}
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
	invariant[s]
	// There are some changes to be committed in the index
	some index.s
	let oldHead = getHEAD[s],
		newHead = getHEAD[s'],
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
	// add objects in the database
	let newHead = getHEAD[s'] {
		object.s' = object.s + newHead + newHead.tree + newHead.tree.^children
		one HEAD.s'
	}
	// The index is empty
	no index.s'
	// working directory doesn't change
	node.s' = node.s
	// refs don't change
	refs.s' = refs.s
}

run {
	commit[SO/first, SO/first.next]
	getHEAD[SO/first].tree != getHEAD[SO/first.next].tree
} for 5 but 2 State


pred equalToHeadCommit[s : State, p : Path] {
	let c = getHEAD[s],
		indexObj = p.index.s,
		workingObj = path2node[s, p].obj, 
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
	invariant[s]
	pathExists[s, p]
	equalToHeadCommit[s, p]
	// postcondition
	index.s' = index.s - p->(path.p).obj
	HEAD.s' = HEAD.s
	// Eunsuk: What's the right behavior here? Can't always remove, because object might live in an existing commit.
	// For now, assume that object db remains the same and unnecessary objects later get "garbage-collected".
	object.s' = object.s 
	node.s' = node.s - path.p
	refs.s' = refs.s
}

run {
	some p : Path | rm[SO/first, SO/first.next, p]
} for 5 but 2 State

pred rm_pathExists [s,s' : State, p : Path] {
	invariant[s]
	not pathExists[s, p]
	s' = s
}

// Very simple rm test
run rm1 {
	some f : File {
		node.first = f
		some getHEAD[first]
		object.first = getHEAD[first] + getHEAD[first].tree + f.obj
		index.first = f.path -> f.obj
		rm[first, first.next, f.path]
	}
} for 3 but 2 State

// rm must delete empty directories
run rm2 {
	some f : File, d : Dir {
		node.first = d+f
		f.path.parent = d.path
		some HEAD.first
		refs.first = HEAD.first
		index.first = f.path -> f.obj
		object.first = (HEAD.first).c + (HEAD.first).c.tree + d.obj + f.obj
		(HEAD.first).c.tree.content  = d.path -> d.obj
		(d.obj).content = f.path -> f.obj
		rm[first, first.next, f.path]
		// there should be no nodes in the post-state
		no node.(first.next)
	}
} for 4 but 2 State

/**
	* Invariant check
	*/
pred invariantPreserved[s, s' : State] {
	(invariant[s] and 
		(commit[s, s'] or some p : Path | add[s, s', p] or rm[s, s', p])) implies
	invariant[s']
}

check InvariantPreservation {
	all s : State - last | invariantPreserved[s, s.next]
} for 8 but 2 State

/** 
	* Facts (no longer enforced, replaced with invariant to be checked
	*/
/*
fact FilesystemConstraints {
	all s : State | no disj n1, n2 : node.s | n1.path = n2.path

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

fact ObjectConstraints {
	Path.index in object
	all s : State | let c = HEAD.s | c.tree + c.tree.^children in object.s
}	

fact CommitConstraints {
	no c : Commit | c in c.^previous

	-- commit trees have no cycles
	no iden & ^children
	-- objects in each commit maps to nodes that actually exist in the file system
	// Eunsuk: Too strong! Not necessarily true if a file is removed from index before changes are committed
	//all s : State | 
	//	let headCommit = head.s,
	//		objs = headCommit.tree.^children |
	//			obj.objs in node.s
	-- there always exists one head
	all s : State | one HEAD.s
}
*/

/**
	* Older stuff
	*/
fun siblings : Path -> Path{
	{p1:Path, p2:Path | some p1.parent and p1.parent = p2.parent and p1 != p2}
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
	//all s : State | all p1 : staged.s | no p2 : staged.s | p2 in p1.^parent
}
