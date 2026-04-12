package WebChat.WebChat.enity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
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

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}