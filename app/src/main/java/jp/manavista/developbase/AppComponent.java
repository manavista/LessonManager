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
    void inject(MainActivity mainActivity);
}
