package gr.maax.gac.interfaces.projectjobsrepo

import gr.maax.gac.interfaces.database.DatabaseManager
import gr.maax.gac.interfaces.gitlab.GitlabManager
import org.gitlab4j.api.models.Artifact
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProjectCleaner(private val projectId: Int): KoinComponent {

    private val gitlabManager: GitlabManager by inject()
    private val databaseManager: DatabaseManager by inject()

    fun clean(result: ProjectAnalyzer.AnalyzeResult) {
        val jobsToDelete = result.trashRefInfo.map { it.jobIdsToDelete }.flatten()

        jobsToDelete.forEach { jobId ->
            gitlabManager.deleteArtifacts(projectId, jobId)

            val job = gitlabManager.getJob(projectId, jobId)
                ?: throw IllegalStateException("Job-ID $jobId not found?")

            val realArtifactSizeMB = job.artifacts.filter { it.fileType != Artifact.FileType.TRACE }
                .sumOf { (it.size ?: 0) / 1000 / 1000 }

            databaseManager.updateArtifactSize(projectId, jobId, realArtifactSizeMB)
        }
    }

}