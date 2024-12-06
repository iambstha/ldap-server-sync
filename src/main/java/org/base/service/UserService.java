package org.base.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.base.dto.CredentialsDto;
import org.base.dto.TokenResDto;
import org.base.dto.UserResDto;
import org.base.exception.ResourceNotFoundException;
import org.base.exception.UnauthorizedAccessException;
import org.base.mapper.UserMapper;
import org.base.model.User;
import org.base.repository.UserRepository;
import org.base.security.JwtUtil;
import org.base.security.LdapUtil;

import java.util.Optional;

@Slf4j
@ApplicationScoped
@Transactional
public class UserService {

    @Inject
    UserMapper userMapper;

    @Inject
    UserRepository userRepository;

    @Inject
    LdapUtil ldapUtil;

    @Inject
    JwtUtil jwtUtil;

    public UserResDto save(CredentialsDto credentialsDto){

        if(ldapUtil.authenticateUser(credentialsDto.getUsername(), credentialsDto.getPassword())){
            User user = ldapUtil.getUserInformation(credentialsDto.getUsername());
            Optional<User> existingUser = userRepository.findByUsernameOptional(credentialsDto.getUsername());
            if(existingUser.isEmpty()){
                user.setCreatedBy(credentialsDto.getUsername());
                userRepository.persist(user);
                userRepository.getEntityManager().flush();
            }
            return userMapper.toResDto(user);
        }else{
            throw new UnauthorizedAccessException("Unauthorized user");
        }

    }

    public TokenResDto login(CredentialsDto credentialsDto){

        if(ldapUtil.authenticateUser(credentialsDto.getUsername(), credentialsDto.getPassword())){
            User user = ldapUtil.getUserInformation(credentialsDto.getUsername());
            Optional<User> existingUser = userRepository.findByUsernameOptional(credentialsDto.getUsername());
            if(existingUser.isEmpty()){
                user.setCreatedBy(credentialsDto.getUsername());
                userRepository.persist(user);
                userRepository.getEntityManager().flush();
            }else{
                user = existingUser.get();
            }

            TokenResDto tokenResDto = new TokenResDto();
            tokenResDto.setToken(jwtUtil.generateToken(user.getId()));

            return tokenResDto;
        }else{
            throw new UnauthorizedAccessException("Unauthorized user");
        }

    }

}
