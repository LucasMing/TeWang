package com.qing.tewang.ui.location;

import java.io.Serializable;

public class CountyModel implements Serializable {
	public String county;

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	@Override
	public String toString() {
		return "CountyModel [county=" + county + "]";
	}

}