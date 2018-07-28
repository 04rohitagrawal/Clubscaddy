package com.clubscaddy.Server;

public class WebService {
	

	public static String SENDER_ID = "462916757647";//http://72.167.41.165/gifkar/webservices/
	public static String hostUrl = "https://clubscaddy.com/appwebservices/";


	//public static String hostUrl = "https://clubscaddy.com/appwebservices/";



	public static String purchasedapp=hostUrl  +"purchasedapp.php";
	public static String login = hostUrl+"login";
	public static String forgotpassword = hostUrl+"forgotpassword";
	public static String changepassword = hostUrl+"changepassword";
	public static String changeClubMessage = hostUrl+"changeclubmessage";
	public static String eventnotification = hostUrl+"eventnotification";
	//http://72.167.41.165//appwebservices/get_member_list
	
	
	public static String phonecode = hostUrl+"phonecode";
	//phonecode
	public static String deleteallbcast = hostUrl+"deleteallbcast";
	//deleteallbcast
	public static String allMemberList = hostUrl+"memberdir";

	public static String getMemberDetail = hostUrl+"get_member_list";

	public static String get_broadcast_list = hostUrl+"broadcastlist";
	public static String get_broadcast_detail = hostUrl+"broadcastdetail";
	public static String pollingreply = hostUrl+"pollingreply";
	
	public static String editevent = hostUrl+"editevent";
	
	public static String scoredetails = hostUrl+"league_detail";
	
	public static String get_pollingdetail = hostUrl+"pollingdetail";
	public static String eventresult = hostUrl+"eventresult";
	public static String editnews = hostUrl+"editnews";
	
	public static String get_score_detail = hostUrl+"scoredetails";
	public static String get_court_list = hostUrl+"courtlist";
	public static String get_group_list = hostUrl+"group";
	
	public static String create_brodcast = hostUrl+"brodcast";
	
	
	public static String get_all_events_list = hostUrl+"currentrunningevents.php";
	public static String myevent = hostUrl+"myevent";
	
	public static String get_dropin_list = hostUrl+"dropins";
	public static String get_notifications = hostUrl+"getnotification";
	
	
	public static String bulkbookingVerify = hostUrl+"bulkbooking";
	public static String bulkbookingSend = hostUrl+"bulkreservation";
	public static String recursivebookingRequest = hostUrl+"recursivereservation";
	public static String addgroup = hostUrl+"addgroup";
	public static String addEvent = hostUrl+"create_event";
	public static String editEvent = hostUrl+"editevent.php";
	public static String createDropIn = hostUrl+"create_dropins";
	public static String createScore = hostUrl+"createscore";
	public static String deletebroadcast = hostUrl+"deletebroadcast";
	public static String deletenotification = hostUrl+"deletenotification";
	//deletenotification
	//http://72.167.41.165//appwebservices/get_member_list
	
	public static String editgroup = hostUrl+"editgroup";
	
	
	
	
	public static String get_slot_list = hostUrl+"timeslotlist";
	public static String get_court_detail = hostUrl+"editinfocourt";
	public static String get_club_defalut_setting = hostUrl+"clubruleslist";
	
	public static String user_signup = hostUrl+"user_signup";
	
	public static String add_admin = hostUrl+"user_signup";
	public static String edit_admin_profile = hostUrl+"editprofbydirector";
	
	public static String get_user_preferences = hostUrl+"userprifrenceslist";
	public static String set_user_preferences = hostUrl+"userprifrences";
	public static String change_Status = hostUrl+"change_status";
	public static String update_logo = hostUrl+"changelogo";
	public static String update_court = hostUrl+"courtrules";
	public static String set_default_club_Setting = hostUrl+"defaultsetrules";
	public static String update_my_profile = hostUrl+"updatedirectorprof";
	public static String get_reservation = hostUrl+"allcourtreserve";
	//public static String get_courts = hostUrl+"courtlist";
	
	public static String send_reservation_request = hostUrl+"courtreservenew";
	public static String send_reply = hostUrl+"dropinreply";
	public static String join_Event = hostUrl+"joinevent";
	public static String WithDrawl = hostUrl+"eventwithdrow";
	
	
	
	public static String cancel_booking = hostUrl+"cancelreservation";
	public static String move_slot = hostUrl+"moveslot";
	public static String deleteMultiple = hostUrl+"deletemultiplebooking";
	public static String deleteRecursive = hostUrl+"deleterecursive";
	public static String deleteGroup = hostUrl+"deleteGroup";
	public static String deleteEvent = hostUrl+"delete_event";
	public static String sendNotificationForEvent = hostUrl+"eventnotification";
	public static String deletedropin = hostUrl+"deletedropin";
	public static String deletedropinUser = hostUrl+"deleteconfirmed";
	public static String getNotificationCount = hostUrl+"unreadnotification";
	
	public static String create_News = hostUrl+"createnews";
	public static String get_News = hostUrl+"news_android.php";
	public static String like_News = hostUrl+"likenews";
	public static String comment_News = hostUrl+"comments";
	
