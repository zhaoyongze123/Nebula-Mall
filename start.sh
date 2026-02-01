#!/bin/bash
# 快速启动脚本
# 用于启动 Nebula-Mall 项目的所有服务

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$SCRIPT_DIR"

echo "=========================================="
echo "Nebula-Mall 项目启动脚本"
echo "=========================================="

# 1. 启动 Docker 中间件
echo ""
echo "[1/3] 启动 Docker 中间件..."
cd "$PROJECT_DIR"
docker-compose up -d
echo "✓ Docker 服务已启动 (MySQL, Redis, Elasticsearch, Nacos, RabbitMQ, MinIO, Seata)"

# 2. 启动后端服务
echo ""
echo "[2/3] 启动后端服务 (renren-fast)..."
cd "$PROJECT_DIR/renren-fast"
if [ ! -f "target/renren-fast-1.0.0.jar" ]; then
    echo "  → 编译项目..."
    mvn clean package -DskipTests -q
fi
nohup java -jar target/renren-fast-1.0.0.jar > /tmp/renren-fast.log 2>&1 &
sleep 3
echo "✓ 后端服务已启动 (http://localhost:8080/renren-fast)"

# 3. 启动前端服务
echo ""
echo "[3/3] 启动前端服务 (renren-fast-vue)..."
# 设置正确的 Node.js 版本
export PATH="/opt/homebrew/Cellar/node@18/18.20.8/bin:$PATH" 2>/dev/null || true

cd "$PROJECT_DIR/renren-fast-vue"
if [ ! -d "node_modules" ]; then
    echo "  → 安装依赖..."
    npm install --legacy-peer-deps -q
fi
nohup npm run dev > /tmp/vue.log 2>&1 &
sleep 5
echo "✓ 前端服务已启动 (http://localhost:8081)"

# 4. 输出访问信息
echo ""
echo "=========================================="
echo "服务已启动！"
echo "=========================================="
echo ""
echo "访问地址:"
echo "  前端:   http://localhost:8081"
echo "  后端:   http://localhost:8080/renren-fast"
echo "  Swagger: http://localhost:8080/renren-fast/swagger-ui.html"
echo ""
echo "测试账号:"
echo "  用户名: admin"
echo "  密码:   admin"
echo ""
echo "日志文件:"
echo "  后端: /tmp/renren-fast.log"
echo "  前端: /tmp/vue.log"
echo ""
echo "停止服务:"
echo "  docker-compose down"
echo "  pkill -f 'java.*renren-fast'"
echo "  pkill -f 'npm run dev'"
echo ""
