package group4.group4.server.dao;

import group4.group4.Exceptions.DaoException;
import group4.group4.server.dto.MobilePhone;

import java.util.List;


public interface DaoMobilePhone extends Dao<MobilePhone> {
    public List<MobilePhone> getPhoneByBrand(int brand) throws DaoException;
}
