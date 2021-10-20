package gr.maax.gac.interfaces.gitlab

import org.gitlab4j.api.models.Job

interface GitlabManager {
    fun getJobs(projectId: Int, page: Int, perPage: Int = 100): List<Job>
}