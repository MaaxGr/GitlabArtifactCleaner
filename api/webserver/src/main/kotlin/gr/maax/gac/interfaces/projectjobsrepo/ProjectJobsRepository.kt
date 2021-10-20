package gr.maax.gac.interfaces.projectjobsrepo

import gr.maax.gac.interfaces.database.DatabaseManager
import gr.maax.gac.interfaces.gitlab.GitlabManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProjectJobsRepository(private val projectId: Int): KoinComponent {

    private val databaseManager: DatabaseManager by inject()
    private val gitlabManager: GitlabManager by inject()

    fun updateDatabase() {
        val newestJobIdInDB = 0
        var page = 1

        jobLoop@ while (true) {
            val jobs = gitlabManager.getJobs(projectId, page)

            // return if no jobs available at this page
            if (jobs.isEmpty()) {
                break@jobLoop
            }

            // go through job
            for (job in jobs) {

                // break if job id is already in database
                if (job.id <= newestJobIdInDB) {
                    break@jobLoop
                }

                // add job to database
                databaseManager.addJob(job)
            }

            page++
        }
    }

    fun analyze(): AnalyzeResult {

        return AnalyzeResult(
            trashJobsTotal = 1000,
            trashArtifactSizeMB = 10,
            trashRefInfo = listOf()
        )

    }

    data class AnalyzeResult(
        val trashJobsTotal: Int,
        val trashArtifactSizeMB: Int,
        val trashRefInfo: List<AnalyzeResultRef>
    )

    data class AnalyzeResultRef(
        val ref: String,
        val jobIdsToKeep: List<Int>,
        val jobIdsToDelete: List<Int>
    )

}