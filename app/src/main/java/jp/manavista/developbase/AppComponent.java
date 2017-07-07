package jp.manavista.developbase;

import javax.inject.Singleton;

import dagger.Component;
import jp.manavista.developbase.module.AppModule;
import jp.manavista.developbase.module.RepositoryModule;
import jp.manavista.developbase.module.ServiceModule;
import jp.manavista.developbase.view.activity.MainActivity;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
@Singleton
@Component(modules = {AppModule.class, ServiceModule.class, RepositoryModule.class})
public interface AppComponent {

    /*
     * TODO: inject argument
     * When the type of activity changes, the type of this inject depends on MainActivity.
     * How to create generic injected components.
     */
    void inject(MainActivity mainActivity);
}
