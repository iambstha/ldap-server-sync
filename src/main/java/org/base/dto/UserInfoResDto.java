package org.base.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResDto {

    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;

    public static UserInfoResDtoBuilder builder() {
        return new UserInfoResDtoBuilder();
    }

}
