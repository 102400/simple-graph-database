package com.vatcore.graphdb.component

import java.util.concurrent.atomic.AtomicLong

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

import scala.collection.mutable

object Database {
  val lastDatabaseId: AtomicLong = new AtomicLong(1)
  val databaseNameMap: mutable.Map[String, Database] = mutable.HashMap()

  def findByDateName(databaseName: String): Option[Database] = {
    databaseNameMap.get(databaseName)
  }

  // 仅测试
  val databaseNameOne = "test-one"
  Database.databaseNameMap.put(databaseNameOne, new Database(
    id = Database.lastDatabaseId.getAndIncrement(),
    name = databaseNameOne
  ))

}

class Database(
  val id: Long,
  val name: String,

//  val nodeNameSet: mutable.Set[String] = mutable.Set(),
  val lastNodeId: AtomicLong = new AtomicLong(1),
  val nodeIdTree: mutable.TreeMap[Long, Node] = mutable.TreeMap(),

//  val relationshipNameSet: mutable.Set[String] = mutable.Set(),
  val lastRelationshipId: AtomicLong = new AtomicLong(1),
  val relationshipIdTree: mutable.TreeMap[Long, Relationship] = mutable.TreeMap(),

//  val documentNameSet: mutable.Set[String] = mutable.Set(),  // 类名 + 所有(字段名 + 类型) 拼接算出的hashCode
  val lastDocumentId: AtomicLong = new AtomicLong(1),
  val documentIdTree: mutable.TreeMap[Long, Document] = mutable.TreeMap()
)

class Node(
  @JsonDeserialize(contentAs = classOf[java.lang.Long])
  var id: Option[Long],
//  val name: String,
  val fromList: mutable.Buffer[Relationship] = mutable.ListBuffer(),
  val toList: mutable.Buffer[Relationship] = mutable.ListBuffer(),
  val document: Document
)

class Relationship(
  @JsonDeserialize(contentAs = classOf[java.lang.Long])
  var id: Option[Long],
//  val name: String,
  val from: Node,
  val to: Node,
  val document: Document
)

class Document(
  @JsonDeserialize(contentAs = classOf[java.lang.Long])
  var id: Option[Long],
  var typeName: String,
  var data: Map[String, AnyRef]  // 可能为 boolean array int
)

object Node {

//  private def findNode(database: Database, node: Node): Option[Node] = {
//    node.id match {
//      case Some(id) =>
//        database.nodeIdTree.get (id)
//      case None => None
//    }
//  }

  def create(database: Database, node: Node): Option[Node] = {
    node.document.id match {
      case Some(id) =>
        Document.findById(database, id) match {
          case Some(doc) =>
            val id = database.lastNodeId.getAndIncrement()
            val n = new Node(
              id = Some(id),
              document = doc
            )
            database.nodeIdTree.put(id, n)
            Some(n)
          case None => None
        }
      case None =>
        val id = database.lastNodeId.getAndIncrement()
        val n = new Node(
          id = Some(id),
          document = Document.create(database, node.document)
        )
        database.nodeIdTree.put(id, n)
        Some(n)
    }
  }

//  def updateFromListById(database: Database, node: Node): Boolean = {
//    findNode(database, node) match {
//      case Some(n) =>
//        n.fromList ++ node.fromList
//        true
//      case None => false
//    }
//  }
//
//  def updateToListById(database: Database, node: Node): Boolean = {
//    findNode(database, node) match {
//      case Some(n) =>
//        n.toList ++ node.toList
//        true
//      case None => false
//    }
//  }

  def findById(database: Database, id: Long): Option[Node] = {
    database.nodeIdTree.get(id)
  }


}

object Relationship {

  // node 必须已经存在(已经有这id)
  def create(database: Database, relationship: Relationship): Option[Relationship] = {

    if (relationship.from.id.equals(relationship.to.id)) return None

    val from: Option[Node] = relationship.from.id match {
      case Some(id) => Node.findById(database, id)
      case None => None
    }
    val to: Option[Node] = relationship.to.id match {
      case Some(id) => Node.findById(database, id)
      case None => None
    }
    from match {
      case Some(x) =>
      case None => return None}
    to match {
      case Some(x) =>
      case None => return None}

    relationship.document.id match {
      case Some(id) =>
        Document.findById(database, id) match {
          case Some(doc) =>
            val id = database.lastRelationshipId.getAndIncrement()
            val nRs = new Relationship(
              id = Some(id),
              from = from.get,
              to = to.get,
              document = doc
            )

            database.relationshipIdTree.put(id, nRs)

            from.get.toList += nRs
            to.get.fromList += nRs

            Some(nRs)
          case None => None
        }
      case None =>
        val id = database.lastRelationshipId.getAndIncrement()
        val nRs = new Relationship(
          id = Some(id),
          from = from.get,
          to = to.get,
          document = Document.create(database, relationship.document)
        )

        database.relationshipIdTree.put(id, nRs)

        from.get.toList += nRs
        to.get.fromList += nRs

        Some(nRs)
    }
  }

}

object Document {

  private def typeName(data: Map[String, AnyRef]): String = {
    Integer.toHexString(data.map(kv => {
      kv._1 + ":" + kv._2.getClass
    }).toString().hashCode)
  }

  def create(database: Database, document: Document): Document = {
    val id = database.lastDocumentId.getAndIncrement()
    val d = new Document(
      id = Some(id),
      typeName = typeName(document.data),
      data = document.data
    )
    database.documentIdTree.put(id, d)

    d
  }

  /**
    * 根据文档id
    * 更新 data, 可能 typeName 也会更新, 如果字段名或类型进行变更
    *
    */
  def updateById(database: Database, document: Document): Option[Document] = {
    document.id match {
      case Some(id) =>
        database.documentIdTree.get(id) match {
          case Some (doc) =>
            doc.typeName = typeName (document.data)
            doc.data = document.data
            Some(doc)
          case None => None
        }
      case None => None
    }
  }

  def deleteById(database: Database): Unit = {


  }

  def findById(database: Database, id: Long): Option[Document] = {
    database.documentIdTree.get(id)
  }

}