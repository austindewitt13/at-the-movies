package edu.cnm.deepdive.atthemovies.controller;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.atthemovies.model.dao.ActorRepository;
import edu.cnm.deepdive.atthemovies.model.dao.MovieRepository;
import edu.cnm.deepdive.atthemovies.model.entity.Actor;
import edu.cnm.deepdive.atthemovies.model.entity.Movie;
import edu.cnm.deepdive.atthemovies.view.FlatActor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("actors")
@ExposesResourceFor(Actor.class)
public class ActorController {

    private final ActorRepository repository;
    private final MovieRepository movieRepository;

    @Autowired
    public ActorController(ActorRepository repository, MovieRepository movieRepository) {
        this.repository = repository;
        this.movieRepository = movieRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonSerialize(contentAs = FlatActor.class)
    public List<Actor> list() {
        return repository.getAllByOrderByName();
    }

    @GetMapping(value = "search", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Actor> search(@RequestParam(value = "q", required = true) String nameFragment) {
        return repository.getAllByNameContainsOrderByNameAsc(nameFragment);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Actor> post(@RequestBody Actor actor) {
        repository.save(actor);
        return ResponseEntity.created(actor.getHref()).body(actor);
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Actor get(@PathVariable ("id") UUID id) {
        return repository.findById(id).get();
    }

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable ("id") UUID id) {
        repository.delete(get(id));
    }

    @PutMapping(value = "{actorId}/movies/{movieId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Actor attach(@PathVariable("actorId") UUID actorId, @PathVariable("movieId") UUID movieId) {
        Actor actor = get(actorId);
        Movie movie = movieRepository.findById(movieId).get();
        if (!actor.getMovies().contains(movie)) {
            actor.getMovies().add(movie);
        }
        return repository.save(actor);
    }

    @DeleteMapping(value = "{actorId}/movies/{movie_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void detach(@PathVariable ("actorId") UUID actorId, @PathVariable ("movie_id") UUID movieId) {
        Actor actor = get(actorId);
        Movie movie = movieRepository.findById(movieId).get();
        actor.getMovies().remove(movie);
        repository.save(actor);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public void notFound() {}
}
