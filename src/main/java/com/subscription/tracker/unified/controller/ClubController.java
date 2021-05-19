package com.subscription.tracker.unified.controller;

import com.subscription.tracker.unified.model.Club;
import com.subscription.tracker.unified.model.request.ClubInformationRequest;
import com.subscription.tracker.unified.model.request.ClubTimetableRequest;
import com.subscription.tracker.unified.model.request.UserAcceptToClubRequest;
import com.subscription.tracker.unified.model.response.ClubInformationResponse;
import com.subscription.tracker.unified.model.response.ClubTimetableResponse;
import com.subscription.tracker.unified.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ClubController {

    private ClubService clubService;

    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @GetMapping("/city/{cityName}/clubs")
    public ResponseEntity<List<Club>> getClubsByCityName(@PathVariable String cityName) {
        return ResponseEntity.ok(clubService.getClubsByCityName(cityName));
    }

    @PostMapping("/clubs/{clubId}/user/{userId}/accept")
    public ResponseEntity<Map<String, String>> doAcceptUserToClub(@Valid @RequestBody UserAcceptToClubRequest body,
                                                                  @PathVariable String clubId,
                                                                  @PathVariable String userId) {
        if (!clubService.isInitiatorAllowedForClub(body.getInitiatorId(), clubId)) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        if (clubService.doAcceptUserForClub(clubId, userId)) {
            Map<String, String> result = new HashMap<>();
            result.put("status", "success");
            return ResponseEntity.ok(result);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/club/{clubId}/timetable/{timeTableId}")
    public ResponseEntity<Map<String, String>> updateTimetable(@Valid @RequestBody ClubTimetableRequest body,
                                                               @PathVariable String clubId,
                                                               @PathVariable String timeTableId) {

        if (clubService.updateTimetable(body.getTimetable(), timeTableId)) {
            Map<String, String> result = new HashMap<>();
            result.put("status", "success");
            return ResponseEntity.ok(result);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/club/{clubId}/timetable")
    public ResponseEntity<Map<String, Integer>> createTimetable(@Valid @RequestBody ClubTimetableRequest body,
                                                                @PathVariable String clubId) {

        Map<String, Integer> result = new HashMap<>();
        result.put("id", clubService.createTimetable(body.getTimetable(), clubId));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/club/{clubId}/timetable")
    public ResponseEntity<ClubTimetableResponse> getTimetable(@PathVariable String clubId) {
        ClubTimetableResponse timetableForClub = clubService.getTimetableForClub(clubId);
        if (timetableForClub == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(timetableForClub);
    }

    @PostMapping("/club/{clubId}/information/{informationId}")
    public ResponseEntity<Map<String, String>> updateInformation(@Valid @RequestBody ClubInformationRequest body,
                                                                 @PathVariable String clubId,
                                                                 @PathVariable String informationId) {

        if (clubService.updateInformation(body.getInformation(), informationId)) {
            Map<String, String> result = new HashMap<>();
            result.put("status", "success");
            return ResponseEntity.ok(result);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/club/{clubId}/information")
    public ResponseEntity<Map<String, Integer>> createInformation(@Valid @RequestBody ClubInformationRequest body,
                                                                  @PathVariable String clubId) {

        Map<String, Integer> result = new HashMap<>();
        result.put("id", clubService.createInformation(body.getInformation(), clubId));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/club/{clubId}/information")
    public ResponseEntity<ClubInformationResponse> getInformation(@PathVariable String clubId) {
        ClubInformationResponse clubInformationResponse = clubService.getInformationForClub(clubId);
        if (clubInformationResponse == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(clubInformationResponse);
    }

}
