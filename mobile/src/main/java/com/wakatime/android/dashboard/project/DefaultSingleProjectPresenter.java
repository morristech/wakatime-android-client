package com.wakatime.android.dashboard.project;

import com.wakatime.android.api.WakatimeClient;
import com.wakatime.android.dashboard.model.Wrapper;
import com.wakatime.android.support.NetworkConnectionWatcher;
import com.wakatime.android.support.net.HeaderFormatter;

import io.realm.Realm;
import io.realm.internal.IOException;
import rx.Scheduler;
import rx.Subscription;
import timber.log.Timber;

/**
 * @author Joao Pedro Evangelista
 */
public class DefaultSingleProjectPresenter implements SingleProjectPresenter {

    private final Realm realm;

    private final WakatimeClient wakatimeClient;

    private final Scheduler ioScheduler;

    private final Scheduler uiScheduler;

    private final NetworkConnectionWatcher watcher;

    private SingleProjectViewModel viewModel;

    private Subscription sub;

    public DefaultSingleProjectPresenter(Realm realm, WakatimeClient wakatimeClient, Scheduler ioScheduler,
                                         Scheduler uiScheduler, NetworkConnectionWatcher watcher) {
        this.realm = realm;
        this.wakatimeClient = wakatimeClient;
        this.ioScheduler = ioScheduler;
        this.uiScheduler = uiScheduler;
        this.watcher = watcher;
    }

    @Override
    public void bind(SingleProjectViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void unbind() {
        this.viewModel = null;
    }

    @Override
    public void onInit(String project) {
        if (watcher.isNetworkAvailable()) {
            this.sub = this.wakatimeClient.fetchProjectLastSevenDays(HeaderFormatter.get(realm), project)
                    .subscribeOn(ioScheduler)
                    .observeOn(uiScheduler)
                    .doOnTerminate(() -> viewModel.hideLoader())
                    .map(Wrapper::getData)
                    .doOnError(viewModel::notifyError)
                    .subscribe(stats -> {
                        viewModel.setData(stats);
                        viewModel.setRotationCache(stats);
                    }, error -> {
                        Timber.e("Error while fetching stats for project %s", error, project);
                        viewModel.notifyError(error);
                    });
        } else {
            viewModel.notifyError(new IOException("No connection found"));
        }
    }

    @Override
    public void onFinish() {
        if (sub != null && !sub.isUnsubscribed()) {
            sub.unsubscribe();
        }
    }
}
