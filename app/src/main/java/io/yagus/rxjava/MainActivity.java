package io.yagus.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.internal.operators.observable.ObservableFromArray;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        // just() 함수 - 인자로 넣은 데이터를 차례로 발행하려고 Observable을 생성합니다.

        Observable.just("Hello, world!")
                .subscribe(textView::setText);

        // fromArray() 함수

        Integer[] arr = {100, 200, 300};
        Observable<Integer> source_fromArray = Observable.fromArray(arr);
        source_fromArray.subscribe(o->Log.d("fromArray:",o.toString()));

        // fromIterable() 함수

        List<String> names = new ArrayList<>();
        names.add("Jerry");
        names.add("William");
        names.add("Bob");

        Observable<String> source_fromIterable = Observable.fromIterable(names);
        source_fromIterable.subscribe(o->Log.d("fromIterable: ",o.toString()));


        // map() 함수 - 입력값을 어떤 함수에 넣어서 원하는 값으로 변환하는 함수

        String[] balls = {"1", "2", "3", "5"};
        Observable<String> source_map = Observable.fromArray(balls)
                .map(ball -> ball+"<>");
        source_map.subscribe(o->Log.d("map: ", o.toString()));

        // filter() 함수 - if문 대체

        String[] objs = {"1 CIRCLE", "2 DIAMOND", "3 TRIANGLE", "4 DIAMOND", "5 CIRCLE", "6 HEXAGON"};
        Observable<String> source_filter = Observable.fromArray(objs)
                .filter(obj -> obj.endsWith("CIRCLE"));
        source_filter.subscribe(o->Log.d("filter: ",o.toString()));

        // reduce() 함수 - 발행한 데이터를 모두 사용하여 최종 결과 데이터를 합성할 때 활용합니다.

        Maybe<String> source_reduce = Observable.fromArray(balls)
                .reduce((ball1, ball2) -> balls + "(" + ball1 + ")");
        source_reduce.subscribe(o->Log.d("reduce: ",o.toString()));

        // range() 함수 - for문 대체
        Observable<Integer> source_range = Observable.range(1,10)
                .filter(number -> number % 2 == 0);
        source_range.subscribe(o->Log.d("range: ",o.toString()));

        // groupBy() 함수 - switch문 대체


        // zip() 함수 - 2개 이상의 Observable을 결합
        String[] shapes = {"BALL", "PENTAGON", "STAR"};
        String[] coloredTriangles = {"2-T", "6-T", "4-T"};

        Observable<String> source_zip = Observable.zip(
                Observable.fromArray(shapes),
                Observable.fromArray(coloredTriangles),
                (suffix, color) ->  suffix + " " + color);

        source_zip.subscribe(o->Log.d("zip: ",o.toString()));

        // combineLatest() 함수 - 2개 이상의 Observable을 기반으로 Observable 각각의 값이 변경되었을 때 갱신해주는 함수



       /* 스케줄러:
       1.계산스케줄러: 일반적인 계산 작업
       2.IO스케줄러: 네트워크상의 요청, 파일 입출력, DB퀴리 등
       3.트램펄린스케줄러: 새 스레드를 생성하지 않고 현재 스레드에 무한한 크기의 대기 행렬을 생성하는 스케줄러*/

       // 계산 스케줄러
       String[] orgs = {"1", "3", "5"};
       Observable<String> source = Observable.fromArray(orgs)
               .zipWith(Observable.interval(100L, TimeUnit.MICROSECONDS), (a,b) -> a);

       source.map(item -> "computation: <<" + item + ">>")
               .subscribeOn(Schedulers.computation())
               .subscribe(o->Log.d("computation_ex",o.toString()));

       source.map(item -> "computation: ##" + item + "##")
               .subscribeOn(Schedulers.computation())
               .subscribe(o->Log.d("computation_ex2",o.toString()));

       // 트램펄린 스케줄러
        source.subscribeOn(Schedulers.trampoline())
                .map(data -> "trampoline: <<" + data + ">>")
                .subscribe(o -> Log.d("trampoline_ex1",o.toString()));

        source.subscribeOn(Schedulers.trampoline())
                .map(data -> "trampoline: ##" + data + "##")
                .subscribe(o -> Log.d("trampoline_ex2",o.toString()));

    }
}
