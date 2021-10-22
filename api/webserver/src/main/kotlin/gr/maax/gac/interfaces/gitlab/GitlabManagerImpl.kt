package gr.maax.gac.interfaces.gitlab

import gr.maax.gac.interfaces.config.ConfigGitlab
import org.gitlab4j.api.GitLabApi
import org.gitlab4j.api.models.AccessLevel
import org.gitlab4j.api.models.Job
import org.gitlab4j.api.models.Project
import org.gitlab4j.api.models.ProjectFilter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GitlabManagerImpl: GitlabManager, KoinComponent {

    private val configGitlab: ConfigGitlab by inject()

    private val api = GitLabApi(configGitlab.hostUrl, configGitlab.token)

    init {

    }

    override fun getJobs(projectId: Int, page: Int, perPage: Int): List<Job> {
        return api.jobApi.getJobs(projectId, page, perPage).toList()
    }

    override fun getJob(projectId: Int, jobId: Int): Job? {
        return api.jobApi.getJob(projectId, jobId)
    }

    override fun getAllProjects(): List<Project> {
        val projects = mutableListOf<Project>()
        var page = 1

        while (true) {

            val filter = ProjectFilter()
            filter.withMembership(true)
            filter.withMinAccessLevel(AccessLevel.MAINTAINER)
            filter.withArchived(false)

            val pageProjects = api.projectApi.getProjects(filter, page, 100)

            if (pageProjects.isEmpty()) {
                break
            }

            projects.addAll(pageProjects)
            page++;
        }

        return projects.filter { it.jobsEnabled }
    }

    override fun deleteArtifacts(projectId: Int, jobId: Int) {
        api.jobApi.deleteArtifacts(projectId, jobId)
    }




}