package com.creativeinfoway.floatingwidget;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.creativeinfoway.floatingwidget.Service.FloatingViewService;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION=2084;
//comment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**check if the permission has draw over other apps or not?
         *   this permission is by default available for api<23
         *   But for api>23 you have to ask for the permission in runtime.
         **/

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)){

            /**
             * if the draw over permission is not available open the settings screen
             * to grant the permission.
             */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:"+ getPackageName()));
            startActivityForResult(intent,CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }
        else{
            initializeView();
        }
    }

    private void initializeView() {
        findViewById(R.id.notify_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, FloatingViewService.class));
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION)
        {
            //Check if the permission is granted or not
            if(resultCode == RESULT_OK){
                initializeView();
            }
            else{
                //permission is not available
                Toast.makeText(this, "Draw over other app permission not available. Closing the application", Toast.LENGTH_SHORT).show();

                finish();
            }
        }
        else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}
