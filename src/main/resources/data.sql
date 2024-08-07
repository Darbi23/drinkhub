INSERT INTO app_user (username, password, email, roles) VALUES
('john_doe', 'password123', 'john@example.com', 'USER'),
('admin', 'admin123', 'admin@example.com', 'ADMIN');

INSERT INTO product (name, description, price, category) VALUES
('Whiskey A', 'A fine whiskey', 50.0, 'Whiskey'),
('Beer B', 'A refreshing beer', 5.0, 'Beer'),
('Wine C', 'A delightful wine', 20.0, 'Wine');

INSERT INTO review (user_id, product_id, rating, comment) VALUES
(1, 1, 5, 'Excellent whiskey!'),
(2, 2, 4, 'Good beer.');

INSERT INTO customer_order (user_id, product_list, total_amount, order_date, status) VALUES
(1, '1,2', 55.0, CURRENT_TIMESTAMP, 'COMPLETED');
