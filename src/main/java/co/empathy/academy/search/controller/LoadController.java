package co.empathy.academy.search.controller;

import co.empathy.academy.search.service.loader.FileLoaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
public class LoadController {

    private FileLoaderService fileLoaderService;


    public LoadController(FileLoaderService fileLoaderService) {
        this.fileLoaderService = fileLoaderService;
    }

    @RequestMapping("/load")
    @ResponseBody
    public ResponseEntity loadUsers(@RequestParam MultipartFile file){
        fileLoaderService.loadMovies(file);
        return ResponseEntity.accepted().build();
    }
}
