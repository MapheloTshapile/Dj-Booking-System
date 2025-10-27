package za.ac.cput.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import za.ac.cput.domain.User;
import za.ac.cput.service.IUserService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    private final IUserService userService;

    @Autowired
    public UserController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * DEPRECATED: Use /api/auth/register instead
     * This endpoint is kept for backward compatibility but should not be used
     * AuthController provides proper security with JWT token response
     */
    @Deprecated
    @PostMapping("/create")
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        User created = userService.create(user);
        return ResponseEntity.status(201).body(created);
    }

    // Admin can view specific user details
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/read/{id}")
    public ResponseEntity<User> read(@PathVariable Long id) {
        User user = userService.read(id);
        return ResponseEntity.of(java.util.Optional.ofNullable(user));
    }

    // Admin can update user details (e.g., change role, update info)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        return ResponseEntity.of(java.util.Optional.ofNullable(userService.update(user)));
    }

    // Admin can delete users
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Admin can view all users
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }

    /**
     * DEPRECATED: Use /api/auth/login instead
     * This endpoint is kept for backward compatibility but should not be used
     * AuthController provides proper JWT authentication with token response
     */
    @Deprecated
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        User user = userService.login(email, password);
        return user != null ? ResponseEntity.ok(user) : ResponseEntity.status(401).build();
    }
}
