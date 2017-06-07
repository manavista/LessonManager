package jp.manavista.developbase;

import android.app.Application;

import jp.manavista.developbase.module.RepositoryModule;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public class ThisApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))

                /*
                 * It is not necessary to describe
                 * when Module is a constructor without arguments.
                 */
//                .repositoryModule(new RepositoryModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
