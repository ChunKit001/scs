#!/bin/bash
set -euo pipefail

APP_NAME="scs-start"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_HOME="$(cd "${SCRIPT_DIR}/.." && pwd)"
LOG_DIR="${APP_HOME}/logs"
PID_FILE="${LOG_DIR}/${APP_NAME}.pid"
STOP_LOG="${LOG_DIR}/${APP_NAME}_stop.log"

mkdir -p "${LOG_DIR}"

log() {
  echo "$(date '+%Y-%m-%d %H:%M:%S') - $1" | tee -a "${STOP_LOG}"
}

if [[ ! -f "${PID_FILE}" ]]; then
  log "No PID file at ${PID_FILE}. Is ${APP_NAME} running?"
  exit 1
fi

PID="$(cat "${PID_FILE}")"
if ! kill -0 "${PID}" 2>/dev/null; then
  log "No running process for PID ${PID}. Removing stale PID file."
  rm -f "${PID_FILE}"
  exit 1
fi

log "Stopping ${APP_NAME} with PID ${PID}..."
kill "${PID}" || true

for _ in $(seq 1 30); do
  if ! kill -0 "${PID}" 2>/dev/null; then
    rm -f "${PID_FILE}"
    log "${APP_NAME} stopped successfully."
    exit 0
  fi
  sleep 1
done

log "Graceful stop timed out, sending SIGKILL..."
kill -9 "${PID}" 2>/dev/null || true
rm -f "${PID_FILE}"
log "${APP_NAME} was killed."
exit 0
