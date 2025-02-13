-- TimescaleDB 확장 활성화
CREATE EXTENSION IF NOT EXISTS timescaledb;

-- 거래소 정보
CREATE TABLE exchanges (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (name, type)  -- name과 type의 조합으로 유니크 제약조건 변경
);

-- 심볼 마스터
CREATE TABLE symbols (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(20) NOT NULL UNIQUE,
    base_asset VARCHAR(10) NOT NULL,
    quote_asset VARCHAR(10) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 초기 데이터 삽입
INSERT INTO exchanges (name, type, status) VALUES
    ('binance', 'SPOT', 'ACTIVE'),
    ('binance', 'FUTURES', 'ACTIVE');

INSERT INTO symbols (symbol, base_asset, quote_asset) VALUES
    ('BTCUSDT', 'BTC', 'USDT'),
    ('ETHUSDT', 'ETH', 'USDT'); 