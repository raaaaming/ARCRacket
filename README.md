# ARCRacket

**ARCRacket**은 **아르카디아(Arcadia)** 스토어 마인크래프트 플러그인에서 사용하기 위해 개발된 **Netty 기반 커스텀 패킷 통신 라이브러리**입니다. `ARCRacket`은 `Racket`이라는 이름을 공유하지만, **[Racket 언어](https://racket-lang.org)** 또는 그 관련 라이브러리와는 **전혀 관계가 없습니다**.

[![Java CI with Gradle](https://github.com/raaaaming/ARCRacket/actions/workflows/gradle.yml/badge.svg?branch=main)](https://github.com/raaaaming/ARCRacket/actions/workflows/gradle.yml)
[![Gradle Package](https://github.com/raaaaming/ARCRacket/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/raaaaming/ARCRacket/actions/workflows/gradle-publish.yml)
---

## 🎯 목적

- **모드(Mod)**와 **플러그인(Plugin)** 간의 **경량화된 네트워크 통신**을 구현
- Minecraft 환경에서 **Netty-all 기반 단방향/양방향 패킷 처리** 지원
- **Mixin 없이** Fabric API 의존을 최소화한 독립적인 구조

---

## 📦 프로젝트 의존성 추가

```
repositories {
  maven {
    url = uri("https://maven.pkg.github.com/raaaaming/ARCRacket")
    credentials {
      username = System.getenv("USERNAME")
      password = System.getenv("TOKEN")
    }
  }
}

dependencies {
  implementation("kr.raming:core:2.1.0")
}
```

---

## 🔌 주요 기능

- ✅ 클라이언트 <-> 서버 간 **커스텀 패킷 처리**
- ✅ 패킷 ID 기반 자동 디스패칭
- ✅ **패킷 직렬화/역직렬화** 모듈화
- ✅ **핸들러 등록**, **버퍼 재사용**, **Netty 채널 관리**
- ✅ 공통 인터페이스를 통한 다양한 플랫폼 통합

---

## 🚫 주의

> `ARCRacket`은 [Racket 언어](https://racket-lang.org)와는 전혀 관련이 없습니다.  
> 단순히 명칭이 동일할 뿐, 구현 목적 및 기술 스택 모두 다릅니다.

---

## 🔧 예시

```kotlin
class MyPacket(override var message: String = "") : Packet()
class SecondPacket(override var message: String = "") : Packet()
class TestPacket(override var message: String = "") : Packet()
```

```kotlin
PacketRegistry
    .register(1) { MyPacket }
    .register(2) { SecondPacket }
```

```kotlin
// 서버 측 초기화
val server = RacketServer()
    .register(MyPacket::class) {
        //클라이언트 -> 서버 패킷 처리 로직
    }
    .register(SecondPacket::class) {
        //클라이언트 -> 서버 패킷 처리 로직
    }
    .port(port = 8080)
    .start()
```

```kotlin
// 클라이언트 측 초기화
val client = RacketClient()
    .register(MyPacket::class) {
        //서버 -> 클라이언트 패킷 처리 로직
    }
    .register(SecondPacket::class) {
        //서버 -> 클라이언트 패킷 처리 로직
    }
    .bind(host = "localhost", port = 8080)
    .start()
```

```kotlin
client.send(MyPacket("Hi! 1"))
client.send(SecondPacket("Hi! 2"))
client.send(TestPacket("Hi! 3")) //전송 불가
```
