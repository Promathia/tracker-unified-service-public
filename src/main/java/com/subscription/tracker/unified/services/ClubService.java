package com.subscription.tracker.unified.services;

import com.subscription.tracker.unified.model.Club;
import com.subscription.tracker.unified.model.UserClub;
import com.subscription.tracker.unified.model.response.ClubInformationResponse;
import com.subscription.tracker.unified.model.response.ClubTimetableResponse;

import java.util.List;

public interface ClubService {

    List<Club> getClubsByCityName(String cityId);

    List<UserClub> getClubsByUserId(Integer userId);

    boolean isInitiatorAllowedForClub(String initiatorId, String clubId);

    boolean doAcceptUserForClub(String clubId, String userId);

    boolean updateTimetable(String escapedMessage, String timeTableId);

    Integer createTimetable(String escapedMessage, String clubId);

    ClubTimetableResponse getTimetableForClub(String clubId);

    boolean updateInformation(String escapedMessage, String informationId);

    Integer createInformation(String escapedMessage, String clubId);

    ClubInformationResponse getInformationForClub(String clubId);
}
