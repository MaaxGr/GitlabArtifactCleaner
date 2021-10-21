package gr.maax.gac.interfaces.database.dao

import org.ktorm.database.Database
import org.ktorm.dsl.batchInsert
import org.ktorm.dsl.eq
import org.ktorm.dsl.insert
import org.ktorm.entity.Entity
import org.ktorm.entity.filter
import org.ktorm.entity.maxBy
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.date
import org.ktorm.schema.int
import org.ktorm.schema.varchar
import java.time.LocalDate

class JobCacheDao(private val database: Database) {

    fun getNewestJob(projectId: Int): Int? {
        return database.sequenceOf(DBTable).filter { it.projectId eq projectId } .maxBy { it.jobId }
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

    fun insert(
        projectId: Int,
        jobInt: Int,
        ref: String,
        artifactSize: Int,
        status: String,
        createdAt: LocalDate
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
        val createdAt = date("created_at").bindTo { it.createdAt }
    }

    interface DBEntity: Entity<DBEntity> {
        val id: Int
        val projectId: Int
        val jobId: Int
        val ref: String
        val artifactSize: Int
        val status: String
        val createdAt: LocalDate
    }

    data class GitlabJobCache(
        val id: Int,
        val projectId: Int,
        val jobId: Int,
        val ref: String,
        val artifactSize: Int,
        val status: String,
        val createdAt: LocalDate
    )


}