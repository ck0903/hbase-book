# DOC API (针对单个索引的API)
## 单个文档的API
##### Index API
##### Get API
##### Delete API
##### Update API
## 多文档的API
##### Muti Get API
##### Bulk API
##### Delete By Query API
##### Upadate By Query API
#####
## 单个文档的API
### Index API
```
一般，下面是为一个Json 文档建立索引的例子：
curl -XPUT 's200:9200/twitter/tweet/1?pretty' -H '
Content-Type: application/json' -d'
{
    "user" : "kimchy",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
'

执行后的结果类似如下：
{
  "_index" : "twitter",  // 索引 表名
  "_type" : "tweet",  // 类型，对应cloumnFamily
  "_id" : "1",    // id ，唯一值，一般是rowkeys
  "_version" : 3,   // 包括第一次的添加，内容变动过三次
  "result" : "updated",  // updated，crated,
  "_shards" : {         // 分片情况
    "total" : 2,        // 总的分片
    "successful" : 1,    // 成功的分片
    "failed" : 0           // 失败分片
  },
  "created" : false  // 是否新建
}

创建索引的时候可以为其制定id，也可以不指定（由Elasticsearch 自动生成）

put 的时候可以设置版本号，在数据一致性上，一般更新的时候，先把版本号获取，
然后进行put 的时候，也把版本号带上。
例子如下：
curl -XPUT 's200:9200/twitter/tweet/1?version=2&pretty' -H '
Content-Type: application/json' -d'
{
    "message" : "elasticsearch now has versioning support, double cool!"
}
'


强制制定为添加操作，如果已经存在，则会添加失败：
curl -XPUT 'localhost:9200/twitter/tweet/1?op_type=create&pretty' -H '
Content-Type: application/json' -d'
{
    "user" : "kimchy",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
'
等同于如下：
curl -XPUT 'localhost:9200/twitter/tweet/1/_create?pretty' -H '
Content-Type: application/json' -d'
{
    "user" : "kimchy",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
'

以下是自动生成ID 的例子：
curl -XPOST 'localhost:9200/twitter/tweet/?pretty' -H '
Content-Type: application/json' -d'
{
    "user" : "kimchy",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
'

文档间还存在父子的关系
curl -XPUT 'localhost:9200/blogs?pretty' -H '
Content-Type: application/json' -d'
{
  "mappings": {
    "tag_parent": {},
    "blog_tag": {
      "_parent": {
        "type": "tag_parent"
      }
    }
  }
}
'
curl -XPUT 'localhost:9200/blogs/blog_tag/1122?parent=1111&pretty' -H '
Content-Type: application/json' -d'
{
    "tag" : "something"
}
'




```

