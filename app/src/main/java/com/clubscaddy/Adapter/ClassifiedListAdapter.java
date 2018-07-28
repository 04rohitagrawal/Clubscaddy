package com.clubscaddy.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.clubscaddy.Bean.ClassifiedCommentBean;
import com.clubscaddy.Bean.ClassifiedItem;
import com.clubscaddy.Bean.MemberListBean;
import com.clubscaddy.custumview.ExpandableHeightListView;
import com.clubscaddy.utility.AppConstants;
import com.clubscaddy.utility.SessionManager;
import com.clubscaddy.utility.ShowUserMessage;
import com.clubscaddy.utility.Utill;
import com.clubscaddy.utility.Validation;
import com.clubscaddy.DirectorFragmentManageActivity;
import com.clubscaddy.Interface.DialogBoxButtonListner;
import com.clubscaddy.Interface.OnServerRespondingListener;
import com.clubscaddy.R;
import com.clubscaddy.Server.HttpRequest;
import com.clubscaddy.Server.WebService;
import com.clubscaddy.fragment.ClassifiedFragment;
import com.clubscaddy.fragment.EditClassifiedProductDetail;
import com.clubscaddy.fragment.EventFullImageViewActivity;
import com.clubscaddy.fragment.UserProfileActivity;
import com.clubscaddy.utility.CircleBitmapTranslation;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by administrator on 6/5/17.
 */

public class ClassifiedListAdapter extends RecyclerView.Adapter<ClassifiedListAdapter.ViewHolder> {
    Activity activity;
    ArrayList<ClassifiedItem> classifiedItemArrayList;
    double currentLat , currentLong;
    ClassifiedFragment fragment;

    HttpRequest httpRequest;
    ShowUserMessage showUserMessage;

    SessionManager sessionManager ;



    //1 for classification 2 for my classification


