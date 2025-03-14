package com.example.cabServiceMegaCity.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.example.cabServiceMegaCity.models.BookingModel;
import com.example.cabServiceMegaCity.models.CabModel;

@Repository
public class BookingServices {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // Get a booking by its ID
    public BookingModel getBooking(int bookingID) {
        String sql = "SELECT * FROM Booking WHERE bookID=?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, bookingID);

        if (rows.next()) {
            BookingModel booking = new BookingModel();
            booking.setBookID(rows.getInt("bookID"));
            booking.setUserID(rows.getInt("userID"));
            booking.setCabID(rows.getInt("cabID"));
            booking.setUserFullName(rows.getString("userFullName"));
            booking.setUserContactNumber(rows.getString("userContactNumber"));
            booking.setDriverOption(rows.getString("driverOption"));
            booking.setDates(rows.getString("Dates"));
            booking.setBookingDate(rows.getString("bookingDate"));
            booking.setTotalAmount(rows.getDouble("totalAmount"));
            return booking;
        }
        return null;
    }

    public BookingModel newBooking(BookingModel booking) {
        String sql = "INSERT INTO Booking (userID, cabID, userFullName, userContactNumber, driverOption, Dates, bookingDate, totalAmount) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        int count = jdbcTemplate.update(sql, 
            booking.getUserID(), booking.getCabID(), booking.getUserFullName(), 
            booking.getUserContactNumber(), booking.getDriverOption(), 
            booking.getDates(), booking.getBookingDate(), booking.getTotalAmount());

        if (count > 0) {
            int id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
            return getBooking(id);
        }
        return null; 
    }


    @SuppressWarnings("deprecation")
	public List<CabModel> getAvailableCabs(String category, List<String> dates) {
        // Convert the list of dates into a SQL LIKE condition to check for overlapping dates
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Cabs WHERE category = ? AND id NOT IN ( " +
                "SELECT cabID FROM Booking WHERE ");

        for (int i = 0; i < dates.size(); i++) {
            if (i > 0) {
                sqlBuilder.append(" OR ");
            }
            sqlBuilder.append(" FIND_IN_SET(?, Dates) > 0 "); // FIND_IN_SET checks if a date exists in the comma-separated list
        }

        sqlBuilder.append(")");

        // Create parameters
        Object[] params = new Object[dates.size() + 1];
        params[0] = category;
        for (int i = 0; i < dates.size(); i++) {
            params[i + 1] = dates.get(i);
        }

        // Execute the query
        return jdbcTemplate.query(sqlBuilder.toString(), params, (rs, rowNum) -> {
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
    public List<BookingModel> getBookingsByUserID(int userID) {
        String sql = "SELECT * FROM Booking WHERE userID = ?";
        
        return jdbcTemplate.query(sql, new Object[]{userID}, (rs, rowNum) -> {
            BookingModel booking = new BookingModel();
            booking.setBookID(rs.getInt("bookID"));
            booking.setUserID(rs.getInt("userID"));
            booking.setCabID(rs.getInt("cabID"));
            booking.setUserFullName(rs.getString("userFullName"));
            booking.setUserContactNumber(rs.getString("userContactNumber"));
            booking.setDriverOption(rs.getString("driverOption"));
            booking.setDates(rs.getString("Dates"));
            booking.setBookingDate(rs.getString("bookingDate"));
            booking.setTotalAmount(rs.getDouble("totalAmount"));
            return booking;
        });
    }


    public boolean deleteBooking(int bookingID) {
        String sql = "DELETE FROM Booking WHERE bookID = ?";
        int count = jdbcTemplate.update(sql, bookingID);
        return count > 0;
    }
    
    public List<BookingModel> getAllBookings() {
        String sql = "SELECT * FROM booking";
        List<BookingModel> bookings = new ArrayList<>();
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql);

        while (rows.next()) {
        	BookingModel booking = new BookingModel();
            booking.setBookID(rows.getInt("bookID"));
            booking.setUserID(rows.getInt("userID"));
            booking.setCabID(rows.getInt("cabID"));
            booking.setUserFullName(rows.getString("userFullName"));
            booking.setUserContactNumber(rows.getString("userContactNumber"));
            booking.setDriverOption(rows.getString("driverOption"));
            booking.setDates(rows.getString("Dates"));
            booking.setBookingDate(rows.getString("bookingDate"));
            booking.setTotalAmount(rows.getDouble("totalAmount"));
            bookings.add(booking);
        }
        return bookings;
    }
    
    public int getBookingCount() {
        String sql = "SELECT COUNT(*) FROM Booking";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
