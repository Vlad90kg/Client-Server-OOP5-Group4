package group4.group4.server;


import group4.group4.server.dto.MobilePhone;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class JsonConverter {
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


}
