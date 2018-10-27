package cn.yyfullstack.chapter2.web.controller;

import cn.yyfullstack.chapter2.model.Customer;
import cn.yyfullstack.chapter2.service.ICustomerService;
import cn.yyfullstack.chapter2.service.impl.CustomerServiceImpl;
import cn.yyfullstack.chapter2.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

    private ICustomerService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Customer> customerList = service.getCustomerList();
        req.setAttribute("customerList", customerList);
        req.getRequestDispatcher("/view/customer/index.jsp").forward(req, resp);
    }

    @Override
    public void init() throws ServletException {
        service = new CustomerServiceImpl();
    }
}
