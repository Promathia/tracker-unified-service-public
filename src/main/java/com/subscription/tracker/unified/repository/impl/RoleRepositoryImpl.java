package com.subscription.tracker.unified.repository.impl;

import com.subscription.tracker.unified.model.Role;
import com.subscription.tracker.unified.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RoleRepositoryImpl implements RoleRepository {

    private static final String SELECT_ROLES_BY_USER_ID =
            "SELECT roles.id, roles.role_name " +
            "FROM user_role " +
            "INNER JOIN roles " +
            "ON roles.id = user_role.role_id " +
            "WHERE user_role.user_id = :userId";

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public RoleRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Set<Role> getRolesByUserId(final Integer id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("userId", id);
        List<Role> result = namedParameterJdbcTemplate.query(SELECT_ROLES_BY_USER_ID, mapSqlParameterSource, (new BeanPropertyRowMapper<>(Role.class)));
        return new HashSet<>(result);
    }

}
