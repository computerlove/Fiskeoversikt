package no.lillehaug.landingsopplysninger.repository.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.function.Function;

public class JDBC {

    /*public static <T> T readOnly(DataSource ds, Function<Connection, T> action) throws SQLException {
        Connection c = ds.getConnection();
        boolean readOnly = c.isReadOnly();
        c.setReadOnly(true);
        try(c) {
            return action.apply(c);
        } finally {
            c.setReadOnly(readOnly);
        }
    }

    public static <T> T transactional(DataSource ds, Function<Connection, T> action) throws SQLException {
        Connection c = ds.getConnection();
        boolean autoCommit = c.getAutoCommit();
        c.setAutoCommit(false);
        try(c) {
            return action.apply(c);
        } catch (Throwable e) {
            c.rollback();
            throw e;
        } finally {
            c.setAutoCommit(autoCommit);
        }
    }*/
}
