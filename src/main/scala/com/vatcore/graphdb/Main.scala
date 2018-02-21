package com.vatcore.graphdb

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.{CorsRegistry, InterceptorRegistry, WebMvcConfigurerAdapter}

// @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
// @Configuration 标注一个类为配置类(注解形式)
// @EnableAutoConfiguration 根据依赖自动配置
// @ComponentScan 自动扫描收集所有的Spring组件
@SpringBootApplication
class MySpringBootConfig() {

}

@Configuration
class MyWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {

  // 定制HTTP消息转换器
  override def configureMessageConverters(converters: java.util.List[HttpMessageConverter[_]]): Unit = {
    val mapper = new ObjectMapper with ScalaObjectMapper
    mapper.registerModule(DefaultScalaModule)
    converters.add(new MappingJackson2HttpMessageConverter(mapper))
    super.configureMessageConverters(converters)
  }

  // 拦截器
  override def addInterceptors(registry: InterceptorRegistry): Unit = {
    //    registry.addInterceptor(authWebRequestInterceptor)
  }

  override def addCorsMappings(registry: CorsRegistry): Unit = {
    registry.addMapping("/api/*").allowedOrigins("*")
  }

}

object Main extends App{
  SpringApplication.run(classOf[MySpringBootConfig])
}