package ir.bigz.transaction.hibernate;

import org.hibernate.jdbc.Work;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class ResultWork<T> implements Work {

    protected String query;
    protected Function<ResultSet, T> mapper;
    protected Map<String, ?> params;

    public ResultWork(String query, Function<ResultSet, T> mapper, Map<String, ?> params) {
        this.query = query;
        this.mapper = mapper;
        this.params = params;
    }

    abstract protected void makeResult(ResultSet rs) throws SQLException;

    @Override
    public void execute(Connection connection) throws SQLException {
        Map<String, Integer> listParams = params.entrySet()
                .stream()
                .filter(p -> p.getValue() instanceof Collection)
                .collect(Collectors.toMap(Map.Entry::getKey, e -> ((Collection<?>)e.getValue()).size()));
        Parameters parameters = Parameters.parse(query, listParams);
        try (PreparedStatement st = connection.prepareStatement(parameters.getSQL())){
            parameters.apply(st, params);
            makeResult(st.executeQuery());
        }
    }
}
