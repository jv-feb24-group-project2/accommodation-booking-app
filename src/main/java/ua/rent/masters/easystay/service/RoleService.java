package ua.rent.masters.easystay.service;

import ua.rent.masters.easystay.model.Role;
import ua.rent.masters.easystay.model.Role.RoleName;

public interface RoleService {
    Role getByName(RoleName name);
}