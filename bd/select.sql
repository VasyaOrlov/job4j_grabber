CREATE TABLE company
(
    id integer NOT NULL,
    name character varying,
    CONSTRAINT company_pkey PRIMARY KEY (id)
);

CREATE TABLE person
(
    id integer NOT NULL,
    name character varying,
    company_id integer references company(id),
    CONSTRAINT person_pkey PRIMARY KEY (id)
);

select person.name as name_person, company.name as name_company 
from person 
join company 
on company_id = company.id 
where company_id != 5;

select company.name, count(company.id)
from person 
join company 
on company_id = company.id
group by company.id
having count(company.id) = (select count(company_id)
							from person
							group by company_id
							order by count(company_id) desc
							limit 1);