package oktenweb.models;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity(name = "Avatars")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString(exclude = "restaurant")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    String image;
    String description;

    @ManyToOne(cascade = CascadeType.DETACH,
            fetch = FetchType.LAZY)
    Restaurant restaurant;

}
