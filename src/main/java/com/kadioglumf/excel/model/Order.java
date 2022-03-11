package com.kadioglumf.excel.model;

import com.kadioglumf.excel.annotation.Excel;
import com.kadioglumf.excel.annotation.ExcelColumn;

@Excel(sheetName = "order")
public class Order extends BaseExcel {

    @ExcelColumn(col = 0, columnName = "NAME")
    private String name;

    @ExcelColumn(col = 1, columnName = "LAST NAME")
    private String lastName;

    @ExcelColumn(col = 2, columnName = "ORDER NAME")
    private String orderName;

    @ExcelColumn(col = 3, columnName = "COUNT")
    private Integer count;

    public Order(String name, String lastName, String orderName, int count) {
        this.name = name;
        this.lastName = lastName;
        this.orderName = orderName;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
