import com.google.gson.Gson
import gr.maax.gac.interfaces.config.ConfigLoader
import gr.maax.gac.interfaces.config.ConfigYaml
import gr.maax.gac.interfaces.database.DatabaseManager
import gr.maax.gac.interfaces.database.DatabaseManagerImpl
import gr.maax.gac.interfaces.gitlab.GitlabManager
import gr.maax.gac.interfaces.gitlab.GitlabManagerImpl
import gr.maax.gac.interfaces.projectjobsrepo.ProjectJobsRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.io.File

class MainTests: KoinComponent {

    @BeforeEach
    fun before() {
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
    }

    @AfterEach
    fun afterEach() {
        stopKoin()
    }

    @Test
    fun test() {
        val logFile = File("app.log")
        val gitlabManager: GitlabManager by inject()

        val projects = gitlabManager.getAllProjects()

        projects.forEach { project ->
            logFile.appendText("Start update for ${project.id}" + System.lineSeparator())
            val repo = ProjectJobsRepository(project.id)
            repo.updateDatabase()
            logFile.appendText("Finished update for ${project.id}" + System.lineSeparator())
        }
    }

    @Test
    fun projects() {
        val gitlabManager: GitlabManager by inject()
        val projects = gitlabManager.getAllProjects()

        println(projects.size)

    }

}
