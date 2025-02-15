-- TimescaleDB 확장 추가 (맨 위에 추가)
CREATE EXTENSION IF NOT EXISTS timescaledb;

-- exchanges 테이블 생성
CREATE TABLE exchanges (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (name, type)
);

-- symbols 테이블 생성
CREATE TABLE symbols (
    id BIGSERIAL PRIMARY KEY,
    exchange_id BIGINT NOT NULL REFERENCES exchanges(id),
    exchange_symbol VARCHAR(255) NOT NULL,
    base_asset VARCHAR(255) NOT NULL,
    quote_asset VARCHAR(255) NOT NULL,
    instrument_type VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- spot_market_data 테이블 생성
CREATE TABLE spot_market_data (
    id BIGSERIAL,
    symbol_id BIGINT NOT NULL REFERENCES symbols(id),
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    price NUMERIC(30, 8) NOT NULL,
    volume NUMERIC(30, 8),
    open NUMERIC(30, 8),
    high NUMERIC(30, 8),
    low NUMERIC(30, 8),
    close NUMERIC(30, 8),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id, timestamp)
);

-- futures_market_data 테이블 생성
CREATE TABLE futures_market_data (
    id BIGSERIAL,
    symbol_id BIGINT NOT NULL REFERENCES symbols(id),
    exchange_id BIGINT NOT NULL REFERENCES exchanges(id),
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    price NUMERIC(30, 8) NOT NULL,
    volume NUMERIC(30, 8),
    open_interest NUMERIC(30, 8),
    funding_rate NUMERIC(30, 8),
    mark_price NUMERIC(30, 8),
    index_price NUMERIC(30, 8),
    open NUMERIC(30, 8),
    high NUMERIC(30, 8),
    low NUMERIC(30, 8),
    close NUMERIC(30, 8),
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id, timestamp)
);

-- options_market_data 테이블 생성
CREATE TABLE options_market_data (
    id BIGSERIAL,
    symbol_id BIGINT NOT NULL REFERENCES symbols(id),
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    price NUMERIC(30, 8) NOT NULL,
    volume NUMERIC(30, 8),
    implied_volatility NUMERIC(30, 8),
    delta NUMERIC(30, 8),
    gamma NUMERIC(30, 8),
    theta NUMERIC(30, 8),
    vega NUMERIC(30, 8),
    strike_price NUMERIC(30, 8) NOT NULL,
    option_type VARCHAR(255) NOT NULL,
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id, timestamp)
);

-- volume_aggregations 테이블 생성
CREATE TABLE volume_aggregations (
    id BIGSERIAL,
    symbol_id BIGINT NOT NULL REFERENCES symbols(id),
    instrument_type VARCHAR(255) NOT NULL,
    period_type VARCHAR(255) NOT NULL,
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    volume NUMERIC(30, 8) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id, start_time)
);

-- TimescaleDB 하이퍼테이블 생성
SELECT create_hypertable('spot_market_data', 'timestamp');
SELECT create_hypertable('futures_market_data', 'timestamp');
SELECT create_hypertable('options_market_data', 'timestamp');
SELECT create_hypertable('volume_aggregations', 'start_time');

-- spot_market_data 인덱스 추가
CREATE INDEX idx_spot_market_data_symbol_timestamp ON spot_market_data(symbol_id, timestamp DESC);

-- futures_market_data 인덱스 추가
CREATE INDEX idx_futures_market_data_symbol_timestamp ON futures_market_data(symbol_id, timestamp DESC);
CREATE INDEX idx_futures_market_data_exchange_timestamp ON futures_market_data(exchange_id, timestamp DESC);

-- options_market_data 인덱스 추가
CREATE INDEX idx_options_market_data_symbol_timestamp ON options_market_data(symbol_id, timestamp DESC);

-- volume_aggregations 인덱스 추가
CREATE INDEX idx_volume_aggregations_symbol_start_time ON volume_aggregations(symbol_id, start_time DESC); 

INSERT INTO exchanges (name, type, status) VALUES
('binance', 'SPOT', 'ACTIVE'),
('binance', 'FUTURES', 'ACTIVE'),
('deribit', 'OPTIONS', 'ACTIVE'); 