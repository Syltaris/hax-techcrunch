package com.tuanzi.baidumap;

import java.io.Serializable;
import java.util.List;

public class Dingwei_Info {

	List<Info> info;
	public static class Info implements Serializable{
		private double latitude;
		private double longitude;
		private String name;
		public double getLatitude() {
			return latitude;
		}
		public void setLatitude(double latitude) {
			this.latitude = latitude;
		}
		public double getLongitude() {
			return longitude;
		}
		public void setLongitude(double longitude) {
			this.longitude = longitude;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
	
}
