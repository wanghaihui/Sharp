package com.conquer.exoplayer.core.offline;

import android.app.Notification;
import android.app.Service;
import android.support.annotation.Nullable;

import com.conquer.exoplayer.core.scheduler.Scheduler;

/** A {@link Service} for downloading media. */
public abstract class DownloadService extends Service {

    /** Invalid(无效的) foreground notification id which can be used to run the service in the background. */
    public static final int FOREGROUND_NOTIFICATION_ID_NONE = 0;

    /** Default foreground notification update interval in milliseconds. */
    // 默认的前台通知更新间隔(毫秒)
    public static final long DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL = 1000;

    /**
     * Creates a DownloadService.
     *
     * <p>If {@code foregroundNotificationId} is {@link #FOREGROUND_NOTIFICATION_ID_NONE} (value
     * {@value #FOREGROUND_NOTIFICATION_ID_NONE}) then the service runs in the background. No
     * foreground notification is displayed and {@link #getScheduler()} isn't called.
     *
     * <p>If {@code foregroundNotificationId} isn't {@link #FOREGROUND_NOTIFICATION_ID_NONE} (value
     * {@value #FOREGROUND_NOTIFICATION_ID_NONE}) the service runs in the foreground with {@link
     * #DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL}. In that case {@link
     * #getForegroundNotification(TaskState[])} should be overridden in the subclass.
     *
     * @param foregroundNotificationId The notification id for the foreground notification, or {@link
     *     #FOREGROUND_NOTIFICATION_ID_NONE} (value {@value #FOREGROUND_NOTIFICATION_ID_NONE})
     */
    protected DownloadService(int foregroundNotificationId) {
        this(foregroundNotificationId, DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL);
    }




    /**
     * Returns a {@link Scheduler} to restart the service when requirements allowing downloads to take
     * place are met(当满足允许下载的要求时). If {@code null}, the service will only be restarted if the process is still in
     * memory(进程仍在内存中) when the requirements are met.
     */
    protected abstract @Nullable Scheduler getScheduler();

    /**
     * Should be overridden in the subclass if the service will be run in the foreground.
     *
     * <p>Returns a notification to be displayed when this service running in the foreground.
     *
     * <p>This method is called when there is a task state change and periodically while there are
     * active tasks. The periodic update interval can be set using {@link #DownloadService(int,
     * long)}.
     *
     * <p>On API level 26 and above, this method may also be called just before the service stops,
     * with an empty {@code taskStates} array. The returned notification is used to satisfy system
     * requirements for foreground services.
     *
     * @param taskStates The states of all current tasks.
     * @return The foreground notification to display.
     */
    protected Notification getForegroundNotification(TaskState[] taskStates) {
        throw new IllegalStateException(
                getClass().getName()
                        + " is started in the foreground but getForegroundNotification() is not implemented.");
    }
}
