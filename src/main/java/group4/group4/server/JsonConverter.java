package group4.group4.server;


import group4.group4.server.dto.MobilePhone;
import org.json.JSONArray;
import java.util.List;

public class JsonConverter {
    String phonesListJson(List<MobilePhone> phonesList){
        if(phonesList == null){
            System.out.println("PhonesList is null");
            return null;
        }
        JSONArray jsonArray = new JSONArray();
        for (MobilePhone mobilePhone : phonesList) {
            jsonArray.put(mobilePhone);
        }
        return jsonArray.toString();
    }


}
