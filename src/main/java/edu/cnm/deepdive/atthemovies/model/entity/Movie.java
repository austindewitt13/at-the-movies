package edu.cnm.deepdive.atthemovies.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import java.util.UUID;

@Entity()
@Component
@JsonIgnoreProperties(value = {"id", "created", "updated", "href"}, allowGetters = true,
        ignoreUnknown = true)
public class Movie {

    private static EntityLinks entityLinks;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "movie_id", columnDefinition = "CHAR(16) FOR BIT DATA",
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

    @Column(nullable = false)
    private String title;

    private String screenwriter;

    public UUID getId() {
        return id;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getScreenwriter() {
        return screenwriter;
    }

    public void setScreenwriter(String screenwriter) {
        this.screenwriter = screenwriter;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Enumerated(EnumType.STRING)
    private Genre genre;

    public URI getHref() {
        return entityLinks.linkForSingleResource(Movie.class, id).toUri();
    }

    @PostConstruct
    private void init() {
        String ignore = entityLinks.toString();
    }

    @Autowired
    private void setEntityLinks(EntityLinks entityLinks) {
        Movie.entityLinks = entityLinks;
    }

    public enum Genre {
        ACTION, ROM_COM, HORROR, DOCUMENTARY, ANIME, SCI_FI, FANTASY
    }
}
