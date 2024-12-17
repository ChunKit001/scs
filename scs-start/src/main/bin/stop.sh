#!/bin/bash

# 设置应用程序名称
APP_NAME="scs-start"

# 获取脚本所在目录的绝对路径
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# 设置 PID 文件路径和日志文件路径
PID_FILE="${SCRIPT_DIR}/logs/${APP_NAME}.pid"
LOG_PATH="${SCRIPT_DIR}/logs/${APP_NAME}_stop.log"

# 创建日志目录（如果不存在）
mkdir -p "${SCRIPT_DIR}/logs"

# 函数：记录日志信息
log() {
  echo "$(date '+%Y-%m-%d %H:%M:%S') - $1" >> $LOG_PATH
}

# 检查并读取 PID 文件以获取进程 ID
if [ -f "$PID_FILE" ]; then
  PID=$(cat "$PID_FILE")

  # 检查进程是否存在
  if ps -p $PID > /dev/null; then
    log "Stopping $APP_NAME with PID $PID..."

    # 尝试发送 SIGTERM 信号给进程
    kill $PID

    # 等待进程结束，给予一定时间让应用优雅退出
    for i in {1..30}; do
      if ! ps -p $PID > /dev/null; then
        log "$APP_NAME stopped successfully."
        rm -f "$PID_FILE"  # 删除 PID 文件
        exit 0
      fi
      sleep 1
    done

    # 如果进程在等待时间内没有结束，则发送 SIGKILL 信号强制终止
    log "Failed to stop $APP_NAME gracefully, forcing shutdown..."
    kill -9 $PID
    rm -f "$PID_FILE"  # 删除 PID 文件
    log "$APP_NAME was killed."
  else
    log "No running process found for PID $PID. Removing stale PID file."
    rm -f "$PID_FILE"
  fi
else
  log "No PID file found. Is $APP_NAME running?"
fi

exit 1
