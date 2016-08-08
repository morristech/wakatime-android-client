package com.wakatime.android.dashboard;

import com.wakatime.android.api.WakatimeClient;
import com.wakatime.android.dashboard.environment.DefaultEnvironmentPresenter;
import com.wakatime.android.dashboard.environment.EnvironmentPresenter;
import com.wakatime.android.dashboard.leaderboard.DefaultLeaderboardPresenter;
import com.wakatime.android.dashboard.leaderboard.LeaderboardPresenter;
import com.wakatime.android.dashboard.project.DefaultProjectPresenter;
import com.wakatime.android.dashboard.project.DefaultSingleProjectPresenter;
import com.wakatime.android.dashboard.project.ProjectPresenter;
import com.wakatime.android.dashboard.project.SingleProjectPresenter;
import com.wakatime.android.di.IOScheduler;
import com.wakatime.android.di.NetworkModule;
import com.wakatime.android.di.UIScheduler;
import com.wakatime.android.support.NetworkConnectionWatcher;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import rx.Scheduler;

/**
 * @author Joao Pedro Evangelista
 */
@Module(includes = NetworkModule.class)
public class DashboardModule {


    @Provides
    @Singleton
    EnvironmentPresenter programmingPresenter(Realm realm, WakatimeClient wakatimeClient,
                                              @IOScheduler Scheduler ioScheduler,
                                              @UIScheduler Scheduler uiScheduler,
                                              NetworkConnectionWatcher watcher) {
        return new DefaultEnvironmentPresenter(realm, wakatimeClient, ioScheduler, uiScheduler, watcher);
    }

    @Provides
    @Singleton
    ProjectPresenter projectPresenter(Realm realm, WakatimeClient wakatimeClient,
                                      @IOScheduler Scheduler ioScheduler,
                                      @UIScheduler Scheduler uiScheduler,
                                      NetworkConnectionWatcher watcher) {
        return new DefaultProjectPresenter(realm, wakatimeClient, ioScheduler, uiScheduler, watcher);
    }

    @Provides
    @Singleton
    LeaderboardPresenter leaderboardPresenter(Realm realm, WakatimeClient wakatimeClient,
                                              @IOScheduler Scheduler ioScheduler,
                                              @UIScheduler Scheduler uiScheduler,
                                              NetworkConnectionWatcher watcher) {
        return new DefaultLeaderboardPresenter(realm, wakatimeClient, ioScheduler, uiScheduler, watcher);
    }

    @Provides
    @Singleton
    SingleProjectPresenter singleProjectPresenter(Realm realm, WakatimeClient wakatimeClient,
                                                  @IOScheduler Scheduler ioScheduler,
                                                  @UIScheduler Scheduler uiScheduler,
                                                  NetworkConnectionWatcher watcher) {
        return new DefaultSingleProjectPresenter(realm, wakatimeClient, ioScheduler, uiScheduler, watcher);
    }
}
