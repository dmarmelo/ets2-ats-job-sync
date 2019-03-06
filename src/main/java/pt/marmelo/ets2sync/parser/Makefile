RAGEL=ragel
JAVAC=javac

default: generate

SiiTextParserGen: 
	$(RAGEL) -J SiiTextParser.rl -o SiiTextParser.java
CfgParserGen: 
	$(RAGEL) -J CfgParser.rl -o CfgParser.java

generate: SiiTextParserGen CfgParserGen

compile: SiiTextParser.java CfgParser.java ParseCallback.java Context.java
	$(JAVAC) SiiTextParser.java CfgParser.java ParseCallback.java Context.java

all: generate compile

clean:
	rm -f SiiTextParser.java SiiTextParser.class CfgParser.java CfgParser.class ParseCallback.class Context.class *~
