package com.km.config

import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class GlobalWebConfiguration :WebMvcConfigurer{

    override fun addCorsMappings(registry: CorsRegistry) {

        //允许所有请求跨域
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedHeaders("*")
            .exposedHeaders("*")
            .allowCredentials(true)
            .allowedOrigins("http://localhost:8081/")
            .allowedMethods(*RequestMethod.values().run { Array(size){ get(it).name } })
            .maxAge(3600)
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {

    }

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(String::class.java,kotlinx.datetime.LocalDate::class.java){
            kotlinx.datetime.LocalDate.parse(it)
        }
    }

//    override fun addInterceptors(registry: InterceptorRegistry) {
//        registry.addInterceptor(MyInterceptor())
//    }


}