package com.subscription.tracker.unified.services;

import com.subscription.tracker.unified.model.request.UserCreateBody;
import com.subscription.tracker.unified.model.request.UserUpdateBody;
import com.subscription.tracker.unified.model.response.SingleUserResponseBody;
import com.subscription.tracker.unified.model.response.UserForClubResponseBody;

import java.util.List;

public interface UserService {

    Integer createUser(UserCreateBody body);

    SingleUserResponseBody getSingleUserData(String userId);

    List<UserForClubResponseBody> getUsersForClub(String clubId);

    UserForClubResponseBody getUserForClub(String clubId, String userId);

    boolean isInitiatorAllowedToUpdateUser(String initiatorId, String clubId);

    boolean updateUserData(String userId, UserUpdateBody body);
}
