FROM mcr.microsoft.com/devcontainers/java:11

# Install OpenJFX 17 and headless dependencies
RUN apt-get update && apt-get install -y \
    openjfx=17* \
    libopenjfx-java=17* \
    openjfx-source=17* \
    libgl1-mesa-glx \
    libxrender1 \
    libxtst6 \
    libxi6 \
    xvfb \
    && rm -rf /var/lib/apt/lists/*

# Set environment variables for JavaFX headless mode
ENV JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:$PATH"
ENV DISPLAY=:99
ENV MAVEN_OPTS="-Djava.awt.headless=true -Dprism.order=sw -Dglass.platform=gtk"

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

USER vscode
