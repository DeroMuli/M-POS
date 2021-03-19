package com.muli.m_pos.model;

import android.content.Context;

public interface ResponseHandler {

    public void handleresponse(String jsonresponse, RESTfulContentProvider resTfulContentProvider, String requesttag, Context context);
    
}
