pred show[s,s':State,p:Path]
{
	s != s'
	some Commit <: object.s
	add[p,s,s']
}

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
	root : set State
}

sig File extends Node {
	blob : one Blob
}

fact {
	all s : State {
		// one root
		one root.s & node.s
		// root has no parent
		no (root.s).parent
		// everyone else has one
		all o : node.s - root.s | some o.parent & node.s
	}
	// parent is acyclic
	no n : Node | n in n.^parent
	// siblings have different names
	all d : Dir | no disj o1,o2 : parent.d | o1.name = o2.name
	// paths are correct
	all n : Node | n.name = n.path.name and n.parent.path = n.path.parent
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

check {
	all s : State, c : object.s & Commit, o : Object | lone o.(c.path.s)
} for 6 but 1 State

sig Tag extends Object {
	commit : one Commit,
	name : one Name
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
	not (node.s' = node.s and root.s' = root.s)
}

//run other for 4 but 2 State

// git add
pred add [p : Path, s,s' : State] {
	p in (node.s).path
	path.p in File
	object.s' = object.s + (path.p).blob
	index.s' = index.s ++ p->(path.p).blob
//	ref.s' = ref.s
	head.s' = head.s
	HEAD.s' = HEAD.s
	node.s' = node.s
	root.s' = root.s
}

pred rm [p : Path, s,s' : State] {
	p in (index.s).Blob

	object.s' = object.s
	index.s' = index.s - p->Blob
	head.s' = head.s
	HEAD.s' = HEAD.s
	node.s' = node.s
	root.s' = root.s
}



//run add for 4 but 2 State
=======
run rm for 8 but exactly 2 State
