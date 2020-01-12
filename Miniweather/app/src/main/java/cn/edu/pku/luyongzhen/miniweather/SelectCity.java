package cn.edu.pku.luyongzhen.miniweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.luyongzhen.app.MyApplication;
import cn.edu.pku.luyongzhen.bean.City;

public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackbtn;
    private ListView mCityView;
    private List<String> cityCodes=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);                                      //加载select_city布局
        mBackbtn=(ImageView) findViewById(R.id.title_back);
        mBackbtn.setOnClickListener(this);
       initiaView();                                                               //初始化ListView控件
    }
    void initiaView(){
        mCityView=(ListView) findViewById(R.id.tite_list);
        List<String> cityName=new ArrayList<String>();
        List<City> citis=MyApplication.getInstance().getmCityList();
        for(int i=0;i<citis.size();i++){
            cityName.add(citis.get(i).getCity());
            cityCodes.add(citis.get(i).getNumber());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cityName);
        mCityView.setAdapter(adapter);
        mCityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?>adapterView, View view, int i, long l) {
                Intent ite=new Intent();
                ite.putExtra("cityCode",cityCodes.get(i));                //返回时，带回101160101城市的citycode，用来更新主界面城市天气
                setResult(RESULT_OK,ite);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v){                                                   //设置onClick事件响应方式
        switch (v.getId()){
            case R.id.title_back:                                                 //如果是返回，返回主界面
                Intent i=new Intent();
                i.putExtra("cityCode","101160101");                //返回时，带回101160101城市的citycode，用来更新主界面城市天气
                setResult(RESULT_OK,i);
            finish();
            break;
            default:
                break;
        }
    }
}
