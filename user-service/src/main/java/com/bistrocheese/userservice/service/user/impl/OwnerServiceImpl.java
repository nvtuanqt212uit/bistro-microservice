package com.bistrocheese.userservice.service.user.impl;

import com.bistrocheese.userservice.constant.MessageConstant;
import com.bistrocheese.userservice.dto.request.user.UserRequest;
import com.bistrocheese.userservice.exception.BadRequestException;
import com.bistrocheese.userservice.model.user.Owner;
import com.bistrocheese.userservice.model.user.baseUser.User;
import com.bistrocheese.userservice.repository.user.OwnerRepository;
import com.bistrocheese.userservice.service.user.ManagerService;
import com.bistrocheese.userservice.service.user.OwnerService;
import com.bistrocheese.userservice.service.user.StaffService;
import com.bistrocheese.userservice.service.user.UserService;
import com.bistrocheese.userservice.service.user.factory.OwnerFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OwnerServiceImpl implements OwnerService {
    private final OwnerFactory ownerFactory;
    private final OwnerRepository ownerRepository;
    private final ManagerService managerService;
    private final StaffService staffService;

    private final Map<Integer, UserService> roleToServiceMap;
    private final UserService[] services;

    public OwnerServiceImpl(
            OwnerFactory ownerFactory,
            OwnerRepository ownerRepository,
            ManagerService managerService,
            StaffService staffService,
            Map<Integer, UserService> roleToServiceMap
    ) {
        this.ownerFactory = ownerFactory;
        this.ownerRepository = ownerRepository;
        this.managerService = managerService;
        this.staffService = staffService;
        this.roleToServiceMap = roleToServiceMap;
        this.services = new UserService[]{this, managerService, staffService};
    }

    @PostConstruct
    private void initRoleToServiceMap() {
        roleToServiceMap.put(0, this);
        roleToServiceMap.put(1, managerService);
        roleToServiceMap.put(2, staffService);
    }

    // UserService implementation Start
    @Override
    public Boolean isEmailExists(String email) {
        return ownerRepository.findByEmail(email).isPresent();
    }

    @Override
    public void saveUser(UserRequest userRequest) {
        ownerRepository.save((Owner) ownerFactory.create(userRequest));
    }

    @Override
    public List<User> getUsers() {
        return  new ArrayList<>(ownerRepository.findAll());
    }

    @Override
    public Optional<? extends User> getUserById(String userId) {
        return ownerRepository.findById(userId);
    }

    @Override
    public void deleteUserById(String userId) {
        ownerRepository.deleteById(userId);
    }

    @Override
    public void updateUserById(User user, UserRequest userRequest) {
        ownerRepository.save((Owner) ownerFactory.update(user, userRequest));
    }
    // UserService implementation End

    // OwnerService implementation Start
    @Override
    public void createUser(UserRequest userRequest) {
        this.checkEmailExists(userRequest.getEmail());
        UserService userService = roleToServiceMap.get(userRequest.getRole());
        if (userService == null) {
            throw new BadRequestException(MessageConstant.INVALID_ROLE_ID);
        } else {
            userService.saveUser(userRequest);
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        for (UserService service : this.services) {
            users.addAll(service.getUsers());
        }
        return users;
    }

    @Override
    public User getUserDetail(String userId) {
        for (UserService service : this.services) {
            Optional<? extends User> user = service.getUserById(userId);
            if (user.isPresent()) {
                return user.get();
            }
        }
        throw new BadRequestException(MessageConstant.USER_NOT_FOUND);
    }

    @Override
    public void deleteUser(String userId) {
        for (UserService service : this.services) {
            Optional<? extends User> user = service.getUserById(userId);
            if (user.isPresent()) {
                service.deleteUserById(userId);
                return;
            }
        }
        throw new BadRequestException(MessageConstant.USER_NOT_FOUND);
    }

    @Override
    public void updateUser(String userId, UserRequest userRequest) {
        this.checkEmailExists(userRequest.getEmail());
        for (UserService service : this.services) {
            Optional<? extends User> user = service.getUserById(userId);
            if (user.isPresent()) {
                // Update Role
                if (!Objects.equals(user.get().getRole().ordinal(), userRequest.getRole())) {
                    UserService userService = roleToServiceMap.get(userRequest.getRole());
                    if (userService == null) {
                        throw new BadRequestException(MessageConstant.INVALID_ROLE_ID);
                    } else {
                        // Delete User In Old Role
                        service.deleteUserById(userId);
                        // Update User In New Role
                        userService.updateUserById(user.get(), userRequest);
                    }
                    return;
                }
                // Update User
                service.updateUserById(user.get(), userRequest);
                return;
            }
        }
        throw new BadRequestException(MessageConstant.USER_NOT_FOUND);
    }
    // OwnerService implementation End

    private void checkEmailExists(String email) {
        for (UserService service : this.services) {
            if (service.isEmailExists(email)) {
                throw new BadRequestException(MessageConstant.EMAIL_ALREADY_EXISTS);
            }
        }
    }
}
