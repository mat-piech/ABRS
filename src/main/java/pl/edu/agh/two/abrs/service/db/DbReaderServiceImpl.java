package pl.edu.agh.two.abrs.service.db;

import org.springframework.stereotype.Service;
import pl.edu.agh.two.abrs.Row;
import pl.edu.agh.two.abrs.model.ColumnType;
import pl.edu.agh.two.abrs.model.LocalSchemaColumn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DbReaderServiceImpl implements DbReaderService {

    private static final String SELECT_STATEMENT_PATTERN = "SELECT * FROM %s";

    private Connection connect(ConnectionParams params) throws DbReaderException {
        try {
            Class.forName(params.driverClassName);
        } catch (ClassNotFoundException e) {
            throw new DbReaderException("Cannot find driver: " + params.driverClassName, e);
        }
        try {
            return DriverManager.getConnection(params.connectionString, params.username, params.password);
        } catch (SQLException e) {
            throw new DbReaderException(e);
        }
    }

    public void testConnection(ConnectionParams params) throws DbReaderException {
        Connection connection = connect(params);
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DbReaderException(e);
        }
    }

    public List<Row> readTable(ConnectionParams params, String tableName) throws DbReaderException {
        return readSql(params, String.format(SELECT_STATEMENT_PATTERN, tableName));
    }

    public List<Row> readSql(ConnectionParams params, String sql) throws DbReaderException {
        List<Row> rows = new ArrayList<>();

        try (Connection connection = connect(params)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();

            while (resultSet.next()) {
                List<Object> fields = new ArrayList<>();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    fields.add(resultSet.getObject(i + 1));
                }
                rows.add(new Row(fields));
            }

            return rows;
        } catch (SQLException e) {
            throw new DbReaderException(e);
        }
    }

    public List<LocalSchemaColumn> getColumns(ConnectionParams params, String tableName) throws DbReaderException {

        List<LocalSchemaColumn> columns = new ArrayList<>();

        try (Connection connection = connect(params)) {
            Statement statement = connection.createStatement();

            String sql = String.format(SELECT_STATEMENT_PATTERN, tableName);
            ResultSet res = statement.executeQuery(sql);
            ResultSetMetaData metadata = res.getMetaData();

            for (int i = 1; i <= metadata.getColumnCount(); i++) {
                columns.add(createLocalSchemaColumn(metadata.getColumnLabel(i), metadata.getColumnType(i)));
            }
        } catch (SQLException e) {
            throw new DbReaderException(e);
        }
        return columns;
    }

    private LocalSchemaColumn createLocalSchemaColumn(String name, int type) throws DbReaderException {
        ColumnType columnType;
        switch (type) {
            case Types.BOOLEAN:
                columnType = ColumnType.BOOLEAN;
                break;
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
                columnType = ColumnType.INTEGER;
                break;
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                columnType = ColumnType.DOUBLE;
                break;
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
            case Types.CLOB:
            case Types.NCLOB:
                columnType = ColumnType.STRING;
                break;
            case Types.DATE:
            case Types.TIMESTAMP:
            case Types.TIME:
                columnType = ColumnType.DATE;
                break;
            // Not supported below that line//
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.NULL:
            case Types.OTHER:
            case Types.JAVA_OBJECT:
            case Types.DISTINCT:
            case Types.STRUCT:
            case Types.BLOB:
            case Types.REF:
            case Types.DATALINK:
            case Types.ROWID:
            case Types.SQLXML:
            default:
                throw new DbReaderException("Not supported JDBC type (java.sql.Types): " + type + ".");
        }

        return new LocalSchemaColumn(name, columnType, null);
    }
}