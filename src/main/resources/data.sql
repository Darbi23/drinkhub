INSERT INTO app_user (username, password, email, roles) VALUES
('admin', '$2a$10$QFNM01X3j6f7dGUpkN8C1uJynKshr.UzYwOyfeor7RBb9v9YNg6fq', 'admin@example.com', 'ADMIN');

-- Insert products
INSERT INTO products (name, category, description, price) VALUES
('Bowmore', 'Whiskey', 'A fine whiskey from Islay, Scotland', 65.0),
('Glenfiddich', 'Whiskey', 'A smooth single malt scotch', 45.9),
('Absolut', 'Vodka', 'Premium Swedish vodka', 20.0);

-- Insert orders
INSERT INTO orders (user_id, total_amount, status) VALUES
(1, 110.9, 'COMPLETED'),
(2, 45.9, 'PENDING');

-- Insert into order_products
INSERT INTO order_products (order_id, product_id) VALUES
(1, 1),
(1, 2),
(2, 2);

-- Insert carts
INSERT INTO carts (user_id) VALUES
(1),
(2);

