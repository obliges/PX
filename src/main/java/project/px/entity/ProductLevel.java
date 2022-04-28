package project.px.entity;

/**
 * A => The product that only can be handled by the mart that is located outside of the site of military.
 * B => Health Functional Food that mart can handle only when mart get permission to sell.
 * C => All Mart can handle.
 */

public enum ProductLevel {
    A("A"), B("B"), C("C");

    private final String description;

    ProductLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
