package com.qing.tewang.ui.location;

import java.io.Serializable;
import java.util.ArrayList;

public class CityModel implements Serializable {
	public String city;
	ArrayList<CountyModel> countyList;
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public ArrayList<CountyModel> getCountyList() {
		return countyList;
	}
	public void setCounty_list(ArrayList<CountyModel> countyList) {
		this.countyList = countyList;
	}
	
	@Override
	public String toString() {
		return "CityModel [city=" + city + ", countyList=" + countyList + "]";
	}

}
