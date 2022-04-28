package project.px.entity;

/**
 * CONTRACTED => The Contraction of product is valid.
 * CANCELLED => The contraction of product is cancelled.
 */

public enum ContractStatus {
    CONTRACTED("Contracted"), CANCELLED("Cancelled");

    private final String description;

    ContractStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
