package gr.maax.gac.interfaces.webserver.endpoints.v1


import gr.maax.gac.interfaces.projectjobsrepo.ProjectJobsRepository
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import org.koin.core.component.KoinComponent

class PostAnalyzeProject: KoinComponent {

    suspend fun run(call: ApplicationCall) {
        val request = call.receive<Request>()

        val result = ProjectJobsRepository(request.projectId).run {
            //updateDatabase()
            analyze()
        }

        call.respond(result)
    }

    data class Request(
        val projectId: Int
    )

}