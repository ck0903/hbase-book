# һ�� Elastric ����֪ʶ��

## 1 �������
### 1.1,HBase ��״���� ��Elasticsearch ���
```
HBase �Ĳ�ѯ����Ȼ˵�ǿ���ʵ��PB �������Ŀ����ʵ��ǧ���е�������ز�ѯ��
����HBase �Ĳ�ѯ����ǿ������rowKey �����õģ�һ����rowKey �����һ���������ģ�
rowKey �����ܵİ���ص���Ϣ��������rowKey �У����ԣ���ѯ�ٶȾͱȽϿ죬���ǣ�
�����һЩ���ƹ�ϵ�����ݿ��ϵ�ʮ�־�ȷ���ӵĲ�ѯ��ֻ����HBase �������������ġ�
���ҿ�����Hbase ��һ�������е��ǣ�����˽ṹ�Ͱ�ṹ�����ݴ洢�У�����������
�����������⣬��TP��PB ���ŵ����⡣

Elasticsearch
Elasticsearch ��һ���ֲ�ʽ������չ��ʵʱ�����������ݷ������档
�ܶ�Ĺ�˾������Elaticsearch ��ʵ����ز�ѯ������githup ����Ĵ���Ĳ�ѯ��
�ų���12��PB ���ϵ����ݣ��ײ���HBase ���ݿ⣬
```

### 1.2�����HBase ���������Ĳ�ѯ
```
�ؼ����������Elastic ��HBase ֪ʶ�ܹ���ʹ�ã�
HBase ��Elasticsearch ֮�佨������
```
## 2��Elastic ��װ�ͼ�ʹ��
### 2.1��Elasticsearch �İ�װ
```
(Elastic ��java д�������ͷ�����������jdk��װ)
���ڵ㰲װ��
I�� ��Elasticsearch �����ϣ��Ѱ�װ�����ص����أ�
II�� ��ѹtar����
III�� ��tar �����ļ���Ŀ¼�ĳɷ�root �û���
IV�� ����ElasticSearch  
./bin/elasticsearch ����./bin/elasticsearch -d

elastic �������ĵ����²ο����£�
���ڵ�elasticsearch.yml ����
cluster.name: my-cluser
node.name: s100
network.host: 192.168.1.132
http.port: 9200
#http.cors.enabled: true
#http.cors.allow-origin: "*"
#node.master: true
#node.data: true
#discovery.zen.ping.unicast.hosts: ["s100", "s101","s102"]
bootstrap.system_call_filter: false

��Ⱥģʽ��
cluster.name: my-cluser
node.name: s100
network.host: 192.168.1.132
http.port: 9200
http.cors.enabled: true
http.cors.allow-origin: "*"
node.master: true
node.data: true
discovery.zen.ping.unicast.hosts: ["s100", "s101","s102"]
bootstrap.system_call_filter: false

```
### 2.2��Elastic ��һЩ������͸���
```
ES shell ��ʽ
һ�� Elasticsearch ������κ� HTTP ����һ����������ͬ�Ĳ�����ɣ�

curl -X<VERB> '<PROTOCOL>://<HOST>:<PORT>/<PATH>?<QUERY_STRING>' -d '<BODY>'
�� < > ��ǵĲ�����
VERB
�ʵ��� HTTP ���� �� ν�� : GET`�� `POST`�� `PUT`�� `HEAD ���� `DELETE`��
PROTOCOL
http ���� https`��������� Elasticsearch ǰ����һ�� `https ����
HOST
Elasticsearch ��Ⱥ������ڵ���������������� localhost �����ػ����ϵĽڵ㡣
PORT
���� Elasticsearch HTTP ����Ķ˿ںţ�Ĭ���� 9200 ��
PATH
API ���ն�·�������� _count �����ؼ�Ⱥ���ĵ���������Path ���ܰ�����������
���磺_cluster/stats �� _nodes/stats/jvm ��
QUERY_STRING
�����ѡ�Ĳ�ѯ�ַ������� (���� ?pretty ����ʽ������� JSON ����ֵ��ʹ��������Ķ�)
BODY
һ�� JSON ��ʽ�������� (���������Ҫ�Ļ�)


ͨ�� curl 'http://localhost:9200/?pretty' �鿴Elasticsearch  �Ƿ������ɹ���
���ؼ�Ⱥ���ĵ���������
(���Լ�I ��ʾͷ��Ϣ)
curl -XGET 'http://localhost:9200/_count?pretty' -d '
{
"query": {
"match_all": {}
}
}

��ѯ��Ⱥ���ĵ��ĸ�����
curl -XGET 'http://s101:9200/_count?pretty' -d '
{
    "query": {
        "match_all": {}
    }
}
'


```
### 2.3��������
```
һ����Ա����ϵͳ������ԱĿ¼��
����
֧�ְ�����ֵ��ǩ����ֵ���Լ�ȫ�ı�������
������һ��Ա��������Ϣ
����ṹ�������������ѯ 30 �����ϵ�Ա��
����򵥵�ȫ�������Լ��ϸ��ӵĶ�������
֧����ƥ���ĵ������и�����ʾ����Ƭ��
֧�ֻ������ݴ����͹�������Ǳ���
A�����һ����Ա��Ϣ��
curl -XPUT 'localhost:9200/megacorp/employee/1?pretty' -H 'Content-Type: application/json' -d'
{
"first_name" : "John",
"last_name" :  "Smith",
"age" :        25,
"about" :      "I love to go rock climbing",
"interests": [ "sports", "music" ]
}
'


��ӵڶ�����Ա��Ϣ��
curl -XPUT 'localhost:9200/megacorp/employee/2?pretty' -H 'Content-Type: application/json' -d'
{
"first_name" :  "Jane",
"last_name" :   "Smith",
"age" :         32,
"about" :       "I like to collect rock albums",
"interests":  [ "music" ]
}
'

��ӵ�������Ա��Ϣ��
curl -XPUT 'localhost:9200/megacorp/employee/3?pretty' -H 'Content-Type: application/json' -d'
{
"first_name" :  "Douglas",
"last_name" :   "Fir",
"age" :         35,
"about":        "I like to build cabinets",
"interests":  [ "forestry" ]
}
'

B, ��ȡһ����Ա����Ϣ
curl -XGET 'localhost:9200/megacorp/employee/1?pretty'
�� HTTP ������ PUT ��Ϊ GET �������������ĵ���ͬ���ģ�
����ʹ�� DELETE ������ɾ���ĵ����Լ�ʹ�� HEAD ָ��������ĵ��Ƿ���ڡ�
���������Ѵ��ڵ��ĵ���ֻ���ٴ� PUT

C������������
curl -XGET 'localhost:9200/_search?pretty'
������Ա������
curl -XGET 'localhost:9200/megacorp/employee/1?pretty'  ����
����last_nameΪSmith ����
curl -XGET 'localhost:9200/megacorp/employee/_search?q=last_name:Smith&pretty'

D����ѯ���ʽ������(Query-string)
curl -XGET 'localhost:9200/megacorp/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
"query" : {
"match" : {
"last_name" : "Smith"
}
}
}
'

E, ��������
��ͬ����ʡ��curl -X[PUT|GET] ''localhost:9200/megacorp/employee/_search?pretty' 
-H 'Content-Type: application/json' -d' **** '
curl -XGET 'localhost:9200/megacorp/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
"query" : {
"bool": {
"must": {
"match" : {
"last_name" : "smith" 
}
},
"filter": {
"range" : {
"age" : { "gt" : 30 } 
}
}
}
}
}
'


F,ȫ����������صĶ����ҳ��� ֻҪ����rock ����climbing��
curl -XGET 'localhost:9200/megacorp/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
"query" : {
"match" : {
"about" : "rock climbing"
}
}
}
'

G�����������������ȫ��������
curl -XGET 'localhost:9200/megacorp/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
"query" : {
"match_phrase" : {
"about" : "rock climbing"
}
}
}
'
H����������
curl -XGET 'localhost:9200/megacorp/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
"query" : {
"match_phrase" : {
"about" : "rock climbing"
}
},
"highlight": {
"fields" : {
"about" : {}
}
}
}
'

I����������ͳ�ƣ�
����Ȥ���û���
curl -XGET 'localhost:9200/megacorp/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
"aggs": {
"all_interests": {
"terms": { "field": "interests" }
}
}
}
'

�Զ���������
����������Smith ���˵���Ȥ���ã�������
curl -XGET 'localhost:9200/megacorp/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
"query": {
"match": {
"last_name": "Smith"
}
},
"aggs": {
"all_interests": {
"terms": {
"field": "interests"
}
}
}
}
'
��ѯ�ض���Ȥ���õ��˵�ƽ�����䡣
curl -XGET 'localhost:9200/megacorp/employee/_search?pretty' -H 'Content-Type: application/json' -d'
{
"aggs" : {
"all_interests" : {
"terms" : { "field" : "interests" },
"aggs" : {
"avg_age" : {
"avg" : { "field" : "age" }
}
}
}
}
}
'
```



