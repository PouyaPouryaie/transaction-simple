package ir.bigz.transaction;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

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

    public static Function<ResultSet, Human> mapper() {
        return (ResultSet rs) -> {
            Human result = new Human();

            try {
                result.setId(rs.getLong("id"));
            } catch (SQLException ignored) {

            }

            try {
                result.setVersion(rs.getLong("version"));
            } catch (SQLException ignored) {

            }

            try {
                result.setFirstName(rs.getString("first_name"));
            } catch (SQLException ignored) {

            }
            try {
                result.setLastName(rs.getString("last_name"));
            } catch (SQLException ignored) {

            }
            try {
                result.setAge(rs.getInt("age"));
            } catch (SQLException ignored) {

            }
            try {
                result.setNationalCode(rs.getString("national_code"));
            } catch (SQLException ignored) {

            }
            try {
                result.setActive(rs.getBoolean("is_active"));
            } catch (SQLException ignored) {

            }

            return result;
        };
    }

    public static Human mapper(Map<String, Object> data) {

        Human human = new Human();
        human.setId((Long) data.get("id"));
        human.setFirstName((String) data.get("first_name"));
        human.setLastName((String) data.get("last_name"));
        human.setNationalCode((String) data.get("national_code"));
        return human;
    }
}
