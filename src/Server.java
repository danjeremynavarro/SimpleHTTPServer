/**
 * title: Server.java
 * description: A simple logging capable HTTP server that serves files from a directory
 *
 * To run the program pass the port for the server and the absolute path of the directory to serve. Example
 * <code> java Server 8080 /home/user/Nextcloud/COMP348JavaNetworking/Assignment2/public </code>
 * @author Dan Jeremy Navarro
 * @assignment COMP348 - Assignment 2
 * @date Sept/02/2024
 */
/**
 I declare that this assignment is my own work and that all material previously written
 or published in any source by any other person has been duly acknowledged in the assignment.
 I have not submitted this work, or a significant part thereof, previously as part of any
 academic program. In submitting this assignment I give permission to copy it for assessment
 purposes only.
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <h3>Documentation</h3>
 * A simple logging capable HTTP server that serves a directory and logs all connection. The server will by default serve
 * a file called index.html.
 * The server will create a log file on the same location it is run from.
 * An example to run the program is below:
 * <code> java Server 8080 /home/user/Nextcloud/COMP348JavaNetworking/Assignment2/public </code>
 *
 * <h3>Test Plan</h3>
 * To test the program run the Server program and supply a port number to run from and the absolute path of the directory to run
 * <code> java Server 8080 /home/user/Nextcloud/COMP348JavaNetworking/Assignment2/public </code>
 * 1. Create an index.html file or use the sample directory. The sample directory contains a pdf and img that should be displayed on the web browser
 * 2. Visit the ip address on the port. For example open a web browser and visit the location http://127.0.0.1:8080
 * 3. You should see the index.html file on your browser.
 * 4. All the links that serves resources should display as long as they are all in the same directory
 */
public class Server {
    // The port that the server run from
    private int port;
    // The directory to serve on the webserver
    private String path;
    // The max thread to run
    private final int MAX_THREADS = Runtime.getRuntime().availableProcessors();

    /** Server
     * A simple logging capable HTTP server that serves a directory and logs all connection. The server will by default serve
     * a file called index.html.
     * @param port - the port to serve from
     * @param path - the path to serve from
     */
    public Server(int port, String path){
        this.port = port;
        this.path = path;
    }

    /**
     * Creates multiple threads  according to the maximum processors supported by the host
     */
    public void start(){
        ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);
        try (ServerSocket server = new ServerSocket(port)){
            while (true) {
                // Create a new thread for each connection
                Socket client = server.accept();
                pool.submit(new HTTPHandler(client, path));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Read from the arguments supplied and run the server
    public static void main(String[] args) {
        try {
            int port = Integer.parseInt(args[0]);
            String path = args[1];
            System.out.printf("Running using directory: %s\n", path);
            System.out.printf("Running using port: %d\n", port);
            Server server = new Server(port, path);
            server.start();
        } catch (IndexOutOfBoundsException e){
            System.err.println("Invalid arguments");
        }

    }
}