## 3��ElasticSearch ��Ⱥ��ԭ��(�������)
### 3.1 ��Ⱥ�ڵ�ԭ��
```
�ڵ㣺 һ��Elasticsearch ʵ��Ϊһ���ڵ㣬ͨ��һ���ڵ�ֻ��һ��Elasticsearch ʵ����
��Ⱥ�� �ɾ�����ͬ��cluster.name �Ľڵ���ɡ�


Elastic �������̣�
1���������͵��κ�һ���ڵ㣬��Ϊÿ���ڵ㶼֪�����ݴ����ĸ�λ�ã��������ĸ��ڵ��ϣ�
2���յ�����Ľڵ�������ֱ�ӷ��͵����ݴ��ڵĽڵ㣬
3�����ݽڵ㴦��õ�����󣬻�ֱ�Ӿ��ɵ�һ��������Ľڵ㣬�����ݷ���


��Ⱥ�������
curl -XGET 'localhost:9200/_cluster/health?pretty'


������
�������ݵĵط���ֻ��һ�����߶�������Ƭ���߼������ռ䡣

��Ƭ��
�ײ��һ��������Ԫ���������������ݵ�һ�������ݣ���Ҳ����һ��Lucence ʵ������һ���������������档

Elasticsearch �����ݣ�������ת��Ϊjson ��ʽ����ͨ�õ�NoSql ���ݽ����ĸ�ʽ����
�Ѷ���ת�����ĵ���ʽ���ֲ�ʽ�Ĵ洢

����Ϊ���һ��������
curl -XPOST 'localhost:9200/website/blog/?pretty' -H 'Content-Type: application/json' -d'
{
"title": "My second blog entry",
"text":  "Still trying this out...",
"date":  "2014/01/01"
}
'


curl -XPUT 'localhost:9200/website/blog/123?pretty' -H 'Content-Type: application/json' -d'
{
"title": "My first blog entry",
"text":  "Just trying this out...",
"date":  "2014/01/01"
}
'


ȡ��һ���ĵ�����ȡ��һ�����ݣ�
curl -XGET 'localhost:9200/website/blog/123?pretty&pretty'
curl -i -XGET 'http://localhost:9200/website/blog/124?pretty'
curl -i -XGET http://localhost:9200/website/blog/124?pretty

�����ĵ��Ĳ������ݣ�
curl -XGET 'localhost:9200/website/blog/123?_source=title,text&pretty'


ֻ����_source�����ݣ�
curl -XGET 'localhost:9200/website/blog/123/_source?pretty'

�����ĵ��Ƿ���ڣ�
curl -i -XHEAD http://localhost:9200/website/blog/123

�޸��ĵ�������PUT
curl -XPUT 'localhost:9200/website/blog/123?pretty' -H 'Content-Type: application/json' -d'
{
"title": "My first blog entry",
"text":  "I am starting to get the hang of this...",
"date":  "2014/01/02"
}
'

���̣�
1���Ӿ��ĵ����� JSON
2�����ĸ� JSON
3��ɾ�����ĵ�
4������һ�����ĵ�

������Ҫ��һ���ͻ������󣬲���Ҫ��������get��index ������


�������ĵ������ȼ����û�У�û���򱨴����򷵻ء�
PUT /website/blog/123?op_type=create
PUT /website/blog/123/_create

���ȼ����û�У�û���򱨴����򷵻�
DELETE /website/blog/123

�������ƣ�
��������
�Ȼ�ȡ���ݰ汾�ţ��޸ĵ�ʱ�򣬰ѻ�ȡ�İ汾��Ҳ���������������ʧ�ܣ��������ʧ�ܽ��д����������ύ��

���²������ݣ�
curl -XPOST 'localhost:9200/website/blog/1/_update?pretty' -H 'Content-Type: application/json' -d'
{
"doc" : {
"tags" : [ "testing" ],
"views": 0
}
}
'
ʹ�ýű����²������ݣ�
curl -XPOST 'localhost:9200/website/blog/1/_update?pretty' -H 'Content-Type: application/json' -d'
{
"script" : "ctx._source.views+=1"
}
'

�ű����ⲿ���Σ�params
curl -XPOST 'localhost:9200/website/blog/1/_update?pretty' -H 'Content-Type: application/json' -d'
{
"script" : "ctx._source.tags+=new_tag",
"params" : {
"new_tag" : "search"
}
}
'

curl -XPOST 'localhost:9200/website/blog/1/_update?pretty' -H '
Content-Type: application/json' -d'
{
"script" : "ctx.op = ctx._source.views == count ? \u0027delete\u0027 : \u0027none\u0027",
"params" : {
"count": 1
}
}
'

����ĵ������ڵ������upsert
curl -XPOST 'localhost:9200/website/pageviews/1/_update?pretty' -H '
Content-Type: application/json' -d'
{
"script" : "ctx._source.views+=1",
"upsert": {
"views": 1
}
}
'

������ͻ�ĺ�������retry_on_conflict
curl -XPOST 'localhost:9200/website/pageviews/1/_update?retry_on_conflict=5&pretty' -H '
Content-Type: application/json' -d'
{
"script" : "ctx._source.views+=1",
"upsert": {
"views": 0
}
}
'

���ϼ���������������
curl -XGET 'localhost:9200/_mget?pretty' -H 'Content-Type: application/json' -d'
{
"docs" : [
{
"_index" : "website",
"_type" :  "blog",
"_id" :    2
},
{
"_index" : "website",
"_type" :  "pageviews",
"_id" :    1,
"_source": "views"
}
]
}
'

ָ��index ��type 
curl -XGET 'localhost:9200/website/blog/_mget?pretty' -H 'Content-Type: application/json' -d'
{
"docs" : [
{ "_id" : 2 },
{ "_type" : "pageviews", "_id" :   1 }
]
}
'

```


