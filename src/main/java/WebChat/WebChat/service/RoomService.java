package WebChat.WebChat.service;

import WebChat.WebChat.enity.Room;
import WebChat.WebChat.enity.RoomMember;
import WebChat.WebChat.enity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import WebChat.WebChat.repository.RoomMemberRepository;
import WebChat.WebChat.repository.RoomRepository;
import WebChat.WebChat.repository.UserRepository;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private RoomMemberRepository roomMemberRepo;

    @Autowired
    private UserRepository userRepo;

    // tạo room + add members
    public Room createRoom(String roomId, String roomName, List<String> usernames) {

        Room room = new Room();
        room.setRoomId(roomId);
        room.setRoomName(roomName);

        roomRepo.save(room);

        for (String username : usernames) {
            User user = userRepo.findByUsername(username).get();

            RoomMember rm = new RoomMember();
            rm.setRoom(room);
            rm.setUser(user);

            roomMemberRepo.save(rm);
        }

        return room;
    }

    // join room
    public void joinRoom(String roomId, String username) {

        if (roomMemberRepo.existsByRoom_RoomIdAndUser_Username(roomId, username)) {
            return;
        }

        Room room = roomRepo.findById(roomId).get();
        User user = userRepo.findByUsername(username).get();

        RoomMember rm = new RoomMember();
        rm.setRoom(room);
        rm.setUser(user);

        roomMemberRepo.save(rm);
    }

    // lấy room của user
    public List<Room> getRoomsByUser(String username) {
        return roomMemberRepo.findByUser_Username(username)
                .stream()
                .map(RoomMember::getRoom)
                .toList();
    }
}
