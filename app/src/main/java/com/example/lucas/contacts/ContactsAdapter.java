package com.example.lucas.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lucas on 03/06/2017.
 */

public class ContactsAdapter extends BaseAdapter {

    Context context;
    List<String> stringList;

    public ContactsAdapter(Context context, List<String> stringList){
        this.context = context;
        this.stringList = stringList;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return stringList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.contactslist, parent, false);
        }

        String texto = stringList.get(position);
        TextView textView = (TextView) convertView.findViewById(R.id.texto);
        textView.setText(texto);

        return convertView;
    }
}