## 4��Elastic ��Ⱥ�����������������
### 4.1 �ĵ��ĸ���
```
�����߸�����
��������ô������
/twitter/tweet/1 
����ʵ����ָ����һ���ĵ���λ�ã��������ΪLinux ���ļ��ľ���·��
```
### 4.2 �ĵ�Ԫ����
```
_index
�ĵ����Ĵ��
_type
�ĵ���ʾ�Ķ������
_id
�ĵ�Ψһ��ʶ
```

###4.3 �����ĵ������ĵ����Ա�����
```
��ʽ�������£�
curl -XPUT 'localhost:9200/website/blog/123?pretty' -H 'Content-Type: application/json' -d'
{
  "title": "My first blog entry",
  "text":  "Just trying this out...",
  "date":  "2014/01/01"
}
'

�Զ�����ID  ����������һ��ȫ�µ��ĵ��������г�ͻ��
curl -XPOST 'localhost:9200/website/blog/?pretty' -H 'Content-Type: application/json' -d'
{
  "title": "My second blog entry",
  "text":  "Still trying this out...",
  "date":  "2014/01/01"
}
'

��Put ��ʱ��ָ���汾��
curl -XPUT 'localhost:9200/twitter/tweet/1?version=2&pretty' -H 'Content-Type: application/json' -d'
{
    "message" : "elasticsearch now has versioning support, double cool!"
}
'

�����µ��ĵ�
curl -XPUT 'localhost:9200/twitter/tweet/1?op_type=create&pretty' -H '
Content-Type: application/json' -d'
{
    "user" : "kimchy",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
'
��ͬ�����£�
curl -XPUT 'localhost:9200/twitter/tweet/1/_create?pretty' -H '
Content-Type: application/json' -d'
{
    "user" : "kimchy",
    "post_date" : "2009-11-15T14:12:12",
    "message" : "trying out Elasticsearch"
}
'
```
### 4.4 ȡ���ĵ�
```
����һ���ĵ�
curl -XGET 'localhost:9200/website/blog/123?pretty&pretty'
curl -i -XGET http://localhost:9200/website/blog/124?pretty

�����ĵ��Ĳ������ݣ�
curl -XGET 'localhost:9200/website/blog/123?_source=title,text&pretty'

�����ĵ�ֻ����source ����
curl -XGET 'localhost:9200/website/blog/123/_source?pretty'

```
### 4.5 �鿴�ĵ��Ƿ����
```
curl -i -XHEAD http://localhost:9200/website/blog/123
```

### 4.6 �����ĵ�
```
curl -XPUT 'localhost:9200/website/blog/123?pretty' -H 'Content-Type: application/json' -d'
{
  "title": "My first blog entry",
  "text":  "I am starting to get the hang of this...",
  "date":  "2014/01/02"
}
'

```

### 4.7 ɾ���ĵ�
```
curl -XDELETE 'localhost:9200/website/blog/123?pretty'
```

### 4.8 ���²����ĵ�
```
ע���PUT ������
curl -XPOST 'localhost:9200/website/blog/1/_update?pretty' -H 'Content-Type: application/json' -d'
{
   "doc" : {
      "tags" : [ "testing" ],  // ��������ֶ�������
      "views": 0
   }
}
'

ʹ�ýű����в��ָ���

curl -XPOST 'localhost:9200/website/blog/1/_update?pretty' -H 'Content-Type: application/json' -d'
{
   "script" : "ctx._source.views+=1"
}
'

ʹ�ýű����ָ��£����Ҵ���
curl -XPOST 'localhost:9200/website/blog/1/_update?pretty' -H 'Content-Type: application/json' -d'
{
   "script" : "ctx._source.tags+=new_tag",
   "params" : {
      "new_tag" : "search"
   }
}
'

������������ѡ��ͨ������ ctx.op Ϊ delete ��ɾ�����������ݵ��ĵ���

POST /website/blog/1/_update
{
   "script" : "ctx.op = ctx._source.views == count ? 'delete' : 'none'",
    "params" : {
        "count": 1
    }
}

���µ�ʱ������ĵ������ڣ����´���upsert �ֶ�
curl -XPOST 'localhost:9200/website/pageviews/1/_update?pretty' -H 'Content-Type: application/json' -d'
{
   "script" : "ctx._source.views+=1",
   "upsert": {
       "views": 1
   }
}
'

���µ�ʱ�����ָ���汾�ţ��г�ͻ������µĴ���������ִ����Σ��ð汾���Զ����ӣ�
curl -XPOST 'localhost:9200/website/pageviews/1/_update?retry_on_conflict=5&pretty' -H '
Content-Type: application/json' -d'
{
   "script" : "ctx._source.views+=1",
   "upsert": {
       "views": 0
   }
}
'


```

### 4.9 ȡ�ض���ĵ�
```
curl -XGET 'localhost:9200/_mget?pretty' -H 'Content-Type: application/json' -d'
{
   "docs" : [   // docs ������ָ��Ҫȡ�ص��ĵ�
      {
         "_index" : "website",
         "_type" :  "blog",
         "_id" :    2
      },
      {
         "_index" : "website",
         "_type" :  "pageviews",
         "_id" :    1,
         "_source": "views"  // source ��ֻ����views
      }
   ]
}
'

// ȡ������/websiter/blog ���������
// Ҳ�����ú����ֵ����ָ��
curl -XGET 'localhost:9200/website/blog/_mget?pretty' -H 'Content-Type: application/json' -d'
{
   "docs" : [
      { "_id" : 2 },
      { "_type" : "pageviews", "_id" :   1 }
   ]
}
'



```
### 4.10 ������˼��ȡ����
```
curl -XPOST 'localhost:9200/_bulk?pretty' -H 'Content-Type: application/json' -d'
{ "delete": { "_index": "website", "_type": "blog", "_id": "123" }} 
{ "create": { "_index": "website", "_type": "blog", "_id": "123" }}
{ "title":    "My first blog post" }
{ "index":  { "_index": "website", "_type": "blog" }}
{ "title":    "My second blog post" }
{ "update": { "_index": "website", "_type": "blog", "_id": "123", "_retry_on_conflict" : 3} }
{ "doc" : {"title" : "My updated blog post"} }
'


���ʽΪ��
{ action: { metadata }}\n
{ request body        }\n
{ action: { metadata }}\n
{ request body        }\n


request body �����ĵ��� _source �������--�ĵ��������ֶκ�ֵ��
���� index �� create ����������ģ������е���ģ�������ṩ�ĵ���������

��Ҳ�� update ����������ģ�����Ӧ�ð����㴫�ݸ� 
update API ����ͬ�����壺 doc �� upsert �� script �ȵȡ� ɾ����������Ҫ request body �С�
```

