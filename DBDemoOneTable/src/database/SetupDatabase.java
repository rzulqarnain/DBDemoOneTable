package database;

/**
 * Must run at least once to setup the database (DB).
 *
 * Can run by itself, or can call setupDB() from a controller.
 *
 * If run more than once, will restore DB to its original state.
 *
 * Reads in the create.sql script assumed present.
 *
 * Prior to running, must add libraries: cputils.jar, Java DB Driver (will be 3
 * jar files).
 *
 * A successful run will delete and recreate the database DB in the main project
 * folder.
 *
 */
import cputils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetupDatabase {

    private static Connection conn = null;

    // change these if you want to
    public static final String DATABASE = "DB";
    private static final String CREATEFILE = "create.sql";

    public static void createDB() throws IOException {

        deleteDB();

        try {
            System.out.println("Creating new version of " + DATABASE + " -- please wait...");
            conn = ConnectionManager.getConnection(true);
            processFile();
            conn.close();
            System.out.println("Finished");
        } catch (Throwable e) {
            if (e instanceof SQLException) {
                while (e != null) {
                    System.out.println(e.toString());
                    e = ((SQLException) e).getNextException();
                }
            } else {
                e.printStackTrace();
            }
        }
    }

    private static void processFile() throws SQLException {
        Statement s = conn.createStatement();

        String query = null;
        String line = null;

        List<String> lines = FileUtils.readIntoList(CREATEFILE);

        for (int i = 0; i < lines.size(); i++) {
            query = "";
            line = lines.get(i).trim();
            if (line.length() < 4) {
                System.out.println("Blank line: " + line);
            } else if (line.substring(0, 6).equalsIgnoreCase("create")) {
                // found line starting with create, read until semicolon at end of a line:
                do {
                    query += line;
                    if (line.charAt(line.length() - 1) == ';') {
                        // add line and break if found semicolon at end of line
                        query = query.substring(0, query.length() - 1);
                        break;
                    } else {
                        // no semicolon: get another line
                        i++;
                        line = lines.get(i).trim();
                    }
                } while (i < lines.size());
            } else if ((line.substring(0, 6).equalsIgnoreCase("insert")
                    || line.substring(0, 4).equalsIgnoreCase("drop"))
                    && line.charAt(line.length() - 1) == ';') {
                // found line starting with insert or drop and ending with semicolon:
                query = line.substring(0, line.length() - 1);
            } else {
                // cannot parse this line -- could be a comment? (not handled)
                System.err.println("Unexpected line: " + line);
            }
            if (query != null && !query.isEmpty()) {
                System.out.println("Query: " + query);
                s.execute(query);
            }
        }
        s.close();
    }

    public static void deleteDB() throws IOException {
        System.out.println("Deleting old version of " + DATABASE);
        try {
            ConnectionManager.getConnection(false);
            if (new File(DATABASE).exists()) {
                Path directory = Paths.get(DATABASE);
                System.out.println("Deleting all files in folder " + directory + " -- please wait...");
                Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        Files.delete(file);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        Files.delete(dir);
                        return FileVisitResult.CONTINUE;
                    }
                });
                System.out.println("Finished");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SetupDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws IOException {
        createDB();
    }

}
