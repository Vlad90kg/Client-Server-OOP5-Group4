package group4.group4.server.dto;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class MobilePhone implements Serializable {
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

    public MobilePhone(JSONObject jsonObject) {
        this.id = jsonObject.getInt("id");
        this.brand_id = jsonObject.getInt("brand_id");
        this.model = jsonObject.getString("model");
        this.quantity = jsonObject.getInt("quantity");
        this.price = jsonObject.getDouble("price");
    }

    public MobilePhone() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrandId() {
        return brand_id;
    }

    public void setBrandId(int brand_id) {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MobilePhone)) return false;
        MobilePhone that = (MobilePhone) o;
        return id == that.id &&
                brand_id == that.brand_id &&
                quantity == that.quantity &&
                Double.compare(that.price, price) == 0 &&
                Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, brand_id, model, quantity, price);
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