## 5,�ֲ�ʽ�ĵ��洢
### 5.1 ��Ҫ�������ĵ��Ĵ洢ģ�ͺ����̣����Ժ������������˽�

## 6������������
### 6.1,��������ȫ��������
```
curl -XGET 'localhost:9200/_search?pretty'
```
### 6.2,�������µ�����
```
/_search
�����е��������������е�����
/gb/_search
�� gb �������������е�����
/gb,us/_search
�� gb �� us �������������е��ĵ�
/g*,u*/_search
���κ��� g ���� u ��ͷ���������������е�����
/gb/user/_search
�� gb ���������� user ����
/gb,us/user,tweet/_search
�� gb �� us ���������� user �� tweet ����
/_all/user,tweet/_search
�����е����������� user �� tweet ����
```

### 6.3 ��ҳ����
```
curl -XGET 'localhost:9200/_search?size=5&pretty'
curl -XGET 'localhost:9200/_search?size=5&from=5&pretty'
curl -XGET 'localhost:9200/_search?size=5&from=10&pretty'
```
### 6.4 ��������
```
��������url ��ָ��������
��ѯ�� tweet ������ tweet �ֶΰ��� elasticsearch ���ʵ������ĵ���
curl -XGET 'localhost:9200/_all/tweet/_search?q=tweet:elasticsearch&pretty'
��ѯ�� name �ֶ��а��� john ������ tweet �ֶ��а��� mary ���ĵ���ʵ�ʵĲ�ѯ��������
curl -XGET 'localhost:9200/_search?q=%2Bname%3Ajohn+%2Btweet%3Amary&pretty'
���ذ��� mary �������ĵ��������������ض��ֶΣ������ѯ�ַ�����ʹ�� _all �ֶν���������
curl -XGET 'localhost:9200/_search?q=mary&pretty'
����Ĳ�ѯ���tweents���ͣ���ʹ�����µ�������
name �ֶ��а��� mary ���� john
date ֵ���� 2014-09-10
_all_ �ֶΰ��� aggregations ���� geo
ƴ�ӳ��ַ���������ӣ�
?q=%2Bname%3A(mary+john)+%2Bdate%3A%3E2014-09-10+%2B(aggregations+geo)
```

## 7�� ӳ�������������ƥ�䣩
```
curl -XGET 'localhost:9200/_search?q=2014              # 12 results&pretty'
curl -XGET 'localhost:9200/_search?q=2014-09-15        # 12 results !&pretty'
curl -XGET 'localhost:9200/_search?q=date:2014-09-15   # 1  result&pretty'
curl -XGET 'localhost:9200/_search?q=date:2014         # 0  results !&pretty'

�鿴 Elasticsearch ����ν����ĵ��ṹ�ģ�
curl -XGET 'localhost:9200/gb/_mapping/tweet?pretty'
```
### 7.1 ��ȷֵ��ȫ��
```
һ���Ǿ�ȷ�ز��ҵ�ĳ��ֵ��
��һ����ģ����ƥ��ĳ��ֵ
```

### 7.2����������
```
Elasticsearch ʹ�õ��������ĸ���������ȫ��������������ʲô�ǵ�������������Ҫ���������⣩
```

### 7.2�������������
```
�������̣�
1���������ֳ��ʺ��ڵ��������Ĵ�����
2����������׼������߿������ԡ�

�ַ���������
�����ݹ��˳��ַ���
�ִ���
���ַ����ֳɴ���Token
Token ��������


��������
��׼������
�򵥷�����
�ո������
���Է�����
���磺
"Set the shape to semi-transparent by calling set_trans(5)"
��׼������
��׼��������ElasticsearchĬ��ʹ�õķ����������Ƿ������������ı���õ�ѡ��
������ Unicode ���� ����� ���ʱ߽� �����ı���ɾ�����󲿷ֱ�㡣��󣬽�����Сд���������
set, the, shape, to, semi, transparent, by, calling, set_trans, 5

�򵥷�����
�򵥷��������κβ�����ĸ�ĵط��ָ��ı���������Сд���������
set, the, shape, to, semi, transparent, by, calling, set, trans

�ո������
�ո�������ڿո�ĵط������ı����������
Set, the, shape, to, semi-transparent, by, calling, set_trans(5)

���Է�����
�ض����Է����������� �ܶ����ԡ����ǿ��Կ���ָ�����Ե��ص㡣���磬
 Ӣ�� ������������һ��Ӣ�����ôʣ����õ��ʣ����� and ���� the ��
 ���Ƕ������û�ж���Ӱ�죩�����ǻᱻɾ���� �������Ӣ���﷨�Ĺ���
 ����ִ���������ȡӢ�ﵥ�ʵ� �ʸ� ��
Ӣ�� �ִ������������Ĵ�����
set, shape, semi, transpar, call, set_tran, 5
```

### 7.3, ӳ��
```
curl -XGET 'localhost:9200/gb/_mapping/tweet?pretty'

ָ����ȫ����������ȷ���ң����߲��Ҳ�����
index ���Կ������������ַ���������������������ֵ��
analyzed
���ȷ����ַ�����Ȼ�������������仰˵����ȫ�����������
not_analyzed
  ����������������ܹ������������������Ǿ�ȷֵ������������з�����
no
���������������򲻻ᱻ��������
string �� index ����Ĭ���� analyzed �����������ӳ������ֶ�Ϊһ����ȷֵ��
������Ҫ������Ϊ not_analyzed ��


ָ����������
analyzer�༭
���� analyzed �ַ������� analyzer ����ָ��������������ʱʹ�õķ�������Ĭ�ϣ� 
Elasticsearch ʹ�� standard ��������
 �������ָ��һ�����õķ���������������� whitespace �� simple �� `english`��
{
    "tweet": {
        "type":     "string",
        "analyzer": "english"
    }
}


Ϊ�˸���ӳ�䣬��Ҫ��ɾ��������
curl -XDELETE 'localhost:9200/gb?pretty'

Ȼ�����½�������
curl -XPUT 'localhost:9200/gb?pretty' -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "tweet" : {
      "properties" : {
        "tweet" : {
          "type" :    "string",
          "analyzer": "english"
        },
        "date" : {
          "type" :   "date"
        },
        "name" : {
          "type" :   "string"
        },
        "user_id" : {
          "type" :   "long"
        }
      }
    }
  }
}
'

���ӳ�䣺
curl -XPUT 'localhost:9200/gb/_mapping/tweet?pretty' -H 'Content-Type: application/json' -d'
{
  "properties" : {
    "tag" : {
      "type" :    "string",
      "index":    "not_analyzed"
    }
  }
}
'

����ӳ������
curl -XGET 'localhost:9200/gb/_analyze?pretty' -H 'Content-Type: application/json' -d'
{
  "field": "tweet",
  "text": "Black-cats" 
}
'
curl -XGET 'localhost:9200/gb/_analyze?pretty' -H 'Content-Type: application/json' -d'
{
  "field": "tag",
  "text": "Black-cats" 
}
'
```
### 7.4 ���Ӻ���ӳ�����
```
��ֵ��
{ "tag": [ "search", "nosql" ]}
����
"null_value":               null,
"empty_array":              [],
"array_with_null_value":    [ null ]

��㼶����
���ö���

```
## 8�������������������ѯ,��ѯ�����ض����ԣ�
### 8.1 �ղ�ѯ
```
curl -XGET 'localhost:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{}
'
֧��POST ����
```

