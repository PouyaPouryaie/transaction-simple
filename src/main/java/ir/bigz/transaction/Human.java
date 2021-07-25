package ir.bigz.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.*;

@Data
@NoArgsConstructor
@ToString
@Entity
public class Human {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Version //this check, entity must be persist or merge
    private Long version;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private Integer age;
    private String nationalCode;
}
