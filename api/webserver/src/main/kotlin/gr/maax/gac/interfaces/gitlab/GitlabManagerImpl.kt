package gr.maax.gac.interfaces.gitlab

import gr.maax.gac.interfaces.config.ConfigGitlab
import org.gitlab4j.api.GitLabApi
import org.gitlab4j.api.models.Job
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



}