### 8.2 ��ѯ���ʽ
```
�ṹ��
GET /_search
{
    "query": YOUR_QUERY_HERE
}
���ӣ�
curl -XGET 'localhost:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query": {
        "match_all": {}
    }
}
'
һ����ѯ��� �ĵ��ͽṹ��

{
    QUERY_NAME: {
        ARGUMENT: VALUE,
        ARGUMENT: VALUE,...
    }
}
��������ĳ���ֶΣ���ô���Ľṹ���£�

{
    QUERY_NAME: {
        FIELD_NAME: {
            ARGUMENT: VALUE,
            ARGUMENT: VALUE,...
        }
    }
}
�ٸ����ӣ������ʹ�� match ��ѯ��� ����ѯ tweet �ֶ��а��� elasticsearch �� tweet��
{
    "match": {
        "tweet": "elasticsearch"
    }
}

��������Query DSL
curl -XGET 'localhost:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query": {
        "match": {
            "tweet": "elasticsearch"
        }
    }
}
'

�ϲ���ѯ����䣺
��ѯ���(Query clauses) ����һЩ�򵥵���Ͽ� ��
��Щ��Ͽ���Ա˴�֮��ϲ���ɸ����ӵĲ�ѯ����Щ��������������ʽ��
Ҷ����䣨Leaf clauses�� (���� match ���) �����ڽ���ѯ�ַ�����һ���ֶΣ����߶���ֶΣ��Աȡ�
����(Compound) ��� ��Ҫ���� �ϲ�������ѯ��䡣 ���磬
һ�� bool ��� ����������Ҫ��ʱ�����������䣬������ must ƥ�䡢 must_not
 ƥ�仹�� should ƥ�䣬ͬʱ�����԰��������ֵĹ�������filters����
{
    "bool": {
        "must":     { "match": { "tweet": "elasticsearch" }},
        "must_not": { "match": { "name":  "mary" }},
        "should":   { "match": { "tweet": "full text" }},
        "filter":   { "range": { "age" : { "gt" : 30 }} }
    }
}
һ�����������Ժϲ� �κ� ������ѯ��䣬����������䣬�˽���һ���Ǻ���Ҫ�ġ�
�����ζ�ţ��������֮����Ի���Ƕ�ף����Ա��ǳ����ӵ��߼���
���磬���²�ѯ��Ϊ���ҳ��ż����İ��� business opportunity ���Ǳ��ʼ�
���������ռ������İ��� business opportunity �ķ������ʼ���
{
    "bool": {
        "must": { "match":   { "email": "business opportunity" }},
        "should": [
            { "match":       { "starred": true }},
            { "bool": {
                "must":      { "match": { "folder": "inbox" }},
                "must_not":  { "match": { "spam": true }}
            }}
        ],
        "minimum_should_match": 1
    }
}
```

### 8.3 ����(bool ��ѯ)�Ͳ�ѯ
```
���ˣ�
��ȷ�еģ�
created ʱ���Ƿ��� 2013 �� 2014 ������䣿
status �ֶ��Ƿ���� published ������ʣ�
lat_lon �ֶα�ʾ��λ���Ƿ���ָ����� 10km ��Χ�ڣ�

��ѯ��
������ full text search ����������ƥ����ĵ�
���� run ����ʣ�Ҳ��ƥ�� runs �� running �� jog ���� sprint
���� quick �� brown �� fox �⼸���� �� ��֮�����Խ�����ĵ������Խ��
���� lucene �� search ���� java ��ǩ �� ��ǩԽ�࣬�����Խ��

�������ܸ��ڲ�ѯ

```

### 8.4 һЩ���õĲ�ѯ��
```
match_all ��ѯ��
{ "match_all": {}}
match ��ѯ��
{ "match": { "tweet": "About Search" }}
{ "match": { "age":    26           }}
{ "match": { "date":   "2014-09-01" }}
{ "match": { "public": true         }}
{ "match": { "tag":    "full_text"  }}

muti_match ��ѯ��
������ֶΣ�
{
    "multi_match": {
        "query":    "full text search",
        "fields":   [ "title", "body" ]
    }
}

range ��ѯ��
{
    "range": {
        "age": {
            "gte":  20,
            "lt":   30
        }
    }
}

term ��ѯ������ȷ��ѯ��
{ "term": { "age":    26           }}
{ "term": { "date":   "2014-09-01" }}
{ "term": { "public": true         }}
{ "term": { "tag":    "full_text"  }}

terms ��ѯ��
{ "terms": { "tag": [ "search", "full_text", "nosql" ] }}


terms ��ѯ�� term ��ѯһ��������������ָ����ֵ����ƥ�䡣
�������ֶΰ�����ָ��ֵ�е��κ�һ��ֵ����ô����ĵ�����������

exists ��ѯ�� missing ��ѯ�༭
exists ��ѯ�� missing ��ѯ�����ڲ�����Щָ���ֶ�����ֵ 
(exists) ����ֵ (missing) ���ĵ�������SQL�е� IS_NULL (missing) �� 
NOT IS_NULL (exists) �ڱ����Ͼ��й��ԣ�

{
    "exists":   {
        "field":    "title"
    }
}
```
### 8.5 ��ϲ�ѯ
```
must
�ĵ� ���� ƥ����Щ�������ܱ�����������
must_not
�ĵ� ���벻 ƥ����Щ�������ܱ�����������
should
���������Щ����е�������䣬������ _score ������
���κ�Ӱ�졣������Ҫ��������ÿ���ĵ�������Ե÷֡�
filter
���� ƥ�䣬�����Բ����֡�����ģʽ�����С���Щ��������û�й��ף�
ֻ�Ǹ��ݹ��˱�׼���ų�������ĵ���

����Ĳ�ѯ���ڲ��� title �ֶ�ƥ�� how to make millions ���Ҳ�����ʶΪ
 spam ���ĵ�����Щ����ʶΪ starred ����2014֮����ĵ���
����������Щ�ĵ�ӵ�и��ߵ���������� _����_ �����㣬��ô�����������ߣ�
{
    "bool": {
        "must":     { "match": { "title": "how to make millions" }},
        "must_not": { "match": { "tag":   "spam" }},
        "should": [
            { "match": { "tag": "starred" }},
            { "range": { "date": { "gte": "2014-01-01" }}}
        ]
    }
}
������ǲ�����Ϊ�ĵ���ʱ���Ӱ��÷֣������� filter �������дǰ������ӣ�

{
    "bool": {
        "must":     { "match": { "title": "how to make millions" }},
        "must_not": { "match": { "tag":   "spam" }},
        "should": [
            { "match": { "tag": "starred" }}
        ],
        "filter": {
          "range": { "date": { "gte": "2014-01-01" }} 
        }
    }
}

�������Ҫͨ�������ͬ�ı�׼����������ĵ���
bool ��ѯ����Ҳ���Ա����������ֵĲ�ѯ���򵥵ؽ������õ� filter ����в����ڲ����������߼���
{
    "bool": {
        "must":     { "match": { "title": "how to make millions" }},
        "must_not": { "match": { "tag":   "spam" }},
        "should": [
            { "match": { "tag": "starred" }}
        ],
        "filter": {
          "bool": { 
              "must": [
                  { "range": { "date": { "gte": "2014-01-01" }}},
                  { "range": { "price": { "lte": 29.99 }}}
              ],
              "must_not": [
                  { "term": { "category": "ebooks" }}
              ]
          }
        }
    }
}

```
### 8.6 ��֤��ѯ
```
��֤��ѯ��
curl -XGET 'localhost:9200/gb/tweet/_validate/query?pretty' -H 'Content-Type: application/json' -d'
{
   "query": {
      "tweet" : {
         "match" : "really powerful"
      }
   }
}
'

������
curl -XGET 's200:9200/gb/tweet/_validate/query?explain&pretty' -H 'Content-Type: application/json' -d'
{
   "query": {
      "tweet" : {
         "match" : "really powerful"
      }
   }
}
'

����ѯ���
curl -XGET 'localhost:9200/_validate/query?explain&pretty' -H 'Content-Type: application/json' -d'
{
   "query": {
      "match" : {
         "tweet" : "really powerful"
      }
   }
}
'
```

