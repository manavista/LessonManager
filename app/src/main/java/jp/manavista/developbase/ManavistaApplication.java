package jp.manavista.developbase;

import android.app.Application;

import jp.manavista.developbase.injector.DependencyInjector;

/**
 *
 * Manavista Application
 *
 * <p>
 * Overview:<br>
 * </p>
 */
public class ManavistaApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DependencyInjector.init(this);
        DependencyInjector.appComponent().inject(this);
    }
}
