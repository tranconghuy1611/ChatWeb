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
import java.util.Objects;

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
        if (roomRepo.existsById(roomId)) {
            throw new RuntimeException("Room đã tồn tại");
        }

        Room room = new Room();
        room.setRoomId(roomId);
        room.setRoomName(roomName);

        roomRepo.save(room);

        for (String username : usernames) {
            User user = userRepo.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại: " + username));

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

        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room không tồn tại"));
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

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

    public List<Room> getAllRooms() {
        return roomRepo.findAll();
    }

    public Room updateRoomName(String roomId, String roomName) {
        if (roomName == null || roomName.isBlank()) {
            throw new RuntimeException("Tên nhóm không được để trống");
        }

        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room không tồn tại"));
        room.setRoomName(roomName.trim());
        return roomRepo.save(room);
    }

    public List<User> getRoomMembers(String roomId) {
        if (!roomRepo.existsById(roomId)) {
            throw new RuntimeException("Room không tồn tại");
        }

        return roomMemberRepo.findByRoom_RoomId(roomId)
                .stream()
                .map(RoomMember::getUser)
                .toList();
    }

    public void addMembers(String roomId, List<String> usernames) {
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room không tồn tại"));

        if (usernames == null || usernames.isEmpty()) {
            throw new RuntimeException("Danh sách thành viên cần thêm không được rỗng");
        }

        for (String username : usernames.stream().filter(Objects::nonNull).map(String::trim).toList()) {
            if (username.isBlank()) {
                continue;
            }
            if (roomMemberRepo.existsByRoom_RoomIdAndUser_Username(roomId, username)) {
                continue;
            }

            User user = userRepo.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User không tồn tại: " + username));

            RoomMember rm = new RoomMember();
            rm.setRoom(room);
            rm.setUser(user);
            roomMemberRepo.save(rm);
        }
    }

    public void removeMember(String roomId, String username) {
        if (!roomRepo.existsById(roomId)) {
            throw new RuntimeException("Room không tồn tại");
        }
        if (username == null || username.isBlank()) {
            throw new RuntimeException("Username không hợp lệ");
        }

        long deleted = roomMemberRepo.deleteByRoom_RoomIdAndUser_Username(roomId, username.trim());
        if (deleted == 0) {
            throw new RuntimeException("Thành viên không thuộc nhóm");
        }
    }
}
