package jp.manavista.developbase;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import jp.manavista.developbase.injector.component.ActivityComponent;
import jp.manavista.developbase.injector.component.FragmentComponent;
import jp.manavista.developbase.injector.module.AppModule;
import jp.manavista.developbase.injector.module.RepositoryModule;
import jp.manavista.developbase.injector.module.ServiceModule;

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
