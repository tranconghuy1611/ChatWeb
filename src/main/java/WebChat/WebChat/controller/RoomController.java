package WebChat.WebChat.controller;

import WebChat.WebChat.dto.request.PrivateRoomRequest;
import WebChat.WebChat.dto.request.RoomRequest;
import WebChat.WebChat.dto.request.RoomMembersRequest;
import WebChat.WebChat.dto.request.RoomUpdateRequest;
import WebChat.WebChat.dto.response.RoomResponse;
import WebChat.WebChat.dto.response.UserResponse;
import WebChat.WebChat.enity.Room;
import WebChat.WebChat.enity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import WebChat.WebChat.service.RoomService;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Locale;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/private")
    public RoomResponse createPrivate(@RequestBody PrivateRoomRequest req, Principal principal) {
        String other = req.getTargetUsername();
        Room room = roomService.createPrivateRoom(principal.getName(), other);
        return new RoomResponse(room.getRoomId(), room.getRoomName());
    }

    // =====================
// TẠO NHÓM NHIỀU NGƯỜI
// =====================
    @PostMapping("/group")
    public RoomResponse createGroup(@RequestBody RoomRequest req) {
        Room room = roomService.createGroupRoom(req.getRoomName(), req.getUsers());
        return new RoomResponse(room.getRoomId(), room.getRoomName());
    }

    @GetMapping("/{username}")
    public List<Room> getByUser(@PathVariable String username, Principal principal, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin && !principal.getName().equals(username)) {
            throw new RuntimeException("Không có quyền truy cập room của user khác");
        }
        return roomService.getRoomsByUser(username);
    }

    @GetMapping("/me")
    public List<Room> myRooms(Principal principal) {
        return roomService.getRoomsByUser(principal.getName());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms().stream()
                .map(room -> new RoomResponse(
                        room.getRoomId(),
                        room.getRoomName()
                ))
                .toList();
    }

    @PutMapping("/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoomResponse updateRoom(@PathVariable String roomId, @RequestBody RoomUpdateRequest req) {
        Room room = roomService.updateRoomName(roomId, req.getRoomName());
        return new RoomResponse(room.getRoomId(), room.getRoomName());
    }

    @GetMapping("/{roomId}/members")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getMembers(@PathVariable String roomId) {
        List<User> users = roomService.getRoomMembers(roomId);
        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getFullname(),
                        user.getRole()
                ))
                .toList();
    }

    @PostMapping("/{roomId}/members")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> addMembers(@PathVariable String roomId, @RequestBody RoomMembersRequest req) {
        roomService.addMembers(roomId, req.getUsernames());
        return getMembers(roomId);
    }

    @DeleteMapping("/{roomId}/members/{username}")
    public List<UserResponse> removeMember(
            @PathVariable String roomId,
            @PathVariable String username,
            Authentication authentication
    ) {
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(authority -> authority != null && !authority.isBlank())
                .map(authority -> authority.toUpperCase(Locale.ROOT))
                .anyMatch(authority -> authority.equals("ADMIN") || authority.equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Bạn không có quyền xóa thành viên");
        }

        roomService.removeMember(roomId, username);
        return getMembers(roomId);
    }
}
