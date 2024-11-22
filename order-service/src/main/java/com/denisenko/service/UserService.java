package com.denisenko.service;

import com.denisenko.dto.UserDto;
import com.denisenko.event.UserCreatedEvent;
import com.denisenko.model.User;
import com.denisenko.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    @Value("${spring.kafka.topics.user-creation}")
    private String userCreatedTopic;

    public UserDto createUser(UserDto userDto) {
        User user = mapToEntity(userDto);
        userRepository.save(user);
        kafkaTemplate.send(userCreatedTopic, new UserCreatedEvent(user.getId(), user.getUsername()));
        return mapToDto(user);
    }

    private User mapToEntity(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .username(userDto.getUsername())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .build();
    }

    private UserDto mapToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
}
