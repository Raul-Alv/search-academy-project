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

    }

}
