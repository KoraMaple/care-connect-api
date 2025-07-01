package com.careconnect.coreapi.user.internal;

import com.careconnect.coreapi.user.UserInfo;
import com.careconnect.coreapi.user.UserService;
import com.careconnect.coreapi.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    @Override
    public Optional<UserInfo> findUserById(UUID userId) {
        return userRepository.findById(userId)
                .map(this::mapToUserInfo);
    }
    
    @Override
    public Optional<UserInfo> findUserByClerkId(String clerkUserId) {
        return userRepository.findByClerkUserId(clerkUserId)
                .map(this::mapToUserInfo);
    }
    
    
    @Override
    public Optional<User> findUserEntityById(UUID userId) {
        return userRepository.findById(userId);
    }

    private UserInfo mapToUserInfo(User user) {
        return new UserInfo(
            user.getId(),
            user.getClerkUserId()
        );
    }
}
