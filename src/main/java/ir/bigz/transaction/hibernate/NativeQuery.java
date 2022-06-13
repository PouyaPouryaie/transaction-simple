package ir.bigz.transaction.hibernate;

import org.hibernate.JDBCException;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NativeQuery<T> {

    private final EntityManager em;
    private final Map<String, Object> params = new HashMap<>();
    private final String query;
    private final Function<ResultSet, T> mapper;

    private NativeQuery(EntityManager em, String query, Function<ResultSet, T> mapper) {
        this.em = em;
        this.query = query;
        this.mapper = mapper;
    }

    public static <T> NativeQuery<T> createNativeQuery(EntityManager em, String query, Function<ResultSet, T> mapper) {
        return new NativeQuery<>(em, query, mapper);
    }

    public NativeQuery<T> setParameter(String name, Object value) {
        params.put(name, value);
        return this;
    }

    public NativeQuery<T> setParameters(Map<String, Object> params) {
        this.params.putAll(params);
        return this;
    }

    public List<T> getResultList() {
        ListResultWork<T> work = new ListResultWork<>(query, mapper, params);
        doWork(work);
        return work.getResult();
    }

    public T getSingleResult() {
        SingleResultWork<T> work = new SingleResultWork<>(query, mapper, params);
        doWork(work);
        return work.getResult();
    }


    private void doWork(ResultWork<T> work) {
        try {
            em.unwrap(Session.class).doWork(work);
        } catch (Exception e) {
            String paramStr = params.entrySet()
                    .stream()
                    .map(o -> o.getKey() + ":" + o.getValue())
                    .collect(Collectors.joining(","));
            String message = (e.getMessage() + (e instanceof JDBCException ? ", sql exception: " +
                    ((JDBCException)e).getSQLException().getMessage() : "")) + "\nparams: " + paramStr + "\nquery: " + query;
            throw new RuntimeException(message, e);
        }
    }
}
