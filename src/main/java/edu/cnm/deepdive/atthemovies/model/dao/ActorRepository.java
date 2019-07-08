package edu.cnm.deepdive.atthemovies.model.dao;

import edu.cnm.deepdive.atthemovies.model.entity.Actor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface ActorRepository extends CrudRepository<Actor, UUID> {

    List<Actor> getAllByOrderByName();

    List<Actor> getAllByNameContainsOrderByNameAsc(String nameFragment);


}
