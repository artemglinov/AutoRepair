package model;

import java.time.LocalDate;

public class Order {
    private String description;
    private long client_id;
    private long mechanic_id;
    private LocalDate creation;
    private LocalDate finish;
    private double cost;
    private long id;
    private Status status;


    public enum Status {
        PLANNED,
        EXECUTED,
        ACCEPTED,
        NOT_DEFINED
    }

    // Non-static version of mapping String to Order.Status

    public String getStatusFromString() {

        if (this.status == null) {
            return null;
        }
        switch (this.status) {
            case EXECUTED:
                return "Выполнен";
            case ACCEPTED:
                return "Принят клиентом";
            case PLANNED:
                return "Запланирован";
        }

        return "Статус не определён";
    }

    // Static version

    public static String statusToString(Status status) {
        if (status == null) {
            return null;
        }
        switch (status) {
            case EXECUTED:
                return "Выполнен";
            case ACCEPTED:
                return "Принят клиентом";
            case PLANNED:
                return "Запланирован";
            default:
                return null;
        }
    }

    public static Status stringToStatus(String s) {
        if (s == null) {
            return Status.NOT_DEFINED;
        }

        switch (s) {
            case "Выполнен":
                return Status.EXECUTED;
            case "Принят клиентом":
                return Status.ACCEPTED;
            case "Запланирован":
                return Status.PLANNED;
        }

        return Status.NOT_DEFINED;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClient_id() {
        return client_id;
    }

    public void setClient_id(long client_id) {
        this.client_id = client_id;
    }

    public long getMechanic_id() {
        return mechanic_id;
    }

    public void setMechanic_id(long mechanic_id) {
        this.mechanic_id = mechanic_id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreation() {
        return creation;
    }

    public void setCreation(LocalDate creation) {
        this.creation = creation;
    }

    public LocalDate getFinish() {
        return finish;
    }

    public void setFinish(LocalDate finish) {
        this.finish = finish;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
