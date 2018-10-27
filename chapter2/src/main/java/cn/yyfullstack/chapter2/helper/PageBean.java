package cn.yyfullstack.chapter2.helper;

import java.util.List;

public class PageBean {

    private int currentPage;

    private int pageSize;

    private int count; //总记录数

    private int totalPage; //总页数

    private static final int DEFAULT_PAGE_SIZE = 10;

    private static final int DEFAULT_CURRENT_PAGE = 1;

    private List<?> T;

    public PageBean() {
        this(DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE);
    }

    public PageBean(int pageSize) {
        this(DEFAULT_CURRENT_PAGE, pageSize);
    }

    public PageBean(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<?> getT() {
        return T;
    }

    public void setT(List<?> t) {
        T = t;
    }
}
