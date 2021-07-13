I have packaged my source code into jar files for simplicity of running it and because I am not sure if all lab machines have java 14, whose features I used (switch expressions, specifically). Since this is a terminal application, it will need to be run from a console. Alternatively, you can run each project’s main classes from an IDE if you so choose and the machine has Java 14.


Running jars:
1. Open Windows command line or another console of your choice
2. Navigate to the folder with the jars.
3. Run command “java -jar Client.jar” or “java -jar Server.jar” to start a server or a client instance, respectively.


Running from an IDE:
1. Make sure you have java 14 or later installed.
2. Unpack the Server and Client archives as 2 separate projects
3. Run Server.main to start a server or Client.main to start a client. Don’t forget to configure clients to be allowed to run in parallel.