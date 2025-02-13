---
title: "금융 데이터 분석 및 AI 연동 솔루션 - API 명세서"
date: "2025-02-12"
category: "projects"
description: "금융 데이터 분석 시스템의 RESTful API 및 WebSocket 명세"
tags:
  [
    "api",
    "rest",
    "websocket",
    "swagger",
    "endpoint",
    "authentication",
    "documentation",
  ]
thumbnail: "/images/cryptocurrency.jpg"
---

# 금융 데이터 분석 시스템 API 명세서

## 🔐 인증 및 보안

### 1. 인증 방식

#### 1.1 JWT 인증

```json
{
  "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### 1.2 API Key 인증

```json
{
  "X-API-Key": "your-api-key-here"
}
```

### 2. 보안 정책

- Rate Limiting: 1000 requests/minute
- IP Whitelisting 지원
- SSL/TLS 필수

## 📡 RESTful API 엔드포인트

### 1. 시장 데이터 API

#### 1.1 실시간 시세 조회

```http
GET /api/v1/market/price/{symbol}

Response 200:
{
  "symbol": "BTC-USDT",
  "price": "65432.10",
  "timestamp": "2024-03-21T09:30:00Z",
  "volume": "123.45",
  "exchange": "binance"
}
```

#### 1.2 과거 데이터 조회

```http
GET /api/v1/market/history/{symbol}
Parameters:
  - interval: "1m" | "5m" | "1h" | "1d"
  - start: ISO8601
  - end: ISO8601

Response 200:
{
  "symbol": "BTC-USDT",
  "data": [
    {
      "timestamp": "2024-03-21T09:00:00Z",
      "open": "65000.00",
      "high": "65100.00",
      "low": "64900.00",
      "close": "65050.00",
      "volume": "234.56"
    }
  ]
}
```

### 2. 포트폴리오 API

#### 2.1 포지션 조회

```http
GET /api/v1/portfolio/positions

Response 200:
{
  "positions": [
    {
      "symbol": "BTC-USDT",
      "size": "1.5",
      "entry_price": "64000.00",
      "current_price": "65000.00",
      "pnl": "1500.00",
      "timestamp": "2024-03-21T09:30:00Z"
    }
  ]
}
```

#### 2.2 포지션 생성

```http
POST /api/v1/portfolio/positions
Request:
{
  "symbol": "BTC-USDT",
  "size": "1.5",
  "type": "long",
  "leverage": "1"
}

Response 201:
{
  "position_id": "pos_123456",
  "status": "success",
  "timestamp": "2024-03-21T09:30:00Z"
}
```

### 3. AI 분석 API

#### 3.1 가격 예측

```http
GET /api/v1/analysis/prediction/{symbol}
Parameters:
  - timeframe: "1h" | "4h" | "1d"
  - model: "lstm" | "ensemble"

Response 200:
{
  "symbol": "BTC-USDT",
  "prediction": "66000.00",
  "confidence": 0.85,
  "timeframe": "1h",
  "model": "ensemble",
  "timestamp": "2024-03-21T09:30:00Z"
}
```

#### 3.2 리스크 분석

```http
GET /api/v1/analysis/risk/{symbol}

Response 200:
{
  "symbol": "BTC-USDT",
  "var_95": "3200.00",
  "expected_shortfall": "3800.00",
  "volatility": "0.45",
  "timestamp": "2024-03-21T09:30:00Z"
}
```

## 🔄 WebSocket API

### 1. 시장 데이터 스트림

#### 1.1 실시간 시세 구독

```javascript
// 연결
ws://api.example.com/ws/market

// 구독 메시지
{
  "action": "subscribe",
  "channel": "market",
  "symbols": ["BTC-USDT", "ETH-USDT"]
}

// 수신 데이터
{
  "symbol": "BTC-USDT",
  "price": "65432.10",
  "timestamp": "2024-03-21T09:30:00.123Z",
  "volume": "1.23"
}
```

#### 1.2 주문북 구독

```javascript
// 구독 메시지
{
  "action": "subscribe",
  "channel": "orderbook",
  "symbol": "BTC-USDT",
  "depth": 10
}

// 수신 데이터
{
  "symbol": "BTC-USDT",
  "bids": [
    ["65000.00", "1.234"],
    ["64999.00", "2.345"]
  ],
  "asks": [
    ["65001.00", "1.234"],
    ["65002.00", "2.345"]
  ],
  "timestamp": "2024-03-21T09:30:00.123Z"
}
```

## 📊 에러 코드

### 1. HTTP 상태 코드

```json
{
  "400": "잘못된 요청",
  "401": "인증 실패",
  "403": "권한 없음",
  "404": "리소스 없음",
  "429": "요청 한도 초과",
  "500": "서버 내부 오류"
}
```

### 2. 비즈니스 에러 코드

```json
{
  "MARKET_001": "유효하지 않은 심볼",
  "MARKET_002": "거래소 연결 실패",
  "PORT_001": "잔액 부족",
  "PORT_002": "포지션 한도 초과",
  "AI_001": "모델 로딩 실패",
  "AI_002": "예측 실패"
}
```

## 📝 API 사용 예시

### 1. REST API 호출

```python
import requests

# 시장 데이터 조회
def get_market_price(symbol):
    url = f"https://api.example.com/v1/market/price/{symbol}"
    headers = {
        "Authorization": f"Bearer {token}",
        "X-API-Key": "your-api-key"
    }
    response = requests.get(url, headers=headers)
    return response.json()

# 포지션 생성
def create_position(symbol, size):
    url = "https://api.example.com/v1/portfolio/positions"
    data = {
        "symbol": symbol,
        "size": size,
        "type": "long"
    }
    response = requests.post(url, json=data, headers=headers)
    return response.json()
```

### 2. WebSocket 연결

```javascript
const ws = new WebSocket("ws://api.example.com/ws/market");

ws.onopen = () => {
  ws.send(
    JSON.stringify({
      action: "subscribe",
      channel: "market",
      symbols: ["BTC-USDT"],
    })
  );
};

ws.onmessage = (event) => {
  const data = JSON.parse(event.data);
  console.log(`${data.symbol}: ${data.price}`);
};
```

이 문서는 금융 데이터 분석 시스템의 API 명세를 제공합니다. API는 지속적으로 업데이트되며, 최신 버전은 Swagger UI를 통해 확인할 수 있습니다. 🚀
