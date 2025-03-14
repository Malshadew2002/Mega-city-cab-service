package com.example.cabServiceMegaCity.models;

public class AmountModel {
	private int id;
    private double driverAmount;
    private double tax;
    private double discount;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getDriverAmount() {
		return driverAmount;
	}
	public void setDriverAmount(double driverAmount) {
		this.driverAmount = driverAmount;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
}
