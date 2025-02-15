ALTER TABLE options_market_data
ADD COLUMN underlying_price DECIMAL,
ADD COLUMN open_interest DECIMAL,
ADD COLUMN bid_price DECIMAL,
ADD COLUMN ask_price DECIMAL,
ADD COLUMN rho DECIMAL; 