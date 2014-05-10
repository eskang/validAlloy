/**
	* Git model of Alloy
	*/
module Git

sig State {}

sig Name {
	refs : Commit -> State,
	heads : set State,
	HEAD : set State
}

fun HEAD[s : State] : Commit {
	pointsTo[HEAD.s, s]
}

/**
	* File system nodes
	*/
abstract sig Node {
	name : one Name,
	parent : lone Dir,
	current : set State,		-- set of nodes currently in file system
	samepath : set Node, 	-- auxiliary relation
	-- n -> o -> c in belongsTo iff  o is an object in c.tree that corresponds to n
	belongsTo : Object lone -> Commit, 
}{
	all o : Object, c : Commit |
		o -> c in belongsTo implies committedAs[this, o, c]
	(some o : Object, c : Commit | committedAs[this, o, c]) implies
		some belongsTo
}

-- true iff fsys node 'n' is commited as git object 'o' in commit 'c'
pred committedAs[n : Node, o : Object, c : Commit] {
	-- either n is rooted at c or
	n in Root and o = c.tree or
	-- n is stored a child of some intermediate tree in c
	some p : Tree {		-- p is a tree in c that stores n's blob as a child
		p in c.tree.^children + c.tree	
		o = p.content[n.name]
		p -> c in n.parent.belongsTo
	}
}

sig File extends Node {
	content : one Blob,
	index : set State		-- in staging area or not
}{
	belongsTo.Commit in content
}

sig Dir extends Node {
	tbc : Tree -> State -- auxiliary relation
}{
	belongsTo.Commit in Tree
}

one sig Root extends Dir {}

fact SamePath {
	-- two nodes have the same path iff
	all x,y : Node | x->y in samepath iff 
		-- they have the same name and
		(x.name = y.name and 	
			-- they both have no parents, or their parents have the same path
			((no x.parent and no y.parent) or  
			 (some x.parent and some y.parent and x.parent->y.parent in samepath)))
}

fact TreeFacts {
	all n : Node - Root | one n.parent
	no Root.parent
	all n : Node | n not in n.^parent
}

check {
	all s : State, f : File, f' : f.samepath & File | invariant[s] implies f.content = f'.content
} for 7 but 1 State

check {
	all s : State, n : current.s | invariant[s] implies no (current.s & (n.samepath-n))
} for 7 but 1 State

run SamePathsArePossible {
	all s : State | invariant[s]
	some samepath-iden
} for 4 but 2 State


/**
	*  Git objects (trees and blobs)
	*/

abstract sig Object {
	stored : set State
}

sig Blob extends Object {
	conflict : set Blob,
	-- b1 -> b2 -> b' in merging means b' is a result of merging b1 and b2
	merging : Blob -> lone Blob
}

fact BlobFacts {
	all b1, b2 : Blob | 
		b1 -> b2 in conflict implies b2 -> b1 in conflict
	
	-- conflicting blobs can't be merged
	all b1, b2 : Blob |
		b1 -> b2 in conflict implies no b1.merging[b2] 	

	all b1, b2, b' : Blob |
		b1 -> b2 -> b' in merging implies
			b2 -> b1 -> b' in merging
}	

sig Tree extends Object {
	content : Name -> lone (Blob+Tree)
}
fact Canonicalization {
	no disj b1, b2 : Blob |
		b1.conflict = b2.conflict and b1.merging = b2.merging
	no disj t1, t2 : Tree |
		t1.content = t2.content
}

fun children : Tree -> Object {
	{t : Tree, o : Object | some n : Name | t->n->o in content}
}

sig Commit extends Object {
	previous : set Commit,
	tree : one Tree,
}

fact Acyclic {
	// Acyclic commits
	no c : Commit | c in c.^previous
	// Acyclic trees
	no t : Tree | t in t.^children
}

fun pointsTo[n : Name, s : State] : Commit {
	n.refs.s
}

