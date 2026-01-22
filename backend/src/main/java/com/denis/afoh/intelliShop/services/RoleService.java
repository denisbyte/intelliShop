package com.denis.afoh.intelliShop.services;

import com.denis.afoh.intelliShop.entity.Role;

public interface RoleService {
    Role getOrCreateRole(String roleName);
}
