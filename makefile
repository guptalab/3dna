JAVAC=javac
JAVA=java
MAIN=MainFrame
TEST=Test
sources = $(wildcard *.java)
classes = $(sources:.java=.class)
CLASSPATH= .:./jar_dependencies/j3dcore.jar:./jar_dependencies/j3dutils.jar:./jar_dependencies/vecmath.jar:./jar_dependencies/itextpdf-5.4.1.jar:./jar_dependencies/jcommon-1.0.20.jar:./jar_dependencies/jfreechart-1.0.16.jar 
all:	$(classes)

clean :
		rm -f *.class

%.class : %.java
		 $(JAVAC) -classpath $(CLASSPATH) $<
run: $(MAIN).class
	    java -cp $(CLASSPATH) $(MAIN)
test: $(TEST).class
		java -cp $(CLASSPATH) $(TEST) 
jar:
		jar cfm 3DNA.jar MANIFEST.MF *.class *.java
