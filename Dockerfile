# Stage 1 - Build WAR with Maven + JDK 21
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

# Cache dependency layer separately from source
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

# Extract the PostgreSQL JDBC driver from the local Maven repo so we can
# drop it into GlassFish's lib directory in the runtime stage.
RUN find /root/.m2 -name "postgresql-*.jar" \
        ! -name "*sources*" ! -name "*javadoc*" \
        -exec cp {} /build/postgresql.jar \;

# ═══════════════════════════════════════════════════════════════
# Stage 2 — GlassFish 7.1.0 runtime (JDK 21)
# ═══════════════════════════════════════════════════════════════
FROM eclipse-temurin:21-jdk AS runtime

ENV GLASSFISH_HOME=/opt/glassfish7
ENV PATH="${GLASSFISH_HOME}/bin:${PATH}"

# ── Install utilities ────────────────────────────────────────────
RUN apt-get update -qq && \
    apt-get install -y --no-install-recommends curl unzip dos2unix && \
    rm -rf /var/lib/apt/lists/*

# ── Copy GlassFish 7.1.0 from the local tools/ directory ────────
# (If you prefer to download it, replace these two lines with:
#   RUN wget -q https://download.eclipse.org/ee4j/glassfish/glassfish-7.1.0.zip \
#       -O /tmp/gf.zip && unzip -q /tmp/gf.zip -d /opt && rm /tmp/gf.zip )
COPY tools/glassfish7 ${GLASSFISH_HOME}

# Convert ALL Windows CRLF → LF across the entire GlassFish tree.
# dos2unix auto-skips true binary files (detects NUL bytes), so .jar/.class/.so
# are safe to include in the find — they will simply be ignored.
RUN find "${GLASSFISH_HOME}" -type f \
        ! -name "*.jar" ! -name "*.class" ! -name "*.zip" \
        ! -name "*.war" ! -name "*.ear" ! -name "*.so"  \
        ! -name "*.dll" ! -name "*.exe" ! -name "*.png" \
        ! -name "*.gif" ! -name "*.jpg" ! -name "*.ico" \
        ! -name "*.jks" ! -name "*.p12" ! -name "*.ser" \
    | xargs dos2unix --quiet 2>/dev/null; true

# Replace the Windows JDK path baked into domain.xml with the container's JAVA_HOME.
RUN sed -i "s|java-home=\"[^\"]*\"|java-home=\"${JAVA_HOME}\"|" \
    "${GLASSFISH_HOME}/glassfish/domains/domain1/config/domain.xml"

# Make all GlassFish scripts executable (they sometimes lose the bit on Windows)
RUN chmod +x ${GLASSFISH_HOME}/bin/* 2>/dev/null || true && \
    chmod +x ${GLASSFISH_HOME}/glassfish/bin/* 2>/dev/null || true

# ── Drop the PostgreSQL JDBC driver into GlassFish lib ──────────
# The driver must be on the server classpath so the connection pool can use it.
COPY --from=build /build/postgresql.jar ${GLASSFISH_HOME}/glassfish/lib/postgresql.jar

# ── Copy the application WAR ─────────────────────────────────────
COPY --from=build /build/target/jakarta-labs-1.0-SNAPSHOT.war /tmp/app.war

# ── Copy the startup script ──────────────────────────────────────
COPY docker/setup-glassfish.sh /setup-glassfish.sh
# Strip Windows CRLF line endings so the shebang is parsed correctly on Linux.
RUN sed -i 's/\r$//' /setup-glassfish.sh && chmod +x /setup-glassfish.sh

EXPOSE 8080 4848

CMD ["/setup-glassfish.sh"]
