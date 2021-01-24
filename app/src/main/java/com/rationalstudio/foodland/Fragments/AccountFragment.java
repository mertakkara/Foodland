package com.rationalstudio.foodland.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rationalstudio.foodland.R;
import com.rationalstudio.foodland.SetupActivity;
import com.rationalstudio.foodland.Utils.GetSharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;


public class AccountFragment extends Fragment {


    private View view;
    // views for button
    private Button btnSelect, btnUpload,addrecipe;
    private EditText name,ind,recip;
    String generatedFilePath="";
    // view for image view
    private ImageView imageView;
    private TextView yazi;
    private GetSharedPreferences getSharedPreferences;
    // Uri indicates, where the image will be picked from
    private Uri filePath;
    String postUrl = "http://10.0.2.2:5000/collabcsv";
    String url = "http://10.0.2.2:5000/zero_all";
    String url2 = "http://10.0.2.2:5000/itemcsv";
    private String id;
    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    ContentResolver contentResolver;
    FirebaseStorage storage;
    StorageReference storageReference;


    public AccountFragment() {
// Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_account, container, false);

// initialise views
        btnSelect = view.findViewById(R.id.btnChoose);
        btnUpload = view.findViewById(R.id.btnUpload);
        addrecipe = view.findViewById(R.id.addrecipe);
        imageView = view.findViewById(R.id.imgView);
        name = view.findViewById(R.id.name);
        ind = view.findViewById(R.id.ind);
        recip = view.findViewById(R.id.recip);
        getSharedPreferences = new GetSharedPreferences(getActivity());
        id = getSharedPreferences.getSession().getString("id",null);
        contentResolver = getContext().getContentResolver();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        // get the Firebase  storage reference
        final ProgressDialog progressDialog2 = new ProgressDialog(getContext());
        progressDialog2.setTitle("Yükleniyor...");


        final ProgressDialog progressDialog3 = new ProgressDialog(getContext());
        progressDialog3.setTitle("Yükleniyor...");



        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        addrecipe.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(getContext(), "Yemeğin ismini giriniz", Toast.LENGTH_LONG).show();
                }
                else  if(TextUtils.isEmpty(ind.getText().toString())){
                    Toast.makeText(getContext(), "İçindekiler kısmını doldurun", Toast.LENGTH_LONG).show();
                }
                else  if(TextUtils.isEmpty(recip.getText().toString())){
                    Toast.makeText(getContext(), "Tarif kısmını doldurun", Toast.LENGTH_LONG).show();
                }
                else  if(TextUtils.isEmpty(generatedFilePath.toString())){
                    Toast.makeText(getContext(), "Fotoğraf ekleyin", Toast.LENGTH_LONG).show();
                }
                else{


                    String postUrl = "http://10.0.2.2:5000/addrecipe";
                    //String postUrl = "http://localhost:5000/addlist";


                    JSONObject data2 = new JSONObject();
                    try {
                        data2.put("name", name.getText());
                        String ind2 = String.valueOf(ind.getText());
                        ind2 = ind2.replaceAll(",", " ");
                        data2.put("ind",ind2);
                        data2.put("recip",recip.getText());
                        data2.put("image",generatedFilePath);
                        data2.put("user_id",id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jsObjRequest3 = new JsonObjectRequest(Request.Method.POST, postUrl, data2, new Response.Listener<JSONObject>() {


                        @Override
                        public void onResponse(JSONObject response) {


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("idid", "onErrorResponse: "+error);

                        }
                    });
                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    requestQueue2.add(jsObjRequest3 );

                }
                progressDialog3.show();
                progressDialog2.show();

                JsonObjectRequest jsObjRequest2 = new JsonObjectRequest(Request.Method.POST, postUrl, null, new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog2.dismiss();

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("idid", "onErrorResponse: "+error);

                    }
                });
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(jsObjRequest2);


                //--------------------------------------------------------------


      /*  JSONObject jsonobj; // declared locally so that it destroys after serving its purpose
        jsonobj = new JSONObject();
        try {
            // adding some keys
            jsonobj.put("user", id);


        } catch (JSONException ex) {

            ex.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, jsonobj, new com.android.volley.Response.Listener<JSONObject>() {


            @Override
            public void onResponse(JSONObject response) {
progressDialog.dismiss();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("idid", "onErrorResponse: "+error);

            }
        });
        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsObjRequest);
*/


                /////////////////////////////////////////

                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url2, null, new com.android.volley.Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog3.dismiss();

                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("idid", "onErrorResponse: "+error);

                    }
                });
                RequestQueue requestQueue3 = Volley.newRequestQueue(getContext());
                requestQueue3.add(jsObjRequest);






            }
        });













        return view;
    }

    private void uploadImage() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(
                                    new OnCompleteListener<Uri>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            String fileLink = task.getResult().toString();
                                            generatedFilePath = fileLink;

                                            progressDialog.dismiss();
                                            //next work with URL

                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void SelectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                contentResolver,
                                filePath);
                imageView.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


}
