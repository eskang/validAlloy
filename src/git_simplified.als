sig State {}

sig Name {}

abstract sig Node {
	name : one Name,
	parent : lone Dir,
	current : set State
}

sig File extends Node {
	content : one Blob,
	index : set State
}

sig Dir extends Node {}

one sig Root extends Dir {}

fact Tree {
	all n : Node-Root | one n.parent
	no Root.parent
	all n : Node | n not in n.^parent
}

abstract sig Object {
	stored : set State
}

sig Blob extends Object {}

sig Tree extends Object {
	content : Name -> lone (Blob+Tree)
}

fun children : Tree -> Object {
	{t : Tree, o : Object | some n : Name | t->n->o in content}
}

sig Commit extends Object {
	previous : set Commit,
	tree : one Tree,
	head : set State,
	ref : set State
}

fact Acyclic {
	// Acyclic commits
	no c : Commit | c in c.^previous
	// Acyclic trees
	no t : Tree | t in t.^children
}

fact Atoms {
	Node = current.State
	Object = stored.State
}

pred invariant [s : State] {
	// Current file system
	all n : current.s | n.parent in current.s
	Root in current.s
	all d : current.s, disj x,y : (parent.d) & current.s | x.name != y.name
	// Stored objects
	all t : stored.s & Tree | Name.(t.content) in stored.s
	all c : stored.s & Commit | c.previous in stored.s and c.tree in stored.s
	ref.s in stored.s
	// index blobs must be in the object database
	(index.s).content in stored.s
	// at most one head
	lone head.s
	// head must be a reference
	head.s in ref.s
}

run invariant for 4 but 1 State

fun leaves [s : State, n : Node] : set File {
	(*parent).n & File & current.s
}

pred add [s,s' : State, n : Node] {
	invariant[s]
	s != s'
	// paht exists
	n in current.s
	// add paths to index
	index.s' = index.s + leaves[s,n]
	// add blobs to object
	stored.s' = stored.s + leaves[s,n].content
	// frame
	current.s' = current.s
	head.s' = head.s
	ref.s' = ref.s
}

run add for 3 but 2 State

check {
	all s,s' : State, n : Node | invariant[s] and add[s,s',n] implies invariant[s']
} for 5 but 2 State
