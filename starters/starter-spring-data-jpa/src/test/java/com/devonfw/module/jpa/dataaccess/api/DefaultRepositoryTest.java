package com.devonfw.module.jpa.dataaccess.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.devonfw.example.TestApplication;
import com.devonfw.example.component.common.api.to.FooSearchCriteriaTo;
import com.devonfw.example.component.dataaccess.api.FooEntity;
import com.devonfw.example.component.dataaccess.api.FooRepository;
import com.devonfw.module.basic.common.api.query.ComparableSearchConfigTo;
import com.devonfw.module.basic.common.api.query.ComparableSearchOperator;
import com.devonfw.module.basic.common.api.query.LikePatternSyntax;
import com.devonfw.module.basic.common.api.query.StringSearchConfigTo;
import com.devonfw.module.basic.common.api.query.StringSearchOperator;
import com.devonfw.module.test.common.base.ComponentTest;

/**
 * Test of {@link FooRepository} in order to test the following infrastructure:
 * <ul>
 * <li>{@link com.devonfw.module.jpa.dataaccess.api.data.DefaultRepository}</li>
 * <li>{@link com.devonfw.module.jpa.dataaccess.api.data.GenericRepository}</li>
 * <li>{@link com.devonfw.module.jpa.dataaccess.impl.data.GenericRepositoryFactoryBean}</li>
 * <li>{@link com.devonfw.module.jpa.dataaccess.impl.data.GenericRepositoryImpl}</li>
 * </ul>
 */
@SpringBootTest(classes = { TestApplication.class }, webEnvironment = WebEnvironment.NONE)
public class DefaultRepositoryTest extends ComponentTest {

  @Inject
  private FooRepository fooRepository;

  @PersistenceContext
  private EntityManager entityManager;

  @Inject
  private TransactionTemplate template;

  private static final Date EXAMPLE_DATE_1 = new Date(1640991600000L); // 1st Jan 2022 00:00

  private static final Date EXAMPLE_DATE_2 = new Date(1641078000000L); // 2nd Jan 2022 00:00

  private static final Date EXAMPLE_DATE_3 = new Date(1641164400000L); // 3rd Jan 2022 00:00

  private <T> T doInTx(TransactionCallback<T> lambda) {

    return this.template.execute(lambda);
  }

  /**
   * Test of
   * {@link com.devonfw.module.jpa.dataaccess.api.data.GenericRepository#forceIncrementModificationCounter(Object)}.
   * Ensures that the modification counter is updated after the call of that method when the transaction is closed.
   */
  @Test
  public void testForceIncrementModificationCounter() {

    // given
    FooEntity entity = doInTx((x) -> newFoo("Foo"));

    // when
    FooEntity entity2 = doInTx((x) -> {
      FooEntity foo = this.fooRepository.find(entity.getId());
      this.fooRepository.forceIncrementModificationCounter(foo);
      return foo;
    });

    // then
    assertThat(entity.getModificationCounter()).isEqualTo(0);
    assertThat(entity2.getModificationCounter()).isEqualTo(1);
  }

  private FooEntity newFoo(String message) {

    return newFoo(message, message, 1, EXAMPLE_DATE_1);
  }

  private FooEntity newFoo(String message, String name) {

    return newFoo(message, name, 1, EXAMPLE_DATE_1);

  }

  private FooEntity newFoo(String message, String name, int count, Date createdAt) {

    FooEntity entity = new FooEntity();
    entity.setMessage(message);
    entity.setName(name);
    entity.setCount(count);
    entity.setCreatedAt(createdAt);
    this.fooRepository.save(entity);
    assertThat(entity.getId()).isNotNull();
    assertThat(entity.getModificationCounter()).isEqualTo(0);
    assertThat(this.fooRepository.find(entity.getId())).isSameAs(entity);
    return entity;
  }

  /**
   * Test of
   * {@link com.devonfw.module.jpa.dataaccess.api.data.GenericRepository#forceIncrementModificationCounter(Object)}.
   * Ensures that the modification counter is updated after the call of that method when the transaction is closed.
   */
  @Test
  @Transactional
  public void testFindByMessage() {

    this.fooRepository.deleteAll();

    // given
    int hitsPerPage = 5;
    int totalPages = 3;
    int pageNumber = totalPages - 1;
    String message = "Foo";
    int totalElements = hitsPerPage * totalPages - 1;
    for (int i = 0; i < totalElements; i++) {
      newFoo(message, createName(i));
    }
    Sort sort = JpaSort.unsafe(Direction.ASC, "name");
    Pageable pageable = PageRequest.of(pageNumber, hitsPerPage, sort);

    // when
    Page<FooEntity> page = this.fooRepository.findByMessage(message, pageable);

    // then
    assertThat(page.getNumber()).isEqualTo(pageNumber);
    assertThat(page.getNumberOfElements()).isEqualTo(hitsPerPage - 1);
    assertThat(page.getSize()).isEqualTo(hitsPerPage);
    assertThat(page.getTotalPages()).isEqualTo(totalPages);
    assertThat(page.getTotalElements()).isEqualTo(totalElements);
    assertThat(page.getContent()).hasSize(page.getNumberOfElements());
    int i = pageNumber * hitsPerPage;
    for (FooEntity foo : page.getContent()) {
      assertThat(foo.getName()).isEqualTo(createName(i));
      i++;
    }
  }

