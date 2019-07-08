package edu.cnm.deepdive.atthemovies.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.cnm.deepdive.atthemovies.view.FlatActor;
import edu.cnm.deepdive.atthemovies.view.FlatMovie;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.net.URI;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

//TODO Add annotations, fields, and methods to make an Entity class.

@Entity
@Component
@JsonIgnoreProperties(value = {"id","created", "updated", "href", "movies"}, allowGetters = true,
        ignoreUnknown = true)
public class Actor implements FlatActor {

    private static EntityLinks entityLinks;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "actor_id", columnDefinition = "CHAR(16) FOR BIT DATA",
            nullable = false, updatable = false)
    private UUID id;

    @NonNull
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date created;

    @NonNull
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date updated;

    @NonNull
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JoinTable(joinColumns = @JoinColumn(name = "actor_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    @OrderBy("title asc")
    @JsonSerialize(contentAs = FlatMovie.class)
    private List<Movie> movies = new LinkedList<>();

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public Date getCreated() {
        return created;
    }

    @Override
    public Date getUpdated() {
        return updated;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    @Override
    public URI getHref() {
        return entityLinks.linkForSingleResource(Actor.class, id).toUri();
    }

    @PostConstruct
    private void init() {
        String ignore = entityLinks.toString();
    }

    @Autowired
    private void setEntityLinks(EntityLinks entityLinks) {
        Actor.entityLinks = entityLinks;
    }


}