/*
 * Copyright (c) 2017 manavista. All rights reserved.
 */

package jp.manavista.lessonmanager.injector;

import jp.manavista.lessonmanager.AppComponent;
import jp.manavista.lessonmanager.DaggerAppComponent;
import jp.manavista.lessonmanager.ManavistaApplication;
import jp.manavista.lessonmanager.injector.module.AppModule;

/**
 *
 * DependencyInjector
 *
 * <p>
 * Overview:<br>
 *
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
