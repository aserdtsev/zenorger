package ru.serdtsev.zenorger.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.bind.annotation.RequestHeader
import ru.serdtsev.zenorger.ZenorgerApplication
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger.web.*
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.Collections.emptySet

@Configuration
@EnableSwagger2
class ApiDocConfig {

    private val securityContexts: List<SecurityContext>
        get() {
            val securityReference = SecurityReference.builder()
                    .reference(AUTH_NAME)
                    .scopes(arrayOf(AuthorizationScope("test", "")))
                    .build()
            return listOf(SecurityContext.builder()
                    .securityReferences(listOf(securityReference))
                    .forPaths(PathSelectors.ant("/v2/service/?realmCode?/**")).build())
        }

    @Bean
    fun productApi(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(listOf(BasicAuth(AUTH_NAME)))
                .securityContexts(securityContexts)
                .select()
                .apis(RequestHandlerSelectors.basePackage(ZenorgerApplication::class.java.getPackage().name))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .tags(
                        Tag(API_TAG, "Public API", 1),
                        Tag(TAG, "Private API", 2))
                .ignoredParameterTypes(RequestHeader::class.java)
                .apiInfo(apiInfo())
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
                "Zenorger",
                DESCRIPTION,
                javaClass.getPackage().implementationVersion, null, null, null, null,
                emptySet())
    }

    @Bean
    fun uiConfiguration(): UiConfiguration {
        return UiConfigurationBuilder.builder()
                .validatorUrl(null)
                .docExpansion(DocExpansion.LIST)
                .tagsSorter(TagsSorter.ALPHA)
                .operationsSorter(OperationsSorter.ALPHA)
                .defaultModelRendering(ModelRendering.MODEL)
                .build()
    }

    companion object {
        private const val DESCRIPTION = "Zenorger"
        private const val AUTH_NAME = "test-api"
        const val API_TAG = "public-api"
        const val TAG = "private-api"
    }
}