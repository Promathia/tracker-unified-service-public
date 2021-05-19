package com.subscription.tracker.unified.repository;

import com.subscription.tracker.unified.model.User;
import com.subscription.tracker.unified.model.request.UserCreateBody;
import com.subscription.tracker.unified.model.request.UserUpdateBody;
import com.subscription.tracker.unified.model.response.UserForClubResponseBody;

import java.util.List;

public interface UserRepository {

    Integer createUserData(UserCreateBody body);

    User getUser(String userId);

    List<UserForClubResponseBody> getUsersForClub(String clubId);

    UserForClubResponseBody getUserForClub(String clubId, String userId);

    Integer isInitiatorAllowedToUpdateUser(String initiatorId, String clubId);

    Integer updateUserProfile(String userId, UserUpdateBody body);

    Integer updateUserRole(String userId, int userRole);
}
