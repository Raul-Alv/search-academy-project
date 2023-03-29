package co.empathy.academy.search.service.loader;

import co.empathy.academy.search.models.Movie;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@EnableAsync
@Service
public class FileLoaderServiceImpl implements FileLoaderService{

    private LinkedList<Movie> movies = new LinkedList<>();

    public FileLoaderServiceImpl() {
    }

    @Async
    @Override
    public void loadMovies(MultipartFile file) {
        //To read only one file
        List<String> files = Arrays.asList(file.getOriginalFilename().split("\\s*,\\s*"));

        //To read multiple files
        //List<String> files = Arrays.asList("/test/test.txt", "/test2/test2.txt");

        files.stream().map(Paths::get).flatMap(
                path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Stream.empty();
                }
        ).skip(1).forEach(line -> {
                    Movie u = createMovie(line);
                    movies.add(u);
                }
        );
    }

    protected Movie createMovie(String data) {
        String[] movieData = data.split("\t");
        String tconst = movieData[0];
        String titleType = movieData[1];
        String primaryTitle = movieData[2];
        String originalTitle = movieData[3];
        boolean isAdult = Boolean.parseBoolean(movieData[4]);
        int startYear = Integer.parseInt(movieData[5]);
        int endYear = (movieData[6].equals("\\N") ? -1 : Integer.parseInt(movieData[6]));
        int runtimeMinutes = (movieData[7].equals("\\N") ? -1 : Integer.parseInt(movieData[7]));
        String genres = movieData[8];

        return new Movie(tconst, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, genres);

    }
}
