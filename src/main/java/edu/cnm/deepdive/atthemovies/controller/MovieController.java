package edu.cnm.deepdive.atthemovies.controller;

import edu.cnm.deepdive.atthemovies.model.dao.ActorRepository;
import edu.cnm.deepdive.atthemovies.model.dao.MovieRepository;
import edu.cnm.deepdive.atthemovies.model.entity.Actor;
import edu.cnm.deepdive.atthemovies.model.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("movies")
@ExposesResourceFor(Movie.class)
public class MovieController {

    private final MovieRepository repository;
    private final ActorRepository actorRepository;

    @Autowired
    public MovieController(MovieRepository repository, ActorRepository actorRepository) {
        this.repository = repository;
        this.actorRepository = actorRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> list(@RequestParam(value = "genre", required = false) Movie.Genre genre){
        if(genre == null) {
            return repository.getAllByOrderByTitleAsc();
        } else  {
            return repository.getAllByGenreOrderByTitleAsc(genre);
        }
    }

    @GetMapping(value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Movie> search(@RequestParam(value = "q", required = true) String titleFragment) {
        return repository.getAllByTitleContainsOrderByTitleAsc(titleFragment);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Movie> post(@RequestBody Movie movie) {
        repository.save(movie);
        return ResponseEntity.created(movie.getHref()).body(movie);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Movie get(@PathVariable("id") UUID id) {
        return repository.findById(id).get();
    }

    @Transactional
    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable("id") UUID id) {
       Movie movie = get(id);
       List<Actor> actors = movie.getActors();
       actors.forEach((actor) -> actor.getMovies().remove(movie));
       actorRepository.saveAll(actors);
       repository.delete(movie);
    }

    @PutMapping(value = "{movieId}/actors/{actorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Movie attach(@PathVariable("movieId") UUID movieId, @PathVariable("actorId") UUID actorId) {
        Movie movie = get(movieId);
        Actor actor = actorRepository.findById(actorId).get();
        if (!actor.getMovies().contains(movie)){
            actor.getMovies().add(movie);
        }
        actorRepository.save(actor);
        return movie;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public void notFound() {}

}
