compile:
	javac $(tr '\n' ' ' < src/tinylog.properties) -d bin -sourcepath src -cp lib/antlr-3.5-complete.jar:lib/alloy4.2.jar:lib/commons-io-2.4.jar:lib/tinylog.jar src/VallidAlloy.java

clean:
	$(RM) bin/*.class


rmo:
	rm -rf output
