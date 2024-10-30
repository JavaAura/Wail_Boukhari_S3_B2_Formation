-- Insert Classrooms
INSERT INTO classrooms (name, room_number, current_capacity, max_capacity, created_at) VALUES
('Java Development Lab', 'JDL-101', 0, 25, CURRENT_TIMESTAMP),
('Web Development Studio', 'WDS-201', 0, 30, CURRENT_TIMESTAMP),
('Data Science Room', 'DSR-301', 0, 20, CURRENT_TIMESTAMP),
('Cloud Computing Lab', 'CCL-401', 0, 25, CURRENT_TIMESTAMP),
('Mobile Development Lab', 'MDL-501', 0, 20, CURRENT_TIMESTAMP);

-- Insert Trainers
INSERT INTO trainers (last_name, first_name, email, specialty, classroom_id, created_at) VALUES
('Smith', 'John', 'john.smith@formation.com', 'Java Development', 1, CURRENT_TIMESTAMP),
('Johnson', 'Emily', 'emily.johnson@formation.com', 'Web Development', 2, CURRENT_TIMESTAMP),
('Williams', 'Michael', 'michael.williams@formation.com', 'Data Science', 3, CURRENT_TIMESTAMP),
('Brown', 'Sarah', 'sarah.brown@formation.com', 'Cloud Computing', 4, CURRENT_TIMESTAMP),
('Davis', 'Robert', 'robert.davis@formation.com', 'Mobile Development', 5, CURRENT_TIMESTAMP);

-- Insert Courses
INSERT INTO courses (
    title, 
    level, 
    prerequisites, 
    min_capacity, 
    max_capacity, 
    current_capacity,
    start_date, 
    end_date, 
    status, 
    trainer_id, 
    created_at
) VALUES
('Java Spring Boot Development', 'Advanced', 'Java Core, REST APIs', 5, 20, 0, 
 CURRENT_DATE + INTERVAL '30 days', CURRENT_DATE + INTERVAL '90 days', 'PLANNED', 1, CURRENT_TIMESTAMP),

('Full Stack JavaScript', 'Intermediate', 'HTML, CSS, JavaScript Basics', 5, 25, 0,
 CURRENT_DATE + INTERVAL '15 days', CURRENT_DATE + INTERVAL '75 days', 'PLANNED', 2, CURRENT_TIMESTAMP),

('Python for Data Science', 'Intermediate', 'Python Basics, Statistics', 5, 15, 0,
 CURRENT_DATE, CURRENT_DATE + INTERVAL '60 days', 'IN_PROGRESS', 3, CURRENT_TIMESTAMP),

('AWS Cloud Architecture', 'Advanced', 'Cloud Basics, Networking', 5, 20, 0,
 CURRENT_DATE + INTERVAL '45 days', CURRENT_DATE + INTERVAL '105 days', 'PLANNED', 4, CURRENT_TIMESTAMP),

('Android App Development', 'Intermediate', 'Java Core, XML', 5, 15, 0,
 CURRENT_DATE + INTERVAL '20 days', CURRENT_DATE + INTERVAL '80 days', 'PLANNED', 5, CURRENT_TIMESTAMP);

-- Insert Students
INSERT INTO students (
    last_name, 
    first_name, 
    email, 
    level, 
    course_id, 
    classroom_id, 
    registration_date
) VALUES
('Anderson', 'Thomas', 'thomas.anderson@email.com', 'Advanced', 1, 1, CURRENT_TIMESTAMP),
('Wilson', 'Emma', 'emma.wilson@email.com', 'Intermediate', 2, 2, CURRENT_TIMESTAMP),
('Martinez', 'Carlos', 'carlos.martinez@email.com', 'Intermediate', 3, 3, CURRENT_TIMESTAMP),
('Taylor', 'Sophie', 'sophie.taylor@email.com', 'Advanced', 4, 4, CURRENT_TIMESTAMP),
('Lee', 'David', 'david.lee@email.com', 'Intermediate', 5, 5, CURRENT_TIMESTAMP),
('Garcia', 'Maria', 'maria.garcia@email.com', 'Advanced', 1, 1, CURRENT_TIMESTAMP),
('Miller', 'James', 'james.miller@email.com', 'Intermediate', 2, 2, CURRENT_TIMESTAMP),
('Chen', 'Lisa', 'lisa.chen@email.com', 'Intermediate', 3, 3, CURRENT_TIMESTAMP),
('Kumar', 'Raj', 'raj.kumar@email.com', 'Advanced', 4, 4, CURRENT_TIMESTAMP),
('Patel', 'Priya', 'priya.patel@email.com', 'Intermediate', 5, 5, CURRENT_TIMESTAMP);

-- Update current capacities for courses
UPDATE courses 
SET current_capacity = (
    SELECT COUNT(*) 
    FROM students 
    WHERE students.course_id = courses.id
);

-- Update current capacities for classrooms
UPDATE classrooms 
SET current_capacity = (
    SELECT COUNT(*) 
    FROM students 
    WHERE students.classroom_id = classrooms.id
);