-- TimescaleDB 확장 활성화
CREATE EXTENSION IF NOT EXISTS timescaledb;

-- 시장 데이터 테이블
CREATE TABLE market_data (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(20) NOT NULL,
    exchange VARCHAR(20) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    price DECIMAL(20,8) NOT NULL,
    volume DECIMAL(20,8) NOT NULL,
    open DECIMAL(20,8) NOT NULL,
    high DECIMAL(20,8) NOT NULL,
    low DECIMAL(20,8) NOT NULL,
    close DECIMAL(20,8) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 시계열 하이퍼테이블로 변환
SELECT create_hypertable('market_data', 'timestamp');

-- 인덱스 생성
CREATE INDEX idx_market_data_symbol_timestamp ON market_data (symbol, timestamp DESC);
CREATE INDEX idx_market_data_exchange_timestamp ON market_data (exchange, timestamp DESC);

-- 옵션 데이터 테이블
CREATE TABLE option_data (
    id BIGSERIAL PRIMARY KEY,
    underlying VARCHAR(20) NOT NULL,
    strike_price DECIMAL(20,8) NOT NULL,
    expiry_date TIMESTAMPTZ NOT NULL,
    option_type VARCHAR(4) NOT NULL,
    price DECIMAL(20,8) NOT NULL,
    iv DECIMAL(10,4),
    delta DECIMAL(10,4),
    gamma DECIMAL(10,4),
    theta DECIMAL(10,4),
    vega DECIMAL(10,4),
    timestamp TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 시계열 하이퍼테이블로 변환
SELECT create_hypertable('option_data', 'timestamp');

-- 인덱스 생성
CREATE INDEX idx_option_data_underlying_expiry ON option_data (underlying, expiry_date);
CREATE INDEX idx_option_data_timestamp ON option_data (timestamp DESC);

-- 사용자 포트폴리오 테이블
CREATE TABLE portfolio (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID NOT NULL,
    symbol VARCHAR(20) NOT NULL,
    position_type VARCHAR(10) NOT NULL,
    position_size DECIMAL(20,8) NOT NULL,
    entry_price DECIMAL(20,8) NOT NULL,
    leverage DECIMAL(10,2) DEFAULT 1.0,
    timestamp TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- 인덱스 생성
CREATE INDEX idx_portfolio_user_timestamp ON portfolio (user_id, timestamp DESC); 