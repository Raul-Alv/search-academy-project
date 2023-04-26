package co.empathy.academy.search.controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.empathy.academy.search.models.Facet;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(description = "Search exact term on specific category", responses = {
            @ApiResponse(responseCode = "202", description = "Movie title found"),
            @ApiResponse(responseCode = "404", description = "Movie title not found")
    })
    @GetMapping(value="/term", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> termSearch(@Parameter(description = "What text to search for", required = true) @RequestParam(name = "query") String query,
                                                  @Parameter(description = "What field to search the term in", required = true) @RequestParam("field") String field) throws IOException {
        return ResponseEntity.ok(service.termSearch(query, field));
    }

    @Operation(description = "Returns movies that contain the term searched and filters for another category", responses = {
            @ApiResponse(responseCode = "202", description = "Movies containing the title and filtered by a category"),
            @ApiResponse(responseCode = "404", description = "No movies matching the title and filter")
    })
    @GetMapping(value="/termAndFilter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> termAndFilterSearch(@Parameter(description = "Movie title to search") @RequestParam(name = "query") String query,
                                                           @Parameter(description = "Name of the field to search (originalTitle)")@RequestParam("field") String field,
                                                           @Parameter(description = "Category to filter by") @RequestParam("filterField") String filterField,
                                                           @Parameter(description = "Value for the category") @RequestParam("filterValue") String filterValue) throws IOException {
        return ResponseEntity.ok(service.termAndFilterSearch(query, field, filterField, filterValue));
    }

    @Operation(description = "Get the results for the reccomendation quiz", responses = {
            @ApiResponse(responseCode = "202", description = "Movies matching the quiz"),
            @ApiResponse(responseCode = "404", description = "No movies matching the quiz")
    })
    @GetMapping(value="/quizResult", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> quizResult(@Parameter(description = "What genre to search for") @RequestParam(name = "generoQuery") String generoQuery,
                                                  @Parameter(description = "Lowest duration for the movies") @RequestParam(name = "minDurationQuery") String minDurationQuery,
                                                  @RequestParam(name = "Highest duration for the movies") String maxDurationQuery,
                                                  @RequestParam(name = "Minimal rating for the movies") String ratingQuery) throws IOException {
        return ResponseEntity.ok(service.quizResult(generoQuery, minDurationQuery, maxDurationQuery, ratingQuery));
    }

    @Operation(description = "Get the latest movies", responses = {
            @ApiResponse(responseCode = "202", description = "Movies are found"),
            @ApiResponse(responseCode = "404", description = "No movies are found")
    })
    @GetMapping(value="/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Movie>> latest() throws IOException {
        return ResponseEntity.ok(service.latest());
    }

    @Operation(description = "Get the genres", responses = {
            @ApiResponse(responseCode = "202", description = "Genres are found"),
            @ApiResponse(responseCode = "404", description = "No genres are found")
    })
    @GetMapping(value="/genres", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Facet> generes() throws IOException {
        return ResponseEntity.ok(service.genres());
    }
}
