import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * <h3>Documentation</h3>
 * A helper class that open the files to be served by the webserver
 */
public class FileHandler {
    // The location to serve the files from
    private String path;
    // The file to serve
    private String file;
    // The mimetype for the file to serve
    private String mimeType;
    // The bytearray of the file to be served
    private byte[] data;

    /**
     * A class that stores the mimetype and the bytearray of the file to be served.
     * @param path - the directory to serve the file from
     * @param file - the filename to serve
     */
    FileHandler(String path, String file) {
        this.path = path;
        // If no filename given serve the index.html file instead
        if (file.equals("/")){
            this.file = "index.html";
        } else {
            this.file = file;
        }
    }

    // Reads the file, fetch the mimetype and store it in the bytearray
    public FileHandler readFile() throws IOException {
        Path p = FileSystems.getDefault().getPath(path, file);
        data = Files.readAllBytes(p);
        // Get the mimetype according to the filename
        mimeType = URLConnection.getFileNameMap().getContentTypeFor(p.getFileName().toString());
        return this;
    }

    // Returns the mimetype
    public String getMimeType() {
        return mimeType;
    }

    // Returns the bytearray of the file
    public byte[] getData() {
        return data;
    }
}
