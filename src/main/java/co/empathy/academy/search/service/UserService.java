package co.empathy.academy.search.service;

import co.empathy.academy.search.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {

    private UserService(){

    }
    ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public User createUser(User user){
        users.put(user.getId(), user);
        return users.get(user.getId());
    }

    public void deleteUser(User user){
        Iterator<Map.Entry<String, User>> iterator = users.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, User> entry = iterator.next();
            if(entry.getKey().equals(user.getId())){
                iterator.remove();
            }
        }
    }

    public User updateUser(String id, User newUser){
        users.computeIfPresent(id, (key, oldValue) -> oldValue = newUser);
        return users.get(id);
    }

    public List<User> listUsers() {
        List<User> allUsers = new ArrayList<>();
        for(Map.Entry<String, User> entry : users.entrySet()){
            allUsers.add(entry.getValue());
        }
        return allUsers;
    }

    public User getUserById(String id){
        User u = null;
        Iterator<Map.Entry<String, User>> iterator = users.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<String, User> entry = iterator.next();
            if(entry.getKey().equals(id)){
                u = entry.getValue();
            }
        }
        return u;
    }
}
