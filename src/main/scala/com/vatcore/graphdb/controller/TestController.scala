package com.vatcore.graphdb.controller

import java.io.{File, FileOutputStream, ObjectInputStream}
import java.lang.reflect.Constructor
import java.util.concurrent.{AbstractExecutorService, CompletionService, ScheduledFuture}
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest

import com.vatcore.graphdb.component.{Database, Document, Node}
import com.vatcore.graphdb.json.Response
import org.springframework.web.bind.annotation.{PostMapping, RequestBody, RequestMapping, RestController}

import scala.util.Random

@RestController
@RequestMapping(Array("/api/test"))
class TestController {

  @PostMapping(Array("/initType"))
  def initType(@RequestBody anyRef: AnyRef): Unit = {
    println(anyRef)
  }

  @PostMapping(Array("/initTypeA"))
//  def initTypeA(@RequestBody anyRef: AnyRef): Unit = {
  def initTypeA(request: HttpServletRequest): Unit = {
//    println(anyRef)
//    println(anyRef.getClass.toString)

    val servletInputStream = request.getInputStream

    val fileOutputStream = new FileOutputStream(new File("D:\\xxaa.ser"))

//    fileOutputStream.write(servletInputStream.readAllBytes())
//    fileOutputStream.flush()
//    fileOutputStream.close()
//
//    System.out.println(servletInputStream.isReady)
//    System.out.println(servletInputStream.isFinished)

    val objectInputStream = new ObjectInputStream(servletInputStream)
//    val anyRef = objectInputStream.readObject().asInstanceOf[Node]

//    println(anyRef.getClass.toString)
//    println(anyRef)



  }



}
