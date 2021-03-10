package ir.bigz.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransactionRepository extends JpaRepository<Human, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Human h set h.isActive = :active where h.id = :id")
    int updateHuman(@Param("id") Long id, @Param("active") boolean active);
}
