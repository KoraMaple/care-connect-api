package com.careconnect.coreapi.user.internal;

import com.careconnect.coreapi.user.UserInfo;
import com.careconnect.coreapi.user.api.RegisterUserCommand;
import com.careconnect.coreapi.user.api.UpdateUserCommand;
import com.careconnect.coreapi.user.domain.User;
import com.careconnect.coreapi.user.events.UserDeletedEvent;
import com.careconnect.coreapi.user.events.UserRegisteredEvent;
import com.careconnect.coreapi.user.events.UserUpdatedEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UUID userId;
    private String clerkUserId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        clerkUserId = "clerk_user_123";

        testUser = new User();
        testUser.setId(userId);
        testUser.setClerkUserId(clerkUserId);
        testUser.setCreatedAt(Instant.now());
        testUser.setUpdatedAt(Instant.now());
    }

    @Test
    void findUserById_WhenUserExists_ShouldReturnUserInfo() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // When
        Optional<UserInfo> result = userService.findUserById(userId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(userId);
        assertThat(result.get().clerkUserId()).isEqualTo(clerkUserId);
        verify(userRepository).findById(userId);
    }

    @Test
    void findUserById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<UserInfo> result = userService.findUserById(userId);

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findById(userId);
    }

    @Test
    void findUserByClerkId_WhenUserExists_ShouldReturnUserInfo() {
        // Given
        when(userRepository.findByClerkUserId(clerkUserId)).thenReturn(Optional.of(testUser));

        // When
        Optional<UserInfo> result = userService.findUserByClerkId(clerkUserId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().id()).isEqualTo(userId);
        assertThat(result.get().clerkUserId()).isEqualTo(clerkUserId);
        verify(userRepository).findByClerkUserId(clerkUserId);
    }

    @Test
    void findUserByClerkId_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(userRepository.findByClerkUserId(clerkUserId)).thenReturn(Optional.empty());

        // When
        Optional<UserInfo> result = userService.findUserByClerkId(clerkUserId);

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findByClerkUserId(clerkUserId);
    }

    @Test
    void findUserEntityById_WhenUserExists_ShouldReturnUser() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // When
        Optional<User> result = userService.findUserEntityById(userId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testUser);
        verify(userRepository).findById(userId);
    }

    @Test
    void findUserEntityById_WhenUserDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.findUserEntityById(userId);

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findById(userId);
    }

    @Test
    void registerUser_WithValidCommand_ShouldCreateUserAndPublishEvent() {
        // Given
        RegisterUserCommand command = RegisterUserCommand.of(clerkUserId);
        User newUser = User.createNewUser(clerkUserId);
        newUser.setId(userId);

        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // When
        UserInfo result = userService.registerUser(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.clerkUserId()).isEqualTo(clerkUserId);
        assertThat(result.id()).isEqualTo(userId);

        verify(userRepository).save(any(User.class));

        // Verify event publication
        ArgumentCaptor<UserRegisteredEvent> eventCaptor = ArgumentCaptor.forClass(UserRegisteredEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        
        UserRegisteredEvent publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.getUserId()).isEqualTo(userId);
        assertThat(publishedEvent.getClerkUserId()).isEqualTo(clerkUserId);
    }

    @Test
    void updateUser_WithValidCommand_ShouldUpdateUserAndPublishEvent() {
        // Given
        String newClerkUserId = "new_clerk_user_456";
        UpdateUserCommand command = UpdateUserCommand.of(userId, newClerkUserId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);

        // When
        UserInfo result = userService.updateUser(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(userId);

        verify(userRepository).findById(userId);
        verify(userRepository).save(testUser);

        // Verify event publication
        ArgumentCaptor<UserUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(UserUpdatedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        
        UserUpdatedEvent publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.getUserId()).isEqualTo(userId);
    }

    @Test
    void updateUser_WhenUserNotFound_ShouldThrowIllegalArgumentException() {
        // Given
        UpdateUserCommand command = UpdateUserCommand.of(userId, "new_clerk_id");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found: " + userId);

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUserAndPublishEvent() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository).findById(userId);
        verify(userRepository).delete(testUser);

        // Verify event publication
        ArgumentCaptor<UserDeletedEvent> eventCaptor = ArgumentCaptor.forClass(UserDeletedEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        
        UserDeletedEvent publishedEvent = eventCaptor.getValue();
        assertThat(publishedEvent.getUserId()).isEqualTo(userId);
        assertThat(publishedEvent.getClerkUserId()).isEqualTo(clerkUserId);
    }

    @Test
    void deleteUser_WhenUserNotFound_ShouldThrowIllegalArgumentException() {
        // Given
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found: " + userId);

        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any());
        verifyNoInteractions(eventPublisher);
    }
}