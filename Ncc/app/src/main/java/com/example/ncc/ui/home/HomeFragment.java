package com.example.ncc.ui.home;

        import android.app.ProgressDialog;
        import android.content.ContentResolver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.drawable.Drawable;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.net.Uri;
        import android.os.Bundle;
        import android.provider.MediaStore;
        import android.text.TextUtils;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.webkit.MimeTypeMap;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProvider;


        import com.bumptech.glide.Glide;
        import com.example.ncc.R;
        import com.example.ncc.databinding.FragmentHomeBinding;
        import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.OnProgressListener;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

        import org.jetbrains.annotations.NotNull;

        import java.io.IOException;
        import java.util.concurrent.Executor;

        import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {
    public static final String PREF_NAME = "SS";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    EditText Name,Regno,Phoneno,Address,Department;
    Button upload;
    DatabaseReference mbase;
    private Uri filePath;
    ImageView profile;
    FirebaseStorage storage1;
    StorageReference storageReference;
    private final int PICK_IMAGE_REQUEST = 22;
    private int count1 = 0;
    private FirebaseAuth mAuth;
    public static final String TAG = "YOUR-TAG-NAME";
    private int a = 0;




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mAuth = FirebaseAuth.getInstance();

        /* perform your actions here*/

        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull @NotNull Task task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInAnonymously:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.getException());
                    Toast.makeText(getContext(),"Authenticate failed...",Toast.LENGTH_LONG).show();


                }
            }
        });


        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        ConnectivityManager conMgr =  (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){

            Toast.makeText(getContext(),"No Internet Available",Toast.LENGTH_LONG).show();


        }

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Name = binding.editTextTextPersonName;
        Regno = binding.editTextMark;
        Phoneno = binding.editphoneno;
        Address = binding.editAddress;
        Department = binding.editDep;
        profile = binding.imageView;
        upload = binding.button;
        DatePicker Date;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Drawable customErrorDrawable = getResources().getDrawable(R.drawable.ic_error);
        customErrorDrawable.setBounds(0, 0, customErrorDrawable.getIntrinsicWidth(), customErrorDrawable.getIntrinsicHeight());


        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {


                textView.setText(s);
                upload.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {

                        String name = binding.editTextTextPersonName.getText().toString();
                        String regno = binding.editTextMark.getText().toString();
                        String phoneno = binding.editphoneno.getText().toString();
                        String address = binding.editAddress.getText().toString();
                        String department = binding.editDep.getText().toString();
                        int date = binding.simpleDatePicker.getDayOfMonth();
                        int month = binding.simpleDatePicker.getMonth();
                        int year = binding.simpleDatePicker.getYear();




                        if (netInfo == null) {

                            Toast.makeText(getContext(), "No Internet Available", Toast.LENGTH_LONG).show();


                        } else if (TextUtils.isEmpty(name)) {
                            Name.setError("Name must be filled", customErrorDrawable);
                            Name.requestFocus();
                            return;
                        } else if (TextUtils.isEmpty(regno)) {
                            Regno.setError("Regno must be filled", customErrorDrawable);
                            Regno.requestFocus();
                            return;
                        } else if (TextUtils.isEmpty(phoneno)) {
                            Phoneno.setError("Phone no must be filled", customErrorDrawable);
                            Phoneno.requestFocus();
                            return;
                        } else if (phoneno.length() < 10) {
                            Phoneno.setError("Phone no length must be 10", customErrorDrawable);
                            Phoneno.requestFocus();
                            return;

                        }else if (phoneno.contains(".")) {
                            Phoneno.setError("Contains '.' symbol ", customErrorDrawable);
                            Phoneno.requestFocus();
                            return;

                        }else if (TextUtils.isEmpty(address)) {
                            Address.setError("Address must be filled", customErrorDrawable);
                            Address.requestFocus();
                            return;
                        } else if (TextUtils.isEmpty(department)) {
                            Department.setError("Department must be filled", customErrorDrawable);
                            Department.requestFocus();
                            return;
                        } else {
                            mbase = FirebaseDatabase.getInstance().getReference();
                            mbase.addListenerForSingleValueEvent(new ValueEventListener() {


                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                    int count = (int) snapshot.child("Students").getChildrenCount();
                                    if(snapshot.child("Students").exists()) {
                                        for (int i = 1; i < count + 1; i++) {
                                            String ss=snapshot.child("Students").child(String.valueOf(i)).child("Reg No").getValue().toString();
                                            if (ss.equals(regno)) {
                                                Regno.setError("Already Exists!",customErrorDrawable);
                                                a = 1;
                                                return;

                                            } else {


                                            }
                                        }
                                    }
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (snapshot.child("photo").exists()) {




                                        if (a == 0) {
                                            mbase.child("imagesrc").child("no").setValue(count + 1);
                                            mbase.child("Students").child(String.valueOf(count + 1)).child("Name").setValue(name);
                                            mbase.child("Students").child(String.valueOf(count + 1)).child("Reg No").setValue(regno);
                                            mbase.child("Students").child(String.valueOf(count + 1)).child("Phone No").setValue(phoneno);
                                            mbase.child("Students").child(String.valueOf(count + 1)).child("DOB").setValue(date + "/" + month + "/" + year);
                                            mbase.child("Students").child(String.valueOf(count + 1)).child("Address").setValue(address);
                                            mbase.child("Students").child(String.valueOf(count + 1)).child("Department").setValue(department);

                                            uploadImage();
                                            mbase.child("photo").removeValue();
                                        }



                                    } else {
                                        Toast.makeText(getContext(), "No Photo has been  Selected!", Toast.LENGTH_LONG).show();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        }
                    }

                });
                profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(
                                Intent.createChooser(
                                        intent,
                                        "Select Image from here..."),
                                PICK_IMAGE_REQUEST);

                    }
                });
            }
        });
        return root;
    }



    public static final int PICK_IMAGE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            if(data.equals(null))
            {

                Toast.makeText(getContext(), "No Photo has been  Selected!", Toast.LENGTH_LONG).show();
            }

            // Get the Uri of data
            filePath = data.getData();
            try {
                DatabaseReference mbase1=FirebaseDatabase.getInstance().getReference();
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContext().getContentResolver(),
                                filePath);
                count1=1;
                mbase1.child("photo").setValue("1");
                profile.setImageBitmap(bitmap);
                SharedPreferences settings = getContext().getSharedPreferences(PREF_NAME,0);
                SharedPreferences.Editor editor=settings.edit();
                editor.putInt("count",count1);
                editor.commit();




            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }

        }


    }


    @Override
    public void onStart() {


        DatabaseReference mbase4= FirebaseDatabase.getInstance().getReference();
        mbase4.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(count1 == 0) {
                    if (snapshot.child("photo").exists()) {
                        mbase4.child("photo").removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        super.onStart();
    }

    @Override
    public void onStop() {
        DatabaseReference mbase3= FirebaseDatabase.getInstance().getReference();
        mbase3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.child("photo").exists()) {
                    mbase3.child("photo").removeValue();

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        DatabaseReference mbase2= FirebaseDatabase.getInstance().getReference();
        mbase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.child("photo").exists())
                {
                    mbase2.child("photo").removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        super.onDestroyView();
        binding = null;

    }
    private String getExtension(Uri uri)
    {
        ContentResolver er = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(er.getType(uri));
    }
    private void uploadImage() {
        StorageReference Ref=storageReference.child(System.currentTimeMillis()+"."+getExtension(filePath));
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(HomeFragment.this.getContext());
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            StorageReference riversRef = storageReference.child(System.currentTimeMillis()+"."+getExtension(filePath));
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            final String downloadurl = taskSnapshot.getStorage().getName();
                            mbase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                    int No = Integer.parseInt(snapshot.child("imagesrc").child("no").getValue().toString());
                                    mbase.child("Students").child(String.valueOf(No)).child("url").setValue(downloadurl);



                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });

                            Object progress= 100;
                            String geturl = "https://firebasestorage.googleapis.com/v0/b/ncc-bac.appspot.com/o/"+downloadurl+"?alt=media&token";
                            Glide.with(getContext())
                                    .load(geturl)
                                    .into(profile);
                            profile.setImageDrawable(Drawable.createFromPath(String.valueOf(R.drawable.ic_pro)));
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                            progressDialog.dismiss();
                            Glide.with(getContext())
                                    .load("https://firebasestorage.googleapis.com/v0/b/ncc-bac.appspot.com/o/round-account-button-with-user-inside.png?alt=media&token=fabc281c-6729-4f10-a348-a2a4bcc97406")
                                    .into(profile);

                            //and displaying a success toast
                            Toast.makeText(HomeFragment.this.getContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            Name.setText("");
                            Regno.setText("");
                            Phoneno.setText("");
                            Address.setText("");
                            Department.setText("");
                            onStart();
                            return;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(HomeFragment.this.getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }

                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
            Toast.makeText(HomeFragment.this.getContext(), "Error Uploading!", Toast.LENGTH_LONG).show();
        }


    }

}