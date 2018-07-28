package com.clubscaddy.Interface;

import java.util.ArrayList;

/**
 * Created by administrator on 28/7/17.
 */

public interface ImageResizingListener
{
    public void onImageResize(String filePath);
    public void onImageResize(ArrayList<String> imagePathList);
}
