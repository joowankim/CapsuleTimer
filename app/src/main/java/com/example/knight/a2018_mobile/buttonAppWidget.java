package com.example.knight.a2018_mobile;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class buttonAppWidget extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        if(AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(action))
        {
            Bundle extras = intent.getExtras();
            //Bundle 은 Key-Value 쌍으로 이루어진 일종의 해쉬맵 자료구조
            //한 Activity에서 Intent 에 putExtras로 Bundle 데이터를 넘겨주고,
            //다른 Activity에서 getExtras로 데이터를 참조하는 방식입니다.
            if(extras!=null)
            {
                int [] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
                if(appWidgetIds!=null && appWidgetIds.length>0)
                    this.onUpdate(context,AppWidgetManager.getInstance(context),appWidgetIds);
            }
        }//업데이트인 경우
        else if(action.equals("Click1"))
        {
            Toast.makeText(context,"Hello",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        final int N = appWidgetIds.length;
        for(int i=0;i<N;i++)
        {
            int appWidgetId = appWidgetIds[i];
            RemoteViews views = buildViews(context);
            appWidgetManager.updateAppWidget(appWidgetId,views);
        }
    }

    private PendingIntent buildURIIntent(Context context)
    {
        // click에 대한 반응으로 ACTION_VIEW를 통해 블로그 창을 띄운다
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("http://puzzleleaf.tistory.com"));
        PendingIntent pi = PendingIntent.getActivity(context,0,intent,0);
        return pi;
    }

    //Click1 이라는 Action을 onReceive로 보낸다.
    private PendingIntent buildToastIntent(Context context)
    {
        // manifest에 지정해둔 Click1 액션으로 지정해 인텐트를 보낸 후 if문을 통해 다음 action을 결정한다
        Intent in = new Intent("Click1");
        PendingIntent pi = PendingIntent.getBroadcast(context,0,in, PendingIntent.FLAG_UPDATE_CURRENT);
        return pi;
    }

    //위젯에 멀티 버튼 추가하기
    private RemoteViews buildViews(Context context)
    {
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.button_app_widget);
        views.setOnClickPendingIntent(R.id.simple_btn,buildURIIntent(context));
        views.setOnClickPendingIntent(R.id.simple_btn2,buildToastIntent(context));

        return views;
    }

}

