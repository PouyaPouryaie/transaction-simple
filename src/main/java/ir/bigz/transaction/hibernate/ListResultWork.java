package ir.bigz.transaction.hibernate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ListResultWork<T> extends ResultWork<T> {

    private final List<T> result = new ArrayList<>();

    public ListResultWork(String query, Function<ResultSet, T> mapper, Map<String, ?> params) {
        super(query, mapper, params);
    }

    public List<T> getResult() {
        return result;
    }

    @Override
    protected void makeResult(ResultSet rs) throws SQLException {
        while (rs.next()){
            result.add(mapper.apply(rs));
        }
    }
}