## 9,�����������
### 9.1 ����
```

�����ֶν�������
curl -XGET 'localhost:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "bool" : {
            "filter" : { "term" : { "user_id" : 1 }}
        }
    },
    "sort": { "date": { "order": "desc" }}
}
'
�༶����
curl -XGET 'localhost:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "bool" : {
            "must":   { "match": { "tweet": "manage text search" }},
            "filter" : { "term" : { "user_id" : 2 }}
        }
    },
    "sort": [
        { "date":   { "order": "desc" }},
        { "_score": { "order": "desc" }}
    ]
}
'

curl -XGET 's101:9200/_search?sort=date:desc&sort=_score&q=search&pretty'

```
### 9.2 ****** ����������Ƚ���������������ʱ���ٽ�������

## 10�� �ֲ�ʽ������ԭ������
```
�ֲ�ʽ������ԭ��Ҳ�����Ƚ����Թ�
```

## 11�������Ĺ���
### 11.1 ����һ������
```
����һ��������ʱ�򣬿���Ϊ����������Լ�ӳ�䣬��������Ż�������
��ʽ�������£�
PUT /my_index
{
    "settings": { ... any settings ... },
    "mappings": {
        "type_one": { ... any mappings ... },
        "type_two": { ... any mappings ... },
        ...
    }
}


```
### 11.2 ɾ������
```
�����µ������� ɾ������:

DELETE /my_index
��Ҳ��������ɾ�����������

DELETE /index_one,index_two
DELETE /index_*
��������������ɾ�� ȫ�� ������

DELETE /_all
DELETE /*


```

### 11.3 ��������
```
����һ����������������ƬΪһ��������ƬΪ0
curl -XPUT 'localhost:9200/my_temp_index?pretty' -H 'Content-Type: application/json' -d'
{
    "settings": {
        "number_of_shards" :   1,
        "number_of_replicas" : 0
    }
}
'

���������ĸ���ƬΪ1��
curl -XPUT 'localhost:9200/my_temp_index/_settings?pretty' -H 'Content-Type: application/json' -d'
{
    "number_of_replicas": 1
}
'

```
### 11.4�����÷�����
```
��ʱ�ò����������ٽ����о�
```

### 11.5 �Զ��������
```
��ʱ�ò����������ٽ����о�
```
### 11.6 ������ӳ��
```
��ʱ�ò����������ٽ����о�
```

### 11.6 ������
```
_type,_index �ȵĺ���
��ʱ�ò����������ٽ����о�
```

### 11.7 ��̬ӳ��
```
�����������е��ĵ���ʽ�У����µ��ֶμ���ʱ��Ϊ��������ӳ�䡣
```
### 11.8 �Զ��嶯̬ӳ��
```
��ʱ�ò����������ٽ����о�
```

### 11.9 ȱʡӳ��
```
��ʱ�ò����������ٽ����о�
```

### 11.10 ���������������
```
��ʱ�ò����������ٽ����о�
```
### 11.11 ������������ͣ�������������������ݣ�
```
��ʱ�ò����������ٽ����о�
```

## 12 ��Ⱥ�ڷ�Ƭԭ��
```
�Ƚ��������������ٻع�ͷ����
```

