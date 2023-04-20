package co.empathy.academy.search.models;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String tconst;
    private String titleType;
    private String primaryTitle;
    private String originalTitle;
    private boolean isAdult;
    private int startYear;
    private int endYear;
    private int runtimeMinutes;
    private String genres;
    private double averageRating;
    private int numVotes;
    private List<Akas> akas = new ArrayList<>();
    private List<Principal> cast = new ArrayList<>();
    private Crew director;


    public Movie(String tconst, String titleType, String primaryTitle, String originalTitle, boolean isAdult, int startYear, int endYear, int runtimeMinutes, String genres) {
        this.tconst = tconst;
        this.titleType = titleType;
        this.primaryTitle = primaryTitle;
        this.originalTitle = originalTitle;
        this.isAdult = isAdult;
        this.startYear = startYear;
        this.endYear = endYear;
        this.runtimeMinutes = runtimeMinutes;
        this.genres = genres;

    }

    public String getTconst() {
        return tconst;
    }

    public String getTitleType() {
        return titleType;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public int getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public String getGenres() {
        return genres;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setAdult(boolean adult) {
        isAdult = adult;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public void setRuntimeMinutes(int runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void addAkas(Akas aka) {
        akas.add(aka);
    }

    public void setAverageRating(double parseDouble) {
        this.averageRating = parseDouble;
    }

    public void setNumVotes(int parseInt) {
        this.numVotes = parseInt;
    }

    public void addPrincipal(Principal principal) {
        this.cast.add(principal);
    }

    public List<Principal> getCast(){
        return cast;
    }

    public void addDirector(Crew c) {
        this.director = c;
    }

    public Crew getDirector(){
        return director;
    }

    public List<Akas> getAkas(){
        return akas;
    }
}
