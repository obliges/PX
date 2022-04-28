package project.px.entity;

/**
 * A => Level A mart can handle Level A, B, C product
 * B => Level B mart can handle Level B, C product
 * C => Level C mart can handle Level C Product
 */

public enum MartLevel {
    A("A"), B("B"), C("C");

    private final String description;

    MartLevel(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
