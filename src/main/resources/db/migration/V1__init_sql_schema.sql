--init hotels table + alters
drop table if exists hotels;
create table hotels(
  hotel_id    bigint primary key auto_increment not null,
  hotel_name    varchar(50) not null,
  stars   int not null,
  email    varchar(50) not null,
  earliest_possible_check_in    time not null,
  latest_possible_check_in    time not null,
  possible_check_out    time not null,
  latest_possible_check_out   time not null,
  check_out_fee   decimal not null
  );
 --alters from embedded adress entity
alter table hotels add column name varchar(50);
alter table hotels add column street_adress_1 varchar(50) not null;
alter table hotels add column street_adress_2 varchar(50);
alter table hotels add column country varchar(25) check (country in
                         ('Belgium', 'Austria', 'Bulgaria', 'Croatia', 'Cyprus',
                            'Czech', 'Denmark', 'Estonia',
                         'Finland', 'France', 'Germany', 'Greece',
                         'Hungary', 'Ireland', 'Italy', 'Lativia',
                         'Lithuania', 'Luxembourg', 'Malta',
                         'Netherlands', 'Poland', 'Portugal', 'Romania', 'Slovakia',
                         'Slovenia', 'Spain', 'Sweden', 'UK', 'Israel', 'Serbia'));
alter table hotels add column city varchar(50) not null;
alter table hotels add column postcode varchar(5) not null;

--init reservations table
drop table if exists reservations;
create table reservations(
  id    int primary key auto_increment not null,
  uuid    uuid,
  created_at    datetime
  );
--alters from embedded reservation dates entity & policy & succesful payment id
alter table reservations add column check_in_date date not null;
alter table reservations add column check_out_date date not null;
alter table reservations add column possible_check_in_time time not null;
alter table reservations add column delayed_check_out_time bit not null default false;
alter table reservations add column policy bit not null default false;
alter table reservations add column successful_payment_id int;

--init rooms table
drop table if exists rooms;
create table rooms(
  id    int primary key auto_increment not null,
  room_number   varchar(10) not null,
  room_type   varchar(25) check (room_type in ('Penthouse', 'Balcony', 'Studio', 'Standard', 'Apartament', 'Loft')),
  beds    int not null,
  price_per_night   decimal not null,           unique(room_number)
);
--alters from room entity
alter table rooms add column hotel_hotel_id bigint null;
alter table rooms add foreign key(hotel_hotel_id) references hotels(hotel_id);
alter table rooms add column reservation_id int null;
alter table rooms add foreign key(reservation_id) references reservations(id);

--reservations table extension for room identification.
alter table reservations add column room int null;
alter table reservations add foreign key(room) references rooms(id);

--init guests table
drop table if exists guests;
create table guests(
  id    int primary key auto_increment not null,
  first_name        varchar(50) not null,
  last_name     varchar(50) not null,
  underaged     bit
);

--init reservation guests table
drop table if exists reservation_guests;
create table reservation_guests(
  reservation_id    int,
  guest_id    int
);

--init reservation general extras
drop table if exists reservation_general_extras;
create table reservation_general_extras(
  reservation_id    int,
  general_extra_id    int
);

--init extras table
drop table if exists extras;
create table extras(
  id    int primary key auto_increment not null,
  description   varchar(250) not null,
  per_day_price   decimal not null,
  type    varchar(25) check (type in('Basic', 'Premium', 'Luxury')),
  category    varchar(25) check (category in ('General', 'Food'))
);

--init meal plans table
drop table if exists meal_plans;
create table meal_plans(
  id    int primary key auto_increment not null,
  meal_plan_id      uuid,
  diet_preference_list varchar(250)
);
--alters with foreign keys
alter table meal_plans add column guest_id int null;
alter table meal_plans add foreign key (guest_id) references guests (id);
alter table meal_plans add column reservation_id int references reservations (id);
alter table meal_plans add foreign key (reservation_id) references reservations (id);

--init meal plans food extras
drop table if exists meal_plans_food_extras;
create table meal_plans_food_extras(
    meal_plan_id        int,
    food_extras_id      int
);

--init meal plan diet preference list
drop table if exists meal_plan_diet_preference_list;
create table meal_plan_diet_preference_list(
    meal_plan_id        int,
    diet_preference_list        int
);

--init succesfull payment table
drop table if exists successful_payment;
create table successful_payment(
  id    int primary key auto_increment not null,
  transaction_id    uuid not null,
  credit_card_type      varchar(25) check (credit_card_type in('Visa', 'MasterCard')),
  last_four_numbers_of_credit_card      varchar(4) not null,
  cvv_number        varchar(3) not null,
  card_expiration_date      date not null
);

--init reservations meal plan list table
drop table if exists reservations_meal_plan_list;
create table reservations_meal_plan_list(
  reservation_id    int,
  meal_plan_list_id   int
);

