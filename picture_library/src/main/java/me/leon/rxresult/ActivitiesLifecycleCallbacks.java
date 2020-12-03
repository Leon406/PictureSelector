package me.leon.rxresult;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;


import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

class ActivitiesLifecycleCallbacks {
   final Application application;
   volatile Activity liveActivityOrNull;
   Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;
   /**
    * Emits just one time a valid reference to the current activity
    *
    * @return the current activity
    */
   volatile boolean emitted = false;

   public ActivitiesLifecycleCallbacks(Application application) {
       this.application = application;
       registerActivityLifeCycle();
   }

   private void registerActivityLifeCycle() {
       if (activityLifecycleCallbacks != null)
           application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);

       activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
           @Override
           public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
               liveActivityOrNull = activity;
           }

           @Override
           public void onActivityStarted(Activity activity) {
               liveActivityOrNull = activity;

           }

           @Override
           public void onActivityResumed(Activity activity) {
               liveActivityOrNull = activity;
           }

           @Override
           public void onActivityPaused(Activity activity) {
               liveActivityOrNull = null;
           }

           @Override
           public void onActivityStopped(Activity activity) {
           }

           @Override
           public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
           }

           @Override
           public void onActivityDestroyed(Activity activity) {
           }
       };

       application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
   }

   @Nullable
   public Activity getLiveActivity() {
       return liveActivityOrNull;
   }

   @Nullable
   public void setLiveActivity(Activity activity) {
       liveActivityOrNull = activity;
   }

   public Observable<Activity> getOLiveActivity() {
       emitted = false;
       return Observable.interval(50, 50, TimeUnit.MILLISECONDS)
               .map(aLong -> {
                   if (liveActivityOrNull == null) return 0;
                   return liveActivityOrNull;
               })
               .takeWhile(candidate -> {
                   boolean continueEmitting = true;
                   if (emitted) continueEmitting = false;
                   if (candidate instanceof Activity) emitted = true;
                   return continueEmitting;
               })
               .filter(candidate -> candidate instanceof Activity)
               .map(activity -> (Activity) activity);
   }

}
