package cn.yyfullstack.chapter2.web.controller;

import cn.yyfullstack.chapter2.model.Customer;
import cn.yyfullstack.chapter2.service.ICustomerService;
import cn.yyfullstack.chapter2.service.impl.CustomerServiceImpl;
import cn.yyfullstack.chapter2.util.CastUtil;
import cn.yyfullstack.chapter2.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/customer_edit")
public class CustomerEditServlet extends HttpServlet {

    public static final Logger LOGGER = LoggerFactory.getLogger(CustomerEditServlet.class);

    private ICustomerService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idStr = req.getParameter("id");
        if (StringUtil.isNotEmpty(idStr)) {
            Customer customer = service.getCustomer(Long.parseLong(idStr));
            req.setAttribute("customer", customer);
            req.getRequestDispatcher("/view/customer/edit.jsp").forward(req, resp);
        } else {
            LOGGER.error("execute doGet failure without id");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> filedMap = new HashMap<String, Object>();
        String idStr = req.getParameter("id");
        if (StringUtil.isNotEmpty(idStr)) {
            filedMap.put("name", req.getParameter("name"));
            filedMap.put("contact", req.getParameter("contact"));
            filedMap.put("telephone", req.getParameter("telephone"));
            filedMap.put("email", req.getParameter("email"));
            filedMap.put("remark", req.getParameter("remark"));
            if (service.updateCustomer(Long.parseLong(idStr), filedMap)) {
                resp.sendRedirect(req.getContextPath() + "/customer");
            }
        } else {
            LOGGER.error("execute doPost failure without id");
        }
    }

    @Override
    public void init() throws ServletException {
        service = new CustomerServiceImpl();
    }
}
