package com.pivot.myandroiddemo.test.rx;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.pivot.myandroiddemo.R;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

public class RxJavaDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_demo);

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {

            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.e("tag", "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("tag", e.getMessage());
            }

            @Override
            public void onNext(String s) {
                Log.e("tag", s);
            }

            @Override
            public void onStart() {
                Log.e("tag", "start");
            }
        };

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.e("tag", s);
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e("tag", throwable.getMessage());
            }
        };

        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Log.e("tag", "completed");
            }
        };

        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onNext("3");
                subscriber.onCompleted();
            }
        });
        Observable<String> observable1 = Observable.just("1", "2", "3");
        String[] arr = new String[]{"1", "2", "3"};
        Observable<String> observable2 = Observable.from(arr);
//        observable.subscribe(subscriber);
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
    }
}