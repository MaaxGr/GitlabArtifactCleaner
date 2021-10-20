package gr.maax.gac

import gr.maax.gac.interfaces.webserver.Webserver
import gr.maax.gac.interfaces.config.ConfigLoader
import gr.maax.gac.interfaces.config.ConfigYaml
import gr.maax.gac.interfaces.database.DatabaseManager
import gr.maax.gac.interfaces.database.DatabaseManagerImpl
import gr.maax.gac.interfaces.gitlab.GitlabManager
import gr.maax.gac.interfaces.gitlab.GitlabManagerImpl
import org.koin.core.context.startKoin
import org.koin.dsl.module


fun main() {

    startKoin {
        val mainModule = module {
            single { ConfigLoader().loadConfig() }
            single { get<ConfigYaml>().database }
            single { get<ConfigYaml>().gitlab }
            single { get<ConfigYaml>().webserver }
            single<GitlabManager> { GitlabManagerImpl() }
            single<DatabaseManager> { DatabaseManagerImpl() }
        }
        modules(mainModule)
    }


    Webserver()

}