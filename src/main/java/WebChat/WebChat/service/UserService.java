package WebChat.WebChat.service;


import WebChat.WebChat.enity.User;

import java.util.List;

public interface UserService {

    List<User> searchUsers(String keyword);

    User findByUsername(String username);

}