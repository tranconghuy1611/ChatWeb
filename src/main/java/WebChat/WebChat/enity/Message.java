package WebChat.WebChat.enity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender", referencedColumnName = "username")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver", referencedColumnName = "username")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    private String content;
    private String messageType;

    private LocalDateTime createdAt;
}