package com.palladium46.odonatesdota.user.service;

import com.palladium46.odonatesdota.steamAuth.model.PlayerInfo;
import com.palladium46.odonatesdota.steamAuth.service.SteamApiInteractionService;
import com.palladium46.odonatesdota.user.model.User;
import com.palladium46.odonatesdota.user.model.UserDto;
import com.palladium46.odonatesdota.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SteamApiInteractionService steamApiInteractionService;

    @Mock
    private UserValidationService userValidationService;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    private String steamId64bitsTest = "76561197968554190";

//    @Test
//    public void should_save_user() {
//        // given
//        PlayerInfo playerInfo = new PlayerInfo("herald slayer", "banana.jpg", 10, "http", null);
//
//        String steamId64bits = steamId64bitsTest;
//
//        doNothing().when(userValidationService).checkUserRepositoryPresence();
//
//        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
//            User savedUser = invocation.getArgument(0);
//            return new User();
//        });
//
//        // when
//        User response = userService.saveUser(playerInfo, steamId64bits);
//
//        // then
//        verify(userRepository, times(1)).save(userCaptor.capture());
//
//        User capturedUser = userCaptor.getValue();
//        assertEquals(steamId64bits, capturedUser.getSteamId64bits());
//        assertEquals("herald slayer", capturedUser.getUserName());
//        System.out.println(capturedUser.getUserName());
//    }

//    @Test
//    public void should_get_all_users() {
//        // given
//        User user1 = new User();
//        user1.setUserName("user1");
//        user1.setSteamId64bits("steamId1");
//        user1.setRankTier(1);
//        User user2 = new User();
//        user2.setUserName("user2");
//        user2.setSteamId64bits("steamId2");
//        user2.setRankTier(2);
//        ArrayList<User> userList = new ArrayList<>();
//        userList.add(user1);
//        userList.add(user2);
//
//        when(userRepository.findAll()).thenReturn(userList);
//
//        // when
//        Set<UserDto> userDtoSet = userService.getUsersSet();
//
//        // then
//        verify(userRepository, times(1)).findAll();
//
//        assertEquals(2, userDtoSet.size());
//
//        UserDto userDto1 = userDtoSet.stream().filter(u -> u.getUserName().equals("user1")).findFirst().orElse(null);
//        assertEquals("user1", userDto1.getUserName());
//        assertEquals("steamId1", userDto1.getSteamId64bits());
//        assertEquals(1, userDto1.getRankTier());
//
//        UserDto userDto2 = userDtoSet.stream().filter(u -> u.getUserName().equals("user2")).findFirst().orElse(null);
//        assertEquals("user2", userDto2.getUserName());
//        assertEquals("steamId2", userDto2.getSteamId64bits());
//        assertEquals(2, userDto2.getRankTier());
//    }

    @Test
    public void should_delete_existing_user() {
        // given
        String userId = "123e4567-e89b-12d3-a456-426614174001";

        doNothing().when(userValidationService).checkUserAndUserRepositoryPresence(userId);
        doNothing().when(userRepository).deleteById(UUID.fromString(userId));

        // when
        ResponseEntity<String> response = userService.deleteUser(userId);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        verify(userValidationService, times(1)).checkUserAndUserRepositoryPresence(userId);

        verify(userRepository, times(1)).deleteById(UUID.fromString(userId));
    }

    @Test
    public void should_throw_exception_when_deleting_with_invalid_uuid_format(){
        // given
        String invalidId = "invalid_uuid_format";
        doNothing().when(userValidationService).checkUserAndUserRepositoryPresence(invalidId);
        // when & then
        assertThrows(IllegalArgumentException.class, () ->{
            userService.deleteUser(invalidId.toString());
        });
    }



//    @Test
//    public void should_edit_user() {
//        // given
//        User existingUser = new User();
//        existingUser.setSteamId64bits("steamId123");
//        existingUser.setUserName("oldUserName");
//        existingUser.setRankTier(3);
//
//        PlayerInfo playerInfo = new PlayerInfo("newUserName", "newImage.jpg", 5, "http", null);
//
//        doNothing().when(userValidationService).checkUserAndUserRepositoryPresence("steamId123");
//
//        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
//            User savedUser = invocation.getArgument(0);
//            return savedUser; // You may modify this based on your actual implementation
//        });
//
//        // when
//        User editedUser = userService.editUser(existingUser, playerInfo );
//
//        // then
//        verify(userValidationService, times(1)).checkUserAndUserRepositoryPresence("steamId123");
//        verify(userRepository, times(1)).save(userCaptor.capture());
//
//        User capturedUser = userCaptor.getValue();
//        assertEquals("steamId123", capturedUser.getSteamId64bits());
//        assertEquals("newUserName", capturedUser.getUserName());
//        assertEquals(5, capturedUser.getRankTier());
//    }

    // J'arrive pas à Mock plusieurs services ici, donc le mieux que je puisse faire c'est de tester indépendament
    // chaque méthode de chaque service

//    @Test
//    public void should_save_or_update_existing_user() throws Exception {
//        // given
//        String steamId64bits = steamId64bitsTest;
//        User foundUser = new User();
//        foundUser.setSteamId64bits(steamId64bits);
//        foundUser.setLastEdit(LocalDateTime.now().minusHours(23));
//
//        when(steamApiInteractionService.getPlayerInfo(eq(steamId64bits)))
//                .thenReturn(new PlayerInfo("newUserName", "newImage.jpg", 5));
//
//        when(userRepository.existsBySteamId64bits(steamId64bits)).thenReturn(true);
//        when(userService.findBySteamId(steamId64bits)).thenReturn(foundUser);
//
//        // when
//        User result = userService.saveOrUpdate(steamId64bits);
//
//        // then
//        verify(userRepository, times(1)).existsBySteamId64bits(steamId64bits);
//        verify(steamApiInteractionService, times(1)).getPlayerInfo(eq(steamId64bits));
//
//        assertEquals(steamId64bits, result.getSteamId64bits());
//        assertEquals("newUserName", result.getUserName());
//        assertEquals(5, result.getRankTier());
//    }
//
//
//    @Test
//    public void should_check_user_presence(){
//        // given
//        UUID id = UUID.randomUUID();
//
//        // when & then
//        assertThrows(EntityNotFoundException.class, () -> {
//            userValidationService.checkUserPresence(id.toString());
//        });
//    }

    @Test
    public void should_return_npe_when_user_repository_is_null(){
        // given
        userRepository = null;
        // when & then
        assertThrows(NullPointerException.class, () -> {
            userRepository.findAll();
        });
    }

}