#!/bin/bash
set -euo pipefail

# Thin layout: APP_HOME/{bin,config,lib,logs}
# config is on the classpath so i18n/ and logback-spring.xml resolve;
# application*.yml are also loaded from config via spring.config.additional-location.

APP_NAME="scs-start"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
APP_HOME="$(cd "${SCRIPT_DIR}/.." && pwd)"
LIB_DIR="${APP_HOME}/lib"
CONFIG_DIR="${APP_HOME}/config"
LOG_DIR="${APP_HOME}/logs"
PID_FILE="${LOG_DIR}/${APP_NAME}.pid"
OUT_FILE="${LOG_DIR}/${APP_NAME}.out"

mkdir -p "${LOG_DIR}"

if [[ ! -d "${LIB_DIR}" ]]; then
  echo "lib directory not found: ${LIB_DIR}" >&2
  exit 1
fi
if [[ ! -d "${CONFIG_DIR}" ]]; then
  echo "config directory not found: ${CONFIG_DIR}" >&2
  exit 1
fi

# Prefer plain module jar; ignore sources/javadoc if present
MAIN_JAR="$(ls -1 "${LIB_DIR}/${APP_NAME}"-*.jar 2>/dev/null | grep -Ev 'sources|javadoc' | sort -r | head -n 1 || true)"
if [[ -z "${MAIN_JAR}" ]]; then
  echo "No ${APP_NAME}-*.jar found in ${LIB_DIR}" >&2
  exit 1
fi

if [[ -f "${PID_FILE}" ]]; then
  OLD_PID="$(cat "${PID_FILE}")"
  if kill -0 "${OLD_PID}" 2>/dev/null; then
    echo "${APP_NAME} already running with PID ${OLD_PID}" >&2
    exit 1
  fi
  rm -f "${PID_FILE}"
fi

# Profile: SPRING_PROFILES_ACTIVE > first CLI arg > default dev
# Optional second arg kept for backward compat as extra spring.config location (unused if empty)
PROFILE="${SPRING_PROFILES_ACTIVE:-${1:-dev}}"

# -cp (not -jar): java -jar ignores CLASSPATH; config/ must be on classpath for i18n + logback
CLASSPATH="${CONFIG_DIR}:${LIB_DIR}/*"

JAVA_OPTS="${JAVA_OPTS:-}"
LOGGING_CONFIG="${LOGGING_CONFIG:-file:${CONFIG_DIR}/logback-spring.xml}"

echo "APP_HOME=${APP_HOME}"
echo "MAIN_JAR=${MAIN_JAR}"
echo "PROFILE=${PROFILE}"
echo "CLASSPATH=${CLASSPATH}"

nohup java ${JAVA_OPTS} \
  -cp "${CLASSPATH}" \
  -Dspring.profiles.active="${PROFILE}" \
  -Dspring.config.additional-location="optional:file:${CONFIG_DIR}/" \
  -Dlogging.config="${LOGGING_CONFIG}" \
  com.scs.start.Application \
  > "${OUT_FILE}" 2>&1 &

echo $! > "${PID_FILE}"
echo "Started ${APP_NAME} with PID $(cat "${PID_FILE}") (log: ${OUT_FILE})"