pred invariant [s : State] {
	-- Current file system
	all n : current.s | n.parent in current.s
	Root in current.s
	all d : current.s, disj x,y : (parent.d) & current.s | x.name != y.name
	-- Stored objects
	-- all descendant of a commit must also be stored
	all t : stored.s & Tree | Name.(t.content) in stored.s
	all c : stored.s & Commit | c.previous in stored.s and c.tree in stored.s
	Name.refs.s in stored.s
	// index blobs must be in the object database
	(index.s).content in stored.s
	// exactly one HEAD
	one HEAD.s
	// Eunsuk: Actually, it turns out the invariant is too strong
	// Index must not contain duplicate paths -- I'm not sure we really need this, but its kind of weird
	all f : index.s | one f.samepath & index.s
	// Another invariant that was missing concerning index
	all f : index.s | no (f.^parent.samepath & index.s)
	// HEAD doesnt necessarily need to be referenced
	// was: HEAD must be a reference
	HEAD.s in heads.s
	heads.s in refs.s.Commit
	// each name can only point to at most one commit object
	all n : Name, s : State | lone pointsTo[n, s]
}

pred tbc [s : State] {
	// trees ready to-be-commited
	all d : Dir | some ^parent.d & index.s implies {
		one d.(tbc.s)
		d.(tbc.s).content = {n : Name, o : Object | some x : parent.d & (index.s).*parent | o = x.(tbc.s+content) and n = x.name}
	}
}

/* some helper functions */
fun leaves [s : State, n : Node] : set File {
	(*parent).n & File & current.s
}

fun children[s : State, n : Node] : set Node {
	(^parent).n & current.s
}

pred descendantOf[o : Object, p : Object] {
	o in p.^children
}

-- staged for commit
fun staged[s : State] : set File {
	{f : File | f in index.s}
}

-- Git knows nothing about this file
fun untracked[s : State] : set File {
	{f : File |
		f not in index.s and
		no f2 : File |	
 			f -> f2 in samepath and 
			some f2.belongsTo.(HEAD[s])}
}

-- modified but not staged yet
fun modified[s : State] : set File {
	{f : File | 
		f not in index.s and 
		some f2 : File |
 			f -> f2 in samepath and
			some f2.belongsTo.(HEAD[s]) and 
			f2.content != f.content}	
}

/*
It seems the old git add is now equivalent to git add --ignore-removal
New git add is the same as git add --all
I'm assuming we are modeling the old git
*/
pred add [s,s' : State, n : Node] {
	// this is actually possible: git add .
	// is it a limitation of the testbench? I implemented a little hack to generate path . when is n is Root
	//n != Root
	invariant[s]
	s != s'
	// paht exists
	some (n.samepath & current.s)
	// add paths to index
	/*
	Eunsuk: Shouldn't it be "(*parent.n).samepath" instead of (n.*parent.samepath)?
	Alcino: I guess so, but got no differences so far, which is weird. Or not: actually, this only deletes files
	whose path is a prefix of the given path, wich must be done to add the given path, and ignores deleted
	files wich path is an extension of the the given path
	*/
	index.s' = index.s - n.*parent.samepath - samepath.(*parent).(n.samepath) + leaves[s,n.samepath]
	// add blobs to object
	stored.s' = stored.s + leaves[s,n.samepath].content
	// frame
	current.s' = current.s
	HEAD.s' = HEAD.s
	heads.s' = heads.s
	refs.s' = refs.s
	no tbc.s
	no tbc.s'
}

run add for 3 but 2 State

check add_correct {
	all s,s' : State, n : Node | invariant[s] and add[s,s',n] implies invariant[s']
} for 7 but 2 State

pred add_invalid_path [s,s' : State, n : Node] {
	invariant[s]
	no (n.samepath & current.s)
	s' = s
}

run add_invalid_path for 6 but exactly 1 State

run add_test1 {
	some s,s' : State, f : File {
		f.parent = Root
		f in current.s
		add[s,s',Root]
	}
} for 3 but 2 State

