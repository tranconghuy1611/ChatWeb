package WebChat.WebChat.controller;

import WebChat.WebChat.dto.request.RoomRequest;
import WebChat.WebChat.dto.response.RoomResponse;
import WebChat.WebChat.enity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import WebChat.WebChat.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping
    public RoomResponse create(@RequestBody RoomRequest req) {

        Room room = roomService.createRoom(
                req.getRoomId(),
                req.getRoomName(),
                req.getUsers()
        );

        return new RoomResponse(
                room.getRoomId(),
                room.getRoomName()
        );
    }

    @GetMapping("/{username}")
    public List<Room> getByUser(@PathVariable String username) {
        return roomService.getRoomsByUser(username);
    }
}
