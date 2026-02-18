package org.hibernate.bugs;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.hibernate.cfg.AvailableSettings;

import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.ServiceRegistry;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.hibernate.testing.orm.junit.Setting;
import org.junit.jupiter.api.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM,
 * using its built-in unit test framework. Although ORMStandaloneTestCase is
 * perfectly acceptable as a reproducer, usage of this class is much preferred.
 * Since we nearly always include a regression test with bug fixes, providing
 * your reproducer using this method simplifies the process.
 * <p>
 * What's even better? Fork hibernate-orm itself, add your test case directly to
 * a module's unit tests, then submit it as a PR!
 */
@DomainModel(annotatedClasses = { Article.class })
@ServiceRegistry(

		settings = {
				// For your own convenience to see generated queries:
				@Setting(name = AvailableSettings.SHOW_SQL, value = "true"),
				@Setting(name = AvailableSettings.FORMAT_SQL, value = "true"),

		})
@SessionFactory
class ORMUnitTestCase {

	/**
	 * Having the same column used with two different aliases does not work
	 * as expected in Hibernate 6/7. The array of aliases only contains the last
	 * alias for the column (but twice). Using an AliasToBeanTransformer would result in null values.
	 */
	@Test
	void hhh20159Test(SessionFactoryScope scope) throws Exception {
		scope.inTransaction(session -> {
			// insert some article
			Article article = new Article();
			article.setId(1L);
			article.setName("Foobar");
			article.setPrice(new BigDecimal(42));
			session.persist(article);

			// the problem
			String sql = "SELECT a.price as price, a.price as prevPrice FROM Article a";
			session.createQuery(sql,Article.class).setTupleTransformer((tuple, aliases) -> {
				List<String> list = Arrays.asList(aliases);
				assertTrue("No alias for 'price'", list.contains("price"));
				assertTrue("No alias for 'prevPrice'", list.contains("prevPrice"));
				return null;
			}).getResultList();
		});
	}
}
