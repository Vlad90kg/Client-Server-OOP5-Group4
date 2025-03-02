package group4.group4.dao;

import group4.group4.dto.MobilePhone;

import java.util.List;

public interface DaoMobilePhone extends Dao<MobilePhone> {
    List<MobilePhone> findPhoneByFilter(MobilePhone mobilePhone);
}
