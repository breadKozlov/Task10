package by.kozlov.hibernate;

import by.kozlov.entity.Address;
import by.kozlov.entity.Person;
import by.kozlov.util.HibernateUtil;
import lombok.Cleanup;
import org.junit.jupiter.api.Test;

class HibernateRunnerTest {

    @Test
    public void manyToOneAddress() {

        @Cleanup var session = HibernateUtil.getSession();

        session.beginTransaction();
        var address = session.get(Address.class,1L);
        var persons = session.createQuery("FROM Person P WHERE P.address = :address",Person.class)
                .setParameter("address",address).list();
        System.out.println(persons);
        session.getTransaction().commit();
    }

    @Test
    public void addPersonToNewAddress() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var address = Address.builder().city("Moscow").street("Usika").building(23).build();
        var person = Person.builder().name("Nicolai").surname("Petrov").age(23).build();
        var person1 = Person.builder().name("Pavel").surname("Ivanov").age(33).build();

        address.addPerson(person);
        address.addPerson(person1);
        session.save(address);
        session.save(person);
        session.save(person1);

        System.out.println(address.getPersons());
        session.getTransaction().commit();
    }

    @Test
    public void addPersonToOldAddress() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var address = (Address) session.createQuery("FROM Address A WHERE A.city = :name_city",Address.class)
                .setParameter("name_city","Moscow").list().stream().findFirst().orElseThrow();
        var person = Person.builder().name("Sasha").surname("Kovalev").age(12)
                .address(address).build();
        var person1 = Person.builder().name("Olga").surname("Petrova").age(43)
                .address(address).build();
        session.save(person);
        session.save(person1);

        System.out.println(session.createQuery("FROM Person P WHERE P.address.city = :name_city",Person.class)
                .setParameter("name_city",address.getCity()).list());
        session.getTransaction().commit();
    }

    @Test
    public void addNewAddressWithoutPersons() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var address = Address.builder().city("Minsk").street("Nemiga").building(11).build();
        var address1 = Address.builder().city("Grodno").street("Lenina").building(1).build();

        session.save(address);
        session.save(address1);

        System.out.println(session.createQuery("FROM Address",Address.class).list());
        session.getTransaction().commit();

    }

    @Test
    public void oneToManyPersons() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var address = session.get(Address.class,1L);
        System.out.println(address.getPersons());
        session.getTransaction().commit();
    }

    @Test
    public void deletePerson() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var person = (Person)session.createQuery("FROM Person P WHERE P.name = :name",Person.class)
                        .setParameter("name","Pavel").list().stream().findFirst()
                        .orElseThrow();

        session.delete(person);

        System.out.println(session.createQuery("FROM Person").list());
        session.getTransaction().commit();
    }

    @Test
    public void deleteAddressAndAllPersonsIncluded() {

        @Cleanup var session = HibernateUtil.getSession();
        session.beginTransaction();

        var address = session.get(Address.class,1L);
        session.delete(address);

        System.out.println(session.createQuery("FROM Address",Address.class).list());
        session.getTransaction().commit();
    }

}