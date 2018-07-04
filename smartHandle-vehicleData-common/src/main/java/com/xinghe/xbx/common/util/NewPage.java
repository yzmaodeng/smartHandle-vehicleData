package com.xinghe.xbx.common.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 一个分页类，没什么可说的，普普通通却方便使用
 * 
 * @author 靳广博
 * @since 1.0
 * @version 1.0
 */
public class NewPage<T> implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -671397060968633921L;
	/**
     * 当前第几页
     */
    private int page = 1;
    /**
     * 每页多少个
     */
    private int pageSize = 20;
    /**
     * 总共多少个
     */
    private long allCount = 0;
    /**
     * 内容
     */
    private List<T> list;

    public NewPage(int page, int pageSize, long allCount, List<T> list) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        this.allCount = allCount;
        this.page = page;
        this.list = list;
        this.pageSize = pageSize;
    }

    /**
     * 只有一页内容
     * 
     * @param list
     *            页面内容
     */
    public NewPage(List<T> list) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        this.allCount = list.size();
        this.page = 1;
        this.list = list;
        this.pageSize = list.size();
    }

    /**
     * 获取当前页数
     */
    public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    /**
     * 获取每页条数
     */
    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取总条数
     */
    public long getAllCount() {
        return this.allCount;
    }

    public void setAllCount(long allCount) {
        this.allCount = allCount;
    }

    /**
     * 获取当前页数据
     */
    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}