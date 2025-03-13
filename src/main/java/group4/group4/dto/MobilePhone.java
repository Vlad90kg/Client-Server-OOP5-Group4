package group4.group4.dto;

public class MobilePhone {
    private int id;
    private int brand_id;
    private String model;
    private int quantity;
    private double price;

    public MobilePhone(double price) {
        this.price = price;
    }

    public MobilePhone(int id, int brand_id, String model, int quantity, double price) {
        this.id = id;
        this.brand_id = brand_id;
        this.model = model;
        this.quantity = quantity;
        this.price = price;
    }

    public MobilePhone(int brand_id, String model, int quantity, double price) {
        this.brand_id = brand_id;
        this.model = model;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "MobilePhone{" +
                " id='" + id + '\'' +
                " model='" + model + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
