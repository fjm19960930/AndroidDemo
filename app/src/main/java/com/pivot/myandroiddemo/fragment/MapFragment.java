package com.pivot.myandroiddemo.fragment;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.BaseFragment;
import com.pivot.myandroiddemo.base.FragmentParam;
import com.zcolin.frame.util.SPUtil;

import androidx.annotation.Nullable;

import static com.baidu.mapapi.map.MapViewLayoutParams.ALIGN_LEFT;
import static com.baidu.mapapi.map.MapViewLayoutParams.ALIGN_TOP;

/**
 * 地图Fragment
 */
@FragmentParam(isShowToolBar = true)
public class MapFragment extends BaseFragment {
    private MapView mMapView;
    private BaiduMap mMapController;//地图控制器
    private LocationClient mLocationClient;//定位服务控制器
    private LatLng location;//当前定位位置
    private boolean showInfoWindowEnable = true;//是否显示InfoWindow

    private volatile static MapFragment instance = null;
    public static MapFragment newInstance() {
        if (instance == null) {
            synchronized (MapFragment.class) {
                if (instance == null) {
                    Bundle args = new Bundle();
                    instance = new MapFragment();
                    instance.setArguments(args);
                    return instance;
                }
            }
        }
        return instance;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mLocationClient.restart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        mLocationClient.stop();//当应用进入后台时停止定位服务避免耗电过度
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        mMapController.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    protected int getRootViewLayId() {
        return R.layout.fragment_map;
    }

    @Override
    protected void lazyLoad(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void createView(@Nullable Bundle savedInstanceState) {
        super.createView(savedInstanceState);
        setToolbarTitle("我的足迹", Color.WHITE);
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));

        mMapView = getView(R.id.map_view);
        mMapController = mMapView.getMap();//获取地图控制器
        mMapController.setCompassEnable(false);//不显示指南针
        mMapController.setMyLocationEnabled(true);//开启定位图层
        getLocation();
//        mMapController.setTrafficEnabled(true);//添加交通图
//        mMapController.setBaiduHeatMapEnabled(true);//添加热力图，地图层级在11-20时才会显示热力

        //在地图上添加按钮面板并添加点击响应
        View mapBtnView = LayoutInflater.from(getContext()).inflate(R.layout.map_btn_layout, null);
        mapBtnView.findViewById(R.id.map_btn_layer).setOnClickListener(v -> {//图层
            mMapController.setMapType(mMapController.getMapType() == 1 ? BaiduMap.MAP_TYPE_SATELLITE : BaiduMap.MAP_TYPE_NORMAL);//图层在常规和卫星图之间切换
        });
        mapBtnView.findViewById(R.id.map_btn_location).setOnClickListener(v -> {//定位
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(location).zoom(18.0f);
            mMapController.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        });
        mapBtnView.findViewById(R.id.map_btn_change).setOnClickListener(v -> {});
        mapBtnView.findViewById(R.id.map_btn_clear).setOnClickListener(v -> mMapController.clear());//清除地图上的所有覆盖物以及InfoWindow
        MapViewLayoutParams params = new MapViewLayoutParams.Builder().align(ALIGN_LEFT, ALIGN_TOP).point(new Point(960,30)).build();
        mMapView.addView(mapBtnView, params);
    }

    /**
     * 获取当前位置信息
     */
    private void getLocation() {
        mLocationClient = new LocationClient(getContext());//定位初始化

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setIsNeedLocationDescribe(true);//是否需要当前位置文字描述，可以在BDLocation.getLocationDescribe里得到
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        
        mLocationClient.setLocOption(option);//设置locationClientOption
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {//注册LocationListener监听器
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null || mMapView == null){
                    return;
                }
                System.out.println(bdLocation.getLocType() + bdLocation.getCity());
                SPUtil.putString("locationCity", bdLocation.getCity());
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        .direction(bdLocation.getDirection())// 此处设置开发者获取到的方向信息，顺时针0-360
                        .latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude())
                        .build();
                location = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                mMapController.setMyLocationConfiguration(new MyLocationConfiguration(//配置定位点的样式、颜色等
                        MyLocationConfiguration.LocationMode.NORMAL,//定位图层显示方式
                        true,//是否显示方向信息
                        BitmapDescriptorFactory.fromResource(R.mipmap.map_icon_position),//自定义定位点图标
                        0x88FFFF88,//精度圈填充色
                        0xAA00FF00//精度圈边框颜色
                ));
                mMapController.setOnMyLocationClickListener(() -> {//设置定位点点击响应，弹出自定义marker显示当前位置信息
                    View markerView = LayoutInflater.from(getContext()).inflate(R.layout.map_location_marker_layout, null);
                    TextView tvLocation = markerView.findViewById(R.id.tv_map_location);//位置描述
                    TextView tvLng = markerView.findViewById(R.id.tv_map_location_lng);//经度
                    TextView tvLat = markerView.findViewById(R.id.tv_map_location_lat);//纬度
                    tvLocation.setText(bdLocation.getLocationDescribe());
                    tvLng.setText(String.valueOf(bdLocation.getLongitude()));
                    tvLat.setText(String.valueOf(bdLocation.getLatitude()));
                    
                    //以这种方式添加markerView，marker里面的控件是可以获取到焦点的，每个控件都有点击响应
                    InfoWindow infoWindow = new InfoWindow(markerView, location, -50);//-50是y方向上的偏移
                    if (showInfoWindowEnable) {
                        mMapController.showInfoWindow(infoWindow);
                    } else {
                        mMapController.hideInfoWindow();
                    }
                    showInfoWindowEnable = !showInfoWindowEnable;
                    
                    //以这种方式添加markerView，里面的控件获取不到焦点，只有markerView整体有点击响应
//                    BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(MapUtils.getBitmapFromView(markerView));//view转bitmap
//                    OverlayOptions option = new MarkerOptions().position(location).icon(bitmap).perspective(true).yOffset(-50);
//                    mMapController.addOverlay(option);
//                    mMapController.setOnMarkerClickListener(marker -> false);
                    return false;
                });
                mMapController.setMyLocationData(locData);
            }
        });
        mLocationClient.start();//开启地图定位
    }
}
