package ua.epan.elearn.selection.committee.spring.model.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ua.epan.elearn.selection.committee.spring.model.dto.UserDto;
import ua.epan.elearn.selection.committee.spring.model.entity.Role;
import ua.epan.elearn.selection.committee.spring.model.entity.User;
import ua.epan.elearn.selection.committee.spring.model.exception.EmailIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.exception.UsernameIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.repository.RoleRepository;
import ua.epan.elearn.selection.committee.spring.model.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    private final static Long ID = 1L;
    private final static String USERNAME = "TestUser";
    private final static String EMAIL = "v.redko@gmail.com";
    private final static String RANDOM_PASSWORD = "Password12345";
    private final static String HASHED_PASSWORD = "$2a$10$gFo.7l4JUrpKa1CX9T8yguek6X13LDyiX/y4kOPKOokaskJByJKvm";
    private final static String FIRST_NAME = "Vladislav";
    private final static String SECOND_NAME = "Redko";
    private final static String PATRONYMIC = "Petrovich";
    private final static String CITY = "Kiev";
    private final static String REGION = "Kiev";
    private final static String INSTITUTION = "School 163";

    private User USER;
    private UserDto DTO_USER;
    private final static Role CLIENT_ROLE = Role.builder().roleEnum(Role.RoleEnum.CLIENT).build();

    @BeforeEach
    void setUp() {
        USER = User.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(HASHED_PASSWORD)
                .firstName(FIRST_NAME)
                .secondName(SECOND_NAME)
                .patronymic(PATRONYMIC)
                .city(CITY)
                .region(REGION)
                .institution(INSTITUTION)
                .role(CLIENT_ROLE)
                .blocked(false)
                .build();
        DTO_USER = UserDto.builder()
                .username(USERNAME)
                .email(EMAIL)
                .password(RANDOM_PASSWORD)
                .passwordCopy(RANDOM_PASSWORD)
                .firstName(FIRST_NAME)
                .secondName(SECOND_NAME)
                .patronymic(PATRONYMIC)
                .region(REGION)
                .city(CITY)
                .institution(INSTITUTION)
                .build();
    }

    @Test
    void registerUserThrowsUsernameIsReservedException() {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(true);
        assertThrows(UsernameIsReservedException.class, () -> userService.registerNewAccount(DTO_USER));
    }

    @Test
    void registerUserThrowsEmailIsReservedException() {
        when(userRepository.existsByEmail(EMAIL)).thenReturn(true);
        assertThrows(EmailIsReservedException.class, () -> userService.registerNewAccount(DTO_USER));
    }

    @Test
    void registerUserReturnSavedUser() throws EmailIsReservedException, UsernameIsReservedException {
        when(userRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(userRepository.existsByEmail(EMAIL)).thenReturn(false);
        when(userRepository.save(USER)).thenReturn(USER);
        when(passwordEncoder.encode(RANDOM_PASSWORD)).thenReturn(HASHED_PASSWORD);
        when(roleRepository.findByRoleEnum(Role.RoleEnum.CLIENT)).thenReturn(CLIENT_ROLE);

        assertEquals(userService.registerNewAccount(DTO_USER), USER);
        verify(userRepository, times(1)).save(USER);
    }

    @Test
    void loadUserByUsernameThrowsUsernameNotFoundException() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(USERNAME));
    }

    @Test
    void getById() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(USER));
        assertEquals(userService.getById(ID), USER);

        verify(userRepository, times(0)).save(USER);
    }

    @Test
    void getByUsername() {
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(USER));
        assertEquals(userService.getByUsername(USERNAME), USER);

        verify(userRepository, times(0)).save(USER);
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAllByRole()).thenReturn(List.of(USER));
        assertEquals(userService.getAllUsers(), List.of(USER));

        verify(userRepository, times(0)).save(USER);
    }

    @Test
    void findUserById() {

        when(userRepository.findById(ID)).thenReturn(Optional.of(USER));
        assertEquals(userService.findUserById(ID), USER);

        verify(userRepository, times(0)).save(USER);
    }

    @Test
    void unblockUserByIdDoNotUnblock() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        userService.unblockUserById(ID);
        verify(userRepository, times(0)).save(USER);
    }


    @Test
    void blockUserByIdDoNotBlock() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        userService.blockUserById(ID);
        verify(userRepository, times(0)).save(USER);
    }

    @Test
    void blockUserByIdUnblock() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(USER));

        userService.unblockUserById(ID);
        verify(userRepository, times(1)).save(USER);
    }

    @Test
    void blockUserByIdBlock() {
        when(userRepository.findById(ID)).thenReturn(Optional.of(USER));

        userService.blockUserById(ID);
        verify(userRepository, times(1)).save(USER);
    }
}
