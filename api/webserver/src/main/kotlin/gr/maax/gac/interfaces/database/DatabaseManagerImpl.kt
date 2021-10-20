package gr.maax.gac.interfaces.database

import gr.maax.gac.interfaces.config.ConfigDatabaseMySQL
import org.gitlab4j.api.models.Job
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DatabaseManagerImpl : DatabaseManager, KoinComponent {

    private val databaseConfig: ConfigDatabaseMySQL by inject()

    init {

    }

    override fun addJob(job: Job) {

    }



}