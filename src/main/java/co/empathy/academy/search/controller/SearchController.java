package co.empathy.academy.search.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.sql.QueryResponse;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import java.util.List;

import java.io.IOException;


@RestController
@RequestMapping(value="/search")
public class SearchController {

    public SearchService service;

    @Autowired
    public SearchController(SearchService service){
        this.service = service;
    }

    @GetMapping(value="/genre", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SearchResponse> genereSearch(@RequestParam(name = "query", required = false, defaultValue="unknown") String query) throws IOException {
        return null;
    }

    @GetMapping(value="/term", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> termSearch(@RequestParam(name = "query") String query, @RequestParam("field") String field) throws IOException {
        return ResponseEntity.ok(service.termSearch(query, field));
    }

    @GetMapping(value="/terms", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> multiTermSearch(@RequestParam(name = "query") String query, @RequestParam("field") String field) throws IOException {
        return ResponseEntity.ok(service.multiTermSearch(query, field));
    }

    @GetMapping(value="/termAndFilter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> termAndFilterSearch(@RequestParam(name = "query") String query, @RequestParam("field") String field, @RequestParam("filterField") String filterField, @RequestParam("filterValue") String filterValue) throws IOException {
        return ResponseEntity.ok(service.termAndFilterSearch(query, field, filterField, filterValue));
    }

    @GetMapping(value="/quizResult", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> quizResult(@RequestParam(name = "generoQuery") String generoQuery,
                                                  @RequestParam(name = "duracionMinQuery") String duracionMinQuery,
                                                  @RequestParam(name = "duracionMaxQuery") String duracionMaxQuery,
                                                  @RequestParam(name = "ratingQuery") String ratingQuery) throws IOException {
        return ResponseEntity.ok(service.quizResult(generoQuery, duracionMinQuery, duracionMaxQuery, ratingQuery));
    }
}
