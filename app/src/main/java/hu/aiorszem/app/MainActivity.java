package hu.aiorszem.app;

import android.app.Activity;
import android.os.Bundle;
import android.os.BatteryManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Environment;
import android.os.StatFs;
import android.app.ActivityManager;
import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends Activity {
    private TextView report;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(32, 48, 32, 32);
        root.setBackgroundColor(Color.rgb(15, 23, 42));
        TextView title = new TextView(this);
        title.setText("AI-Őrszem");
        title.setTextSize(30);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);
        root.addView(title);
        TextView subtitle = new TextView(this);
        subtitle.setText("Helyi rendszerállapot és figyelmeztetések");
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setTextSize(15);
        subtitle.setGravity(Gravity.CENTER);
        subtitle.setPadding(0, 10, 0, 32);
        root.addView(subtitle);
        Button refresh = new Button(this);
        refresh.setText("Állapot frissítése");
        root.addView(refresh);
        ScrollView scroll = new ScrollView(this);
        report = new TextView(this);
        report.setTextColor(Color.WHITE);
        report.setTextSize(18);
        report.setPadding(8, 26, 8, 12);
        scroll.addView(report);
        root.addView(scroll);
        setContentView(root);
        refresh.setOnClickListener(v -> updateStatus());
        updateStatus();
    }
    private void updateStatus() {
        StringBuilder b = new StringBuilder();
        Intent battery = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = battery == null ? -1 : battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = battery == null ? -1 : battery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int pct = level >= 0 && scale > 0 ? Math.round(level * 100f / scale) : -1;
        b.append("AKKUMULÁTOR\n");
        b.append(pct >= 0 ? pct + "%" : "Nem elérhető").append("\n");
        if (pct >= 0 && pct <= 15) b.append("⚠ Alacsony töltöttség!\n");
        b.append("\nTÁRHELY\n");
        try {
            StatFs stat = new StatFs(Environment.getDataDirectory().getAbsolutePath());
            long free = stat.getAvailableBytes(), total = stat.getTotalBytes();
            b.append(String.format(Locale.getDefault(), "Szabad: %.1f GB / %.1f GB\n", free / 1e9, total / 1e9));
            if (total > 0 && free * 100 / total < 10) b.append("⚠ Kevés szabad tárhely!\n");
        } catch (Exception ex) { b.append("Nem elérhető\n"); }
        b.append("\nMEMÓRIA\n");
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (am != null) {
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);
            b.append(String.format(Locale.getDefault(), "Szabad RAM: %.2f GB\n", mi.availMem / 1e9));
            b.append(String.format(Locale.getDefault(), "Összes RAM: %.2f GB\n", mi.totalMem / 1e9));
            if (mi.lowMemory) b.append("⚠ Alacsony szabad memória!\n");
        }
        b.append("\nADATVÉDELEM\n");
        b.append("A rendszeradatok helyben kerülnek feldolgozásra.\n");
        b.append("\nEz az első, alapverzió. AI-elemzés, háttérfigyelés és értesítések még nincsenek beépítve.");
        report.setText(b.toString());
    }
}
