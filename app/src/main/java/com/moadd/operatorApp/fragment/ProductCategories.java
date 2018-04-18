package com.moadd.operatorApp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.moadd.operatorApp.WebView;
import com.moaddi.operatorApp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductCategories extends Fragment {
Button moaddil,operator,supplier,create,fast;

    public ProductCategories() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_product_categories, container, false);
        moaddil= (Button) v.findViewById(R.id.moaddiProducts);
        operator= (Button) v.findViewById(R.id.OperatorProducts);
        supplier= (Button) v.findViewById(R.id.supplierProducts);
        create= (Button) v.findViewById(R.id.createProduct);
        fast= (Button) v.findViewById(R.id.fastListProducts);
        moaddil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllItems fragment = new AllItems();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               SupplierItems fragment = new SupplierItems();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        fast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastItems fragment = new FastItems();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView fragment = new WebView();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
        return v;
    }

}
