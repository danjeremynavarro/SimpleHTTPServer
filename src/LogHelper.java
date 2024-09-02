import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * A LogHelper singleton class that creates an object to be used for logging.
 */
public class LogHelper {
    // The single instance of Loghelper
    private static final LogHelper instance = new LogHelper();

    // A simple java logger
    private final Logger log = Logger.getLogger("HttpServer");

    // Returns the single instance of LogHelper
    public static LogHelper getInstance() {
        return instance;
    }

    /**
     * LogHelper is private so it cant be instantiated. It configures the Logger to save on a file
     * and to store the logs in a specific format
     */
    private LogHelper() {
        try {
            // A filehandler used to store the logs
            java.util.logging.FileHandler fileHandler = new java.util.logging.FileHandler("./log.txt", 5242880,10,true);
            // Add a custom formatter to the logger
            fileHandler.setFormatter(new Formatter(){
                // The log format
                private String format = "%s\n";

                @Override
                public synchronized String format(LogRecord record) {
                    return String.format(format, record.getMessage());
                }
            });
            // Add the filehandler to the logger
            log.addHandler((Handler)fileHandler);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    // The log method used to log to the file
    public void log(String host,
                     String method,
                     String path,
                     int length,
                     int code){
        // Get the current time
        LocalDateTime now = LocalDateTime.now();
        // Format the time according to the webserver format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss xxxx");
        // Set the timezone to utc on the log
        String date = now.atZone(ZoneOffset.UTC).format(formatter);
        // Format the log line
        String logText = String.format("%s - - [%s] \"%s %s HTTP/1.1\" %d %d",host,date,method,path,code, length);
        // Log to the file
        log.info(logText);
    }
}
