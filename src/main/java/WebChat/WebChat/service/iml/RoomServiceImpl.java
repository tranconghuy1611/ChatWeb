package WebChat.WebChat.service.iml;


import WebChat.WebChat.enity.Room;
import WebChat.WebChat.enity.RoomMember;
import WebChat.WebChat.enity.User;
import WebChat.WebChat.repository.RoomMemberRepository;
import WebChat.WebChat.repository.RoomRepository;
import WebChat.WebChat.repository.UserRepository;
import WebChat.WebChat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final UserRepository userRepository;

    @Override
    public Room createRoom(String roomName, String creatorUsername, List<String> members) {

        User creator = userRepository.findByUsername(creatorUsername)
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        // tạo room
        Room room = Room.builder()
                .roomId(UUID.randomUUID().toString())
                .roomName(roomName)
                .createdBy(creator)
                .build();

        roomRepository.save(room);

        // thêm creator vào room
        RoomMember creatorMember = RoomMember.builder()
                .room(room)
                .user(creator)
                .build();
        roomMemberRepository.save(creatorMember);

        // thêm members
        for (String username : members) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));

            RoomMember member = RoomMember.builder()
                    .room(room)
                    .user(user)
                    .build();

            roomMemberRepository.save(member);
        }

        return room;
    }

    @Override
    public List<Room> getRoomsByUser(String username) {
        return roomRepository.findByCreatedBy_Username(username);
    }
}