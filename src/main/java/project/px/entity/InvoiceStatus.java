package project.px.entity;

public enum InvoiceStatus {
    DELIVERING("Delivering"), ARRIVED("Arrived"), CANCELLED("Cancelled");

    private final String description;

    InvoiceStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