	public static String eventwithdrow = hostUrl+"eventwithdrow";
	
	public static String scoreresult = hostUrl+"scoreresult";
	
	
	
	public static String KEY_STATUS = "status";
	public static String KEY_SUCCESS = "success";
	public static String KEY_RESULT = "data";
	public static String KEY_MESSAGE = "message";
	public static String VALUE_STATUS_TRUE = "true";
	public static String VALUE_STATUS_FALSE = "false";
	public static String Response = "Response";
	public static String deleteallnotification =  hostUrl+"deleteallnotification";
	
	public static String sporttype =  hostUrl+"sporttype";
	public static String clubratting =  hostUrl+"clubratting";
	
	public static String deletenews =  hostUrl+"deletenews";
	public static String usermail =  hostUrl+"usermail";
	public static String editdirectorinfo =  hostUrl+"editdirectorinfo";
	public static String changemail =  hostUrl+"changemail";
	public static String notificationdetail =  hostUrl+"notificationdetail";
	public static String myreservation =  hostUrl+"myreservation";
	
	public static String scorelist =  hostUrl+"scorelist";
	public static String editscore =  hostUrl+"editscore";
	
	public static String deletescore =  hostUrl+"deletescores";
	
	public static String support =  hostUrl+"support";
	
	
	public static String changeusertype =  hostUrl+"changeusertype";
	
	public static String signupfromweb =  hostUrl+"signupfromweb";
	public static String deletecomment =  hostUrl+"deletecomment";
	public static String termsandpolicyurls =  hostUrl+"termsandpolicyurls.php";
	
	
	public static String urls =  hostUrl+"urls.php";


	public static String UiLevel =  hostUrl+"ui_label.php";


	public static String update_profile =  hostUrl+"update_profile.php";

	public static String cat_list =  hostUrl+"cat_list.php";
	public static String ManageEvent =  hostUrl+"event_manage.php";
	public static String news_like_list =  hostUrl+"news_like_list.php";
	public static String emailid_check =  hostUrl+"emailid_check";

	public static String delete_profilepic =  hostUrl+"delete_profilepic";

	public static String mail_score =  hostUrl+"mail_score";

	public static String usernoshowlist =  hostUrl+"usernoshowlist";

	public static String coachreserve =  hostUrl+"coachreserve";
	public static String coachreservations =  hostUrl+"coachreservations";
	//
	public static String deletecoachbooking =  hostUrl+"deletecoachbooking";
	public static String mycoachreserve =  hostUrl+"mycoachreserve";
	public static String coachmemberbookinglist =  hostUrl+"coachmemberbookinglist";
	public static String create_classified =  hostUrl+"create_classified";
	public static String classifieds_list =  hostUrl+"classifieds_list";
	public static String delete_classified =  hostUrl+"delete_classified";


	public static String editclassified =  hostUrl+"editclassified";
	//
	public static String create_schedule =  hostUrl+"create_schedule";
	public static String schedule_list =  hostUrl+"schedule_list";
	public static String myschedule_list =  hostUrl+"myschedule_list";

	public static String classified_status =  hostUrl+"classified_status";

	public static String update_classified_status =  hostUrl+"update_classified_status";

	public static String myclassifieds_list =  hostUrl+"myclassifieds_list";


	public static String coach_list =  hostUrl+"coach_list";


	public static String createcoach =  hostUrl+"createcoach";

	public static String coach_recursivereservation =  hostUrl+"coach_recursivereservation";

	public static String coach_deleterecursive =  hostUrl+"coach_deleterecursive";

	public static String league_withdraw =  hostUrl+"league_withdraw";
	public static String join_league =  hostUrl+"join_league";

	public static String delete_schedule =  hostUrl+"delete_schedule";


	public static String leaguenotification =  hostUrl+"leaguenotification";

	public static String schedule_change =  hostUrl+"schedule_change";
	public static String edit_schedule =  hostUrl+"edit_schedule";
	public static String classifieds_comment =  hostUrl+"classifieds_comment.php";
	public static String delete_classifieds_comment =  hostUrl+"delete_classifieds_comment.php";

	public static String add_insta_key =  hostUrl+"add_insta_key.php";


	public static String createCclassLink =  hostUrl+"create_class.php";

	public static String class_list =  hostUrl+"class_list.php";


	public static String class_detail_list =  hostUrl+"class_detail_list.php";
	public static String delete_class =  hostUrl+"delete_class.php";

	public static String classGridLink =  hostUrl+"class_grid.php";

	public static String classSignupLink =  hostUrl+"class_signup.php";

	public static String classUserList =  hostUrl+"class_user_list.php";

	public static String class_member_delete =  hostUrl+"class_member_delete.php";


	public static String sendnotification_class =  hostUrl+"sendnotification_class.php";


//class_user_list

	//public static String coachmemberbookinglist =  hostUrl+"coachmemberbookinglist";

//
}


