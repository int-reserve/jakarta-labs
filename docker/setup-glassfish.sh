#!/usr/bin/env bash
set -e

ASADMIN="${GLASSFISH_HOME}/bin/asadmin"
DOMAIN=domain1
LOG="${GLASSFISH_HOME}/glassfish/domains/${DOMAIN}/logs/server.log"

echo "[setup] Starting GlassFish domain '${DOMAIN}'..."
"${ASADMIN}" start-domain "${DOMAIN}"

echo "[setup] Waiting for GlassFish admin port (4848)..."
until curl -sf "http://localhost:4848/management/domain" > /dev/null 2>&1; do
  sleep 3
done
echo "[setup] GlassFish is ready."

echo "[setup] Deploying application..."
"${ASADMIN}" deploy --name jakarta-labs --contextroot jakarta-labs /tmp/app.war
echo "[setup] Deployment complete."
echo "[setup] Application: http://localhost:8080/jakarta-labs/sessions"
echo "[setup] Admin panel: http://localhost:8080/jakarta-labs/admin"

exec tail -f "${LOG}"
