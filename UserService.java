package za.ac.cput.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import za.ac.cput.domain.User;
import za.ac.cput.domain.Role;
import za.ac.cput.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User create(User user) {
        if (user == null) return null;

        // Ensure a newly registered user gets the EVENT_ORGANIZER role by default
        if (user.getRole() == null) {
            user = new User.Builder().copy(user).setRole(Role.EVENT_ORGANIZER).build();
        }

        // Encode the password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user = new User.Builder().copy(user).setPassword(encodedPassword).build();

        return repository.save(user);
    }

    @Override
    public User read(Long userId) {
        Optional<User> opt = repository.findById(userId);
        return opt.orElse(null);
    }

    @Override
    public User update(User user) {
        if (repository.existsById(user.getUserId())) {
            return repository.save(user);
        }
        return null;
    }

    @Override
    public boolean delete(Long userId) {
        if (repository.existsById(userId)) {
            repository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User login(String email, String password) {
        User user = repository.findByEmail(email);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }
        return null;
    }
}
