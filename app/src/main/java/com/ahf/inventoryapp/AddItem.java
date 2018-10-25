package com.ahf.inventoryapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.ahf.inventoryapp.Data.InventoryDBHelper;
import com.ahf.inventoryapp.Data.InventoryItem;

import java.io.IOException;

public class AddItem extends AppCompatActivity {

    Bitmap bitmap;
    Uri contentURI;
    EditText name;
    EditText price;
    EditText quantity;
    EditText supName;
    EditText supEmail;
    EditText supPhone;
    ImageView imageView;
    Button btnChoose;
    Button addItem;
    private int GALLERY = 1, CAMERA = 2;
    InventoryDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        name = (EditText) findViewById(R.id.product_name_edit);
        price = (EditText) findViewById(R.id.price_edit);
        quantity = (EditText) findViewById(R.id.quantity_edit);
        supName = (EditText) findViewById(R.id.supplier_name_edit);
        supEmail = (EditText) findViewById(R.id.supplier_email_edit);
        supPhone = (EditText) findViewById(R.id.supplier_phone_edit);
        imageView = (ImageView) findViewById(R.id.image_view);

        btnChoose = (Button) findViewById(R.id.select_image);
        addItem = (Button) findViewById(R.id.insert);

        dbHelper = new InventoryDBHelper(this);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid()) {
                    final InventoryItem inventoryItem = new InventoryItem(name.getText().toString()
                            , Integer.parseInt(String.valueOf(quantity.getText()))
                            , price.getText().toString()
                            , supName.getText().toString()
                            , supEmail.getText().toString()
                            , supPhone.getText().toString()
                            , contentURI.toString());
                    String id = dbHelper.insertItem(inventoryItem);
                    inventoryItem.setId(id);

                    Intent intent = new Intent(AddItem.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public boolean valid() {
        if (TextUtils.isEmpty(name.getText() + "")) {
            name.setError("Please enter product name");
            name.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(price.getText() + "")) {
            price.setError("Please enter price");
            price.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(quantity.getText() + "")) {
            quantity.setError("Please enter quantity");
            quantity.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(supName.getText() + "")) {
            supName.setError("Please enter supplier name");
            supName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(supPhone.getText() + "")) {
            supPhone.setError("Please enter supplier phone");
            supPhone.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(supEmail.getText() + "")) {
            supEmail.setError("Please enter supplier phone");
            supEmail.requestFocus();
            return false;
        }
        return true;
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Image");
        String[] pictureDialogItems = {
                "Gallery",
                "Camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == CAMERA) {
            contentURI = data.getData();
            bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }
}
