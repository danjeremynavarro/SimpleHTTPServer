 
This is run using OpenJDK 22 and using a linux operating system

Directory structure
 - out: contains the compiled class
 - src: sourcefiles
 - public: sample index.html file with img and pdf file that is to be served by the webserver

Documentation and the main class is the Server.java file

A directory called public is provided that is used for testing. It contains an html file and an img and pdf that should be rendered when the program is run


 -- DOCUMENTATION --
A simple logging capable HTTP server that serves a directory and logs all connection. The server will by default serve
a file called index.html.
The server will create a log file on the same location it is run from.

An example to run the program is below:
java Server 8080 /home/user/Nextcloud/COMP348JavaNetworking/Assignment2/public

To test the program run the Server program and supply a port number to run from and the absolute path of the directory to run
java Server 8080 /home/user/Nextcloud/COMP348JavaNetworking/Assignment2/public

1. Create an index.html file or use the sample directory. The sample directory contains a pdf and img that should be displayed on the web browser
2. Visit the ip address on the port. For example open a web browser and visit the location http://127.0.0.1:8080
3. You should see the index.html file on your browser.
4. All the links that serves resources should display as long as they are all in the same directory
