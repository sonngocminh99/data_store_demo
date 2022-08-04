package com.example.data_store_demo;


import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.data_store_demo.Model.Student;
import com.nifcloud.mbaas.core.DoneCallback;
import com.nifcloud.mbaas.core.FetchCallback;
import com.nifcloud.mbaas.core.FindCallback;
import com.nifcloud.mbaas.core.NCMBBase;
import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBObject;
import com.nifcloud.mbaas.core.NCMBQuery;
import com.nifcloud.mbaas.core.NCMBUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StudentRVAdapter extends RecyclerView.Adapter<StudentRVAdapter.StudentViewHolder> {
    private List<Student> students;

    public StudentRVAdapter(List<Student> students) {
        this.students = students;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_recycler_view_item,parent,false);
        return new StudentViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {

    Student student = students.get(position);

    if(student== null) return;

    if(position%2 == 0) holder.itemLayout.setBackgroundColor(Color.parseColor("#f2e7e6"));
    holder.nameText.setText(student.getName());
    holder.emailText.setText(student.getEmail());
    holder.mssvText.setText(student.getMSSV());
    holder.className.setText(student.getStudentClass().getClassName());
    holder.objectID.setText(student.getObjectID());


    holder.editBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String mssv,name,email,className,objectID;
            mssv = holder.mssvText.getText().toString();
            name = holder.nameText.getText().toString();
            email = holder.emailText.getText().toString();
            className = holder.className.getText().toString();
            objectID = holder.objectID.getText().toString();
            AddStudentDialog dialog = new AddStudentDialog(mssv,name,email,objectID,className,1);
            dialog.show(((MainActivity)view.getContext()).getSupportFragmentManager(), "edit student");

            Log.d("test","1."+ mssv);
        }
    });
    holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            deleteStudent(holder.objectID.getText().toString(),holder.className.getText().toString(),view);
            ((MainActivity) view.getContext()).clear();
            ((MainActivity) view.getContext()).getData();
        }
    });
    }

    private void deleteStudent(String objectID,String className,View view){

        NCMBObject object = new NCMBObject(className);
        try {
            object.setObjectId(objectID);
        } catch (NCMBException e) {
            e.printStackTrace();
        }
        object.deleteObjectInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if(e!=null){
                    Log.d("deleteErro",e.getMessage());
                    deleteStudent(objectID,className,view);
                }else{
                    Toast.makeText(view.getContext(),
                            "Delete success", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return students != null ? students.size():0;
    }

    class StudentViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.mssv)
        TextView mssvText;
        @BindView(R.id.name)
        TextView nameText;
        @BindView(R.id.email)
        TextView emailText;
        @BindView(R.id.item_layout)
        RelativeLayout itemLayout;
        @BindView(R.id.class_name)
        TextView className;
        @BindView(R.id.edit_student)
        ImageButton editBtn;
        @BindView(R.id.delete_student)
        ImageButton deleteBtn;
        @BindView(R.id.object_id)
        TextView objectID;
        public StudentViewHolder(@NonNull View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

    }
}
