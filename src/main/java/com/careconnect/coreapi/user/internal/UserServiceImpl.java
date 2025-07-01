package com.careconnect.coreapi.user.internal;

import com.careconnect.coreapi.user.UserInfo;
import com.careconnect.coreapi.user.UserService;
import com.careconnect.coreapi.user.api.RegisterUserCommand;
import com.careconnect.coreapi.user.api.UpdateUserCommand;
import com.careconnect.coreapi.user.api.UserManagement;
import com.careconnect.coreapi.user.domain.User;
import com.careconnect.coreapi.user.events.UserRegisteredEvent;
import com.careconnect.coreapi.user.events.UserUpdatedEvent;
import com.careconnect.coreapi.user.events.UserDeletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Internal implementation of User module services.
 * 
 * Architecture Principles:
 * - Implements both internal and public APIs
 * - Publishes domain events for inter-module communication
 * - Handles transactional boundaries
 * - Encapsulates business logic
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserManagement {
    
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    @Override
    public Optional<UserInfo> findUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(UserInfo::fromUser);
    }
    
    @Override
    public Optional<UserInfo> findUserByClerkId(String clerkUserId) {
        return userRepository.findByClerkUserId(clerkUserId)
                .map(UserInfo::fromUser);
    }
    
    @Override
    public Optional<User> findUserEntityById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    @Transactional
    public UserInfo registerUser(RegisterUserCommand command) {
        User user = User.createNewUser(command.clerkUserId());
        User savedUser = userRepository.save(user);
        
        // Publish domain event
        eventPublisher.publishEvent(
            UserRegisteredEvent.now(savedUser.getId(), savedUser.getClerkUserId())
        );
        
        return UserInfo.fromUser(savedUser);
    }

    @Override
    @Transactional
    public UserInfo updateUser(UpdateUserCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + command.userId()));
        
        user.updateClerkUserId(command.clerkUserId());
        User savedUser = userRepository.save(user);
        
        // Publish domain event
        eventPublisher.publishEvent(
            UserUpdatedEvent.now(savedUser.getId(), savedUser.getClerkUserId())
        );
        
        return UserInfo.fromUser(savedUser);
    }

    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        
        userRepository.delete(user);
        
        // Publish domain event
        eventPublisher.publishEvent(
            UserDeletedEvent.now(user.getId(), user.getClerkUserId())
        );
    }
}
