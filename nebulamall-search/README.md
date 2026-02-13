# Nebula-Search Module

搜索服务模块，基于 Elasticsearch 和 IK 分词器实现高效的中文全文搜索能力。

## 模块说明

### 核心功能
- 产品全文搜索（支持中文 IK 分词）
- 分类筛选搜索
- 品牌筛选搜索
- 价格范围搜索
- 多条件组合搜索

### 技术栈
- **Elasticsearch 8.10.2** - 搜索引擎
- **Spring Data Elasticsearch** - ORM 框架
- **IK Analyzer** - 中文分词器
- **Spring Cloud** - 微服务框架
- **Nacos** - 配置管理和服务发现

## 项目结构

```
nebulamall-search/
├── src/main/java/com/example/nebulamall/search/
│   ├── NebulaSearchApplication.java      # 应用启动类
│   ├── config/
│   │   └── ElasticsearchConfig.java      # ES 配置
│   ├── entity/
│   │   └── ProductSearch.java            # 搜索实体
│   ├── repository/
│   │   └── ProductSearchRepository.java  # 数据仓储
│   ├── service/
│   │   └── ProductSearchService.java     # 业务逻辑
│   ├── controller/
│   │   └── ProductSearchController.java  # REST 接口
│   └── dto/
│       └── ProductSearchRequest.java     # 请求 DTO
├── src/main/resources/
│   ├── application.yml                   # 应用配置
│   └── bootstrap.properties              # Nacos 配置
└── pom.xml                               # Maven 配置
```

## 快速开始

### 1. 构建项目

```bash
cd ~/项目/project/Nebula-Mall

# 编译模块
./mvnw clean compile -DskipTests -pl nebulamall-search

# 打包模块
./mvnw clean package -DskipTests -pl nebulamall-search
```

### 2. 启动服务

```bash
# 方式一：直接运行
cd nebulamall-search
../../mvnw spring-boot:run -DskipTests

# 方式二：运行 jar 包
java -jar target/nebulamall-search-0.0.1-SNAPSHOT.jar
```

服务启动后访问：`http://localhost:10004`

### 3. 测试 API

#### 关键字搜索
```bash
curl -s "http://localhost:10004/search/product/keyword?q=中文分词" | python3 -m json.tool
```

#### 分类搜索
```bash
curl -s "http://localhost:10004/search/product/category/1" | python3 -m json.tool
```

#### 品牌搜索
```bash
curl -s "http://localhost:10004/search/product/brand/1" | python3 -m json.tool
```

#### 价格范围搜索
```bash
curl -s "http://localhost:10004/search/product/price?min=10&max=100" | python3 -m json.tool
```

#### 新增产品到索引
```bash
curl -X POST "http://localhost:10004/search/product" \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "skuId": 1,
    "productName": "人工智能产品",
    "productDescription": "基于深度学习的智能产品",
    "categoryId": 1,
    "categoryName": "电子产品",
    "brandId": 1,
    "brandName": "品牌A",
    "price": 999.99,
    "sales": 100,
    "createTime": 1644826800000
  }' | python3 -m json.tool
```

## API 文档

### 搜索接口

| 端点 | 方法 | 说明 |
|------|------|------|
| `/search/product/keyword` | GET | 关键字搜索 |
| `/search/product/category/{categoryId}` | GET | 按分类搜索 |
| `/search/product/brand/{brandId}` | GET | 按品牌搜索 |
| `/search/product/price` | GET | 价格范围搜索 |
| `/search/product/{id}` | GET | 获取单个产品 |
| `/search/product` | POST | 保存产品到索引 |
| `/search/product/{id}` | DELETE | 删除产品 |
| `/search/product/count` | GET | 获取索引总数 |

### 请求参数

**关键字搜索**
- `q` (string, required) - 搜索关键词

**分类/品牌搜索**
- `categoryId` / `brandId` (number, required) - 分类/品牌ID

**价格范围搜索**
- `min` (number, required) - 最低价格
- `max` (number, required) - 最高价格

### 响应示例

```json
[
  {
    "id": 1,
    "skuId": 1,
    "productName": "人工智能产品",
    "productDescription": "基于深度学习的智能产品",
    "categoryId": 1,
    "categoryName": "电子产品",
    "brandId": 1,
    "brandName": "品牌A",
    "price": 999.99,
    "sales": 100,
    "productImage": "http://example.com/image.jpg",
    "createTime": 1644826800000,
    "updateTime": 1644826800000
  }
]
```

## Elasticsearch 索引配置

### 索引名称
- `nebula_product` - 产品索引

### 字段配置

| 字段 | 类型 | 分词器 | 说明 |
|------|------|--------|------|
| id | keyword | - | 产品ID（主键） |
| sku_id | keyword | - | SKU ID |
| product_name | text | ik_max_word | 产品名称（支持中文分词） |
| product_description | text | ik_max_word | 产品描述（支持中文分词） |
| category_id | keyword | - | 分类ID |
| category_name | keyword | - | 分类名称 |
| brand_id | keyword | - | 品牌ID |
| brand_name | keyword | - | 品牌名称 |
| price | double | - | 价格 |
| sales | integer | - | 销售量 |
| product_image | keyword | - | 产品图片 |
| create_time | date | - | 创建时间 |
| update_time | date | - | 更新时间 |

## 配置说明

### application.yml

```yaml
server:
  port: 10004                    # 服务端口
  
spring:
  application:
    name: nebula-search         # 应用名称
  elasticsearch:
    rest:
      uris: http://127.0.0.1:9200  # ES 连接地址
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848  # Nacos 服务发现
      config:
        server-addr: 127.0.0.1:8848  # Nacos 配置管理
```

## 常见问题

### Q: 如何手动创建索引？
```bash
# 使用 Elasticsearch API
curl -X PUT "http://localhost:9200/nebula_product" \
  -H "Content-Type: application/json" \
  -d '{
    "settings": {
      "number_of_shards": 1,
      "number_of_replicas": 0
    },
    "mappings": {
      "properties": {
        "product_name": {
          "type": "text",
          "analyzer": "ik_max_word"
        },
        "product_description": {
          "type": "text",
          "analyzer": "ik_max_word"
        }
      }
    }
  }'
```

### Q: 如何测试 IK 分词效果？
```bash
# 参考 ES_IK_GUIDE.md 中的分词测试命令
curl -s -X POST http://localhost:9200/_analyze \
  -H "Content-Type: application/json" \
  -d '{"analyzer":"ik_max_word","text":"你的测试文本"}' | python3 -m json.tool
```

### Q: 如何批量导入数据到索引？
创建 `BulkImportService`，使用 `RestHighLevelClient.bulk()` 实现批量操作。

## 相关文档

- [Elasticsearch IK 分词器配置指南](../ES_IK_GUIDE.md)
- [Spring Data Elasticsearch 官方文档](https://spring.io/projects/spring-data-elasticsearch)
- [Elasticsearch 官方文档](https://www.elastic.co/guide/en/elasticsearch/reference/8.10/index.html)

## 后续开发计划

- [ ] 实现高级搜索（多条件组合）
- [ ] 实现搜索结果分页
- [ ] 实现搜索建议（autocomplete）
- [ ] 实现热词统计
- [ ] 实现搜索分析和统计
- [ ] 实现相关度排序优化
- [ ] 集成 Logstash 进行数据同步

---

**模块创建时间：2026年2月13日**
