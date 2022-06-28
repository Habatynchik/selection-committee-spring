package ua.epan.elearn.selection.committee.spring.model.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.epan.elearn.selection.committee.spring.model.dto.UserDto;
import ua.epan.elearn.selection.committee.spring.model.entity.Role;
import ua.epan.elearn.selection.committee.spring.model.entity.User;
import ua.epan.elearn.selection.committee.spring.model.exception.EmailIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.exception.UsernameIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.repository.RoleRepository;
import ua.epan.elearn.selection.committee.spring.model.repository.UserRepository;

import java.util.List;
import java.util.Optional;


/**
 * Service for business logic related with User.
 *
 * @author Nikita Gamaiunov
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;


    /**
     * Process creating new user account.
     *
     * @param userDto object that contains validated information for creating User.
     * @return saved User in database.
     * @throws UsernameIsReservedException Indicates that username is reserved.
     * @throws EmailIsReservedException    Indicates that email is reserved.
     */
    @Transactional
    public User registerNewAccount(UserDto userDto) throws UsernameIsReservedException, EmailIsReservedException {
        checkUsernameIsUnique(userDto.getUsername());
        checkEmailIsUnique(userDto.getEmail());

        User user = User.builder()
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .password(encoder.encode(userDto.getPassword()))
                .firstName(userDto.getFirstName())
                .secondName(userDto.getSecondName())
                .patronymic(userDto.getPatronymic())
                .city(userDto.getCity())
                .region(userDto.getRegion())
                .institution(userDto.getInstitution())
                .blocked(false)
                .role(roleRepository.findByRoleEnum(Role.RoleEnum.CLIENT))
                .build();

        log.info("New account '{}' has been created", user);
        return userRepository.save(user);
    }

    /**
     * @param username String representing username.
     * @return UserDetails instance.
     * @throws UsernameNotFoundException Indicates that username not found in database.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    public User blockUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setBlocked(true);
            log.info("User (id = {}) has been blocked", id);
            return userRepository.save(user.get());
        }
        return null;
    }

    @Transactional
    public User unblockUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setBlocked(false);
            log.info("User (id = {}) has been unblocked", id);
            return userRepository.save(user.get());
        }
        return null;
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(RuntimeException::new);
    }

    public List<User> getAllUsers() {
        return userRepository.findAllByRole();
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId).get();
    }

    public Page<User> getUserPagination(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    private void checkUsernameIsUnique(String username) throws UsernameIsReservedException {
        if (userRepository.existsByUsername(username)) {
            log.info("Username {} is reserved", username);
            throw new UsernameIsReservedException();
        }

    }

    private void checkEmailIsUnique(String email) throws EmailIsReservedException {
        if (userRepository.existsByEmail(email)) {
            log.info("Email {} is reserved", email);
            throw new EmailIsReservedException();
        }

    }

}
