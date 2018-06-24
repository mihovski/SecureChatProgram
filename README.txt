File Structure:

'Secure Chat Final'- the chat application, contains the two instances 'ChatCLient'and 'ChatServer'.

'ChatClient'- the client instance of the application, contains a 'src' folder which represents the packages,classes and methods used in the client part of the program.

'ChatServer'- the server instance of the application, contains a 'src' folder which represents the packages, classes and methods used in the server part of the program.

*Tests are located in 'ChatClient\src' in the 'test' package.

Installation Eclipse:

1.	A working version of ‘Eclipse IDE for Java Developers’ is required to run the program (the prototype was developed and tested on ‘Version: Oxygen.2 Release (4.7.2)’).

2.	Import both projects in ‘Eclipse’ (‘ChatServer’ and ‘ChatClient’) using the build in import feature and selecting the import method to be ‘projects from folder or archive’

3.	The already imported two projects would contain a single main folder: ‘src’, for the source code of the project.

4.	To start the server instance of the program simply navigate to ‘ChatServer\src\server’ and run class ‘ChatServer.java’ to start the server of the application using local host.

5.	To start the client instance of the program simply navigate to ‘ChatClient\src\client’ and run class ‘SecureChat.java’ to start a single interface of the program, however for testing purposes run ‘SecureChat.java’ twice to start two interfaces which would represent the communication between two users.

6.	The program would recognise that both the server and client are started properly and it would establish the connection between both interfaces.

7.	To run the tests of the program navigate to ‘ChatClient\test’ and run the desired test as a Junit test.

*The program also does read command line arguments if the user wishes to manually input port for server and IP and port for client.

Usage:

The usage of the program is described in details in the report with screenshot examples of each functionality.