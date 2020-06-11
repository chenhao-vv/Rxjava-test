package com.vivo.chmusicdemo.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GankBeautyResult {

    private boolean error;
    private @SerializedName("results") List<GankBeauty> beauties;
}
