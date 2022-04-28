package project.px.entity;

/**
 * HIGH => high demand
 * MIDDLE => middle demand
 * LOW => low demand
 */

public enum DemandStatus {
    HIGH("High"), MIDDLE("Middle"), LOW("Low");

    private final String description;

    DemandStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
