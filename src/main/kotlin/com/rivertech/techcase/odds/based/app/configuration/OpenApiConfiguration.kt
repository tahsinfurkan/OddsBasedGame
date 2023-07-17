package com.rivertech.techcase.odds.based.app.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@OpenAPIDefinition(info = Info(title = "Tahsin Furkan Keles Technical Case", version = "v1"))
class OpenApiConfiguration {

    @Value("\${openapi.server.url}")
    private val serverUrl: String? = null

    @Value("\${openapi.server.title}")
    private val title: String? = null

    @Value("\${openapi.server.version}")
    private val version: String? = null

    @Bean
    fun openApi(): OpenAPI? {
        return OpenAPI()
                .addServersItem(Server().url(serverUrl))
                .info(io.swagger.v3.oas.models.info.Info()
                        .title(title)
                        .version(version)
                        .license(License()))
    }
}