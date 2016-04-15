package com.icloudmoo.common.vo;

import java.util.List;

public class PaginatedList<T> extends ValueObject {
	private static final long serialVersionUID = 1L;
	//页面大小
	private int pageSize=20;
	//当前页
	private int pageNumber;
	//总页数
	private int pageCount;
	//当前页码数据
	private List<T> results;
	//总的结果集合
	private int rowCount;
	//开始的记录素
	private int startRownum;
	//结束的记录数
	private int endRownum;
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public List<T> getResults() {
		return results;
	}
	public void setResults(List<T> results) {
		this.results = results;
	}
	public int getRowCount() {
		return rowCount;
	}
	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}
	public int getStartRownum() {
		return startRownum;
	}
	public void setStartRownum(int startRownum) {
		this.startRownum = startRownum;
	}
	public int getEndRownum() {
		return endRownum;
	}
	public void setEndRownum(int endRownum) {
		this.endRownum = endRownum;
	}
	
	
}
