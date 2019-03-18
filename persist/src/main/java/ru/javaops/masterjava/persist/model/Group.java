package ru.javaops.masterjava.persist.model;

import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Group extends BaseEntity {
    @Column("project_id")
    private @NonNull Integer projectId;
    private @NonNull String name;
    private @NonNull GroupFlag flag;

    public Group(Integer id, Integer projectId, String name, GroupFlag flag) {
        this(projectId, name, flag);
        this.id=id;
    }
}