package by.kozlov.hibernate;

import by.kozlov.entity.Address;
import by.kozlov.entity.Person;
import by.kozlov.util.HibernateUtil;

public class HibernateRunner {
    public static void main(String[] args) {

        //var person = Person.builder().name("Sasha").surname("Ivanov").age(32).address(address).build();
        try(var session = HibernateUtil.getSession()) {

            session.beginTransaction();

            var person1 = Person.builder().name("Olga").surname("Ivanova").age(34)
                    .address(session.get(Address.class,2L)).build();

            session.save(person1);
            session.getTransaction().commit();
        }
    }
}
