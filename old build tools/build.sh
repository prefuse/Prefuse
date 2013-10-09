#!/bin/sh

# prefuse build script

echo "building prefuse..."


if [ "$JAVA_HOME" = "" ] ; then
    echo "... BUILD FAILED"
    echo " The JAVA_HOME environment variable was not found."
    echo " Please set the environment variable JAVA_HOME to the location"
    echo " of your preferred Java installation."
    exit 1
fi

LOCALCLASSPATH=$JAVA_HOME/lib/tools.jar:./lib/ant.jar
ANT_HOME=./lib

echo ... using classpath $CLASSPATH:$LOCALCLASSPATH
echo

echo Starting Ant...
echo

$JAVA_HOME/bin/java -Dant.home=$ANT_HOME -classpath $LOCALCLASSPATH:$CLASSPATH org.apache.tools.ant.Main $*
