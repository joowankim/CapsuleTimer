package com.example.knight.a2018_mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 * @brief show loading page when app first execute
 * @author Knight
 * @date 2018.06.02
 * @version 1.0.0.1
 */

public class Splash extends Activity {


    /**
     * @brief show loading page in 1500 milli seconds and start login screen
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 19) {
            try{
                Thread.sleep(1500);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            Intent intent = new Intent (this, Login.class);
            intent.putExtra("state","launch");
            startActivity(intent);
            finish();
        }
        else {
            setContentView(R.layout.activity_spalsh);
            Handler hd = new Handler();
            hd.postDelayed(new splashhandler(), 3000); // 1초 후에 hd handler 실행  3000ms = 3초
        }
    }

    /**
     * @brief when loading is finish, then move to login screen and remove loading page from activity stack
     */
    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), Login.class)); //로딩이 끝난 후, ChoiceFunction 이동
            Splash.this.finish(); // 로딩페이지 Activity stack에서 제거
        }
    }

    /**
     * @brief in splash screen, do not any action when back button is clicked
     */
    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }

}
