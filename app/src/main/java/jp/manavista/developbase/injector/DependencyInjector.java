package jp.manavista.developbase.injector;

import jp.manavista.developbase.AppComponent;
import jp.manavista.developbase.DaggerAppComponent;
import jp.manavista.developbase.ManavistaApplication;
import jp.manavista.developbase.module.AppModule;

/**
 * <p>
 * Overview:<br>
 * </p>
 */

public class DependencyInjector {

    private static AppComponent appComponent;

    /** constructor */
    private DependencyInjector() {
        /* no description */
    }

    public static void init(ManavistaApplication application) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build();
    }

    public static AppComponent appComponent() {
        return appComponent;
    }
}
