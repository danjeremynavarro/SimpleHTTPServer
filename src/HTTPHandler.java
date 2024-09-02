import java.io.*;
import java.net.Socket;

/**
 * A simple HTTP server that logs all request and serves files from a directory.
 * The class is implemented as a Runnable so that the each connection is implemented in a thread.
 */
public class HTTPHandler implements Runnable {
    // The Socket class of the client connection
    private final Socket connection;

    // The default directory to serve the file from
    private String home;

    // The logger object that logs connections to a file
    private final LogHelper logger = LogHelper.getInstance();

    /**
     * A simple HTTP server that logs all request and serves files from a directory.
     * @param connection - The Socket connection object
     * @param home - The directory to serve file from
     * @throws IOException
     */
    HTTPHandler(Socket connection, String home) throws IOException {
        this.connection = connection;
        this.home = home;
    }

    /**
     * Gets the filename to be served from the request
     * @param request
     * @return
     */
    private String getPath(String request){
        StringBuilder path = new StringBuilder();
        // According to the HTTP protocol. The path is after the first 2 spaces
        boolean space = false;
        for (char c : request.toCharArray()) {
            if (c == ' ' && !space) {
                space = true;
            } else if (c == ' ' && space) {
                break; // End of path request
            } else if (space) { // We are in the request character
                path.append(c);
            }
        }
        return path.toString();
    }

    /**
     * Gets the method used by the client for logging
     * @param request
     * @return
     */
    private String getMethod(String request){
        StringBuilder path = new StringBuilder();
        for (char c : request.toCharArray()) {
            if (c == ' ' ) {
                break;
            } else { // We are in the request character
                path.append(c);
            }
        }
        return path.toString();
    }

    /**
     * Gets the request header from the connection. Reads the file to serve. and return a FileHandler instance
     * @param request
     * @return a FileHandler instance
     * @throws IOException
     */
    public FileHandler handleRequest(String request) throws IOException {
        FileHandler file = new FileHandler(home, this.getPath(request));
        file = file.readFile();
        return file;
    }

    /**
     * Runs the web server
     */
    @Override
    public void run() {
        try {
            // Opens an outputstream on the connection
            OutputStream outputStream = new BufferedOutputStream(
                    connection.getOutputStream()
            );
            // Opens an inputstream on the connection
            InputStream inputStream = new BufferedInputStream(
                    connection.getInputStream()
            );
            // A stringbuiler used to store the request
            StringBuilder stringBuilder = new StringBuilder();
            // Used to mark the end of a line
            boolean end = false;
            // Loop through all the request
            while (true) {
                int c = inputStream.read();
                if (c == -1) {break;}
                char character = (char) c;
                stringBuilder.append(character);
                if (character == '\n' && !end) {
                    end = true;
                } else if (character != '\n' && character != '\r') {
                    end = false;
                } else if (character == '\n'){
                    break; // Second instance of line terminator in sequence which ends a typical http request
                }
            }
            // The request in a string
            String request = stringBuilder.toString();
            try {
                // The file to serve
                FileHandler file = this.handleRequest(request);
                // The bytearray that contains the bytes to be served
                byte[] content = file.getData();
                String header = "HTTP/1.1 200 OK\r\n"
                        + "Server: danjeremynavarro.com\r\n"
                        + "Content-length: " + content.length + "\r\n"
                        + "Content-type: " + file.getMimeType() + "\r\n\r\n";
                // Logs the connection to a file
                logger.log(connection.getInetAddress().getHostAddress(),
                        this.getMethod(request),
                        this.getPath(request),
                        content.length,
                        200);
                outputStream.write(header.getBytes());
                outputStream.write(content);
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                // If file is not found return a 404 and log to a file
                logger.log(connection.getInetAddress().getHostAddress(),
                        this.getMethod(request),
                        this.getPath(request),
                        0,
                        404);
                String header = "HTTP/1.1 404 Not Found\r\n"
                        + "Content-length: " + "0\r\n";
                outputStream.write(header.getBytes());
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                // If the server fails for whatever reason send a code 500 and log to file
                logger.log(connection.getInetAddress().getHostAddress(),
                        this.getMethod(request),
                        this.getPath(request),
                        0,
                        500);
                String header = "HTTP/1.1 500 Server Error\r\n"
                        + "Content-length: " + "0\r\n";
                outputStream.write(header.getBytes());
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
