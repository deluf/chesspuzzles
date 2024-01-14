package it.unipi.chesspuzzles.server.database;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface AttempRepository extends CrudRepository<DatabaseAttemp, Integer> {
    Iterable<DatabaseAttemp> findAllByUserIdAndTimestampAfter(Integer userId, LocalDateTime timestamp);
}
