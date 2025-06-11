# ARCRacket

**ARCRacket**은 **아르카디아(Arcadia)** 스토어 마인크래프트 플러그인에서 사용하기 위해 개발된 **Netty 기반 커스텀 패킷 통신 라이브러리**입니다. `ARCRacket`은 `Racket`이라는 이름을 공유하지만, **[Racket 언어](https://racket-lang.org)** 또는 그 관련 라이브러리와는 **전혀 관계가 없습니다**.

---

## 🎯 목적

- **모드(Mod)**와 **플러그인(Plugin)** 간의 **경량화된 네트워크 통신**을 구현
- Minecraft 환경에서 **Netty-all 기반 단방향/양방향 패킷 처리** 지원
- **Mixin 없이** Fabric API 의존을 최소화한 독립적인 구조

---

## 📦 프로젝트 구조

ARCRacket은 **멀티 모듈 구조**로 구성되어 있습니다:

```
arcracket/
├── core # 공통 패킷 처리 및 Netty 통신 로직
├── fabric # Fabric 클라이언트/서버 전용 구현
└── bukkit # Bukkit 서버 전용 구현
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

## 🔧 예시 (초기화 코드)

```kotlin
// 서버 측 초기화
RacketServerBootstrap()
    .register(MyPacket::class, MyPacketHandler())
    .bind(port = 8080)
```

```kotlin
// 클라이언트 측 초기화
RacketClientBootstrap()
    .register(MyPacket::class, MyPacketHandler())
    .connect(host = "localhost", port = 8080)

```
