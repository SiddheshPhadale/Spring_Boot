package Siddhesh.Banking_App.repositories;

import Siddhesh.Banking_App.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class AccountRepository {

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    public AccountRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<User> userRowMapper = new RowMapper<>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new User(
                    rs.getLong("Id"),
                    rs.getString("Username"),
                    rs.getString("Password"),
                    rs.getDouble("Balance"),
                    rs.getString("Role")
            );
        }
    };

    public User findByUsername(String username){
        String sql = "SELECT * FROM user WHERE Username = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, username);
    }

    public User getReferenceById(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, id);
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public int save(User user) {
        String sql = "INSERT INTO user (Id, Username, Password, Balance, Role) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, user.getId(), user.getUsername(), user.getPassword(), user.getBalance(), user.getRole());
    }

    public int update(User user) {
        String sql = "UPDATE user SET Username = ?, Password = ?, Balance = ? WHERE id = ?";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getBalance(), user.getId());
    }

    public int delete(Long id) {
        String sql = "DELETE FROM user WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
