package com.palladium46.odonatesdota.user.service;

import com.palladium46.odonatesdota.exceptions.EntityNotFoundException;
import com.palladium46.odonatesdota.exceptions.ValidationException;
import com.palladium46.odonatesdota.user.model.User;
import com.palladium46.odonatesdota.user.model.UserDto;
import com.palladium46.odonatesdota.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserValidationService {

    private final UserRepository userRepository;

    public UserValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkUserAndUserRepositoryPresence(String steamId){
        this.checkUserPresence(steamId);
        this.checkUserRepositoryPresence();
    }

    public void checkUserRepositoryPresence() {
        Objects.requireNonNull(userRepository, "UserRepository must not be null");
    }
    public void checkUserValidations(BindingResult result){
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors()
                    .stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            throw new ValidationException(errorMessages);
        }
    }

    public User checkUserPresence(String steamId) {
        User user = userRepository.findBySteamId64bits(steamId);
        if (user == null) throw new EntityNotFoundException("User not found with steamId : " + steamId);
        return user;
    }

    public void setRankTier(User user, UserDto userDto){
        if (userDto.getRankTier() != null) {
            user.setRankTier(userDto.getRankTier());
        }
    }

}
