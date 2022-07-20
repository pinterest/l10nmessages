package com.pinterest.l10nmessages;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import com.pinterest.l10nmessages.Predicates.OnDuplicate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PredicatesTest {

  @Test
  void distinctByKey() {
    Predicate<String> predicate = Predicates.distinctByKey(s -> s);
    assertThat(predicate.test("a")).isTrue();
    assertThat(predicate.test("b")).isTrue();
    assertThat(predicate.test("a")).isFalse();
  }

  @Test
  void distinctByKeyWithOnDuplicate() {
    OnDuplicate onDuplicateMock = Mockito.mock(OnDuplicate.class);

    AtomicInteger atomicInteger = new AtomicInteger();
    Predicate<String> predicate =
        Predicates.distinctByKey(s -> s, s -> atomicInteger.getAndIncrement(), onDuplicateMock);

    assertThat(predicate.test("a")).isTrue();
    assertThat(predicate.test("b")).isTrue();
    assertThat(predicate.test("a")).isFalse();
    verify(onDuplicateMock).apply("a", 2, 0);
  }
}
