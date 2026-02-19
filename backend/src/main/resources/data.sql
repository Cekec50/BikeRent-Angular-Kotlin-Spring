INSERT INTO "user" (username, password, first_name, last_name, phone, email, is_admin) VALUES
('admin', 'admin', 'Djordje', 'Cekerevac', '1234567890', 'admin@gmail.com', TRUE),
('user1', 'user1', 'John', 'Doe', '0987654321', 'john@gmail.com', FALSE),
('user2', 'user2', 'Jane', 'Smith', '5551234567', 'jane@gmail.com', FALSE);

INSERT INTO parking (name, latitude, longitude) VALUES
('Vukov Spomenik Parking', 44.8055, 20.4772),
('Slavija Garage', 44.8033, 20.4661),
('Zeleni Venac', 44.8131, 20.4553),
('Kalemegdan Parking', 44.8236, 20.4528),
('Ada Ciganlija Parking', 44.7872, 20.4144);

INSERT INTO bike (type, price, status, location, latitude, longitude, nearest_parking_id, distance_to_nearest_parking) VALUES
('Electric Bike', 15.0, 1, 'Bulevar Kralja Aleksandra 56', 44.807656, 20.470389, 1, 587),
('Electric Bike', 20.0, 0, 'Kraljice Marija 41', 44.806174, 20.478485, 1, 125),
('Electric Bike', 25.0, 1, 'Dimitrija Tucovica 16', 44.806662, 20.483165, 1, 489),
('City Bike', 5.0, -1, 'Cvijiceva 97', 44.810297, 20.480313, 1, 588),
('Hybrid Bike', 18.0, 1, 'Krunska 3a', 44.808882, 20.465090, 2, 626),
('Kids Bike', 10.0, 1, 'Resavska 18', 44.808372, 20.466337, 2, 565);

INSERT INTO report (bike_id, description, photo_url) VALUES
(4, 'Flat tire', 'https://example.com/report1.jpg'),
(4, 'The tire is flat', 'https://example.com/report1.jpg'),
(2, 'Broken chain', 'https://example.com/report2.jpg'),
(1, 'Scratched frame', 'https://example.com/report3.jpg');

INSERT INTO history (user_id, bike_id, start_time, end_time, total_price, duration, photo_url) VALUES
(2, 1, '2023-10-27T10:00:00', '2023-10-27T12:00:00', 30.0, 120, 'https://example.com/photo1.jpg'),
(3, 3, '2023-10-28T14:00:00', '2023-10-28T16:00:00', 50.0, 120, 'https://example.com/photo2.jpg'),
(3, 2, '2023-10-29T09:00:00', '2023-10-29T11:00:00', 40.0, 120, 'https://example.com/photo3.jpg'),
(2, 5, '2023-11-01T15:00:00', '2023-11-01T16:30:00', 27.0, 90, 'https://example.com/photo4.jpg'),
(2, 6, '2023-11-03T10:00:00', '2023-11-03T11:00:00', 10.0, 60, 'https://example.com/photo5.jpg');
