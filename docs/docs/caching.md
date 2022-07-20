---
title: Caching
---

Caching is **disabled** by default.

A [basic cache](#basic-cache) is provided for simple usages. Any other cache can be plugged in by
implementing the `MessageFormatLoadingCacheProvider` interface, see
[Guava Cache example](#guava-cache).

Be aware of [thread-safety](#thread-safety) when using caching.

## Why use a cache?

`MessageFormat`s can be costly to instantiate. When performance matters, if the same message is
going to be formatted over and over it is better to only instantiate it once.

#### Avoid

```java
class Example {

  {
    for (int i = 0; i < 5; i++) {
      System.out.println(
          new MessageFormat("Welcome {username}!").format(ImmutableMap.of("username", "Bob")));
    }
  }
}

```

#### Prefer

Instantiate the `MessageFormat` outside of the loop and keep re-using it.

```java
class Example {

  {
    MessageFormat messageFormat = new MessageFormat("Welcome {username}!");
    for (int i = 0; i < 5; i++) {
      System.out.println(messageFormat.format(ImmutableMap.of("username", "Bob")));
    }
  }
}

```

#### Multiple messages / different scopes

Maintaining instances for multiple messages and scope can become tedious. `L10nMessages` will solve
this with cache enabled.

```java
class Example {

  {
    ConcurrentHashMap<String, MessageFormat> messagesConcurrentHashMap = new ConcurrentHashMap<>();
    messagesConcurrentHashMap.put("welcome_user", new MessageFormat("Welcome {username}!"));
    messagesConcurrentHashMap.put("bye_user", new MessageFormat("Bye {username}!"));

    scope1:
    {
      for (int i = 0; i < 5; i++) {
        System.out.println(
            messagesConcurrentHashMap.get("welcome_user")
                .format(ImmutableMap.of("username", "Bob")));
      }
    }

    scope2:
    {
      for (int i = 0; i < 5; i++) {
        System.out.println(
            messagesConcurrentHashMap.get("welcome_user")
                .format(ImmutableMap.of("username", "Bob")));
        System.out.println(
            messagesConcurrentHashMap.get("bye_user").format(ImmutableMap.of("username", "Bob")));
      }
    }
  }
}
```

#### Similarly with disabled cache

Following code will re-create the `MessageFormat` in each iteration

```java
class Example {

  {
    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();

    for (int i = 0; i < 5; i++) {
      System.out.println(m.format(welcome_user, "username", "Bob"));
    }
  }
}

```

#### With cache enabled

The `MessageFormat`s will be instantiated only once, and accessing them is transparent.

```java
class Example {

  {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .messageFormatLoadingCacheProvider(CONCURRENT_HASH_MAP)
            .build();

    scope1:
    {
      for (int i = 0; i < 5; i++) {
        System.out.println(m.format(welcome_user, "username", "Bob"));
      }
    }

    scope2:
    {
      for (int i = 0; i < 5; i++) {
        System.out.println(m.format(welcome_user, "username", "Bob"));
        System.out.println(m.format(bye_user, "username", "Bob"));
      }
    }
  }
}

```

## Basic Cache

A basic cache based on a `ConcurrentHashMap` is available.

```java
class Example {

  {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .messageFormatLoadingCacheProvider(CONCURRENT_HASH_MAP)
            .build();
  }
}
```

## Guava Cache

This is an example how to implement the `MessageFormatLoadingCacheProvider` interface with a Guava
Cache. For the cache configuration itself, check more information at
[Guava Caches Explained](https://github.com/google/guava/wiki/CachesExplained)

```java
class Example {

  {
    L10nMessages<Messages> m =
        L10nMessages.builder(Messages.class)
            .messageFormatLoadingCacheProvider(
                (locale, messageFormatProvider) ->
                    CacheBuilder.newBuilder()
                        .maximumSize(10000)
                        .build(
                            CacheLoader.from(
                                (String message) -> messageFormatProvider.get(message, locale)))
                        ::getUnchecked)
            .build();
  }
}
```

## Thread Safety

When using a cache, it is important to understand the thread-safety of the underlying
`MessageFormat` before sharing the `L10nMessages` instance between multiple threads.

Consider having 1 instance of `L10nMessages` per thread, or to have some sort of synchronization
around the MessageFormat instance.

From the ICU4J documentation:

> MessageFormats are not synchronized.
>
> It is recommended to create separate format instances for each thread. If multiple threads access
> a format concurrently, it must be synchronized externally.
