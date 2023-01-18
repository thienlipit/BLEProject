package com.example.opengl.bouncycube;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.WindowManager;

public class BouncyCubeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        GLSurfaceView view = new GLSurfaceView(this);
        view.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);  //fix No config chosen error.  No idea what this actually does though.
        view.setRenderer(new BouncyCubeRenderer(true));
        setContentView(view);
    }
}