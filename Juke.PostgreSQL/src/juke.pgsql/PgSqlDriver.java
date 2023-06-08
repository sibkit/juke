package juke.postgresql;

import juke.jdbc.JdbcDriver;
import juke.jdbc.sqlbuilding.SqlBuilder;

public class PgSqlDriver extends JdbcDriver {
    public PgSqlDriver() {
        super.setSqlBuilder(new PgSqlBuilder());
    }
}
