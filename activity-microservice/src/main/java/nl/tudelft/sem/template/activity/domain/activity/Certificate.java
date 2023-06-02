package nl.tudelft.sem.template.activity.domain.activity;

public enum Certificate {
    C_FOUR("C4"),
    FOUR_PLUS("4+"),
    EIGHT_PLUS("8+");

    private final String certificate;

    Certificate(String certificate) {
        this.certificate = certificate;
    }

    @Override
    public String toString() {
        return certificate;
    }
}
