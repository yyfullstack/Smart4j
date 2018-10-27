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

@WebServlet("/customer_show")
public class CustomerShowServlet extends HttpServlet {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomerShowServlet.class);

    private ICustomerService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (StringUtil.isNotEmpty(idStr)) {
            Customer customer = service.getCustomer(Long.parseLong(idStr));
            req.setAttribute("customer", customer);
            req.getRequestDispatcher("/view/customer/show.jsp").forward(req, resp);
        } else {
            LOGGER.error("execute doGet failure without id");
        }
    }

    @Override
    public void init() throws ServletException {
        service = new CustomerServiceImpl();
    }
}
