package gr.maax.gac.interfaces.database

import org.gitlab4j.api.models.Job

interface DatabaseManager {
    fun addJob(projectId: Int, job: Job)
    fun getNewestJob(projectId: Int): Int?
    fun addJobs(projectId: Int, jobs: List<Job>)
}