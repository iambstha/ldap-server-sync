package org.base.mapper;

import org.base.dto.UserReqDto;
import org.base.dto.UserResDto;
import org.base.model.User;
import org.mapstruct.Mapper;

@Mapper(config = QuarkusMappingConfig.class)
public interface UserMapper {

    UserResDto toResDto(User user);

    User toEntity(UserReqDto userReqDto);

    User toEntityFromResDto(UserResDto userResDto);

}
