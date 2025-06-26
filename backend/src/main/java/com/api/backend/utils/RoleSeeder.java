package com.api.backend.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.api.backend.entities.Roles;
import com.api.backend.repositories.RoleRepository;

@Component
public class RoleSeeder implements ApplicationListener<ContextRefreshedEvent> {
  private final RoleRepository roleRepo;

  public RoleSeeder(RoleRepository roleRepo) {
    this.roleRepo = roleRepo;
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    this.loadRoles();
  }

  public void loadRoles() {
    RolesEnum[] roles = new RolesEnum[] { RolesEnum.USER, RolesEnum.ADMIN, RolesEnum.SUPER_ADMIN };

    Map<RolesEnum, String> roleDescriptionMap = Map.of(
        RolesEnum.USER, "Basic Privileges",
        RolesEnum.ADMIN, "Supervisor over users",
        RolesEnum.SUPER_ADMIN, "PEAK RULES of the website");

    Arrays.stream(roles).forEach((role) -> {
      Optional<Roles> optionalRole = roleRepo.findByName(role);

      optionalRole.ifPresentOrElse(System.out::println, () -> {
        Roles roleToCreate = new Roles();

        roleToCreate.setUserRole(role);
        roleToCreate.setUserRoleDescription(roleDescriptionMap.get(role));

        roleRepo.save(roleToCreate);
      });
      ;
    });
  }
}
