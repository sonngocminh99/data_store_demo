package com.example.data_store_demo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.nifcloud.mbaas.core.FindCallback;
import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBObject;
import com.nifcloud.mbaas.core.NCMBQuery;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddStudentDialog extends AppCompatDialogFragment{

    @BindView(R.id.email)
    EditText emailText;
    @BindView(R.id.name)
    EditText nameText;
    @BindView(R.id.mssv)
    EditText mssvText;
    @BindView(R.id.spinner)
    Spinner classNameText;
    String mssv,name,email,objectID,className;

    List<String> classNames;
    ArrayAdapter<String> adapter;
    int type;
    AddStudentDialogListener listener;
    String btnTitle,title;
    public AddStudentDialog(int type) {
        this.type = type;
    }

    public AddStudentDialog( String mssv, String name, String email,String objectID,String className, int type) {

        this.mssv = mssv;
        this.name = name;
        this.email = email;
        this.objectID = objectID;
        this.className = className;
        this.type = type;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_student_dialog,null);
        ButterKnife.bind(this,view);

        classNames= new ArrayList<>();
        adapter = new ArrayAdapter<String>(view.getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,classNames);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        classNameText.setAdapter(adapter);
        if(type == 0) {title = "Add New Student"; btnTitle = "add";
        classNameText.setVisibility(View.VISIBLE);
        getData();
        }
        else {title = "Edit Student"; btnTitle = "Save";
        classNameText.setVisibility(View.INVISIBLE);}
        this.mssvText.setText(mssv);
        this.nameText.setText(name);
        this.emailText.setText(email);
        builder.setView(view)
                .setTitle(title)
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton(btnTitle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String mssv,name,email,class_Name;
                        mssv = mssvText.getText().toString();
                        name = nameText.getText().toString();
                        email = emailText.getText().toString();
                        if(type==0) { class_Name = classNameText.getSelectedItem().toString();}
                        else class_Name=className;
                        listener.applyTexts(mssv,name,email,class_Name,objectID,type);
                    }
                });

        return builder.create();
    }
    public void getData(){
        NCMBQuery<NCMBObject> query = new NCMBQuery<>("StudentClass");
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if (e != null) {
                    Log.d("errorData", e.getMessage());
                    getData();
                }
                else {
                    if (results != null) {
                        for (NCMBObject obj : results) {
                            classNames.add(obj.getString("ClassName"));
                        }
                        Log.d("Minh", results.size() + "");

                        adapter.notifyDataSetChanged();
                        classNameText.refreshDrawableState();
                    }
                }
            }
        });

    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddStudentDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implements listener");
        }

    }

    public interface AddStudentDialogListener{
        void applyTexts(String mssv,String name,String email,String className,String objectID,int type);
    }
}
