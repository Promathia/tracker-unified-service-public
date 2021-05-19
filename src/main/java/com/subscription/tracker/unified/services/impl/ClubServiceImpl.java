package com.subscription.tracker.unified.services.impl;

import com.subscription.tracker.unified.model.Club;
import com.subscription.tracker.unified.model.UserClub;
import com.subscription.tracker.unified.model.response.ClubInformationResponse;
import com.subscription.tracker.unified.model.response.ClubTimetableResponse;
import com.subscription.tracker.unified.repository.ClubRepository;
import com.subscription.tracker.unified.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class ClubServiceImpl implements ClubService {

    private ClubRepository clubRepository;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    @Override
    public List<Club> getClubsByCityName(String cityName) {
        return clubRepository.getClubsByCityName(cityName);
    }

    @Override
    public List<UserClub> getClubsByUserId(Integer userId) {
        return clubRepository.getClubsByUserId(userId);
    }

    @Override
    public boolean isInitiatorAllowedForClub(String initiatorId, String clubId) {
        return clubRepository.isInitiatorAllowedForClub(initiatorId, clubId) > 0;
    }

    @Override
    public boolean doAcceptUserForClub(String clubId, String userId) {
        return clubRepository.doAcceptUserForClub(clubId, userId) > 0;
    }

    @Override
    public boolean updateTimetable(String rawMessage, String timeTableId) {
        return clubRepository.updateTimetable(HtmlUtils.htmlEscape(rawMessage), timeTableId) > 0;
    }

    @Override
    public Integer createTimetable(String rawMessage, String clubId) {
        return clubRepository.createTimetable(HtmlUtils.htmlEscape(rawMessage), clubId);
    }

    @Override
    public ClubTimetableResponse getTimetableForClub(String clubId) {
        return clubRepository.getTimetableForClub(clubId);
    }

    @Override
    public boolean updateInformation(String rawMessage, String informationId) {
        return clubRepository.updateInformation(HtmlUtils.htmlEscape(rawMessage), informationId) > 0;
    }

    @Override
    public Integer createInformation(String rawMessage, String clubId) {
        return clubRepository.createInformation(HtmlUtils.htmlEscape(rawMessage), clubId);
    }

    @Override
    public ClubInformationResponse getInformationForClub(String clubId) {
        return clubRepository.getInformationForClub(clubId);
    }
}
