package gr.maax.gac.interfaces.gitlab

import org.gitlab4j.api.models.Job
import org.gitlab4j.api.models.Project

interface GitlabManager {
    fun getJobs(projectId: Int, page: Int, perPage: Int = 100): List<Job>
    fun getAllProjects(): MutableList<Project>
    fun deleteArtifacts(projectId: Int, jobId: Int)
    fun getJob(projectId: Int, jobId: Int): Job?
}