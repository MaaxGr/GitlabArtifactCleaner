package gr.maax.gac.interfaces.config

import com.charleskorn.kaml.PolymorphismStyle
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.decodeFromString
import java.io.File

class ConfigLoader {

    companion object {
        const val CONFIG_FILE_NAME = "config.yaml"
    }

    fun loadConfig(): ConfigYaml {
        val configFile = loadConfigFileFromResources()
        val configFileContent = configFile.readText()
        val yamlParser = createYamlParser()

        return yamlParser.decodeFromString(configFileContent)
    }

    private fun loadConfigFileFromResources(): File {
        return File(CONFIG_FILE_NAME)
    }

    private fun createYamlParser(): Yaml {
        val yamlConfig = Yaml.default.configuration.copy(
            polymorphismStyle = PolymorphismStyle.Property,
            polymorphismPropertyName = "type",
        )
        return Yaml(Yaml.default.serializersModule, yamlConfig)
    }


}