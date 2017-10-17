package com.example.administrator.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private User user;
    private UserDao userDao;
    private DaoSession daoSession;
    private DaoMaster.DevOpenHelper devOpenHelper;
    private DaoMaster daoMaster;
    private TextView textView;
    private Button btn_add,btn_delete,btn_updata,btn_find;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        devOpenHelper = new DaoMaster.DevOpenHelper(getApplication(), "user-db", null);
        daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession = daoMaster.newSession();
        userDao = daoSession.getUserDao();

        initView();

    }

    private void initView()
    {
        textView = (TextView) findViewById(R.id.textView);
        btn_add = (Button) findViewById(R.id.add);
        btn_delete = (Button) findViewById(R.id.delete);
        btn_updata = (Button) findViewById(R.id.updata);
        btn_find = (Button) findViewById(R.id.find);

        btn_add.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_updata.setOnClickListener(this);
        btn_find.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.add:
                userDao.insert(new User(null,"1","dmz"));
                break;
            case R.id.delete:
                User findUser = userDao.queryBuilder().where(UserDao.Properties.Nickname.eq("newDmin")).build().unique();
                if(findUser != null){
                    userDao.deleteByKey(findUser.getId());
                }
                break;
            case R.id.updata:
                User findUser_updata = userDao.queryBuilder().where(UserDao.Properties.Nickname.eq("dmz")).build().unique();
                if(findUser_updata != null) {
                    findUser_updata.setNickname("newDmin");
                    userDao.update(findUser_updata);
                    Toast.makeText(getApplication(), "修改成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "用户不存在", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.find:
                List<User> userList = userDao.queryBuilder()
                        .where(UserDao.Properties.Id.notEq(999))
                        .orderAsc(UserDao.Properties.Id)
                        .limit(5)
                        .build().list();
                for (int i=0;i<userList.size();i++)
                {
                    Log.i("dmin",userList.get(i).getNickname().toString());
                }
                break;
        }
    }
}
