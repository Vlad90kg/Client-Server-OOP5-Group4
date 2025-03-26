package group4.group4.server;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dao.Dao;
import group4.group4.server.dao.DaoMobilePhone;
import group4.group4.server.dto.MobilePhone;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.List;

public class JsonConverter {
    DaoMobilePhone daoMobilePhone;
    String phonesListJson(List<MobilePhone> phonesList) throws DaoException {
        JSONArray jsonArray = new JSONArray(phonesList);
        return jsonArray.toString();
    }

    public List<MobilePhone> getAllPhones(DaoMobilePhone daoMobilePhone) throws DaoException {
        // Feature 1
        List<MobilePhone> list = daoMobilePhone.getAll();
        for (MobilePhone mobilePhone : list) {
            System.out.println(mobilePhone);
        }
        return list;
    }
}
