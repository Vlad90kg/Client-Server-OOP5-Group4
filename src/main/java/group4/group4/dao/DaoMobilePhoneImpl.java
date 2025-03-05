package group4.group4.dao;

import group4.group4.dto.MobilePhone;

import java.util.List;

public class DaoMobilePhoneImpl implements DaoMobilePhone {

    @Override
    public List<MobilePhone> findPhoneByFilter(MobilePhone mobilePhone) {
        return List.of();
    }

    @Override
    public List<MobilePhone> getAll() {
        return List.of();
    }

    @Override
    public MobilePhone getById(int id) {
        return null;
    }

    @Override
    public MobilePhone insert(MobilePhone mobilePhone) {
        return null;
    }

    @Override
    public void update(int id, MobilePhone mobilePhone) {

    }
}
