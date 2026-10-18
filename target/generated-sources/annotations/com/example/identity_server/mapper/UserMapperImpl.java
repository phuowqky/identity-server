package com.example.identity_server.mapper;

import com.example.identity_server.dto.request.UserRequestDTO;
import com.example.identity_server.dto.response.UserResponseDTO;
import com.example.identity_server.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-10-18T16:36:20+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.9 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponseDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId( user.getId() );
        userResponseDTO.setUsername( user.getUsername() );
        userResponseDTO.setEmail( user.getEmail() );
        userResponseDTO.setFullName( user.getFullName() );
        userResponseDTO.setAge( user.getAge() );
        userResponseDTO.setEnabled( user.getEnabled() );
        userResponseDTO.setDonViId( user.getDonViId() );
        userResponseDTO.setCreatedAt( user.getCreatedAt() );
        userResponseDTO.setUpdatedAt( user.getUpdatedAt() );

        return userResponseDTO;
    }

    @Override
    public User toEntity(UserRequestDTO userRequestDTO) {
        if ( userRequestDTO == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( userRequestDTO.getUsername() );
        user.setEmail( userRequestDTO.getEmail() );
        user.setPassword( userRequestDTO.getPassword() );
        user.setFullName( userRequestDTO.getFullName() );
        user.setAge( userRequestDTO.getAge() );
        user.setDonViId( userRequestDTO.getDonViId() );

        return user;
    }

    @Override
    public void updateEntityFromDTO(UserRequestDTO userRequestDTO, User entity) {
        if ( userRequestDTO == null ) {
            return;
        }

        if ( userRequestDTO.getUsername() != null ) {
            entity.setUsername( userRequestDTO.getUsername() );
        }
        if ( userRequestDTO.getEmail() != null ) {
            entity.setEmail( userRequestDTO.getEmail() );
        }
        if ( userRequestDTO.getPassword() != null ) {
            entity.setPassword( userRequestDTO.getPassword() );
        }
        if ( userRequestDTO.getFullName() != null ) {
            entity.setFullName( userRequestDTO.getFullName() );
        }
        if ( userRequestDTO.getAge() != null ) {
            entity.setAge( userRequestDTO.getAge() );
        }
        if ( userRequestDTO.getDonViId() != null ) {
            entity.setDonViId( userRequestDTO.getDonViId() );
        }
    }

    @Override
    public UserResponseDTO toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId( user.getId() );
        userResponseDTO.setUsername( user.getUsername() );
        userResponseDTO.setEmail( user.getEmail() );
        userResponseDTO.setFullName( user.getFullName() );
        userResponseDTO.setAge( user.getAge() );
        userResponseDTO.setEnabled( user.getEnabled() );
        userResponseDTO.setDonViId( user.getDonViId() );
        userResponseDTO.setCreatedAt( user.getCreatedAt() );
        userResponseDTO.setUpdatedAt( user.getUpdatedAt() );

        return userResponseDTO;
    }
}
