package co.empathy.academy.search.service;

import co.empathy.academy.search.models.Movie;

import java.io.IOException;
import java.util.List;

public interface SearchService {

    String search(String query) throws IOException;
    List<String> searchGenre(String query) throws IOException;

    List<Movie> termSearch(String query, String field) throws IOException;

    List<Movie> multiTermSearch(String query, String field);
}
