compile:
	javac -d bin -sourcepath src -cp lib/alloy4.2.jar:lib/antlr-3.5-complete.jar:lib/commons-io-2.4.jar src/VallidAlloy.java

clean:
	$(RM) bin/*.class


rmo:
	rm -rf output
