package pl.nbd.entities;

public class PremiumClient extends Client {

    private int membershipLevel;
    private double discount;

    public PremiumClient(String firstName, String lastName, String personalId, int membershipLevel, double discount) {
        super(firstName, lastName, personalId);
        this.membershipLevel = membershipLevel;
        this.discount = discount;
    }

    public PremiumClient() {

    }


    @Override
    public double applyDiscount(double price) {
        return this.discount / membershipLevel;
    }
}
