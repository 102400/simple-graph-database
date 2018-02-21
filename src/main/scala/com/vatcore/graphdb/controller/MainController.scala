package com.vatcore.graphdb.controller

import com.vatcore.graphdb.component.{Database, Document, Node, Relationship}
import com.vatcore.graphdb.json.Response
import org.springframework.web.bind.annotation.{PostMapping, RequestBody, RequestMapping, RestController}

@RestController
@RequestMapping(Array("/api/"))
class MainController {

  @PostMapping(Array("/createNode"))
  def createNode(db: String, @RequestBody node: Node): Response[AnyRef] = {
    println("createNode")
    Database.findByDateName(db) match {
      case Some(database) =>
        database.synchronized({
          Node.create(database, node) match {
            case Some(n) => Response.OK(n)
            case None => Response.ActionError
          }
        })
      case None => Response.DatabaseNotExist
    }
  }

  @PostMapping(Array("/createRelationship"))
  def createRelationship(db: String, @RequestBody relationship: Relationship): Response[AnyRef] = {
    println("createRelationship")
    Database.findByDateName(db) match {
      case Some(database) =>
        database.synchronized({
          Relationship.create(database, relationship) match {
            case Some(r) => Response.OK(
              Map(
                "relationshipId" -> r.id,
                "documentId" -> r.document.id
              )
            )
            case None => Response.ActionError
          }
        })
      case None => Response.DatabaseNotExist
    }
  }

  @PostMapping(Array("/updateDocument"))
  def updateDocument(db: String, @RequestBody document: Document): Response[AnyRef] = {
    println("updateDocument")
    Database.findByDateName(db) match {
      case Some(database) =>
        database.synchronized({
          Document.updateById(database, document) match {
            case Some(doc) => Response.OK(doc)
            case None => Response.ActionError
          }
        })
      case None => Response.DatabaseNotExist
    }
  }

  @PostMapping(Array("/f"))
  def f(db: String): Response[AnyRef] = {
    println("f")
    Database.findByDateName(db) match {
      case Some(database) =>
        database.synchronized({


          Response.OK
        })
      case None => Response.DatabaseNotExist
    }
  }

}
