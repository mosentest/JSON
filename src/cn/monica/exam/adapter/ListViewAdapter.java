package cn.monica.exam.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ListViewAdapter<T> extends BaseAdapter {
    private List<T> list;
    private Context context;
    private IListViewCallBack iListViewCallBack;

    public ListViewAdapter(Context context, List<T> list , IListViewCallBack iListViewCallBack) {
        this.list = list;
        this.context = context;
        this.iListViewCallBack = iListViewCallBack;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return  iListViewCallBack.getView(context,position,convertView,list);
    }
}
