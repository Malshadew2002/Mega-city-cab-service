package com.example.cabServiceMegaCity.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.cabServiceMegaCity.models.UserModel;

@Repository
public class UserServices {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public boolean isNicExists(String nic) {
        String query = "SELECT COUNT(*) FROM users WHERE nic = ?";
        Integer result = jdbcTemplate.queryForObject(query, Integer.class, nic);
        return result != null && result > 0;
    }

    public boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer result = jdbcTemplate.queryForObject(query, Integer.class, username);
        return result != null && result > 0;
    }

    public boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer result = jdbcTemplate.queryForObject(query, Integer.class, email);
        return result != null && result > 0;
    }

    public UserModel addUser(UserModel user) {
        String insertQuery = "INSERT INTO users (name, address, nic, email, username, password) " +
                             "VALUES (?, ?, ?, ?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(insertQuery, 
                user.getName(), user.getAddress(), 
                user.getNic(), user.getEmail(), user.getUsername(), user.getPassword());

        if (rowsAffected > 0) {
            String fetchIdQuery = "SELECT LAST_INSERT_ID()";
            Integer userID = jdbcTemplate.queryForObject(fetchIdQuery, Integer.class);
            user.setID(userID);
            return user;
        }
        return null; 
    }
    
    @SuppressWarnings("deprecation")
	public UserModel getUser(int userID) {
        String sql = "SELECT * FROM users WHERE id = ?";
        UserModel user = null;

        try {
            user = jdbcTemplate.queryForObject(sql, new Object[]{userID}, (rs, rowNum) -> {
                UserModel u = new UserModel();
                u.setID(rs.getInt("id"));
                u.setName(rs.getString("name"));
                u.setAddress(rs.getString("address"));
                u.setNic(rs.getString("nic"));
                u.setEmail(rs.getString("email"));
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                return u;
            });
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }

        return user;
    }

    public List<UserModel> getAllUsers() {
        String sql = "SELECT * FROM users";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            UserModel user = new UserModel();
            user.setID(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setAddress(rs.getString("address"));
            user.setNic(rs.getString("nic"));
            user.setEmail(rs.getString("email"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }
    
    @SuppressWarnings("deprecation")
	public UserModel getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username}, (rs, rowNum) -> {
                UserModel user = new UserModel();
                user.setID(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setAddress(rs.getString("address"));
                user.setNic(rs.getString("nic"));
                user.setEmail(rs.getString("email"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                return user;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    
    public boolean updateUser(UserModel user) {

        String updateQuery = "UPDATE users SET name = ?, address = ?, nic = ?, email = ?, username = ?, password = ? WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(updateQuery, 
                user.getName(), user.getAddress(), 
                user.getNic(), user.getEmail(), 
                user.getUsername(), user.getPassword(), user.getID());

        System.out.println("Rows affected: " + rowsAffected);
        return rowsAffected > 0;
    }
    
    public int getUserCount() {
        String sql = "SELECT COUNT(*) FROM users";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public boolean deleteUser(int userID) {
        String sql = "DELETE FROM users WHERE id = ?";
        int count = jdbcTemplate.update(sql, userID);
        return count > 0;
    }
}
