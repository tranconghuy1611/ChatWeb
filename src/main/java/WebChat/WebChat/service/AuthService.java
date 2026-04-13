package WebChat.WebChat.service;


import WebChat.WebChat.dto.response.AuthResponse;
import WebChat.WebChat.enity.User;

public interface AuthService {

    AuthResponse login(String username, String password);

    User register(String username, String password, String fullname);

}