package models;

public enum PaginationParams {
    PAGE("page"),
    PAGE_SIZE("pageSize");

    private final String text;

    PaginationParams(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
