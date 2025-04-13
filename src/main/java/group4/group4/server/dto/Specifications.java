package group4.group4.server.dto;

import org.json.JSONObject;

import java.util.Objects;

public class Specifications {
    int phone_id;
    String storage;
    String chipset;

    public int getPhone_id() {
        return phone_id;
    }

    public void setPhone_id(int phone_id) {
        this.phone_id = phone_id;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getChipset() {
        return chipset;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }


    public Specifications(int phone_id, String storage, String chipset) {
        this.phone_id = phone_id;
        this.storage = storage;
        this.chipset = chipset;
    }
    public Specifications(String storage, String chipset) {
        this.storage = storage;
        this.chipset = chipset;
    }
    public Specifications(){
    }

    public Specifications(JSONObject jsonObject) {
        this.phone_id = jsonObject.getInt("phone_id");
        this.storage = jsonObject.getString("storage");
        this.chipset = jsonObject.getString("chipset");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Specifications that)) return false;
        return phone_id == that.phone_id && Objects.equals(storage, that.storage) && Objects.equals(chipset, that.chipset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone_id, storage, chipset);
    }

    @Override
    public String toString() {
        return "Specifications{" +
                "phone_id=" + phone_id +
                ", storage='" + storage + '\'' +
                ", chipset='" + chipset + '\'' +
                '}';
    }
}
