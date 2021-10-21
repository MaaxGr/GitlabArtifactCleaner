package gr.maax.gac.interfaces.projectjobsrepo

import gr.maax.gac.interfaces.database.DatabaseManager
import gr.maax.gac.interfaces.database.dao.JobCacheDao
import gr.maax.gac.interfaces.gitlab.GitlabManager
import org.gitlab4j.api.models.Job
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Timestamp
import java.util.*

class ProjectJobsRepository(private val projectId: Int): KoinComponent {


    private val databaseManager: DatabaseManager by inject()
    private val gitlabManager: GitlabManager by inject()

    fun updateDatabase() {
        val newestJobIdInDB = databaseManager.getNewestJob(projectId) ?: 0
        var page = 1

        val jobsToInsert = mutableListOf<Job>()

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
                jobsToInsert.add(job)
            }

            if (jobsToInsert.size > 0) {
                databaseManager.addJobs(projectId, jobsToInsert)
                jobsToInsert.clear()
            }

            page++
        }

        if (jobsToInsert.size > 0) {
            databaseManager.addJobs(projectId, jobsToInsert)
            jobsToInsert.clear()
        }

    }




}