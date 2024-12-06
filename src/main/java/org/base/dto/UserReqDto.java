package org.base.dto;

import lombok.Data;

@Data
public class UserReqDto {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String displayName;
    private String department;
    private String mobile;

}
