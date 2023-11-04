package com.bistrocheese.userservice.service.factory;

import com.bistrocheese.userservice.dto.request.user.UserRequest;
import com.bistrocheese.userservice.model.user.Staff;
import com.bistrocheese.userservice.model.user.baseUser.User;
import org.springframework.stereotype.Component;

@Component
public class StaffFactory extends UserFactory{
    @Override
    protected User createUser(User user, UserRequest userRequest) {
        return new Staff(
                user,
                userRequest.getForeignLanguage(),
                userRequest.getAcademicLevel()
        );
    }

    @Override
    protected User updateUser(User user, UserRequest userRequest) {
        return new Staff(
                user,
                userRequest.getForeignLanguage(),
                userRequest.getAcademicLevel()
        );
    }
}
