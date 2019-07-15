package edu.cnm.deepdive.atthemovies.view;

import edu.cnm.deepdive.atthemovies.model.entity.Movie;
import edu.cnm.deepdive.atthemovies.model.entity.Genre;
import java.net.URI;
import java.util.Date;
import java.util.UUID;

public interface FlatMovie {

    UUID getId();

    Date getCreated();

    Date getUpdated();

    String getTitle();

    String getScreenwriter();

    Genre getGenre();

    URI getHref();

}
