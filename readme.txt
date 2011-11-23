prefuse README
beta release - initial release february 2006, current release 2007.10.20

--INTRO--

prefuse is a Java-based toolkit for building interactive information
visualization applications. Prefuse supports a rich set of features for data
modeling, visualization, and interaction. It provides optimized data
structures for tables, graphs, and trees, a host of layout and visual encoding
techniques, and support for animation, dynamic queries, integrated search, and
database connectivity. Prefuse is written in Java, using the Java 2D graphics
library, and is easily integrated into Java Swing applications or web applets.
Prefuse is licensed under the terms of a BSD license, and can be freely used
for both commercial and non-commercial purposes.

--STRUCTURE--

The toolkit distribution uses the following organization:

+ prefuse
|-- build  Directory where compiled classes and jar files are placed
|-- data   Various example data files used by the demo applications
|-- demos  Demo applications and applets showing the toolkit in use
|-- doc    Documentation. The Javadoc API files reside here once generated
|-- lib    Third-party libraries useful with prefuse and their licenses
|-- src    The source code for the prefuse toolkit
|-- test   JUnit tests for the toolkit (still a bit sparse at the moment)

--REQUIREMENTS--

prefuse is written in Java 1.4, using the Java2D graphics library. To compile
the prefuse code, and to build and run prefuse applications, you'll need a
copy of the Java Development Kit (JDK) for version 1.4.2 or greater. You can
download the most recent version of the JDK from
http://java.sun.com/j2se/1.5.0/download.html.

We also recommended (though by no means is it required) that you use an
Integrated Development Environment such as Eclipse (http://eclipse.org).
Especially if you are a Java novice, it will likely make your life much easier.

--BUILDING--

prefuse uses the Ant system from the Apache Group to compile the files. Ant is
bundled with this distribution, and can be run using the "build.bat" script
(in Windows) or the "build.sh" script (in UNIX). For example, running
"build.bat usage" will provide a list of available commands, "build.bat all"
will compile the toolkit and demos into jar files, running "build.bat api"
will generate the html API documentation for the toolkit in the doc directory.

Alternatively, you can use the Eclipse integrated development environment
(available for free at http://ww.eclipse.org) to load the source files, then
Eclipse will compile the software for you. Within Eclipse, right-click the
background of the "Package Explorer" panel and choose "Import". Then select
"Existing Projects into Workspace". In resulting dialog, click the radio
button for "Select archive file" and browse for the prefuse distribution
zip file. The "prefuse" project should then appear in the area below.
Now just click the "Finish" button to import the project and build it.
Once prefuse has been loaded as a project within Eclipse, you
can then run various demos directly from within Eclipse by right-clicking the
class file for a demo (e.g., demos/prefuse.demos.GraphView.java) and
selecting "Run >> Java Application" from the menu.

--MORE--

Additional information and documentation, a help forum, a gallery of featured
prefuse applications, and more, are available online. Point your web browser at
 -- http://prefuse.org --
