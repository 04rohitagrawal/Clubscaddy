package com.clubscaddy.Bean;
import java.util.ArrayList;

public class NewsBean {
	
	
	  public boolean isSendLike =true;
	  public boolean isSendLike() {
		return isSendLike;
	}
	public void setSendLike(boolean isSendLike) {
		this.isSendLike = isSendLike;
	}


	private String news_feed_id;
	  private String news_user_id;
	  private String news_title;
	  private String news_details;
	  private String news_feed_attach_type;
	  private String news_feed_attach_url;
	  private String likeCount;
	public  String news_details_html_tag;
	  private String myLikeStatus;
	  private String editValue = "";
	  private String vedioUrl = "";
	  private ArrayList<String>  news_feed_thumb_url_list = new ArrayList<String>();
	  private ArrayList<String>  delete_image_list = new ArrayList<String>();
	  private ArrayList<String> newImage = new ArrayList<String>();
	  String comment ;
	  
	  boolean isfiledownliad ;
	  
	  public boolean isIsfiledownliad() {
		return isfiledownliad;
	}
	public void setIsfiledownliad(boolean isfiledownliad) {
		this.isfiledownliad = isfiledownliad;
	}
	public ArrayList<String> getNewImage() {
		return newImage;
	}
	  public String getComment() {
			return comment;
		}
	  public void setComment(String comment)
	  {
		  this.comment = comment;
	  }
	public String getVedioUrl() {
		return vedioUrl;
	}
	public void setVedioUrl(String vedioUrl) {
		this.vedioUrl = vedioUrl;
	}
	public void setNewImage(ArrayList<String> newImage) {
		this.newImage = newImage;
	}
	public String getEditValue() {
		return editValue;
	}
	public void setEditValue(String editValue) {
		this.editValue = editValue;
	}
	public String getMyLikeStatus() {
		return myLikeStatus;
	}
	public void setMyLikeStatus(String myLikeStatus) {
		this.myLikeStatus = myLikeStatus;
	}
	
	
	public void setDeleteImageList(ArrayList<String>   delete_image_list) {
		this.delete_image_list = delete_image_list;
	}
	
	
	ArrayList<CommentBean> commentBean = new ArrayList<CommentBean>();
	  public ArrayList<CommentBean> getCommentBean() {
		return commentBean;
	}
	public void setCommentBean(ArrayList<CommentBean> commentBean) {
		this.commentBean = commentBean;
	}
	public String getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}
	public String getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
	private String commentCount;
	  
	public String getNews_feed_id() {
		return news_feed_id;
	}
	public void setNews_feed_id(String news_feed_id) {
		this.news_feed_id = news_feed_id;
	}
	public String getNews_user_id() {
		return news_user_id;
	}
	public void setNews_user_id(String news_user_id) {
		this.news_user_id = news_user_id;
	}
	public String getNews_title() {
		return news_title;
	}
	public void setNews_title(String news_title) {
		this.news_title = news_title;
	}
	public String getNews_details() {
		return news_details;
	}
	public void setNews_details(String news_details) {
		this.news_details = news_details;
	}
	public String getNews_feed_attach_type() {
		return news_feed_attach_type;
	}
	public void setNews_feed_attach_type(String news_feed_attach_type) {
		this.news_feed_attach_type = news_feed_attach_type;
	}
	public String getNews_feed_attach_url() {
		return news_feed_attach_url;
	}
	public void setNews_thumb_url(ArrayList<String>  news_feed_thumb_url_list) {
		this.news_feed_thumb_url_list = news_feed_thumb_url_list;
	}
	public void setNews_feed_attach_url(String news_feed_attach_url) {
		this.news_feed_attach_url = news_feed_attach_url;
	}  
	public ArrayList<String> getNews_thumb_ur() {
		return news_feed_thumb_url_list;
	}
	public ArrayList<String> getDeleteImageList() {
		return delete_image_list;
	}
	//thumbUrl

	public String getNews_details_html_tag() {
		return news_details_html_tag;
	}

	public void setNews_details_html_tag(String news_details_html_tag) {
		this.news_details_html_tag = news_details_html_tag;
	}

	String user_profilepic ;

	public String getUser_profilepic() {
		return user_profilepic;
	}

	public void setUser_profilepic(String user_profilepic) {
		this.user_profilepic = user_profilepic;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	String user_name ;



}
