sig State {}

// paths

sig Name {
	HEAD : set State
}

sig Path {
	name : one Name,
	parent : lone Path,
	index : Blob lone -> State
}

fact {
	// normalization
	no disj p1,p2 : Path | p1.name = p2.name and p1.parent = p2.parent
	// paths are acyclic
	no p : Path | p in p.^parent
}

// file system

abstract sig Node {
	node : set State,
	name : one Name,
	parent : lone Dir,
	path : one Path -- auxiliary relation
}

sig Dir extends Node {
}

sig File extends Node {
	blob : one Blob
}


fact {
	// parent is acyclic
	no n : Node | n in n.^parent
	// siblings have different names
	all s:State , n:node.s | no n.parent => all n2:node.s - n | n.name != n2.name 
	all d : Dir | no disj o1,o2 : parent.d | o1.name = o2.name
	// paths are correct
	all n : Node | n.name = n.path.name and n.parent.path = n.path.parent
	all s:State, n:node.s | one n.parent => n.parent in node.s
}

// git objects and index (see path)

abstract sig Object {
	object : set State,
}

sig Blob extends Object {}

sig Tree extends Object {
	content : Name -> lone (Blob+Tree)
}

fun children : Object -> Object {
	{t : Tree, o : Object | some n : Name | t->n->o in content} 
}

sig Commit extends Object {
	previous : set Commit,
	tree : one Tree,
	path : (Tree+Blob) -> Path -> State
}

fact MatichingObjectsToPaths {
	all s : State {
		path.s in object.s -> object.s -> Path
	}
	all s : State, c : Commit & object.s { 
		c.path.s in (c.tree.^children -> some Path)
		all o : c.tree.children {
			one o.(c.path.s) and no o.(c.path.s).parent and o.(c.path.s).name = c.tree.content.o
		}
		all o : c.tree.children.^children {
			all p : (children.o).(c.path.s) | one p' : o.(c.path.s) | p'.parent = p and p'.name = (children.o).content.o
			all p' : o.(c.path.s) | one p : (children.o).(c.path.s) | p'.parent = p and p'.name = (children.o).content.o
		}
	}
}

/*
check {
	all s : State, c : object.s & Commit, o : Object | lone o.(c.path.s)
} for 6 but 1 State
*/
sig Tag extends Object {
	commit : one Commit,
	name : one Name
}

fun siblings : Path -> Path{
	
	{p1:Path, p2:Path | p1.parent = p2.parent and p1 != p2}

}

fun points : Object -> Object {
	children + previous + tree + commit
}

fun staged : Path -> State {
	{p : Path, s : State | p in (index.s).(object.s)}
}

fun modified : Path -> State {
	{p : Path, s : State | p in staged.s and p.index.s != (path.p & node.s).blob}
}

fun deleted : Path -> State {
	{p : Path, s : State | p in staged.s and no path.p & File & node.s}
}

fun untracked : Path -> State {
	{p : Path, s : State | some path.p & File & node.s and no p.index.s}
}

//check {all s : State | deleted.s in modified.s} for 5

fact {
	// object is closed under refs
	all s : State | (object.s).points in object.s
	// no different trees with the same content
	no disj t1,t2 : Tree | t1.content = t2.content
	// no different commits with the same previous and tree
	no disj c1,c2 : Commit | c1.previous = c2.previous and c1.tree = c2.tree
	// no different tags with the same commit and name
	no disj t1,t2 : Tag | t1.commit = t2.commit and t1.name = t2.name
	// all refs are acyclic
	no ^points & iden
	// indexed blobs are in the objects
	all s : State | index.s in Path -> object.s
	// if a path is in the index none of its ancestors can be
	all s : State | all p1 : staged.s | no p2 : staged.s | p2 in p1.^parent
}

// git references and head

sig Ref in Name {
	head : Commit lone -> State
}

fact {
	all s : State {
		// there always exists one head
		one HEAD.s
		// refs point to an object in the database
		head.s in Ref -> object.s
	}
}




//run {} for 4 but exactly 1 State

//run {all s : State | some modified.s and some deleted.s and some untracked.s} for 4 but exactly 1 State

// non git command
pred other [s,s' : State] {
	object.s' = object.s 
	index.s' = index.s
//	ref.s' = ref.s
	head.s' = head.s
}

//run other for 4 but 2 State

// git add
pred add [s,s' : State,p : Path] {
	s != s'
	p in (node.s).path
	path.p in File
	object.s' = object.s + (path.p).blob
	index.s' = index.s ++ p->(path.p).blob
//	ref.s' = ref.s
	head.s' = head.s
	HEAD.s' = HEAD.s
	node.s' = node.s
}

//run rm for 4 but exactly 2 State

pred oldrm [s,s' : State,p : Path]{
	//preconditions
	p in (index.s).Blob //path must be in index
	no path.p & node.s or one f:path.p & node.s | f.blob = p.index.s  //contents on file must match contents on index
	some o:(((HEAD.s).head.s).path.s).p| o in p.index.s //p must be unmodified since the last commit
	 
	
	//delete new empty dirs
	
	
	
	some path.p & node.s => some path.(p.siblings) & node.s //too strong, because i am not yet deleting empty folders.
	path.p in File & node.s //too strong because i am not yet processing the rm of folders
	

	/*no path.p
	/*p.parent.name = p.name
	one d:File | d in node.s' and d.path = p.parent
	*/
	
	//behaviour
	index.s' = index.s - p->Blob
	node.s' = node.s - path.p
	

	//stuff that stays the same
	object.s' = object.s
	head.s' = head.s
	HEAD.s' = HEAD.s
}

//run rmAB{} for 5 but 2 State

--The files being removed have to be identical to the tip of the branch
pred rmConditionA[s:State,p:Path]{
		(((HEAD.s).head.s).path.s).p & Blob = (path.p & node.s).blob
}

 --in man pages: no updates to their contents can be staged in the index.
pred rmConditionB[s:State,p:Path]{
	  p.index.s in (((HEAD.s).head.s).path.s).p 

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
	object.s' = object.s
	head.s' = head.s
	HEAD.s' = HEAD.s
}



//run gitCommit for 5 but exactly 2 State


pred pc[s:State]{

some index.s
#head.s > 2 
some Commit & object.s & (HEAD.s).head.s

--one Tree

}


fun child: Path -> Path
{
	~parent :> Path
}
 

--run gitCommit

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


pred branchPC[s:State, b:Ref]
{
	some (HEAD.s).head.s
	b not in (head.s).univ
}



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


--run gitCheckout for 4 but 2 State, 15 Node

pred gitCheckout[s,s':State, r:Ref]
{
	checkoutPC[s,r]

	HEAD.s' = r

	

	
	head.s' = head.s
	object.s' = object.s
	--index.s' = index.s
}