    public ClassifiedListAdapter(ClassifiedFragment fragment, ArrayList<ClassifiedItem> classifiedItemArrayList ) {
        this.activity = fragment.getActivity();
        this.classifiedItemArrayList = classifiedItemArrayList;
        this.fragment = fragment;

        sessionManager = new SessionManager(activity);


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {

       LinearLayout convertView = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.classified_adapter_layout, parent ,false);

        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position)
    {
        viewHolder.classified_title_tv.setText(classifiedItemArrayList.get(position).getClassifieds_title());
        viewHolder.classified_discription_tv.setText(classifiedItemArrayList.get(position).getClassifieds_desc());




      if (Validation.isStringNullOrBlank(classifiedItemArrayList.get(position).getClassifieds_user_contact()))
      {
          viewHolder.email_layout.setVisibility(View.GONE);
          viewHolder.mobile_layout.setVisibility(View.GONE);
      }
      else
      {
          viewHolder.email_layout.setVisibility(View.VISIBLE);
          viewHolder.mobile_layout.setVisibility(View.VISIBLE);
      }



       try
       {
           String contactArray[] = classifiedItemArrayList.get(position).getClassifieds_user_contact().split("\n");

           if (contactArray.length == 2)
           {
               String value = contactArray[1] ;

               if (value.contains("Email"))
               {
                   viewHolder.email_address_tv.setText(Html.fromHtml("<p><u>"+value.split("-")[1]+"</p></u>"));
                   viewHolder.mobile_layout.setVisibility(View.GONE);
               }
               else
               {
                   viewHolder.mobile_edit_tv.setText(Html.fromHtml("<p><u>"+value.split("-")[1]+"</p></u>"));
                   viewHolder.email_layout.setVisibility(View.GONE);
               }


           }

           if (contactArray.length == 3)
           {
               String email = contactArray[1] ;
               String mobile = contactArray[2] ;
               viewHolder.email_address_tv.setText(Html.fromHtml("<p><u>"+email.split("-")[1]+"</p></u>"));

               viewHolder.mobile_edit_tv.setText(Html.fromHtml("<p><u>"+mobile.split("-")[1]+"</p></u>"));


           }


       }
       catch (Exception e)
       {

       }

       viewHolder.email_address_tv.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try
               {
                   TextView textView = (TextView) v;
                   Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                           "mailto",textView.getText().toString(), null));
                  // intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                   //intent.putExtra(Intent.EXTRA_TEXT, message);
                   activity.startActivity(Intent.createChooser(intent, "Choose an Email client :"));
               }
               catch (Exception e)
               {

               }
           }
       });


        viewHolder.mobile_edit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                try
                {
                    TextView textView = (TextView) v;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+textView.getText().toString()));
                    activity.startActivity(intent);
                }
                catch (Exception e)
                {

                }

            }
        });



        //extView  ,mobile_edit_tv;
        //LinearLayout email_layout , mobile_layout;

       if (classifiedItemArrayList.get(position).getClassifieds_cost() - (int)classifiedItemArrayList.get(position).getClassifieds_cost() == 0)
       {
           viewHolder.producr_price_tv.setText(sessionManager.getCurrencyCode(activity)+""+ (int) classifiedItemArrayList.get(position).getClassifieds_cost());

       }
       else
       {
           viewHolder.producr_price_tv.setText(sessionManager.getCurrencyCode(activity)+""+classifiedItemArrayList.get(position).getClassifieds_cost());

       }




        httpRequest = new HttpRequest(activity);
        showUserMessage = new ShowUserMessage(activity);
        NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(3);

        if (classifiedItemArrayList.get(position).getClassified_other_pics().size() == 1)
        {
            viewHolder.classified_item_gridview.setNumColumns(1);
        }
        else
        {
            viewHolder.classified_item_gridview.setNumColumns(2);
        }

        viewHolder.classified_user_name.setText(classifiedItemArrayList.get(position).getUser_name());


        if (fragment.listIdentifire == 2)
        {
           viewHolder.classified_modify_linear_layout.setVisibility(View.VISIBLE);
        }
        else
        {
            if (SessionManager.getUser_type(fragment.getActivity()).equals(AppConstants.USER_TYPE_DIRECTOR)||SessionManager.getUser_type(fragment.getActivity()).equals(AppConstants.USER_TYPE_MOBILE_ADMIN))
            {
                viewHolder.classified_modify_linear_layout.setVisibility(View.VISIBLE);
                viewHolder.edit_news_btn.setVisibility(View.INVISIBLE);
                viewHolder.delete_news_btn.setVisibility(View.VISIBLE);

            }
            else
            {
                viewHolder.classified_modify_linear_layout.setVisibility(View.INVISIBLE);

            }


        }







        try
        {
            viewHolder.imageLoaderProgressbar.setVisibility(View.VISIBLE);
                   Glide.with(activity)
                  .load(classifiedItemArrayList.get(position)
                  .getClassifieds_profile())
                  .placeholder(R.drawable.default_img_profile)
                  .error(R.drawable.default_img_profile)
                  .transform(new CircleBitmapTranslation(activity))
                           .listener(new RequestListener<String, GlideDrawable>() {
                               @Override
                               public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                   viewHolder.imageLoaderProgressbar.setVisibility(View.GONE);

                                   return false;
                               }

                               @Override
                               public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                   viewHolder.imageLoaderProgressbar.setVisibility(View.GONE);
                                   return false;
                               }
                           })
                  .into(viewHolder.classified_user_profile);
        }
        catch (Exception e)
        {
            viewHolder.imageLoaderProgressbar.setVisibility(View.GONE);

        }

        ClassifiedProductListAdapter classifiedProductListAdapter = new ClassifiedProductListAdapter(activity , classifiedItemArrayList.get(position).getClassified_other_pics()) ;
        viewHolder.classified_item_gridview.setAdapter(classifiedProductListAdapter);

        try
        {

            DisplayMetrics metrics = fragment.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            if(classifiedItemArrayList.get(position).getClassified_other_pics().size() == 0)
            {
                viewHolder.classified_item_gridview.setNumColumns(0);
                viewHolder.classified_item_gridview.setNumColumns(1);

                viewHolder.params.height = 0 ;
                viewHolder.classified_item_gridview.setLayoutParams(viewHolder.params);
            }
            if(classifiedItemArrayList.get(position).getClassified_other_pics().size() == 1)
            {
                viewHolder.classified_item_gridview.setNumColumns(1);

                viewHolder.params.height = width/2 ;
                viewHolder.classified_item_gridview.setLayoutParams(viewHolder.params);
			/*.height = Viewholder.new_image_gridview.getCount()*/;
            }
            if(classifiedItemArrayList.get(position).getClassified_other_pics().size() == 2)
            {
                viewHolder.classified_item_gridview.setNumColumns(2);
                //Viewholder.new_image_gridview.setNumColumns(1);

                viewHolder.params.height = width /2;
                viewHolder.params.width = width-30;
                viewHolder.classified_item_gridview.setLayoutParams(viewHolder.params);
            }
            if(classifiedItemArrayList.get(position).getClassified_other_pics().size() > 2)
            {
                width = metrics.widthPixels;
                height = metrics.heightPixels;
                viewHolder.classified_item_gridview.setNumColumns(2);
                viewHolder. params = viewHolder.classified_item_gridview.getLayoutParams();

                if(viewHolder.classified_item_gridview.getCount()%2 == 0)
                {
                    viewHolder.params.height = (width /2)*((classifiedItemArrayList.get(position).getClassified_other_pics().size()+1)/2);
                }
                else
                {
                    viewHolder.params.height = (width /2)*((classifiedItemArrayList.get(position).getClassified_other_pics().size()+1)/2);
                }
			/*  Toast.makeText(mContext, bean.getNews_title()+" "+newsList.get(position).getNews_thumb_ur().size()+" "+width, 1).show();
			 */   	viewHolder.params.width = width-30;
                //Toast.makeText(mContext, "Height "+(newsList.get(position).getNews_thumb_ur().size()), 1).show();
                viewHolder.classified_item_gridview.setLayoutParams(viewHolder.params);
            }












           // ExpandableHeightListView expandableHeightListView = new ExpandableHeightListView();
          //  expandableHeightListView.getGridViewSize(viewHolder.classified_item_gridview);
        }
        catch (Exception e)
        {

        }



        viewHolder.classified_item_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {


                ArrayList<String>eventList = new ArrayList<String>();

                for (int i = 0 ; i < classifiedItemArrayList.get(position).getClassified_other_pics().size() ;i++ )
                {
                    eventList.add( classifiedItemArrayList.get(position).getClassified_other_pics().get(i).getUrl());
                }

                Intent intent = new Intent(activity , EventFullImageViewActivity.class);
                intent.putExtra("path_list" , eventList);
                intent.putExtra("pos" , pos+"");


                activity.startActivity(intent);


            }
        });

        viewHolder.profile_linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goOnProfilePic(classifiedItemArrayList.get(position).getClassifieds_uid());
            }
        });

        viewHolder.edit_news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditClassifiedProductDetail editClassifiedProductDetail = new EditClassifiedProductDetail();
                editClassifiedProductDetail.setInstanse(classifiedItemArrayList.get(position) ,  fragment.editClassifedListener);
                fragment.getActivity().getSupportFragmentManager().beginTransaction().add(R.id.content_frame ,editClassifiedProductDetail  ,"editClassifiedProductDetail" ).addToBackStack("editClassifiedProductDetail").commit();

            }
        });


        viewHolder.delete_news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to delete this classified?", new DialogBoxButtonListner() {
                    @Override
                    public void onYesButtonClick(DialogInterface dialog) {
                        dialog.dismiss();
                        deleteClassifiedDetailProduct(classifiedItemArrayList.get(position) ,position);
                    }
                });


            }
        });





        Log.e("position" , position+"");


        viewHolder.comment_edit_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                try
                {
                    classifiedItemArrayList.get(position).setCmtText(s.toString());

                }
                catch (Exception e)
                {

                }

            }
        });



        if (position == classifiedItemArrayList.size()-1 && fragment.isWebServicesisCalling == false && fragment.lastCallItemCount != 0)
        {
            if (fragment.listIdentifire == 1)
            {
                fragment.getClassifiedListWithoutPd(fragment.minimumValue , fragment.maximumValue , fragment.searchingWord ,classifiedItemArrayList.get(position).getClassifieds_id()+"" );
            }
            else
            {
                fragment.getMyClassifiedListWithoutPd(fragment.minimumValue , fragment.maximumValue , fragment.searchingWord ,classifiedItemArrayList.get(position).getClassifieds_id()+"" );
            }


        }


        viewHolder.send_comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utill.hideKeybord(fragment.getActivity() , viewHolder.comment_edit_tv);

                String commentt = viewHolder.comment_edit_tv.getText().toString().replace("\n" , "\n");

                if (Validation.isStringNullOrBlank(commentt))
                {
                  Utill.showDialg(  "Please enter comment" ,fragment.getActivity());
                }
                else
                {
                    viewHolder.comment_edit_tv.setText("");
                    classifiedItemArrayList.get(position).setCmtText("");
                    fragment.sendComment(classifiedItemArrayList.get(position).getClassifieds_id() , commentt , position );

                }


            }
        });
        final ArrayList<ClassifiedCommentBean> classifiedCommentBeanArrayList = classifiedItemArrayList.get(position).getClassifiedCommentBeanArrayList();

