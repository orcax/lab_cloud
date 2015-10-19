SELECT concat('DROP TABLE IF EXISTS ', table_name, ';')
FROM information_schema.tables
WHERE table_schema = 'labcloud';

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS class;
DROP TABLE IF EXISTS class_reservation;
DROP TABLE IF EXISTS course;
DROP TABLE IF EXISTS course_experiment;
DROP TABLE IF EXISTS experiment;
DROP TABLE IF EXISTS lab;
DROP TABLE IF EXISTS lab_experiment;
DROP TABLE IF EXISTS lab_plan;
DROP TABLE IF EXISTS semester;
DROP TABLE IF EXISTS student_class;
DROP TABLE IF EXISTS student_labplan;
DROP TABLE IF EXISTS student_labrecord;
DROP TABLE IF EXISTS student_reservation;
SET FOREIGN_KEY_CHECKS = 1;
