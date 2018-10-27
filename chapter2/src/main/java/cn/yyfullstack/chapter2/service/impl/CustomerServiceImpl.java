package cn.yyfullstack.chapter2.service.impl;


import cn.yyfullstack.chapter2.dao.ICustomerDao;
import cn.yyfullstack.chapter2.dao.impl.CustomerDaoImpl;
import cn.yyfullstack.chapter2.model.Customer;
import cn.yyfullstack.chapter2.service.ICustomerService;

import java.util.List;
import java.util.Map;

/**
 * Created by yong on 2016/10/31 0031.
 */
public class CustomerServiceImpl implements ICustomerService {

    private ICustomerDao dao = new CustomerDaoImpl();

    @Override
    public List<Customer> getCustomerList() {
        return dao.getCustomerList();
    }

    @Override
    public Customer getCustomer(long id) {
        return dao.getCustomer(id);
    }

    @Override
    public boolean createCustomer(Map<String, Object> fieldMap) {
        return dao.createCustomer(fieldMap);
    }

    @Override
    public boolean updateCustomer(long id, Map<String, Object> fieldMap) {
        return dao.updateCustomer(id, fieldMap);
    }

    @Override
    public boolean deleteCustomer(long id) {
        return dao.deleteCustomer(id);
    }
}
