/**
	* Model of the Git
	* Authors: Jose Pinheiro, Tiago, Guimaraes, Eunsuk Kang, Alcino Cunha
	*
	* Changes from git_dynamics.als:
	* - removed "name" from Path and Node, simplified relevant constraints
	**/

open util/ordering[State] as SO

sig State {}

// Paths
sig Path {
	parent : lone Path,
	-- Eunsuk: Shouldn't this be (Blob + Tree) lone -> State? Can you stage an entire directory?
	index : (Blob  + Tree) lone -> State
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
abstract sig Object {
	-- Eunsuk: Do we need this?
	//object : set State,
}

sig Blob extends Object {}
sig Tree extends Object {
	content : Path -> lone (Blob+Tree)
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
	all s : State | 
		let headCommit = head.s,
			objs = headCommit.tree.^children |
				obj.objs in node.s
	-- there always exists one head
	all s : State | one head.s
}

sig Tag extends Object {
	commit : Commit
}

pred pathExists [s : State, p : Path] {
	p in (node.s).path
}

// git add
pred add [s,s' : State,p : Path] {
	// precondition
	s != s'
	pathExists[s, p]
	// postcondition
	index.s' = index.s ++ p->(path.p).obj
	head.s' = head.s
	node.s' = node.s
}

run {
	some p : Path | add[SO/first, SO/first.next, p]
} for 5 but 2 State


pred commit[s, s' : State] {
	s != s'
	// precondition
	// There are some changes to be committed in the index
	some index.s
	let oldHead = head.s,
		newHead = head.s' {
			oldHead in newHead.previous
			((newHead.tree).^children + newHead.tree).content = index.s
		}
	// postcondiiton
	// The index is empty
	no index.s'
}

run {
	commit[SO/first, SO/first.next]
	head.SO/first.tree != head.(SO/first.next).tree
} for 5 but 2 State






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

// git references and head



//run rm for 4 but exactly 2 State

/*
pred oldrm [s,s' : State,p : Path]{
	//preconditions
	p in (index.s).Blob //path must be in index
	no path.p & node.s or one f:path.p & node.s | f.blob = p.index.s  //contents on file must match contents on index
	some o:((head.s).path.s).p| o in p.index.s //p must be unmodified since the last commit
	 
	//delete new empty dirs
	some path.p & node.s => some path.(p.siblings) & node.s //too strong, because i am not yet deleting empty folders.
	path.p in File & node.s //too strong because i am not yet processing the rm of folders
	
	//behaviour
	index.s' = index.s - p->Blob
	node.s' = node.s - path.p
	

	//stuff that stays the same
	object.s' = object.s
	head.s' = head.s
}
*/

//run rmAB{} for 5 but 2 State

--The files being removed have to be identical to the tip of the branch
pred rmConditionA[s:State,p:Path]{
//		((head.s).path.s).p & Blob = (path.p & node.s).blob
}

 --in man pages: no updates to their contents can be staged in the index.
pred rmConditionB[s:State,p:Path]{
//	  p.index.s in ((head.s).path.s).p 

}
/*
pred rmConditionManPageB[s:State,p:Path]{
	all f:path.p & File | f.blob = p.index.s
}*/

pred rmAB[s,s':State,p:Path]{
	rmConditionA[s,p]
	rmConditionB[s,p]
	rmBehaviour[s,s',p]
}


pred rmA[s,s':State,p:Path]{
	rmConditionA[s,p]
	not rmConditionB[s,p]
	rmBehaviour[s,s',p]
}

pred rmB[s,s':State,p:Path]{
	not rmConditionA[s,p]
	rmConditionB[s,p]
	rmBehaviour[s,s',p]
}

pred rmNOP[s,s':State,p:Path]{
	not rmConditionA[s,p]
	not rmConditionB[s,p]
	rmBehaviour[s,s',p]
}



pred rmBehaviour[s,s':State,p:Path]{

	-- the path must be in the index
	p in (index.s).univ

	some path.p & node.s => some path.(p.siblings) & node.s //too strong, because i am not yet deleting empty folders.
	path.p in File & node.s //too strong because i am not yet processing the rm of folders
	
	
	//behaviour
	index.s' = index.s - p->Blob
	node.s' = node.s - path.p
	

	//stuff that stays the same
//	object.s' = object.s
	head.s' = head.s
}

//run gitCommit for 5 but exactly 2 State


pred pc[s:State]{

some index.s
#head.s > 2 
//some Commit & object.s & head.s

--one Tree

}

fun child: Path -> Path
{
	~parent :> Path
}
 
--run gitCommit

/*
pred gitCommit[s,s':State]{

	pc[s]
	

			--There are differences
			((((HEAD.s).head.s).tree.^children&Blob).(((HEAD.s).head.s).path.s') != (index.s).univ or
			(((HEAD.s).head.s).tree.^children&Tree).(((HEAD.s).head.s).path.s') != ((index.s).univ).^parent)


		one t:Tree |  one c:Commit {
			all b:univ.index.s | b in object.s => b in t.^children
			
		
			
			(t.^children&Blob).(c.path.s') = (index.s).univ
			(t.^children&Tree).(c.path.s') = ((index.s).univ).^parent
	--		t not in object.s
	--		c not in object.s
			c.previous = (HEAD.s).head.s
			(HEAD.s').head.s' = c 
			c.tree = t
			object.s' = object.s + t + c + t.^children
 
 		}


	 

	node.s' = node.s
	index.s' = index.s
	HEAD.s' = HEAD.s
	all p:(Ref - HEAD.s) | p.head.s = p.head.s'

}
*/
/*
pred branchPC[s:State, b:Ref]
{
	some (HEAD.s).head.s
	b not in (head.s).univ
}
*/


/*
pred gitBranch[s,s': State , n:Ref ]{

	branchPC[s,n]

	head.s' = head.s +  (n->((HEAD.s).head.s))
	
	HEAD.s' = HEAD.s	
	object.s' = object.s
	node.s' = node.s
	index.s' = index.s
}

pred checkoutPC[s:State,r:Ref]
{
	r != HEAD.s
	one c:Commit | r in (head.s).c
}
*/

--run gitCheckout for 4 but 2 State, 15 Node
/*
pred gitCheckout[s,s':State, r:Ref]
{
	checkoutPC[s,r]

	HEAD.s' = r
	
	head.s' = head.s
	object.s' = object.s
	--index.s' = index.s
}
*/


