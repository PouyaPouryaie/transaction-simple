package ir.bigz.transaction.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Function;

public class SingleResultWork<T> extends ResultWork<T> {

    private T result;

    public SingleResultWork(String query, Function<ResultSet, T> mapper, Map<String, ?> params) {
        super(query, mapper, params);
    }

    public T getResult() {
        return result;
    }

    @Override
    protected void makeResult(ResultSet rs) throws SQLException {
        if (rs.next()) {
            result = mapper.apply(rs);
        }
    }

}
