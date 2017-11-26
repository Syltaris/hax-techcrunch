package com.tuanzi.baidumap;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    /** 郑重说明 |: appkey  根据官网指示，自己去申请，之后替换掉本项目minifast中即可。另外，模拟器不支持定位的，会崩掉，建议真机运行*/
    MapView mMapView = null;
    ///////////第一步
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private double myLatitude;
    private double myLongitude;
    private BaiduMap mBaidumap;
    boolean isFirstLoc = true;// 是否首次定位
    private View layout;
    private TextView endPoint;
    private AlertDialog dialog;
    private String mMap_WeiZhi;
    private Button gift;


    /*public enum Gift{
        money,mei_gui,tick;
    }*/
    int money=1;

    int mei_gui=2;
    int tick=3;
     int giftSelect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
//        SDKInitializer.initialize(this);
        ///这个地方我在application 写过了，就注释了，避免重复
        setContentView(R.layout.activity_main);
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaidumap =  mMapView.getMap();
        mBaidumap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        gift=(Button) findViewById(R.id.gift);
        gift.setOnClickListener(this);
        ///////////第一步初始化控件
        mLocationClient = new LocationClient(this);     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        ///////////配置
        initLocation();
        ///////////第五步开启定位
        mLocationClient.start();
        ///////////开启导航
        initView(mMapView,this);
    }

    ///////////第二步配置地图设置
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }
    ///////////第三步设置定位当前位置时间，并且添加当前位置图标
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // 取经纬度
            myLatitude = location.getLatitude();
            myLongitude = location.getLongitude();

            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            if (locData != null && mBaidumap != null)
                mBaidumap.setMyLocationData(locData);
            LatLng = new LatLng(location.getLatitude(),location.getLongitude());

            if (isFirstLoc) {/////判断是不是第一次定位
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaidumap.animateMapStatus(u);
            }
            mMap_WeiZhi =location.getAddrStr();

//            Toast.makeText(MainActivity.this,"myLatitude:"+myLatitude+"\n"+"myLongitude:"+myLongitude,Toast.LENGTH_SHORT).show();
            //////////////////添加显示自己位置图标
            addMarker(myLatitude,myLongitude);
        }
    }

    /** 添加地图图标方法*/

    public void  addMarker(double myLatitude,double myLongitude ){
        //定义Maker坐标点
        LatLng point = new LatLng(myLatitude, myLongitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.marker);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaidumap.addOverlay(option);
    }

    /**添加礼物图标**/

    public void addGift(double myLatitude,double myLongitude ){
        //定义Gift坐标点
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.money);
        LatLng point = new LatLng(myLatitude, myLongitude);
        //构建Gift图标
        switch(giftSelect){
            case 1:
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.money);
                break;
            case 2:
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.mei_gui);
                break;
            case 3:
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.mipmap.tick);
                break;
            default:
                break;

        }

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaidumap.addOverlay(option);


    }

    /*弹出礼物清单*/
    @Override
    public void onClick(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        MenuInflater inflater = popupMenu.getMenuInflater();
        //添加点击事件
        inflater.inflate(R.menu.popupmenu, popupMenu.getMenu());
        //绑定菜单项的点击事件
        popupMenu.setOnMenuItemClickListener(this);
        //显示(这一行代码不要忘记了)
        popupMenu.show();
    }

    //弹出式菜单的单击事件处理
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.money:
                giftSelect =money;
                break;
            case R.id.mei_gui:
                giftSelect =mei_gui;
                break;
            case R.id.tick:
                giftSelect=tick;
                break;
            default:
                break;


        }
        return false;




    }





    /*礼物放好后，设置能够获得者条件*/
     public void appointOwer (){


         //上面选定好礼物的类型后，点击可以投放相对应的礼物

        /* View view = View.inflate(this, R.layout.dialog_phone, null);
         TextView tv_dl_title = (TextView) view.findViewById(R.id.ll_tishi);
         tv_dl_title.setText("是否确认地图导航");


         dialog = new AlertDialog.Builder(this).create();
         dialog.show();
         dialog.getWindow().setContentView(view);
         WindowManager.LayoutParams ps = dialog.getWindow().getAttributes();
         dialog.getWindow().setAttributes(ps);*/

     }






    /** 地图导航   实际是初始化地图在上面进行了*/
    public void initView(MapView mMapView, Context context) {
        // 获得 LayoutInflater 实例
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.custom_text_view, null);
        endPoint = (TextView)layout.findViewById(R.id.driver_location_name);
        // 构造一个更新地图的msu对象，然后设置该对象为缩放等级14.0，最后设置地图状态。
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaidumap.setMapStatus(msu);
//        mBaidumap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                // 获得marker中的数据
//                GetOkBack(marker);
//                return true;
//            }
//        });

        ////////设置地图点击事件，获取当前点击位置的经纬度，并且添加图标
        mBaidumap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double   lat = latLng.latitude;
                double   lon  = latLng.longitude;
                addGift(lat,lon);   //添加了图标，可改成添加礼物
                appointOwer ();



                //后面的代码把位置信息保存到info,以提供导航位置
            //   latLng.describeContents();
               // Dingwei_Info.Info info = new Dingwei_Info.Info();
              //  info.setLatitude(lat);
               // info.setLongitude(lon);
               // GetOkBack(info);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }

    /*///////点击地图上某一点 弹出对话框，提示是否导航
    public  void GetOkBack(final Dingwei_Info.Info info){
        View view = View.inflate(this, R.layout.dialog_phone, null);
        TextView tv_dl_title = (TextView) view.findViewById(R.id.ll_tishi);
        tv_dl_title.setText("是否确认地图导航");

        ((Button) view.findViewById(R.id.bt_dialog_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ( (LinearLayout) view.findViewById(R.id.ll_tishi_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ((LinearLayout) view.findViewById(R.id.ll_tishi_ok)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Dingwei_Info.Info info = (Dingwei_Info.Info) marker.getExtraInfo().get("info");
                startNavi(info.getLatitude(),info.getLongitude(),info.getName());
                dialog.dismiss();
            }
        });
        //对话框
        dialog = new AlertDialog.Builder(this).create();
        dialog.show();
        dialog.getWindow().setContentView(view);
        WindowManager.LayoutParams ps = dialog.getWindow().getAttributes();
        dialog.getWindow().setAttributes(ps);
    }


    /**
     * 启动百度地图导航(Native)
     * @param Endname
     *
     */
    public LatLng LatLng;
    /*public void startNavi(double lat,double lng, String Endname) {
        LatLng pt2 = new LatLng(lat,lng);
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(LatLng).endPoint(pt2)
                .startName(mMap_WeiZhi).endName(Endname);
        try {
            BaiduMapNavigation.openBaiduMapNavi(para, this);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
            showDialog();
        }
    }*/

    /*
     * 提示未安装百度地图app或app版本过低
     *

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(MainActivity.this);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
