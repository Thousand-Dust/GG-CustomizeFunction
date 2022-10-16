package Thousand_Dust;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        MyWindowManager.newInstance(this);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (!MyWindowManager.isInstanceEmpty()) {
            MyWindowManager.destInstance();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            MyWindowManager.newInstance(Tools.getContext());
        }
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {
    }
}