# �����߼�����
## 1���ṹ�����������˲���������������--����ԱȽϣ�Ч�ʸߣ�
### 1.1 ��ȷֵ����
```
������Es �ϲ����������ݣ�
curl -XPOST 'localhost:9200/my_store/products/_bulk?pretty' -H 'Content-Type: application/json' -d'
{ "index": { "_id": 1 }}
{ "price" : 10, "productID" : "XHDK-A-1293-#fJ3" }
{ "index": { "_id": 2 }}
{ "price" : 20, "productID" : "KDKE-B-9947-#kL5" }
{ "index": { "_id": 3 }}
{ "price" : 30, "productID" : "JODL-X-1937-#pV7" }
{ "index": { "_id": 4 }}
{ "price" : 30, "productID" : "QQPX-R-3956-#aD8" }
'


term����������ѯ����,boolean,�����Լ��ı�
term ��ѯ����
�������£�
curl -XGET 'localhost:9200/my_store/products/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "constant_score" : { 
            "filter" : {
                "term" : { 
                    "price" : 20
                }
            }
        }
    }
}
'

trem ��ѯ�ı���
curl -XGET 'localhost:9200/my_store/products/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "term" : {
                    "productID" : "XHDK-A-1293-#fJ3"
                }
            }
        }
    }
}
'

������ѯ�������⣬
�ز�����ȷ��ֵ��
ԭ������ΪproductID ��������˶��token�����ֶΣ�
���������²鿴�����ķ�������
curl -XGET 'localhost:9200/my_store/_analyze?pretty' -H 'Content-Type: application/json' -d'
{
  "field": "productID",
  "text": "XHDK-A-1293-#fJ3"
}
'
��������Ϊ���£�
{
  "tokens" : [ {
    "token" :        "xhdk",
    "start_offset" : 0,
    "end_offset" :   4,
    "type" :         "<ALPHANUM>",
    "position" :     1
  }, {
    "token" :        "a",
    "start_offset" : 5,
    "end_offset" :   6,
    "type" :         "<ALPHANUM>",
    "position" :     2
  }, {
    "token" :        "1293",
    "start_offset" : 7,
    "end_offset" :   11,
    "type" :         "<NUM>",
    "position" :     3
  }, {
    "token" :        "fj3",
    "start_offset" : 13,
    "end_offset" :   16,
    "type" :         "<ALPHANUM>",
    "position" :     4
  } ]
}

����������������Ҫ���´���������prodectID ƥ��ɾ�ȷ���ң�
curl -XDELETE 'localhost:9200/my_store?pretty'
curl -XPUT 'localhost:9200/my_store?pretty' -H 'Content-Type: application/json' -d'
{
    "mappings" : {
        "products" : {
            "properties" : {
                "productID" : {
                    "type" : "string",
                    "index" : "not_analyzed" 
                }
            }
        }
    }
'

Ȼ������Es ���뿪ʼ��ʱ�������
�����������ɡ�

term ��ѯ

```
### 1.2 ��Ϲ�����
```
�����������༭
һ�� bool ����������������ɣ�
{
   "bool" : {
      "must" :     [],
      "should" :   [],
      "must_not" : [],
   }
}
must
���е���䶼 ���루must�� ƥ�䣬�� AND �ȼۡ�
must_not
���е���䶼 ���ܣ�must not�� ƥ�䣬�� NOT �ȼۡ�
should
������һ�����Ҫƥ�䣬�� OR �ȼۡ�


curl -XGET 'localhost:9200/my_store/products/_search?pretty' -H 'Content-Type: application/json' -d'
{
   "query" : {
      "filtered" : { 
         "filter" : {
            "bool" : {
              "should" : [
                 { "term" : {"price" : 20}}, 
                 { "term" : {"productID" : "XHDK-A-1293-#fJ3"}} 
              ],
              "must_not" : {
                 "term" : {"price" : 30} 
              }
           }
         }
      }
   }
}
'

Ƕ�׵�bool ������
curl -XGET 'localhost:9200/my_store/products/_search?pretty' -H 'Content-Type: application/json' -d'
{
   "query" : {
      "filtered" : {
         "filter" : {
            "bool" : {
              "should" : [
                { "term" : {"productID" : "KDKE-B-9947-#kL5"}}, 
                { "bool" : { 
                  "must" : [
                    { "term" : {"productID" : "JODL-X-1937-#pV7"}}, 
                    { "term" : {"price" : 30}} 
                  ]
                }}
              ]
           }
         }
      }
   }
}
'
```

### 1.3 ���Ҷ����ȷֵ
```
terms ��ѯ
���Ҷ����ȷֵ��
curl -XGET 'localhost:9200/my_store/products/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "terms" : { 
                    "price" : [20, 30]
                }
            }
        }
    }
}
'

 term �� terms �� ���������must contain�� ������������ ���뾫ȷ��ȣ�must equal exactly�� ��
 
 ���ֶξ�ȷ����
 # Delete the `my_index` index
 DELETE /my_index
 
 # Index example docs
 PUT /my_index/my_type/1
 {
   "tags": [
     "search"
   ],
   "tag_count": 1
 }
 
 PUT /my_index/my_type/2
 {
   "tags": [
     "search",
     "open_source"
   ],
   "tag_count": 2
 }
 
 # Where tags = "search" only
 GET /my_index/my_type/_search
 {
   "query": {
     "constant_score": {
       "filter": {
         "bool": {
           "must": [
             {
               "term": {
                 "tags": "search"
               }
             },
             {
               "term": {
                 "tag_count": 1
               }
             }
           ]
         }
       }
     }
   }
 }

```

### 1.4 ��Χ����
```
# Delete the `my_store` index
DELETE /my_store

# Index example docs
POST /my_store/products/_bulk
{"index":{"_id":1}}
{"price":10,"productID":"XHDK-A-1293-#fJ3"}
{"index":{"_id":2}}
{"price":20,"productID":"KDKE-B-9947-#kL5"}
{"index":{"_id":3}}
{"price":30,"productID":"JODL-X-1937-#pV7"}
{"index":{"_id":4}}
{"price":30,"productID":"QQPX-R-3956-#aD8"}


# Where 20 <= `price` < 40
GET /my_store/products/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "range": {
          "price": {
            "gte": 20,
            "lt": 40
          }
        }
      }
    }
  }
}

# Where `price` > 20
GET /my_store/products/_search
{
  "query": {
    "constant_score": {
      "filter": {
        "range": {
          "price": {
            "gt": 20
          }
        }
      }
    }
  }
}


ʱ�䷶Χ��
range ��ѯͬ������Ӧ���������ֶ��ϣ�
"range" : {
    "timestamp" : {
        "gt" : "2014-01-01 00:00:00",
        "lt" : "2014-01-07 00:00:00"
    }
}
��ʹ�������������ֶ�ʱ�� range ��ѯ֧�ֶ� ���ڼ��㣨date math�� 
���в������ȷ�˵��������������ʱ����ڹ�ȥһСʱ�ڵ������ĵ���
"range" : {
    "timestamp" : {
        "gt" : "now-1h"
    }
}
�����������һֱ����ʱ����ڹ�ȥһ��Сʱ�ڵ������ĵ����ù�������
Ϊһ��ʱ�� �������ڣ�sliding window�� �������ĵ���
���ڼ��㻹���Ա�Ӧ�õ�ĳ�������ʱ�䣬����ֻ����һ
���� now ������ռλ����ֻҪ��ĳ�����ں����һ��˫�ܷ��� (||) ������һ��������ѧ���ʽ����������
"range" : {
    "timestamp" : {
        "gt" : "2014-01-01 00:00:00",
        "lt" : "2014-01-01 00:00:00||+1M" 
    }
}

�ֵ���
5, 50, 6, B, C, a, ab, abb, abc, b

�����������Ҵ� a �� b �������������ַ�����ͬ������ʹ�� range ��ѯ�﷨��

"range" : {
    "title" : {
        "gte" : "a",
        "lt" :  "b"
    }
}
```
### 1.5 ����NULL ֵ
```
curl -XPOST 'localhost:9200/my_index/posts/_bulk?pretty' -H 'Content-Type: application/json' -d'
{ "index": { "_id": "1"              }}
{ "tags" : ["search"]                }  
{ "index": { "_id": "2"              }}
{ "tags" : ["search", "open_source"] }  
{ "index": { "_id": "3"              }}
{ "other_field" : "some data"        }  
{ "index": { "_id": "4"              }}
{ "tags" : null                      }  
{ "index": { "_id": "5"              }}
{ "tags" : ["search", null]          }
'

�鿴�ֶ��Ƿ���ڣ�
curl -XGET 'localhost:9200/my_index/posts/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "constant_score" : {
            "filter" : {
                "exists" : { "field" : "tags" }
            }
        }
    }
}
'


�鿴�Ƿ�ȱʧ
curl -XGET 'localhost:9200/my_index/posts/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query" : {
        "constant_score" : {
            "filter": {
                "missing" : { "field" : "tags" }
            }
        }
    }
}
'

�ڲ������ȱʧ�ᱻ���������£�
{
    "exists" : { "field" : "name" }
}
ʵ��ִ�е��ǣ�

{
    "bool": {
        "should": [
            { "exists": { "field": "name.first" }},
            { "exists": { "field": "name.last" }}
        ]
    }
}
```

