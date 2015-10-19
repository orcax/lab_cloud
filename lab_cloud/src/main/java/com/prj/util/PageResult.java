package com.prj.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PageResult<T> implements Serializable{

	private static final long serialVersionUID = 6192560777709033330L;

	public static int DEFAULT_NUM_PER_PAGE = 10;
	public static int DEFAULT_CURR_PAGE_NUM = 1;
	
	private int numPerPage = DEFAULT_NUM_PER_PAGE;
	
	private int currPageNum = DEFAULT_CURR_PAGE_NUM;
	
	private List<T> data = new ArrayList<T>();
	
	private int totalItemNum;
	
	public PageResult() {
		this(DEFAULT_CURR_PAGE_NUM, DEFAULT_NUM_PER_PAGE);
	}

	public PageResult(int currPageNum, int numPerPage) {
		this.currPageNum = currPageNum;
		this.numPerPage  = numPerPage;
	}

	public int getTotalItemNum() {
		return totalItemNum;
	}

	public void setTotalItemNum(int totalItemNum) {
		this.totalItemNum = totalItemNum;
	}
	
	public int getTotalPageNum() {
		if(totalItemNum % numPerPage == 0)
			return totalItemNum / numPerPage;
		else
			return totalItemNum / numPerPage + 1;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
	
	public void setNumPerPage(int numPerPage) {
		this.numPerPage = numPerPage;
	}
	
	public int getNumPerPage() {
		return numPerPage;
	}
	
	public void setCurrPageNum(int currPageNum) {
		this.currPageNum = currPageNum;
	}
	
	public int getCurrPageNum() {
		return currPageNum;
	}
	
	public boolean isHasNextPage() {
		return this.getCurrPageNum() < this.getTotalPageNum();
	}
	
	public boolean isHasPrePage() {
		return this.getCurrPageNum() > 1;
	}
	
	protected static int getStartOfPage(int currPageNum) {
		return getStartOfPage(currPageNum, DEFAULT_NUM_PER_PAGE);
	}
	
	public static int getStartOfPage(int currPageNum, int numPerPage) {
		return (currPageNum - 1) * numPerPage;
	}
}
