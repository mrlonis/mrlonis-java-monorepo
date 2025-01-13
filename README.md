# mrlonis-java-monorepo

This repo contains all my personal Java related projects under one repo

## Table of Contents

- [mrlonis-java-monorepo](#mrlonis-java-monorepo)
  - [Table of Contents](#table-of-contents)
  - [Prerequisites](#prerequisites)
    - [Gradle](#gradle)
      - [Windows](#windows)
      - [Mac-OS / WSL](#mac-os--wsl)
    - [JDK](#jdk)
      - [Mac-OS](#mac-os)
      - [WSL - Ubuntu](#wsl---ubuntu)
  - [Running the Project](#running-the-project)

## Prerequisites

### Gradle

#### Windows

```cmd
choco install gradle
```

#### Mac-OS / WSL

```shell
brew install gradle
```

### JDK

#### Mac-OS

```shell
brew install oracle-jdk@17
```

#### WSL - Ubuntu

```shell
apt-get install gnupg2 software-properties-common
add-apt-repository ppa:linuxuprising/java
apt-get install oracle-java17-installer oracle-java17-set-default
```

## Running the Project

To run the project, run the following command:

```shell
./gradlew build
```

If on Windows, run the following command:

```cmd
gradlew.bat build
```