////////////

      if (classifiedCommentBeanArrayList.size() != 0)
      {
          if (classifiedItemArrayList.get(position).getClassifieds_uid() == Integer.parseInt(SessionManager.getUserId(fragment.getActivity())))
          {
              viewHolder.delete_cmt_btn.setVisibility(View.VISIBLE);
          }
          else
          {
              if (Integer.parseInt(SessionManager.getUserId(activity)) == classifiedCommentBeanArrayList.get(classifiedCommentBeanArrayList.size()-1).getClassifieds_user_id())
              {
                  viewHolder.delete_cmt_btn.setVisibility(View.VISIBLE);

              }
              else
              {
                  viewHolder.delete_cmt_btn.setVisibility(View.INVISIBLE);

              }
          }
      }








         if (classifiedCommentBeanArrayList.size() >1)
         {
             viewHolder.total_comment_text_view.setText(classifiedCommentBeanArrayList.size()+" Comments");

         }
         else
         {
             viewHolder.total_comment_text_view.setText(classifiedCommentBeanArrayList.size()+" Comment");

         }




        if (classifiedItemArrayList.get(position).getClassifiedCommentBeanArrayList().size() == 0)
        {
            viewHolder.comment_relative_layout.setVisibility(View.GONE);

        }
        else
        {
            viewHolder.comment_relative_layout.setVisibility(View.VISIBLE);

            ClassifiedCommentBean lastClassifiedBean = classifiedCommentBeanArrayList.get(classifiedCommentBeanArrayList.size()-1);
            viewHolder.commentUserProgrssBar.setVisibility(View.VISIBLE);

             viewHolder.commenter_name_tv.setText(lastClassifiedBean.getUser_name());
            viewHolder.comment_tv.setText(lastClassifiedBean.getClassifieds_comment_text());
                     Glide.with(fragment.getActivity())
                     .load(lastClassifiedBean.getUser_profilepic())
                     .placeholder(R.drawable.default_img_profile)
                     .error(R.drawable.default_img_profile)
                     .listener(new RequestListener<String, GlideDrawable>() {
                                 @Override
                                 public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                     viewHolder.commentUserProgrssBar.setVisibility(View.GONE);

                                     return false;
                                 }

                                 @Override
                                 public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                     viewHolder.commentUserProgrssBar.setVisibility(View.GONE);
                                     return false;
                                 }
                             })
                     .transform(new CircleBitmapTranslation(fragment.getActivity()))
                     .into(viewHolder.commenter_image_view);

        }

        viewHolder.delete_cmt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ShowUserMessage showUserMessage = new ShowUserMessage(fragment.getActivity());
                showUserMessage.showDialogBoxWithYesNoButton("Are you sure you want to delete this comment?", new DialogBoxButtonListner() {
                    @Override
                    public void onYesButtonClick(DialogInterface dialog) {
                        ArrayList<ClassifiedCommentBean> classifiedCommentBeanArrayList = classifiedItemArrayList.get(position).getClassifiedCommentBeanArrayList();
                        ClassifiedCommentBean lastClassifiedBean = classifiedCommentBeanArrayList.get(classifiedCommentBeanArrayList.size()-1);
                        fragment.deleteComment(lastClassifiedBean.getClassifieds_comment_id() , lastClassifiedBean.getClassifieds_user_id()+"" , position ,classifiedCommentBeanArrayList.size()-1 );

                    }
                });



            }
        });

        viewHolder.comment_edit_tv.setText(classifiedItemArrayList.get(position).getCmtText());


        viewHolder.total_comment_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (classifiedCommentBeanArrayList.size() != 0)
                {
                    if (classifiedItemArrayList.get(position).getClassifieds_uid() == Integer.parseInt(SessionManager.getUserId(fragment.getActivity())))
                    {
                        fragment.showClassofiedCommentDialog(classifiedCommentBeanArrayList , true , position);

                    }
                    else
                    {
                        fragment.showClassofiedCommentDialog(classifiedCommentBeanArrayList , false ,position);

                    }


                }

            }
        });



        viewHolder.comment_box_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                DirectorFragmentManageActivity directorFragmentManageActivity = (DirectorFragmentManageActivity) fragment.getActivity();
                directorFragmentManageActivity.SwitcFragmentToUserInfoActivity(classifiedCommentBeanArrayList.get(classifiedCommentBeanArrayList.size()-1).getClassifieds_user_id()+"");
            }
        });




    }

    @Override
    public int getItemCount() {
        return classifiedItemArrayList.size();
    }



    public class ViewHolder  extends RecyclerView.ViewHolder
    {
        TextView classified_title_tv;
        TextView classified_discription_tv;
         GridView classified_item_gridview;
        TextView producr_price_tv;
        ImageView classified_user_profile;
        TextView classified_user_name;
        LinearLayout classified_modify_linear_layout;
        ImageButton edit_news_btn ,delete_news_btn;
        LinearLayout profile_linear_layout;


        TextView email_address_tv ,mobile_edit_tv;
        LinearLayout email_layout , mobile_layout;

        ViewGroup.LayoutParams params ;


        TextView total_comment_text_view;
        RelativeLayout comment_relative_layout;
        ImageView send_comment_btn;
        EditText comment_edit_tv;
        ImageView commenter_image_view;
        ImageView delete_cmt_btn;
        LinearLayout comment_box_layout;
        TextView commenter_name_tv;
        TextView comment_tv;

        ProgressBar  imageLoaderProgressbar;

        ProgressBar commentUserProgrssBar ;






        public ViewHolder(LinearLayout convertView) {
            super(convertView);
            classified_title_tv = (TextView) convertView.findViewById(R.id.classified_title_tv);
            classified_discription_tv = (TextView) convertView.findViewById(R.id.classified_discription_tv);

            producr_price_tv = (TextView) convertView.findViewById(R.id.producr_price_tv);
            classified_user_name = (TextView) convertView.findViewById(R.id.classified_user_name);
            classified_item_gridview = (GridView) convertView.findViewById(R.id.classified_item_gridview);
            classified_modify_linear_layout = (LinearLayout) convertView.findViewById(R.id.classified_modify_linear_layout);
            classified_user_profile = (ImageView) convertView.findViewById(R.id.classified_user_profile);

            edit_news_btn = (ImageButton) convertView.findViewById(R.id.edit_news_btn);
            delete_news_btn = (ImageButton) convertView.findViewById(R.id.delete_news_btn);

            profile_linear_layout = (LinearLayout) convertView.findViewById(R.id.profile_linear_layout);


            email_address_tv = (TextView) convertView.findViewById(R.id.email_address_tv);
            mobile_edit_tv = (TextView) convertView.findViewById(R.id.mobile_edit_tv);
            email_layout = (LinearLayout) convertView.findViewById(R.id.email_layout);
            mobile_layout = (LinearLayout) convertView.findViewById(R.id.mobile_layout);

            commentUserProgrssBar = (ProgressBar) convertView.findViewById(R.id.comment_user_progrss_bar);

            total_comment_text_view = (TextView) convertView.findViewById(R.id.total_comment_text_view);
            comment_relative_layout = (RelativeLayout) convertView.findViewById(R.id.comment_relative_layout);
            send_comment_btn = (ImageView) convertView.findViewById(R.id.send_comment_btn);
            comment_edit_tv = (EditText) convertView.findViewById(R.id.comment_edit_tv);
            commenter_image_view = (ImageView) convertView.findViewById(R.id.commenter_image_view);
            delete_cmt_btn = (ImageView) convertView.findViewById(R.id.delete_cmt_btn);
            comment_box_layout = (LinearLayout) convertView.findViewById(R.id.comment_box_layout);
            commenter_name_tv = (TextView) convertView.findViewById(R.id.commenter_name_tv);
            comment_tv = (TextView) convertView.findViewById(R.id.comment_tv);

            imageLoaderProgressbar = (ProgressBar) convertView.findViewById(R.id.image_loader_progressbar);


            params = classified_item_gridview.getLayoutParams();




        }
    }

    public void deleteClassifiedDetailProduct(ClassifiedItem classifiedItem ,final int position)
    {
        HashMap<String , Object> params = new HashMap<>();
        params.put("classifieds_id" ,classifiedItem.getClassifieds_id()+"" );
        httpRequest.getResponse(activity, WebService.delete_classified, params, new OnServerRespondingListener(activity) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                try
                {
                    if (jsonObject.getBoolean("status"))
                    {
                        classifiedItemArrayList.remove(position);
                        notifyDataSetChanged();
                       ShowUserMessage.showUserMessage(fragment.getActivity() , jsonObject.getString("message"));
                    }
                    else
                    {
                        Utill.showDialg(jsonObject.getString("message") , fragment.getActivity());
                    }
                }
                catch (Exception e)
                {

                }



            }
        });

    }


    public void goOnProfilePic(int userId)
    {
        HashMap<String, Object>params = new HashMap<String, Object>();
        params.put("user_club_id", SessionManager.getUser_Club_id(activity));
        params.put("user_type", AppConstants.USER_TYPE_DIRECTOR);

        params.put("user_id", userId+"");
        httpRequest.getResponse(activity, WebService.getMemberDetail, params, new OnServerRespondingListener(activity) {
            @Override
            public void onSuccess(JSONObject jsonObject)
            {
                if (jsonObject.optBoolean("status"))
                {
                    JSONObject object = jsonObject.optJSONArray("response").optJSONObject(0);
                    MemberListBean bean = new MemberListBean();
                    if(object.optString("user_gender").equals("1"))
                    {
                        bean.setUser_gender("Male");
                    }
                    else
                    {
                        bean.setUser_gender("Female");
                    }
                    bean.setInstragram_url(object.optString("instagram"));
                    bean.setUser_type(object.optString("user_type"));
                    bean.setUser_status(object.optString("status"));
                    bean.setLinkedin_url(object.optString("linkedin"));
                    bean.setUser_junior(object.optString("user_junior"));
                    bean.setUser_club_id(SessionManager.getUser_Club_id(activity));
                    bean.setUser_rating(object.optString("user_rating"));
                    bean.setUser_profilepic(object.optString("user_profilepic"));
                    bean.setUser_email(object.optString("user_email"));
                    bean.setTwitter_url(object.optString("twitter"));
                    bean.setUser_first_name(object.optString("user_first_name"));
                    bean.setUser_about_me(object.optString("addabout"));
                    bean.setUser_phone(object.optString("user_phone"));
                    bean.setFace_book_url(object.optString("facebookurl"));
                    bean.setUser_last_name(object.optString("user_last_name"));
                    bean.setUser_id(object.optString("user_id"));




                    if (SessionManager.getUser_email(activity).equals(bean.getUser_email()))
                    {
                        UserProfileActivity fragment = new UserProfileActivity();
                        fragment.setInatanse(bean ,1);
                        ClassifiedListAdapter.this.fragment.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();

                    }
                    else
                    {
                        UserProfileActivity fragment = new UserProfileActivity();
                        fragment.setInatanse(bean ,2);
                        ClassifiedListAdapter.this.fragment.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, "fragment").addToBackStack(null).commit();

                    }




                }
                else
                {
                    ShowUserMessage.showUserMessage(activity , jsonObject.optString("message"));
                }

            }
        });
    }



   /* resultsmlsrealtors@gmail.com
    Dblr2017!*/
}