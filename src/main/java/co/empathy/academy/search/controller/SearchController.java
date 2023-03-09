package co.empathy.academy.search.controller;

import co.elastic.clients.elasticsearch.sql.QueryResponse;
import co.empathy.academy.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.awt.*;
import java.io.IOException;


@RestController
public class SearchController {

    public SearchService service;

    @Autowired
    public SearchController(SearchService service){
        this.service = service;
    }

    @RequestMapping(value="/search")
    @ResponseBody
    public ResponseEntity<String> search(@RequestParam(name = "query", required = false, defaultValue="unknown") String query) throws IOException {
        return ResponseEntity.ok(service.search(query));
    }
}
