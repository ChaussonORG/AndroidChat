package com.sudaotech.chatlibrary.network.response;

import com.sudaotech.chatlibrary.model.BaseMessage;

import java.util.List;

/**
 * Created by Samuel on 2016/12/8 13:10
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class MessageRecordResponse {

    /**
     * offset : 0
     * limit : 1
     * total : 0
     * sortField : lastUpdate
     * sortOrder : DESC
     * items : []
     * startPage : 1
     */

    private int offset;
    private int limit;
    private int total;
    private String sortField;
    private String sortOrder;
    private int startPage;
    private List<BaseMessage> items;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public List<BaseMessage> getItems() {
        return items;
    }

    public void setItems(List<BaseMessage> items) {
        this.items = items;
    }
}
