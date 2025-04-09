package group4.group4.server;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dao.DaoMobilePhone;
import group4.group4.server.dto.MobilePhone;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonConverterTest {
    DaoMobilePhone daoMobilePhone = new DaoMobilePhone() {
        @Override
        public List<MobilePhone> getAll() throws DaoException {
            List<MobilePhone> mobilePhones = new ArrayList<>();
            MobilePhone phone1 = new MobilePhone(1,1, "lool",55, 256);
            MobilePhone phone2 = new MobilePhone(2,1, "lool",55, 512);
            mobilePhones.add(phone1);
            mobilePhones.add(phone2);
            return mobilePhones;
        }

        @Override
        public MobilePhone getById(int id) throws DaoException {
            return null;
        }

        @Override
        public MobilePhone insert(MobilePhone mobilePhone) throws DaoException {
            return null;
        }

        @Override
        public int update(int id, MobilePhone mobilePhone) throws DaoException {
            return 0;
        }

        @Override
        public int delete(int id) throws DaoException {
            return 0;
        }

        @Override
        public List<MobilePhone> findByFilter(Comparator<MobilePhone> comparator) throws DaoException {
            return List.of();
        }
    };


    List<MobilePhone> dummyList = daoMobilePhone.getAll();
    JsonConverter jsonConverter = new JsonConverter();
    JsonConverterTest() throws DaoException {

    }

    @Test
    void phonesListJsonSizeCheck()  {
        String jsonResult = jsonConverter.phonesListJson(dummyList);

        JSONArray jsonArray = new JSONArray(jsonResult);

        assertEquals(dummyList.size(), jsonArray.length());

    }

    @Test
    void comparingListJsonContent()  {
        String jsonResult = jsonConverter.phonesListJson(dummyList);
        JSONArray jsonArray = new JSONArray(jsonResult);

        System.out.println(jsonResult);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            assertEquals(new MobilePhone(jsonObject), dummyList.get(i));
        }

    }


    @Test
    public void feature7() {
        ArrayList<MobilePhone> mobilePhones = new ArrayList<>();
        MobilePhone phone1 = new MobilePhone(1,1, "lool",55, 256);
        MobilePhone phone2 = new MobilePhone(2,1, "lool",55, 512);
        mobilePhones.add(phone1);
        mobilePhones.add(phone2);

        String nullString = jsonConverter.phonesListJson(null);
        String emptyString = jsonConverter.phonesListJson(new ArrayList<>());
        String list = jsonConverter.phonesListJson(mobilePhones);

        assertNull(nullString);
        assertEquals("[]", emptyString);
        assertEquals("[{\"quantity\":55,\"price\":256,\"brandId\":1,\"model\":\"lool\",\"id\":1},{\"quantity\":55,\"price\":512,\"brandId\":1,\"model\":\"lool\",\"id\":2}]", list);
    }


    @Test
    public void feature8() {
        String nullString = jsonConverter.phoneToJson(null);
        String emptyString = jsonConverter.phoneToJson(new MobilePhone());
        String phoneString = jsonConverter.phoneToJson(dummyList.get(0));

        assertNull(nullString);
        assertEquals("{\"quantity\":0,\"price\":0,\"brandId\":0,\"id\":0}", emptyString);
        assertEquals("{\"quantity\":55,\"price\":256,\"brandId\":1,\"model\":\"lool\",\"id\":1}", phoneString);
    }
}
