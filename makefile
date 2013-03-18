compile:
	javac -d bin -sourcepath src -cp lib/alloy4.2.jar:lib/antlr-3.5-complete.jar src/VallidAlloy.java

	java -cp bin:lib/antlr-3.5-complete.jar:lib/alloy4.2.jar VallidAlloy src/example.cfg




clean:
	$(RM) bin/*.class


rmo:
	rm -rf output
