package com.getstream.sdk.chat.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FileSendResponse {
    @SerializedName("file")
    @Expose
    private String fileUrl;

    public String getFileUrl() {
        return fileUrl;
    }
}
