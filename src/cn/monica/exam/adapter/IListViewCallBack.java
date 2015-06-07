package cn.monica.exam.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;


public interface IListViewCallBack<T> {
    public View getView(Context context,int position, View convertView,List<T> list);
}
