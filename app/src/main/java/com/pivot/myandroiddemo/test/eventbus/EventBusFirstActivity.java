package com.pivot.myandroiddemo.test.eventbus;

import android.content.Intent;
import android.os.Bundle;

import com.pivot.myandroiddemo.R;
import com.pivot.myandroiddemo.base.ActivityParam;
import com.pivot.myandroiddemo.base.BaseActivity;
import com.pivot.myandroiddemo.test.shimmertext.ShimmerTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@ActivityParam(isShowToolBar = true, isShowReturn = true)
public class EventBusFirstActivity extends BaseActivity {

    private ShimmerTextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus_first);
        setToolbarTitle("EventBus1");
        getToolBarTitleView().setTextColor(getResources().getColor(R.color.white));
        setToolBarBackgroundColor(getResources().getColor(R.color.colorPrimary));

        EventBus.getDefault().register(this);
        findViewById(R.id.btn_goto_2).setOnClickListener(v -> startActivity(new Intent(mActivity, EventBusSecondActivity.class)));
        tv = findViewById(R.id.tv_event_bus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 如果使用onEvent作为订阅函数，那么该事件在哪个线程发布出来的，onEvent就会在这个线程中运行，
     * 也就是说发布事件和接收事件线程在同一个线程。使用这个方法时，在onEvent方法中不能执行耗时操作，
     * 如果执行耗时操作容易导致事件分发延迟
     */
    @Subscribe
    public void onEvent(MyEvent event) {
        System.out.println("onEvent:" + event.getmMsg());
    }

    /**
     * 如果使用onEventMainThread作为订阅函数，那么不论事件是在哪个线程中发布出来的，
     * onEventMainThread都会在UI线程中执行，接收事件就会在UI线程中运行，这个在Android中是非常有用的，
     * 因为在Android中只能在UI线程中跟新UI，所以在onEvnetMainThread方法中是不能执行耗时操作的
     */
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventMainThread123(MyEvent event) {
        String msg = "onEventMainThread收到了消息：" + event.getmMsg();
        tv.setText(msg);
        System.out.println("onEventMainThread123:" + event.getmMsg());
    }
    @Subscribe
    public void onEventMainThread(MyEvent event) {//这个方法和上一个效果一样，这个方法是默认的，上一个方法的名称是自定义的
        String msg = "onEventMainThread收到了消息：" + event.getmMsg();
        tv.setText(msg);
        System.out.println("onEventMainThread:" + event.getmMsg());
    }

    /**
     * 如果使用onEventBackgrond作为订阅函数，那么如果事件是在UI线程中发布出来的，
     * 那么onEventBackground就会在子线程中运行，如果事件本来就是子线程中发布出来的，
     * 那么onEventBackground函数直接在该子线程中执行
     */
    @Subscribe
    public void onEventBackground(MyEvent event) {
        System.out.println("onEventBackground:" + event.getmMsg());
    }

    /**
     * 使用这个函数作为订阅函数，那么无论事件在哪个线程发布，都会创建新的子线程再执行onEventAsync
     */
    @Subscribe
    public void onEventAsync(MyEvent event) {
        System.out.println("onEventAsync:" + event.getmMsg());
    }
}
