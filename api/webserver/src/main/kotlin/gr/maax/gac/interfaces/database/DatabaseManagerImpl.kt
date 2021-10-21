package gr.maax.gac.interfaces.database

import gr.maax.gac.interfaces.config.ConfigDatabaseMySQL
import gr.maax.gac.interfaces.database.dao.JobCacheDao
import org.gitlab4j.api.models.Artifact
import org.gitlab4j.api.models.Job
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.ktorm.database.Database
import java.sql.Timestamp

class DatabaseManagerImpl : DatabaseManager, KoinComponent {

    private val databaseConfig: ConfigDatabaseMySQL by inject()

    private val database: Database = Database.connect(
        url = "jdbc:mysql://${databaseConfig.hostname}:${databaseConfig.port}/${databaseConfig.database}",
        user = databaseConfig.username,
        password = databaseConfig.password
    )

    private val jobCacheDao = JobCacheDao(database)

    override fun getNewestJob(projectId: Int): Int? {
        return jobCacheDao.getNewestJob(projectId)
    }

    override fun addJob(projectId: Int, job: Job) {
        // filter logs and convert bytes to mb
        val realArtifactSizeMB = job.artifacts.filter { it.fileType != Artifact.FileType.TRACE }
            .sumOf { (it.size ?: 0) / 1000 / 1000 }

        jobCacheDao.insert(
            projectId = projectId,
            jobInt = job.id,
            ref = job.ref,
            artifactSize = realArtifactSizeMB,
            status = job.status.name,
            createdAt = Timestamp(job.createdAt.time).toLocalDateTime().toLocalDate()
        )

    }

    override fun addJobs(projectId: Int, jobs: List<Job>) {
        jobCacheDao.insertMultiple(
            jobs.map { job ->
                val realArtifactSizeMB = job.artifacts.filter { it.fileType != Artifact.FileType.TRACE }
                    .sumOf { (it.size ?: 0) / 1000 / 1000 }

                JobCacheDao.GitlabJobCache(
                    projectId = projectId,
                    jobId = job.id,
                    ref = job.ref,
                    artifactSize = realArtifactSizeMB,
                    status = job.status.name,
                    createdAt = Timestamp(job.createdAt.time).toLocalDateTime().toLocalDate(),
                    id = -1
                )
            }
        )

    }



}