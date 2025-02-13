-- 현물 시장 데이터
CREATE TABLE spot_market_data (
    id BIGSERIAL,
    symbol_id BIGINT REFERENCES symbols(id),
    exchange_id BIGINT REFERENCES exchanges(id),
    timestamp TIMESTAMPTZ NOT NULL,
    price DECIMAL(20,8) NOT NULL,
    volume DECIMAL(20,8) NOT NULL,
    open DECIMAL(20,8) NOT NULL,
    high DECIMAL(20,8) NOT NULL,
    low DECIMAL(20,8) NOT NULL,
    close DECIMAL(20,8) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id, timestamp)
);

-- TimescaleDB 하이퍼테이블로 변환
SELECT create_hypertable('spot_market_data', 'timestamp');

-- 인덱스 생성
CREATE INDEX idx_spot_market_data_symbol_timestamp ON spot_market_data (symbol_id, timestamp DESC);
CREATE INDEX idx_spot_market_data_exchange_timestamp ON spot_market_data (exchange_id, timestamp DESC); 