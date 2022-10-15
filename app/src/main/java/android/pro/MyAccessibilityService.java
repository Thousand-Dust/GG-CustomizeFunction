package android.pro;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService {

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        MyWindowManager.newInstance(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("MyAccessibilityService", "onAccessibilityEvent: ");
    }

    @Override
    public void onInterrupt() {
        Log.d("MyAccessibilityService", "onInterrupt: ");
    }
}