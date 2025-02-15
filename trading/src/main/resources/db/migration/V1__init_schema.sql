CREATE EXTENSION IF NOT EXISTS timescaledb;

CREATE TABLE exchanges (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (name, type)
);

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

CREATE TABLE option_series (
    id BIGSERIAL PRIMARY KEY,
    underlying_asset VARCHAR(10),
    expiry_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE option_instruments (
    id BIGSERIAL PRIMARY KEY,
    series_id BIGINT NOT NULL REFERENCES option_series(id),
    strike_price NUMERIC(30,8) NOT NULL,
    option_type VARCHAR(4) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE option_market_data (
    id BIGSERIAL,
    instrument_id BIGINT NOT NULL REFERENCES option_instruments(id),
    price NUMERIC(30,8) NOT NULL,
    underlying_price NUMERIC(30,8),
    implied_volatility NUMERIC(10,4),
    delta NUMERIC(10,4),
    gamma NUMERIC(10,4),
    theta NUMERIC(10,4),
    vega NUMERIC(10,4),
    rho NUMERIC(10,4),
    volume NUMERIC(30,8),
    open_interest NUMERIC(30,8),
    bid_price NUMERIC(30,8),
    ask_price NUMERIC(30,8),
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id, timestamp)
);

-- TimescaleDB 하이퍼테이블 설정을 테이블 생성 후에 실행
SELECT create_hypertable('spot_market_data', 'timestamp');
SELECT create_hypertable('futures_market_data', 'timestamp');
SELECT create_hypertable('option_market_data', 'timestamp');
SELECT create_hypertable('volume_aggregations', 'start_time');

-- 인덱스 생성
CREATE INDEX idx_option_series_expiry ON option_series(expiry_date);
CREATE INDEX idx_option_instruments_series_strike ON option_instruments(series_id, strike_price);
CREATE INDEX idx_option_instruments_type ON option_instruments(option_type);
CREATE INDEX idx_option_market_data_instrument_time ON option_market_data(instrument_id, timestamp DESC);
CREATE INDEX idx_option_market_data_implied_vol ON option_market_data(implied_volatility) WHERE implied_volatility IS NOT NULL;
CREATE INDEX idx_option_market_data_delta ON option_market_data(delta) WHERE delta IS NOT NULL;
CREATE INDEX idx_option_market_data_underlying ON option_market_data(underlying_price, timestamp DESC) WHERE underlying_price IS NOT NULL;

-- 마지막에 exchange 데이터 삽입
INSERT INTO exchanges (name, type, status) VALUES
('binance', 'SPOT', 'ACTIVE'),
('binance', 'FUTURES', 'ACTIVE'),
('deribit', 'OPTION', 'ACTIVE');