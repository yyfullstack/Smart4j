package cn.yyfullstack.chapter2.dao.impl;


import cn.yyfullstack.chapter2.dao.ICustomerDao;
import cn.yyfullstack.chapter2.helper.DatabaseHelper;
import cn.yyfullstack.chapter2.model.Customer;

import java.util.List;
import java.util.Map;

/**
 * Created by yong on 2016/10/31 0031.
 */
public class CustomerDaoImpl implements ICustomerDao {

    @Override
    public List<Customer> getCustomerList() {
        String sql = "SELECT * FROM " + DatabaseHelper.getTableName(Customer.class);
        return DatabaseHelper.queryEntityList(Customer.class, sql);
    }

    @Override
    public Customer getCustomer(long id) {
        String sql = "SELECT * FROM " + DatabaseHelper.getTableName(Customer.class) + " where id=?";
        return DatabaseHelper.queryEntity(Customer.class, sql, id);
    }

    @Override
    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    @Override
    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    @Override
    public boolean deleteCustomer(long id) {
        return DatabaseHelper.deleteEntity(Customer.class, id);
    }
}
