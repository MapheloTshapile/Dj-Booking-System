package za.ac.cput.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "djs")
public class DJ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long djId;

    private String stageName;
    private String genre; 
    private double ratePerHour;
    
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    private String bio;
    
    @Lob
    @JsonIgnore
    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    private byte[] imageData;
    
    private String imageContentType;

    @OneToMany(mappedBy = "dj")
    @JsonIgnore
    private List<Booking> bookings;

    @OneToMany(mappedBy = "dj")
    @JsonIgnore
    private List<Review> reviews;

    protected DJ() {}

    private DJ(Builder builder) {
        this.djId = builder.djId;
        this.stageName = builder.stageName;
        this.genre = builder.genre; 
        this.ratePerHour = builder.ratePerHour;
        this.availabilityStatus = builder.availabilityStatus;
        this.bio = builder.bio;
        this.imageData = builder.imageData;
    this.imageContentType = builder.imageContentType;
        this.bookings = builder.bookings;
        this.reviews = builder.reviews;
    }

    public Long getDjId() { return djId; }
    public String getStageName() { return stageName; }
    public String getGenre() { return genre; }
    public double getRatePerHour() { return ratePerHour; }
    public AvailabilityStatus getAvailabilityStatus() { return availabilityStatus; }
    public String getBio() { return bio; }
    public byte[] getImageData() { return imageData; }
    public String getImageContentType() { return imageContentType; }
    public List<Booking> getBookings() { return bookings; }
    public List<Review> getReviews() { return reviews; }

    @Override
    public String toString() {
        return "DJ{" +
                "djId=" + djId +
                ", stageName='" + stageName + '\'' +
                ", genre='" + genre + '\'' +
                ", ratePerHour=" + ratePerHour +
                ", availabilityStatus=" + availabilityStatus +
                ", bio='" + bio + '\'' +
                
                '}';
    }

    public static class Builder {
        private Long djId;
        private String stageName, genre;
        private double ratePerHour;
        private AvailabilityStatus availabilityStatus;
    private String bio;
    private byte[] imageData;
    private String imageContentType;
        private List<Booking> bookings;
        private List<Review> reviews;

        public Builder setDjId(Long djId) { this.djId = djId; return this; }
        public Builder setStageName(String stageName) { this.stageName = stageName; return this; }
        public Builder setGenre(String genre) { this.genre = genre; return this; }
        public Builder setRatePerHour(double ratePerHour) { this.ratePerHour = ratePerHour; return this; }
        public Builder setAvailabilityStatus(AvailabilityStatus availabilityStatus) { this.availabilityStatus = availabilityStatus; return this; }
    public Builder setBio(String bio) { this.bio = bio; return this; }
    public Builder setImageData(byte[] imageData) { this.imageData = imageData; return this; }
    public Builder setImageContentType(String imageContentType) { this.imageContentType = imageContentType; return this; }
        public Builder setBookings(List<Booking> bookings) { this.bookings = bookings; return this; }
        public Builder setReviews(List<Review> reviews) { this.reviews = reviews; return this; }

        public Builder copy(DJ d) {
            this.djId = d.djId;
            this.stageName = d.stageName;
            this.genre = d.genre;
            this.ratePerHour = d.ratePerHour;
            this.availabilityStatus = d.availabilityStatus;
            this.bio = d.bio;
            // imageUrl removed — no-op
            this.imageData = d.imageData;
            this.imageContentType = d.imageContentType;
            this.bookings = d.bookings;
            this.reviews = d.reviews;
            return this;
        }

        public DJ build() { return new DJ(this); }
    }
}