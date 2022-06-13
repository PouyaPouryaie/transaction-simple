package ir.bigz.transaction.hibernate;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.ResultSet;
import java.util.function.Function;

@Transactional(readOnly = true)
public abstract class TransactionalServiceAbs {

    @PersistenceContext
    public EntityManager em;

    public <T> NativeQuery<T> createNativeQuery(String query, Function<ResultSet, T> mapper) {
        return NativeQuery.createNativeQuery(em, query, mapper);
    }
}
