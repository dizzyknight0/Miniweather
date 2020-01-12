package cn.edu.pku.luyongzhen.app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.luyongzhen.bean.City;
import cn.edu.pku.luyongzhen.db.CityDB;

public class MyApplication extends Application {
    private static final String TAG="MyAPP";
    private static MyApplication mApplication;
    private CityDB mCityDB;
    private List<City> mCityList;
    @Override
    public void onCreate(){
        super.onCreate();
        Log.d(TAG,"MyApplication->onCreate");
        mApplication=this;
        mCityDB=openCityDB();                                       //创建一个数据库
        initCityList();
    }
    public List<City> getmCityList(){
        return this.mCityList;
    }
    private void initCityList() {                                  //通过
        mCityList = new ArrayList<City>();
        new Thread(new Runnable() {                                 //在线程中初始化城市列表
            @Override
            public void run() {
// TODO Auto-generated method stub
                prepareCityList();                                   //初始化列表mCityList并打印出消息
            }
        }).start();
    }
        private boolean prepareCityList() {
            mCityList = mCityDB.getAllCity();
            int i=0;
            for (City city : mCityList) {
                i++;
                String cityName = city.getCity();
                String cityCode = city.getNumber();
                Log.d(TAG,cityCode+":"+cityName);
            }
            Log.d(TAG,"i="+i);
            return true;
        }
    public List<City> getCityList() {
        return mCityList;
    }
    public static MyApplication getInstance(){
        return mApplication;
    }
    private CityDB openCityDB() {                                                 //打开数据库的方法
        String path = "/data"                                                     //存放数据库文件的路径
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                + File.separator + "databases1"
                + File.separator
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        Log.d(TAG,path);
        if (!db.exists()) {
            String pathfolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName()
                    + File.separator + "databases1"
                    + File.separator;
            File dirFirstFolder = new File(pathfolder);
            if(!dirFirstFolder.exists()){
                dirFirstFolder.mkdirs();
                Log.i("MyApp","mkdirs");
            }
            Log.i("MyApp","db is not exists");
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    fos.flush();
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this, path);
    }
}
