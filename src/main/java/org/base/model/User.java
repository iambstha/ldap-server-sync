package org.base.model;

import jakarta.persistence.*;
import lombok.*;
import org.base.domain.BaseEntity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_table")
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String firstName;

    private String lastName;

    private String displayName;

    private String department;

    private String mobile;

    public static UserBuilder builder() {
        return new UserBuilder();
    }

}