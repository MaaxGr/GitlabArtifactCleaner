package gr.maax.gac.interfaces.config

import kotlinx.serialization.Serializable


@Serializable
data class ConfigYaml(
    val webserver: ConfigWebserver,
    val database: ConfigDatabaseMySQL,
    val gitlab: ConfigGitlab
)

@Serializable
data class ConfigGitlab(
    val hostUrl: String,
    val token: String
)

@Serializable
data class ConfigDatabaseMySQL(
    val hostname: String,
    val port: Int,
    val username: String,
    val password: String,
    val database: String
)

@Serializable
data class ConfigWebserver(
    val port: Int
)