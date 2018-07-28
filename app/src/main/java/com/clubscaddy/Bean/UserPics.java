package com.clubscaddy.Bean;

import java.io.Serializable;

/**
 * Created by administrator on 1/7/17.
 */

public class UserPics implements Serializable
{
    int imageid;

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    String image_thumb ;
    String image_url ;
}