### 1.6�� ���ڻ���
```
```
## 2, ȫ���������������Ժͷ������ǹ��ˣ�
###2.1, ���ڴ����������ȫ�ļ���
###2.2��ƥ���ѯ
```
��������
curl -XDELETE 'localhost:9200/my_index?pretty'
curl -XPUT 'localhost:9200/my_index?pretty' -H 'Content-Type: application/json' -d'
{ "settings": { "number_of_shards": 1 }}
'
curl -XPOST 'localhost:9200/my_index/my_type/_bulk?pretty' -H 'Content-Type: application/json' -d'
{ "index": { "_id": 1 }}
{ "title": "The quick brown fox" }
{ "index": { "_id": 2 }}
{ "title": "The quick brown fox jumps over the lazy dog" }
{ "index": { "_id": 3 }}
{ "title": "The quick brown fox jumps over the quick dog" }
{ "index": { "_id": 4 }}
{ "title": "Brown fox brown dog" }
'

���������ƥ�䣺
curl -XGET 'localhost:9200/my_index/my_type/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query": {
        "match": {
            "title": "QUICK!"
        }
    }
}
'
```
### 2.3 ��ʲ�ѯ
```
��ѯ����ʵ�ƥ��
curl -XGET 'localhost:9200/my_index/my_type/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query": {
        "match": {
            "title": "BROWN DOG!"
        }
    }
}
'

��߾��ȣ�
curl -XGET 'localhost:9200/my_index/my_type/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query": {
        "match": {
            "title": {      
                "query":    "BROWN DOG!",
                "operator": "and"
            }
        }
    }
}
'
���ƾ��ȣ�
curl -XGET 'localhost:9200/my_index/my_type/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "query": {
    "match": {
      "title": {
        "query":                "quick brown dog",
        "minimum_should_match": "75%"
      }
    }
  }
}
'

```
### 2.4 ��ϲ�ѯ
```
curl -XGET 'localhost:9200/my_index/my_type/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "must":     { "match": { "title": "quick" }},
      "must_not": { "match": { "title": "lazy"  }},
      "should": [
                  { "match": { "title": "brown" }},
                  { "match": { "title": "dog"   }}
      ]
    }
  }
}
'
���ƾ��ȣ�
curl -XGET 'localhost:9200/my_index/my_type/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "should": [
        { "match": { "title": "brown" }},
        { "match": { "title": "fox"   }},
        { "match": { "title": "dog"   }}
      ],
      "minimum_should_match": 2 
    }
  }
}
'

```

### 2.5 boolƥ���ʹ��
```
�ȼ۲���
{
    "match": { "title": "brown fox"}
}
{
  "bool": {
    "should": [
      { "term": { "title": "brown" }},
      { "term": { "title": "fox"   }}
    ]
  }
}

�ȼ۲�����
{
    "match": {
        "title": {
            "query":    "brown fox",
            "operator": "and"
        }
    }
}
{
  "bool": {
    "must": [
      { "term": { "title": "brown" }},
      { "term": { "title": "fox"   }}
    ]
  }
}

�ȼ۲�����
{
    "match": {
        "title": {
            "query":                "quick brown fox",
            "minimum_should_match": "75%"
        }
    }
}
{
  "bool": {
    "should": [
      { "term": { "title": "brown" }},
      { "term": { "title": "fox"   }},
      { "term": { "title": "quick" }}
    ],
    "minimum_should_match": 2 
  }
}
```

### 2.6  Ȩ�ش����Ǹ����صĸ���Ҫ
```
��ͨ�Ĳ���Ȩ�ص�����£�
curl -XGET 'localhost:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query": {
        "bool": {
            "must": {
                "match": {
                    "content": { 
                        "query":    "full text search",
                        "operator": "and"
                    }
                }
            },
            "should": [ 
                { "match": { "content": "Elasticsearch" }},
                { "match": { "content": "Lucene"        }}
            ]
        }
    }
}
'

����Ȩ�ص�����£�
curl -XGET 'localhost:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
    "query": {
        "bool": {
            "must": {
                "match": {  
                    "content": {
                        "query":    "full text search",
                        "operator": "and"
                    }
                }
            },
            "should": [
                { "match": {
                    "content": {
                        "query": "Elasticsearch",
                        "boost": 3 
                    }
                }},
                { "match": {
                    "content": {
                        "query": "Lucene",
                        "boost": 2 
                    }
                }}
            ]
        }
    }
}
'
```
### 2.7 ���Ʒ�������
```
Ϊ�ֶ����÷�����
curl -XPUT 'localhost:9200/my_index/_mapping/my_type?pretty' -H 'Content-Type: application/json' -d'
{
    "my_type": {
        "properties": {
            "english_title": {
                "type":     "string",
                "analyzer": "english"
            }
        }
    }
}
'

�鿴�������ķ�����Ϊ
curl -XGET 'localhost:9200/my_index/_analyze?pretty' -H 'Content-Type: application/json' -d'
{
  "field": "my_type.title",   
  "text": "Foxes"
}
'
curl -XGET 'localhost:9200/my_index/_analyze?pretty' -H 'Content-Type: application/json' -d'
{
  "field": "my_type.english_title",   
  "text": "Foxes"
}
'

�������̣�
��ѯ�Լ������ analyzer ������
�ֶ�ӳ���ﶨ��� search_analyzer ������
�ֶ�ӳ���ﶨ��� analyzer ������
������������Ϊ default_search �ķ�������Ĭ��Ϊ
������������Ϊ default �ķ�������Ĭ��Ϊ
standard ��׼������
```

## 3�� ���ֶ�����
### 3.1 ���ֶβ�ѯ
```
����1��
curl -XGET 'localhost:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "should": [
        { "match": { "title":  "War and Peace" }},
        { "match": { "author": "Leo Tolstoy"   }}
      ]
    }
  }
}
'

����2��
curl -XGET 'localhost:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "should": [
        { "match": { "title":  "War and Peace" }},
        { "match": { "author": "Leo Tolstoy"   }},
        { "bool":  {
          "should": [
            { "match": { "translator": "Constance Garnett" }},
            { "match": { "translator": "Louise Maude"      }}
          ]
        }}
      ]
    }
  }
}
'


�������޼���һ���ϸ���Ȩ��������

curl -XGET 'localhost:9200/_search?pretty' -H 'Content-Type: application/json' -d'
{
  "query": {
    "bool": {
      "should": [
        { "match": { 
            "title":  {
              "query": "War and Peace",
              "boost": 2
        }}},
        { "match": { 
            "author":  {
              "query": "Leo Tolstoy",
              "boost": 2
        }}},
        { "bool":  { 
            "should": [
              { "match": { "translator": "Constance Garnett" }},
              { "match": { "translator": "Louise Maude"      }}
            ]
        }}
      ]
    }
  }
}
'
����bool ��ѯ��Ȩ��Ϊ1
```



