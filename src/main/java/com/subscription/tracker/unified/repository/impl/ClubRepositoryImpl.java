package com.subscription.tracker.unified.repository.impl;

import com.subscription.tracker.unified.model.Club;
import com.subscription.tracker.unified.model.Role;
import com.subscription.tracker.unified.model.UserClub;
import com.subscription.tracker.unified.model.response.ClubInformationResponse;
import com.subscription.tracker.unified.model.response.ClubTimetableResponse;
import com.subscription.tracker.unified.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@Repository
public class ClubRepositoryImpl implements ClubRepository {

    private static final String SELECT_ALL_CLUBS_BY_CITY_ID =
            "SELECT club.id AS club_id, club.club_name, club.club_name_alt, club.image_name " +
            "FROM club " +
            "INNER JOIN city " +
            "ON club.city_id = city.id " +
            "WHERE city_name_en = :cityEn " +
            "OR city_name_ru = :cityRu";
    private static final String GET_CLUBS_BY_USER_ID =
            "SELECT user_club.user_accepted, user_club.active_club, club.id AS club_id, club.club_name, club.club_name_alt, club.image_name, roles.id AS role_id, roles.role_name " +
            "FROM user_club " +
            "INNER JOIN club " +
            "ON club.id = user_club.club_id " +
            "INNER JOIN user_role " +
            "ON user_club.user_id = user_role.user_id " +
            "INNER JOIN roles " +
            "ON roles.id = user_role.role_id " +
            "WHERE user_club.user_id = :userId";
    private static final String IS_INITIATOR_ALLOWED_FOR_CLUB =
            "SELECT COUNT(user_id) " +
            "FROM user_role " +
            "WHERE user_role.user_id = :userId " +
            "AND user_role.club_id = :clubId " +
            "AND user_role.role_id IN ('1', '2', '3')";
    private static final String ACCEPT_USER_FOR_CLUB =
            "UPDATE user_club " +
            "SET user_club.user_accepted = '1' " +
            "WHERE user_club.user_id = :userId " +
            "AND user_club.club_id = :clubId";

    private static final String UPDATE_TIMETABLE =
            "UPDATE club_timetable " +
            "SET club_timetable.timetable = :message " +
            "WHERE club_timetable.id = :timeTableId";

    private static final String SELECT_TIMETABLE =
            "SELECT club_timetable.id,  club_timetable.timetable " +
            "FROM club_timetable " +
            "WHERE club_timetable.club_id = :clubId";

    private static final String UPDATE_INFORMATION =
            "UPDATE club_info " +
            "SET club_info.information = :information " +
            "WHERE club_info.id = :informationId";

    private static final String SELECT_INFORMATION =
            "SELECT club_info.id,  club_info.information " +
            "FROM club_info " +
            "WHERE club_info.club_id = :clubId";

    private final RowMapper<UserClub> userClubRowMapper = (rs, rowNum) -> {
        final UserClub userClub = (new BeanPropertyRowMapper<>(UserClub.class)).mapRow(rs, rowNum);
        if (userClub != null) {
            userClub.setClub((new BeanPropertyRowMapper<>(Club.class)).mapRow(rs, rowNum));
            userClub.setRole((new BeanPropertyRowMapper<>(Role.class)).mapRow(rs, rowNum));
        }
        return userClub;
    };

    @PersistenceContext
    private EntityManager entityManager;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ClubRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Club> getClubsByCityName(String cityName) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("cityEn", cityName);
        mapSqlParameterSource.addValue("cityRu", cityName);
        return namedParameterJdbcTemplate.query(SELECT_ALL_CLUBS_BY_CITY_ID, mapSqlParameterSource, (new BeanPropertyRowMapper<>(Club.class)));
    }

    @Override
    public List<UserClub> getClubsByUserId(final Integer userId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("userId", userId);
        return namedParameterJdbcTemplate.query(GET_CLUBS_BY_USER_ID, mapSqlParameterSource, userClubRowMapper);
    }

    @Override
    public Integer isInitiatorAllowedForClub(final String initiatorId, final String clubId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("userId", initiatorId);
        mapSqlParameterSource.addValue("clubId", clubId);
        return namedParameterJdbcTemplate.queryForObject(IS_INITIATOR_ALLOWED_FOR_CLUB, mapSqlParameterSource, Integer.class);
    }

    @Override
    public int doAcceptUserForClub(final String clubId, final String userId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("userId", userId);
        mapSqlParameterSource.addValue("clubId", clubId);
        return namedParameterJdbcTemplate.update(ACCEPT_USER_FOR_CLUB, mapSqlParameterSource);
    }

    @Override
    public int updateTimetable(final String escapedMessage, final String timeTableId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("timeTableId", timeTableId);
        mapSqlParameterSource.addValue("message", escapedMessage);
        return namedParameterJdbcTemplate.update(UPDATE_TIMETABLE, mapSqlParameterSource);
    }

    @Override
    public Integer createTimetable(final String escapedMessage, final String clubId) {
        StoredProcedureQuery insertUser = entityManager.createStoredProcedureQuery("InsertTimetable");
        insertUser.registerStoredProcedureParameter("club_id", Integer.class, ParameterMode.IN);
        insertUser.registerStoredProcedureParameter("timetable", String.class, ParameterMode.IN);
        insertUser.registerStoredProcedureParameter("result_timetable_id", Integer.class, ParameterMode.OUT);
        insertUser.setParameter("club_id", Integer.parseInt(clubId));
        insertUser.setParameter("timetable", escapedMessage);
        insertUser.execute();
        return (Integer) insertUser.getOutputParameterValue("result_timetable_id");
    }

    @Override
    public ClubTimetableResponse getTimetableForClub(final String clubId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("clubId", clubId);
        try {
            return namedParameterJdbcTemplate.queryForObject(SELECT_TIMETABLE, mapSqlParameterSource, (new BeanPropertyRowMapper<>(ClubTimetableResponse.class)));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int updateInformation(final String escapedMessage, final String informationId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("informationId", informationId);
        mapSqlParameterSource.addValue("information", escapedMessage);
        return namedParameterJdbcTemplate.update(UPDATE_INFORMATION, mapSqlParameterSource);
    }

    @Override
    public Integer createInformation(final String escapedMessage, final String clubId) {
        StoredProcedureQuery insertUser = entityManager.createStoredProcedureQuery("InsertClubInfo");
        insertUser.registerStoredProcedureParameter("club_id", Integer.class, ParameterMode.IN);
        insertUser.registerStoredProcedureParameter("information", String.class, ParameterMode.IN);
        insertUser.registerStoredProcedureParameter("result_information_id", Integer.class, ParameterMode.OUT);
        insertUser.setParameter("club_id", Integer.parseInt(clubId));
        insertUser.setParameter("information", escapedMessage);
        insertUser.execute();
        return (Integer) insertUser.getOutputParameterValue("result_information_id");
    }

    @Override
    public ClubInformationResponse getInformationForClub(final String clubId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("clubId", clubId);
        try {
            return namedParameterJdbcTemplate.queryForObject(SELECT_INFORMATION, mapSqlParameterSource, (new BeanPropertyRowMapper<>(ClubInformationResponse.class)));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
