/**
 * Copyright 2013 Joan Zapata
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.laka.live.help.quickAdapter;


import java.util.List;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.laka.live.help.EventBusManager;

import org.greenrobot.eventbus.EventBus;

import static  com.laka.live.help.quickAdapter.BaseAdapterHelper.get;

/**
 * Abstraction class of a BaseAdapter in which you only need
 * to provide the convert() implementation.<br/>
 * Using the provided BaseAdapterHelper, your code is minimalist.
 * @param <T> The type of the items in the list.
 */
public abstract class QuickAdapter<T> extends BaseQuickAdapter<T, BaseAdapterHelper> {
    Context mContext;
    /**
     * Create a QuickAdapter.
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     */
    public QuickAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
        mContext = context;
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    public QuickAdapter(Context context, int layoutResId, List<T> data) {
        super(context,layoutResId,data);
        mContext = context;
    }

	protected BaseAdapterHelper getAdapterHelper(int position, View convertView, ViewGroup parent) {
		return get(context, convertView, parent, layoutResId, position);
	}

	public void unRegistEvenBus(){
		EventBusManager.unregister(this);
	}
	
//	 TipsDialog loadingDialog;
//	    public void showDialog(String msg, boolean cancelDisable) {
//	        if (loadingDialog == null) {
//	            loadingDialog = new TipsDialog(mContext);
//	        }
//	        loadingDialog.startLoading(msg, cancelDisable);
//	    }
//	    public void showDialog(int... msg) {
//	        if (loadingDialog == null) {
//	            loadingDialog = new TipsDialog(mContext);
//	        }
//	        String text = null;
//	        if (msg != null && msg.length > 0&&msg[0]>0) {
//	            text =mContext.getString(msg[0]) ;
//	        }else{
//	            text = mContext.getString(R.string.please_wait);
//	        }
//	        loadingDialog.startLoading(text, false);
//	    }
//	    public void showDialog(String... msg) {
//	        if (loadingDialog == null) {
//	            loadingDialog = new TipsDialog(mContext);
//	        }
//	        String text = "";
//	        if (msg != null && msg.length > 0) {
//	            text = msg[0];
//	        }
//	        loadingDialog.startLoading(text, false);
//	    }
//	    public void dismissDialog() {
//	        if(loadingDialog!=null){
//	        loadingDialog.stopLoading();
//	        }
//	    }
}
