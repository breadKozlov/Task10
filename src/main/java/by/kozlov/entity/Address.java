package by.kozlov.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "addresses",schema = "public")
@ToString(exclude = "persons")
@EqualsAndHashCode(exclude = "persons")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String city;
    String street;
    Integer building;
    @OneToMany(mappedBy = "address",cascade = CascadeType.ALL)
    @Builder.Default
    Set<Person> persons = new HashSet<>();

    public void addPerson(Person person) {
        persons.add(person);
        person.setAddress(this);
    }
}
