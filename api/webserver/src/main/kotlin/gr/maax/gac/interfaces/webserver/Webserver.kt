package gr.maax.gac.interfaces.webserver

import gr.maax.gac.interfaces.config.ConfigYaml
import gr.maax.gac.interfaces.webserver.endpoints.v1.PostAnalyzeProject
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.slf4j.event.Level

class Webserver: KoinComponent {

    private val config: ConfigYaml by inject()

    init {

        println("Config: $config")

        embeddedServer(Netty, port = 8080) {
            install(CallLogging) {
                level = Level.INFO
            }
            install(ContentNegotiation) {
                gson {  }
            }

            routing {
                get("/") {
                    call.respond("Hello World")
                }

                post("/v1/analyzeproject") {
                    PostAnalyzeProject().run(call)
                }


            }


        }.start(true)


    }

}