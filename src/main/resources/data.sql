INSERT INTO app_user (username, password, email, roles) VALUES
('admin', '$2a$10$QFNM01X3j6f7dGUpkN8C1uJynKshr.UzYwOyfeor7RBb9v9YNg6fq', 'admin@example.com', 'ADMIN');

INSERT INTO product (name, description, price, category) VALUES
('Whiskey A', 'A fine whiskey', 50.0, 'Whiskey'),
('Beer B', 'A refreshing beer', 5.0, 'Beer'),
('Wine C', 'A delightful wine', 20.0, 'Wine');

INSERT INTO review (user_id, product_id, rating, comment) VALUES
(1, 1, 5, 'Excellent whiskey!'),
(2, 2, 4, 'Good beer.');

INSERT INTO customer_order (user_id, product_list, total_amount, order_date, status) VALUES
(1, '1,2', 55.0, CURRENT_TIMESTAMP, 'COMPLETED');
