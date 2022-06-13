package ir.bigz.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * / 1 - The @Modifying is used to enhance the @Query annotation to execute not only SELECT queries but also
 *   INSERT, UPDATE, DELETE, and even DDL queries.
 *
 *   2 - you can clearAutomatically in @Modifying when you want change db directly
 */

@Repository
public interface TransactionRepository extends JpaRepository<Human, Long> {

//    @Transactional
//    @Modifying
//    @Query("update Human h set h.isActive = ?2 where h.id = ?1")
//    int updateHuman(Long id, boolean active);


    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Human h set h.isActive = :active where h.id = :id")
    int updateHuman(@Param("id") Long id, @Param("active") boolean active);

    @Query(value = "select h.first_name, h.last_name, h.national_code  from Human h where h.age = :age", nativeQuery = true)
    List<Map<String, Object>> getHumanByAge(@Param("age") Long age);
}
