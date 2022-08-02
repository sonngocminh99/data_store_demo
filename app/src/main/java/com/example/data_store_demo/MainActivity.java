package com.example.data_store_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.data_store_demo.Model.Student;
import com.example.data_store_demo.Model.StudentClass;
import com.nifcloud.mbaas.core.DoneCallback;
import com.nifcloud.mbaas.core.FetchCallback;
import com.nifcloud.mbaas.core.FindCallback;
import com.nifcloud.mbaas.core.NCMB;
import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBObject;
import com.nifcloud.mbaas.core.NCMBQuery;
import com.nifcloud.mbaas.core.NCMBUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class MainActivity extends AppCompatActivity implements AddStudentDialog.AddStudentDialogListener {
    public static String APP_KEY = "2bfb444423219ff54256bbe41ff270c5d8c3e81eaa3121c18603363e99b0b673";
    public static String CLIENT_KEY = "2e0167555ae06b73a73a8b2ef1ea9614d566b17cb7c0d191da80797221088bf2";

    @BindView(R.id.rv_student)
    RecyclerView recyclerView;
    @BindView(R.id.search_text)
    EditText searchText;
    List<Student> students;
    StudentRVAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        NCMB.initialize(this.getApplicationContext(),APP_KEY,CLIENT_KEY);

        students = new ArrayList<>();

        adapter = new StudentRVAdapter(students);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        getData();

    }

public void clear(){
        students.clear();
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
                            getStudentInClass(obj.getString("ClassName"));
                        }
                        Log.d("Minh", results.size() + "");
                    }
                }
            }
        });

    }
    private void getStudentInClass(String className){

        NCMBQuery<NCMBObject> query = new NCMBQuery<>(className);
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if(e!=null) {Log.d("errorStudent", e.getMessage()+e.getCode());
                getStudentInClass(className);
                }
                else {
                    if (results != null) {
                        for (NCMBObject obj : results) {
                            Student student = new Student();
                            student.setMSSV(obj.getString("MSSV"));
                            student.setName(obj.getString("Name"));
                            student.setEmail(obj.getString("Email"));
                            student.setStudentClass(new StudentClass(obj.getClassName()));
                            student.setObjectID(obj.getObjectId());
                            students.add(student);
                        }

                        adapter.notifyDataSetChanged();
                        recyclerView.refreshDrawableState();
                    }
                }
            }
        });

    }
    private void editStudent(String objectID,String name,String email,String mssv,String className){

        NCMBObject obj = new NCMBObject("Class_2");
        try {
            obj.setObjectId(objectID);
            obj.put("MSSV", mssv);
            obj.put("Name",name);
            obj.put("Email",email);
        } catch (NCMBException e) {
            e.printStackTrace();
        }
        Log.d("Edit",obj.getString("MSSV")+"1");

        obj.saveInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e != null) {

                    Log.d("errorEdit",e.getMessage()+e.getCode());
                } else {
                        students.clear();
                        getData();
                }
            }
        });
    }
    private void AddNewStudent(String mssv, String name, String email, String className) throws NCMBException {
        NCMBObject obj = new NCMBObject(className);
        obj.put("MSSV", mssv);
        obj.put("Name",name);
        obj.put("Email",email);
        obj.saveInBackground(new DoneCallback() {
            @Override
            public void done(NCMBException e) {
                if (e != null) {

                    Log.d("errorAdd",e.getMessage());
                } else {

                    Toast.makeText(MainActivity.this,
                            "Add success", Toast.LENGTH_LONG).show();
                    students.clear();
                    getData();
                }
            }
        });
    }
    @OnClick(R.id.add_student)
    void AddStudentClick(){
        AddStudentDialog dialog = new AddStudentDialog(0);
        dialog.show(MainActivity.this.getSupportFragmentManager(),"add student");
    }
    @OnTextChanged(R.id.search_text)
    void searchTextChange(){

        NCMBQuery<NCMBObject> query = new NCMBQuery<>("StudentClass");
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if(e!=null) {Log.d("errorData", e.getMessage());
                searchTextChange();}
                else {
                    if (results != null) {
                        for (NCMBObject obj : results) {
                            findStudent(obj.getString("ClassName"));

                        }

                        Log.d("Minh", results.size() + "");
                    }
                }
            }
        });

    }
    private void findStudent(String className){
        NCMBQuery query = new NCMBQuery(className);
        String keyword = searchText.getText().toString();
        query.whereEqualTo("MSSV", keyword);
        query.findInBackground(new FindCallback<NCMBObject>() {
            @Override
            public void done(List<NCMBObject> results, NCMBException e) {
                if (e != null) {
                    Log.d("errorFind", e.getMessage());
                    findStudent(className);
                }
                else{
                    for (NCMBObject obj : results) {
                        Student student = new Student();
                        student.setMSSV(obj.getString("MSSV"));
                        student.setName(obj.getString("Name"));
                        student.setEmail(obj.getString("Email"));
                        student.setStudentClass(new StudentClass(obj.getClassName()));
                        student.setObjectID(obj.getObjectId());
                        if(student != students.get(0))
                        {   students.clear();
                            students.add(student);}
                    }

                    adapter.notifyDataSetChanged();
                    recyclerView.refreshDrawableState();

                }
            }
        });
    }
    @Override
    public void applyTexts(String mssv, String name, String email, String className,String objectID, int type) {
        if(type==0){
            try {
                AddNewStudent(mssv, name, email, className);
            } catch (NCMBException e) {
                e.printStackTrace();
            }
        }else {
            editStudent(objectID, name, email, mssv, className);
            Log.d("test",className);
        }
    }
}