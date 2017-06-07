package jp.manavista.developbase;

import javax.inject.Singleton;

import dagger.Component;
import jp.manavista.developbase.module.RepositoryModule;

/**
 * <p>
 * Overview:<br>
 * </p>
 */
@Singleton
@Component(modules = {AppModule.class, RepositoryModule.class})
public interface AppComponent {


    /*
     * TODO: inject argument
     * When the type of activity changes, the type of this inject depends on MainActivity.
     * How to create generic injected components.
     */
    void inject(MainActivity mainActivity);
}
