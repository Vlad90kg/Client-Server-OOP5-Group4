package group4.group4.server.dto;

import org.json.JSONObject;

import java.util.Objects;

public class Brand {
    String name;
    String description;

    public Brand(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Brand(JSONObject json) {
        this.name = json.getString("name");
        this.description = json.getString("description");
    }

    public Brand() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Brand brand)) return false;
        return Objects.equals(name, brand.name) && Objects.equals(description, brand.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "Brand{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
