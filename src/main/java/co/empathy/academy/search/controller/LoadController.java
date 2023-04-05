package co.empathy.academy.search.controller;

import co.empathy.academy.search.service.loader.FileLoaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

@RestController
@RequestMapping("/imdb")
public class LoadController {

    private FileLoaderService fileLoaderService;


    public LoadController(FileLoaderService fileLoaderService) {
        this.fileLoaderService = fileLoaderService;
    }


    @PostMapping("/loadMovies")
    public ResponseEntity loadUsers( @RequestParam MultipartFile basics, @RequestParam MultipartFile ratings,
                                     @RequestParam MultipartFile akas, @RequestParam MultipartFile principals,
                                     @RequestParam MultipartFile crew, @RequestParam MultipartFile episodes){
        fileLoaderService.loadMovies(basics, ratings, akas, principals, crew, episodes);
        return ResponseEntity.accepted().build();
    }
}
