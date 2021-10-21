package gr.maax.gac.interfaces.projectjobsrepo

import gr.maax.gac.interfaces.database.DatabaseManager
import gr.maax.gac.interfaces.database.dao.JobCacheDao
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Timestamp
import java.util.*

class ProjectAnalyzer(private val projectId: Int): KoinComponent {

    private val databaseManager: DatabaseManager by inject()


    fun analyze(): AnalyzeResult {
        val jobs = databaseManager.getAllSuccessJobsWithArtifacts(projectId)

        val totalJobCount = jobs.size
        val totalArtifactSize = jobs.sumOf { it.artifactSize }

        val jobsOlder15Days = jobs.filter {
            val startDayMillis = Timestamp.valueOf(it.createdAt).time
            startDayMillis < System.currentTimeMillis() - (1000L * 60 * 60 * 24 * 15)
        }
        val artifactsOlder15Days = jobsOlder15Days.sumOf { it.artifactSize }
        val refGroups = jobsOlder15Days.groupBy { it.ref }

        val trashRefInfo = refGroups.map { (ref, jobs) -> analyzeGroup(ref, jobs) }

        val jobsToDelete = trashRefInfo.map { it.jobIdsToDelete }.flatten().size
        val artifactSizeToDelete = trashRefInfo.sumOf { it.artifactSizeToDelete }

        val submitToken = UUID.randomUUID()

        return AnalyzeResult(
            totalSuccessJobsWithArtifact = totalJobCount,
            totalArtifactSize = totalArtifactSize,
            artifactJobsOlder15Days = jobsOlder15Days.size,
            artifactSizeOlder15Days = artifactsOlder15Days,
            trashRefInfo = trashRefInfo,
            jobsToDelete = jobsToDelete,
            artifactSizeToDelete = artifactSizeToDelete,
            submitDeleteToken = submitToken
        )
    }

    private fun analyzeGroup(ref: String, jobs: List<JobCacheDao.DBEntity>): AnalyzeResultRef {
        val sortedJobs = jobs.sortedByDescending { it.jobId }

        val retainedJob = sortedJobs.first()
        val trashJobs = sortedJobs.drop(1)

        return AnalyzeResultRef(
            ref = ref,
            jobIdToKeep = retainedJob.jobId,
            jobIdsToDelete = trashJobs.map { it.jobId },
            artifactSizeToDelete = trashJobs.sumOf { it.artifactSize }
        )
    }

    data class AnalyzeResult(
        val totalSuccessJobsWithArtifact: Int,
        val totalArtifactSize: Int,
        val artifactJobsOlder15Days: Int,
        val artifactSizeOlder15Days: Int,
        val trashRefInfo: List<AnalyzeResultRef>,
        val jobsToDelete: Int,
        val artifactSizeToDelete: Int,
        val submitDeleteToken: UUID
    )

    data class AnalyzeResultRef(
        val ref: String,
        val jobIdToKeep: Int,
        val jobIdsToDelete: List<Int>,
        val artifactSizeToDelete: Int
    )

}