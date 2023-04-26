package co.empathy.academy.search.controller;

import co.empathy.academy.search.service.loader.FileLoaderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/imdb")
public class LoadController {

    private FileLoaderService fileLoaderService;


    public LoadController(FileLoaderService fileLoaderService) {
        this.fileLoaderService = fileLoaderService;
    }


    @Operation(description = "Load the movies from the csv files", responses = {
            @ApiResponse(responseCode = "202", description = "Movies loaded"),
            @ApiResponse(responseCode = "404", description = "Movies not loaded")
    })
    @PostMapping("/loadMovies")
    public ResponseEntity loadUsers( @RequestParam MultipartFile basics, @RequestParam MultipartFile ratings,
                                     @RequestParam MultipartFile akas, @RequestParam MultipartFile principals,
                                     @RequestParam MultipartFile crew) {
        fileLoaderService.createIndex();
        fileLoaderService.loadMovies(basics, ratings, akas, principals, crew);
        return ResponseEntity.accepted().build();
    }
}
