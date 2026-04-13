package WebChat.WebChat.enity;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "rooms")
@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    private String roomId;

    private String roomName;

    @ManyToOne
    @JoinColumn(name = "created_by", referencedColumnName = "username")
    private User createdBy;

    private LocalDateTime createdAt;
}