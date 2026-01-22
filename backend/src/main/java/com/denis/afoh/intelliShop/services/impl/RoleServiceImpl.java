package com.denis.afoh.intelliShop.services.impl;

import com.denis.afoh.intelliShop.entity.Role;
import com.denis.afoh.intelliShop.repository.RoleRepository;
import com.denis.afoh.intelliShop.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role getOrCreateRole(String roleName) {
        return roleRepository.findByNom(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }
}
