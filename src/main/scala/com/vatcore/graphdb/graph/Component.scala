package com.vatcore.graphdb.graph

object Component {}

class Node[T](
  val id: Long,
  val data: T
)

class Edge[T](
  val id: Long,
  val data: T
)