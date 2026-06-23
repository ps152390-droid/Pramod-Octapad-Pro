# Use Ubuntu as base and install Android SDK
FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH=${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/platform-tools:${ANDROID_SDK_ROOT}/build-tools/34.0.0:${PATH}
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64

# Install dependencies
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk-headless \
    wget \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# Create Android SDK directory
RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools

# Download and install Android SDK command-line tools
RUN cd /tmp && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip && \
    unzip -q commandlinetools-linux-11076708_latest.zip && \
    mv cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/latest && \
    rm commandlinetools-linux-11076708_latest.zip

# Accept licenses and install SDK components
RUN mkdir -p ${ANDROID_SDK_ROOT}/licenses && \
    echo "24333f8a63b6825ea9c5514f83c2829b004d1fee" > ${ANDROID_SDK_ROOT}/licenses/android-sdk-license && \
    yes | sdkmanager "platforms;android-34" "platforms;android-30" "build-tools;34.0.0"

WORKDIR /app

# Copy the project
COPY octapad-app /app/

# Run gradle build
RUN gradle clean assembleDebug -Dorg.gradle.daemon=false

# Output the APK
RUN mkdir -p /app/output && \
    cp app/build/outputs/apk/debug/app-debug.apk /app/output/Octapad-Pro-Debug.apk || true
