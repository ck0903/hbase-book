# һ�� Elastric ֪ʶ��

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
I�� ��Elasticsearch �����ϣ��Ѱ�װ�����ص����أ�
II�� ��ѹtar����
III�� ��tar �����ļ���Ŀ¼�ĳɷ�root �û���
IV�� ����ElasticSearch  
./bin/elasticsearch ����./bin/elasticsearch -d
```
### 2.2��Elastic ��һЩ������͸���
```
ͨ�� curl 'http://localhost:9200/?pretty' �鿴Elasticsearch  �Ƿ������ɹ���
���ؼ�Ⱥ���ĵ���������
(���Լ�I ��ʾͷ��Ϣ)
curl -XGET 'http://localhost:9200/_count?pretty' -d '
{
"query": {
"match_all": {}
}
}
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





