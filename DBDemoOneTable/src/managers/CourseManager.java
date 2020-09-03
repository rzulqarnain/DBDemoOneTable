/**
 * This class manages one table only.
 *
 * Copy and edit for each additional table.
 */
package managers;

import database.ConnectionManager;
import exceptions.CourseException;
import models.Course;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseManager {

    // global variables used to simplify the open/close for each query
    private static Connection conn;
    private static PreparedStatement ps;
    private static ResultSet rs;

    public static void create(Course data) throws SQLException {

        ps = openAndPrepareStatement("insert into course values(?,?,?)");
        ps.setString(1, data.getId());
        ps.setString(2, data.getName());
        ps.setInt(3, data.getCredits());

        executeQueryAndClose();
    }

    public static void update(Course data) throws SQLException {

        ps = openAndPrepareStatement("update course set name = ?, credits = ? where ID = ?");
        ps.setString(1, data.getName());
        ps.setInt(2, data.getCredits());
        ps.setString(3, data.getId());

        executeQueryAndClose();
    }

    public static void delete(String id) throws SQLException {

        ps = openAndPrepareStatement("delete from course where ID = ?");
        ps.setString(1, id);

        executeQueryAndClose();
    }

    public static List<Course> findAll() throws SQLException {

        ps = openAndPrepareStatement("select * from course");

        return executeQueryAndGetListAndClose();
    }

    public static List<Course> findByCredits(int min, int max) throws SQLException {

        ps = openAndPrepareStatement("select * from course where credits >= ? and credits <= ?");
        ps.setInt(1, min);
        ps.setInt(2, max);

        return executeQueryAndGetListAndClose();
    }

    public static List<Course> findByCourseId(String prefix) throws SQLException {

        ps = openAndPrepareStatement("select * from COURSE where ID LIKE ?");
        ps.setString(1, prefix + "%");

        return executeQueryAndGetListAndClose();
    }

    public static Course findCourse(String id) throws SQLException {

        ps = openAndPrepareStatement("select * from COURSE where ID = ?");
        ps.setString(1, id);

        List<Course> list = executeQueryAndGetListAndClose();
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    public static List<Course> findByCourseName(String name) throws SQLException {

        ps = openAndPrepareStatement("select * from COURSE where NAME LIKE ?");
        ps.setString(1, "%" + name + "%");

        return executeQueryAndGetListAndClose();
    }

    private static List<Course> executeQueryAndGetListAndClose() throws SQLException {
        rs = ps.executeQuery();
        List<Course> list = new ArrayList<>();
        while (rs.next()) {
            try {
                list.add(
                        new Course(
                                rs.getString("ID"),
                                rs.getString("NAME"),
                                rs.getInt("CREDITS")
                        )
                );
            } catch (CourseException ex) {
                System.out.println("Course data invalid: " + rs.getString("ID") + " -- " + ex.getMessage());
            }
        }
        close();
        return list;
    }

    private static void executeQueryAndClose() throws SQLException {
        ps.execute();
        close();
    }

    private static PreparedStatement openAndPrepareStatement(String query) throws SQLException {
        conn = ConnectionManager.getConnection(true);
        return conn.prepareStatement(query);
    }

    private static void close() throws SQLException {
        if (rs != null) {
            rs.close();
        }
        ps.close();
        conn.close();
    }
}