  private String createName(int i) {

    return "Name" + String.format("%03d", i);
  }

  /**
   * Test of
   * {@link com.devonfw.module.jpa.dataaccess.api.data.GenericRepository#forceIncrementModificationCounter(Object)}.
   * Ensures that the modification counter is updated after the call of that method when the transaction is closed.
   */
  @Test
  @Transactional
  public void testFindByCriteria() {

    this.fooRepository.deleteAll();

    // given
    FooSearchCriteriaTo criteria = new FooSearchCriteriaTo();
    criteria.setMessage("T?st");
    StringSearchConfigTo config = StringSearchConfigTo.of(LikePatternSyntax.GLOB);
    config.setIgnoreCase(true);
    config.setMatchSubstring(true);
    criteria.setMessageOption(config);
    PageRequest pageable = PageRequest.of(0, 100, Sort.by(Direction.DESC, "message"));
    criteria.setPageable(pageable);
    List<String> values = new ArrayList<>(Arrays.asList("MY_TEST", "Sometest", "Test", "Testing", "Xtest"));
    Collections.shuffle(values);
    for (String message : values) {
      newFoo(message);
    }
    newFoo("Aaa");
    newFoo("Xxx");

    // when
    Page<FooEntity> hits = this.fooRepository.findByCriteria(criteria);

    // then
    Collections.sort(values, (x, y) -> -x.compareTo(y));
    assertThat(hits.getContent().stream().map(x -> x.getMessage()).collect(Collectors.toList()))
        .containsExactlyElementsOf(values);

    // and when
    config.setOperator(StringSearchOperator.NOT_LIKE);
    hits = this.fooRepository.findByCriteria(criteria);

    // then
    assertThat(hits.getContent().stream().map(x -> x.getMessage()).collect(Collectors.toList())).containsExactly("Xxx",
        "Aaa");

    // and when
    config.setOperator(StringSearchOperator.LIKE);
    config.setIgnoreCase(false);
    hits = this.fooRepository.findByCriteria(criteria);

    // then
    assertThat(hits.getContent().stream().map(x -> x.getMessage()).collect(Collectors.toList()))
        .containsExactly("Testing", "Test");

    // and when
    config.setMatchSubstring(false);
    hits = this.fooRepository.findByCriteria(criteria);

    // then
    assertThat(hits.getContent().stream().map(x -> x.getMessage()).collect(Collectors.toList()))
        .containsExactly("Test");

    // and when
    criteria.setMessage("Test");
    config.setLikeSyntax(null);
    config.setOperator(StringSearchOperator.NE);
    pageable = PageRequest.of(0, 100, Sort.by(Direction.ASC, "message"));
    criteria.setPageable(pageable);
    hits = this.fooRepository.findByCriteria(criteria);

    // then
    assertThat(hits.getContent().stream().map(x -> x.getMessage()).collect(Collectors.toList())).containsExactly("Aaa",
        "MY_TEST", "Sometest", "Testing", "Xtest", "Xxx");
  }

  /**
   * Tests the extension of SearchConfig options for Comparable types
   */
  @Test
  @Transactional
  public void testFindByCriteriaComparableExtension() {

    this.fooRepository.deleteAll();

    // given
    FooSearchCriteriaTo criteria = new FooSearchCriteriaTo();
    criteria.setCreatedAt(EXAMPLE_DATE_2);
    criteria.setCreatedAtOption(ComparableSearchConfigTo.of(ComparableSearchOperator.LE));

    PageRequest pageable = PageRequest.of(0, 100, Sort.by(Direction.DESC, "message"));
    criteria.setPageable(pageable);

    List<Date> values = new ArrayList<>(Arrays.asList(EXAMPLE_DATE_1, EXAMPLE_DATE_2, EXAMPLE_DATE_3));
    Collections.shuffle(values);
    for (Date date : values) {
      newFoo("Foo", "Bar", 1, date);
    }

    // when
    Page<FooEntity> hits = this.fooRepository.findByCriteria(criteria);
    List<Date> dateHits = hits.get().map(x -> x.getCreatedAt()).collect(Collectors.toList());

    // then
    assertThat(dateHits).contains(EXAMPLE_DATE_1, EXAMPLE_DATE_2);
    assertThat(dateHits).doesNotContain(EXAMPLE_DATE_3);

  }

  /**
   * Tests the extension of SearchConfig options for Number types
   */
  @Test
  @Transactional
  public void testFindByCriteriaNumberExtension() {

    this.fooRepository.deleteAll();

    // given
    FooSearchCriteriaTo criteria = new FooSearchCriteriaTo();
    criteria.setCount(3);
    criteria.setCountOption(ComparableSearchConfigTo.of(ComparableSearchOperator.GT));

    PageRequest pageable = PageRequest.of(0, 100, Sort.by(Direction.DESC, "message"));
    criteria.setPageable(pageable);

    List<Integer> values = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 10));
    Collections.shuffle(values);
    for (Integer num : values) {
      newFoo("Foo", "Bar", num, EXAMPLE_DATE_1);
    }

    // when
    Page<FooEntity> hits = this.fooRepository.findByCriteria(criteria);
    List<Integer> countHits = hits.get().map(x -> x.getCount()).collect(Collectors.toList());

    // then
    assertThat(countHits).contains(4, 5, 10);
    assertThat(countHits).doesNotContain(1, 2, 3);

  }

};
