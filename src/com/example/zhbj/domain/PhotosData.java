package com.example.zhbj.domain;

import java.util.ArrayList;

/**
 * ͼƬbean
 * 
 * @author Administrator
 * 
 */
public class PhotosData {

	public int retcode;
	public photoInfo data;

	public class photoInfo {
		public String more;
		public ArrayList<NewsPhoto> news;

		@Override
		public String toString() {
			return "photoInfo [news=" + news + "]";
		}
	}

	public class NewsPhoto {
		public String id;
		public String listimage;
		public String pubdate;
		public String title;
		public String type;
		public String url;

		@Override
		public String toString() {
			return "NewsPhoto [listimage=" + listimage + ", title=" + title
					+ ", url=" + url + "]";
		}

	}

	@Override
	public String toString() {
		return "PhotosData [data=" + data + "]";
	}

}
