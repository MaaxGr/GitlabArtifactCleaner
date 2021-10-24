package gr.maax.gac.interfaces.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
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

    private val dataSource = HikariDataSource(HikariConfig().apply {
        jdbcUrl = "jdbc:mysql://${databaseConfig.hostname}:${databaseConfig.port}/${databaseConfig.database}"
        username = databaseConfig.username
        password = databaseConfig.password
        addDataSourceProperty( "cachePrepStmts" , "true" );
        addDataSourceProperty( "prepStmtCacheSize" , "250" );
        addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
    })

    private val database: Database = Database.connect(dataSource)

    private val jobCacheDao = JobCacheDao(database)

    override fun getAllSuccessJobsWithArtifacts(projectId: Int): List<JobCacheDao.DBEntity> {
        return jobCacheDao.getAllSuccessJobsWithArtifacts(projectId)
    }

    override fun getNewestJob(projectId: Int): Int? {
        return jobCacheDao.getNewestJob(projectId)
    }

    override fun addJob(projectId: Int, job: Job) {
        // filter logs and convert bytes to kb
        val realArtifactSizeKB = job.artifacts.filter { it.fileType != Artifact.FileType.TRACE }
            .sumOf { (it.size ?: 0) / 1000 }

        jobCacheDao.insert(
            projectId = projectId,
            jobInt = job.id,
            ref = job.ref,
            artifactSize = realArtifactSizeKB,
            status = job.status.name,
            createdAt = Timestamp(job.createdAt.time).toLocalDateTime()
        )

    }

    override fun addJobs(projectId: Int, jobs: List<Job>) {
        jobCacheDao.insertMultiple(
            jobs.map { job ->
                val realArtifactSizeKB = job.artifacts.filter { it.fileType != Artifact.FileType.TRACE }
                    .sumOf { (it.size ?: 0) / 1000 }

                JobCacheDao.GitlabJobCache(
                    projectId = projectId,
                    jobId = job.id,
                    ref = job.ref,
                    artifactSize = realArtifactSizeKB,
                    status = job.status.name,
                    createdAt = Timestamp(job.createdAt.time).toLocalDateTime(),
                    id = -1
                )
            }
        )
    }

    override fun updateArtifactSize(projectId: Int, jobId: Int, newSize: Int) {
        jobCacheDao.updateArtifactSize(projectId, jobId, newSize)
    }



}