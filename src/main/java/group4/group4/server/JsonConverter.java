package group4.group4.server;


import group4.group4.server.dto.MobilePhone;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import java.util.List;

public class JsonConverter {
    // Feature 7
    String phonesListJson(List<MobilePhone> phonesList){
        if(phonesList == null){
            System.out.println("PhonesList is null");
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (MobilePhone mobilePhone : phonesList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", mobilePhone.getId());
            jsonObject.put("brandId", mobilePhone.getBrandId());
            jsonObject.put("model", mobilePhone.getModel());
            jsonObject.put("quantity", mobilePhone.getQuantity());
            jsonObject.put("price", mobilePhone.getPrice());
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    // Feature 8
    String phoneToJson(MobilePhone phone) {
        if (phone == null) {
            System.out.println("Phone is null");
            return null;
        }
        else {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("id", phone.getId());
            jsonObject.put("brandId", phone.getBrandId());
            jsonObject.put("model", phone.getModel());
            jsonObject.put("quantity", phone.getQuantity());
            jsonObject.put("price", phone.getPrice());

            return jsonObject.toString();
        }
    }
}
