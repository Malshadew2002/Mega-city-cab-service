package com.example.cabServiceMegaCity.models;

public class BookingModel {
	private int bookID;
    private int userID;
    private int cabID;
    private String userFullName;
    private String userContactNumber;
    private String driverOption;
    private String Dates;
    private String bookingDate;
    private double totalAmount;
    public int getBookID() {
		return bookID;
	}
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getCabID() {
		return cabID;
	}
	public void setCabID(int cabID) {
		this.cabID = cabID;
	}
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	public String getUserContactNumber() {
		return userContactNumber;
	}
	public void setUserContactNumber(String userContactNumber) {
		this.userContactNumber = userContactNumber;
	}
	public String getDriverOption() {
		return driverOption;
	}
	public void setDriverOption(String driverOption) {
		this.driverOption = driverOption;
	}
	public String getDates() {
		return Dates;
	}
	public void setDates(String dates) {
		Dates = dates;
	}
	public String getBookingDate() {
		return bookingDate;
	}
	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
}
