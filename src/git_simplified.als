sig State {}

abstract sig Object {
	object : set State
}

abstract sig Node extends Object {}

sig Blob extends Node {
	index : set State
}

sig Tree extends Node {
	content : some Node,
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

pred add [s,s' : State, n : Node] {
	invariant[s]
	s != s'
	// paht exists
	n in (root.s).^content
	// add all blobs to the index and object
	let blobs = (n.*content & Blob) {
		index.s' = index.s + blobs
		object.s' = object.s + blobs
	}
	// frame
	root.s' = root.s
	head.s' = head.s
	ref.s' = ref.s
}

run add for 3 but 2 State

check {
	all s,s' : State, n : Node | invariant[s] and add[s,s',n] implies invariant[s']
} for 5 but 2 State