pred commit [s,s' : State, n : Node] {
	invariant[s]
	tbc[s]
	s != s'
	some index.s
	some c : Commit-stored.s {
		c.previous = HEAD[s]
		HEAD.s' = HEAD.s
		heads.s' = heads.s
		refs.s' = refs.s ++ HEAD.s -> c			
		c.tree = Root.(tbc.s)
		stored.s' = stored.s + (index.s).^parent.(tbc.s) + c
	}
	current.s' = current.s
	index.s' = index.s
}

run commit for 4 but 2 State

check {
	all s,s' : State, n : Node | invariant[s] and commit[s,s', n] implies invariant[s']
} for 3 but 2 State

pred pathInIndex [s : State, n : Node] {
	one n.samepath & index.s
}

pred noUpdatesStaged [s : State, f : File] {
	(f.samepath & index.s).content = (f.samepath & index.s).belongsTo.(HEAD[s]) or (f not in current.s)
}

pred noLocalChanges [s : State, f : File] {
	f not in current.s or (f.samepath & index.s).belongsTo.(HEAD[s]) = f.content
}

check {
	all f : File, s : State | invariant[s] and pathInIndex[s,f] and noUpdatesStaged[s,f] implies f.content = (f.samepath & index.s).content
} for 3 but 1 State

fun dirsContainingOnly [s : State, f : set File] : set Dir {
	{n : current.s - Root | 
		n in f.*parent.samepath and 
		(*parent.n & current.s) in f.*parent.samepath and 
		(some d : *parent.n & current.s | f in parent.d)
	}
}

-- Remove operation
pred rm[s, s' : State, f : File]{
	-- preconditions
	invariant[s]
	s != s'
	pathInIndex[s,f]
	noUpdatesStaged[s,f]
	noLocalChanges[s,f]
	-- postconditions
	index.s' = index.s - f.samepath
	current.s' = current.s - f.samepath - dirsContainingOnly[s,f.samepath]
	HEAD.s' = HEAD.s
	stored.s' = stored.s 
	refs.s' = refs.s	
}

run rm for 6 but 2 State

check rm_correct {
	all s,s' : State, n : Node | invariant[s] and rm[s,s',n] implies invariant[s']
} for 6 but 2 State

pred rm_PathNotInIndex[s, s' : State, f : File] {
	invariant[s]
	not pathInIndex[s,f]
	s' = s
}

pred rm_UpdatesStaged[s, s' : State, f : File] {
	invariant[s]
	pathInIndex[s,f]
	not noUpdatesStaged[s,f]
	s' = s
}

pred rm_LocalChanges[s, s' : State, f : File] {
	invariant[s]
	pathInIndex[s,f]
	noUpdatesStaged[s,f]
	not noLocalChanges[s,f]
	s' = s
}

run rm_LocalChanges for 4 but 2 State

pred equalToHEADCommit[s : State, n : Node] {
	let c = HEAD[s] |
		all f : leaves[s, n] |
			let indexObj = (f.samepath & index.s).content,
				workingObj = f.content, 
				commitObj = f.belongsTo.c |
					// file being removed have to be identical to the tip of the branch 
					// (i.e. obj in HEAD commit)
					workingObj = commitObj and  
					// no updates to the file's content can be staged in index
					indexObj = workingObj 
}

-- Remove operation (git rm -r)
-- Alcino: actually remove without -r option does not remove any directories, only files.
-- I'll leave this version as a tentative rm -r
pred rm_rec[s, s' : State, n : Node]{
	-- preconditions
	invariant[s]
	s != s'	
	//	n in current.s 			-- node must exist in the filesystem
	-- file (or files, if n is a dir) must also be in index
	all f : leaves[s,n] | some f.samepath & index.s
	-- n can't be an empty directory
	some leaves[s, n]
	equalToHEADCommit[s, n]
	-- postconditions
	index.s' = index.s - (*parent.n).samepath
	current.s' = current.s - (leaves[s, n] + n)
	HEAD.s' = HEAD.s
	heads.s' = heads.s
	// Eunsuk: What's the right behavior here? Can't always remove, because object might live in an existing commit.
	// For now, assume that object db remains the same and unnecessary objects later get "garbage-collected".
	stored.s' = stored.s 
	refs.s' = refs.s	
}

