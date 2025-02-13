---
title: "금융 데이터 분석 및 AI 연동 솔루션 - 데이터베이스 설계"
date: "2025-02-12"
category: "projects"
description: "금융 데이터 분석 시스템의 데이터베이스 스키마 및 설계 문서"
tags:
  [
    "database",
    "schema",
    "postgresql",
    "timescaledb",
    "erd",
    "indexing",
    "partitioning",
  ]
thumbnail: "/images/cryptocurrency.jpg"
---

# 금융 데이터 분석 시스템 데이터베이스 설계

## 📊 데이터베이스 아키텍처

### 1. 데이터베이스 선정 이유

- **PostgreSQL**: 안정성과 확장성이 검증된 RDBMS
- **TimescaleDB**: 시계열 데이터 처리에 최적화된 확장
- **파티셔닝**: 대용량 데이터의 효율적 관리

### 2. 핵심 테이블 구조

#### 2.1 Market Data Tables

```sql
-- 실시간 시장 데이터
CREATE TABLE market_data (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(20) NOT NULL,
    exchange VARCHAR(20) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    price DECIMAL(20,8) NOT NULL,
    volume DECIMAL(20,8) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
) PARTITION BY RANGE (timestamp);

-- 옵션 데이터
CREATE TABLE option_data (
    id BIGSERIAL PRIMARY KEY,
    underlying VARCHAR(20) NOT NULL,
    strike_price DECIMAL(20,8) NOT NULL,
    expiry_date TIMESTAMPTZ NOT NULL,
    option_type VARCHAR(4) NOT NULL,
    iv DECIMAL(10,4),
    delta DECIMAL(10,4),
    gamma DECIMAL(10,4),
    theta DECIMAL(10,4),
    vega DECIMAL(10,4),
    timestamp TIMESTAMPTZ NOT NULL
) PARTITION BY RANGE (timestamp);
```

#### 2.2 분석 데이터 테이블

```sql
-- AI 모델 예측 결과
CREATE TABLE predictions (
    id BIGSERIAL PRIMARY KEY,
    model_id VARCHAR(50) NOT NULL,
    symbol VARCHAR(20) NOT NULL,
    prediction_type VARCHAR(20) NOT NULL,
    predicted_value DECIMAL(20,8) NOT NULL,
    confidence DECIMAL(5,4) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL
);

-- 포트폴리오 상태
CREATE TABLE portfolio_status (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    asset_type VARCHAR(20) NOT NULL,
    position_size DECIMAL(20,8) NOT NULL,
    entry_price DECIMAL(20,8) NOT NULL,
    current_price DECIMAL(20,8) NOT NULL,
    pnl DECIMAL(20,8) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL
);
```

### 3. 인덱싱 전략

#### 3.1 시장 데이터 인덱스

```sql
-- 시장 데이터 조회 최적화
CREATE INDEX idx_market_data_symbol_timestamp ON market_data (symbol, timestamp DESC);
CREATE INDEX idx_market_data_exchange_timestamp ON market_data (exchange, timestamp DESC);

-- 옵션 데이터 조회 최적화
CREATE INDEX idx_option_data_underlying_expiry ON option_data (underlying, expiry_date);
CREATE INDEX idx_option_data_strike_type ON option_data (strike_price, option_type);
```

#### 3.2 분석 데이터 인덱스

```sql
-- 예측 데이터 조회 최적화
CREATE INDEX idx_predictions_model_symbol ON predictions (model_id, symbol);
CREATE INDEX idx_predictions_timestamp ON predictions (timestamp DESC);

-- 포트폴리오 조회 최적화
CREATE INDEX idx_portfolio_user_timestamp ON portfolio_status (user_id, timestamp DESC);
```

### 4. 파티셔닝 전략

#### 4.1 시계열 데이터 파티셔닝

```sql
-- 월별 파티션 생성
CREATE TABLE market_data_y2024m01 PARTITION OF market_data
    FOR VALUES FROM ('2024-01-01') TO ('2024-02-01');
CREATE TABLE market_data_y2024m02 PARTITION OF market_data
    FOR VALUES FROM ('2024-02-01') TO ('2024-03-01');
```

#### 4.2 보관 정책

- 실시간 데이터: 최근 3개월
- 집계 데이터: 최근 1년
- 히스토리 데이터: S3 아카이브

## 📈 성능 최적화

### 1. 쿼리 최적화

#### 1.1 자주 사용되는 쿼리

```sql
-- 특정 기간의 OHLCV 데이터 조회
CREATE MATERIALIZED VIEW mv_ohlcv_1h AS
SELECT
    symbol,
    date_trunc('hour', timestamp) as timeframe,
    first(price) as open,
    max(price) as high,
    min(price) as low,
    last(price) as close,
    sum(volume) as volume
FROM market_data
GROUP BY symbol, timeframe;
```

#### 1.2 캐싱 전략

- Redis 캐싱 레이어 구현
- 실시간 데이터 메모리 캐싱
- 집계 데이터 캐시 갱신 주기 설정

### 2. 백업 전략

#### 2.1 정기 백업

- 일일 증분 백업
- 주간 전체 백업
- 월간 아카이브

#### 2.2 복구 계획

- Point-in-Time Recovery 설정
- 장애 복구 시나리오 문서화
- 복구 테스트 계획

## 🔄 데이터 흐름

### 1. 데이터 수집

```mermaid
graph LR
    A[거래소 API] --> B[데이터 수집기]
    B --> C[데이터 정제]
    C --> D[TimescaleDB]
    D --> E[분석 엔진]
```

### 2. 데이터 처리

- 실시간 스트림 처리
- 배치 처리
- 이상치 탐지 및 처리

## ⚡ 확장성 고려사항

### 1. 수평적 확장

- 읽기 전용 복제본 구성
- 샤딩 전략 수립
- 로드 밸런싱 설정

### 2. 수직적 확장

- 리소스 모니터링
- 성능 지표 설정
- 스케일 업 임계값 정의

이 문서는 금융 데이터 분석 시스템의 데이터베이스 설계 기준을 제공합니다. 시스템의 요구사항과 성능을 고려하여 지속적으로 업데이트될 예정입니다. 🚀
