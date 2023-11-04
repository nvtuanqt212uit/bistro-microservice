package com.bistrocheese.userservice.service.factory;

import com.bistrocheese.userservice.dto.request.user.UserRequest;
import com.bistrocheese.userservice.model.user.Manager;
import com.bistrocheese.userservice.model.user.baseUser.User;
import org.springframework.stereotype.Component;

@Component
public class ManagerFactory extends UserFactory {
    @Override
    protected User createUser(User user, UserRequest userRequest) {
        return new Manager(
                user,
                userRequest.getCertificationManagement(),
                userRequest.getForeignLanguage(),
                userRequest.getExperiencedYear()
        );
    }

    @Override
    protected User updateUser(User user, UserRequest userRequest) {
        return new Manager(
                user,
                userRequest.getCertificationManagement(),
                userRequest.getForeignLanguage(),
                userRequest.getExperiencedYear()
        );
    }
}
