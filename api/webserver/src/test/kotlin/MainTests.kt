import gr.maax.gac.interfaces.config.ConfigLoader
import gr.maax.gac.interfaces.config.ConfigYaml
import gr.maax.gac.interfaces.database.DatabaseManager
import gr.maax.gac.interfaces.database.DatabaseManagerImpl
import gr.maax.gac.interfaces.gitlab.GitlabManager
import gr.maax.gac.interfaces.gitlab.GitlabManagerImpl
import gr.maax.gac.interfaces.projectjobsrepo.ProjectJobsRepository
import org.junit.jupiter.api.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.io.File

class MainTests: KoinComponent {

    @Test
    fun test() {

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


        val gitlabManager: GitlabManager by inject()

        val jobs = gitlabManager.getJobs(1, 1, 10)
        println(jobs.map { it.id })

        val repo = ProjectJobsRepository(1)
        repo.updateDatabase()

    }

}
