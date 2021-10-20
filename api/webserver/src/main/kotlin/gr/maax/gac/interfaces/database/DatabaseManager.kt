package gr.maax.gac.interfaces.database

import org.gitlab4j.api.models.Job

interface DatabaseManager {
    fun addJob(job: Job)
}