package edu.cnm.deepdive.atthemovies.model.dao;

import edu.cnm.deepdive.atthemovies.model.entity.Movie;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface MovieRepository extends CrudRepository<Movie, UUID> {

    List<Movie> getAllByOrderByTitleAsc();

    List<Movie> getAllByGenreOrderByTitleAsc(Movie.Genre genre);

    List<Movie> getAllByTitleContainsOrderByTitleAsc(String titleFragment);



}
