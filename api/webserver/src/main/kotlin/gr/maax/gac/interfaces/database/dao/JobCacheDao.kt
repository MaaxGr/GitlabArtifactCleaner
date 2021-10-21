package gr.maax.gac.interfaces.database.dao

import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.entity.*
import org.ktorm.schema.*
import java.time.LocalDate
import java.time.LocalDateTime

class JobCacheDao(private val database: Database) {

    fun getNewestJob(projectId: Int): Int? {
        return database.sequenceOf(DBTable).filter { it.projectId eq projectId } .maxBy { it.jobId }
    }

    fun getAllSuccessJobsWithArtifacts(projectId: Int): List<DBEntity> {
        return database.sequenceOf(DBTable).filter {
            (it.projectId eq projectId) and
                    (it.status eq "SUCCESS") and
                    (it.artifactSize greater  0)
        }.toList()
    }

    fun insertMultiple(jobs: List<GitlabJobCache>) {
        database.batchInsert(DBTable) {
            for (job in jobs) {
                item {
                    set(it.projectId, job.projectId)
                    set(it.jobId, job.jobId)
                    set(it.ref, job.ref)
                    set(it.artifactSize, job.artifactSize)
                    set(it.status, job.status)
                    set(it.createdAt, job.createdAt)
                }
            }
        }

    }

    fun updateArtifactSize(projectId: Int, jobId: Int, artifactSize: Int) {
        database.update(DBTable) {
            set(it.artifactSize, artifactSize)
            where {
                (it.projectId eq projectId) and (it.jobId eq jobId)
            }
        }
    }

    fun insert(
        projectId: Int,
        jobInt: Int,
        ref: String,
        artifactSize: Int,
        status: String,
        createdAt: LocalDateTime
    ) {
        database.insert(DBTable) {
            set(it.projectId, projectId)
            set(it.jobId, jobInt)
            set(it.ref, ref)
            set(it.artifactSize, artifactSize)
            set(it.status, status)
            set(it.createdAt, createdAt)
        }
    }


    object DBTable: Table<DBEntity>("gitlab_job_cache") {
        val id = int("id").primaryKey().bindTo { it.id }
        val projectId = int("project_id").bindTo { it.projectId }
        val jobId = int("job_id").bindTo { it.jobId }
        val ref = varchar("ref").bindTo { it.ref }
        val artifactSize = int("artifact_size").bindTo { it.artifactSize }
        val status = varchar("status").bindTo { it.status }
        val createdAt = datetime("created_at").bindTo { it.createdAt }
    }

    interface DBEntity: Entity<DBEntity> {
        val id: Int
        val projectId: Int
        val jobId: Int
        val ref: String
        val artifactSize: Int
        val status: String
        val createdAt: LocalDateTime
    }

    data class GitlabJobCache(
        val id: Int,
        val projectId: Int,
        val jobId: Int,
        val ref: String,
        val artifactSize: Int,
        val status: String,
        val createdAt: LocalDateTime
    )


}