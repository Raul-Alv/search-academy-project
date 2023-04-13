package co.empathy.academy.search.service;

import co.empathy.academy.search.models.Movie;
import co.empathy.academy.search.models.User;
import org.mockito.Mock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@EnableAsync
@Service
public class UserServiceImpl implements UserService {

    private UserServiceImpl() {

    }

    ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    @Override
    public User createUser(User user) {
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    @Override
    public void deleteUser(User user) {
        Iterator<Map.Entry<String, User>> iterator = users.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, User> entry = iterator.next();
            if (entry.getKey().equals(user.getId())) {
                iterator.remove();
            }
        }
    }

    @Override
    public User updateUser(String id, User newUser) {
        users.computeIfPresent(id, (key, oldValue) -> oldValue = newUser);
        return users.get(id);
    }

    @Override
    public List<User> listUsers() {
        List<User> allUsers = new ArrayList<>();
        for (Map.Entry<String, User> entry : users.entrySet()) {
            allUsers.add(entry.getValue());
        }
        return allUsers;
    }

    @Override
    public User getUserById(String id) {
        User u = null;
        Iterator<Map.Entry<String, User>> iterator = users.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, User> entry = iterator.next();
            if (entry.getKey().equals(id)) {
                u = entry.getValue();
            }
        }
        return u;
    }

    @Async
    @Override
    public void loadUsers(MultipartFile file) {
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
                    //String[] movieData = line.split("\n");
                    //System.out.println("pruebas:" + movieData[0] + " fin");
                    Movie u = createMovie(line);
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