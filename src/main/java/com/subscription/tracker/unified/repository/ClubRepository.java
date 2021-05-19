package com.subscription.tracker.unified.repository;

import com.subscription.tracker.unified.model.Club;
import com.subscription.tracker.unified.model.UserClub;
import com.subscription.tracker.unified.model.response.ClubInformationResponse;
import com.subscription.tracker.unified.model.response.ClubTimetableResponse;

import java.util.List;

public interface ClubRepository {

    List<Club> getClubsByCityName(String cityId);

    List<UserClub> getClubsByUserId(Integer userId);

    Integer isInitiatorAllowedForClub(String initiatorId, String clubId);

    int doAcceptUserForClub(String clubId, String userId);

    int updateTimetable(String escapedMessage, String timeTableId);

    Integer createTimetable(String escapedMessage, String clubId);

    ClubTimetableResponse getTimetableForClub(String clubId);

    int updateInformation(String escapedMessage, String informationId);

    Integer createInformation(String escapedMessage, String clubId);

    ClubInformationResponse getInformationForClub(String clubId);

}
