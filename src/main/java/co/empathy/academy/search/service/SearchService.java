package co.empathy.academy.search.service;

import co.elastic.clients.elasticsearch._types.aggregations.BucketAggregationBase;
import co.elastic.clients.json.JsonData;
import co.empathy.academy.search.models.Facet;
import co.empathy.academy.search.models.Movie;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface SearchService {

    String search(String query) throws IOException;
    List<String> searchGenre(String query) throws IOException;

    List<Movie> termSearch(String query, String field) throws IOException;

    List<Movie> termAndFilterSearch(String query, String field, String filterField, String filterValue) throws IOException;

    List<Movie> quizResult(String genreQuery, String minDurationQuery, String maxDurationQuery, String ratingQuery) throws IOException;

    List<Movie> latest() throws IOException;

    Facet genres() throws IOException;

    List<Movie> allFilterSearch(Optional<String> genres, Optional<String> types,
                                Optional<Integer> maxYear, Optional<Integer> minYear,
                                Optional<Integer> maxRuntime, Optional<Integer> minRuntime,
                                Optional<Double> maxScore, Optional<Double> minScore,
                                Optional<Integer> maxNHits, Optional<String> sortRating) throws IOException;
}
