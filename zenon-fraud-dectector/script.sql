use zenon;

create table transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    step integer not null,
    type varchar(20) not null,
    amount decimal(12, 2) not null,
    isFraud boolean default 0,
    isFlaggedFraud boolean default 0,
    nameOrigin varchar(20) not null,
    originOldAmount decimal(12, 2) not null,
    originNewAmount decimal(12, 2) not null,
    nameDestination varchar(20) not null,
    destinationOldAmount decimal(12, 2) not null,
    destinationNewAmount decimal(12, 2) not null
);

--1,PAYMENT,9839.64,C1231006815,170136.0,160296.36,M1979787155,0.0,0.0,0,0
INSERT INTO transaction
(step, type, amount, isFraud, isFlaggedFraud,
 nameOrigin, originOldAmount, originNewAmount,
 nameDestination, destinationOldAmount, destinationNewAmount)
VALUES
    (1,"PAYMENT",9839.64,0,0,
     "C1231006815",170136.0,160296.36,
     "M1979787155",0.0,0.0);

select * from transaction;
delete from transaction;


