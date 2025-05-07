package ru.miniprog.minicrmapp.users.internal.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ru.miniprog.minicrmapp.users.internal.model.Role;
import ru.miniprog.minicrmapp.users.internal.model.UserCrm;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<UserCrm> userRowMapper = (rs, rowNum) -> {
        UserCrm user = new UserCrm();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(Role.valueOf(rs.getString("role")));
        return user;
    };
    
    public RowMapper<UserCrm> getUserRowMapper() {
        return userRowMapper;
    }

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<UserCrm> findByUsername(String username) {
        String sql = "SELECT * FROM user_crm WHERE username = ?";
        List<UserCrm> users = jdbcTemplate.query(sql, userRowMapper, username);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM user_crm WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM user_crm WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public UserCrm save(UserCrm user) {
        if (user.getId() == null) {
            String sql = "INSERT INTO user_crm (username, password, email, role) VALUES (?, ?, ?, ?) RETURNING id";
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getRole().name());
            user.setId(id);
        } else {
            String sql = "UPDATE user_crm SET username = ?, password = ?, email = ?, role = ? WHERE id = ?";
            jdbcTemplate.update(sql,
                    user.getUsername(),
                    user.getPassword(),
                    user.getEmail(),
                    user.getRole().name(),
                    user.getId());
        }
        return user;
    }

    public List<UserCrm> findAll() {
        String sql = "SELECT * FROM user_crm";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public Optional<UserCrm> findById(Long id) {
        String sql = "SELECT * FROM user_crm WHERE id = ?";
        List<UserCrm> users = jdbcTemplate.query(sql, userRowMapper, id);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM user_crm WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}