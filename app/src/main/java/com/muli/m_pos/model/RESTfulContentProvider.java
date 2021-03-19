package com.muli.m_pos.model;

import android.content.ContentProvider;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

public abstract class RESTfulContentProvider extends ContentProvider {

    private Map<String, UriRequestTask> mRequestsInProgress =
            new HashMap<String, UriRequestTask>();

    private UriRequestTask getRequestTask(String queryText) {
        return mRequestsInProgress.get(queryText);
    }


    public abstract SQLiteDatabase getDatabase();

    public void requestComplete(String mQueryText) {
        synchronized (mRequestsInProgress) {
            mRequestsInProgress.remove(mQueryText);
        }
    }

    UriRequestTask newQueryTask( String uniqueuri) {
        UriRequestTask requestTask;
        requestTask = new UriRequestTask (this, uniqueuri, getContext());
        mRequestsInProgress.put(uniqueuri, requestTask);
        return requestTask;
    }

    public void asyncQueryRequest(String queryUri) {
        synchronized (mRequestsInProgress) {
            UriRequestTask requestTask = getRequestTask(queryUri);
            if (requestTask == null) {
                requestTask = newQueryTask(queryUri);
                Thread t = new Thread(requestTask);
                t.start();
            }
        }
    }

}
