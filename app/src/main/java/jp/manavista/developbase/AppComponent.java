package jp.manavista.developbase;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import jp.manavista.developbase.component.ActivityComponent;
import jp.manavista.developbase.component.FragmentComponent;
import jp.manavista.developbase.module.AppModule;
import jp.manavista.developbase.module.RepositoryModule;
import jp.manavista.developbase.module.ServiceModule;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
@Singleton
@Component(modules = {AppModule.class, ServiceModule.class, RepositoryModule.class})
public interface AppComponent extends ActivityComponent, FragmentComponent {
    void inject(Application application);
}
