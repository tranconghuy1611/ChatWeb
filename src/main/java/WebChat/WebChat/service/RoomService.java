package WebChat.WebChat.service;



import WebChat.WebChat.enity.Room;

import java.util.List;

public interface RoomService {

    Room createRoom(String roomName, String creator, List<String> members);

    List<Room> getRoomsByUser(String username);

}