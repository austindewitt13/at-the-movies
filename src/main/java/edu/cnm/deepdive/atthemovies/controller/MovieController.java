package edu.cnm.deepdive.atthemovies.controller;

import edu.cnm.deepdive.atthemovies.model.dao.MovieRepository;
import edu.cnm.deepdive.atthemovies.model.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("movies")
public class MovieController {

    private MovieRepository repository;

    @Autowired
    public MovieController(MovieRepository repository) {
        this.repository = repository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    List<Movie> list(@RequestParam(value = "genre", required = false) Movie.Genre genre){
        if(genre == null) {
            return repository.getAllByOrderByTitleAsc();
        } else  {
            return repository.getAllByGenreOrderByTitleAsc(genre);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    Movie post(@RequestBody Movie movie) {
        return repository.save(movie);// TODO Build a ResponseEntity.

    }
}
