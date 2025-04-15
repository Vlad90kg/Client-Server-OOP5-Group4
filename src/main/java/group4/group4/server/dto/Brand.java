package group4.group4.server.dto;

import org.json.JSONObject;

import java.util.Objects;

public class Brand {
    int id;
    String name;
    String description;

    public Brand(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
