package group4.group4.server;


import group4.group4.server.dto.Brand;
import group4.group4.server.dto.MobilePhone;
import group4.group4.server.dto.Specifications;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import java.util.List;

public class JsonConverter {
    // Feature 7
    public String phonesListJson(List<MobilePhone> phonesList){
        if(phonesList == null){
            System.out.println("PhonesList is null");
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (MobilePhone mobilePhone : phonesList) {
            JSONObject jsonObject = serializeMobilePhone(mobilePhone);
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    // Feature 8
    public String phoneToJson(MobilePhone phone) {
        if (phone == null) {
            return "Phone ID has not been found";
        }
        else {
            JSONObject jsonObject = serializeMobilePhone(phone);

            return jsonObject.toString();
        }
    }

    public JSONObject serializeMobilePhone(MobilePhone phone) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", phone.getId());
        jsonObject.put("brand_id", phone.getBrandId());
        jsonObject.put("model", phone.getModel());
        jsonObject.put("quantity", phone.getQuantity());
        jsonObject.put("price", phone.getPrice());
        return jsonObject;
    }

    public JSONObject serializeSpecifications(MobilePhone mobilePhone) {
        if (mobilePhone == null) {
            System.out.println("MobilePhone is null");
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        Specifications spec = mobilePhone.getSpecifications();
        jsonObject.put("phone_id", mobilePhone.getId());
        jsonObject.put("storage", spec.getStorage());
        jsonObject.put("chipset", spec.getChipset());

        return jsonObject;
    }


    public String brandsListJson(List<Brand> brandsList){

        if(brandsList == null){
            System.out.println("Brands list is null");
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (Brand brand : brandsList) {
            JSONObject jsonObject = serializeBrand(brand);
            System.out.println(jsonObject);
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    // Feature 8
    public String brandToJson(Brand brand) {
        if (brand == null) {
            return "Phone ID has not been found";
        }
        else {
            JSONObject jsonObject = serializeBrand(brand);

            return jsonObject.toString();
        }
    }

    public JSONObject serializeBrand(Brand brand) {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", brand.getId());
        jsonObject.put("name", brand.getName());
        jsonObject.put("description", brand.getDescription());

        System.out.println("brand in converter");
        System.out.println(jsonObject);
        return jsonObject;
    }

}
