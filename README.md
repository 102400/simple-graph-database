# Simple Graph Database
- RESTful
- 内存型
- 不支持事务
- 数据为文档

## Component(组件)

### Node(节点)
- id: Option[Long],
- name: String,
- fromList: mutable.Buffer[Relationship],
- toList: mutable.Buffer[Relationship],
- document: Document

### Relationship(关系)
- id: Option[Long],
- name: String,
- from: Node,
- to: Node,
- document: Document

### Document(文档)
- id: Option[Long],
- typeName: String,
- data: Map[String, AnyRef]