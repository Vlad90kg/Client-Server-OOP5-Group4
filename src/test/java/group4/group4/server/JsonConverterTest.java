package group4.group4.server;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dao.DaoMobilePhone;
import group4.group4.server.dto.MobilePhone;
import org.json.JSONArray;
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
            MobilePhone phone1 = new MobilePhone(256);
            MobilePhone phone2 = new MobilePhone(512);
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
        System.out.println(new JSONArray(dummyList));
        assertEquals(dummyList.getFirst().toString(), jsonArray.get(0));
        assertEquals(dummyList.get(1).toString(), jsonArray.get(1));

    }


    @Test
    public void testConvertNullList() {
        String nullString = jsonConverter.phonesListJson(null);
        assertNull(nullString);
    }


    @Test
    public void testEmptyList() {
        String emptyString = jsonConverter.phonesListJson(new ArrayList<>());
        assertEquals("[]", emptyString);
    }




}