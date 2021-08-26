INSERT  INTO roles_table(uuid,name) VALUES('ashjkoiytrev', 'ROLE_USER');
INSERT  INTO roles_table(uuid,name) VALUES('yuiklopmkjgf','ROLE_ADMIN');
INSERT INTO users_table(uuid, first_name, last_name, user_name, email, password,sex, dob,is_online, is_account_active , age) VALUES('AugagKAugabh', 'jdoe','john','doe','augastinesimwela@gmail.com', '$2a$10$wXc2IaDyBKNe/57DoiN8U.RHGm2Vz2.LdvsSbv2lLkwaYetDEcsp.', 'male', '1996-01-28', false,true,24);
INSERT INTO users_table(uuid, first_name, last_name, user_name, email, password,sex, dob, is_online, is_account_active,age) VALUES('NeljgKNelRKh', 'Nelson','Simwela','Nsimwela','bsc-49-16@cc.ac.mw', '$2a$10$wXc2IaDyBKNe/57DoiN8U.RHGm2Vz2.LdvsSbv2lLkwaYetDEcsp.', 'male','1996-01-28', false,true,24);
INSERT INTO experience_table(uuid,exp_name, began_on,years,months,user_id) VALUES('fgvdhprtqudf','Java','2015-10-15',6,12,1),('fgvdhprbqudf','Javascript','2014-10-15',7,12,1),('fgndhprtqudf','c++','2017-10-23',4,16,1),('fgvshprtqudf','php','2018-10-15',3,12,1),('lgvdhprtqudf','python','2015-10-15',6,24,1);

INSERT INTO user_roles_table(user_id, role_id) VALUES(1,2);
INSERT INTO user_roles_table(user_id, role_id) VALUES(2,1);

INSERT INTO projects_table(uuid,description,location_link,name,role,user_id)VALUES('qwertyuiopdf','INTRODUCTION TO JAVA','www.learnjava.com','Java','lead developer',1),('qwegtyuiopdf','MOBILE APPLICATION DEVELOPEMNT','www.learnANDROID.com','android','lead developer',1),('qwertyuiopgf','INTRODUCTION TO django','www.learnpython.com','pythno','lead developer',1);
