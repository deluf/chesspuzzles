package it.unipi.chesspuzzles.server.database;

import org.springframework.data.repository.CrudRepository;

public interface FavouriteRepository extends CrudRepository<DatabaseFavourite, Integer> {
    Iterable<DatabaseFavourite> findAllByUserId(Integer userId);
}
