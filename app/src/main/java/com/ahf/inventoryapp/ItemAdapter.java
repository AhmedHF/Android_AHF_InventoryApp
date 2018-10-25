package com.ahf.inventoryapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahf.inventoryapp.Data.InventoryDBHelper;
import com.ahf.inventoryapp.Data.InventoryItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ahmed Hassan on 27/08/2018.
 */

public class ItemAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<InventoryItem> itemArrayList;


    public class ViewHolder {
        TextView name;
        TextView price;
        TextView quantity;
        ImageView imageView;
        Button sale;

        public ViewHolder(View convertView) {
            name = (TextView) convertView.findViewById(R.id.name);
            price = (TextView) convertView.findViewById(R.id.price_item);
            quantity = (TextView) convertView.findViewById(R.id.quantity_item);
            imageView = (ImageView) convertView.findViewById(R.id.image_uri);
            sale = (Button) convertView.findViewById(R.id.add_cart);
        }
    }

    public ItemAdapter(Context context, ArrayList<InventoryItem> arrayList) {
        this.context = context;
        itemArrayList = arrayList;
    }

    @Override
    public int getCount() {
        return itemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final InventoryItem item = itemArrayList.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String name = item.getProductName();
        viewHolder.name.setText(name);
        int quantity = item.getCurrentQuantity();
        viewHolder.quantity.setText(quantity + "");
        String price = item.getPrice();
        viewHolder.price.setText(price + "");
//        viewHolder.imageView.setImageURI(Uri.parse(item.getImage()));
        Picasso.with(context).load(item.getImage()).into(viewHolder.imageView);
        viewHolder.sale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getCurrentQuantity() == 1) {
                    viewHolder.sale.setEnabled(false);
                }
                InventoryDBHelper dbHelper = new InventoryDBHelper(context);
                int newQuqntity = dbHelper.reduceQuantity(item);
                item.setCurrentQuantity(newQuqntity);
//                Log.e("aaa",item.getId());
                viewHolder.quantity.setText(newQuqntity + "");
            }
        });
        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("id", item);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
