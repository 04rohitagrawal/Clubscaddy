package com.clubscaddy.Bean;

public class NotificationsBean {
	private String notifications_id;
    private String notifications_sender_id;
    private String sender_name;
    private String notification_dropin_id;
    private String notification_type;
    private String notification_message;
    private String notification_invitation_status;
    private String notification_status;
    private String timeago;
    private String notifications_action_status;
    private String NotificationScoreId;
    private String notificationCommonId;
    
  //  private String dropin_member_user_id;
    
public String getNotificationCommonId() {
		return notificationCommonId;
	}
	public void setNotificationCommonId(String notificationCommonId) {
		this.notificationCommonId = notificationCommonId;
	}
public String getNotificationScoreId() {
		return NotificationScoreId;
	}
	public void setNotificationScoreId(String notificationScoreId) {
		NotificationScoreId = notificationScoreId;
	}
/*	public String getDropin_member_user_id() {
		return dropin_member_user_id;
	}
	public void setDropin_member_user_id(String dropin_member_user_id) {
		this.dropin_member_user_id = dropin_member_user_id;
	}
*/	public String getNotifications_action_status() {
		return notifications_action_status;
	}
	public void setNotifications_action_status(String notifications_action_status) {
		this.notifications_action_status = notifications_action_status;
	}
	public String getNotifications_id() {
		return notifications_id;
	}
	public void setNotifications_id(String notifications_id) {
		this.notifications_id = notifications_id;
	}
	public String getNotifications_sender_id() {
		return notifications_sender_id;
	}
	public void setNotifications_sender_id(String notifications_sender_id) {
		this.notifications_sender_id = notifications_sender_id;
	}
	public String getSender_name() {
		return sender_name;
	}
	public void setSender_name(String sender_name) {
		this.sender_name = sender_name;
	}
	public String getNotification_dropin_id() {
		return notification_dropin_id;
	}
	public void setNotification_dropin_id(String notification_dropin_id) {
		this.notification_dropin_id = notification_dropin_id;
	}
	public String getNotification_type() {
		return notification_type;
	}
	public void setNotification_type(String notification_type) {
		this.notification_type = notification_type;
	}
	public String getNotification_message() {
		return notification_message;
	}
	public void setNotification_message(String notification_message) {
		this.notification_message = notification_message;
	}
	public String getNotification_invitation_status() {
		return notification_invitation_status;
	}
	public void setNotification_invitation_status(String notification_invitation_status) {
		this.notification_invitation_status = notification_invitation_status;
	}
	public String getNotification_status() {
		return notification_status;
	}
	public void setNotification_status(String notification_status) {
		this.notification_status = notification_status;
	}
	public String getTimeago() {
		return timeago;
	}
	public void setTimeago(String timeago) {
		this.timeago = timeago;
	}
}
