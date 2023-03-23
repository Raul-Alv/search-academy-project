package co.empathy.academy.search.service;

import co.empathy.academy.search.models.User;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserService {

    public User createUser(User user);
    public void deleteUser(User user);
    public User updateUser(String id, User newUser);
    public List<User> listUsers();
    public User getUserById(String id);
    public void loadUsers(MultipartFile file);

}
