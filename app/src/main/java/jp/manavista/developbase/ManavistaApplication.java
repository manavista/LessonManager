package jp.manavista.developbase;

import android.app.Application;

import jp.manavista.developbase.injector.DependencyInjector;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public class ManavistaApplication extends Application {

//    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

//        appComponent = DaggerAppComponent.builder()
//                .appModule(new AppModule(this))
//                .build();

        DependencyInjector.init(this);
        DependencyInjector.appComponent().inject(this);

    }

//    public AppComponent getAppComponent() {
//        return appComponent;
//    }
}
