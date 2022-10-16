package Thousand_Dust;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
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
        return super.onUnbind(intent);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {
    }
}