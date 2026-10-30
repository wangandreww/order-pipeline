-- Starting stock, inserted on startup. ON CONFLICT keeps restarts from resetting stock.
INSERT INTO products (id, stock) VALUES
	('ABC', 10),
	('XYZ', 5)
ON CONFLICT (id) DO NOTHING;
