package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Message extends BaseEntity {
    @Column("send_to")
    private @NonNull String to;
    @Column("send_cc")
    private String cc;
    private @NonNull String subject;
    private @NonNull String body;
    private @NonNull LocalDateTime sent;

    public Message(Integer id, String to, String cc, String subject, String body, LocalDateTime sent) {
        this(to, cc, subject, body, sent);
        this.id=id;
    }
}