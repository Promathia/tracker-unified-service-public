package com.subscription.tracker.unified.services.impl;

import com.subscription.tracker.unified.model.User;
import com.subscription.tracker.unified.model.request.UserCreateBody;
import com.subscription.tracker.unified.model.request.UserUpdateBody;
import com.subscription.tracker.unified.model.response.SingleUserResponseBody;
import com.subscription.tracker.unified.model.response.UserForClubResponseBody;
import com.subscription.tracker.unified.repository.UserRepository;
import com.subscription.tracker.unified.services.ClubService;
import com.subscription.tracker.unified.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ClubService clubService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ClubService clubService) {
        this.userRepository = userRepository;
        this.clubService = clubService;
    }

    @Override
    public Integer createUser(UserCreateBody body) {
        return userRepository.createUserData(body);
    }

    @Override
    public SingleUserResponseBody getSingleUserData(String userId) {
        User user = userRepository.getUser(userId);
        if (user == null) {
            return null;
        }
        SingleUserResponseBody singleUserResponseBody = new SingleUserResponseBody(user);
        if (StringUtils.hasLength(singleUserResponseBody.getFirstName())
                && StringUtils.hasLength(singleUserResponseBody.getLastName())
                && singleUserResponseBody.getBirthDate() != null) {
            singleUserResponseBody.setFilled(true);
        }
        singleUserResponseBody.setUserClubs(clubService.getClubsByUserId(singleUserResponseBody.getId()));
        return singleUserResponseBody;
    }

    @Override
    public List<UserForClubResponseBody> getUsersForClub(String clubId) {
        return userRepository.getUsersForClub(clubId);
    }

    @Override
    public UserForClubResponseBody getUserForClub(String clubId, String userId) {
        return userRepository.getUserForClub(clubId, userId);
    }

    @Override
    public boolean isInitiatorAllowedToUpdateUser(String initiatorId, String clubId) {
        return userRepository.isInitiatorAllowedToUpdateUser(initiatorId, clubId) > 0;
    }

    @Override
    public boolean updateUserData(String userId, UserUpdateBody body) {
        int roleUpdateResult = userRepository.updateUserRole(userId, body.getUserRole());
        int userUpdateResult = userRepository.updateUserProfile(userId, body);
        return roleUpdateResult != 0 || userUpdateResult != 0;
    }
}
