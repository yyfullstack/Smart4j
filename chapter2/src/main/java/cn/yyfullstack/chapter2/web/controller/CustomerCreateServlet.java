package cn.yyfullstack.chapter2.web.controller;

import cn.yyfullstack.chapter2.service.ICustomerService;
import cn.yyfullstack.chapter2.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/customer_create")
public class CustomerCreateServlet extends HttpServlet {

    private ICustomerService service;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/view/customer/create.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, Object> filedMap = new HashMap<String, Object>();
        filedMap.put("name", req.getParameter("name"));
        filedMap.put("contact", req.getParameter("contact"));
        filedMap.put("telephone", req.getParameter("telephone"));
        filedMap.put("email", req.getParameter("email"));
        filedMap.put("remark", req.getParameter("remark"));
        if (service.createCustomer(filedMap)) {
            resp.sendRedirect(req.getContextPath() + "/customer");
        }
    }

    @Override
    public void init() throws ServletException {
        service = new CustomerServiceImpl();
    }
}
