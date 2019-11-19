package com.tfzq.pr.model.pr;

import com.alibaba.fastjson.annotation.JSONField;
import com.tfzq.pr.model.generator.GoUserFavorite;

import java.util.Date;
import java.util.List;

/**
 * @author yuzhitao
 */
@SuppressWarnings("serial")
public class GoUserFavoriteBean extends GoUserFavorite {
	private String favoriteTypeText;
	private String favoriteTitle;
	private String favoritePath;
	private Date favoriteTime;

	public String getFavoritePath() {
		return favoritePath;
	}

	public void setFavoritePath(String favoritePath) {
		this.favoritePath = favoritePath;
	}

	public String getFavoriteTitle() {
		return favoriteTitle;
	}

	public void setFavoriteTitle(String favoriteTitle) {
		this.favoriteTitle = favoriteTitle;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getFavoriteTime() {
		return favoriteTime;
	}

	public void setFavoriteTime(Date favoriteTime) {
		this.favoriteTime = favoriteTime;
	}

	public void setFavoriteTypeText(String favoriteTypeText) {
		this.favoriteTypeText = favoriteTypeText;
	}

	public String getFavoriteTypeText() {
		return this.favoriteTypeText;
	}
}