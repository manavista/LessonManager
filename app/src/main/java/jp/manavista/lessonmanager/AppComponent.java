package jp.manavista.lessonmanager;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import jp.manavista.lessonmanager.injector.component.ActivityComponent;
import jp.manavista.lessonmanager.injector.component.FragmentComponent;
import jp.manavista.lessonmanager.injector.module.AppModule;
import jp.manavista.lessonmanager.injector.module.RepositoryModule;
import jp.manavista.lessonmanager.injector.module.ServiceModule;

/**
 *
 * Application Component
 *
 * <p>
 * Overview:<br>
 *
 * </p>
 */
@Singleton
@Component(modules = {AppModule.class, ServiceModule.class, RepositoryModule.class})
public interface AppComponent extends ActivityComponent, FragmentComponent {
    void inject(Application application);
}
