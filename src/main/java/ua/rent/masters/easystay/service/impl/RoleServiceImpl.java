package ua.rent.masters.easystay.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.rent.masters.easystay.model.Role;
import ua.rent.masters.easystay.repository.RoleRepository;
import ua.rent.masters.easystay.service.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getByName(Role.RoleName name) {
        return roleRepository.findByName(name)
                .orElseThrow(
                        () -> new IllegalArgumentException("No role found with name: " + name));
    }
}
