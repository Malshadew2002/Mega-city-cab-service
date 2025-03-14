package com.example.cabServiceMegaCity.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.cabServiceMegaCity.models.CabModel;

@Repository
public class CabServices {
	@Autowired
    private JdbcTemplate jdbcTemplate;
	
    public boolean isCabNumberExist(String cabNumber) {
        String query = "SELECT COUNT(*) FROM cabs WHERE cabNumber = ?";
        Integer result = jdbcTemplate.queryForObject(query, Integer.class, cabNumber);
        return result != null && result > 0;
    }
    
    public CabModel addCab(CabModel cab) {
        String insertQuery = "INSERT INTO cabs (cabNumber, model, seats, category, ownerContact, perDayAmount, image) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?)";

        int rowsAffected = jdbcTemplate.update(insertQuery, 
                cab.getCabNumber(), cab.getModel(), cab.getSeats(), 
                cab.getCategory(), cab.getOwnerContact(), cab.getPerDayAmount(), cab.getImage());

        if (rowsAffected > 0) {
            String fetchIdQuery = "SELECT LAST_INSERT_ID()";
            Integer cabID = jdbcTemplate.queryForObject(fetchIdQuery, Integer.class);
            cab.setId(cabID);
            return cab;
        }
        return null;
    }
    @SuppressWarnings("deprecation")
    public CabModel getCab(int cabID) {
        String sql = "SELECT * FROM cabs WHERE id = ?";
        CabModel cab = null;

        try {
            cab = jdbcTemplate.queryForObject(sql, new Object[]{cabID}, (rs, rowNum) -> {
                CabModel c = new CabModel();
                c.setId(rs.getInt("id"));
                c.setCabNumber(rs.getString("cabNumber"));
                c.setModel(rs.getString("model"));
                c.setSeats(rs.getInt("seats"));
                c.setCategory(rs.getString("category"));
                c.setOwnerContact(rs.getString("ownerContact"));
                c.setPerDayAmount(rs.getDouble("perDayAmount"));
                c.setImage(rs.getString("image"));
                return c;
            });
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }

        return cab;
    }

    public List<CabModel> getAllCabs() {
        String sql = "SELECT * FROM cabs";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CabModel cab = new CabModel();
            cab.setId(rs.getInt("id"));
            cab.setCabNumber(rs.getString("cabNumber"));
            cab.setModel(rs.getString("model"));
            cab.setSeats(rs.getInt("seats"));
            cab.setCategory(rs.getString("category"));
            cab.setOwnerContact(rs.getString("ownerContact"));
            cab.setPerDayAmount(rs.getDouble("perDayAmount"));
            cab.setImage(rs.getString("image"));
            return cab;
        });
    }
    
    @SuppressWarnings("deprecation")
	public CabModel getCabByCabNumber(String cabNumber) {
        String query = "SELECT * FROM cabs WHERE cabNumber = ?";
        try {
            return jdbcTemplate.queryForObject(query, new Object[]{cabNumber}, (rs, rowNum) -> {
                CabModel cab = new CabModel();
                cab.setId(rs.getInt("id"));
                cab.setCabNumber(rs.getString("cabNumber"));
                cab.setModel(rs.getString("model"));
                cab.setSeats(rs.getInt("seats"));
                cab.setCategory(rs.getString("category"));
                cab.setOwnerContact(rs.getString("ownerContact"));
                cab.setPerDayAmount(rs.getDouble("perDayAmount"));
                cab.setImage(rs.getString("image"));
                return cab;
            });
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }
    }



    public boolean updateCab(CabModel cab) {
        String updateQuery = "UPDATE cabs SET cabNumber = ?, model = ?, seats = ?, category = ?, ownerContact = ?, perDayAmount = ?, image = ? WHERE id = ?";

        int rowsAffected = jdbcTemplate.update(updateQuery, 
                cab.getCabNumber(), cab.getModel(), cab.getSeats(), 
                cab.getCategory(), cab.getOwnerContact(), cab.getPerDayAmount(), cab.getImage(), cab.getId());

        return rowsAffected > 0;
    }

    public boolean deleteCab(int cabId) {
        String deleteQuery = "DELETE FROM cabs WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(deleteQuery, cabId);
        return rowsAffected > 0;
    }
    
    public int getCabsCount() {
        String sql = "SELECT COUNT(*) FROM cabs";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
    
}
