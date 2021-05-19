package com.subscription.tracker.unified.repository.impl;

import com.subscription.tracker.unified.model.RegistrationType;
import com.subscription.tracker.unified.model.Role;
import com.subscription.tracker.unified.model.User;
import com.subscription.tracker.unified.model.request.UserCreateBody;
import com.subscription.tracker.unified.model.request.UserUpdateBody;
import com.subscription.tracker.unified.model.response.UserForClubResponseBody;
import com.subscription.tracker.unified.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String SELECT_ALL_USERS =
            "SELECT users.id, users.external_id, users.first_name, users.last_name, " +
            "users.phone, users.email, users.birth_date, users.registration_type_id, " +
            "registrationtype.id, registrationtype.registration_type_name " +
            "FROM users " +
            "INNER JOIN registrationtype " +
            "ON users.registration_type_id = registrationtype.id";

    private static final String SELECT_USER_BY_EXTERNAL_ID =
            "SELECT users.id AS uid, users.external_id, users.first_name, users.last_name, " +
            "users.phone, users.email, users.birth_date, " +
            "registrationtype.id, registrationtype.registration_type_name " +
            "FROM users " +
            "INNER JOIN registrationtype " +
            "ON users.registration_type_id = registrationtype.id " +
            "WHERE users.external_id = :externalId";
    private static final String SELECT_USERS_FOR_CLUB =
            "SELECT users.id, users.first_name, users.last_name, users.phone, users.email, users.birth_date, roles.role_name, user_club.user_accepted " +
            "FROM users " +
            "INNER JOIN user_club " +
            "ON users.id = user_club.user_id " +
            "INNER JOIN user_role " +
            "ON users.id = user_role.user_id " +
            "INNER JOIN roles " +
            "ON roles.id = user_role.role_id " +
            "WHERE user_club.club_id = :clubId";

    private static final String SELECT_USER_FOR_CLUB =
            "SELECT users.id, users.first_name, users.last_name, users.phone, users.email, users.birth_date, roles.id as roleId, roles.role_name, user_club.user_accepted " +
            "FROM users " +
            "INNER JOIN user_club " +
            "ON users.id = user_club.user_id " +
            "INNER JOIN user_role " +
            "ON users.id = user_role.user_id " +
            "INNER JOIN roles " +
            "ON roles.id = user_role.role_id " +
            "WHERE user_club.club_id = :clubId " +
            "AND users.id = :userId";

    private static final String IS_INITIATOR_ALLOWED_TO_UPDATE_USER =
            "SELECT COUNT(user_id) " +
            "FROM user_role " +
            "WHERE user_role.user_id = :initiatorId " +
            "AND user_role.club_id = :clubId " +
            "AND user_role.role_id IN ('1', '2')";

    private static final String USER_ROLE_UPDATE_QUERY =
            "UPDATE user_role " +
            "SET user_role.role_id = :roleId " +
            "WHERE user_role.user_id = :userId";

    private static final String USER_DATA_UPDATE_QUERY =
            "UPDATE users " +
            "SET %s " +
            "WHERE users.id = :userId";

    @PersistenceContext
    private EntityManager entityManager;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public UserRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private final RowMapper<User> userMapper = (rs, rowNum) -> {
        User user = (new BeanPropertyRowMapper<>(User.class)).mapRow(rs, rowNum);
        if (user != null) {
            user.setRegistrationType((new BeanPropertyRowMapper<>(RegistrationType.class)).mapRow(rs, rowNum));
        }
        return user;
    };

    private final RowMapper<UserForClubResponseBody> usersForClubMapper = (rs, rowNum) -> {
        UserForClubResponseBody userForClubResponseBody =
                (new BeanPropertyRowMapper<>(UserForClubResponseBody.class)).mapRow(rs, rowNum);
        if (userForClubResponseBody != null) {
            userForClubResponseBody
                    .setRole((new BeanPropertyRowMapper<>(Role.class)).mapRow(rs, rowNum));
        }
        return userForClubResponseBody;
    };

    @Override
    public Integer createUserData(UserCreateBody body) {
        StoredProcedureQuery insertUser = entityManager.createStoredProcedureQuery("InsertUser");
        insertUser.registerStoredProcedureParameter("external_id", String.class, ParameterMode.IN);
        insertUser.registerStoredProcedureParameter("first_name", String.class, ParameterMode.IN);
        insertUser.registerStoredProcedureParameter("last_name", String.class, ParameterMode.IN);
        insertUser.registerStoredProcedureParameter("phone", String.class, ParameterMode.IN);
        insertUser.registerStoredProcedureParameter("email", String.class, ParameterMode.IN);
        insertUser.registerStoredProcedureParameter("birth_date", Date.class, ParameterMode.IN);
        insertUser.registerStoredProcedureParameter("registration_type_id", Integer.class, ParameterMode.IN);
        insertUser.registerStoredProcedureParameter("club_id", Integer.class, ParameterMode.IN);
        insertUser.registerStoredProcedureParameter("result_user_id", Integer.class, ParameterMode.OUT);
        insertUser.setParameter("external_id", body.getExternalId());
        insertUser.setParameter("first_name", body.getFirstName());
        insertUser.setParameter("last_name", body.getLastName());
        insertUser.setParameter("phone", body.getPhone());
        insertUser.setParameter("email", body.getEmail());
        insertUser.setParameter("birth_date", body.getBirthDate());
        insertUser.setParameter("registration_type_id", body.getRegistrationType().getId());
        insertUser.setParameter("club_id", body.getClubId());
        insertUser.execute();
        return (Integer) insertUser.getOutputParameterValue("result_user_id");
    }

    @Override
    public User getUser(final String userId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("externalId", userId);
        try {
            return namedParameterJdbcTemplate.queryForObject(SELECT_USER_BY_EXTERNAL_ID, mapSqlParameterSource, userMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<UserForClubResponseBody> getUsersForClub(String clubId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("clubId", clubId);
        return namedParameterJdbcTemplate.query(SELECT_USERS_FOR_CLUB, mapSqlParameterSource, usersForClubMapper);
    }

    @Override
    public UserForClubResponseBody getUserForClub(String clubId, String userId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("clubId", clubId);
        mapSqlParameterSource.addValue("userId", userId);
        try {
            return namedParameterJdbcTemplate.queryForObject(SELECT_USER_FOR_CLUB, mapSqlParameterSource, usersForClubMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Integer isInitiatorAllowedToUpdateUser(String initiatorId, String clubId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("initiatorId", initiatorId);
        mapSqlParameterSource.addValue("clubId", clubId);
        return namedParameterJdbcTemplate.queryForObject(IS_INITIATOR_ALLOWED_TO_UPDATE_USER, mapSqlParameterSource, Integer.class);
    }

    @Override
    public Integer updateUserProfile(String userId, UserUpdateBody body) {
        final StringBuilder updateFields = new StringBuilder();
        final MapSqlParameterSource userDataUpdateParams = new MapSqlParameterSource();
        userDataUpdateParams.addValue("userId", userId);
        if (body.getUserName() != null && !body.getUserName().isEmpty()) {
            updateFields.append("users.first_name = :userName");
            userDataUpdateParams.addValue("userName", body.getUserName());
        }
        if (body.getUserSurname() != null && !body.getUserSurname().isEmpty()) {
            if (updateFields.length() != 0 ) {
                updateFields.append(", ");
            }
            updateFields.append("users.last_name = :userSurname");
            userDataUpdateParams.addValue("userSurname", body.getUserSurname());
        }
        if (body.getUserPhone() != null && !body.getUserPhone().isEmpty()) {
            if (updateFields.length() != 0 ) {
                updateFields.append(", ");
            }
            updateFields.append("users.phone = :userPhone");
            userDataUpdateParams.addValue("userPhone", body.getUserPhone());
        }
        if (body.getUserEmail() != null && !body.getUserEmail().isEmpty()) {
            if (updateFields.length() != 0 ) {
                updateFields.append(", ");
            }
            updateFields.append("users.email = :userEmail");
            userDataUpdateParams.addValue("userEmail", body.getUserEmail());
        }
        if (body.getUserBirthdate() != null) {
            if (updateFields.length() != 0 ) {
                updateFields.append(", ");
            }
            updateFields.append("users.birth_date = :birthdate");
            userDataUpdateParams.addValue("birthdate", body.getUserBirthdate());
        }
        if (updateFields.length() != 0) {
            return namedParameterJdbcTemplate.update(String.format(USER_DATA_UPDATE_QUERY, updateFields.toString()), userDataUpdateParams);
        }
        return 0;
    }

    @Override
    public Integer updateUserRole(String userId, int userRole) {
        if (userRole != 0) {
            final MapSqlParameterSource roleUpdateParams = new MapSqlParameterSource();
            roleUpdateParams.addValue("roleId", userRole);
            roleUpdateParams.addValue("userId", userId);
            return namedParameterJdbcTemplate.update(USER_ROLE_UPDATE_QUERY, roleUpdateParams);
        }
        return 0;
    }
}