run rm_rec for 5 but 2 State

check rm_rec_correct {
	all s,s' : State, n : Node | invariant[s] and rm_rec[s,s',n] implies invariant[s']
} for 3 but 2 State


run test {
	one Dir - Root
	some s0, s1 : State, d : Dir {
		rm_rec[s0, s1, d]
		(HEAD[s0]).tree.content[Name] in Blob		
	}
}for 5 but 2 State

-- true iff f1 and f2 have the same path and f2 belongs to commit c
pred commonFiles[f1, f2 : File, s : State, c : Commit] {
	f1 -> f2 in samepath and
	f1 in current.s and
	some f2.belongsTo.c
}

fun merge[f1, f2 : File] : set File {
	{ f3 : File | 
		f1.content -> f2.content -> f3.content in merging }
}	

pred checkout_branch[s, s' : State, c : Commit] {
	-- Preconditions
	invariant[s]
	s != s'
	c in Name.refs.s	-- commit must be referenced
	-- no conflicts between files
	no f1, f2 : File |
		commonFiles[f1, f2, s, c] and
		f1.content -> f2.content in conflict
	-- no modified files 
	no modified[s]	

	-- Postconditions
	-- update the working tree
	-- For two non-conflicting versions of the file that appears in c, merge them
	all f1, f2 : File |
		commonFiles[f1, f2, s, c] implies
			some f3 : File |
				f1 -> f3 in samepath and f3 in current.s' and f3 in merge[f1, f2]

	all n : Node |
		-- every file in the new working tree is either
		n in current.s' iff {
			-- (1) the result of merging two versions of the file or
			(some f1, f2 : File | commonFiles[f1, f2, s, c] and n in merge[f1, f2]) or
			-- (2) the file from the commit being checked out
			(some n.belongsTo.c and no f1 : File | commonFiles[f1, n, s, c]) or
			-- (3) untracked file in the existing tree
			n in untracked[s]
		}
	-- index remains the same
	index.s' = index.s
	-- no new objects stored
	stored.s' = stored.s 
	-- update the HEAD
	HEAD[s'] = c
	heads.s' = heads.s	
	-- refs stay the same
	refs.s' =refs.s
}

run checkout_branch for 5 but exactly 2 State

run checkout_branch_interesting {
	some s1, s2 : State, c : Commit {
		checkout_branch[s1, s2, c]
		HEAD.s2 != HEAD.s1
		some HEAD.s1
		some f1, f2 : File |
			commonFiles[f1, f2, s1, pointsTo[HEAD.s2, s2]] and
			f1.content.merging[f2.content] != f1.content
	}
} for 7 but 2 State, 2 Commit

-- checkout a version of the file f from commit "from" (if provided), or from the HEAD
pred checkout_file[s, s' : State, f : File, from : lone Commit] {
	-- Preconditions
	invariant[s]
	s != s'
	-- specified commit (or HEAD) must contain a file with the same path as f
	some f' : File |
		f -> f' in samepath and
		some f'.belongsTo.(HEAD[s]) or (some from and some f'.belongsTo.from)
	-- Postconditions
	let c = some from implies from else HEAD.s {
		some f' : File |
			f -> f' in samepath and
			some f'.belongsTo.c and
			current.s' = (current.s - f) + f'
	}
	index.s' = index.s - f
	HEAD.s' = HEAD.s
	heads.s' = heads.s
	refs.s' = refs.s
	stored.s' = stored.s
}

run checkout_file for 5 but exactly 2 State

run checkout_file_interesting {
	some s1, s2 : State, f : File, c : Commit {
		checkout_file[s1, s2, f, c]
		some HEAD.s1
		current.s1 != current.s2
		c != HEAD[s1]
		c.tree != HEAD[s1].tree
	}
} for 5 but 2 State

