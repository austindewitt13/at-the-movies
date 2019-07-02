package edu.cnm.deepdive.atthemovies.controller;

import edu.cnm.deepdive.atthemovies.model.dao.MovieRepository;
import edu.cnm.deepdive.atthemovies.model.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.management.MonitorInfo;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("movies")
@ExposesResourceFor(Movie.class)
public class MovieController {

    private MovieRepository repository;

    @Autowired
    public MovieController(MovieRepository repository) {
        this.repository = repository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> list(@RequestParam(value = "genre", required = false) Movie.Genre genre){
        if(genre == null) {
            return repository.getAllByOrderByTitleAsc();
        } else  {
            return repository.getAllByGenreOrderByTitleAsc(genre);
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public Movie post(@RequestBody Movie movie) {
        return repository.save(movie);// TODO Build a ResponseEntity.

    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Movie get(@PathVariable("id") UUID id) {
        return repository.findById(id).get();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public void notFound() {}
}
