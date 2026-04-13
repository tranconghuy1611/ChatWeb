package WebChat.WebChat.controller;



import WebChat.WebChat.dto.request.CreateRoomRequest;
import WebChat.WebChat.enity.Room;
import WebChat.WebChat.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    // tạo group
    @PostMapping
    public Room createRoom(@RequestBody CreateRoomRequest req) {
        return roomService.createRoom(
                req.getRoomName(),
                req.getCreator(),
                req.getMembers()
        );
    }

    // lấy room theo user
    @GetMapping
    public List<Room> getRooms(@RequestParam String username) {
        return roomService.getRoomsByUser(username);
    }
}
