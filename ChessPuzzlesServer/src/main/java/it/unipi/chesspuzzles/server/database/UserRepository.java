package it.unipi.chesspuzzles.server.database;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends CrudRepository<DatabaseUser, String> {
    DatabaseUser findByUsername(String username);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE user SET id = ?1 WHERE username = ?2")
    void setCustomId(Integer newId, String username);
}
