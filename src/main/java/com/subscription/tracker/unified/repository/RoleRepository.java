package com.subscription.tracker.unified.repository;

import com.subscription.tracker.unified.model.Role;

import java.util.Set;

public interface RoleRepository {

    Set<Role> getRolesByUserId(Integer id);

}
