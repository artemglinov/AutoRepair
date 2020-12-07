package model;

public class Mechanic extends Person {
    private int hourly_payment;

    public int getHourly_payment() {
        return hourly_payment;
    }

    public void setHourly_payment(int hourly_payment) {
        this.hourly_payment = hourly_payment;
    }
}
