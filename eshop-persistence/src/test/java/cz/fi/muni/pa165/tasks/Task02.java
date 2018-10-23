package cz.fi.muni.pa165.tasks;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.ConstraintViolationException;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.PersistenceSampleApplicationContext;
import cz.fi.muni.pa165.entity.Category;
import cz.fi.muni.pa165.entity.Product;

 
@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
public class Task02 extends AbstractTestNGSpringContextTests {

	@PersistenceUnit
	private EntityManagerFactory emf;

	//Create instance variables to be filled
	private Category catKitchen = new Category();
	private Category catElectro = new Category();
	private Product flashlight = new Product();
	private Product kitchenRobot = new Product();
	private Product plate = new Product();

	@BeforeClass
	public void createTestData() {
		//Create and place products into appropriate categories
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		catElectro.setName("Electro");
		em.persist(catElectro);

		catKitchen.setName("Kitchen");
		em.persist(catKitchen);

		flashlight.setName("Flashlight");
		flashlight.addCategory(catElectro);
		em.persist(flashlight);

		kitchenRobot.setName("Kitchen Robot");
		kitchenRobot.addCategory(catElectro);
		kitchenRobot.addCategory(catKitchen);
		em.persist(kitchenRobot);

		plate.setName("Plate");
		plate.addCategory(catKitchen);
		em.persist(plate);

		em.getTransaction().commit();
		em.close();
	}

	//5 unit tests, everything belongs where it should
	@Test
	public void testFlashlight(){
		EntityManager em = emf.createEntityManager();

		Product Needle = em.find(Product.class, flashlight.getId());
		Assert.assertEquals(Needle.getCategories().size(), 1);
		Assert.assertEquals(Needle.getCategories().iterator().next().getName(), "Electro");

		em.close();
	}

	@Test
	public void testPlate(){
		EntityManager em = emf.createEntityManager();

		Product Needle = em.find(Product.class, plate.getId());
		Assert.assertEquals(Needle.getCategories().size(), 1);
		Assert.assertEquals(Needle.getCategories().iterator().next().getName(), "Kitchen");

		em.close();
	}


	@Test
	public void testKitchenRobot(){
		EntityManager em = emf.createEntityManager();

		Product Needle = em.find(Product.class, kitchenRobot.getId());
		Assert.assertEquals(Needle.getCategories().size(), 2);
		assertContainsCategoryWithName(Needle.getCategories(),"Electro");
		assertContainsCategoryWithName(Needle.getCategories(),"Kitchen");

		em.close();
	}



	@Test
	public void testCategoryKitchen(){
		EntityManager em = emf.createEntityManager();

		Category Needle = em.find(Category.class, catKitchen.getId());
		Assert.assertEquals(Needle.getProducts().size(), 2);
		assertContainsProductWithName(Needle.getProducts(), "Kitchen Robot");
		assertContainsProductWithName(Needle.getProducts(), "Plate");

		em.close();
	}

	@Test
	public void testCategoryElectro(){
		EntityManager em = emf.createEntityManager();

		Category Needle = em.find(Category.class, catElectro.getId());
		Assert.assertEquals(Needle.getProducts().size(), 2);
		assertContainsProductWithName(Needle.getProducts(), "Flashlight");
		assertContainsProductWithName(Needle.getProducts(), "Kitchen Robot");

		em.close();
	}

	//Test jestli je to null
	@Test(expectedExceptions=ConstraintViolationException.class)
	public void testDoesntSaveNullName(){
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Product p = new Product();
		em.persist(p);
		em.getTransaction().commit();
		em.close();
	}


	private void assertContainsCategoryWithName(Set<Category> categories,
			String expectedCategoryName) {
		for(Category cat: categories){
			if (cat.getName().equals(expectedCategoryName))
				return;
		}
			
		Assert.fail("Couldn't find category "+ expectedCategoryName+ " in collection "+categories);
	}
	private void assertContainsProductWithName(Set<Product> products,
			String expectedProductName) {
		
		for(Product prod: products){
			if (prod.getName().equals(expectedProductName))
				return;
		}
			
		Assert.fail("Couldn't find product "+ expectedProductName+ " in collection "+products);
	}

	
}
