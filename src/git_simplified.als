sig State {}

abstract sig Object {
	object : set State
}

sig Blob extends Object {
	index : set State
}

sig Tree extends Object {
	content : some (Blob+Tree),
	root : set State
}

sig Commit extends Object {
	previous : set Commit,
	tree : one Tree,
	head : set State,
	ref : set State
}

pred invariant [s : State] {
	// referential integrity
	all t : object.s & Tree | t.content in object.s
	all c : object.s & Commit | c.previous in object.s and c.tree in object.s
	ref.s in object.s
	// acyclic
	no t : Tree | t in t.^content
	no c : Commit | c in c.^previous
	// index must be in the object database
	index.s in object.s
	// at most one head
	lone head.s
	// head must be a reference
	head.s in ref.s
	// there is always one root in the filesystem
	one root.s
}

run invariant for 4 but 1 State
