package com.example.cabServiceMegaCity.services;

import org.springframework.stereotype.Repository;

import com.example.cabServiceMegaCity.models.AmountModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

@Repository
public class AmountServices {
	   @Autowired
	    private JdbcTemplate jdbcTemplate;

	    public AmountModel getAmount(int id) {
	        String sql = "SELECT * FROM amount WHERE id=?";
	        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id);

	        if (rows.next()) {
	            AmountModel amount = new AmountModel();
	            amount.setId(rows.getInt("id"));
	            amount.setDriverAmount(rows.getDouble("driver_amount"));
	            amount.setTax(rows.getDouble("tax"));
	            amount.setDiscount(rows.getDouble("discount"));
	            return amount;
	        }
	        return null;
	    }

	    public void addDefaultAmounts() {
	        String checkSql = "SELECT COUNT(*) FROM amount WHERE id = 1";
	        int count = jdbcTemplate.queryForObject(checkSql, Integer.class);

	        if (count == 0) {
	            String insertSql = "INSERT INTO amount (id, driver_amount, tax, discount) VALUES (1, 0.00, 0.00, 0.00)";
	            jdbcTemplate.update(insertSql);
	        }
	    }

	    public boolean updateAmounts(AmountModel amountModel) {
	        String sql = "UPDATE amount SET driver_amount=?, tax=?, discount=? WHERE id=?";
	        int count = jdbcTemplate.update(sql, 
	            amountModel.getDriverAmount(), amountModel.getTax(), amountModel.getDiscount(), amountModel.getId());

	        return count > 0;
	    }
}
