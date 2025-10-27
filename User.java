package za.ac.cput.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String fullName;
    private String email;
    private String password;
    private String phone;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Booking> bookings;

    protected User() {}

    private User(Builder b) {
        this.userId = b.userId;
        this.fullName = b.fullName;
        this.email = b.email;
        this.password = b.password;
        this.phone = b.phone;
        this.role = b.role;
        this.bookings = b.bookings;
    }

    public Long getUserId() { return userId; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public Role getRole() { return role; }
    public List<Booking> getBookings() { return bookings; }

    public static class Builder {
        private Long userId;
        private String fullName, email, password, phone;
        private Role role;
        private List<Booking> bookings;

        public Builder setUserId(Long userId) { this.userId = userId; return this; }
        public Builder setFullName(String fullName) { this.fullName = fullName; return this; }
        public Builder setEmail(String email) { this.email = email; return this; }
        public Builder setPassword(String password) { this.password = password; return this; }
        public Builder setPhone(String phone) { this.phone = phone; return this; }
        public Builder setRole(Role role) { this.role = role; return this; }
        public Builder setBookings(List<Booking> bookings) { this.bookings = bookings; return this; }

        public Builder copy(User u) {
            this.userId = u.userId;
            this.fullName = u.fullName;
            this.email = u.email;
            this.password = u.password;
            this.phone = u.phone;
            this.role = u.role;
            this.bookings = u.bookings;
            return this;
        }

        public User build() { return new User(this); }
    }
}
