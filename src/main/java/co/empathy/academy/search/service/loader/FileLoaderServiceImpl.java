package co.empathy.academy.search.service.loader;

import co.empathy.academy.search.models.Akas;
import co.empathy.academy.search.models.Crew;
import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.Principal;
import co.empathy.academy.search.repositories.ElasticClient;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ElasticClient elasticClient;

    private int readAkas = 1;
    private int readRatings = 1;
    private int readCrew = 1;
    private int readPrincipals = 1;
    public FileLoaderServiceImpl() {

    }

    /**
     * Method to create an index and add a mapping to it
     */
    @Override
    public void createIndex() {
        try {
            elasticClient.createIndex();
            elasticClient.settings();
            elasticClient.mapping();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Method to read the files and create a movie object, indexing it
     * @param basics
     * @param ratings
     * @param akas
     * @param principals
     * @param crew
     */
    @Async
    @Override
    public void loadMovies(MultipartFile basics, MultipartFile ratings,
                           MultipartFile akas, MultipartFile principals,
                           MultipartFile crew) {

        List<String> basicsList = Arrays.asList(basics.getOriginalFilename().split("\\s*,\\s*"));
        List<String> ratingsList = Arrays.asList(ratings.getOriginalFilename().split("\\s*,\\s*"));
        List<String> akasList = Arrays.asList(akas.getOriginalFilename().split("\\s*,\\s*"));
        List<String> principalsList = Arrays.asList(principals.getOriginalFilename().split("\\s*,\\s*"));
        List<String> crewList = Arrays.asList(crew.getOriginalFilename().split("\\s*,\\s*"));

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
            System.out.println("creating: " + line);
            Movie u = createMovie(line);

            if(!u.isAdult() || !u.getPrimaryTitle().contains("Episode #")) {
                addAkas(u, akasList);
                addRatings(u, ratingsList);
                addCrew(u, crewList);
                addPrincipals(u, principalsList);

                movies.add(u);
                elasticClient.indexSingleDocument(u);
            }
        });
    }

    /**
     * Adds the Akas to the movie
     * @param u
     * @param akas
     */
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
        ).skip(readAkas).takeWhile(line -> line.split("\t")[0].equals(u.getTconst())).forEach(line -> {
            if(line.split("\t")[0].equals(u.getTconst())) {
                Akas aka = new Akas();
                aka.setTitle(line.split("\t")[2]);
                aka.setRegion(line.split("\t")[3]);
                aka.setLanguage(line.split("\t")[4]);
                aka.setIsOriginalTitle(Boolean.parseBoolean(line.split("\t")[7]));
                u.addAkas(aka);
                readAkas++;

            }
        });
    }

    /**
     * Adds the ratings for the movie
     * @param u
     * @param ratings
     */
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
        ).skip(1).skip(readRatings).takeWhile(line->line.split("\t")[0].equals(u.getTconst())).forEach(line -> {
            if(line.split("\t")[0].equals(u.getTconst())) {
                u.setAverageRating(Double.parseDouble(line.split("\t")[1]));
                u.setNumVotes(Integer.parseInt(line.split("\t")[2]));
                readRatings++;
            }
        });
    }

    /**
     * Adds the actors to the movie
     * @param u
     * @param principals
     */
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
        ).skip(1).skip(readPrincipals).takeWhile(line->line.split("\t")[0].equals(u.getTconst())).forEach(line -> {
            if(line.split("\t")[0].equals(u.getTconst()) && (line.split("\t")[3].equals("actor") || line.split("\t")[3].equals("actress"))){
                    Principal principal = new Principal();
                    principal.setNconst(line.split("\t")[1]);
                    principal.setCharacters(line.split("\t")[5]);
                    u.addPrincipal(principal);
                    readPrincipals++;
            }
        });
    }

    /**
     * Adds the directors to the movie
     * @param u
     * @param crew
     */
    private void addCrew(Movie u, List<String> crew) {
        crew.stream().map(Paths::get).flatMap(
                path -> {
                    try {
                        return Files.lines(path);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return Stream.empty();
                }
        ).skip(1).skip(readCrew).takeWhile(line->line.split("\t")[0].equals(u.getTconst())).forEach(line -> {
            if(line.split("\t")[0].equals(u.getTconst())){
                Crew c = new Crew();
                c.setNconst(line.split("\t")[1]);
                u.addDirector(c);
                readCrew++;
            }
        });
    }

    /**
     * Creates a movie object
     * @param data
     * @return
     */
    protected Movie createMovie(String data) {
        String[] movieData = data.split("\t");
        String tconst = movieData[0];
        String titleType = movieData[1];
        String primaryTitle = movieData[2];
        String originalTitle = movieData[3];
        boolean isAdult = Integer.parseInt(movieData[4])==0 ? false : true;
        int startYear = (movieData[5].equals("\\N") ? -1 : Integer.parseInt(movieData[5]));
        int endYear = (movieData[6].equals("\\N") ? -1 : Integer.parseInt(movieData[6]));
        int runtimeMinutes = (movieData[7].equals("\\N") ? -1 : Integer.parseInt(movieData[7]));
        String genres = movieData[8];
        return new Movie(tconst, titleType, primaryTitle, originalTitle, isAdult, startYear, endYear, runtimeMinutes, genres);

    }


}
