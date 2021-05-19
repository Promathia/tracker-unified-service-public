package com.subscription.tracker.unified.repository.impl;

import com.subscription.tracker.unified.model.Subscription;
import com.subscription.tracker.unified.model.UserSubscription;
import com.subscription.tracker.unified.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private static final String SELECT_SUBSCRIPTIONS_FOR_CLUB =
            "SELECT subscription.id AS subscription_id, subscription.visits_limit, subscription.term_days " +
            "FROM subscription " +
            "WHERE subscription.club_id = :clubId";
    private static final String IS_INITIATOR_ALLOWED_FOR_CLUB =
            "SELECT COUNT(user_id) " +
            "FROM subscription " +
            "INNER JOIN user_role " +
            "ON subscription.club_id = user_role.club_id " +
            "WHERE user_role.user_id = :userId " +
            "AND subscription.id = :subscriptionId " +
            "AND user_role.role_id IN ('1', '2', '3')";
    private static final String ADD_SUBSCRIPTION_TO_USER =
            "INSERT INTO user_subscription (user_id, subscription_id, buy_date, deadline, visit_counter) " +
            "VALUES (:userId, :subscriptionId, :buyDate, :deadline, '0')";
    private static final String GET_SUBSCRIPTIONS_FOR_USER =
            "SELECT user_subscription.id, subscription.visits_limit, user_subscription.buy_date, user_subscription.deadline, user_subscription.visit_counter " +
            "FROM user_subscription " +
            "INNER JOIN subscription " +
            "ON subscription.id = user_subscription.subscription_id " +
            "WHERE user_subscription.user_id = :userId " +
            "AND user_subscription.deadline >= date(:today) " +
            "AND user_subscription.visit_counter < subscription.visits_limit " +
            "OR user_subscription.user_id = :userId " +
            "AND subscription.visits_limit = 0 " +
            "AND user_subscription.deadline >= date(:today)";

    @PersistenceContext
    private EntityManager entityManager;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public SubscriptionRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<Subscription> getSubscriptionsForClub(String clubId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("clubId", clubId);
        return namedParameterJdbcTemplate.query(SELECT_SUBSCRIPTIONS_FOR_CLUB, mapSqlParameterSource, new BeanPropertyRowMapper<>(Subscription.class));
    }

    @Override
    public Integer isInitiatorAllowedForAddSubscription(String initiatorId, String subscriptionId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("userId", initiatorId);
        mapSqlParameterSource.addValue("subscriptionId", subscriptionId);
        return namedParameterJdbcTemplate.queryForObject(IS_INITIATOR_ALLOWED_FOR_CLUB, mapSqlParameterSource, Integer.class);
    }

    @Override
    public Integer addSubscriptionToUser(String userId, String subscriptionId, int term) {
        final Date currentDate = new Date();
        LocalDateTime localDateTime = currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusDays(term);
        final Date deadline = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("userId", userId);
        mapSqlParameterSource.addValue("subscriptionId", subscriptionId);
        mapSqlParameterSource.addValue("buyDate", currentDate);
        mapSqlParameterSource.addValue("deadline", deadline);
        return namedParameterJdbcTemplate.update(ADD_SUBSCRIPTION_TO_USER, mapSqlParameterSource);
    }

    @Override
    public List<UserSubscription> getSubscriptionsForUser(String userId) {
        final MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("userId", userId);
        mapSqlParameterSource.addValue("today", new Date());
        return namedParameterJdbcTemplate.query(GET_SUBSCRIPTIONS_FOR_USER, mapSqlParameterSource, new BeanPropertyRowMapper<>(UserSubscription.class));
    }

    @Override
    public Integer useUserSubscription(String userId, String subscriptionId) {
        final StoredProcedureQuery useUserSubscription = entityManager.createStoredProcedureQuery("UseUserSubscription");
        useUserSubscription.registerStoredProcedureParameter("subscriptionId", Integer.class, ParameterMode.IN);
        useUserSubscription.registerStoredProcedureParameter("visitDate", Date.class, ParameterMode.IN);
        useUserSubscription.registerStoredProcedureParameter("rowCount", Integer.class, ParameterMode.OUT);
        useUserSubscription.setParameter("subscriptionId", Integer.parseInt(subscriptionId));
        useUserSubscription.setParameter("visitDate", new Date());
        useUserSubscription.execute();
        final Object rowCount = useUserSubscription.getOutputParameterValue("rowCount");
        if (rowCount != null) {
            return (Integer) rowCount;
        }
        return 0;
    }
}
