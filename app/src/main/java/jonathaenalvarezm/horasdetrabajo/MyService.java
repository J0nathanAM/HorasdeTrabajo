package jonathaenalvarezm.horasdetrabajo;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;


/**
 * Created by IDS Comercial on 05/10/2017.
 */

public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //Thread.sleep(3600000*48);
                    Thread.sleep(60000);

                    NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(
                            getBaseContext())
                            .setSmallIcon(R.drawable.icono)
                            .setContentTitle("Horas de Trabajo")
                            .setContentText("Usted tiene 48hrs. sin realizar registro de horas de trabajo !")
                            .setWhen(System.currentTimeMillis());

                    nManager.notify(10, builder.build());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        this.stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }
}
