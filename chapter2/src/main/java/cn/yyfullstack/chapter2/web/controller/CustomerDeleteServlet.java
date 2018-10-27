package cn.yyfullstack.chapter2.web.controller;

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

@WebServlet("/customer_delete")
public class CustomerDeleteServlet extends HttpServlet {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomerDeleteServlet.class);

    private ICustomerService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (StringUtil.isNotEmpty(idStr)) {
            if (service.deleteCustomer(Long.parseLong(idStr))) {
                resp.sendRedirect(req.getContextPath() + "/customer");
            }
        } else {
            LOGGER.error("execute doGet failure without id");
        }
    }

    @Override
    public void init() throws ServletException {
        service = new CustomerServiceImpl();
    }
}
