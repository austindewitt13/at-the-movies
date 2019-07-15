package edu.cnm.deepdive.atthemovies.model.dao;

import edu.cnm.deepdive.atthemovies.model.entity.Genre;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface GenreRepository extends CrudRepository<Genre, UUID> {

    List<Genre> getAllByOrderByNameAsc();

}
