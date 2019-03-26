package java8.ex04;

import java8.data.Account;
import java8.data.Data;
import java8.data.Person;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Exercice 04 - FuncCollection
 */
public class Lambda_04_Test {

	// tag::interfaces[]
	interface GenericPredicate<T> {

		boolean test(T t);

	}

	interface GenericMapper<T, E> {
		
		E map(T e);
		
	}

	interface Processor<T> {
		
		void process(T t);
		
	}
	// end::interfaces[]

	// tag::FuncCollection[]
	class FuncCollection<T> {

		private Collection<T> list = new ArrayList<>();

		public void add(T t) {
			list.add(t);
		}

		public void addAll(Collection<T> all) {
			for (T el : all) {
				list.add(el);
			}
		}
		// end::FuncCollection[]

		// tag::methods[]
		private FuncCollection<T> filter(GenericPredicate<T> predicate) {
			FuncCollection<T> result = new FuncCollection<>();

			for (T r : result.list) {

				if(predicate.test(r)){
					result.add(r);
				}
				
			}

			return result;
		}


		private <E> FuncCollection<E> map(GenericMapper<T, E> mapper) {
			FuncCollection<T> result = new FuncCollection<>();
			
			FuncCollection<E> list = new FuncCollection<>();

			for (T e : result.list) {
				list.add((E) mapper.map(e));
			}

			return list;
		}

		private void forEach(Processor<T> processor) {
			FuncCollection<T> result = new FuncCollection<>();
			for (T e : result.list) {
				processor.process((T) e);
			}
		}
		// end::methods[]

	}

	// tag::test_filter_map_forEach[]
	@Test
	public void test_filter_map_forEach() throws Exception {

		List<Person> personList = Data.buildPersonList(100);
		FuncCollection<Person> personFuncCollection = new FuncCollection<>();
		personFuncCollection.addAll(personList);

		personFuncCollection
				// TODO filtrer, ne garder uniquement que les personnes ayant un
				// age > 50
				.filter(t -> t.getAge() > 50)
				// TODO transformer la liste de personnes en liste de comptes.
				// Un compte a par défaut un solde à 1000.
				.map(t -> {
					Account a = new Account();
					a.setOwner((Person) t);
					a.setBalance(1000);
					return a;
				})
				// TODO vérifier que chaque compte a un solde à 1000.
				// TODO vérifier que chaque titulaire de compte a un age > 50
				.forEach(t -> {
					assertTrue(t.getBalance() == 1000);
					assertTrue(t.getOwner().getAge() > 50);
				});
	}
	// end::test_filter_map_forEach[]

	// tag::test_filter_map_forEach_with_vars[]
	@Test
	public void test_filter_map_forEach_with_vars() throws Exception {

		List<Person> personList = Data.buildPersonList(100);
		FuncCollection<Person> personFuncCollection = new FuncCollection<>();
		personFuncCollection.addAll(personList);

		// TODO créer un variable filterByAge de type GenericPredicate
		// TODO filtrer, ne garder uniquement que les personnes ayant un age >
		// 50
		GenericPredicate<Person> filterByAge = t -> t.getAge() > 50;

		// TODO créer un variable mapToAccount de type GenericMapper
		// TODO transformer la liste de personnes en liste de comptes. Un compte
		// a par défaut un solde à 1000.
		GenericMapper<Person, Account> mapToAccount = t -> {
			Account a = new Account();
			a.setOwner((Person) t);
			a.setBalance(1000);
			return a;
		};

		// TODO créer un variable verifyAccount de type GenericMapper
		// TODO vérifier que chaque compte a un solde à 1000.
		// TODO vérifier que chaque titulaire de compte a un age > 50
		Processor<Account> verifyAccount = t -> {
			assertTrue(t.getBalance() == 1000);
			assertTrue(t.getOwner().getAge() > 50);
		};

		/*
		 * TODO Décommenter personFuncCollection .filter(filterByAge)
		 * .map(mapToAccount) .forEach(verifyAccount);
		 */
		personFuncCollection .filter(filterByAge)
			.map(mapToAccount) .forEach(verifyAccount);
	}
	// end::test_filter_map_forEach_with_vars[]

}
