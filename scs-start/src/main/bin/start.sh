#!/bin/bash

# 设置应用程序名称
APP_NAME="scs-start"

# 获取脚本所在目录的绝对路径
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PARENT_DIR="$(dirname "$SCRIPT_DIR")"


# 查找 lib 目录下的最新 JAR 文件
JAR_FILE=$(find "${PARENT_DIR}/lib" -name "${APP_NAME}-*.jar" | sort -r | head -n 1)

# 检查是否找到了 JAR 文件
if [ -z "$JAR_FILE" ]; then
  echo "No JAR file found in ${PARENT_DIR}/lib directory."
  exit 1
else
  echo "JAR_FILE: $JAR_FILE"
fi

# 检查是否提供了配置文件参数，如果没有则使用默认的配置文件路径
if [ -z "$1" ]; then
  CONFIG_PATH="${PARENT_DIR}/config/application.yml"
  echo "Using default config path: $CONFIG_PATH"
else
  CONFIG_PATH="$1"
  echo "Using provided config path: $CONFIG_PATH"
fi

# 设置日志文件路径
LOG_PATH="${PARENT_DIR}/logs/${APP_NAME}.log"

# 创建日志目录（如果不存在）
mkdir -p "${PARENT_DIR}/logs"

# 构建 nohup 命令
NOHUP_COMMAND="nohup java \
  -Dspring.config.location=$CONFIG_PATH \
  --spring.profiles.active=dev \
  -jar \"$JAR_FILE\" \
  > $LOG_PATH 2>&1 &"

# 输出 nohup 命令
echo "Executing command: $NOHUP_COMMAND"

# 启动应用程序
eval $NOHUP_COMMAND

# 打印进程ID
echo "Started $APP_NAME with PID $!"