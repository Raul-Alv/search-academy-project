package co.empathy.academy.search.service.loader;

import co.empathy.academy.search.models.Akas;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.Principal;
import co.empathy.academy.search.repositories.ElasticClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
    @Autowired
    private ElasticClient elasticClient;
    public FileLoaderServiceImpl() {

    }

    @Override
    public void createIndex(InputStream file) {
        elasticClient.createIndex(file);
    }

    @Async
    @Override
    public void loadMovies(MultipartFile basics, MultipartFile ratings,
                           MultipartFile akas, MultipartFile principals,
                           MultipartFile crew, MultipartFile episodes) {
        //To read only one file
        List<String> basicsList = Arrays.asList(basics.getOriginalFilename().split("\\s*,\\s*"));
        List<String> ratingsList = Arrays.asList(ratings.getOriginalFilename().split("\\s*,\\s*"));
        List<String> akasList = Arrays.asList(akas.getOriginalFilename().split("\\s*,\\s*"));
        List<String> principalsList = Arrays.asList(principals.getOriginalFilename().split("\\s*,\\s*"));
        List<String> crewList = Arrays.asList(crew.getOriginalFilename().split("\\s*,\\s*"));
        List<String> episodesList = Arrays.asList(episodes.getOriginalFilename().split("\\s*,\\s*"));

        /*
        //To read multiple files
        List<String> files = Arrays.asList(
              basics.getOriginalFilename().split("\\s*,\\s*")[0],
                ratings.getOriginalFilename().split("\\s*,\\s*")[0],
                akas.getOriginalFilename().split("\\s*,\\s*")[0],
                principals.getOriginalFilename().split("\\s*,\\s*")[0],
                crew.getOriginalFilename().split("\\s*,\\s*")[0],
                episodes.getOriginalFilename().split("\\s*,\\s*")[0]
                );

         */
        /*
        try {
            elasticClient.createIndex(map.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        */
        try {
            elasticClient.createIndex();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //readBasics(basicsList);
        basicsList.stream().map(Paths::get).flatMap(
                path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Stream.empty();
                }
        ).skip(1).forEach(line -> {
            System.out.println("creando: " + line);
            Movie u = createMovie(line);
            addAkas(u, akasList);
            addRatings(u, ratingsList);
            addPrincipals(u, principalsList);
            addCrew(u, crewList);
            addEpisodes(u, episodesList);
            if(!u.isAdult()) {
                movies.add(u);
            }
                    //movies.add(u);
                    //System.out.println("creada: " + u.getPrimaryTitle());
                }
        );
        elasticClient.indexDocument(movies);
    }

    @Async
    private void addAkas(Movie u, List<String> akas) {
        akas.stream().map(Paths::get).flatMap(
                path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Stream.empty();
                }
        ).skip(1).forEach(line -> {
            if(line.split("\t")[0].equals(u.getTconst())){
                Akas aka = new Akas();
                //aka.setOrdering(Integer.parseInt(line.split("\t")[1]));
                aka.setTitle(line.split("\t")[2]);
                aka.setRegion(line.split("\t")[3]);
                aka.setLanguage(line.split("\t")[4]);
                //aka.setTypes(line.split("\t")[5]);
                //aka.setAttributes(line.split("\t")[6]);
                aka.setIsOriginalTitle(Boolean.parseBoolean(line.split("\t")[7]));
                u.addAkas(aka);
            }

        }
        );
    }

    private void addRatings(Movie u, List<String> ratings) {
        ratings.stream().map(Paths::get).flatMap(
                path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Stream.empty();
                }
        ).skip(1).forEach(line -> {
                    if(line.split("\t")[0].equals(u.getTconst())){
                        u.setAverageRating(Double.parseDouble(line.split("\t")[1]));
                        u.setNumVotes(Integer.parseInt(line.split("\t")[2]));
                    }

                }
        );
    }

    private void addPrincipals(Movie u, List<String> principals) {
        principals.stream().map(Paths::get).flatMap(
                path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Stream.empty();
                }
        ).skip(1).forEach(line -> {
                    if(line.split("\t")[0].equals(u.getTconst())){
                        if(line.split("\t")[3].equals("director")){
                            Principal principal = new Principal();
                            principal.setNconst(line.split("\t")[1]);
                            u.addPrincipal(principal);
                        }
                    }
                }
        );
    }

    private void addCrew(Movie u, List<String> crew) {
    }

    private void addEpisodes(Movie u, List<String> episodes) {
    }



    protected Movie createMovie(String data) {
        String[] movieData = data.split("\t");
        String tconst = movieData[0];
        String titleType = movieData[1];
        String primaryTitle = movieData[2];
        String originalTitle = movieData[3];
        boolean isAdult = Boolean.parseBoolean(movieData[4]);
        int startYear = (movieData[5].equals("\\N") ? -1 : Integer.parseInt(movieData[5]));
        int endYear = (movieData[6].equals("\\N") ? -1 : Integer.parseInt(movieData[6]));
        int runtimeMinutes = (movieData[7].equals("\\N") ? -1 : Integer.parseInt(movieData[7]));
        String genres = movieData[8];
        return new Movie(tconst, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, genres);

    }


}
