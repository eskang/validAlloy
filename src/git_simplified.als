sig State {}

sig Name {}

abstract sig Node {
	name : one Name,
	parent : lone Dir,
	current : set State,
	samepath : set Node -- auxiliary relation
}

fact SamePath {
	all x,y : Node | x->y in samepath iff (x.name = y.name and ((no x.parent and no y.parent) or (some x.parent and some y.parent and x.parent->y.parent in samepath)))
}

run SamePathsArePossible {
	all s : State | invariant[s]
	some samepath-iden
} for 4 but 2 State

sig File extends Node {
	content : one Blob,
	index : set State
}

sig Dir extends Node {
	tbc : Tree -> State -- auxiliary relation
}

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

pred tbc [s : State] {
	// trees ready to-be-commited
	all d : Dir | some ^parent.d & index.s implies {
		one d.(tbc.s)
		d.(tbc.s).content = {n : Name, o : Object | some x : parent.d & (index.s).*parent | o = x.(tbc.s+content) and n = x.name}
	}
}

fun leaves [s : State, n : Node] : set File {
	(*parent).n & File & current.s
}

pred add [s,s' : State, n : Node] {
	invariant[s]
	s != s'
	// paht exists
	n in current.s
	// add paths to index
	index.s' = index.s - n.*parent.samepath + leaves[s,n]
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

pred commit [s,s' : State] {
	invariant[s]
	tbc[s]
	s != s'
	some index.s
	some c : Commit-stored.s {
		c.previous = head.s
		head.s' = c
		c.tree = Root.(tbc.s)
		stored.s' = stored.s + (index.s).^parent.(tbc.s) + c
	}
	current.s' = current.s
	ref.s' = ref.s
	index.s' = index.s
}

run commit for 4 but 2 State

check {
	all s,s' : State | invariant[s] and commit[s,s'] implies invariant[s']
} for 3 but 2 State

