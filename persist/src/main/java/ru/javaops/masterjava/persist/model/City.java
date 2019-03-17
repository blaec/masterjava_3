package ru.javaops.masterjava.persist.model;

import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class City extends BaseEntity {
    private @NonNull String name;
    private @NonNull String code;

    public City(Integer id, String name, String code) {
        this(name, code);
        this.id=id;
    }
}