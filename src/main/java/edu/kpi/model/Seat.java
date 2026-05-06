package edu.kpi.model;

public class Seat {
    private int number;
    private boolean isBooked;

    public Seat(int number, boolean isBooked) {
        this.number = number;
        this.isBooked = isBooked;
    }

    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }
}
