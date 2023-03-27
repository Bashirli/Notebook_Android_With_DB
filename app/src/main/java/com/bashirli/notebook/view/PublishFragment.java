package com.bashirli.notebook.view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bashirli.notebook.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

public class PublishFragment extends Fragment {
    EditText editText,editText2;
    ImageView imageView;
    Button button;
ActivityResultLauncher<String> permissionLauncher;
ActivityResultLauncher<Intent> activityResultLauncher;
Bitmap selectedImage;
Uri image;
FirebaseFirestore firestore;
FirebaseAuth auth;
FirebaseStorage storage;
StorageReference storageReference;
String info;

public PublishFragment(){

}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        firestore=FirebaseFirestore.getInstance();
        storageReference=storage.getReference();
     activitylauncher();

if(getArguments()!=null){
     info=PublishFragmentArgs.fromBundle(getArguments()).getInfo();

}else{
        info="new";
}


}





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_publish,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    editText=view.findViewById(R.id.editTextTextMultiLine);
    imageView=view.findViewById(R.id.imageView);
    button=view.findViewById(R.id.button3);
editText2=view.findViewById(R.id.editTextTextPersonName3);
    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            save(view);
        }
    });
    imageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectimage(view);

        }
    });


        if(info.equals("new")){
            button.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.select);

            editText2.setInputType(InputType.TYPE_CLASS_TEXT);
            imageView.setEnabled(true);

        }else if(info.equals("old")){
            imageView.setEnabled(false);
            button.setVisibility(View.INVISIBLE);

            editText2.setInputType(InputType.TYPE_NULL);
   String email=PublishFragmentArgs.fromBundle(getArguments()).getEmail();
   String date=PublishFragmentArgs.fromBundle(getArguments()).getDate().toString();
   firestore.collection("Note").whereEqualTo("email",email).addSnapshotListener(new EventListener<QuerySnapshot>() {
       @Override
       public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
      if(error!=null){
          Toast.makeText(requireContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG);
      }
      if(value!=null) {
          for (DocumentSnapshot snapshot : value.getDocuments()) {
              if (date.equals(snapshot.getData().get("date").toString())) {
                  editText2.setText((String) snapshot.getData().get("main"));
                  String uriLink=(String) snapshot.getData().get("URI");
                  if(!uriLink.equals("BOS")){
                      Picasso.get().load(uriLink).into(imageView);
                  }

                  editText.setText(snapshot.getData().get("note").toString());
              }
          }
      }
       }
   });


        }
    }

    public int problem_tapma(){
        if(editText.getText().toString().equals("")||editText2.getText().toString().equals("")){
            Toast.makeText(getActivity().getApplicationContext(), "Boş not və ya başlıq qeyd etmək olmaz.", Toast.LENGTH_SHORT).show();
       return 0;
        }
        return 1;
    }

    public void save(View view){

    if(problem_tapma()==0){
        return;
    }
Snackbar.make(view,"Not əlavə edilir...",Snackbar.LENGTH_LONG).show();
    if(selectedImage!=null) {
        UUID uuid = UUID.randomUUID();
    String imageData="image/"+uuid+".jpg";
    storageReference.child(imageData).putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            StorageReference newreferance=storage.getReference(imageData);
            newreferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    String uriLink=uri.toString();
                    FirebaseUser user=auth.getCurrentUser();
                    String main=editText2.getText().toString();
                    String email=user.getEmail();
                    String note=editText.getText().toString();
                    HashMap<String,Object> data=new HashMap<>();
                    data.put("email",email);
                    data.put("main",main);
                    data.put("note",note);
                    data.put("URI",uriLink);
                    data.put("date", FieldValue.serverTimestamp());
                    firestore.collection("Note").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(getActivity().getApplicationContext(), "Not uğurla əlavə edildi.", Toast.LENGTH_LONG).show();
                     NavDirections navDirections=PublishFragmentDirections.actionPublishFragmentToFirstFragment();
                            Navigation.findNavController(view).navigate(navDirections);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity().getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

        }
    });



    }
    else{
        //SEKIL OLMAYAN HAL
        FirebaseUser user=auth.getCurrentUser();
        String main=editText2.getText().toString();
        String email=user.getEmail();
        String note=editText.getText().toString();
        HashMap<String,Object> data=new HashMap<>();
        data.put("email",email);
        data.put("main",main);
        data.put("note",note);
        data.put("URI","BOS");
        data.put("date", FieldValue.serverTimestamp());
        firestore.collection("Note").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity().getApplicationContext(), "Not uğurla əlavə edildi.", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getActivity().getApplicationContext(),NoteActivity.class);
            startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity().getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    }

    public void selectimage(View view){
   if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
       if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)){
           //permission
           permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
       }else{
           Snackbar.make(view,"Icazə yoxdur.",Snackbar.LENGTH_LONG).setAction("İcazə ver", new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   //permission

                   permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
               }
           }).show();
       }
   }else{
       //activity
       Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
       activityResultLauncher.launch(intent);


   }


    }

    public void activitylauncher(){
        activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
if(result.getResultCode()==-1){
    Intent intent=result.getData();
    if(intent!=null){
         image=intent.getData();
        try {
            if(Build.VERSION.SDK_INT>=28){
                ImageDecoder.Source source=ImageDecoder.createSource(getActivity().getApplicationContext().getContentResolver(),image);
                selectedImage=ImageDecoder.decodeBitmap(source);
                imageView.setImageBitmap(selectedImage);
            }else{
                selectedImage= MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), image);
                imageView.setImageBitmap(selectedImage);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
            }
        });


        permissionLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result!=null){
                 //activity
                    Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 activityResultLauncher.launch(intent);

                }
            }
        });
    }


}