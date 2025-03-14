package com.example.cabServiceMegaCity.models;

public class CabModel {
	   	private int id;
	    private String cabNumber;
	    private String model;
	    private int seats;
	    private String category;
	    private String ownerContact;
	    private double perDayAmount;
	    private String image;
	    public String getImage() {
			return image;
		}
		public void setImage(String image) {
			this.image = image;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getCabNumber() {
			return cabNumber;
		}
		public void setCabNumber(String cabNumber) {
			this.cabNumber = cabNumber;
		}
		public String getModel() {
			return model;
		}
		public void setModel(String model) {
			this.model = model;
		}
		public int getSeats() {
			return seats;
		}
		public void setSeats(int seats) {
			this.seats = seats;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public String getOwnerContact() {
			return ownerContact;
		}
		public void setOwnerContact(String ownerContact) {
			this.ownerContact = ownerContact;
		}
		public double getPerDayAmount() {
			return perDayAmount;
		}
		public void setPerDayAmount(double perDayAmount) {
			this.perDayAmount = perDayAmount;
		}
		
}
