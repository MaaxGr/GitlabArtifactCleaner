package gr.maax.gac.interfaces.database

import gr.maax.gac.interfaces.database.dao.JobCacheDao
import org.gitlab4j.api.models.Job

interface DatabaseManager {
    fun addJob(projectId: Int, job: Job)
    fun getNewestJob(projectId: Int): Int?
    fun addJobs(projectId: Int, jobs: List<Job>)
    fun getAllSuccessJobsWithArtifacts(projectId: Int): List<JobCacheDao.DBEntity>
    fun updateArtifactSize(projectId: Int, jobId: Int, newSize: Int)
}