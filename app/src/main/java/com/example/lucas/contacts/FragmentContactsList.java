package com.example.lucas.contacts;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class FragmentContactsList extends Fragment{

    DBContactHelper mydb;

   @Override
   public View onCreateView(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
      /**
       * Inflate the layout for this fragment
       */
       View view = inflater.inflate(R.layout.fragment_list, container, false);
       ListView listView = (ListView) view.findViewById(R.id.lista);

       mydb = new DBContactHelper(view.getContext());
       ArrayList stringList = mydb.getAllCotacts();

       ContactsAdapter adapter = new ContactsAdapter(view.getContext(), stringList);

       listView.setVisibility((adapter.isEmpty())?View.GONE:View.VISIBLE);

       listView.setAdapter(adapter);

      return view;
   }
}
