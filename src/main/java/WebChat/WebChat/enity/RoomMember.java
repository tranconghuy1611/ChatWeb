package WebChat.WebChat.enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "room_members")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
}