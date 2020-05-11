package com.qing.tewang.ui.location;

import java.io.Serializable;
import java.util.ArrayList;

public class ProvinceModel implements Serializable {
	public String province;
    ArrayList<CityModel> cityList;
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public ArrayList<CityModel> getCityList() {
		return cityList;
	}

	public void setCity_list(ArrayList<CityModel> cityList) {
		this.cityList = cityList;
	}

	@Override
	public String toString() {
		return "ProvinceModel [province=" + province + ", cityList="
				+ cityList + "]";
	}

}
