package ir.bigz.transaction.hibernate;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class Parameters {

    private final String sql;
    private final LinkedList<String> keys;

    private static final int EOF = -1;
    private Parameters(String sql, LinkedList<String> keys) {
        this.sql = sql;
        this.keys = keys;
    }

    /**
     * Returns the parsed SQL.
     *
     * @return The parsed SQL.
     */
    public String getSQL() {
        return sql;
    }

    /**
     * Applies the provided argument values to a prepared statement.
     *
     * @param statement The prepared statement.
     *
     * @param arguments The argument values.
     *
     * @throws SQLException If an exception occurs while applying the argument
     *                      values.
     */
    public void apply(PreparedStatement statement, Map<String, ?> arguments) throws SQLException {
        int i = 1;

        for (String key : keys) {
            Object param = arguments.get(key);
            if (param instanceof Collection) {
                for (Object object : ((Collection<?>) param)) {
                    statement.setObject(i++, object);
                }
            } else {
                statement.setObject(i++, param);
            }
        }
    }

    /**
     * Parses a parameterized SQL statement.
     *
     * @param sql A string containing the SQL to parse.
     *
     * @return An {@link Parameters} instance containing the parsed SQL.
     */
    public static Parameters parse(String sql, Map<String, Integer> multiValueParam) {
        if (sql == null) {
            throw new IllegalArgumentException();
        }

        Parameters parameters;
        try (Reader sqlReader = new StringReader(sql)) {
            parameters = parse(sqlReader, multiValueParam);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        return parameters;
    }

    public static Parameters parse(Reader sqlReader) throws IOException {
        return parse(sqlReader, new HashMap<>());
    }

    /**
     * Parses a parameterized SQL statement.
     *
     * @param sqlReader A reader containing the SQL to parse.
     *
     * @return An {@link Parameters} instance containing the parsed SQL.
     *
     * @throws IOException If an exception occurs while reading the SQL statement.
     */
    public static Parameters parse(Reader sqlReader, Map<String, Integer> multiValueParam) throws IOException {
        if (sqlReader == null) {
            throw new IllegalArgumentException();
        }

        LinkedList<String> keys = new LinkedList<>();

        StringBuilder sqlBuilder = new StringBuilder();

        boolean singleLineComment = false;
        boolean multiLineComment = false;
        boolean quoted = false;

        int c = sqlReader.read();

        while (c != EOF) {
            if (c == '-' && !quoted) {
                sqlBuilder.append((char) c);
                c = sqlReader.read();
                singleLineComment = (c == '-') && !multiLineComment;
                sqlBuilder.append((char) c);
                c = sqlReader.read();
            } else if (c == '\r' || c == '\n') {
                sqlBuilder.append((char) c);
                singleLineComment = false;
                c = sqlReader.read();
            } else if (c == '/' && !quoted) {
                sqlBuilder.append((char) c);
                c = sqlReader.read();
                if (c == '*')
                    multiLineComment = true;
                sqlBuilder.append((char) c);
                c = sqlReader.read();
            } else if (c == '*' && multiLineComment) {
                sqlBuilder.append((char) c);
                c = sqlReader.read();
                if (c == '/')
                    multiLineComment = false;
                sqlBuilder.append((char) c);
                c = sqlReader.read();
            } else if (singleLineComment || multiLineComment) {
                sqlBuilder.append((char) c);
                c = sqlReader.read();
            } else if (c == ':' && !quoted) {
                c = sqlReader.read();
                if (c == ':') {
                    sqlBuilder.append("::");
                    c = sqlReader.read();
                } else {
                    StringBuilder keyBuilder = new StringBuilder();
                    while (c != EOF && Character.isJavaIdentifierPart(c)) {
                        keyBuilder.append((char) c);
                        c = sqlReader.read();
                    }

                    keys.add(keyBuilder.toString());

                    if (multiValueParam.containsKey(keyBuilder.toString()))
                        sqlBuilder.append(
                                String.join(",", Collections.nCopies(multiValueParam.get(keyBuilder.toString()), "?")));
                    else
                        sqlBuilder.append("?");
                }
            } else {
                if (c == '\'') {
                    quoted = !quoted;
                }
                sqlBuilder.append((char) c);
                c = sqlReader.read();
            }
        }

        return new Parameters(sqlBuilder.toString(), keys);
    }
}
