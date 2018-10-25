package com.ahf.inventoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahf.inventoryapp.Data.InventoryDBHelper;
import com.ahf.inventoryapp.Data.InventoryItem;
import com.squareup.picasso.Picasso;

import static com.ahf.inventoryapp.R.id.delet;

public class DetailActivity extends AppCompatActivity {
    TextView name;
    TextView price;
    TextView quantity;
    TextView supName;
    TextView supEmail;
    TextView supPhone;
    ImageView imageView;
    Button increase;
    Button decrease;
    Button order;
    Button delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final Intent intent = getIntent();
        final InventoryItem item = intent.getParcelableExtra("id");

        name = (TextView) findViewById(R.id.name);
        name.setText(item.getProductName());

        price = (TextView) findViewById(R.id.price_item);
        price.setText(item.getPrice());

        quantity = (TextView) findViewById(R.id.quantity_item);
        quantity.setText(item.getCurrentQuantity() + "");

        supName = (TextView) findViewById(R.id.supplier_name);
        supName.setText("Supplier Name     " + item.getSupplierName());

        supEmail = (TextView) findViewById(R.id.supplier_email_title);
        supEmail.setText("Supplier Email     " + item.getSupplierEmail());

        supPhone = (TextView) findViewById(R.id.supplier_phone);
        supPhone.setText("Supplier Phone      " + item.getSupplierPhone());

        imageView = (ImageView) findViewById(R.id.image_uri);
        Picasso.with(this).load(item.getImage()).into(imageView);
        increase = (Button) findViewById(R.id.increase);
        decrease = (Button) findViewById(R.id.decrease);
        order = (Button) findViewById(R.id.order);
        delete = (Button) findViewById(delet);
        final InventoryDBHelper dbHelper = new InventoryDBHelper(getApplicationContext());
        increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQuqntity = dbHelper.increasrQuantity(item);
                item.setCurrentQuantity(newQuqntity);
                quantity.setText(item.getCurrentQuantity() + "");
            }
        });
        decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getCurrentQuantity() == 0) {
                    decrease.setEnabled(false);
                }
                int newQuqntity = dbHelper.reduceQuantity(item);
                item.setCurrentQuantity(newQuqntity);
                quantity.setText(item.getCurrentQuantity() + "");
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder orderDialog = new AlertDialog.Builder(DetailActivity.this);
                orderDialog.setTitle("Order by");
                String[] pictureDialogItems = {"Email", "Phone"};
                orderDialog.setItems(pictureDialogItems,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent sendEmail = new Intent(Intent.ACTION_SEND);
                                        sendEmail.putExtra(Intent.EXTRA_EMAIL, supEmail + "");
                                        sendEmail.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                                        sendEmail.putExtra(Intent.EXTRA_TEXT, "I'm email body.");
                                        startActivity(Intent.createChooser(sendEmail, "Send Email"));
                                        break;
                                    case 1:
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:" + supPhone));
                                        startActivity(callIntent);
                                        break;
                                }
                            }
                        });
                orderDialog.show();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailActivity.this);
                alertDialogBuilder.setMessage("Are you sure,You wanted to delete this item");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                dbHelper.deletItem(item);
                                Intent intent1 = new Intent(DetailActivity.this, MainActivity.class);
                                startActivity(intent1);
                                Toast.makeText(DetailActivity.this, "Item Deleted Successfully", Toast.LENGTH_LONG).show();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }
}
