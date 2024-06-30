package src.Entity;

public enum Currency {
    USD("USD"), VND("VND");

    private String currency;
    Currency(String currency) {
        this.currency = currency;
    }
    public String getCurrency() {
        return currency;
}
}
